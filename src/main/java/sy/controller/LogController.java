package sy.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sy.model.po.Tlog;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.LogServiceI;
import sy.util.ConfigUtil;

/**
 * 日志控制器
 * 
 */
@Controller
@RequestMapping("/rzglController")
public class LogController {

	@Autowired
	private LogServiceI logServiceI;

	/**
	 * 跳转日志管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/rzglmanager")
	public String manager() {
		return "/app/rzgl/rzglManager";
	}

	/**
	 * 查询所有日志
	 * 
	 * DataGrid
	 */
	@RequestMapping("/searchrz")
	@ResponseBody
	public DataGrid searchrz(HttpServletRequest req, PageHelper ph,
			HttpSession session) {
		String stime = req.getParameter("beginTime");
		String etime = req.getParameter("endTime");
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		List<Integer> ugroup = sessionInfo.getUgroup();
		return logServiceI.dataGrid(stime, etime, ugroup, ph);
	}

	/**
	 * 跳转到添加日志页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addrzPage")
	public String addrzPage(HttpServletRequest request) {
		return "/app/rzgl/rzAddPage";
	}

	/**
	 * 添加日志
	 * 
	 * @return
	 */
	@RequestMapping("/addrz")
	@ResponseBody
	public Json addrz(HttpServletRequest req, HttpSession session,
			String content) {
		Json j = new Json();
		Tlog t = new Tlog();
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		t.setTitle(req.getParameter("title"));
		t.setContent(req.getParameter("content"));
		t.setAttachment(req.getParameter("picUrl"));
		t.setCtime(new Date());
		t.setUids(sessionInfo.getId());
		try {
			logServiceI.add(t);
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
		String file_path = grp.getRealPath() + "/upload/rzgl/";
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
					fileName = "upload/rzgl/" + fileName;
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
	 * 跳转到日志查看页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/searchrzPage")
	public String searchrzPage(HttpServletRequest req, String id) {
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		sy.pageModel.Log t = logServiceI.findById(id);
		t.setAttachment(grp.getRealPath() + t.getAttachment());
		req.setAttribute("t", t);
		return "/app/rzgl/searchrz";
	}

}
