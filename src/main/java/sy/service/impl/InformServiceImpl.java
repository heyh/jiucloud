package sy.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import sy.controller.SynchronizationController;
import sy.dao.ApplicationDaoI;
import sy.dao.InformDaoI;
import sy.model.po.TApplication;
import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
import sy.pageModel.Json;
import sy.pageModel.Messages;
import sy.pageModel.PageHelper;
import sy.pageModel.SentMsg;
import sy.service.ApplicationServiceI;
import sy.service.InformServiceI;
import sy.util.ConfigUtil;
import sy.util.DateKit;
/**
 * ****************************************************************
 * 文件名称 : InformServiceImpl.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月24日 上午10:27:41
 * 文件描述 : 服务号通知service
 * 版权声明 : 
 * 修改历史 : 2014年12月24日 1.00 初始版本
 *****************************************************************
 */
@Service
public class InformServiceImpl implements InformServiceI {

	@Autowired
	private InformDaoI infoDao;
	
	/**
	 *	获得当前用户下所有推送消息 
	 * 方法表述
	 * @param fwName
	 * @return
	 * List<TInform>
	 */
	@Override
	public List<TInform> getAllInfo(String fwName,String infoTitle){
		String hql = "from TInform t where t.isDelete = 0 and t.fwName = '"+fwName+"' ";
		Map<String, Object> params = new HashMap<String, Object>();
		if(infoTitle!=null&&!infoTitle.equals("")){
			params.put("infoTitle", "%%" + infoTitle + "%%");
			hql +=" and t.title like :infoTitle";
		}
		List<TInform> l= infoDao.find(hql,params);
		for (TInform t : l) {
			t.setSimpleCon(null);
		}
		return l;
	}

