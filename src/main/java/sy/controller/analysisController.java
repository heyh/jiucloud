package sy.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sy.model.po.Price;
import sy.model.po.Project;
import sy.pageModel.AnalysisData;
import sy.pageModel.AnalysisSearch;
import sy.pageModel.FieldData;
import sy.pageModel.SessionInfo;
import sy.service.AnalysisServiceI;
import sy.service.CostServiceI;
import sy.service.PriceServiceI;
import sy.service.ProjectServiceI;
import sy.service.TaskServiceI;
import sy.util.ConfigUtil;
import sy.util.ExcelExportUtil;
import sy.util.UtilDate;

@Controller
@RequestMapping("/analysisController")
public class analysisController extends BaseController {

	@Autowired
	private ProjectServiceI projectService;

	@Autowired
	private CostServiceI costService;

	@Autowired
	private TaskServiceI taskService;

	@Autowired
	private AnalysisServiceI analysisService;

	@Autowired
	private PriceServiceI priceService;

	/*
	 * 跳转到费用类型选择
	 */

	@RequestMapping("/securi_selectc")
	public String selectc() {
		return "/app/analysis/cost_select";
	}

	/*
	 * 跳转到汇总页
	 */
	@RequestMapping("/showTable")
	public String showTable(HttpSession session, HttpServletRequest request,
			AnalysisSearch analysisSearch) {
//		if (analysisSearch.isNull()) {
//			request.setAttribute("first", UtilDate.getshortFirst());
//			request.setAttribute("last", UtilDate.getshortLast());
//			return "/app/analysis/summary";// 当日期为空时不做任何查询
//		}
		FieldData fieldData = (FieldData) session.getAttribute("analusisInfo");
		analysisSearch.setStartTime(fieldData.getStartTime());
		analysisSearch.setEndTime(fieldData.getEndTime());
		//名字和id
		analysisSearch.setpName(fieldData.getProjectName());
		analysisSearch.setCostTypeName(fieldData.getCostType());
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		List<Integer> ugroup = sessionInfo.getUgroup();

		List<Price> prices = priceService.getpPrices(Integer.parseInt(cid));
		List<Project> projects;

		if (analysisSearch.getpName() == null
				|| "".equals(analysisSearch.getpName())) {
			projects = projectService.getProjects(ugroup);// 当查询条件为空时查询出当前登录权限下的所有工程
//		if (analysisSearch.getProject_id() == null
//				|| "".equals(analysisSearch.getProject_id())) {
//			projects = projectService.getProjects(ugroup);// 当查询条件为空时查询出当前登录权限下的所有工程
		} else {
//			Project project = projectService.findOneView(Integer
//					.parseInt(analysisSearch.getProject_id()));
//			projects = new ArrayList<Project>();
//			projects.add(project);
			//原来是通过id查询单个对象放入集合，现在是通过现场数据的名称模糊查询集合对象赋值给projects集合对象
			projects = projectService.findListView(analysisSearch.getpName(),cid);
		}

		// 获取表格数据
		List<AnalysisData> datas = analysisService.getTable(analysisSearch,
				ugroup, projects, prices,cid);

		List<Double> totals = new ArrayList<Double>();

		// 计算合计
		if (datas.size() > 0) {
			double[] sums = new double[datas.get(0).getMoneys().size()];
			for (int i = 0; i < datas.get(0).getMoneys().size(); i++) {
				sums[i] = 0;
				for (int j = 0; j < datas.size(); j++) {
					sums[i] += datas.get(j).getMoneys().get(i);
				}
				totals.add(sums[i]);
			}
		}

		request.setAttribute("prices", prices);
		request.setAttribute("datas", datas);
		request.setAttribute("analysisSearch", analysisSearch);
		request.setAttribute("totals", totals);
		return "/app/analysis/summary";
	}

