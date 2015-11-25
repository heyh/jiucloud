package sy.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.S_department;
import sy.model.po.Company;
import sy.model.po.Department;
import sy.pageModel.*;
import sy.service.*;
import sy.util.ConfigUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * **************************************************************** 文件名称 :
 * UserController.java 作 者 : Administrator 创建时间 : 2014年12月26日 上午8:55:13 文件描述 :
 * 用户控制器 版权声明 : 修改历史 : 2014年12月26日 1.00
 *****************************************************************
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

	@Autowired
	private UserServiceI userService;

	@Autowired
	private RoleServiceI roleService;

	@Autowired
	private ResourceServiceI resourceService;

	@Autowired
	private IMUserServiceI imuserService;

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private CompanyServiceI companyService;

	@Autowired
	private DepartmentServiceI departmentService;

	// 获得token 同步操作
	private final String token = SynchronizationController.getToken();

	// 同步返回错误信息
	private boolean toError(Integer code, Json j) {
		boolean isOk = true;
		if (code == 400) {
			System.out.println("用户已存在、用户名或密码为空、用户名不合法");
			j.setMsg("用户已存在、用户名或密码为空、用户名不合法");
			isOk = false;
		}
		if (code == 401) {
			System.out.println("未授权[无token,token错误,token过期]");
			j.setMsg("未授权[无token,token错误,token过期]");
			isOk = false;
		}
		if (code == 404) {
			System.out.println("用户不存在了");
			j.setMsg("用户不存在了");
			isOk = false;
		}
		return isOk;
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 *            用户对象
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Json login(HttpSession session, HttpServletRequest request) {
		Json j = new Json();
		System.out.println(request.getParameter("name"));
		System.out.println(request.getParameter("pwd"));
		System.out.println(request.getParameter("id"));
		System.out.println(request.getParameter("cid"));
		User u = null;
		String cid = null;
		try {
			if (request.getParameter("id") != null
					&& !("null".equals(request.getParameter("id")))) {
				u = userService.getUser(request.getParameter("id"));
				System.out.println(111);
			} else {
				u = userService.login(request.getParameter("name"),
						request.getParameter("pwd"));
			}
			if(request.getParameter("cid")!= null
					&& !("null".equals(request.getParameter("cid")))){
				cid = request.getParameter("cid");
			}
			System.out.println("User:" + u);
			List<Integer> dgroup = null;
			List<Integer> ugroup = null;
			if (u != null) {
				j.setSuccess(true);
				j.setMsg("登陆成功！");
				//查询公司名称和id
				Company c = companyService.findOneView(u.getId(),cid);
				System.out.println("Company:" + c);
				if (c == null) {
					j.setSuccess(false);
					j.setMsg("您好，您没有登录权限，请联系管理员订购该业务");
					return j;
				}
				//根据用户id查询部门id和名称,超管不属于任何部门所以是null
				S_department s = departmentService
						.getDepartmentByUid(u.getId(),cid);
				System.out.println(s);
				//根据用户id查询所有职位信息
				Department d = departmentService.findOneView(u.getId(),cid);
				System.out.println("Department:" + d);
				ugroup = departmentService.getUserGroup(d, u.getId(),cid);
				System.out.println(ugroup);
				SessionInfo sessionInfo = new SessionInfo();
				if (u.getId().equals(u.getCorporation_creater())) {
					sessionInfo.setIsadmin(1);
				}
                // add by heyh
                sessionInfo.setUsername(u.getUsername());
				sessionInfo.setName(u.getRealname());
				sessionInfo.setId(u.getId());
				sessionInfo.setResourceList(userService.resourceList(
						String.valueOf(u.getId()), sessionInfo.getIsadmin()));
				sessionInfo.setCompid(String.valueOf(c.getId()));
				sessionInfo.setCompName(c.getName());
				if(d!=null){
					sessionInfo.setDepid(String.valueOf(d.getId()));
					sessionInfo.setDepName(d.getName());
                    sessionInfo.setParentId(d.getParent_id());
				}else{
					sessionInfo.setDepid(null);
					sessionInfo.setDepName(null);
				}
				sessionInfo.setDepartment_id(String.valueOf(s.getId()));
				sessionInfo.setDepartment_name(s.getName());
				sessionInfo.setDgroup(dgroup);
				sessionInfo.setUgroup(ugroup);
				session.setAttribute(ConfigUtil.getSessionInfoName(),
						sessionInfo);
				System.out.println(sessionInfo);
				j.setObj(sessionInfo);
			} else if (u == null) {
				j.setMsg("用户名或密码错误！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setSuccess(false);
			j.setMsg("服务器异常！！！");

		}
		return j;
	}

	/**
	 * 判断是否是服务号 方法表述
	 * 
	 * @param u
	 * @return User
	 */
	// private User isFw(User u) {
	// User user = new User();
	// user.setName(ConfigUtil.FW_NAME + u.getName());
	// user.setPwd(u.getPwd());
	// return userService.login(user);
	// }

	// /**
	// * 用户注册
	// *
	// * @param user
	// * 用户对象
	// * @return
	// */
	// @ResponseBody
	// @RequestMapping("/reg")
	// public Json reg(User user) {
	// Json j = new Json();
	// try {
	// userService.reg(user);
	// j.setSuccess(true);
	// j.setMsg("注册成功！新注册的用户没有任何权限，请让管理员赋予权限后再使用本系统！");
	// j.setObj(user);
	// } catch (Exception e) {
	// // e.printStackTrace();
	// j.setMsg(e.getMessage());
	// }
	// return j;
	// }

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

	/**
	 * 跳转到用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/user";
	}

	/**
	 * 获取用户数据表格
	 * 
	 * @param user
	 * @return
	 */
	// @RequestMapping("/dataGrid")
	// @ResponseBody
	// public DataGrid dataGrid(User user, PageHelper ph) {
	// return userService.dataGrid(user, ph);
	// }

	/**
	 * 跳转到添加用户页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		User u = new User();
		u.setId(UUID.randomUUID().toString());
		request.setAttribute("user", u);
		return "/admin/userAdd";
	}

	/**
	 * 添加用户
	 * 
	 * @return
	 */
	// @RequestMapping("/add")
	// @ResponseBody
	// public Json add(User user, HttpServletRequest request) {
	// Json j = new Json();
	// try {
	// if (user.getIsFw() == 1) {
	// user.setName(ConfigUtil.FW_NAME + user.getName());
	// // 同步添加
	// Integer code = SynchronizationController.saveIMUser(
	// user.getName(), MD5Util.md5(user.getPwd()),
	// user.getRealname(), token);
	// if (!toError(code, j)) {
	// return j;
	// }
	// }
	// // String filepath = request.getRequestURL().toString();
	// // filepath = filepath.substring(0,
	// // filepath.indexOf("imforlan"))+"imforlan";
	// user.setUariva("/upload/uariva/" + user.getUariva());// 设置头像路径
	// // uariva
	// userService.add(user);
	// j.setSuccess(true);
	// j.setMsg("添加成功！");
	// j.setObj(user);
	// } catch (Exception e) {
	// j.setMsg(e.getMessage());
	// }
	// return j;
	// }

	/**
	 * 跳转到用户修改页面
	 * 
	 * @return
	 */
	// @RequestMapping("/editPage")
	// public String editPage(HttpServletRequest request, String id) {
	// User u = userService.get(id);
	// if (u.getu().startsWith(ConfigUtil.FW_NAME)) {
	// String name = u.getUser_name().replaceAll(ConfigUtil.FW_NAME, "");
	// u.setUser_name(name);
	// u.setIsFw(1);
	// }
	// if (u.getUariva() != null) {
	// // String filepath = request.getRequestURL().toString();
	// // filepath = filepath.substring(0,
	// // filepath.indexOf("imforlan"))+"imforlan";
	// String picPath = u.getUariva().replaceAll("/upload/uariva/", "");
	// u.setUariva(picPath);
	// }
	// request.setAttribute("user", u);
	// return "/admin/userEdit";
	// }

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	// @RequestMapping("/edit")
	// @ResponseBody
	// public Json edit(User user, HttpServletRequest request) {
	// Json j = new Json();
	// try {
	// if (user.getIsFw() == 1) {
	// user.setUser_name((ConfigUtil.FW_NAME + user.getUser_name()));
	// // 同步 修改昵称
	// Integer code = SynchronizationController.putIMNickname(
	// user.getUser_name(), user.getUser_realname(), token);
	// if (!toError(code, j)) {
	// return j;
	// }
	// }
	// // String filepath = request.getRequestURL().toString();
	// // filepath = filepath.substring(0,
	// // filepath.indexOf("imforlan"))+"imforlan";
	// user.setUariva("/upload/uariva/" + user.getUariva());// 设置头像路径
	// // uariva
	// // userService.edit(user);
	// j.setSuccess(true);
	// j.setMsg("编辑成功！");
	// j.setObj(user);
	// } catch (Exception e) {
	// // e.printStackTrace();
	// j.setMsg(e.getMessage());
	// }
	// return j;
	// }

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	// @RequestMapping("/delete")
	// @ResponseBody
	// public Json delete(String id, HttpSession session) {
	// SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
	// .getSessionInfoName());
	// Json j = new Json();
	// if (id != null && !id.equalsIgnoreCase(sessionInfo.getId())) {// 不能删除自己
	// User user = userService.get(id);
	// // userService.delete(id);
	// j.setSuccess(true);
	// if (user.getUser_name().startsWith(ConfigUtil.FW_NAME)) {
	// // 同步删除
	// Integer code = SynchronizationController.deleteIMUser(
	// user.getUser_name(), token);
	// if (!toError(code, j)) {
	// return j;
	// }
	// }
	// }
	// j.setMsg("删除成功！");
	// return j;
	// }

	/**
	 * 批量删除用户
	 * 
	 * @param ids
	 *            ('0','1','2')
	 * @return
	 */
	// @RequestMapping("/batchDelete")
	// @ResponseBody
	// public Json batchDelete(String ids, HttpSession session) {
	// Json j = new Json();
	// if (ids != null && ids.length() > 0) {
	// for (String id : ids.split(",")) {
	// if (id != null) {
	// this.delete(id, session);
	// }
	// }
	// }
	// j.setMsg("批量删除成功！");
	// j.setSuccess(true);
	// return j;
	// }

	/**
	 * 跳转到用户授权页面
	 * 
	 * @return
	 */
	@RequestMapping("/grantPage")
	public String grantPage(String ids, HttpServletRequest request) {
		request.setAttribute("ids", ids);
		if (ids != null && !ids.equalsIgnoreCase("") && ids.indexOf(",") == -1) {
			User u = userService.get(ids);
			request.setAttribute("user", u);
		}
		return "/admin/userGrant";
	}

	// /**
	// * 用户授权
	// *
	// * @param ids
	// * @return
	// */
	// @RequestMapping("/grant")
	// @ResponseBody
	// public Json grant(String ids, User user) {
	// Json j = new Json();
	// userService.grant(ids, user);
	// j.setSuccess(true);
	// j.setMsg("授权成功！");
	// return j;
	// }

	/**
	 * 跳转到编辑用户密码页面
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPwdPage")
	public String editPwdPage(String id, HttpServletRequest request) {
		User u = userService.get(id);
		request.setAttribute("user", u);
		return "/admin/userEditPwd";
	}

	/**
	 * 编辑用户密码
	 * 
	 * @param user
	 * @return
	 */
	// @RequestMapping("/editPwd")
	// @ResponseBody
	// public Json editPwd(User user) {
	// Json j = new Json();
	// if (user.getUser_name().startsWith(ConfigUtil.FW_NAME)) {
	// // 同步修改密码
	// Integer code = SynchronizationController.putIMUserPwd(
	// user.getUser_name(), MD5Util.md5(user.getUser_pw()), token);
	// if (!toError(code, j)) {
	// return j;
	// }
	// }
	// // userService.editPwd(user);
	// j.setSuccess(true);
	// j.setMsg("编辑成功！");
	// return j;
	// }

	/**
	 * 跳转到编辑自己的密码页面
	 * 
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwdPage")
	public String editCurrentUserPwdPage() {
		return "/user/userEditPwd";
	}

	/**
	 * 修改自己的密码
	 * 
	 * @param session
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwd")
	@ResponseBody
	public Json editCurrentUserPwd(HttpSession session, String oldPwd,
			String pwd) {
		Json j = new Json();
		if (session != null) {
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(ConfigUtil.getSessionInfoName());
			if (sessionInfo != null) {

			} else {
				j.setMsg("登录超时，请重新登录！");
			}
		} else {
			j.setMsg("登录超时，请重新登录！");
		}
		return j;
	}

	/**
	 * 跳转到显示用户角色页面
	 * 
	 * @return
	 */
	@RequestMapping("/currentUserRolePage")
	public String currentUserRolePage(HttpServletRequest request,
			HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		request.setAttribute("userRoles",
				JSON.toJSONString(roleService.tree(sessionInfo)));
		return "/user/userRole";
	}

	/**
	 * 跳转到显示用户权限页面
	 * 
	 * @return
	 */
	@RequestMapping("/currentUserResourcePage")
	public String currentUserResourcePage(HttpServletRequest request,
			HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		request.setAttribute("userResources",
				JSON.toJSONString(resourceService.allTree(sessionInfo)));
		return "/user/userResource";
	}

	/**
	 * 用户登录时的autocomplete
	 * 
	 * @param q
	 *            参数
	 * @return
	 */
	@RequestMapping("/loginCombobox")
	@ResponseBody
	public List<User> loginCombobox(String q) {
		return null;
	}

	/**
	 * 用户登录时的combogrid
	 * 
	 * @param q
	 * @param ph
	 * @return
	 */
	@RequestMapping("/loginCombogrid")
	@ResponseBody
	public DataGrid loginCombogrid(String q, PageHelper ph) {
		return null;
	}

	/**
	 * 上传头像
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/uploaduariva")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			MultipartHttpServletRequest rt) {
		Json j = new Json();
		sy.util.GetRealPath grp = new sy.util.GetRealPath(req.getSession()
				.getServletContext());
		String field = rt.getParameter("fileds");
		String file_path = "";
		file_path = grp.getRealPath() + "upload/uariva/";
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
	 * 跳转到服务号管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/toFwuser")
	public String toFwuser() {
		return "/admin/fwuser";
	}

	// /**
	// * 服务号登录显示dataGrid信息
	// *
	// * @param q
	// * @param ph
	// * @return
	// */
	// @RequestMapping("/fwdataGrid")
	// @ResponseBody
	// public DataGrid fwData(User user, HttpSession session, PageHelper ph) {
	// SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
	// .getSessionInfoName());
	// String q = sessionInfo.getName();
	// return userService.fwData(q, user, ph);
	// }

	/**
	 * 服务号登录显示dataGrid信息
	 * 
	 * @param q
	 * @param ph
	 * @return
	 */
	@RequestMapping("/findUser")
	@ResponseBody
	public Json findUser(String id) {
		Json j = new Json();
		User u = userService.get(id);
		if (u != null) {
			j.setSuccess(true);
			j.setObj(u);
		}
		return j;
	}

	/**
	 * 查看所有服务号信息
	 */
	// @ResponseBody
	// @RequestMapping(value = "/securi_findAllFW")
	// public Json findAllFW(HttpServletRequest req) {
	// Json j = sy.util.Constant.convertJson(req);
	// if (!j.isSuccess()) {
	// return j;
	// }
	// try {
	// JSONObject o = (JSONObject) j.getObj();
	// j.setObj(null);
	// String username = o.getString("username");
	// String pwd = o.getString("pwd");
	// String page = o.getString("curpage");
	// String rows = o.getString("rows");
	// pwd = MD5.encodePassword(pwd);// 加密
	// User u = new User();
	// u.setUser_name((username));
	// u.setUser_pw(pwd);
	//
	// if (imuserService.findPuserByNameAndPwd(username, pwd) == null) {
	// j.setSuccess(false);
	// j.setCode(1006);
	// j.setMsg("账号登录异常");
	// j.setObj(null);
	// return j;
	// }
	// List<User> list = null;// userService.findAllFW(Integer.parseInt(page),
	// // Integer.parseInt(rows));
	// if (list.size() > 0) {
	// j.setSuccess(true);
	// j.setCode(2000);
	// j.setMsg("查询成功");
	// j.setObj(list);
	// return j;
	// } else {
	// j.setSuccess(false);
	// j.setCode(1004);
	// j.setMsg("查询失败");
	// return j;
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// j.setMsg("参数非法...");
	// j.setSuccess(false);
	// j.setObj(null);
	// j.setCode(1002);
	// return j;
	// }
	// }

	/**
	 * 查看指定服务号信息
	 */
	// @ResponseBody
	// @RequestMapping(value = "/securi_findFWByName")
	// public Json findFWByName(HttpServletRequest req) {
	// Json j = sy.util.Constant.convertJson(req);
	// if (!j.isSuccess()) {
	// return j;
	// }
	// try {
	// JSONObject o = (JSONObject) j.getObj();
	// j.setObj(null);
	// String username = o.getString("username");
	// String pwd = o.getString("pwd");
	// pwd = MD5.encodePassword(pwd);// 加密
	// User u = new User();
	// u.setUser_name((username));
	// u.setUser_pw(pwd);
	// if (imuserService.findPuserByNameAndPwd(username, pwd) == null) {
	// j.setSuccess(false);
	// j.setCode(1006);
	// j.setMsg("账号登录异常");
	// j.setObj(null);
	// return j;
	// }
	// String fwName = o.getString("fwName");// 服务号名称
	// User user = null;// userService.findFWByName(fwName);
	// if (user != null) {
	// j.setSuccess(true);
	// j.setCode(2000);
	// j.setMsg("查询成功");
	// j.setObj(user);
	// return j;
	// } else {
	// j.setSuccess(false);
	// j.setCode(1004);
	// j.setMsg("查询失败");
	// return j;
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// j.setMsg("参数非法...");
	// j.setSuccess(false);
	// j.setObj(null);
	// j.setCode(1002);
	// return j;
	// }
	// }

	/**
	 * 查看当前用户下所有服务号信息
	 */
	// @ResponseBody
	// @RequestMapping(value = "/securi_findAllFWByUser")
	// public Json findAllFWByUser(HttpServletRequest req) {
	// Json j = sy.util.Constant.convertJson(req);
	// if (!j.isSuccess()) {
	// return j;
	// }
	// try {
	// JSONObject o = (JSONObject) j.getObj();
	// j.setObj(null);
	// String username = o.getString("username");
	// String pwd = o.getString("pwd");
	// pwd = MD5.encodePassword(pwd);// 加密
	// User u = new User();
	// u.setUser_name((username));
	// u.setUser_pw(pwd);
	// if (imuserService.findPuserByNameAndPwd(username, pwd) == null) {
	// j.setSuccess(false);
	// j.setCode(1006);
	// j.setMsg("账号登录异常");
	// j.setObj(null);
	// return j;
	// }
	// // 查看好友信息
	// JSONArray arr = SynchronizationController.getFriend(username,
	// SynchronizationController.getToken());
	// List<User> list = new ArrayList<User>();
	// if (arr.size() > 0) {
	// for (int i = 0; i < arr.size(); i++) {
	// if (arr.getString(i).indexOf(ConfigUtil.FW_NAME) == -1) {
	// continue;
	// }
	// User user = null;// userService.findFWByName(arr.getString(i));
	// list.add(user);
	// }
	// }
	// if (list.size() > 0) {
	// j.setSuccess(true);
	// j.setCode(2000);
	// j.setMsg("查询成功");
	// j.setObj(list);
	// return j;
	// } else {
	// j.setSuccess(false);
	// j.setCode(1004);
	// j.setMsg("查询失败");
	// return j;
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// j.setMsg("参数非法...");
	// j.setSuccess(false);
	// j.setObj(null);
	// j.setCode(1002);
	// return j;
	// }
	//
	// }

	/**
	 * 跳转至我的购买信息
	 */
	@RequestMapping("/myOrderInfo")
	public String managerOrder() {
		return "/app/order/showOrderList";
	}

	/**
	 * 跳转至购买预警信息
	 */
	@RequestMapping("/myWarnOrderInfo")
	public String managerWarnOrder() {
		return "/app/order/showWarnOrderList";
	}

	/**
	 * 获取我的购买信息数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/orderDataGrid")
	@ResponseBody
	public DataGrid orderDataGrid(OrderSearch app, PageHelper ph,
			HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		DataGrid dataGrid = orderService.dataGrid(app, ph, uid);
		return dataGrid;
	}

	/**
	 * 获取购买预警信息数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/warnOrderDataGrid")
	@ResponseBody
	public DataGrid warnOrderDataGrid(OrderSearch app, PageHelper ph,
			HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		DataGrid dataGrid = orderService.warnDataGrid(app, ph, uid);
		return dataGrid;
	}

	public String getURLJson(String urlStr, String userName, String userPasswd) {
		URL url = null;
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr + "uname=" + userName + "&upasswd="
					+ userPasswd);
			System.out.println(url.toString());
			in = new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));
			String str = null;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		String result = sb.toString();
		return result;
	}
}
