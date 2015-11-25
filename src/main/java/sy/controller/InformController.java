package sy.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.InformServiceI;
import sy.service.UserServiceI;
import sy.util.ConfigUtil;
import freemarker.template.Template;

/**
 * **************************************************************** 文件名称 :
 * ApplicationController.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:21:38
 * 文件描述 : Android 服务号通知控制器 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/informController")
public class InformController extends BaseController {

	@Autowired
	private InformServiceI informService;

	@Autowired
	private UserServiceI userService;

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/showInform")
	public String manager() {
		return "/app/inform/showInform";
	}

	/**
	 * 显示当前用户下所有推送消息 方法表述
	 * 
	 * @param id
	 * @return Json
	 */
	@RequestMapping("/getAllInfo")
	@ResponseBody
	public Json getAllInfo(HttpServletRequest request) {
		String fwName = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getName();
		Json j = new Json();
		if (!fwName.startsWith(ConfigUtil.FW_NAME)) {
			return j;
		}
		String infoTitle = request.getParameter("infoTitle");
		List<TInform> list = informService.getAllInfo(fwName, infoTitle);
		String filepath = request.getRequestURL().toString();
		filepath = filepath.substring(0, filepath.indexOf("imforlan"))
				+ "imforlan";
		for (TInform t : list) {
			String picPath = filepath + t.getPicPath();
			t.setPicPath(picPath);
		}
		j.setSuccess(true);
		j.setObj(list);
		return j;
	}

	/**
	 * 根据Id获得info 方法表述
	 * 
	 * @param id
	 * @return Json
	 */
	@RequestMapping("/findInfoById")
	@ResponseBody
	public Json findInfoById(HttpServletRequest request) {
		Json j = new Json();
		String infoId = request.getParameter("informId");
		TInform info = null;
		String filepath = request.getRequestURL().toString();
		filepath = filepath.substring(0, filepath.indexOf("imforlan"))
				+ "imforlan";
		if (infoId != null && !infoId.equals("")) {
			info = informService.findById(infoId);
			info.setSimpleCon(null);
			String picPath = filepath + info.getPicPath();
			info.setPicPath(picPath);
		}
		if (info != null) {
			j.setSuccess(true);
			j.setObj(info);
		}
		return j;
	}

	/**
	 * 获取数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(AppSearch app, PageHelper ph,
			HttpServletRequest request) {
		String fwName = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getName();
		return informService.dataGrid(app, ph, fwName);
	}

	/**
	 * 跳转到添加
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addInformPage")
	public String addPage(HttpServletRequest request) {
		return "/app/inform/informAdd";
	}

	/**
	 * 删除接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteInform")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		if (id != null) {
			informService.delete(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchDeleteInform")
	@ResponseBody
	public Json batchDelete(String ids) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delete(id);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 添加
	 * 
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	@RequestMapping(value = "/addInform", method = RequestMethod.POST)
	@ResponseBody
	public Json add(TInform info, HttpServletRequest request)
			throws ParseException {
		Json j = new Json();
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String fwName = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getName();
		if (!fwName.startsWith(ConfigUtil.FW_NAME)) {
			j.setMsg("只有服务号才能操作...");
			return j;
		}
		String count = request.getParameter("content");
		String isOkTime = request.getParameter("time1");
		if (isOkTime != null && !isOkTime.equals("")) {
			info.setIsoktime(smf.parse(isOkTime));
		}

		info.setContent(count);
		info.setCtime(new Date());// 创建时间
		info.setCreateTime(smf.format(new Date()));
		if (info.getId() > 0) {
			String cdate = request.getParameter("cdate");
			if (!cdate.equals("")) {
				info.setCtime(smf.parse(cdate));
				info.setCreateTime(cdate);
			}
		}

		// String filepath = request.getRequestURL().toString();
		// filepath = filepath.substring(0,
		// filepath.indexOf("imforlan"))+"imforlan";
		info.setPicPath("/upload/pic/" + info.getPicPath());// 设置缩略图路径
		info.setFwName(((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getName());
		User user = new User();
		info.setFwRealname(user.getRealname());
		info.setHtmlPath("/Docs/news/");// 保存html路径前半个路径（在推送后修改）
		Integer id = informService.add(info);
		info.setId(id);
		outInfo(request, info);// 生成静态页面
		if (info.getIsok() == 1) {// 立即发布
			informService.sentFwMsg(id.toString());
		}
		j.setSuccess(true);
		j.setMsg("操作成功！");
		return j;
	}

	/**
	 * 文件上传
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/uploadPic")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			MultipartHttpServletRequest rt) {
		Json j = new Json();
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		String field = rt.getParameter("fileds");
		String file_path = "";
		file_path = grp.getRealPath() + "upload/pic/";
		MultipartFile patch = rt.getFile(field);// 获取图片
		String fileName = patch.getOriginalFilename();// 得到文件名
		if (!patch.isEmpty()) {
			File saveDir = new File(file_path);
			if (!saveDir.exists())
				saveDir.mkdirs();
			try {
				String reg = fileName.substring(
						patch.getOriginalFilename().lastIndexOf(".") + 1)
						.toLowerCase();
				if (!reg.equals("png") && !reg.equals("jpg")) {
					j.setMsg("上传格式错误！请上传png或jpg");
					j.setSuccess(false);
					return j;
				} else {
					patch.transferTo(new File(saveDir + "/" + fileName));
					System.out.println(saveDir + "/" + fileName + "  ...");
					j.setSuccess(true);
					j.setMsg("上传成功!");
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
	 * 跳转到编辑页面
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editInformPage")
	public String editPwdPage(String id, HttpServletRequest request) {
		TInform info = informService.findById(id);
		// String filepath = request.getRequestURL().toString();
		// filepath = filepath.substring(0,
		// filepath.indexOf("imforlan"))+"imforlan";
		String picPath = info.getPicPath().replaceAll("/upload/pic/", "");
		info.setPicPath(picPath);
		request.setAttribute("info", info);
		return "/app/inform/editInformPage";
	}

	/**
	 * 推送
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/grantInform")
	@ResponseBody
	public Json grant(String id, HttpServletRequest request) {
		Json j = new Json();
		String fwName = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getName();
		if (!fwName.startsWith(ConfigUtil.FW_NAME)) {
			j.setMsg("只有服务号才能操作...");
			return j;
		}
		if (id != null) {
			informService.sentFwMsg(id);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		}
		return j;
	}

	/**
	 * 批量推送
	 * 
	 * @param ids
	 *            ('0','1','2')
	 * @return
	 */
	@RequestMapping("/batchgrantInform")
	@ResponseBody
	public Json batchgrantApp(String ids, HttpServletRequest request) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.grant(id, request);
				}
			}
		}
		j.setMsg("批量操作成功！");
		j.setSuccess(true);
		return j;
	}

	// 获得freeMarker
	@Resource
	private FreeMarkerConfigurer freemarkerConfiguration;

	public FreeMarkerConfigurer getFreemarkerConfiguration() {
		return freemarkerConfiguration;
	}

	public void setFreemarkerConfiguration(
			FreeMarkerConfigurer freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	/**
	 * 生成静态 html
	 * 
	 * @param request
	 */
	public void outInfo(HttpServletRequest request, TInform info) {
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Template temp = freemarkerConfiguration.getConfiguration()
					.getTemplate("1.ftl");
			root.put("info", info); // 存入数据
			root.put("contextPath", request.getContextPath());
			root.put("back", "《返回");
			root.put("title", "移动社区");
			String filepath = request.getSession().getServletContext()
					.getRealPath("Docs/news");
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
			}
			Writer out = new OutputStreamWriter(new FileOutputStream(filepath
					+ "/" + info.getId() + ".html"), "UTF-8"); // 写入html
			temp.process(root, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
