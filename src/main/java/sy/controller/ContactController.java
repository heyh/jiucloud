package sy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.model.Contact;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.service.ContactServiceI;

@Controller
@RequestMapping("/contactController")
public class ContactController {

	@Autowired
	private ContactServiceI contactService;

	/**
	 * 获取联系人列表信息
	 */
	@RequestMapping("/securi_dataGrid")
	@ResponseBody
	public DataGrid dataGrid(PageHelper ph, HttpServletRequest request) {
		String pid = request.getParameter("pid");
		DataGrid dataGrid = contactService.dataGrid(Integer.parseInt(pid), ph);
		return dataGrid;
	}

	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/securi_toAddPage")
	public String toAddPage(HttpServletRequest request) {
		request.setAttribute("pid", request.getParameter("pid"));
		return "/app/cont/addContact";
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/securi_toEditPage")
	public String toEditPage(int id, HttpServletRequest request) {
		Contact tem = contactService.findoneview(id);
		System.out.println(tem);
		request.setAttribute("detail", tem);
		return "/app/cont/editContact";
	}

	/**
	 * 跳转显示页面
	 */
	@RequestMapping("/securi_toShowPage")
	public String toShowPage(HttpServletRequest request) {
		String pid = request.getParameter("pid");
		System.out.println(pid);
		request.setAttribute("pid", pid);
		return "/app/cont/showContact";
	}

	/**
	 * 跳转预览页面
	 */
	@RequestMapping("/securi_toViewPage")
	public String toViewPage(int id, HttpServletRequest request) {
		Contact tem = contactService.findoneview(id);
		request.setAttribute("detail", tem);
		return "/app/cont/viewContact";
	}

	/**
	 * 删除
	 */
	@RequestMapping("/securi_delete")
	@ResponseBody
	public Json delete(Integer id) {
		Json j = new Json();
		if (id != null) {
			Contact tem = new Contact();
			tem.setId(id);
			contactService.delete(tem);
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
	public Json add(Contact tem) {
		Json j = new Json();
		System.out.println(tem);
		try {
			contactService.add(tem);
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
		Contact tem = new Contact();
		tem.setId(Integer.parseInt(request.getParameter("ththth")));
		tem.setName(request.getParameter("name"));
		tem.setAddress(request.getParameter("address"));
		tem.setJob(request.getParameter("job"));
		tem.setMajor(request.getParameter("major"));
		tem.setPhone(request.getParameter("phone"));
		tem.setQualification(request.getParameter("qualification"));
		tem.setEmail(request.getParameter("email"));
		tem.setPid(Integer.parseInt(request.getParameter("pid")));
		try {
			contactService.update(tem);
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
