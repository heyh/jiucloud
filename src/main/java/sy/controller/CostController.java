package sy.controller;

import jxl.Sheet;
import jxl.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.S_department;
import sy.model.po.Cost;
import sy.model.po.Department_Cost;
import sy.model.po.Tpcost;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.CostServiceI;
import sy.service.DepartmentServiceI;
import sy.service.TaskServiceI;
import sy.service.TpcostServiceI;
import sy.util.ConfigUtil;
import sy.util.GetRealPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * **************************************************************** 文件名称 :
 * ApplicationController.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:21:38
 * 文件描述 : Android 服务号通知控制器 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/costController")
public class CostController extends BaseController {

	@Autowired
	private CostServiceI costService;

	@Autowired
	private DepartmentServiceI departmentService;

	@Autowired
	private TaskServiceI taskService;

	@Autowired
	private TpcostServiceI tpcostService;

	/**
	 * 跳转树形列表页面
	 */
	@RequestMapping("/costShow")
	public String manager() {
		return "/app/cost/showcost";
	}

	/**
	 * 跳转管理页面
	 */
	@RequestMapping("/searchcost")
	public String search() {
		return "/app/cost/showsearchcost";
	}

	/**
	 * 跳转部门费用管理页面
	 */
	@RequestMapping("/cost_department")
	public String cost_department() {
		return "/app/dep/manager";
	}

	/**
	 * 跳转费用导入页面
	 */
	@RequestMapping("/securi_uploadCostPage")
	public String uploadCostPage() {
		return "/app/cost/uploadCost";
	}

	/**
	 * 跳转部门费用添加页面
	 */
	@RequestMapping("/securi_todepartment_costlist")
	public String todepartment_costlist(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("department_id", id);
		return "/app/dep/cost_select";
	}

	/**
	 * 跳转部门费用编辑页面
	 */
	@RequestMapping("/securi_todepartment_list")
	public String todepartment_list(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("department_id", id);
		return "/app/dep/cost_edit";
	}

	/**
	 * 跳转部门费用添加页面
	 */
	@RequestMapping("/securi_cost_select")
	public String cost_select() {
		return "/app/dep/cost_select";
	}

	/**
	 * 获取树形数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/securi_treeGrid")
	@ResponseBody
	public String dataGrid(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String did = sessionInfo.getDepartment_id();
        List<S_department> departments = sessionInfo.getDepartmentIds();
		String title = request.getParameter("title");
		String source = request.getParameter("source");

		System.out.println(title);

		int department_id = 0;
        List<Integer> departmentIds = new ArrayList<Integer>();
		if (title != null) {
//			department_id = title.equals("1") ? Integer.parseInt(did) : 0; // 暂时把部门费用限制去掉
            for (S_department department : departments) {
                departmentIds.add(department.getId());
            }
		}

		JSONArray json = new JSONArray();
//		List<Cost> list = (List<Cost>) costService.dataGrid(department_id, cid,source).getRows();
        List<Cost> list = (List<Cost>) costService.dataGrid(departmentIds, cid,source).getRows();
		for (Cost cost : list) {
//			String costType=cost.getCostType();
//			if (source.equals("doc"))
//			{
//				costType="纯附件";
//			}
			JSONObject jo = new JSONObject();
			jo.put("id", cost.getNid());
			jo.put("costType",cost.getCostType());
			jo.put("cId", cost.getCid());
			jo.put("pid", cost.getPid());
			jo.put("sort", cost.getSort());
			jo.put("nid", cost.getId());
			jo.put("itemCode", cost.getItemCode());
			jo.put("isend", cost.getIsend());
			jo.put("level", cost.getLevel());
			json.put(jo);
		}

		return json.toString();
	}

	/**
	 * 获取管理数据表格
	 * 
	 * @return
	 */
	@RequestMapping("/securi_departmentGrid")
	@ResponseBody
	public DataGrid departmentGrid(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();

		DataGrid dataGrid = costService.departmentGrid(cid);

		System.out.println(dataGrid);

		return dataGrid;
	}

