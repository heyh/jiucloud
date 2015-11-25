package sy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.MessageDaoI;
import sy.dao.MessageTextDaoI;
import sy.model.po.MessageText;
import sy.model.po.TInform;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
import sy.pageModel.Message;
import sy.pageModel.MessageSearch;
import sy.pageModel.PageHelper;
import sy.service.MessageTextServiceI;
import sy.service.UserServiceI;

/**
 * **************************************************************** 文件名称 :
 * IMUserServiceImpl.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:08:11 文件描述
 * : 轻应用Service 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Service
public class MessageTextServiceImpl implements MessageTextServiceI {

	@Autowired
	private MessageTextDaoI messageTextDao;

	@Autowired
	private MessageDaoI messageDao;

	@Autowired
	private UserServiceI userService;

	@Override
	public Integer add(TInform info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGrid dataGrid(MessageSearch app, PageHelper ph, String uid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuffer sb = new StringBuffer();
		sb.append("select m.recid,(select u.user_name from jsw_user u where u.user_id=m.recid) as uname ,mt.title,m.statue,m.id,m.messageid,mt.message,mt.pdate ");
		sb.append("	from tgc_Message m,tgc_MessageText mt ");
		sb.append(where(app, params, uid));
		System.out.println("tset");
		List<Object[]> l = messageTextDao.findBySql(sb.toString(), params,
				ph.getPage(), ph.getRows());
		List<Message> messageVo = new ArrayList<Message>();
		for (Object[] p : l) {
			Message msg = new Message();
			if (p[0] != null) {
				msg.setRecid(p[0].toString());
			}
			if (p[1] != null) {
				msg.setRecName(p[1].toString());
			}
			if (p[2] != null) {
				msg.setTitle(p[2].toString());
			}
			if (p[3] != null) {
				// msg.setStatue(((BigDecimal)p[3]).intValue());
				msg.setStatue(((Integer) p[3]).intValue());
			}
			if (p[4] != null) {
				// msg.setId(((BigDecimal)p[4]).intValue());
				msg.setId(((Integer) p[4]).intValue());
			}
			if (p[5] != null) {
				// msg.setMessageid(((BigDecimal)p[5]).intValue()+"");
				msg.setMessageid(((Integer) p[5]).intValue() + "");
			}
			if (p[6] != null) {
				// msg.setMessage(p[6]);
			}
			if (p[7] != null) {
				msg.setPdate((Date) p[7]);
			}
			messageVo.add(msg);
			// System.out.println(messageTextDao.get("from MessageText t where t.id="+msg.getMessageid()
			// ).getMessage());
		}
		dg.setRows(messageVo);
		List<Object[]> list = messageTextDao.findBySql(sb.toString(), params);
		dg.setTotal(Long.parseLong(list.size() + ""));
		return dg;
	}

	@Override
	public DataGrid datarecGrid(MessageSearch app, PageHelper ph, String uid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuffer sb = new StringBuffer();
		sb.append("select m.recid,(select u.user_name from jsw_user u where u.user_id=m.sendid) as uname ,mt.title,m.statue,m.id,m.messageid,mt.message,mt.pdate ");
		sb.append("	from tgc_Message m,tgc_MessageText mt ");
		sb.append(whereRec(app, params, uid));
		System.out
				.println("--------------------------------------------------------");
		System.out.println(sb.toString());
		List<Object[]> l = messageTextDao.findBySql(sb.toString(), params,
				ph.getPage(), ph.getRows());
		List<Message> messageVo = new ArrayList<Message>();
		for (Object[] p : l) {
			Message msg = new Message();
			if (p[0] != null) {
				msg.setRecid(p[0].toString());
			}
			if (p[1] != null) {
				msg.setRecName(p[1].toString());
			}
			if (p[2] != null) {
				msg.setTitle(p[2].toString());
			}
			if (p[3] != null) {
				msg.setStatue((Integer) (p[3]));
			}
			if (p[4] != null) {
				msg.setId(((Integer) p[4]));
			}
			if (p[5] != null) {
				msg.setMessageid(p[5].toString());
			}
			if (p[6] != null) {
				// msg.setMessage(p[6]);
			}
			if (p[7] != null) {
				msg.setPdate((Date) p[7]);
			}
			messageVo.add(msg);
			// System.out.println(messageTextDao.get("from MessageText t where t.id="+msg.getMessageid()
			// ).getMessage());
		}
		dg.setRows(messageVo);
		List<Object[]> list = messageTextDao.findBySql(sb.toString(), params);
		dg.setTotal(Long.parseLong(list.size() + ""));
		return dg;
	}

	@Override
	public List<MessageText> getfindList(String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		System.out
				.println("--------------------------------------------------------");
		List<sy.model.po.Message> messages = messageDao.find(
				"from Message where statue=0 and isdel=1 and recid=:uid ",
				params, 0, 8);
		StringBuffer sb = new StringBuffer();
		List<MessageText> list = new ArrayList<MessageText>();
		if (messages != null && messages.size() > 0) {
			sb.append("from MessageText where id in(");
			sb.append(messages.get(0).getId());
			for (int i = 1; i < messages.size(); i++) {
				sb.append("," + messages.get(i).getId());
			}
			sb.append(")");
			list = messageTextDao.find(sb.toString());
		}
		return list;
	}

	/**
	 * 获得回收站
	 */
	@Override
	public DataGrid datadelGrid(MessageSearch app, PageHelper ph, String uid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuffer sb = new StringBuffer();
		sb.append("select m.recid,(select u.user_name from jsw_user u where u.user_id=m.sendid) as uname ,mt.title,m.statue,m.id,m.messageid,mt.message,mt.pdate ");
		// sb.append(",(select u.realname from tuser u where u.id=m.sendid) as sname");
		sb.append("	from tgc_Message m,tgc_MessageText mt ");
		sb.append(whereDel(app, params, uid));

		System.out.println(sb.toString());
		List<Object[]> l = messageTextDao.findBySql(sb.toString(), params,
				ph.getPage(), ph.getRows());
		List<Message> messageVo = new ArrayList<Message>();
		for (Object[] p : l) {
			Message msg = new Message();
			if (p[0] != null) {
				msg.setRecid(p[0].toString());
			}
			if (p[1] != null) {
				msg.setSendName(p[1].toString());
			}
			if (p[2] != null) {
				msg.setTitle(p[2].toString());
			}
			if (p[3] != null) {
				// msg.setStatue(((BigDecimal)p[3]).intValue());
				msg.setStatue(((Integer) p[3]).intValue());
			}
			if (p[4] != null) {
				// msg.setId(((BigDecimal)p[4]).intValue());
				msg.setId(((Integer) p[4]).intValue());
			}
			if (p[5] != null) {
				// msg.setMessageid(((BigDecimal)p[5]).intValue()+"");
				msg.setMessageid(((Integer) p[5]).intValue() + "");
			}
			if (p[6] != null) {
				// msg.setMessage(p[6]);
			}
			if (p[7] != null) {
				msg.setPdate((Date) p[7]);
			}

			// if(p[8] != null){
			// msg.setSendName(p[8].toString());
			// }
			messageVo.add(msg);
			// System.out.println(messageTextDao.get("from MessageText t where t.id="+msg.getMessageid()
			// ).getMessage());
		}
		dg.setRows(messageVo);
		List<Object[]> list = messageTextDao.findBySql(sb.toString(), params);
		dg.setTotal(Long.parseLong(list.size() + ""));
		return dg;
	}

	private Object whereDel(MessageSearch app, Map<String, Object> params,
			String uid) {
		String hql = "  where  ((m.sendid=:sendid and m.senderIsdel=0) or (m.recid=:recid and m.isdel=0))  and m.messageid=mt.id     ";
		params.put("sendid", uid);
		params.put("recid", uid);
		if (app != null) {
			if (app.getTitle() != null && app.getTitle() != "") {
				hql += " and mt.title like :title";
				params.put("title", "%%" + app.getTitle() + "%%");
			}
			if (app.getAppcomp() != null && !app.getAppcomp().equals("")) {
				hql += " and m.statue=" + app.getAppcomp();
			}
			if (app.getCreatedatetimeStart() != null) {
				hql += " and mt.pdate >= :createdatetimeStart";
				params.put("createdatetimeStart", app.getCreatedatetimeStart());
			}
			if (app.getCreatedatetimeEnd() != null) {
				hql += " and mt.pdate <= :createdatetimeEnd";
				params.put("createdatetimeEnd", app.getCreatedatetimeEnd());
			}
		}
		hql += " order by m.id desc";
		return hql;
	}

	/**
	 * 查询条件
	 * 
	 * @param app
	 * @param params
	 * @return
	 */
	private String where(MessageSearch app, Map<String, Object> params,
			String uname) {
		String hql = "  where  m.sendid=:sendid and m.messageid=mt.id and m.senderIsdel=1 ";

		params.put("sendid", uname);
		if (app != null) {
			if (app.getTitle() != null && app.getTitle() != "") {
				hql += " and mt.title like :title";
				params.put("title", "%%" + app.getTitle() + "%%");
			}
			if (app.getAppcomp() != null && !app.getAppcomp().equals("")) {
				hql += " and m.statue=" + app.getAppcomp();
			}
			if (app.getCreatedatetimeStart() != null) {
				hql += " and mt.pdate >= :createdatetimeStart";
				params.put("createdatetimeStart", app.getCreatedatetimeStart());
			}
			if (app.getCreatedatetimeEnd() != null) {
				hql += " and mt.pdate <= :createdatetimeEnd";
				params.put("createdatetimeEnd", app.getCreatedatetimeEnd());
			}
		}
		hql += " order by m.id desc";
		return hql;
	}

	/**
	 * 查询条件
	 * 
	 * @param app
	 * @param params
	 * @return
	 */
	private String whereRec(MessageSearch app, Map<String, Object> params,
			String uname) {
		String hql = "  where  m.recid=:recid and m.messageid=mt.id and m.isdel=1 ";
		params.put("recid", uname);
		if (app != null) {
			if (app.getTitle() != null && app.getTitle() != "") {
				hql += " and mt.title like :title";
				params.put("title", "%%" + app.getTitle() + "%%");
			}
			if (app.getAppcomp() != null && !app.getAppcomp().equals("")) {
				hql += " and m.statue=" + app.getAppcomp();
			}
			if (app.getCreatedatetimeStart() != null) {
				hql += " and mt.pdate >= :createdatetimeStart";
				params.put("createdatetimeStart", app.getCreatedatetimeStart());
			}
			if (app.getCreatedatetimeEnd() != null) {
				hql += " and mt.pdate <= :createdatetimeEnd";
				params.put("createdatetimeEnd", app.getCreatedatetimeEnd());
			}
		}
		hql += " order by m.id desc";
		return hql;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message read(String id) {
		sy.model.po.Message m = this.messageDao
				.get("from Message m where m.id=" + id);
		// 更新已读
		if (m.getStatue() == 0) {
			m.setReadtime(new Date());
			m.setStatue(1); // 已读
			this.messageDao.update(m);
		}
		sy.model.po.MessageText mt = this.messageTextDao
				.get("from MessageText j where j.id=" + m.getMessageid());
		Message mvo = new Message();
		mvo.setId(m.getId());
		mvo.setMessage(mt.getMessage());
		mvo.setMessageid(mt.getId() + "");
		mvo.setPdate(mt.getPdate());
		mvo.setRecName(userService.getUser(m.getRecid()).getRealname());
		mvo.setSendName(userService.getUser(m.getSendId()).getRealname());

		mvo.setStatue(m.getStatue());
		mvo.setTitle(mt.getTitle());
		return mvo;
	}

	public sy.pageModel.Message detail(String msgid) {
		sy.model.po.Message m = this.messageDao
				.get("from Message m where m.id=" + msgid);
		sy.model.po.MessageText mt = this.messageTextDao
				.get("from MessageText j where j.id=" + m.getMessageid());
		Message mvo = new Message();
		mvo.setId(m.getId());
		mvo.setMessage(mt.getMessage());
		mvo.setMessageid(mt.getId() + "");
		mvo.setPdate(mt.getPdate());
		mvo.setRecName(userService.getUser(m.getRecid()).getRealname());
		mvo.setSendName(userService.getUser(m.getSendId()).getRealname());
		mvo.setStatue(m.getStatue());
		mvo.setTitle(mt.getTitle());
		mvo.setReadtime(m.getReadtime());
		return mvo;
	}

	@Override
	public List<TInform> getAllInfo(String fwName, String infoTitle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inform getFwMsg(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sentFwMsg(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sentFwMsg(String id, String userName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sentMsgByTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public TInform updateIsRep(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获得列表
	 */
	/*
	 * @Override public DataGrid dataGrid(AppSearch app, PageHelper ph) {
	 * DataGrid dg = new DataGrid(); Map<String, Object> params = new
	 * HashMap<String, Object>(); String hql =
	 * " from IMUser t "+where(app,params); List<IMUser> l = imUserDao.find(hql,
	 * params,ph.getPage(), ph.getRows()); for (IMUser p : l) {
	 * p.setCreateTime(null); p.setIsSynchro(null); } dg.setRows(l);
	 * dg.setTotal(
	 * imUserDao.count("select count(*) from IMUser t "+where(app,params
	 * ),params)); return dg; }
	 *//**
	 * 查询条件
	 * 
	 * @param app
	 * @param params
	 * @return
	 */
	/*
	 * private String where(AppSearch app, Map<String, Object> params) { String
	 * hql = " where t.isDelete = 0 "; if (app != null) { if (app.getAppcomp()
	 * != null&&app.getAppcomp()!="") { hql += " and t.username like :name";
	 * params.put("name", "%%" + app.getAppcomp() + "%%"); } if
	 * (app.getCreatedatetimeStart() != null) { hql +=
	 * " and t.ctime >= :createdatetimeStart"; params.put("createdatetimeStart",
	 * app.getCreatedatetimeStart()); } if (app.getCreatedatetimeEnd() != null)
	 * { hql += " and t.ctime <= :createdatetimeEnd";
	 * params.put("createdatetimeEnd", app.getCreatedatetimeEnd()); } } hql
	 * +=" order by t.isSyn ,t.ctime desc"; return hql; }
	 *//**
	 * 根据Id查询
	 * 
	 * @return
	 */
	/*
	 * @Override public IMUser findById(String id){ if(id==null){ return null; }
	 * return imUserDao.get(IMUser.class, Integer.parseInt(id)); }
	 *//**
	 * 删除信息
	 */
	/*
	 * @Override public void delete(String id) { try { if(id!=null){ IMUser info
	 * = findById(id); if(info!=null){ info.setIsDelete(1); add(info); } } }
	 * catch (Exception e) { e.printStackTrace(); } }
	 *//**
	 * 添加
	 */
	/*
	 * @Override public void add(IMUser info) { imUserDao.saveOrUpdate(info); }
	 *//**
	 * 查看个人资料
	 */
	/*
	 * @Override public IMUser findPuserByNameAndPwd(String username, String
	 * pass) { Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("username", username); params.put("pass", pass); String sql =
	 * " from IMUser u where u.username=:username and u.pass=:pass ";
	 * List<IMUser> list = imUserDao.find(sql, params); IMUser vu = new
	 * IMUser(); if (list != null && list.size() != 0) { vu = list.get(0);
	 * }else{ vu = null; } return vu; }
	 *//**
	 * 修改姓名(昵称)
	 */
	/*
	 * public boolean updateRelName(IMUser vu) { Map<String, Object> params =
	 * new HashMap<String, Object>(); params.put("username", vu.getUsername());
	 * params.put("pass", vu.getPass()); params.put("relname", vu.getRelname());
	 * String sql =
	 * "update IMUser v set v.relname=:relname  where v.username=:username and v.pass=:pass"
	 * ; return update(params,sql); }
	 *//**
	 * 修改返回信息
	 * 
	 * @param params
	 * @return
	 */
	/*
	 * private boolean update(Map<String, Object> params,String sql){ boolean
	 * isUpdate = false; if(imUserDao.executeSql(sql, params)>0){ isUpdate=true;
	 * } return isUpdate; }
	 *//**
	 * 修改性别
	 */
	/*
	 * public boolean updateSex(IMUser vu){ Map<String, Object> params = new
	 * HashMap<String, Object>(); params.put("username", vu.getUsername());
	 * params.put("pass", vu.getPass()); params.put("sex", vu.getSex()); String
	 * sql =
	 * "update IMUser v set v.sex=:sex  where v.username=:username and v.pass=:pass"
	 * ; return update(params,sql); }
	 *//**
	 * 修改省份城市区县
	 */
	/*
	 * public boolean updateArea(IMUser vu){ Map<String, Object> params = new
	 * HashMap<String, Object>(); params.put("username", vu.getUsername());
	 * params.put("pass", vu.getPass()); params.put("province",
	 * vu.getProvinceName()); params.put("city", vu.getCityName());
	 * params.put("district", vu.getDistrictName()); String sql =
	 * "update IMUser v set v.provinceName=:province,v.cityName=:city,v.districtName=:district  where v.username=:username and v.pass=:pass"
	 * ; return update(params,sql); }
	 *//**
	 * 修改头像
	 */
	/*
	 * @Override public void updateUariva(String id, String fileName) {
	 * Map<String, Object> mpr = new HashMap<String, Object>(); mpr.put("image",
	 * fileName);
	 * this.imUserDao.executeSql("update IMUser v set v.uariva=:image where  v.id="
	 * + Integer.parseInt(id), mpr); }
	 *//**
	 * 修改密码
	 */
	/*
	 * public boolean updatePwd(IMUser vu, String newPwd){ Map<String, Object>
	 * params = new HashMap<String, Object>(); params.put("username",
	 * vu.getUsername()); params.put("pass", vu.getPass());
	 * params.put("newpass", newPwd); String sql =
	 * "update IMUser v set v.pass=:newpass  where v.username=:username and v.pass=:pass"
	 * ; return update(params,sql); }
	 *//**
	 * 查询好友信息
	 */
	/*
	 * @Override public IMUser findIMUserInfoByName(String relname) {
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("username", relname); String sql =
	 * " from IMUser u where u.username=:username "; List<IMUser> list =
	 * imUserDao.find(sql, params); IMUser vu = new IMUser(); if (list != null
	 * && list.size() != 0) { vu = list.get(0); }else{ vu = null; } return vu; }
	 *//**
	 * 查看所有通讯录信息
	 */
	/*
	 * @Override public List<SynIMUser> findAll(String username) {
	 * Map<String,List<IMUser>> map=new HashMap<String,List<IMUser>>();
	 * List<IMUser> list=imUserDao.find("from IMUser t where t.isSyn=1 ");
	 * List<SynIMUser> imList = new ArrayList<SynIMUser>(); for(IMUser lo:list){
	 * if(lo!=null){ if(lo.getUsername().equals(username)) continue; String
	 * name=lo.getDepartment(); List<IMUser> value=map.get(name);
	 * if(value==null) value=new ArrayList<IMUser>(); value.add(lo);
	 * map.put(name,value); } } Iterator<String> iter = map.keySet().iterator();
	 * while (iter.hasNext()) { SynIMUser im = new SynIMUser();
	 * im.setDepartment(iter.next()); im.setImuser(map.get(im.getDepartment()));
	 * imList.add(im); } return imList; }
	 *//**
	 * 修改同步
	 */
	/*
	 * @Override public void updateIsSyn(String id) {
	 * this.imUserDao.executeSql("update IMUser v set v.isSyn=1 where v.id=" +
	 * Integer.parseInt(id)); }
	 */
}
