package sy.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import sy.model.IMUser;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SynIMUser;
import sy.service.IMUserServiceI;
import sy.util.ConfigUtil;
import sy.util.MD5;

/**
 * ****************************************************************
 * 文件名称 : IMUserController.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:21:38
 * 文件描述 : IMUser 控制器
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/api/imuser")
public class IMUserController extends BaseController {

	@Autowired
	private IMUserServiceI imuserService;
	
	//获得token 同步操作
	private final String token = SynchronizationController.getToken();
	//同步返回错误信息
	private boolean toError(Integer code,Json j){
		boolean isOk = true;
		if(code==400){
			System.out.println("用户已存在、用户名或密码为空、用户名不合法");
			j.setMsg("用户已存在、用户名或密码为空、用户名不合法");
			isOk = false;
		}
		if(code==401){
			System.out.println("未授权[无token,token错误,token过期]");
			j.setMsg("未授权[无token,token错误,token过期]");
			isOk = false;
		}
		if(code==404){
			System.out.println("用户不存在");
			j.setMsg("用户不存在");
			isOk = false;
		}
		return isOk;
	}

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/showIMuser")
	public String manager() {
		return "/admin/imuser/showImuser";
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
		return imuserService.dataGrid(app, ph);
	}
	
	/**
	 * 跳转到添加
	 * 
	 * @param request
	 * @return 
	 */
	@RequestMapping("/addIMPage")
	public String addPage() {
		return "/admin/imuser/addIMUser";
	}
	
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteIM")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		if (id != null) {
			IMUser user = imuserService.findById(id);
			imuserService.delete(id);
			j.setSuccess(true);
			//同步删除
			Integer code = SynchronizationController.deleteIMUser(user.getUsername(), token);
			if(!toError(code,j)){
				return j;
			}
		}
		
		j.setMsg("删除成功！");
		return j;
	}
	
	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchDeleteIM")
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
	 * 批量同步
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/synIM")
	@ResponseBody
	public Json synIM(String ids) {
		Json j = new Json();
		List<SynIMUser> u = new ArrayList<SynIMUser>();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					IMUser user = imuserService.findById(id);
					SynIMUser im = new SynIMUser(user.getUsername(), "111111", user.getRelname());//密码统一同步
					u.add(im);
				}
			}
		}
		//批量同步添加																								 
		Integer code = SynchronizationController.saveIMUser(u, token);
		if(!toError(code,j)){
			return j;
		}
		
		for (String id : ids.split(",")) {
			if (id != null) {
				imuserService.updateIsSyn(id);//修改isSyn=1
			}
		}
		j.setMsg("批量同步成功！");
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
	@RequestMapping("/addIM")
	@ResponseBody
	public Json add(IMUser user,HttpServletRequest request) throws ParseException{
		Json j = new Json();
		user.setCtime(new Date());//创建时间
		String pass = "";
		if(user.getId()>0){
			String cdate =request.getParameter("cdate");
			if(cdate!=""){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				user.setCtime(sdf.parse(cdate));
			}
			//同步  修改昵称
			Integer code = SynchronizationController.putIMNickname(user.getUsername(), user.getRelname(), token);
			if(!toError(code,j)){
				return j;
			}	
		}else{
			//同步添加																								 
			Integer code = SynchronizationController.saveIMUser(user.getUsername(), "111111",user.getRelname(), token);//密码统一同步
			if(!toError(code,j)){
				return j;
			}
		}
//		String filepath = request.getRequestURL().toString();
//		filepath = filepath.substring(0, filepath.indexOf("imforlan"))+"imforlan";
		user.setUariva("/upload/uariva/"+user.getUariva());//设置头像路径 uariva
		user.setIsSyn(1);
		if(user.getPass().equals("******")){
		  pass = request.getParameter("pp");
		}else{
			pass = MD5.encodePassword(user.getPass());
		}
		user.setPass(pass);
		imuserService.add(user);
		j.setSuccess(true);
		j.setMsg("操作成功！");
		return j;
	}
	
	/**
	 * 上传头像
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/uploaduariva")
	@ResponseBody
	public Json uploadhb( HttpSession session,HttpServletRequest req,MultipartHttpServletRequest rt) {
		Json j = new Json();
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession().getServletContext());
		String field = rt.getParameter("fileds");
		String file_path ="";
		file_path= grp.getRealPath()+"upload/uariva/";
		MultipartFile patch = rt.getFile(field);//获取图片
        String fileName=patch.getOriginalFilename();//得到文件名
		if(!patch.isEmpty()){
			File saveDir = new File(file_path);
			if(!saveDir.exists())
				saveDir.mkdirs();
			try{
				String reg = fileName.substring(patch.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
				if(!reg.equals("png")&&!reg.equals("jpg")){
					j.setMsg("上传格式错误！请上传png或jpg");
					j.setSuccess(false);
					return j;
				}else{
					patch.transferTo(new File(saveDir+"/"+fileName));
					System.out.println(saveDir+"/"+fileName+"  ...");
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
	@RequestMapping("/editIMPage")
	public String editPwdPage(String id, HttpServletRequest request) {
		IMUser app = imuserService.findById(id);
//		String filepath = request.getRequestURL().toString();
//		filepath = filepath.substring(0, filepath.indexOf("imforlan"))+"imforlan";
		String uariva = app.getUariva().replaceAll("/upload/uariva/", "");
		app.setUariva(uariva);
		request.setAttribute("imuser", app);
		return "/admin/imuser/editImPage";
	}
	
	
	/**
	 * 查看所有好友信息
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findAllFriend")
	public Json findAllFriend(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			// 查看好友信息
			JSONArray arr = SynchronizationController.getFriend(username, SynchronizationController.getToken());
			List<IMUser> list = new ArrayList<IMUser>();
			if(arr.size()>0){
				for (int i = 0; i < arr.size(); i++) {
					if(arr.getString(i).indexOf(ConfigUtil.FW_NAME)>-1){
						continue;
					}
					IMUser u = imuserService.findIMUserInfoByName(arr.get(i).toString());
					list.add(u);
				}
			}
			
			if (list.size()>0) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(list);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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
	
	/**
	 * 查看所有通讯录信息
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findAll")
	public Json findAll(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
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
			List<SynIMUser> list = imuserService.findAll(username);
			if (list.size()>0) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(list);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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
	
	/**
	 * 查看个人资料
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findIMUserInfo")
	public Json findIMUserInfo(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			//登录验证
			IMUser vu = imuserService.findPuserByNameAndPwd(username, pwd);
			if (vu != null) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(vu);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("查询失败");
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
	
	/**
	 * 查看好友信息?????
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findIMUserInfoByName")
	public Json findIMUserInfoByName(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String relname = o.getString("relname");//好友昵称
			IMUser vu = new IMUser();
			vu.setUsername(username);
			vu.setPass(pwd);
			// 查看好友信息
			JSONArray arr = SynchronizationController.getFriend(username, SynchronizationController.getToken());
			if(arr.size()>0){
				for (int i = 0; i < arr.size(); i++) {
					if(arr.get(i).equals(relname)){
						break;
					}else{
						j.setSuccess(false);
						j.setCode(1002);
						j.setMsg("没有这个好友...");
						return j;
					}
				}
			}
			IMUser u = imuserService.findIMUserInfoByName(relname);
			if (u!=null) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(u);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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

	/**
	 * 修改用户姓名(昵称)
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_updateRelName")
	public Json updateRelName(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String relname = o.getString("relname");
			IMUser vu = new IMUser();
			vu.setUsername(username);
			vu.setPass(pwd);
			vu.setRelname(relname);
			//同步  修改昵称
			Integer code = SynchronizationController.putIMNickname(username, relname, token);
			if(!toError(code,j)){
				return j;
			}	
			// 根据用户、密码和姓名修改姓名
			boolean isUpdate = imuserService.updateRelName(vu);
			if (isUpdate) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("修改成功");
				j.setObj(null);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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
	
	/**
	 * 修改用户性别
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_updateSex")
	public Json updateSex(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String sex = o.getString("sex");
			IMUser vu = new IMUser();
			vu.setUsername(username);
			vu.setPass(pwd);
			vu.setSex(Integer.parseInt(sex));
			// 根据手机号码、密码、性别修改性别
			boolean isUpdate = imuserService.updateSex(vu);
			if (isUpdate) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("修改成功");
				j.setObj(null);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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
	/**
	 * 修改用户密码
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_updatePwd")
	public Json updatePwd(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String newPwd = o.getString("newPwd");
			IMUser vu = new IMUser();
			vu.setUsername(username);
			vu.setPass(pwd);
			// 根据手机号码、密码
			boolean isUpdate = imuserService.updatePwd(vu,MD5.encodePassword(newPwd));
			if (isUpdate) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("修改成功");
				j.setObj(null);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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
	/**
	 * 修改省份城市区县
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_updateArea")
	public Json updateArea(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String province = o.getString("province");
			String city = o.getString("city");
			String district = o.getString("district");
			IMUser vu = new IMUser();
			vu.setUsername(username);
			vu.setPass(pwd);
			vu.setProvinceName(province);
			vu.setCityName(city);
			vu.setDistrictName(district);
			// 根据手机号码、密码、省份城市修改省份城市
			boolean isUpdate = imuserService.updateArea(vu);
			if (isUpdate) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("修改成功");
				j.setObj(null);
				return j;
			} else {
				j.setSuccess(false);
				j.setCode(1004);
				j.setMsg("修改失败");
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

	/**
	 * 上传头像
	 * 
	 * @param req
	 * @param rt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_uploadUariva")
	public Json uploadUariva(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession().getServletContext());
			MultipartHttpServletRequest rt = (MultipartHttpServletRequest)req;
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
//			String imagename = rt.getParameter("imagename");

			IMUser v = imuserService.findPuserByNameAndPwd(username, pwd);
			if(v == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String file_path = grp.getRealPath() + "/upload/uariva/";
			MultipartFile patch = rt.getFile("uariva");// 获取图片
			String fileName=patch.getOriginalFilename();//得到文件名
//			String fileName = imagename + ".jpg";
			if (!patch.isEmpty()) {
				File saveDir = new File(file_path);
				if (!saveDir.exists())
					saveDir.mkdirs();
				String reg = patch.getOriginalFilename().substring(patch.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
				if (!reg.equals("png")&&!reg.equals("jpg")) {
					j.setMsg("上传格式错误！请上传png或jpg");
					j.setSuccess(false);
					j.setCode(1003);
					return j;
				} else {
					patch.transferTo(new File(saveDir + "/" + fileName));
					System.out.println(saveDir + "/" + fileName + "  ...");
				
//					String filepath = req.getRequestURL().toString();
//					filepath = filepath.substring(0, filepath.indexOf("imforlan"))+"imforlan";
					String path = "/upload/uariva/"+fileName;//设置头像路径 uariva
					
					this.imuserService.updateUariva(v.getId()+"", path);
//					String path = "upload/uariva/"+ v.getId() + "/" + fileName;
					j.setSuccess(true);
					j.setMsg("上传成功");
					j.setCode(2000);
					j.setObj(path);
					return j;
				}
			} else {
				j.setMsg("上传文件不存在");
				j.setSuccess(false);
				j.setCode(1005);
				j.setObj(null);
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

	/**
	 * 检验账号密码
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_validateLogin")
	public Json validateLogin(HttpServletRequest req) {
		Json j = sy.util.Constant.convertJson(req);
		if (!j.isSuccess()) {
			return j;
		}
		try {
			JSONObject o = (JSONObject) j.getObj();
			j.setObj(null);
			String username = o.getString("username");
			String pwd = o.getString("pwd");
			pwd=MD5.encodePassword(pwd);//加密
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }else{
		    	j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("账号密码正确");
				j.setObj(null);
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
	
}
