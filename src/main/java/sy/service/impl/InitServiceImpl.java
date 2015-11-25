package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.ResourceDaoI;
import sy.dao.ResourceTypeDaoI;
import sy.dao.RoleDaoI;
import sy.dao.UserDaoI;
import sy.model.Tresource;
import sy.model.Tresourcetype;
import sy.model.Trole;
import sy.service.InitServiceI;

@Service
public class InitServiceImpl implements InitServiceI {

	@Autowired
	private UserDaoI userDao;

	@Autowired
	private RoleDaoI roleDao;

	@Autowired
	private ResourceDaoI resourceDao;

	@Autowired
	private ResourceTypeDaoI resourceTypeDao;

	@Override
	synchronized public void init() {

		initResourceType();// 初始化资源类型

		initResource();// 初始化资源

		initRole();// 初始化角色

		// initUser();// 初始化用户

		// RedisClient.del(ConfigUtil.REDIS_OPENDATA);//删除缓存

	}

	private void initResourceType() {
		Tresourcetype t = new Tresourcetype();
		t.setId("0");
		t.setName("菜单");
		resourceTypeDao.saveOrUpdate(t);

		Tresourcetype t2 = new Tresourcetype();
		t2.setId("1");
		t2.setName("功能");
		resourceTypeDao.saveOrUpdate(t2);
	}

	private void initRole() {
		Trole superAdmin = new Trole();
		superAdmin.setId("0");
		superAdmin.setName("超管");
		superAdmin.getTresources().addAll(resourceDao.find("from Tresource t"));
		// 让超管可以访问所有资源
		superAdmin.setSeq(0);
		superAdmin.setRemark("超级管理员角色，拥有系统中所有的资源访问权限");
		roleDao.saveOrUpdate(superAdmin);
	}

	//
	// private void initUser() {
	// List<Tuser> l = userDao
	// .find("from Tuser t where t.name in ('admin','admin1','admin2','admin3','admin4','admin5','guest')");
	// if (l != null && l.size() > 0) {
	// for (Tuser user : l) {
	// userDao.delete(user);
	// }
	// }
	//
	// Tuser admin = new Tuser();
	// admin.setId("0");
	// admin.setName("admin");
	// admin.setPwd(MD5Util.md5("111111"));
	// admin.setCreatedatetime(new Date());
	// admin.getTroles().addAll(roleDao.find("from Trole t"));// 给用户赋予所有角色
	// userDao.saveOrUpdate(admin);
	// }

	private void initResource() {
		Tresourcetype menuType = resourceTypeDao.get(Tresourcetype.class, "0");// 菜单类型
		Tresourcetype funType = resourceTypeDao.get(Tresourcetype.class, "1");// 功能类型

		// 初始化日常办公
		// initRCBGMENU(menuType, funType);

		// 初始化工程管理
		initGCMENU(menuType, funType);

		// 初始化我的购买管理
		// initWDGM(menuType, funType);

		// 数据统计分析
		initDATA(menuType, funType);

		System.out
				.println("====================ffffffffffffffffffffffffffffffffffffffffffff");
	}

	/**
	 * 初始日常办公 jiucloud_menu_rcbg 二级 jiucloud_menu_rcbg_child 三级
	 * 
	 * 功能级:jiucloud_menu_rcbg_funemaillist
	 * 
	 */
	public void initRCBGMENU(Tresourcetype menuType, Tresourcetype funType) {// 菜单类型
		Tresource jiucloud_menu_rcbg = new Tresource();
		jiucloud_menu_rcbg.setId("jiucloud_menu_rcbg");
		jiucloud_menu_rcbg.setName("日常办公");
		jiucloud_menu_rcbg.setTresourcetype(menuType);
		jiucloud_menu_rcbg.setSeq(0);
		jiucloud_menu_rcbg.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg);

