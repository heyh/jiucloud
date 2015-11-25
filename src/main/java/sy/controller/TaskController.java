package sy.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sy.model.S_department;
import sy.model.po.Ttask;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.TaskServiceI;
import sy.util.ConfigUtil;

/**
 * 任务控制器
 * 
 */
@Controller
@RequestMapping("/rwglController")
public class TaskController {

	@Autowired
	private TaskServiceI taskServiceI;

	/**
	 * 跳转任务管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/rwglmanager")
	public String manager() {
		return "/app/rwgl/rwglManager";
	}

	/**
	 * 查询所有任务
	 * 
	 * DataGrid
	 */
	@RequestMapping("/searchrw")
	@ResponseBody
	public DataGrid searchrw(HttpServletRequest req, PageHelper ph,
			HttpSession session) {
		String stime = req.getParameter("beginTime");
		String etime = req.getParameter("endTime");
		String principal = req.getParameter("principal");
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		List<Integer> ugroup = sessionInfo.getUgroup();
		return taskServiceI.dataGrid(stime, etime, ugroup, principal, ph);
	}

	/**
	 * 跳转到添加任务页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addrwPage")
	public String addrwPage(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		List<S_department> list = taskServiceI.getDepsByCompanyId(Integer
				.parseInt(cid));
		request.setAttribute("departments", list);
		return "/app/rwgl/rwAddPage";
	}

	@RequestMapping("/securi_getUsers")
	@ResponseBody
	public List<User> getCities(HttpServletRequest req) {
		String id = req.getParameter("id");
		List<User> list = taskServiceI.getUserByDepId(Integer.parseInt(id));
		System.out.println(list);
		req.setAttribute("users", list);
		return list;
	}

	/**
	 * 添加任务
	 * 
	 * @return
	 */
	@RequestMapping("/addrw")
	@ResponseBody
	public Json addrw(HttpServletRequest req, HttpSession session,
			String content) {
		Json j = new Json();
		if (req.getParameter("beginTime") == null
				|| req.getParameter("beginTime").length() == 0) {
			j.setMsg("开始时间必须填写");
			return j;
		}
		if (req.getParameter("endTime") == null
				|| req.getParameter("endTime").length() == 0) {
			j.setMsg("结束时间必须填写");
			return j;
		}
		if (req.getParameter("department") == null
				|| req.getParameter("department").length() == 0
				|| Integer.parseInt(req.getParameter("department")) == 0) {
			j.setMsg("请选择部门");
			return j;
		}
		if (req.getParameter("title") == null
				|| req.getParameter("title").length() == 0) {
			j.setMsg("标题必须填写");
			return j;
		}
		if (req.getParameter("principal") == null
				|| req.getParameter("principal").length() == 0) {
			j.setMsg("请选择负责人");
			return j;
		}
		Ttask t = new Ttask();
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		t.setTitle(req.getParameter("title"));
		t.setContent(req.getParameter("content"));
		t.setAttachment(req.getParameter("picUrl"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			t.setStime(sdf.parse(req.getParameter("beginTime")));
			t.setEtime(sdf.parse(req.getParameter("endTime")));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		t.setDepartment(Integer.parseInt(req.getParameter("department")));
		t.setPids(req.getParameter("principal"));
		t.setUids(sessionInfo.getId());
		t.setSender(sessionInfo.getName());
		try {
			taskServiceI.add(t);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 附件上传
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/securi_uploadfj")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			MultipartHttpServletRequest rt) {

		Json j = new Json();

		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		// String fileid = Constants.getimageId(1);//随机生成文件 id
		String field = rt.getParameter("fileds");
		String file_path = grp.getRealPath() + "/upload/rwgl/";
		MultipartFile patch = rt.getFile(field);// 获取图片
		// String str = patch.getOriginalFilename();
		// str = str.substring(str.lastIndexOf("."));
		// String fileName = fileid+str;
		String fileName = patch.getOriginalFilename();
		if (!patch.isEmpty()) {
			File saveDir = new File(file_path);
			if (!saveDir.exists())
				saveDir.mkdirs();
			try {
				String reg = patch
						.getOriginalFilename()
						.substring(
								patch.getOriginalFilename().lastIndexOf(".") + 1)
						.toLowerCase();
				if (!reg.equals("jpg") && !reg.equals("png")
						&& !reg.equals("gif") && !reg.equals("doc")
						&& !reg.equals("xls") && !reg.equals("docx")
						&& !reg.equals("xlsx") && !reg.equals("txt")) {
					j.setMsg("上传格式错误！");
					j.setSuccess(false);
					return j;
				} else {
					patch.transferTo(new File(saveDir + "/" + fileName));
					System.out.println(saveDir + "/" + fileName + "  ...");
					j.setSuccess(true);
					fileName = "upload/rwgl/" + fileName;
					j.setMsg("pic1," + fileName);
					return j;
				}
			} catch (Exception ex) {
				j.setSuccess(false);
				j.setMsg("服务器出错");
				return j;
			}
		} else {
			j.setMsg("文件上传为空");
			j.setSuccess(false);
			return j;
		}

	}

	/**
	 * 跳转到任务查看页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editrwPage")
	public String editrwPage(HttpServletRequest req, String id) {
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		sy.pageModel.Task t = taskServiceI.findById(id);
		t.setAttachment(grp.getRealPath() + t.getAttachment());
		String fsuffix = (t.getStime() == null || t.getStime().toString()
				.length() == 0) ? "未定义" : t.getStime().toString()
				.substring(0, 19);
		String tsuffix = (t.getEtime() == null || t.getEtime().toString()
				.length() == 0) ? "未定义" : t.getEtime().toString()
				.substring(0, 19);
		t.setTime(fsuffix + "-" + tsuffix);
		req.setAttribute("t", t);
		return "/app/rwgl/editrwPage";
	}

	/**
	 * 完成任务
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editrw")
	@ResponseBody
	public Json editrw(HttpServletRequest req) {
		Json j = new Json();
		String id = req.getParameter("id");
		try {
			taskServiceI.updateStatus(id);
			j.setSuccess(true);
			j.setMsg("操作成功!");
			j.setObj(null);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 跳转到任务评论页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editrwBycommentPage")
	public String editrwBycommentPage(HttpServletRequest req, String id) {
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		sy.pageModel.Task t = taskServiceI.findById(id);
		t.setAttachment(grp.getRealPath() + t.getAttachment());
		String fsuffix = (t.getStime() == null || t.getStime().toString()
				.length() == 0) ? "未定义" : t.getStime().toString()
				.substring(0, 19);
		String tsuffix = (t.getEtime() == null || t.getEtime().toString()
				.length() == 0) ? "未定义" : t.getEtime().toString()
				.substring(0, 19);
		t.setTime(fsuffix + "-" + tsuffix);
		req.setAttribute("t", t);
		return "/app/rwgl/commentPage";
	}

	/**
	 * 评论任务
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editrwBycomment")
	@ResponseBody
	public Json editrwBycomment(HttpServletRequest req) {
		Json j = new Json();
		String id = req.getParameter("id");
		String comment = req.getParameter("comments");
		try {
			taskServiceI.updateComment(id, comment);
			j.setSuccess(true);
			j.setMsg("操作成功!");
			j.setObj(null);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
