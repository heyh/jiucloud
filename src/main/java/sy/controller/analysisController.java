package sy.controller;

import freemarker.ext.beans.HashAdapter;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
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
import java.text.SimpleDateFormat;
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
//		List<S_department> departments = sessionInfo.getDepartmentIds();
		List<S_department> departments = departmentService.getFirstLevelUnderDepByUid(cid, uid);

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

		String startDate = request.getParameter("startDate");
		if (StringUtil.trimToEmpty(startDate).equals("")) {
			startDate = DateKit.lastMonth() + "-21";
		}
		String endDate = request.getParameter("endDate");
		if (StringUtil.trimToEmpty(endDate).equals("")) {
			endDate = DateKit.currentMonth() + "-20";
		}
//		List<Map<String, Object>> materialStatInfos = fieldDataService.getMaterialDatas(cid, selectedMonth, ugroup, selDepartmentId);
		List<Map<String, Object>> materialStatInfos = fieldDataService.getMaterialDatas(cid, startDate, endDate, ugroup, selDepartmentId);

		request.setAttribute("materialStatInfos", materialStatInfos);
		request.setAttribute("selectedMonth", selectedMonth);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("selDepartmentId", selDepartmentId);
		request.setAttribute("departments", JSONArray.fromObject(departments));

		return "/app/analysis/materialStatReport";
	}

	@RequestMapping("/boqPage")
	public String boqPage(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String uid = sessionInfo.getId();
		String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
		String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
		if (startDate.equals("")) {
			startDate = UtilDate.getshortFirst() + " 00:00:00";
		} else {
            startDate = startDate + " 00:00:00";
        }
		if (endDate.equals("")) {
			endDate = UtilDate.getshortLast() + " 23:59:59";
		} else {
            endDate = endDate + " 23:59:59";
        }

        String itemCode = StringUtil.trimToEmpty(request.getParameter("itemCode"));

//		List<S_department> departments = sessionInfo.getDepartmentIds();
		List<S_department> departments = departmentService.getFirstLevelUnderDepByUid(cid, uid);

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

		List<FieldData> boq = fieldDataService.getBoq(cid, startDate, endDate, ugroup, "show", itemCode);

		List<Map<String, Object>> projects = new ArrayList<Map<String, Object>>();
		for (FieldData item : boq) {
			boolean hasProj = false;
			for (Map<String, Object> project : projects) {
				if (StringUtil.trimToEmpty(project.get("projectId")).equals(StringUtil.trimToEmpty(item.getProject_id()))) {
					hasProj = true;
				}
			}
			if (!hasProj) {
				Map<String, Object> project = new HashMap<String, Object>();
				project.put("projectId", item.getProject_id());
				project.put("projectName", item.getProjectName());
				projects.add(project);
			}
		}
		request.setAttribute("boq", boq);
		request.setAttribute("projects", projects);
		request.setAttribute("first", startDate.substring(0, 10));
		request.setAttribute("last", endDate.substring(0, 10));
		request.setAttribute("selDepartmentId", selDepartmentId);
		request.setAttribute("itemCode", itemCode);
		Cost cost = costService.getCostByCode(itemCode, cid);
		request.setAttribute("costType", cost == null ? "" : cost.getCostType());
		request.setAttribute("departments", JSONArray.fromObject(departments));
		return "/app/analysis/boq";
	}

    @RequestMapping("/securi_boqExecl")
    public ModelAndView exportExcel(@RequestParam(value = "startDate", required = false) String startDate,
                                    @RequestParam(value = "endDate", required = false) String endDate,
                                    @RequestParam(value = "selDepartmentId", required = false) int selDepartmentId,
                                    HttpServletResponse response, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        try {
            response.setContentType("text/html;charset=utf8");
            SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
            String cid = sessionInfo.getCompid();
            String uid = sessionInfo.getId();
            if (startDate.equals("")) {
                startDate = UtilDate.getshortFirst() + " 00:00:00";
            } else {
                startDate = startDate + " 00:00:00";
            }
            if (endDate.equals("")) {
                endDate = UtilDate.getshortLast() + " 23:59:59";
            } else {
                endDate = endDate + " 23:59:59";
            }
			String itemCode = StringUtil.trimToEmpty(request.getParameter("itemCode"));

//            List<S_department> departments = sessionInfo.getDepartmentIds();
			List<S_department> departments = departmentService.getFirstLevelUnderDepByUid(cid, uid);

			List<Integer> ugroup = new ArrayList<Integer>();
            selDepartmentId = request.getParameter("selDepartmentId") == null ? -1 : Integer.parseInt(request.getParameter("selDepartmentId"));
            if (selDepartmentId == -1) { // 没传
                for (S_department department : departments) {
                    if (department.getId() != 0) {
                        selDepartmentId = department.getId();
                        break;
                    }
                }

            }
            ugroup = departmentService.getUsersByDepartmentId(cid, Integer.parseInt(uid), selDepartmentId);

            List<FieldData> datas = fieldDataService.getBoq(cid, startDate, endDate, ugroup, "execl", itemCode);

            List<Map<String, Object>> projects = new ArrayList<Map<String, Object>>();
            for (FieldData item : datas) {
                boolean hasProj = false;
                for (Map<String, Object> project : projects) {
                    if (StringUtil.trimToEmpty(project.get("projectId")).equals(StringUtil.trimToEmpty(item.getProject_id()))) {
                        hasProj = true;
                    }
                }
                if (!hasProj) {
                    Map<String, Object> project = new HashMap<String, Object>();
                    project.put("projectId", item.getProject_id());
                    project.put("projectName", item.getProjectName());
                    projects.add(project);
                }
            }

            Map<String, Object> dataMap = new HashMap<String, Object>();

            String largeTitleContent = "工程量清单";
            int cellCount = 7;
            Map<String, Object> largeTitle = new HashMap<String, Object>();
            largeTitle.put("largeTitleContent", largeTitleContent);
            largeTitle.put("cellCount", cellCount);
            dataMap.put("largeTitle", largeTitle);


            List<PageData> varList = new ArrayList<PageData>();
            PageData vpd = new PageData();
            for (int i=0; i<projects.size(); i++) {
            	if (i > 0) {
					vpd = new PageData();
					vpd.put("var1", "");
					vpd.put("var2", "");
					vpd.put("var3", "");
					vpd.put("var4", "");
					vpd.put("var5", "");
					vpd.put("var6", "");
					vpd.put("var7", "");
					varList.add(vpd);
				}

				vpd = new PageData();
				vpd.put("var1", "工程名称:" + projects.get(i).get("projectName"));
				vpd.put("var2", "");
				vpd.put("var3", "");
				vpd.put("var4", "");
				vpd.put("var5", "");
				vpd.put("var6", "");
				vpd.put("var7", "");
				varList.add(vpd);

				vpd = new PageData();
				vpd.put("var1", "序号");
				vpd.put("var2", "项目编码");
				vpd.put("var3", "项目名称");
				vpd.put("var4", "项目特征描述");
				vpd.put("var5", "计量单位");
				vpd.put("var6", "工程量");
				vpd.put("var7", "备注");
				varList.add(vpd);

				for (int j = 0; j < datas.size(); j++) {
					if (Integer.parseInt(StringUtil.trimToEmpty(projects.get(i).get("projectId"))) == datas.get(j).getProject_id()) {
						vpd = new PageData();
						vpd.put("var1", StringUtil.trimToEmpty(j + 1));
						vpd.put("var2", datas.get(j).getItemCode());
						vpd.put("var3", datas.get(j).getDataName());
						vpd.put("var4", datas.get(j).getRemark());
						vpd.put("var5", datas.get(j).getUnit());
						vpd.put("var6", datas.get(j).getCount());
						vpd.put("var7", StringUtil.trimToEmpty(datas.get(j).getId()));
						varList.add(vpd);
					}
				}
			}
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

	@RequestMapping("/securi_maintenanceDetails")
	public ModelAndView maintenanceDetails(@RequestParam(value = "exportMaintenanceDetailsStartDate", required = false) String exportMaintenanceDetailsStartDate,
										   @RequestParam(value = "exportMaintenanceDetailsEndDate", required = false) String exportMaintenanceDetailsEndDate,
										   @RequestParam(value = "exportMaintenanceDetailsItemCode", required = false) String exportMaintenanceDetailsItemCode,
										   HttpServletResponse response, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			response.setContentType("text/html;charset=utf8");
			SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
			String cid = sessionInfo.getCompid();

			String startDate = exportMaintenanceDetailsStartDate.length()  > 10 ? exportMaintenanceDetailsStartDate : exportMaintenanceDetailsStartDate + " 00:00:00";
			String endDate= exportMaintenanceDetailsEndDate.length() > 10 ? exportMaintenanceDetailsEndDate : exportMaintenanceDetailsEndDate + " 23:59:59";
			String itemCode = exportMaintenanceDetailsItemCode;

			List<Object[]> tempMaintenanceDetailsList = fieldDataService.getMaintenanceDetails(cid, startDate, endDate, itemCode);

			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			Map<String, Object> maintenanceDetails = new HashMap<String, Object>();

			List<Map<String, Object>> billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");

			int i=4;
			List<PageData> varList = new ArrayList<PageData>();
			PageData vpd = new PageData();
			vpd = new PageData();
			i=4;
			vpd.put("var1", "填报单位：南京市市政工程管理处");
			vpd.put("var2", "");
			vpd.put("var3", "");
			for (Map<String, Object> billCostInfo : billCostInfos) {
				if (Integer.parseInt(StringUtil.trimToEmpty(billCostInfo.get("isSend"))) == 1) {
					vpd.put("var" + i++, "");
				}
			}
			varList.add(vpd);

			vpd = new PageData();
			i=4;
			vpd.put("var1", "littleTitle|3");
			vpd.put("var2", "");
			vpd.put("var3", "");
			for (Map<String, Object> billCostInfo : billCostInfos) {
				if (Integer.parseInt(StringUtil.trimToEmpty(billCostInfo.get("level"))) == 3) {
					int count = 0;
					for (Map<String, Object> bill : billCostInfos) {
						if (Long.parseLong(StringUtil.trimToEmpty(bill.get("pid"))) == Long.parseLong(StringUtil.trimToEmpty(billCostInfo.get("nid")))) {
							count++;
						}
					}
					vpd.put("var" + i++, StringUtil.trimToEmpty(billCostInfo.get("costType")) + "|" +count);

					for (Map<String, Object> bill : billCostInfos) {
						if (Long.parseLong(StringUtil.trimToEmpty(bill.get("pid"))) == Long.parseLong(StringUtil.trimToEmpty(billCostInfo.get("nid")))) {
							vpd.put("var" + i++, "");
						}
					}
					i--;
				}
			}
			varList.add(vpd);

			vpd = new PageData();
			i=4;
			vpd.put("var1", "序号");
			vpd.put("var2", "维护日期");
			vpd.put("var3", "设施名称");
			for (Map<String, Object> billCostInfo : billCostInfos) {
				if (Integer.parseInt(StringUtil.trimToEmpty(billCostInfo.get("isSend"))) == 1) {
					vpd.put("var" + i++, StringUtil.trimToEmpty(billCostInfo.get("costType")));
				}
			}
			varList.add(vpd);

			for (Object[] tempMaintenanceDetails : tempMaintenanceDetailsList) {
				maintenanceDetails = new HashMap<String, Object>();
				maintenanceDetails.put("createDate", tempMaintenanceDetails[0]);
				maintenanceDetails.put("specifications", tempMaintenanceDetails[1]);
				maintenanceDetails.put(StringUtil.trimToEmpty(tempMaintenanceDetails[2]), tempMaintenanceDetails[3]);
				datas.add(maintenanceDetails);
			}




			String largeTitleContent = "维护完成明细表（" + exportMaintenanceDetailsStartDate + " 至 " + exportMaintenanceDetailsEndDate + "）";
			int cellCount = vpd.size();
			Map<String, Object> largeTitle = new HashMap<String, Object>();
			largeTitle.put("largeTitleContent", largeTitleContent);
			largeTitle.put("cellCount", cellCount);
			dataMap.put("largeTitle", largeTitle);

			for (int j = 0; j < datas.size(); j++) {
				vpd = new PageData();
				vpd.put("var1", StringUtil.trimToEmpty(j + 1));
				vpd.put("var2", datas.get(j).get("createDate"));
				vpd.put("var3", datas.get(j).get("specifications"));
				int _j=4;
				for (Map<String, Object> billCostInfo : billCostInfos) {
					if (Integer.parseInt(StringUtil.trimToEmpty(billCostInfo.get("isSend"))) == 1) {
						vpd.put("var" + _j++ , datas.get(j).get(StringUtil.trimToEmpty(billCostInfo.get("itemCode"))));
					}
				}
				varList.add(vpd);
			}

            vpd = new PageData();
			vpd.put("var1", "total");
            vpd.put("var2", "");
            vpd.put("var3", "");
            for (int j=4; j<=varList.get(0).size(); j++) {
                Double total = Double.valueOf(0);
                for (int _j=3; _j<varList.size(); _j++) {
                    String val = StringUtil.trimToEmpty(varList.get(_j).get("var" + j));
                    total += val.equals("") ? 0 : Double.valueOf(val).doubleValue();
                }
                vpd.put("var" + j, new java.text.DecimalFormat("#.00").format(total));
            }
            varList.add(vpd);

			dataMap.put("varList", varList);
            ObjectExcelViewSpecial erv = new ObjectExcelViewSpecial();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}
}
