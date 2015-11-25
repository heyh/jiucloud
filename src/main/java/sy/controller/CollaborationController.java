package sy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.model.Collaboration;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.service.CollaborationServiceI;

@Controller
@RequestMapping("/collcontroller")
public class CollaborationController {

	@Autowired
	private CollaborationServiceI collaborationService;

	/**
	 * 获取表格数据
	 * @param user
	 * @return
	 */
	@RequestMapping("/securi_dataGrid")
	@ResponseBody
	public DataGrid dataGrid(PageHelper ph, HttpServletRequest request) {
		String pid = request.getParameter("pid");
		DataGrid dataGrid = collaborationService.dataGrid(pid, ph);
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
		System.out.println(tem);
		request.setAttribute("detail", tem);
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
		Collaboration tem = collaborationService.findoneview(id);
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