	/* 跳转到详情页 */
	@RequestMapping("/showDetail")
	public String showDetail(HttpServletRequest request,HttpSession session) {

//		FieldData fieldData = (FieldData) session.getAttribute("analusisInfo");
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		List<Integer> ugroup = sessionInfo.getUgroup();
		String cid = sessionInfo.getCompid();
		String project_id = request.getParameter("projectName");// 项目名称
		String price_id = request.getParameter("price_id");// 费用分类id
		String datestr = request.getParameter("date");// 日期
		String datestr2 = request.getParameter("date2");// 日期
//		String datestr = fieldData.getStartTime();// 日期
//		String datestr2 = fieldData.getEndTime();// 日期
//		String project_id = fieldData.getProjectName();
//		String price_id = fieldData.getCostType();

		List<Price> prices = priceService.getpPrices(Integer.parseInt(cid));
		List<Project> projects = projectService.getProjects(ugroup);

		if (!(project_id == null || price_id == null || project_id.equals("") || price_id
				.equals(""))) {
			Project project = projectService.findOneView(Integer
					.parseInt(project_id));
			Price price = priceService.findoneview(Integer.parseInt(price_id));
			List<AnalysisData> analysisDatas = analysisService.getList(datestr,
					datestr2, Integer.parseInt(price_id),
					Integer.parseInt(project_id), ugroup);// 获取明细数据
//		if (!(project_id == null || price_id == null || "".equals(project_id) || "".equals(price_id))) {
//			List<Project> project = projectService.findListView(project_id,cid);
//			List<Price> price = priceService.findListview(price_id,cid);
//			List<AnalysisData> analysisDatas = analysisService.getList(datestr,
//					datestr2, price_id,project_id, ugroup,cid);// 获取明细数据

			System.out.println(analysisDatas);
			
			// 计算合计
			double total = 0;
			for (AnalysisData tem : analysisDatas) {
				total += tem.getMoney();
			}

			request.setAttribute("project", project);
			request.setAttribute("price", price);
			request.setAttribute("datestr", datestr);
			request.setAttribute("datestr2", datestr2);
			request.setAttribute("analysisDatas", analysisDatas);
			request.setAttribute("total", total);
		}

		request.setAttribute("project_id", project_id);
		request.setAttribute("projects", projects);
		request.setAttribute("prices", prices);
		request.setAttribute("first", UtilDate.getshortFirst());
		request.setAttribute("last", UtilDate.getshortLast());

		return "/app/analysis/detail";
	}

	/* 后台导出到excel */
	@RequestMapping("/securi_execl")
	public void OutputToExcel(HttpServletResponse response,
			HttpServletRequest request, AnalysisSearch analysisSearch) {
		response.setContentType("text/html;charset=utf8");
		HttpSession session = request.getSession();
//		if (analysisSearch.isNull()) {
//			return;
//		}
		FieldData fieldData = (FieldData) session.getAttribute("analusisInfo");
		analysisSearch.setStartTime(fieldData.getStartTime());
		analysisSearch.setEndTime(fieldData.getEndTime());
		//名字和id
		analysisSearch.setpName(fieldData.getProjectName());
		analysisSearch.setCostTypeName(fieldData.getCostType());
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		List<Integer> ugroup = sessionInfo.getUgroup();

		List<Price> prices = priceService.getpPrices(Integer.parseInt(cid));
		List<Project> projects;

		if (analysisSearch.getpName() == null
				|| "".equals(analysisSearch.getpName())) {
			projects = projectService.getProjects(ugroup);// 当查询条件为空时查询出当前登录权限下的所有工程
//		if (analysisSearch.getProject_id() == null
//				|| "".equals(analysisSearch.getProject_id())) {
//			projects = projectService.getProjects(ugroup);// 当查询条件为空时查询出当前登录权限下的所有工程
		} else {
//			Project project = projectService.findOneView(Integer
//					.parseInt(analysisSearch.getProject_id()));
//			projects = new ArrayList<Project>();
//			projects.add(project);
			//原来是通过id查询单个对象放入集合，现在是通过现场数据的名称模糊查询集合对象赋值给projects集合对象
			projects = projectService.findListView(analysisSearch.getpName(),cid);
		}

		List<AnalysisData> datas = analysisService.getTable(analysisSearch,
				ugroup, projects, prices,cid);
		// 获取数据的方式和getTable方法类似

		List<Map<String, Object>> map = createExcelRecord(datas);

		// 填充projects数据
		String columnNames[] = new String[1 + prices.size()];// 列名
		String keys[] = new String[1 + prices.size()];// map中的key
		columnNames[0] = "项目名称";// 保留第一行为标题
		keys[0] = "project_name";

		// 为execl表格填充数据
		for (int i = 0; i < prices.size(); i++) {
			columnNames[i + 1] = prices.get(i).getName();
			keys[i + 1] = String.valueOf(i);
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			ExcelExportUtil.createWorkBook(map, keys, columnNames).write(os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置编码格式
			response.setHeader("Content-Disposition",
					"attachment;filename=data.xls");
			ServletOutputStream out;
			out = response.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("文件生成...");
	}

	private List<Map<String, Object>> createExcelRecord(List<AnalysisData> list) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "data.xls");
		listmap.add(map);
		double[] totals = new double[list.get(0).getMoneys().size()];
		for (int j = 0; j < list.size(); j++) {
			AnalysisData tem = list.get(j);
			Map<String, Object> mapValue = new HashMap<String, Object>();
			mapValue.put("project_name", tem.getProject_name());
			for (int k = 0; k < tem.getMoneys().size(); k++) {
				mapValue.put(String.valueOf(k), tem.getMoneys().get(k));
				totals[k] += tem.getMoneys().get(k);
			}
			listmap.add(mapValue);
		}
		Map<String, Object> mapValue = new HashMap<String, Object>();
		mapValue.put("project_name", "合计");
		for (int l = 0; l < totals.length; l++) {
			mapValue.put(String.valueOf(l), totals[l]);
		}
		listmap.add(mapValue);
		return listmap;
	}
}
