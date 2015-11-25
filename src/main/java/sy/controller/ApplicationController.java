package sy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import sy.model.po.TApplication;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.service.ApplicationServiceI;
import sy.service.IMUserServiceI;
import sy.util.Constants;
import sy.util.MD5;


/**
 * ****************************************************************
 * 文件名称 : ApplicationController.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:21:38
 * 文件描述 : Android 轻应用控制器
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/applicationController")
public class ApplicationController extends BaseController {

	@Autowired
	private ApplicationServiceI appService;
	
	@Autowired
	private IMUserServiceI imuserService;
	
	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/showApp")
	public String manager() {
		return "/app/application/showApp";
	}
	
	/**
	 * 获取数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(AppSearch app, PageHelper ph) {
		return appService.dataGrid(app, ph);
	}
	
	/**
	 * 跳转到添加
	 * 
	 * @param request
	 * @return 
	 */
	@RequestMapping("/addAppPage")
	public String addPage() {
		return "/app/application/appAdd";
	}
	
	
	/**
	 * 删除接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteApp")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		if (id != null) {
			appService.delete(id);
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
	@RequestMapping("/batchDeleteApp")
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
	@RequestMapping("/addApp")
	@ResponseBody
	public Json add(TApplication info,HttpServletRequest request) throws ParseException{
		Json j = new Json();
		if(info.getId()>0){
			String retime =request.getParameter("rTime");
			if(retime!=""){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				info.setRepTime(sdf.parse(retime));
			}
		}
		if(info.getIsRep()==1){//立即发布
			info.setRepTime(new Date());
		}
//		String filepath = request.getRequestURL().toString();
//		filepath = filepath.substring(0, filepath.indexOf("imforlan"))+"imforlan";
		info.setIcon("/upload/icon/"+info.getIcon());
		info.setUpPath("/upload/apk/"+info.getUpPath()); 
		appService.add(info);
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
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Json uploadhb( HttpSession session,HttpServletRequest req,MultipartHttpServletRequest rt) {
		Json j = new Json();
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession().getServletContext());
		String field = rt.getParameter("fileds");
		String file_path ="";
		if(field.equals("pic")){
			file_path= grp.getRealPath()+"upload/icon/";
		}else if(field.equals("apk")){
			file_path= grp.getRealPath()+"upload/apk/";
		}
		MultipartFile patch = rt.getFile(field);//获取图片
        String fileName=patch.getOriginalFilename();//得到文件名
		if(!patch.isEmpty()){
			File saveDir = new File(file_path);
			if(!saveDir.exists())
				saveDir.mkdirs();
			try{
				String reg = fileName.substring(patch.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
				boolean isok = false;
				if(field.equals("pic")){
					if(!reg.equals("png")&&!reg.equals("jpg")){
						j.setMsg("上传格式错误！请上传png或jpg");
						isok=true;
					}
				}else{
					if(!reg.equals("apk")){
						j.setMsg("上传格式错误！请上传apk");
						isok=true;
					}
				}
				if(isok){
					j.setSuccess(false);
					return j;
				}else{
					patch.transferTo(new File(saveDir+"/"+fileName));
					System.out.println(saveDir+"/"+fileName+"  ...");
				//	this.musicService.updateMusic(field,id,fileName);
					req.setAttribute("fileName", fileName);
					j.setSuccess(true);
					j.setMsg("上传成功!");
					return j;
				}
			}catch(Exception ex){
				j.setSuccess(false);
				j.setMsg("服务器出错");
				return j;
			}
		}else{
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
	@RequestMapping("/editAppPage")
	public String editPwdPage(String id, HttpServletRequest request) {
		TApplication app = appService.findById(id);
//		String filepath = request.getRequestURL().toString();
//		filepath = filepath.substring(0, filepath.indexOf("imforlan"))+"imforlan";
		String icon = app.getIcon().replaceAll("/upload/icon/", "");
		String upPath = app.getUpPath().replaceAll("/upload/apk/", "");
		app.setIcon(icon);
		app.setUpPath(upPath);
		request.setAttribute("app", app);
		return "/app/application/editAppPage";
	}
	
	/**
	 * 发布
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/grantApp")
	@ResponseBody
	public Json grant(String id) {
		Json j = new Json();
		if(id!=null){
			appService.updateIsRep(id);
			j.setSuccess(true);
			j.setMsg("审核成功！");
		}
		return j;
	}
	
	/**
	 * 批量发布
	 * 
	 * @param ids
	 *            ('0','1','2')
	 * @return
	 */
	@RequestMapping("/batchgrantApp")
	@ResponseBody
	public Json batchgrantApp(String ids) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.grant(id);
				}
			}
		}
		j.setMsg("批量审核成功！");
		j.setSuccess(true);
		return j;
	}
	
	
	/**
	 * 获得全部应用列表
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findApp")
	public Json securi_findApp(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String page = o.getString("curpage");
			String rows = o.getString("rows");
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			// 分页显示所有应用
			List<TApplication> list = appService.findAllApp(Integer.parseInt(page), Integer.parseInt(rows));
			j.setSuccess(true);
			j.setCode(2000);
			j.setMsg("查询成功");
			j.setObj(list);
			return j;
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setMsg("参数非法...");
			j.setSuccess(false);
			j.setObj(null);
			j.setCode(1002);
			return j;
		}
	}
	
	
	/**
	 * 获得指定应用信息
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findAppById")
	public Json securi_findAppById(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String appId = o.getString("appId");
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			
			TApplication app = appService.findById(appId);
			if (app != null) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(app);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("没有数据");
				return j;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setMsg("参数非法...");
			j.setSuccess(false);
			j.setObj(null);
			j.setCode(1002);
			return j;
		}
	}
	
	public static void main(String[] args) {
		
	}
}
