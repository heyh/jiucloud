package sy.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.model.po.Cost;
import sy.model.po.Price;
import sy.model.po.Price_Cost;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.CostServiceI;
import sy.service.PriceServiceI;
import sy.util.ConfigUtil;

@Controller
@RequestMapping("/priceController")
public class PriceController {

	@Autowired
	private PriceServiceI priceService;

	@Autowired
	private CostServiceI costService;

	@RequestMapping("/manager")
	public String manager(HttpSession session, HttpServletRequest request) {
		return "/app/price/manager";
	}

	/* 跳转费用类型添加 */
	@RequestMapping("/securi_toAddCost")
	public String toAddCost(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("price_id", id);
		return "/app/price/cost_select";
	}

	/* 跳转费用类型编辑 */
	@RequestMapping("/securi_toEditCost")
	public String toEditCost(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("price_id", id);
		return "/app/price/cost_edit";
	}

	/* 跳转分类添加页面 */
	@RequestMapping("/securi_toAddPage")
	public String toAddPage(HttpSession session, HttpServletRequest request) {
		return "/app/price/add";
	}

	/* 跳转分类编辑页面 */
	@RequestMapping("/securi_toEditPage")
	public String toEditPage(HttpSession session, HttpServletRequest request) {
		String id = request.getParameter("id");
		try {
			Price price = priceService.findoneview(Integer.parseInt(id));
			request.setAttribute("price", price);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/app/price/edit";
	}

	/* 分类管理 */
	@ResponseBody
	@RequestMapping("/securi_dataGrid")
	public DataGrid datagrid(HttpSession session, HttpServletRequest request,
			PageHelper ph) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String name = request.getParameter("name");
		System.out.println("name"+name);
		DataGrid dataGrid = priceService.dataGrid(Integer.parseInt(cid), name,
				ph);
		return dataGrid;
	}

	/* 添加分类 */
	@ResponseBody
	@RequestMapping("/securi_addPrice")
	public Json addPrice(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		Json json = new Json();
		String name = request.getParameter("name");
		try {
			Price price = new Price();
			price.setName(name);
			price.setCid(Integer.parseInt(cid));
			priceService.add(price);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,添加失败!!!");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("添加成功");
		return json;
	}

	/* 编辑分类 */
	@ResponseBody
	@RequestMapping("/securi_edit")
	public Json edit(HttpServletRequest request) {
		Json json = new Json();
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		try {
			Price price = priceService.findoneview(Integer.parseInt(id));
			price.setName(name);
			priceService.update(price);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,添加失败!!!");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("修改成功");
		return json;
	}

	/* 删除分类 */
	@ResponseBody
	@RequestMapping("/securi_delete")
	public Json delete(HttpServletRequest request) {
		Json json = new Json();
		String id = request.getParameter("id");
		try {
			priceService.delete(Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,添加失败!!!");
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
		String price_id = request.getParameter("price_id");

		try {
			String[] id = ids.trim().split(",");
			for (String tem : id) {
				Cost cost = costService.findById(tem);
				Price_Cost pCost = new Price_Cost();
				pCost.setPrice_id(Integer.parseInt(price_id));
				pCost.setCost_id(cost.getId());
				priceService.add2(pCost);
				// List<Cost> costs = costService.getFamily(cost.getNid(),
				// cost.getCid());
				// for (Cost tem2 : costs) {
				// // if (Arrays.asList(id)
				// // .contains(String.valueOf(tem2.getId()))) {
				// // continue;
				// // }
				// Price_Cost pCost = new Price_Cost();
				// pCost.setPrice_id(Integer.parseInt(price_id));
				// pCost.setCost_id(tem2.getId());
				// priceService.add2(pCost);
				// }
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

	/* 编辑费用类型 */
	@ResponseBody
	@RequestMapping("/securi_deletecost")
	public Json deletecost(HttpServletRequest request) {
		Json json = new Json();
		String ids = request.getParameter("ids");
		String price_id = request.getParameter("price_id");
		try {
			String[] id = ids.trim().split(",");
			System.out.println(Arrays.toString(id));
			for (String tem : id) {
				Price_Cost pCost = priceService.findoneview2(
						Integer.parseInt(tem), Integer.parseInt(price_id));
				System.out.println(pCost.getId() + "--------------");
				priceService.delete2(pCost);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("服务器发生错误,删除失败!");
			return json;
		}
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}

	/* 获取有待添加的费用类型列表数据 */
	@ResponseBody
	@RequestMapping("/securi_costList")
	public DataGrid costList(PageHelper ph, HttpServletRequest request) {
		String cid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getCompid();
		String price_id = request.getParameter("price_id");
		String title = request.getParameter("title");
		String code = request.getParameter("code");

		DataGrid dataGrid = null;
		try {
			dataGrid = costService.dataGridWithPrice(title, code, ph, cid,
					Integer.parseInt(price_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataGrid;
	}

	/* 获取已经添加的费用类型列表数据 */
	@ResponseBody
	@RequestMapping("/securi_priceList")
	public DataGrid priceList(PageHelper ph, HttpServletRequest request) {
		String cid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getCompid();
		String price_id = request.getParameter("price_id");
		String title = request.getParameter("title");
		String code = request.getParameter("code");

		DataGrid dataGrid = null;
		try {
			dataGrid = costService.dataGridInPrice(title, code, ph, cid,
					Integer.parseInt(price_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataGrid;
	}
}