	/**
	 * 获得列表
	 */
	@Override
	public DataGrid dataGrid(AppSearch app, PageHelper ph,String fwName) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TInform t "+where(app,params,fwName);
		List<TInform> l = infoDao.find(hql, params,ph.getPage(), ph.getRows());
		for (TInform p : l) {
			p.setCreateTime(null);
			p.setIsOpen(null);
		}
		dg.setRows(l);
		dg.setTotal(infoDao.count("select count(*) from TInform t "+where(app,params,fwName),params));
		return dg;
	}
	/**
	 * 查询条件
	 * @param app
	 * @param params
	 * @return
	 */
	private String where(AppSearch app, Map<String, Object> params,String fwName) {
		String hql =  "";
		if(fwName.startsWith(ConfigUtil.FW_NAME)){
			hql = " where t.isDelete = 0 and t.fwName = '"+fwName+"' ";
		}else{
			params.put("fwName", "fw1%%");
			hql = " where t.isDelete = 0 and t.fwName like :fwName";
		}
		if (app != null) {
			if (app.getAppcomp() != null&&app.getAppcomp()!="") {
				hql += " and t.title like :name";
				params.put("name", "%%" + app.getAppcomp() + "%%");
			}
			if (app.getCreatedatetimeStart() != null) {
				hql += " and t.ctime >= :createdatetimeStart";
				params.put("createdatetimeStart", app.getCreatedatetimeStart());
			}
			if (app.getCreatedatetimeEnd() != null) {
				hql += " and t.ctime <= :createdatetimeEnd";
				params.put("createdatetimeEnd", app.getCreatedatetimeEnd());
			}
		}
		return hql;
	}
	
	/**
	 * 根据Id查询
	 * @return
	 */
	public TInform findById(String id){
		if(id==null){
			return null;
		}
		return infoDao.get(TInform.class, Integer.parseInt(id));
	}

	/**
	 * 删除信息
	 */
	@Override
	public void delete(String id) {
		try {
			if(id!=null){
				TInform info = findById(id);
				if(info!=null){
					info.setIsDelete(1);
					add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加
	 */
	@Override
	public Integer add(TInform info) {
		Integer id ;
		if(info.getId()>0){
			infoDao.update(info);
			id = info.getId();
		}else{
			id = (Integer) infoDao.save(info);
		}
		return id;
	}
	/**
	 * 发布
	 */
	@Override
	public TInform updateIsRep(String id) {
		TInform app = null;
		if(id!=null){
			app = findById(id);
			if(app!=null){
				if(app.getIsok()==3){
					app.setIsok(1);
				}else{
					app.setIsok(1);
					app.setIsoktime(new Date());
				}
				if(app.getHtmlPath().lastIndexOf(".html")==-1)
					app.setHtmlPath(app.getHtmlPath()+id+".html");//修改html路径
				add(app);
			}
		}
		return app;
	}
	/**
	 * 修改为已发送
	 */
	public void updateSent(String id){
		TInform app = findById(id);
		app.setIsok(3);
		add(app);
	}
	
	/**
	 * 定时发送消息
	 * 方法表述
	 * void
	 */
	synchronized public void sentMsgByTime(){
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = smf.format(new Date());
		params.put("time", date);
		String hql = "from TInform where isDelete = 0 and isok = 2 and to_char(isoktime,'yyyy-MM-dd hh24:mi')=:time ";
		List<TInform> list = infoDao.find(hql, params);
		if(list.size()>0){
			for (TInform tInform : list) {
				sentFwMsg(tInform.getId()+"");//发送服务号消息
			}
		}
	}

	/**
	 * 发送服务号消息
	 * 方法表述
	 * @param id
	 * void
	 */
	public void sentFwMsg(String id){
		TInform info = this.updateIsRep(id);//修改是否推出状态
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
		if(info!=null){
			if(info.getIsok()==1){
				Inform ifo = new Inform();
				ifo.setTitle(info.getTitle());
				String[]str = info.getContent().split("</p>");
				ifo.setContent(str[0].replaceAll("<p>", "")+"...");
				if(info.getHtmlPath().lastIndexOf(".html")==-1)
					info.setHtmlPath(info.getHtmlPath()+id+".html");//修改html路径
				ifo.setHtmlPath(info.getHtmlPath());
				ifo.setFwName(info.getFwName());
				ifo.setIsoktime(sfd.format(info.getIsoktime()));//时间
				ifo.setPicPath(info.getPicPath());
				SentMsg sentMsg = new SentMsg();
				sentMsg.setFrom(info.getFwName());//发送人
				//获取该服务号下的好友信息
				List<String> users = new ArrayList<String>();
				JSONArray arr = SynchronizationController.getFriend(sentMsg.getFrom(),SynchronizationController.getToken()); //获得好友
				for (Object obj : arr) {
					users.add((String)obj);
				}
				sentMsg.setTarget(users);//要发送的人
				
				Messages msg = new Messages();
				msg.setMsg(ifo);//发送消息
				sentMsg.setMsg(msg);
				SynchronizationController.sentMsg(sentMsg, SynchronizationController.getToken());//发送
				updateSent(id);//修改为已发送
			}
		}
	}
	/**
	 * 发送消息给指定的人
	 * 方法表述
	 * @param id 服务号消息id
	 * @param userName 要发送的人
	 * void
	 */
	public void sentFwMsg(String id,String userName){
		TInform info = this.updateIsRep(id);//修改是否推出状态
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
		Inform ifo = new Inform();
		if(info!=null){
			if(info.getIsok()==1){
				ifo.setTitle(info.getTitle());
				String[]str = info.getContent().split("</p>");
				ifo.setContent(str[0].replaceAll("<p>", "")+"...");
				if(info.getHtmlPath().lastIndexOf(".html")==-1)
					info.setHtmlPath(info.getHtmlPath()+id+".html");//修改html路径
				ifo.setHtmlPath(info.getHtmlPath());
				ifo.setFwName(info.getFwName());
				ifo.setIsoktime(sfd.format(info.getIsoktime()));//时间
				ifo.setPicPath(info.getPicPath());
				SentMsg sentMsg = new SentMsg();
				sentMsg.setFrom(info.getFwName());//发送人
				//获取该服务号下的好友信息
				List<String> users = new ArrayList<String>();
				users.add(userName);
				sentMsg.setTarget(users);//要发送的人
				
				Messages msg = new Messages();
				msg.setMsg(ifo);//发送消息
				sentMsg.setMsg(msg);
				SynchronizationController.sentMsg(sentMsg, SynchronizationController.getToken());//发送
				updateSent(id);//修改为已发送
			}
		}
	}
	/**
	 * 获得服务号消息
	 * 方法表述
	 * @param id
	 * @return
	 * Inform
	 */
	public Inform getFwMsg(String id){
		TInform info = this.findById(id);//修改是否推出状态
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
		Inform ifo = new Inform();
		if(info!=null){
			ifo.setTitle(info.getTitle());
			String[]str = info.getContent().split("</p>");
			ifo.setContent(str[0].replaceAll("<p>", "")+"...");
			if(info.getHtmlPath().lastIndexOf(".html")==-1)
				info.setHtmlPath(info.getHtmlPath()+id+".html");//修改html路径
			ifo.setHtmlPath(info.getHtmlPath());
			ifo.setFwName(info.getFwName());
			ifo.setIsoktime(sfd.format(info.getIsoktime()));//时间
			ifo.setPicPath(info.getPicPath());
		}
		return ifo;
	}
	
}