	/**
	 * 在弹出框中获取数据
	 * 
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid showDataGrid(PageHelper ph, HttpServletRequest request) {
		String cid = ((SessionInfo) request.getSession().getAttribute(
				ConfigUtil.getSessionInfoName())).getCompid();
		String title = request.getParameter("title");
		String code = request.getParameter("code");
		DataGrid dataGrid = costService.dataGrid(title, code, ph, cid);
		return dataGrid;
	}

	/**
	 * 跳转到添加
	 * 
	 * @return
	 */
	@RequestMapping("/addCostPage")
	public String addPage(HttpServletRequest request) {
		String pid = request.getParameter("pid");
		request.setAttribute("pid", pid);
		return "/app/cost/addCost";
	}

	/**
	 * 跳转到修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateCostPage")
	public String updatePage(HttpServletRequest request) {
		String id = request.getParameter("id");
		Cost cost = costService.findById(id);
		request.setAttribute("cost", cost);
		return "/app/cost/updateCost";
	}

	/**
	 * 保存
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addCost")
	@ResponseBody
	public Json saveCost(Cost cost, HttpSession session,
			HttpServletRequest request) {
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		try {

			if (costService.getCostByCode(cost.getItemCode(), cid) != null) {
				j.setMsg("该费用编码已经存在");
				return j;
			}
			List<Cost> temCosts = costService.getLikeCostByCode(
					cost.getItemCode(), cid);

			if (temCosts != null && temCosts.size() > 0) {
				j.setMsg("检测到这并不是一个最子节点,为防造成混乱已限制插入,您可以删除该费用编码下的所有子节点后重试");
				return j;
			}

			cost.setIsend(1);
			cost.setCid(cid);
			cost.setNid(costService.getMaxNidByCid(cid));

			Cost parent = costService.getParentByCode(cost.getItemCode(), cid);

			if (parent == null) {
				cost.setPid("-1");
				cost.setLevel(1);
				String sort = cost.getItemCode().replaceAll("[^0-9]", "")
						.trim();
				cost.setSort(Integer.parseInt(sort));
			} else {
				cost.setPid(parent.getNid());
				cost.setLevel(parent.getLevel() + 1);
				String sort = cost
						.getItemCode()
						.substring(parent.getItemCode().length(),
								cost.getItemCode().length())
						.replaceAll("[^0-9]", "").trim();
				cost.setSort(Integer.parseInt(sort));

				if (parent.getIsend() == 1) {
					parent.setIsend(0);
					costService.update(parent);
				}
			}

			costService.add(cost);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateCost")
	@ResponseBody
	public Json updateCost(String id, String costType, HttpSession session) {
		Json j = new Json();
		try {
			Cost cost = costService.findById(id);
			cost.setCostType(costType);
			costService.update(cost);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delCost")
	@ResponseBody
	public Json delCost(String id, HttpSession session) {
		Json j = new Json();
		try {
			Cost tem = costService.findById(id);
			costService.delete(tem);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 批量删除
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_buntchDelCost")
	@ResponseBody
	public Json buntchDel(String ids, HttpSession session) {

		String[] id = ids.split(",");
		System.out.println(Arrays.toString(id));

		Json j = new Json();
		try {
			for (String tem : id) {
				Cost cost = costService.findById(tem);
				costService.delete(cost);
			}
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	// /**
	// * 上移Cost优先级
	// *
	// * @return
	// */
	// @RequestMapping("/upSort")
	// @ResponseBody
	// public Json upSort(String id) {
	// Json j = new Json();
	// j.setSuccess(true);
	// if (!costService.upSort(id)) {
	// j.setSuccess(false);
	// j.setMsg("已经是该级别最顶单位,上移失败");
	// }
	// return j;
	// }
	//
	// /**
	// * 下移Cost优先级
	// *
	// * @return
	// */
	// @RequestMapping("/downSort")
	// @ResponseBody
	// public Json downSort(String id) {
	// Json j = new Json();
	// j.setSuccess(true);
	// costService.downSort(id);
	// if (!costService.downSort(id)) {
	// j.setSuccess(false);
	// j.setMsg("已经是该级别最底单位,下移失败");
	// }
	// return j;
	// }

