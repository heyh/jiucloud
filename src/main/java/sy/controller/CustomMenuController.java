package sy.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.model.po.Button;
import sy.model.po.CustomMenu;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.CustomMenuServiceI;
import sy.service.IMUserServiceI;
import sy.service.InformServiceI;
import sy.util.ConfigUtil;
import sy.util.MD5;

import com.alibaba.fastjson.JSONObject;

/**
 * ****************************************************************
 * 文件名称 : ButtonController.java
 * 作 者 :   tcp
 * 创建时间 : 2015年1月13日 上午10:18:37
 * 文件描述 : 服务号自定义菜单控制器
 * 版权声明 : 
 * 修改历史 : 2015年1月13日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/api/button")
public class CustomMenuController extends BaseController {

	@Autowired
	private CustomMenuServiceI customMenuService;
	
	@Autowired
	private IMUserServiceI imuserService;
	
	@Autowired
	private InformServiceI informService;
	
	
	
	/**
	 * 跳转到服务号菜单管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/fwMenu")
	public String tofwMenu(HttpServletRequest request) {
		String fwName = ((SessionInfo)request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName())).getName();
		if(fwName.startsWith(ConfigUtil.FW_NAME)){
			return "/admin/fwMenu";
		}else{
			return "/admin/fwMenuInfo";
		}
	}
	
	
	/**
	 * 获取数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/fwMenuGrid")
	@ResponseBody
	public DataGrid fwMenuGrid(AppSearch app, PageHelper ph) {
		return customMenuService.dataGrid(app, ph);
	}
	
	/**
	 * 增加一级菜单
	 */
	@RequestMapping("/addPrimaryMenu")
	@ResponseBody
	public Json addPrimaryMenu(HttpServletRequest request){
		Json j = new Json();
		String menuName = request.getParameter("menuName");
		String nodeId = request.getParameter("nodeId");
		String parNode = request.getParameter("parNode");
		if(nodeId!=null&&!nodeId.equals("")){
			Button b = new Button();
			b.setId(Integer.parseInt(nodeId));
			b.setName(menuName);
			customMenuService.updatePrimaryMenu(b);//编辑
			j.setSuccess(true);
		}else{
			Integer id = customMenuService.addPrimaryMenu(menuName);//添加
			if(id!=null&&!id.equals("")){
				j.setSuccess(true);
				j.setObj(id);
				if(parNode!=null&&!parNode.equals("")){//在一级菜单下添加二级菜单
					Button b = customMenuService.getMenuById(parNode);
					if(b.getSubMenu()!=null)
						b.setSubMenu(b.getSubMenu()+id+",");
					else
						b.setSubMenu(id+",");
					customMenuService.updatePrimaryMenu(b);
				}
			}
		}
		return j;
	}

	/**
	 * 删除一级菜单
	 */
	@RequestMapping("/delPrimaryMenu")
	@ResponseBody
	public Json delPrimaryMenu(HttpServletRequest request){
		Json j = new Json();
		String fwName = ((SessionInfo)request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName())).getName();
		String nodeId = request.getParameter("nodeId");
