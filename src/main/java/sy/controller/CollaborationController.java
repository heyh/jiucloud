package sy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.model.Collaboration;
import sy.model.Item;
import sy.pageModel.*;
import sy.service.CollaborationServiceI;
import sy.service.ItemServiceI;
import sy.util.ConfigUtil;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collcontroller")
public class CollaborationController {

	@Autowired
	private CollaborationServiceI collaborationService;

	@Autowired
	private ItemServiceI itemService;

	/**
	 * 获取表格数据
	 * @param ph
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_dataGrid")
	@ResponseBody
	public DataGrid dataGrid(PageHelper ph, HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String pid = request.getParameter("pid");
		DataGrid dataGrid = collaborationService.dataGrid(cid, pid, ph);
		return dataGrid;
	}

	/**
	 * 跳转新增页面
	 * @return
	 */
	@RequestMapping("/securi_toAddPage")
	public String toAddPage(HttpServletRequest request) {
		request.setAttribute("pid", request.getParameter("pid"));
		return "/app/col/addCollabora";
	}

	/**
	 * 跳转编辑页面
	 * @return
	 */
	@RequestMapping("/securi_toEditPage")
	public String toEditPage(int id, HttpServletRequest request) {
		Collaboration tem = collaborationService.findoneview(id);
		request.setAttribute("detail", tem);

		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String uid = sessionInfo.getId();
		List<Map<String, Object>> selectItems = itemService.getSelectItems(cid, String.valueOf(tem.getPid()));
		if (selectItems.size()<=0) {
			List<Item> defaultItemList = itemService.getDefaultItems();
			for (Item defaultItem : defaultItemList) {
				defaultItem.setCid(cid);
				defaultItem.setProjectId(String.valueOf(tem.getPid()));
				defaultItem.setOperator(uid);
				itemService.add(defaultItem);
			}
			selectItems = itemService.getSelectItems(cid, String.valueOf(tem.getPid()));
		}
		request.setAttribute("selectItems", selectItems);

		return "/app/col/editCollabora";
	}

	/**
	 * 跳转管理页面
	 * @return
	 */
	@RequestMapping("/securi_toShowPage")
	public String toShowPage(HttpServletRequest request) {
		String pid = request.getParameter("pid");
		System.out.println(pid);
		request.setAttribute("pid", pid);
		return "/app/col/showCollabora";
	}
	
	/**
	 * 跳转预览页面
	 * @return
	 */
	@RequestMapping("/securi_toViewPage")
	public String toViewPage(int id, HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String sectionName = "";
		Collaboration tem = collaborationService.findoneview(id);

		PmCollaboration pmCollaboration = new PmCollaboration();
		if (tem != null) {
			Item sectionItem = itemService.getSingleItem(sessionInfo.getCompid(), String.valueOf(tem.getPid()), tem.getSection());
			if (sectionItem == null) {
				sectionName = "标段1";
			} else {
				sectionName = sectionItem.getName();
			}
		}
		request.setAttribute("sectionName", sectionName);
		request.setAttribute("detail", tem);
		return "/app/col/viewCollabora";
	}

	/**
	 * 删除
	 */
	@RequestMapping("/securi_delete")
	@ResponseBody
	public Json delete(Integer id) {
		Json j = new Json();
		if (id != null) {
			Collaboration tem = new Collaboration();
			tem.setId(id);
			collaborationService.delete(tem);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 添加
	 */
	@RequestMapping("/securi_add")
	@ResponseBody
	public Json add(Collaboration tem) {
		Json j = new Json();
		System.out.println(tem);
		try {
			collaborationService.add(tem);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("服务器异常！");
			return j;
		}
		j.setMsg("添加成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 更改
	 */
	@RequestMapping("/securi_update")
	@ResponseBody
	public Json update(HttpServletRequest request) {
		Json j = new Json();
		Collaboration tem = new Collaboration();
		tem.setId(Integer.parseInt(request.getParameter("ththth")));
		tem.setName(request.getParameter("name"));
		tem.setTel(request.getParameter("tel"));
		tem.setIndustry(request.getParameter("industry"));
		tem.setAddress(request.getParameter("address"));
		tem.setPower(request.getParameter("power"));
		tem.setRemark(request.getParameter("remark"));
		tem.setPid(Integer.parseInt(request.getParameter("pid")));
		tem.setSection(request.getParameter("section"));
		try {
			collaborationService.update(tem);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("服务器异常！");
			return j;
		}
		j.setMsg("修改成功！");
		j.setSuccess(true);
		return j;
	}
}