	@RequestMapping("/getFamily")
	@ResponseBody
	public List<Cost> getCities(HttpServletRequest req) {
		SessionInfo sessionInfo = (SessionInfo) req.getSession().getAttribute(
				ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String nid = req.getParameter("nid");
		List<Cost> list = costService.getFamily(nid, cid);
		return list;
	}

	@RequestMapping("/securi_selectc")
	public String selectc(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		List<S_department> departments = taskService.getDepsByCompanyId(Integer
				.parseInt(cid));
		List<Cost> costs = costService.getprices(cid);
		request.setAttribute("departments", departments);
		request.setAttribute("costs", costs);
		return "/app/cost/cost_select";
	}

	/**
	 * 往分类中添加费用类型
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/securi_addtpcost")
	public Json addtpcost(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		Json json = new Json();
		try {
			String department_id = request.getParameter("department_id");
			String pcost_id = request.getParameter("pcost_id");
			String cost_id = request.getParameter("proidh");
			System.out.println(department_id + "\t" + pcost_id + "\tcost_id:"
					+ cost_id);
			if (department_id == null || "".equals(department_id)) {
				json.setMsg("请选择部门");
				return json;
			}
			if (pcost_id == null || "".equals(pcost_id)) {
				json.setMsg("请选择价目类型");
				return json;
			}
			if (cost_id == null || "".equals(cost_id)) {
				json.setMsg("请选择费用类型");
				return json;
			}
			Tpcost tpcost = new Tpcost(department_id, pcost_id, cost_id);
			Cost cost = costService.findById(cost_id);

			Tpcost tcost = tpcostService.findOneView(tpcost);
			if (tcost != null) {
				json.setMsg("该部门下的该费用类型已经添加过了!");
				return json;
			}
			List<Cost> tree = costService.getFamily(cost.getNid(), cid);
			for (Cost c : tree) {
				Tpcost tem = new Tpcost(department_id, pcost_id,
						String.valueOf(c.getId()));
				tpcostService.add(tem);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			json.setMsg("系统错误,请重试");
			return json;
		}
		json.setMsg("添加成功,您可以继续添加");
		json.setSuccess(true);
		return json;
	}

	/**
	 * 往分类中删除费用类型
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/securi_delPrice")
	public Json delPrice(Tpcost tcost) {
		Json json = new Json();
		try {
			Tpcost tem = tpcostService.findOneView(tcost);
			tpcostService.delete(tem);
		} catch (Exception e) {
			json.setMsg("系统错误,请重试");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}

	/* 添加费用类型 */
	@ResponseBody
	@RequestMapping("/securi_addcost")
	public Json addcost(HttpServletRequest request) {
		Json json = new Json();
		String ids = request.getParameter("ids");
		String department_id = request.getParameter("department_id");

		try {
			String[] id = ids.trim().split(",");
			for (String tem : id) {
				Cost cost = costService.findById(tem);
				List<Cost> costs = costService.getFamily(cost.getNid(),
						cost.getCid());
				for (Cost tem2 : costs) {
					// if (Arrays.asList(id)
					// .contains(String.valueOf(tem2.getId()))) {
					// continue;
					// }
					Department_Cost department_Cost = new Department_Cost();
					department_Cost.setCost_id(tem2.getId());
					department_Cost.setDepartment_id(Integer
							.parseInt(department_id));
					costService.add2(department_Cost);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,添加失败!");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("添加成功");
		return json;
	}

	/* 删除部门费用类型 */
	@ResponseBody
	@RequestMapping("/securi_deletecost")
	public Json deletecost(HttpServletRequest request) {
		Json json = new Json();
		String ids = request.getParameter("ids");
		String department_id = request.getParameter("department_id");
		try {
			String[] id = ids.trim().split(",");
			for (String tem : id) {
				Department_Cost Department_Cost = costService.findoneview2(
						Integer.parseInt(tem), Integer.parseInt(department_id));
				costService.delete2(Department_Cost);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,添加失败!");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}

	/**
	 * 获取尚未添加的部门费用数据
	 */
	@ResponseBody
	@RequestMapping("/securi_department_costList")
	public DataGrid costList(PageHelper ph, HttpServletRequest request) {
		String cid = ((SessionInfo) request.getSession().getAttribute(
				ConfigUtil.getSessionInfoName())).getCompid();
		String department_id = request.getParameter("department_id");
		String title = request.getParameter("title");
		String code = request.getParameter("code");

		DataGrid dataGrid = null;
		try {
			dataGrid = costService.dataGridWithDepartment(title, code, ph, cid,
					Integer.parseInt(department_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataGrid;
	}

	/**
	 * 获取已经添加的部门费用数据
	 */
	@ResponseBody
	@RequestMapping("/securi_department_list")
	public DataGrid priceList(PageHelper ph, HttpServletRequest request) {
		String cid = ((SessionInfo) request.getSession().getAttribute(
				ConfigUtil.getSessionInfoName())).getCompid();
		String department_id = request.getParameter("department_id");
		String title = request.getParameter("title");
		String code = request.getParameter("code");

		DataGrid dataGrid = null;
		try {
			dataGrid = costService.dataGridInDepartment(title, code, ph, cid,
					Integer.parseInt(department_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataGrid;
	}

	/**
	 * 费用类型文件导入
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/securi_upload")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			MultipartHttpServletRequest rt) {

		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();

		try {

			GetRealPath grp = new GetRealPath(req.getSession()
					.getServletContext());

			String file_path = grp.getRealPath() + "upload/";

			MultipartFile patch = rt.getFile(req.getParameter("name"));// 获取文件

			File file = new File(file_path + "tem/tem.txt");

			patch.transferTo(file);

			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStream is = new FileInputStream(file.getAbsolutePath());
				// jxl提供的Workbook类
				Workbook wb = Workbook.getWorkbook(is);

				Sheet sheet = wb.getSheet(0);
				for (int i = 1; i < sheet.getRows(); i++) {
					Cost cost = new Cost();
					System.out.println(sheet.getCell(0, i).getContents());
					System.out.println(sheet.getCell(1, i).getContents());
					cost.setCostType(sheet.getCell(0, i).getContents());
					cost.setItemCode(sheet.getCell(1, i).getContents());

					if (costService.getCostByCode(cost.getItemCode(), cid) != null) {
						continue;
					}

					List<Cost> temCosts = costService.getLikeCostByCode(
							cost.getItemCode(), cid);

					if (temCosts != null && temCosts.size() > 0) {
						continue;
					}

					cost.setIsend(1);
					cost.setCid(cid);
					cost.setNid(costService.getMaxNidByCid(cid));

					Cost parent = costService.getParentByCode(
							cost.getItemCode(), cid);

					if (parent == null) {
						cost.setPid("-1");
						cost.setLevel(1);
						String sort = cost.getItemCode()
								.replaceAll("[^0-9]", "").trim();
						cost.setSort(Integer.parseInt(sort));
					} else {
						cost.setPid(parent.getNid());
						cost.setLevel(parent.getLevel() + 1);
						String sort = cost
								.getItemCode()
								.substring(parent.getItemCode().length(),
										cost.getItemCode().length())
								.replaceAll("[^0-9]", "").trim();
						cost.setSort(Integer.parseInt(sort));

						if (parent.getIsend() == 1) {
							parent.setIsend(0);
							costService.update(parent);
						}
					}

					costService.add(cost);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			j.setMsg("上传异常:" + ex.getMessage());
			return j;
		}
		j.setSuccess(true);
		j.setMsg("导入成功,请刷新页面");
		return j;
	}
}
