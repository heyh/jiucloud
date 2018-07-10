package sy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.Item;
import sy.model.Param;
import sy.model.S_department;
import sy.model.po.*;
import sy.pageModel.*;
import sy.service.*;
import sy.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

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

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private CostServiceI costService;

    @Autowired
    private ParamService paramService;

    @Autowired
	private PriceServiceI priceService;

    @Autowired
	private FieldDataServiceI fieldDataService;


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
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Json login(HttpSession session, HttpServletRequest request) {
		Json j = new Json();
		User u = null;
		String cid = null;
		try {
			if (request.getParameter("id") != null && !("null".equals(request.getParameter("id")))) {
				u = userService.getUser(request.getParameter("id"));
				System.out.println(111);
			} else {
				u = userService.login(request.getParameter("name"), request.getParameter("pwd"));
			}
			if(request.getParameter("cid")!= null && !("null".equals(request.getParameter("cid")))){
				cid = request.getParameter("cid");
			}
			List<Integer> dgroup = null;
			List<Integer> ugroup = null;
			if (u != null) {
				j.setSuccess(true);
				j.setMsg("登陆成功！");
				//查询公司名称和id

				// 防止多次登录 begin
//				ServletContext application = session.getServletContext();
//				Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
//				if (loginMap == null) {
//					loginMap = new HashMap<String, String>();
//
//				}
//
//				for (String key : loginMap.keySet()) {
//					if (("PC-" + u.getId()).equals(key) && !loginMap.get(key).equals(session.getId())) {
//						j.setSuccess(false);
//						j.setMsg("您好，该用户已登录!");
//						return j;
//					}
//				}
//
//				loginMap.put(("PC-" + u.getId()), session.getId());
//				application.setAttribute("loginMap", loginMap);
//				session.setAttribute("PC-userId", u.getId());

				ServletContext application = session.getServletContext();
				Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
				if (loginMap == null) {
					loginMap = new HashMap<String, String>();

				}

				if (loginMap.containsKey("login-" + u.getId())) {
					if (!StringUtil.trimToEmpty(u.getIsLogin()).equals("") && !loginMap.get("login-" + u.getId()).equals(session.getId())) {
						j.setSuccess(false);
						j.setMsg("您好，该用户已登录!");
						return j;
					}
				}

				loginMap.put(("login-" + u.getId()), session.getId());
				application.setAttribute("loginMap", loginMap);
				userService.updateLoginStatus(u.getId(), u.getId());

				// end

				Company c = companyService.findOneView(u.getId(),cid);
				System.out.println("Company:" + c);
				if (c == null) {
					j.setSuccess(false);
					j.setMsg("您好，您没有登录权限，请联系管理员订购该业务");
					return j;
				}
				//根据用户id查询部门id和名称,超管不属于任何部门所以是null
//				List<S_department> s = departmentService.getDepartmentByUid(u.getId(), cid);
                List<S_department> s = departmentService.getDepartmentsByUid(u.getId(), cid);
				//根据用户id查询所有职位信息
//				Department d = departmentService.findOneView(u.getId(),cid);
//				System.out.println("Department:" + d);
//				ugroup = departmentService.getUserGroup(d, u.getId(),cid);
                ugroup = departmentService.getUsers(cid, Integer.parseInt(u.getId()));
				System.out.println(ugroup);
				SessionInfo sessionInfo = new SessionInfo();
				if (u.getId().equals(u.getCorporation_creater())) {
					sessionInfo.setIsadmin(1);
				}
                // add by heyh
                sessionInfo.setUsername(u.getUsername());
				sessionInfo.setName(u.getRealname());
				sessionInfo.setId(u.getId());
				sessionInfo.setResourceList(userService.resourceList(String.valueOf(u.getId()), sessionInfo.getIsadmin()));
				sessionInfo.setCompid(String.valueOf(c.getId()));
				sessionInfo.setCompName(c.getName());
//				if(d!=null){
//					sessionInfo.setDepid(String.valueOf(d.getId()));
//					sessionInfo.setDepName(d.getName());
//                    sessionInfo.setParentId(d.getParent_id());
//				}else{
//					sessionInfo.setDepid(null);
//					sessionInfo.setDepName(null);
//				}
                if (null != s && s.size() > 0) {
                    for (S_department d : s) {
                        sessionInfo.setDepid(String.valueOf(d.getId()));
					    sessionInfo.setDepName(d.getName());
//                        if (d.getParent_id() == 0) {
//                            sessionInfo.setParentId(0);
//                            break;
//                        } else {
//                            sessionInfo.setParentId(d.getParent_id());
//                        }
                    }
                } else {
                    sessionInfo.setDepid(null);
					sessionInfo.setDepName(null);
                }
//				sessionInfo.setDepartment_id(String.valueOf(s.getId()));
//				sessionInfo.setDepartment_name(s.getName());
                sessionInfo.setDepartmentIds(s); // 隶属多部门
				sessionInfo.setDgroup(dgroup);
				sessionInfo.setUgroup(ugroup);
                List<Integer> departmentIds = new ArrayList<Integer>();
                if (s != null && s.size() > 0) {
                    for (S_department department : s) {
                        departmentIds.add(department.getId());
                    }
                }

				/////////////////////////////////// 默认添加体验信息 /////////////////////////////////////////

                // 增加默认费用类型配置
//				costService.initDefaultCost(String.valueOf(c.getId()));
//				priceService.initPrice(String.valueOf(c.getId()));

				priceService.initDefaultPriceCost(StringUtil.trimToEmpty(c.getId()));

				// 体验帐号拷贝信息
				NodeUtility nodeUtility = new NodeUtility();
				List<Node> nodeList = departmentService.getFirstNodes(cid);
				List<Node> nodes = nodeUtility.getDepartmentNodeList(nodeList);
				boolean isTYZH = false;
				for (Node node : nodes) {
					User user = userService.getUser(StringUtil.trimToEmpty(node.getUserId()));
					if (user != null && user.getUsername() != null) {
						isTYZH = userService.getUser(StringUtil.trimToEmpty(node.getUserId())).getUsername().startsWith("AT");
					}
					if (isTYZH) {
						break;
					}
				}
				if (isTYZH) {

					// 拷贝工程
					if (projectService.getAllProjects(cid) == null || projectService.getAllProjects(cid).size() == 0) {
						projectService.initExperienceProjects(cid);
					}

					List<TFieldData> allTFD = fieldDataService.getAllField(cid);
					if (allTFD == null || allTFD.size()==0) {
						// 拷贝数据
						nodeUtility = new NodeUtility();
						List<Node> defaultNodeList = departmentService.getAllNodes("195");
						List<Node> defaultNodes = nodeUtility.getDepartmentNodeList(defaultNodeList);

						List<TFieldData> defaultFields =  fieldDataService.getAllField("195");
						List<Integer> hasInsertFields = new ArrayList<Integer>();
						for (Node defaultNode : defaultNodes) {
							for (Node node : nodes) {
								if (StringUtil.trimToEmpty(defaultNode.getLevel()).equals(StringUtil.trimToEmpty(node.getLevel()))) {
									for (TFieldData defaultField : defaultFields) {
										if (StringUtil.trimToEmpty(defaultField.getUid()).equals(StringUtil.trimToEmpty(defaultNode.getUserId()))) {
											if (hasInsertFields.contains(defaultField.getId())) {
												continue;
											}

											TFieldData f = new TFieldData();
											f.setUid(StringUtil.trimToEmpty(node.getUserId()));
											f.setUname(userService.getUser(StringUtil.trimToEmpty(node.getUserId())).getUsername());
											f.setCid(cid);
											f.setCompany(c.getName());
											f.setCreatTime(defaultField.getCreatTime());
											f.setDataName(defaultField.getDataName());
											f.setPrice(defaultField.getPrice());
											f.setCount(defaultField.getCount());
											f.setUnit(defaultField.getUnit());
											f.setItemCode(defaultField.getItemCode());
											f.setSpecifications(defaultField.getSpecifications());
											f.setRemark(defaultField.getRemark());
											f.setNeedApproved(defaultField.getNeedApproved());
											f.setApprovedUser(defaultField.getApprovedUser());
											f.setCurrentApprovedUser(defaultField.getCurrentApprovedUser());
											f.setApprovedOption(defaultField.getApprovedOption());
											f.setSection(defaultField.getSection());
											f.setSupplier(defaultField.getSupplier());
											f.setCostType(defaultField.getCostType());
											f.setProjectName(defaultField.getProjectName());
											f.setPrice_ys(defaultField.getPrice_ys());
											f.setPrice_sj(defaultField.getPrice_sj());

											fieldDataService.add(f);

											hasInsertFields.add(defaultField.getId());
										}
									}
								}
							}
						}
					}

				}
                ////////////////////////////////////////////////////////////////////////////

				sessionInfo.setProjectInfos(projectService.getProjectInfos(String.valueOf(c.getId()), departmentIds));
				Map<String, List<Map<String, Object>>> costInfos = costService.getCostTypeInfos(departmentIds, cid);
				List<Map<String, Object>> dataCostList = costInfos.get("dataCostInfos");
				List<Map<String, Object>> docCostList = costInfos.get("docCostInfos");
				List<Map<String, Object>> billCostList = costInfos.get("billCostInfos");
				List<Map<String, Object>> materialCostList = costInfos.get("materialCostInfos");

				List<Map<String, Object>> _dataCostList = new ArrayList<Map<String, Object>>();
				Map<String, Object> _dataCost = new HashMap<String, Object>();
				for (Map<String, Object> dataCost : dataCostList) {
					_dataCost = new HashMap<String, Object>();
					_dataCost.put("text", dataCost.get("costType"));
					_dataCost.put("id", dataCost.get("nid"));
					_dataCost.put("pid", dataCost.get("pid"));
					_dataCost.put("itemCode", dataCost.get("itemCode"));
					_dataCostList.add(_dataCost);
				}
				sessionInfo.setCostTree(Utility.treeList(_dataCostList, "-1"));

				List<Map<String, Object>> _docCostList = new ArrayList<Map<String, Object>>();
				Map<String, Object> _docCost = new HashMap<String, Object>();
				for (Map<String, Object> docCost : docCostList) {
					_docCost = new HashMap<String, Object>();
					_docCost.put("text", docCost.get("costType"));
					_docCost.put("id", docCost.get("nid"));
					_docCost.put("pid", docCost.get("pid"));
					_docCost.put("itemCode", docCost.get("itemCode"));
					_docCostList.add(_docCost);
				}
				sessionInfo.setDocCostTree(Utility.treeList(_docCostList, "-1"));

				List<Map<String, Object>> _billCostList = new ArrayList<Map<String, Object>>();
				Map<String, Object> _billCost = new HashMap<String, Object>();
				for (Map<String, Object> billCost : billCostList) {
					_billCost = new HashMap<String, Object>();
					_billCost.put("text", billCost.get("costType"));
					_billCost.put("id", billCost.get("nid"));
					_billCost.put("pid", billCost.get("pid"));
					_billCost.put("itemCode", billCost.get("itemCode"));
					_billCostList.add(_billCost);
				}
				sessionInfo.setBillCostTree(Utility.treeList(_billCostList, "-1"));

				List<Map<String, Object>> _materialCostList = new ArrayList<Map<String, Object>>();
				Map<String, Object> _materialCost = new HashMap<String, Object>();
				for (Map<String, Object> materialCost : materialCostList) {
					_materialCost = new HashMap<String, Object>();
					_materialCost.put("text", materialCost.get("costType"));
					_materialCost.put("id", materialCost.get("nid"));
					_materialCost.put("pid", materialCost.get("pid"));
					_materialCost.put("itemCode", materialCost.get("itemCode"));
					_materialCostList.add(_materialCost);
				}
				sessionInfo.setMaterialCostTree(Utility.treeList(_materialCostList, "-1"));


				sessionInfo.setCostTypeInfos(costInfos);
                sessionInfo.setUnitParams((List<Param>) paramService.getParams("UP", ""));
                sessionInfo.setRightList(departmentService.getAllRight(cid, Integer.parseInt(u.getId())));
                sessionInfo.setParentId(departmentService.getParentId(cid, Integer.parseInt(u.getId())));
				session.setAttribute(ConfigUtil.getSessionInfoName(), sessionInfo);

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
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {

		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String userId = sessionInfo.getId();
		if (!userId.equals("")) {
			userService.updateLoginStatus(userId, "");
		}

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
	 * @param session
	 * @param req
	 * @param rt
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

	/**
	 * 服务号登录显示dataGrid信息
	 * @param id
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
	 * @param app
	 * @param ph
	 * @param request
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
	 * @param app
	 * @param ph
	 * @param request
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