		// 日志管理
		Tresource jiucloud_menu_rcbg_funrizhilist = new Tresource();
		jiucloud_menu_rcbg_funrizhilist
				.setId("jiucloud_menu_rcbg_funrizhilist");
		jiucloud_menu_rcbg_funrizhilist.setName("日志管理");
		jiucloud_menu_rcbg_funrizhilist.setTresourcetype(menuType);
		jiucloud_menu_rcbg_funrizhilist.setTresource(jiucloud_menu_rcbg);
		jiucloud_menu_rcbg_funrizhilist.setSeq(10);
		jiucloud_menu_rcbg_funrizhilist.setUrl("/rzglController/rzglmanager");
		jiucloud_menu_rcbg_funrizhilist.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funrizhilist);

		Tresource rzSearch = new Tresource();
		rzSearch.setId("rzSearch");
		rzSearch.setName("日志表格");
		rzSearch.setTresourcetype(funType);
		rzSearch.setTresource(jiucloud_menu_rcbg_funrizhilist);
		rzSearch.setSeq(10);
		rzSearch.setUrl("/rzglController/searchrz");
		rzSearch.setIcon("wrench");
		resourceDao.saveOrUpdate(rzSearch);

		Tresource rzAddPage = new Tresource();
		rzAddPage.setId("rzAddPage");
		rzAddPage.setName("增加日志页面");
		rzAddPage.setTresourcetype(funType);
		rzAddPage.setTresource(jiucloud_menu_rcbg_funrizhilist);
		rzAddPage.setSeq(10);
		rzAddPage.setUrl("/rzglController/addrzPage");
		rzAddPage.setIcon("wrench");
		resourceDao.saveOrUpdate(rzAddPage);

		Tresource rzAdd = new Tresource();
		rzAdd.setId("rzAdd");
		rzAdd.setName("增加日志");
		rzAdd.setTresourcetype(funType);
		rzAdd.setTresource(jiucloud_menu_rcbg_funrizhilist);
		rzAdd.setSeq(10);
		rzAdd.setUrl("/rzglController/addrz");
		rzAdd.setIcon("wrench");
		resourceDao.saveOrUpdate(rzAdd);

		Tresource rzSearchPage = new Tresource();
		rzSearchPage.setId("rzSearchPage");
		rzSearchPage.setName("查看日志页面");
		rzSearchPage.setTresourcetype(funType);
		rzSearchPage.setTresource(jiucloud_menu_rcbg_funrizhilist);
		rzSearchPage.setSeq(10);
		rzSearchPage.setUrl("/rzglController/searchrzPage");
		rzSearchPage.setIcon("wrench");
		resourceDao.saveOrUpdate(rzSearchPage);

		// 任务管理
		Tresource jiucloud_menu_rcbg_funrenwulist = new Tresource();
		jiucloud_menu_rcbg_funrenwulist
				.setId("jiucloud_menu_rcbg_funrenwulist");
		jiucloud_menu_rcbg_funrenwulist.setName("任务管理");
		jiucloud_menu_rcbg_funrenwulist.setTresourcetype(menuType);
		jiucloud_menu_rcbg_funrenwulist.setTresource(jiucloud_menu_rcbg);
		jiucloud_menu_rcbg_funrenwulist.setSeq(10);
		jiucloud_menu_rcbg_funrenwulist.setUrl("/rwglController/rwglmanager");
		jiucloud_menu_rcbg_funrenwulist.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funrenwulist);

		Tresource rwSearch = new Tresource();
		rwSearch.setId("rwSearch");
		rwSearch.setName("任务表格");
		rwSearch.setTresourcetype(funType);
		rwSearch.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwSearch.setSeq(10);
		rwSearch.setUrl("/rwglController/searchrw");
		rwSearch.setIcon("wrench");
		resourceDao.saveOrUpdate(rwSearch);

		Tresource rwAddPage = new Tresource();
		rwAddPage.setId("rwAddPage");
		rwAddPage.setName("增加任务页面");
		rwAddPage.setTresourcetype(funType);
		rwAddPage.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwAddPage.setSeq(10);
		rwAddPage.setUrl("/rwglController/addrwPage");
		rwAddPage.setIcon("wrench");
		resourceDao.saveOrUpdate(rwAddPage);

		Tresource rwAdd = new Tresource();
		rwAdd.setId("rwAdd");
		rwAdd.setName("增加任务");
		rwAdd.setTresourcetype(funType);
		rwAdd.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwAdd.setSeq(10);
		rwAdd.setUrl("/rwglController/addrw");
		rwAdd.setIcon("wrench");
		resourceDao.saveOrUpdate(rwAdd);

		Tresource rweditPage = new Tresource();
		rweditPage.setId("rweditPage");
		rweditPage.setName("操作任务页面");
		rweditPage.setTresourcetype(funType);
		rweditPage.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rweditPage.setSeq(10);
		rweditPage.setUrl("/rwglController/editrwPage");
		rweditPage.setIcon("wrench");
		resourceDao.saveOrUpdate(rweditPage);

		Tresource rwEdit = new Tresource();
		rwEdit.setId("rwEdit");
		rwEdit.setName("操作任务");
		rwEdit.setTresourcetype(funType);
		rwEdit.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwEdit.setSeq(10);
		rwEdit.setUrl("/rwglController/editrw");
		rwEdit.setIcon("wrench");
		resourceDao.saveOrUpdate(rwEdit);

		Tresource rwCommentPage = new Tresource();
		rwCommentPage.setId("rwCommentPage");
		rwCommentPage.setName("评论任务页面");
		rwCommentPage.setTresourcetype(funType);
		rwCommentPage.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwCommentPage.setSeq(10);
		rwCommentPage.setUrl("/rwglController/editrwBycommentPage");
		rwCommentPage.setIcon("wrench");
		resourceDao.saveOrUpdate(rwCommentPage);

		Tresource rwComment = new Tresource();
		rwComment.setId("rwComment");
		rwComment.setName("评论任务");
		rwComment.setTresourcetype(funType);
		rwComment.setTresource(jiucloud_menu_rcbg_funrenwulist);
		rwComment.setSeq(10);
		rwComment.setUrl("/rwglController/editrwBycomment");
		rwComment.setIcon("wrench");
		resourceDao.saveOrUpdate(rwComment);

		Tresource jiucloud_menu_rcbg_child = new Tresource();
		jiucloud_menu_rcbg_child.setId("jiucloud_menu_rcbg_child");
		jiucloud_menu_rcbg_child.setName("站内信");
		jiucloud_menu_rcbg_child.setTresourcetype(menuType);
		jiucloud_menu_rcbg_child.setSeq(0);
		jiucloud_menu_rcbg_child.setIcon("plugin");
		/** 二有 */
		jiucloud_menu_rcbg_child.setTresource(jiucloud_menu_rcbg);
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_child);

		// 收件箱-------------------begin
		Tresource jiucloud_menu_rcbg_funemaillist = new Tresource();
		jiucloud_menu_rcbg_funemaillist
				.setId("jiucloud_menu_rcbg_funemaillist");
		jiucloud_menu_rcbg_funemaillist.setName("收件箱");
		jiucloud_menu_rcbg_funemaillist.setTresourcetype(menuType);
		jiucloud_menu_rcbg_funemaillist.setTresource(jiucloud_menu_rcbg_child);
		jiucloud_menu_rcbg_funemaillist.setSeq(10);
		jiucloud_menu_rcbg_funemaillist.setUrl("/messageController/recMsgList");
		jiucloud_menu_rcbg_funemaillist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaillist);

		Tresource jiucloud_menu_rcbg_funemailrec = new Tresource();
		jiucloud_menu_rcbg_funemailrec.setId("jiucloud_menu_rcbg_funemailrec");
		jiucloud_menu_rcbg_funemailrec.setName("收件箱列表");
		jiucloud_menu_rcbg_funemailrec.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemailrec
				.setTresource(jiucloud_menu_rcbg_funemaillist);
		jiucloud_menu_rcbg_funemailrec.setSeq(10);
		jiucloud_menu_rcbg_funemailrec.setUrl("/messageController/datarecGrid");
		jiucloud_menu_rcbg_funemailrec.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemailrec);

		Tresource jiucloud_menu_rcbg_funemailread = new Tresource();
		jiucloud_menu_rcbg_funemailread
				.setId("jiucloud_menu_rcbg_funemailread");
		jiucloud_menu_rcbg_funemailread.setName("收件箱阅读");
		jiucloud_menu_rcbg_funemailread.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemailread
				.setTresource(jiucloud_menu_rcbg_funemaillist);
		jiucloud_menu_rcbg_funemailread.setSeq(10);
		jiucloud_menu_rcbg_funemailread.setUrl("/messageController/recread");
		jiucloud_menu_rcbg_funemailread.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemailread);

		Tresource jiucloud_menu_rcbg_funemailbatchDel = new Tresource();
		jiucloud_menu_rcbg_funemailbatchDel
				.setId("cloud_menu_rcbg_funemailbatch");
		jiucloud_menu_rcbg_funemailbatchDel.setName("收件箱批量删除");
		jiucloud_menu_rcbg_funemailbatchDel.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemailbatchDel
				.setTresource(jiucloud_menu_rcbg_funemaillist);
		jiucloud_menu_rcbg_funemailbatchDel.setSeq(10);
		jiucloud_menu_rcbg_funemailbatchDel
				.setUrl("/messageController/batchDeleteRec");
		jiucloud_menu_rcbg_funemailbatchDel.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemailbatchDel);

		Tresource jiucloud_menu_rcbg_funemailDel = new Tresource();
		jiucloud_menu_rcbg_funemailDel.setId("cloud_menu_rcbg_funemailDel");
		jiucloud_menu_rcbg_funemailDel.setName("收件箱删除");
		jiucloud_menu_rcbg_funemailDel.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemailDel
				.setTresource(jiucloud_menu_rcbg_funemaillist);
		jiucloud_menu_rcbg_funemailDel.setSeq(10);
		jiucloud_menu_rcbg_funemailDel.setUrl("/messageController/deleteInRec");
		jiucloud_menu_rcbg_funemailDel.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemailDel);

		// 收件箱---------------------end

		// 发件箱-------------------begin
		Tresource jiucloud_menu_rcbg_funemaisendllist = new Tresource();
		jiucloud_menu_rcbg_funemaisendllist
				.setId("jiucloud_menu_rcbg_funemaisendllist");
		jiucloud_menu_rcbg_funemaisendllist.setName("发件箱");
		jiucloud_menu_rcbg_funemaisendllist.setTresourcetype(menuType);
		jiucloud_menu_rcbg_funemaisendllist
				.setTresource(jiucloud_menu_rcbg_child);
		jiucloud_menu_rcbg_funemaisendllist.setSeq(10);
		jiucloud_menu_rcbg_funemaisendllist
				.setUrl("/messageController/sendMsgList");
		jiucloud_menu_rcbg_funemaisendllist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaisendllist);

		Tresource jiucloud_menu_rcbg_funemaisenddata = new Tresource();
		jiucloud_menu_rcbg_funemaisenddata
				.setId("jiucloud_menu_rcbg_funemaisenddata");
		jiucloud_menu_rcbg_funemaisenddata.setName("发件箱列表");
		jiucloud_menu_rcbg_funemaisenddata.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemaisenddata
				.setTresource(jiucloud_menu_rcbg_funemaisendllist);
		jiucloud_menu_rcbg_funemaisenddata.setSeq(10);
		jiucloud_menu_rcbg_funemaisenddata
				.setUrl("/messageController/dataGrid");
		jiucloud_menu_rcbg_funemaisenddata.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaisenddata);

		Tresource jiucloud_menu_rcbg_funemaisenddetail = new Tresource();
		jiucloud_menu_rcbg_funemaisenddetail
				.setId("jiucloud_menu_rcbg_funemaisenddetail");
		jiucloud_menu_rcbg_funemaisenddetail.setName("邮件详情");
		jiucloud_menu_rcbg_funemaisenddetail.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemaisenddetail
				.setTresource(jiucloud_menu_rcbg_funemaisendllist);
		jiucloud_menu_rcbg_funemaisenddetail.setSeq(10);
		jiucloud_menu_rcbg_funemaisenddetail
				.setUrl("/messageController/detail");
		jiucloud_menu_rcbg_funemaisenddetail.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaisenddetail);

		Tresource jiu_menu_rcbg_funemaisendbatchdel = new Tresource();
		jiu_menu_rcbg_funemaisendbatchdel
				.setId("jiu_menu_rcbg_funemaisendbatchdel");
		jiu_menu_rcbg_funemaisendbatchdel.setName("邮件批量删除");
		jiu_menu_rcbg_funemaisendbatchdel.setTresourcetype(funType);
		jiu_menu_rcbg_funemaisendbatchdel
				.setTresource(jiucloud_menu_rcbg_funemaisendllist);
		jiu_menu_rcbg_funemaisendbatchdel.setSeq(10);
		jiu_menu_rcbg_funemaisendbatchdel
				.setUrl("/messageController/batchDeleteSend");
		jiu_menu_rcbg_funemaisendbatchdel.setIcon("wrench");
		resourceDao.saveOrUpdate(jiu_menu_rcbg_funemaisendbatchdel);

		Tresource jiu_menu_rcbg_funemaisend = new Tresource();
		jiu_menu_rcbg_funemaisend.setId("jiu_menu_rcbg_funemaisend");
		jiu_menu_rcbg_funemaisend.setName("发送邮件");
		jiu_menu_rcbg_funemaisend.setTresourcetype(funType);
		jiu_menu_rcbg_funemaisend
				.setTresource(jiucloud_menu_rcbg_funemaisendllist);
		jiu_menu_rcbg_funemaisend.setSeq(10);
		jiu_menu_rcbg_funemaisend.setUrl("/messageController/tosendmsg");
		jiu_menu_rcbg_funemaisend.setIcon("wrench");
		resourceDao.saveOrUpdate(jiu_menu_rcbg_funemaisend);

		Tresource jiucloud_menu_rcbg_funemaisenddel = new Tresource();
		jiucloud_menu_rcbg_funemaisenddel
				.setId("cloud_menu_rcbg_funemaisenddel");
		jiucloud_menu_rcbg_funemaisenddel.setName("邮件删除");
		jiucloud_menu_rcbg_funemaisenddel.setTresourcetype(funType);
		jiucloud_menu_rcbg_funemaisenddel
				.setTresource(jiucloud_menu_rcbg_funemaisendllist);
		jiucloud_menu_rcbg_funemaisenddel.setSeq(10);
		jiucloud_menu_rcbg_funemaisenddel
				.setUrl("/messageController/deleteSend");
		jiucloud_menu_rcbg_funemaisenddel.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaisenddel);

		// 回收站
		Tresource jiucloud_menu_rcbg_funemaildellist = new Tresource();
		jiucloud_menu_rcbg_funemaildellist
				.setId("jiucloud_menu_rcbg_funemaildellist");
		jiucloud_menu_rcbg_funemaildellist.setName("回收站");
		jiucloud_menu_rcbg_funemaildellist.setTresourcetype(menuType);
		jiucloud_menu_rcbg_funemaildellist
				.setTresource(jiucloud_menu_rcbg_child);
		jiucloud_menu_rcbg_funemaildellist.setSeq(10);
		jiucloud_menu_rcbg_funemaildellist
				.setUrl("/messageController/todelist");
		jiucloud_menu_rcbg_funemaildellist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_rcbg_funemaildellist);

		// 回收站
		Tresource cld_menu_rcbg_funemahuilist = new Tresource();
		cld_menu_rcbg_funemahuilist.setId("cld_menu_rcbg_funemahuilist");
		cld_menu_rcbg_funemahuilist.setName("回收站列表");
		cld_menu_rcbg_funemahuilist.setTresourcetype(funType);
		cld_menu_rcbg_funemahuilist
				.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_funemahuilist.setSeq(10);
		cld_menu_rcbg_funemahuilist.setUrl("/messageController/datadelGrid");
		cld_menu_rcbg_funemahuilist.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_funemahuilist);

		Tresource cld_menu_rcbg_cddelemahui = new Tresource();
		cld_menu_rcbg_cddelemahui.setId("cld_menu_rcbg_cddelemahui");
		cld_menu_rcbg_cddelemahui.setName("批量彻底删除");
		cld_menu_rcbg_cddelemahui.setTresourcetype(funType);
		cld_menu_rcbg_cddelemahui
				.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_cddelemahui.setSeq(10);
		cld_menu_rcbg_cddelemahui.setUrl("/messageController/batchdelrgc");
		cld_menu_rcbg_cddelemahui.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_cddelemahui);

		Tresource cld_menu_rcbg_picddelemahui = new Tresource();
		cld_menu_rcbg_picddelemahui.setId("cld_menu_rcbg_picddelemahui");
		cld_menu_rcbg_picddelemahui.setName("彻底删除");
		cld_menu_rcbg_picddelemahui.setTresourcetype(funType);
		cld_menu_rcbg_picddelemahui
				.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_picddelemahui.setSeq(10);
		cld_menu_rcbg_picddelemahui.setUrl("/messageController/delrgc");
		cld_menu_rcbg_picddelemahui.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_picddelemahui);

		Tresource cld_menu_rcbg_batchhuiui = new Tresource();
		cld_menu_rcbg_batchhuiui.setId("cld_menu_rcbg_batchhuiui");
		cld_menu_rcbg_batchhuiui.setName("批量恢复");
		cld_menu_rcbg_batchhuiui.setTresourcetype(funType);
		cld_menu_rcbg_batchhuiui
				.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_batchhuiui.setSeq(10);
		cld_menu_rcbg_batchhuiui.setUrl("/messageController/batchhui");
		cld_menu_rcbg_batchhuiui.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_batchhuiui);

		Tresource cld_menu_rcbg_piehuifu = new Tresource();
		cld_menu_rcbg_piehuifu.setId("cld_menu_rcbg_piehuifu");
		cld_menu_rcbg_piehuifu.setName("恢复");
		cld_menu_rcbg_piehuifu.setTresourcetype(funType);
		cld_menu_rcbg_piehuifu.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_piehuifu.setSeq(10);
		cld_menu_rcbg_piehuifu.setUrl("/messageController/huifu");
		cld_menu_rcbg_piehuifu.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_piehuifu);

		Tresource cld_menu_rcbg_piedetau = new Tresource();
		cld_menu_rcbg_piedetau.setId("cld_menu_rcbg_piedetau");
		cld_menu_rcbg_piedetau.setName("详情");
		cld_menu_rcbg_piedetau.setTresourcetype(funType);
		cld_menu_rcbg_piedetau.setTresource(jiucloud_menu_rcbg_funemaildellist);
		cld_menu_rcbg_piedetau.setSeq(10);
		cld_menu_rcbg_piedetau.setUrl("/messageController/dtau");
		cld_menu_rcbg_piedetau.setIcon("wrench");
		resourceDao.saveOrUpdate(cld_menu_rcbg_piedetau);
	}

	public void initGCMENU(Tresourcetype menuType, Tresourcetype funType) {
		Tresource jiucloud_menu_gc = new Tresource();
		jiucloud_menu_gc.setId("jiucloud_menu_gc");
		jiucloud_menu_gc.setName("工程管理");
		jiucloud_menu_gc.setTresourcetype(menuType);
		jiucloud_menu_gc.setSeq(0);
		jiucloud_menu_gc.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_gc);

		/******************************************************************************/

		// 现场数据管理
		Tresource jiucloud_menu_gc_feiylist = new Tresource();
		jiucloud_menu_gc_feiylist.setId("jiucloud_menu_gc_feiylist");
		jiucloud_menu_gc_feiylist.setName("现场数据管理");
		jiucloud_menu_gc_feiylist.setTresourcetype(menuType);
		jiucloud_menu_gc_feiylist.setTresource(jiucloud_menu_gc);
		jiucloud_menu_gc_feiylist.setSeq(10);
		jiucloud_menu_gc_feiylist.setUrl("/fieldDataController/fieldDataShow");
		jiucloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_gc_feiylist);

		// dataGrid
		// 工程名称管理
		Tresource jiucloud_menu_gc_gcmclist = new Tresource();
		jiucloud_menu_gc_gcmclist.setId("jiucloud_menu_gc_gcmclist");
		jiucloud_menu_gc_gcmclist.setName("项目登记管理");
		jiucloud_menu_gc_gcmclist.setTresourcetype(menuType);
		jiucloud_menu_gc_gcmclist.setTresource(jiucloud_menu_gc);
		jiucloud_menu_gc_gcmclist.setSeq(10);
		jiucloud_menu_gc_gcmclist.setUrl("/projectController/recProList");
		jiucloud_menu_gc_gcmclist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_gc_gcmclist);

		// 现场数据跳转添加
		Tresource cloud_menu_gc_xcdatalist = new Tresource();
		cloud_menu_gc_xcdatalist.setId("cloud_menu_gc_xcdatalist");
		cloud_menu_gc_xcdatalist.setName("现场数据跳转添加");
		cloud_menu_gc_xcdatalist.setTresourcetype(funType);
		cloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		cloud_menu_gc_xcdatalist.setSeq(10);
		cloud_menu_gc_xcdatalist.setUrl("/fieldDataController/addfieldData");
		cloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_xcdatalist);

		// 现场数据跳转添加附件
		Tresource cloud_menu_gc_xcdatafilelist = new Tresource();
		cloud_menu_gc_xcdatafilelist.setId("cloud_menu_gc_xcdatafilelist");
		cloud_menu_gc_xcdatafilelist.setName("现场数据跳转添加附件");
		cloud_menu_gc_xcdatafilelist.setTresourcetype(funType);
		cloud_menu_gc_xcdatafilelist.setTresource(jiucloud_menu_gc_feiylist);
		cloud_menu_gc_xcdatafilelist.setSeq(10);
		cloud_menu_gc_xcdatafilelist
				.setUrl("/fieldDataController/addfieldDataFile");
		cloud_menu_gc_xcdatafilelist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_xcdatafilelist);

		// 现场数据添加
		Tresource addcloud_menu_gc_xcdatalist = new Tresource();
		addcloud_menu_gc_xcdatalist.setId("addcloud_menu_gc_xcdatalist");
		addcloud_menu_gc_xcdatalist.setName("现场数据添加");
		addcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		addcloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		addcloud_menu_gc_xcdatalist.setSeq(10);
		addcloud_menu_gc_xcdatalist
				.setUrl("/fieldDataController/savefieldData");
		addcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(addcloud_menu_gc_xcdatalist);

		// 现场数据跳转修改
		Tresource upcloud_menu_gc_xcdatalist = new Tresource();
		upcloud_menu_gc_xcdatalist.setId("upcloud_menu_gc_xcdatalist");
		upcloud_menu_gc_xcdatalist.setName("现场数据跳转修改");
		upcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		upcloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		upcloud_menu_gc_xcdatalist.setSeq(10);
		upcloud_menu_gc_xcdatalist.setUrl("/fieldDataController/upfieldData");
		upcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(upcloud_menu_gc_xcdatalist);

		// 现场数据修改
		Tresource updatecloud_menu_gc_xcdatalist = new Tresource();
		updatecloud_menu_gc_xcdatalist.setId("updatecloud_menu_gc_xcdatalist");
		updatecloud_menu_gc_xcdatalist.setName("现场数据修改");
		updatecloud_menu_gc_xcdatalist.setTresourcetype(funType);
		updatecloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		updatecloud_menu_gc_xcdatalist.setSeq(10);
		updatecloud_menu_gc_xcdatalist
				.setUrl("/fieldDataController/updatefieldData");
		updatecloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(updatecloud_menu_gc_xcdatalist);

		// 现场数据删除
		Tresource delcloud_menu_gc_xcdatalist = new Tresource();
		delcloud_menu_gc_xcdatalist.setId("delcloud_menu_gc_xcdatalist");
		delcloud_menu_gc_xcdatalist.setName("现场数据删除");
		delcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		delcloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		delcloud_menu_gc_xcdatalist.setSeq(10);
		delcloud_menu_gc_xcdatalist.setUrl("/fieldDataController/delfieldData");
		delcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(delcloud_menu_gc_xcdatalist);

		// 现场数据删除
		Tresource detailcloud_menu_gc_xcdatalist = new Tresource();
		detailcloud_menu_gc_xcdatalist.setId("detailcloud_menu_gc_xcdatalist");
		detailcloud_menu_gc_xcdatalist.setName("现场数据详情");
		detailcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		detailcloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		detailcloud_menu_gc_xcdatalist.setSeq(10);
		detailcloud_menu_gc_xcdatalist
				.setUrl("/fieldDataController/detailfieldData");
		detailcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(detailcloud_menu_gc_xcdatalist);

		// 现场数据列表
		Tresource datacloud_menu_gc_xcdatalist = new Tresource();
		datacloud_menu_gc_xcdatalist.setId("datacloud_menu_gc_xcdatalist");
		datacloud_menu_gc_xcdatalist.setName("现场数据列表");
		datacloud_menu_gc_xcdatalist.setTresourcetype(funType);
		datacloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		datacloud_menu_gc_xcdatalist.setSeq(10);
		datacloud_menu_gc_xcdatalist.setUrl("/fieldDataController/dataGrid");
		datacloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(datacloud_menu_gc_xcdatalist);

		// 现场数据上传
		Tresource uploadcloud_menu_gc_xcdatalist = new Tresource();
		uploadcloud_menu_gc_xcdatalist.setId("uploadcloud_menu_gc_xcdatalist");
		uploadcloud_menu_gc_xcdatalist.setName("现场数据上传");
		uploadcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		uploadcloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		uploadcloud_menu_gc_xcdatalist.setSeq(10);
		uploadcloud_menu_gc_xcdatalist.setUrl("/fieldDataController/upload");
		uploadcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(uploadcloud_menu_gc_xcdatalist);

		// 现场数据下载
		Tresource downloadcloud_menu_gc_xcdatalist = new Tresource();
		downloadcloud_menu_gc_xcdatalist
				.setId("downloadcloud_menu_gc_xcdatalist");
		downloadcloud_menu_gc_xcdatalist.setName("现场数据下载");
		downloadcloud_menu_gc_xcdatalist.setTresourcetype(funType);
		downloadcloud_menu_gc_xcdatalist
				.setTresource(jiucloud_menu_gc_feiylist);
		downloadcloud_menu_gc_xcdatalist.setSeq(10);
		downloadcloud_menu_gc_xcdatalist
				.setUrl("/fieldDataController/download");
		downloadcloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(downloadcloud_menu_gc_xcdatalist);

		// 现场数据汇总
		Tresource summarycloud_menu_gc_xcdatalist = new Tresource();
		summarycloud_menu_gc_xcdatalist
				.setId("summarycloud_menu_gc_xcdatalist");
		summarycloud_menu_gc_xcdatalist.setName("现场数据汇总");
		summarycloud_menu_gc_xcdatalist.setTresourcetype(funType);
		summarycloud_menu_gc_xcdatalist.setTresource(jiucloud_menu_gc_feiylist);
		summarycloud_menu_gc_xcdatalist.setSeq(10);
		summarycloud_menu_gc_xcdatalist.setUrl("/fieldDataController/summary");
		summarycloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(summarycloud_menu_gc_xcdatalist);

		/****************************************** 推送权限 ********************************/
		Tresource jiucloud_menu_info_tuisong = new Tresource();
		jiucloud_menu_info_tuisong.setId("jiucloud_menu_info_tuisong");
		jiucloud_menu_info_tuisong.setName("消息推送");
		jiucloud_menu_info_tuisong.setTresourcetype(funType);
		jiucloud_menu_info_tuisong.setTresource(jiucloud_menu_gc_feiylist);// tem方案
		jiucloud_menu_info_tuisong.setSeq(10);
		jiucloud_menu_info_tuisong.setUrl("/informController/grantInform");
		jiucloud_menu_info_tuisong.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_info_tuisong);

		/******************************************************************************/

		// 查询工程名称list列表
		Tresource cloud_menu_gc_gcmclist = new Tresource();
		cloud_menu_gc_gcmclist.setId("cloud_menu_gc_gcmclist");
		cloud_menu_gc_gcmclist.setName("查询工程名称管理列表");
		cloud_menu_gc_gcmclist.setTresourcetype(funType);
		cloud_menu_gc_gcmclist.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_gcmclist.setSeq(10);
		cloud_menu_gc_gcmclist.setUrl("/projectController/dataGrid");
		cloud_menu_gc_gcmclist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_gcmclist);

		// 查询市级list列表
		Tresource cloud_menu_gc_citylist = new Tresource();
		cloud_menu_gc_citylist.setId("cloud_menu_gc_citylist");
		cloud_menu_gc_citylist.setName("查询市级信息列表");
		cloud_menu_gc_citylist.setTresourcetype(funType);
		cloud_menu_gc_citylist.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_citylist.setSeq(10);
		cloud_menu_gc_citylist.setUrl("/projectController/getCities");
		cloud_menu_gc_citylist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_citylist);

		// 跳转到新建工程名称页面
		Tresource cloud_menu_gc_createlist = new Tresource();
		cloud_menu_gc_createlist.setId("cloud_menu_gc_createlist");
		cloud_menu_gc_createlist.setName("跳转到新建工程名称页面");
		cloud_menu_gc_createlist.setTresourcetype(funType);
		cloud_menu_gc_createlist.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_createlist.setSeq(10);
		cloud_menu_gc_createlist.setUrl("/projectController/toAddPage");
		cloud_menu_gc_createlist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_createlist);

		// 执行新增工程名称功能
		Tresource cloud_menu_gc_add = new Tresource();
		cloud_menu_gc_add.setId("cloud_menu_gc_add");
		cloud_menu_gc_add.setName("执行新增功能");
		cloud_menu_gc_add.setTresourcetype(funType);
		cloud_menu_gc_add.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_add.setSeq(10);
		cloud_menu_gc_add.setUrl("/projectController/save");
		cloud_menu_gc_add.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_add);

		// 执行删除单个功能
		Tresource cloud_menu_gc_delone = new Tresource();
		cloud_menu_gc_delone.setId("cloud_menu_gc_delone");
		cloud_menu_gc_delone.setName("执行删除单个功能");
		cloud_menu_gc_delone.setTresourcetype(funType);
		cloud_menu_gc_delone.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_delone.setSeq(10);
		cloud_menu_gc_delone.setUrl("/projectController/deleteProject");
		cloud_menu_gc_delone.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_delone);

		// 执行批量删除功能
		Tresource cloud_menu_gc_batchdel = new Tresource();
		cloud_menu_gc_batchdel.setId("cloud_menu_gc_batchdel");
		cloud_menu_gc_batchdel.setName("执行批量删除功能");
		cloud_menu_gc_batchdel.setTresourcetype(funType);
		cloud_menu_gc_batchdel.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_batchdel.setSeq(10);
		cloud_menu_gc_batchdel.setUrl("/projectController/batchDeleteProject");
		cloud_menu_gc_batchdel.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_batchdel);

		// 执行预览单个项目功能
		Tresource cloud_menu_gc_findOneView = new Tresource();
		cloud_menu_gc_findOneView.setId("cloud_menu_gc_findOneView");
		cloud_menu_gc_findOneView.setName("执行预览单个项目功能");
		cloud_menu_gc_findOneView.setTresourcetype(funType);
		cloud_menu_gc_findOneView.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_findOneView.setSeq(10);
		cloud_menu_gc_findOneView.setUrl("/projectController/findOneView");
		cloud_menu_gc_findOneView.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_findOneView);

		// 跳转到编辑页面
		Tresource cloud_menu_gc_edit = new Tresource();
		cloud_menu_gc_edit.setId("cloud_menu_gc_edit");
		cloud_menu_gc_edit.setName("跳转到编辑页面");
		cloud_menu_gc_edit.setTresourcetype(funType);
		cloud_menu_gc_edit.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_edit.setSeq(10);
		cloud_menu_gc_edit.setUrl("/projectController/edit");
		cloud_menu_gc_edit.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_edit);

		// 执行修改功能
		Tresource cloud_menu_gc_update = new Tresource();
		cloud_menu_gc_update.setId("cloud_menu_gc_update");
		cloud_menu_gc_update.setName("执行修改功能");
		cloud_menu_gc_update.setTresourcetype(funType);
		cloud_menu_gc_update.setTresource(jiucloud_menu_gc_gcmclist);
		cloud_menu_gc_update.setSeq(10);
		cloud_menu_gc_update.setUrl("/projectController/update");
		cloud_menu_gc_update.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_update);

		/******************************************************************************/

	}

	public void initWDGM(Tresourcetype menuType, Tresourcetype funType) {
		Tresource jiucloud_menu_wdgm = new Tresource();
		jiucloud_menu_wdgm.setId("jiucloud_menu_wdgm");
		jiucloud_menu_wdgm.setName("我的购买");
		jiucloud_menu_wdgm.setTresourcetype(menuType);
		jiucloud_menu_wdgm.setSeq(0);
		jiucloud_menu_wdgm.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_wdgm);

		// 我的购买信息
		Tresource jiucloud_menu_wdgm_list = new Tresource();
		jiucloud_menu_wdgm_list.setId("jiucloud_menu_wdgm_list");
		jiucloud_menu_wdgm_list.setName("我的购买信息");
		jiucloud_menu_wdgm_list.setTresourcetype(menuType);
		jiucloud_menu_wdgm_list.setTresource(jiucloud_menu_wdgm);
		jiucloud_menu_wdgm_list.setSeq(10);
		jiucloud_menu_wdgm_list.setUrl("/userController/myOrderInfo");
		jiucloud_menu_wdgm_list.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_wdgm_list);

		// 查询我的购买信息列表list
		Tresource jiucloud_menu_wdgm_listdata = new Tresource();
		jiucloud_menu_wdgm_listdata.setId("jiucloud_menu_wdgm_listdata");
		jiucloud_menu_wdgm_listdata.setName("查询我的购买信息列表");
		jiucloud_menu_wdgm_listdata.setTresourcetype(funType);
		jiucloud_menu_wdgm_listdata.setTresource(jiucloud_menu_wdgm);
		jiucloud_menu_wdgm_listdata.setSeq(10);
		jiucloud_menu_wdgm_listdata.setUrl("/userController/orderDataGrid");
		jiucloud_menu_wdgm_listdata.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_wdgm_listdata);

		// 查询我的购买信息列表list
		Tresource jiucloud_menu_wdgm_wlistdata = new Tresource();
		jiucloud_menu_wdgm_wlistdata.setId("jiucloud_menu_wdgm_wlistdata");
		jiucloud_menu_wdgm_wlistdata.setName("查询购买预警信息列表");
		jiucloud_menu_wdgm_wlistdata.setTresourcetype(funType);
		jiucloud_menu_wdgm_wlistdata.setTresource(jiucloud_menu_wdgm);
		jiucloud_menu_wdgm_wlistdata.setSeq(10);
		jiucloud_menu_wdgm_wlistdata
				.setUrl("/userController/warnOrderDataGrid");
		jiucloud_menu_wdgm_wlistdata.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_wdgm_wlistdata);

		// 购买预警信息
		Tresource jiucloud_menu_wdgm_yjlist = new Tresource();
		jiucloud_menu_wdgm_yjlist.setId("jiucloud_menu_wdgm_yjlist");
		jiucloud_menu_wdgm_yjlist.setName("购买预警信息");
		jiucloud_menu_wdgm_yjlist.setTresourcetype(menuType);
		jiucloud_menu_wdgm_yjlist.setTresource(jiucloud_menu_wdgm);
		jiucloud_menu_wdgm_yjlist.setSeq(10);
		jiucloud_menu_wdgm_yjlist.setUrl("/userController/myWarnOrderInfo");
		jiucloud_menu_wdgm_yjlist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_wdgm_yjlist);
	}

	/**
	 * 数据统计分析
	 * 
	 * @param menuType
	 * @param funType
	 */
	public void initDATA(Tresourcetype menuType, Tresourcetype funType) {
		Tresource jiucloud_menu_data = new Tresource();
		jiucloud_menu_data.setId("jiucloud_menu_data");
		jiucloud_menu_data.setName("统计分析");
		jiucloud_menu_data.setTresourcetype(menuType);
		jiucloud_menu_data.setSeq(0);
		jiucloud_menu_data.setIcon("plugin");
		resourceDao.saveOrUpdate(jiucloud_menu_data);

		Tresource all_jiucloud_menu_data = new Tresource();
		all_jiucloud_menu_data.setId("all_jiucloud_menu_data");
		all_jiucloud_menu_data.setName("项目费用汇总");
		all_jiucloud_menu_data.setTresource(jiucloud_menu_data);
		all_jiucloud_menu_data.setTresourcetype(menuType);
		all_jiucloud_menu_data.setSeq(10);
		all_jiucloud_menu_data.setIcon("wrench");
		all_jiucloud_menu_data.setUrl("/analysisController/showTable");
		resourceDao.saveOrUpdate(all_jiucloud_menu_data);

		Tresource day_jiucloud_menu_data = new Tresource();
		day_jiucloud_menu_data.setId("day_jiucloud_menu_data");
		day_jiucloud_menu_data.setName("费用类型汇总");
		day_jiucloud_menu_data.setTresource(jiucloud_menu_data);
		day_jiucloud_menu_data.setTresourcetype(menuType);
		day_jiucloud_menu_data.setSeq(10);
		day_jiucloud_menu_data.setIcon("wrench");
		day_jiucloud_menu_data.setUrl("/analysisController/showDetail");
		resourceDao.saveOrUpdate(day_jiucloud_menu_data);

		Tresource zjiucloud_menu_system = new Tresource();
		zjiucloud_menu_system.setId("zjiucloud_menu_system");
		zjiucloud_menu_system.setName("系统设置");
		zjiucloud_menu_system.setTresourcetype(menuType);
		zjiucloud_menu_system.setSeq(0);
		zjiucloud_menu_system.setIcon("plugin");
		resourceDao.saveOrUpdate(zjiucloud_menu_system);

		Tresource item_jiucloud_menu_data = new Tresource();
		item_jiucloud_menu_data.setId("item_jiucloud_menu_data");
		item_jiucloud_menu_data.setName("汇总条目管理");
		item_jiucloud_menu_data.setTresource(zjiucloud_menu_system);
		item_jiucloud_menu_data.setTresourcetype(menuType);
		item_jiucloud_menu_data.setSeq(10);
		item_jiucloud_menu_data.setIcon("wrench");
		item_jiucloud_menu_data.setUrl("/priceController/manager");
		resourceDao.saveOrUpdate(item_jiucloud_menu_data);

		// 费用管理
		Tresource jiucloud_menu_gc_xcdatalist = new Tresource();
		jiucloud_menu_gc_xcdatalist.setId("jiucloud_menu_gc_xcdatalist");
		jiucloud_menu_gc_xcdatalist.setName("费用类型管理");
		jiucloud_menu_gc_xcdatalist.setTresourcetype(menuType);
		jiucloud_menu_gc_xcdatalist.setTresource(zjiucloud_menu_system);
		jiucloud_menu_gc_xcdatalist.setSeq(10);
		jiucloud_menu_gc_xcdatalist.setUrl("/costController/searchcost");
		jiucloud_menu_gc_xcdatalist.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_gc_xcdatalist);

		// 费用管理
		Tresource jiucloud_menu_gc_department = new Tresource();
		jiucloud_menu_gc_department.setId("jiucloud_menu_gc_department");
		jiucloud_menu_gc_department.setName("部门费用管理");
		jiucloud_menu_gc_department.setTresourcetype(menuType);
		jiucloud_menu_gc_department.setTresource(zjiucloud_menu_system);
		jiucloud_menu_gc_department.setSeq(10);
		jiucloud_menu_gc_department.setUrl("/costController/cost_department");
		jiucloud_menu_gc_department.setIcon("wrench");
		resourceDao.saveOrUpdate(jiucloud_menu_gc_department);

		Tresource searchcloud_menu_gc_feiylist = new Tresource();
		searchcloud_menu_gc_feiylist.setId("searchcloud_menu_gc_feiylist");
		searchcloud_menu_gc_feiylist.setName("费用类型查询");
		searchcloud_menu_gc_feiylist.setTresourcetype(funType);
		searchcloud_menu_gc_feiylist.setSeq(10);
		searchcloud_menu_gc_feiylist.setUrl("/costController/costShow");
		searchcloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(searchcloud_menu_gc_feiylist);
		// 费用管理
		Tresource cloud_menu_gc_feiylist = new Tresource();
		cloud_menu_gc_feiylist.setId("cloud_menu_gc_feiylist");
		cloud_menu_gc_feiylist.setName("费用类型列表");
		cloud_menu_gc_feiylist.setTresourcetype(funType);
		cloud_menu_gc_feiylist.setTresource(jiucloud_menu_gc_xcdatalist);
		cloud_menu_gc_feiylist.setSeq(10);
		cloud_menu_gc_feiylist.setUrl("/costController/dataGrid");
		cloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(cloud_menu_gc_feiylist);

		// 费用管理addCost
		Tresource addcloud_menu_gc_feiylist = new Tresource();
		addcloud_menu_gc_feiylist.setId("addcloud_menu_gc_feiylist");
		addcloud_menu_gc_feiylist.setName("费用类型增加");
		addcloud_menu_gc_feiylist.setTresourcetype(funType);
		addcloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		addcloud_menu_gc_feiylist.setSeq(10);
		addcloud_menu_gc_feiylist.setUrl("/costController/addCostPage");
		addcloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(addcloud_menu_gc_feiylist);

		// 查询一个cost家族集合
		Tresource familycloud_menu_gc_feiylist = new Tresource();
		familycloud_menu_gc_feiylist.setId("familycloud_menu_gc_feiylist");
		familycloud_menu_gc_feiylist.setName("查询一个cost家族集合");
		familycloud_menu_gc_feiylist.setTresourcetype(funType);
		familycloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		familycloud_menu_gc_feiylist.setSeq(10);
		familycloud_menu_gc_feiylist.setUrl("/costController/getFamily");
		familycloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(familycloud_menu_gc_feiylist);

		// 费用管理updateCost
		Tresource upcloud_menu_gc_feiylist = new Tresource();
		upcloud_menu_gc_feiylist.setId("upcloud_menu_gc_feiylist");
		upcloud_menu_gc_feiylist.setName("费用类型修改");
		upcloud_menu_gc_feiylist.setTresourcetype(funType);
		upcloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		upcloud_menu_gc_feiylist.setSeq(10);
		upcloud_menu_gc_feiylist.setUrl("/costController/updateCostPage");
		upcloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(upcloud_menu_gc_feiylist);

		// 保存费用管理
		Tresource savecloud_menu_gc_feiylist = new Tresource();
		savecloud_menu_gc_feiylist.setId("savecloud_menu_gc_feiylist");
		savecloud_menu_gc_feiylist.setName("费用类型保存");
		savecloud_menu_gc_feiylist.setTresourcetype(funType);
		savecloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		savecloud_menu_gc_feiylist.setSeq(10);
		savecloud_menu_gc_feiylist.setUrl("/costController/addCost");
		savecloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(savecloud_menu_gc_feiylist);

		// 删除费用管理
		Tresource delcloud_menu_gc_feiylist = new Tresource();
		delcloud_menu_gc_feiylist.setId("delcloud_menu_gc_feiylist");
		delcloud_menu_gc_feiylist.setName("费用类型删除");
		delcloud_menu_gc_feiylist.setTresourcetype(funType);
		delcloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		delcloud_menu_gc_feiylist.setSeq(10);
		delcloud_menu_gc_feiylist.setUrl("/costController/delCost");
		delcloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(delcloud_menu_gc_feiylist);

		// 修改费用管理
		Tresource updatecloud_menu_gc_feiylist = new Tresource();
		updatecloud_menu_gc_feiylist.setId("updatecloud_menu_gc_feiylist");
		updatecloud_menu_gc_feiylist.setName("费用类型修改");
		updatecloud_menu_gc_feiylist.setTresourcetype(funType);
		updatecloud_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		updatecloud_menu_gc_feiylist.setSeq(10);
		updatecloud_menu_gc_feiylist.setUrl("/costController/updateCost");
		updatecloud_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(updatecloud_menu_gc_feiylist);

		// tree
		Tresource tree_menu_gc_feiylist = new Tresource();
		tree_menu_gc_feiylist.setId("tree_menu_gc_feiylist");
		tree_menu_gc_feiylist.setName("费用类型tree");
		tree_menu_gc_feiylist.setTresourcetype(funType);
		tree_menu_gc_feiylist.setTresource(cloud_menu_gc_feiylist);
		tree_menu_gc_feiylist.setSeq(10);
		tree_menu_gc_feiylist.setUrl("/costController/tree");
		tree_menu_gc_feiylist.setIcon("wrench");
		resourceDao.saveOrUpdate(tree_menu_gc_feiylist);

		// 提升cost纪录的sort
		Tresource tree_menu_gc_upSort = new Tresource();
		tree_menu_gc_upSort.setId("tree_menu_gc_upSort");
		tree_menu_gc_upSort.setName("提升sort");
		tree_menu_gc_upSort.setTresourcetype(funType);
		tree_menu_gc_upSort.setTresource(cloud_menu_gc_feiylist);
		tree_menu_gc_upSort.setSeq(10);
		tree_menu_gc_upSort.setUrl("/costController/upSort");
		tree_menu_gc_upSort.setIcon("wrench");
		resourceDao.saveOrUpdate(tree_menu_gc_upSort);

		// 降低cost纪录的sort
		Tresource tree_menu_gc_downSort = new Tresource();
		tree_menu_gc_downSort.setId("tree_menu_gc_downSort");
		tree_menu_gc_downSort.setName("降低sort");
		tree_menu_gc_downSort.setTresourcetype(funType);
		tree_menu_gc_downSort.setTresource(cloud_menu_gc_feiylist);
		tree_menu_gc_downSort.setSeq(10);
		tree_menu_gc_downSort.setUrl("/costController/downSort");
		tree_menu_gc_downSort.setIcon("wrench");
		resourceDao.saveOrUpdate(tree_menu_gc_downSort);
	}

}