//		String customMenuId = request.getParameter("customMenuId");//主菜单id
		customMenuService.delPrimaryMenu(nodeId);
		CustomMenu menu = null;
		menu = customMenuService.getCustomMenuByFw(fwName);
		if(menu!=null){
			String sub = "";
			if(menu.getPrimaryMenu().lastIndexOf(nodeId+",")>-1)
				sub = menu.getPrimaryMenu().replaceAll(nodeId+",", "");
			else 
				sub = menu.getPrimaryMenu().replaceAll(nodeId, "");
			menu.setPrimaryMenu(sub);
			customMenuService.saveMenu(menu);
		}
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 删除二级菜单
	 */
	@RequestMapping("/delsubMenu")
	@ResponseBody
	public Json delsubMenu(HttpServletRequest request){
		Json j = new Json();
		String nodeId = request.getParameter("nodeId");
		String parId = request.getParameter("parId");
		if(nodeId!=null){
			customMenuService.delPrimaryMenu(nodeId);
			if(parId!=null){
				Button b = customMenuService.getMenuById(parId);
				String sub = b.getSubMenu().replaceAll(nodeId+",", "");
				b.setSubMenu(sub);
				customMenuService.updatePrimaryMenu(b);
			}
			j.setSuccess(true);
		}
		return j;
	}
	
	
	/**
	 * 添加动作
	 */
	@RequestMapping("/addEvent")
	@ResponseBody
	public Json addEvent(HttpServletRequest request){
		Json j = new Json();
		String nodeId = request.getParameter("nodeId");
		String type = request.getParameter("type");
		String key = "";
		String url = "";
		String informId = "";
		if(type.equals("3"))
			url = request.getParameter("txt");
		else if(type.equals("1"))
			key = request.getParameter("txt");
		else if(type.equals("2"))
			informId = request.getParameter("txt");
		Button b = customMenuService.getMenuById(nodeId);
		b.setType(Integer.parseInt(type));
		b.setKey(key);
		b.setUrl(url);
		b.setInformId(informId);
		customMenuService.updatePrimaryMenu(b);
		j.setSuccess(true);
		j.setMsg("操作成功...");
		return j;
	}
	
	/**
	 * 移除动作
	 */
	@RequestMapping("/delEvent")
	@ResponseBody
	public Json delEvent(HttpServletRequest request){
		Json j = new Json();
		String nodeId = request.getParameter("nodeId");
		Button b = customMenuService.getMenuById(nodeId);
		b.setType(0);
		b.setKey("");
		b.setUrl("");
		customMenuService.updatePrimaryMenu(b);
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 保存菜单
	 */
	@RequestMapping("/saveMenu")
	@ResponseBody
	public Json saveMenu(HttpServletRequest request){
		Json j = new Json();
		String fwName = ((SessionInfo)request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName())).getName();
		if(!fwName.startsWith(ConfigUtil.FW_NAME)){
			j.setMsg("只有服务号才能操作...");
			return j;
		}
		String menuId = request.getParameter("menuId");
		CustomMenu m = customMenuService.getCustomMenuByFw(fwName);
		//String customMenuId = request.getParameter("customMenuId");
		String customMenuId = "";
		if(m!=null)
			customMenuId = m.getId()+"";//customMenuId
		if(menuId!=null){
			CustomMenu menu = new CustomMenu();
			if(customMenuId!=null&&!customMenuId.equals(""))
				menu.setId(Integer.parseInt(customMenuId));
			menu.setPrimaryMenu(menuId);
			menu.setCtime(new Date());
			menu.setFwName(((SessionInfo)request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName())).getName());
			customMenuService.saveMenu(menu);
			j.setSuccess(true);
			j.setMsg("操作成功...");
		}
		return j;
	}
	
	/**
	 * 初始加载菜单节点
	 */
	@RequestMapping("/menuLode")
	@ResponseBody
	public Json menuLode(HttpServletRequest request){
		Json j = new Json();
		String fwName =null;
		String fw = request.getParameter("fwName");
		if(fw!=null&&!fw.equals("")){
			fwName = fw;
		}else{
			fwName = ((SessionInfo)request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName())).getName();
		}
		if(fwName.startsWith(ConfigUtil.FW_NAME)){
			CustomMenu menu = customMenuService.getMenu(fwName);
			if(menu!=null){
				j.setSuccess(true);
				j.setObj(menu);
			}
		}
		return j;
	}
	
	
	/**
	 * 获得菜单节点信息
	 */
	@RequestMapping("/menuInfo")
	@ResponseBody
	public Json menuInfo(HttpServletRequest request){
		Json j = new Json();
		String nodeId = request.getParameter("nodeId");
		if(nodeId!=null&&!nodeId.equals("")){
			Button b = customMenuService.getMenuById(nodeId);
			if(b!=null){
				if(b.getType()!=0){
					j.setSuccess(true);
					j.setObj(b);
				}
			}
		}
		return j;
	}
	
	/**
	 * 发送图文消息
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findImgTxt", method = RequestMethod.POST)
	public Json findImgTxt(HttpServletRequest req) {
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
			User u = new User();
//			u.setUsername((username));
//			u.setUser_pw(pwd);
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String informId = o.getString("informId");//服务号消息id
			Inform info = informService.getFwMsg(informId);
			if (info != null) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(info);
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
	
	
	/**
	 * 获得自定义菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/securi_findCustomMenu", method = RequestMethod.POST)
	public Json findCustomMenu(HttpServletRequest req) {
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
			User u = new User();
//			u.setUser_name(username);
//			u.setUser_pw(pwd);
			if(imuserService.findPuserByNameAndPwd(username, pwd) == null) {
		    	j.setSuccess(false);
		    	j.setCode(1006);
				j.setMsg("账号登录异常");
				j.setObj(null);
		    	return j;
		    }
			String id = o.getString("id");
			// 查询自定义菜单
//			List<Button> list = customMenuService.findAllCustomMenu(id);
			String list = customMenuService.findAllCustomMenu(id);
			if (list != null) {
				j.setSuccess(true);
				j.setCode(2000);
				j.setMsg("查询成功");
				j.setObj(list);
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
}
