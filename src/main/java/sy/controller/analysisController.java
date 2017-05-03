package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.S_department;
import sy.model.po.Cost;
import sy.model.po.Price;
import sy.model.po.Project;
import sy.pageModel.*;
import sy.service.*;
import sy.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

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

	@Autowired
	private FieldDataServiceI fieldDataService;

	@Autowired
	private DepartmentServiceI departmentService;

	/*
	 * 跳转到费用类型选择
	 */

	@RequestMapping("/securi_selectc")
	public String selectc() {
		return "/app/analysis/cost_select";
	}

    /**
     * 新的『项目费用汇总』
     * @param session
     * @param request
     * @param analysisSearch
     * @return
     */
    @RequestMapping("/showTable")
    public String showTableNew(HttpSession session, HttpServletRequest request,
                               AnalysisSearch analysisSearch) {
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
        List<Object[]> allFee = analysisService.getAllFee(cid, ugroup);
        Set<Object> productIds = new HashSet<Object>();
        Set<String> prices = new HashSet<String>();
        if (allFee != null && allFee.size() > 0) {
            for (Object[] fee : allFee) {
                productIds.add(fee[0]);
            }
        }
        List<AnalysisData> datas = new ArrayList<AnalysisData>();
        AnalysisData tem = new AnalysisData();
        List<Double> moneys = new ArrayList<Double>();
        List<String> pricenames = new ArrayList<String>();
        for (Object projectId : productIds) {
            tem = new AnalysisData();
            moneys = new ArrayList<Double>();
            pricenames = new ArrayList<String>();
            for (Object[] fee : allFee) {
                if (fee[0].equals(projectId)) {
                    tem.setProject_name(fee[1]== null ? "" : String.valueOf(fee[1]));
                    pricenames.add(fee[3]== null ? "" : String.valueOf(fee[3]));
                    moneys.add((Double) fee[4]);
                }
            }
            tem.setMoneys(moneys);
            tem.setPriceName(pricenames);
            datas.add(tem);
        }
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
//		List<Project> projects = projectService.getProjects(ugroup);

		if (!(project_id == null || price_id == null || project_id.equals("") || price_id
				.equals(""))) {
			Project project = projectService.findOneView(Integer
					.parseInt(project_id));
			Price price = priceService.findoneview(Integer.parseInt(price_id));
			List<AnalysisData> analysisDatas = analysisService.getList(datestr, datestr2, Integer.parseInt(price_id), Integer.parseInt(project_id), ugroup);// 获取明细数据
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
//		request.setAttribute("projects", projects);
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


    @RequestMapping("/securi_showDetailByItemCode")
    @ResponseBody
    public Json securi_showDetailByItemCode(HttpServletResponse response, HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        List<Integer> ugroup = sessionInfo.getUgroup();
        String cid = sessionInfo.getCompid();
        String projectName = request.getParameter("projectName");// 项目名称
        String startDate = request.getParameter("startDate");// 日期
        String endDate = request.getParameter("endDate");// 日期
        String itemCode = request.getParameter("itemCode");
        String unit = request.getParameter("unit");
        String price = request.getParameter("price");
        List<FieldData> analysisDatasByItemCode = analysisService.getListByItemCode(itemCode, unit, price, startDate, endDate, projectName, ugroup, cid);
        System.out.println(analysisDatasByItemCode);
        j.setMsg("成功");
        j.setObj(analysisDatasByItemCode);
        j.setSuccess(true);
        return j;
    }

	@RequestMapping("/materialStatReportPage")
	public String materialStatReportPage(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String uid = sessionInfo.getId();
		List<S_department> departments = sessionInfo.getDepartmentIds();

		List<Integer> ugroup = new ArrayList<Integer>();
		Integer selDepartmentId = request.getParameter("selDepartmentId") == null ? -1 : Integer.parseInt(request.getParameter("selDepartmentId"));
		if (selDepartmentId == -1) { // 没传，
			for (S_department department : departments) {
				if (department.getId() != 0) {
					selDepartmentId = department.getId();
					break;
				}
			}

		}
		ugroup = departmentService.getUsersByDepartmentId(cid, Integer.parseInt(uid), selDepartmentId);

		String selectedMonth = request.getParameter("selectedMonth");
		if (StringUtil.trimToEmpty(selectedMonth).equals("")) {
			selectedMonth = DateKit.monthlyToStr(DateKit.getDate());
		}
		List<Map<String, Object>> materialStatInfos = fieldDataService.getMaterialDatas(cid, selectedMonth, ugroup, selDepartmentId);
		request.setAttribute("materialStatInfos", materialStatInfos);
		request.setAttribute("selectedMonth", selectedMonth);
		request.setAttribute("selDepartmentId", selDepartmentId);
		if (departments.size()>1) {
			request.setAttribute("departments", departments);
		}

		return "/app/analysis/materialStatReport";
	}
}
