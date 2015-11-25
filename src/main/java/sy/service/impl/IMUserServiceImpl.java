package sy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.IMUserDaoI;
import sy.model.IMUser;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.SynIMUser;
import sy.service.IMUserServiceI;
/**
 * ****************************************************************
 * 文件名称 : IMUserServiceImpl.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:08:11
 * 文件描述 : 轻应用Service
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Service
public class IMUserServiceImpl implements IMUserServiceI {

	@Autowired
	private IMUserDaoI imUserDao;

	/**
	 * 获得列表
	 */
	@Override
	public DataGrid dataGrid(AppSearch app, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from IMUser t "+where(app,params);
		List<IMUser> l = imUserDao.find(hql, params,ph.getPage(), ph.getRows());
		for (IMUser p : l) {
			p.setCreateTime(null);
			p.setIsSynchro(null);
		}
		dg.setRows(l);
		dg.setTotal(imUserDao.count("select count(*) from IMUser t "+where(app,params),params));
		return dg;
	}
	/**
	 * 查询条件
	 * @param app
	 * @param params
	 * @return
	 */
	private String where(AppSearch app, Map<String, Object> params) {
		String hql = " where t.isDelete = 0 ";
		if (app != null) {
			if (app.getAppcomp() != null&&app.getAppcomp()!="") {
				hql += " and t.username like :name";
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
		hql +=" order by t.isSyn ,t.ctime desc";
		return hql;
	}
	
	/**
	 * 根据Id查询
	 * @return
	 */
	@Override
	public IMUser findById(String id){
		if(id==null){
			return null;
		}
		return imUserDao.get(IMUser.class, Integer.parseInt(id));
	}
	/**
	 * 删除信息
	 */
	@Override
	public void delete(String id) {
		try {
			if(id!=null){
				IMUser info = findById(id);
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
	public void add(IMUser info) {
		imUserDao.saveOrUpdate(info);
	}
	
	/**
	 * 查看个人资料 
	 */
	@Override
	public IMUser findPuserByNameAndPwd(String username, String pass) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("pass", pass);
		String sql = " from IMUser u where u.username=:username and u.pass=:pass ";
		List<IMUser> list = imUserDao.find(sql, params);
		IMUser vu = new IMUser();
		if (list != null && list.size() != 0) {
			vu = list.get(0);
		}else{
			vu = null;
		}
		return vu; 
	}
	/**
	 * 修改姓名(昵称)
	 */
	public boolean updateRelName(IMUser vu) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", vu.getUsername());
		params.put("pass", vu.getPass());
		params.put("relname", vu.getRelname());
		String sql = "update IMUser v set v.relname=:relname  where v.username=:username and v.pass=:pass";
		return update(params,sql);
	}
	/**
	 * 修改返回信息
	 * @param params
	 * @return
	 */
	private boolean update(Map<String, Object> params,String sql){
		boolean isUpdate = false;
		if(imUserDao.executeSql(sql, params)>0){
			isUpdate=true;
		}
		return isUpdate;
	}
	/**
	 * 修改性别
	 */
	public boolean updateSex(IMUser vu){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", vu.getUsername());
		params.put("pass", vu.getPass());
		params.put("sex", vu.getSex());
		String sql = "update IMUser v set v.sex=:sex  where v.username=:username and v.pass=:pass";
		return update(params,sql);
	}
	/**
	 * 修改省份城市区县
	 */
	public boolean updateArea(IMUser vu){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", vu.getUsername());
		params.put("pass", vu.getPass());
		params.put("province", vu.getProvinceName());
		params.put("city", vu.getCityName());
		params.put("district", vu.getDistrictName());
		String sql = "update IMUser v set v.provinceName=:province,v.cityName=:city,v.districtName=:district  where v.username=:username and v.pass=:pass";
		return update(params,sql);
	}
	/**
	 * 修改头像
	 */
	@Override
	public void updateUariva(String id, String fileName) {
		Map<String, Object> mpr = new HashMap<String, Object>();
		mpr.put("image", fileName);
		this.imUserDao.executeSql("update IMUser v set v.uariva=:image where  v.id=" + Integer.parseInt(id), mpr);
	}
	
	/**
	 * 修改密码
	 */
	public boolean updatePwd(IMUser vu, String newPwd){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", vu.getUsername());
		params.put("pass", vu.getPass());
		params.put("newpass", newPwd);
		String sql = "update IMUser v set v.pass=:newpass  where v.username=:username and v.pass=:pass";
		return update(params,sql);
	}
	/**
	 * 查询好友信息
	 */
	@Override
	public IMUser findIMUserInfoByName(String relname) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", relname);
		String sql = " from IMUser u where u.username=:username ";
		List<IMUser> list = imUserDao.find(sql, params);
		IMUser vu = new IMUser();
		if (list != null && list.size() != 0) {
			vu = list.get(0);
		}else{
			vu = null;
		}
		return vu; 
	}
	
	/**
	 * 查看所有通讯录信息
	 */
	@Override
	public List<SynIMUser> findAll(String username) {
		Map<String,List<IMUser>> map=new HashMap<String,List<IMUser>>();
		List<IMUser> list=imUserDao.find("from IMUser t where t.isSyn=1 ");
		List<SynIMUser> imList = new ArrayList<SynIMUser>();
		for(IMUser lo:list){
			if(lo!=null){
				if(lo.getUsername().equals(username))
					continue;
			  String name=lo.getDepartment();
			  List<IMUser> value=map.get(name);
			  if(value==null)
			      value=new ArrayList<IMUser>();
			  value.add(lo);
			  map.put(name,value);
			}
		}
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
		    SynIMUser im = new SynIMUser();
		    im.setDepartment(iter.next());
		    im.setImuser(map.get(im.getDepartment()));
		    imList.add(im);
		}
		return imList;
	}
	/**
	 * 修改同步
	 */
	@Override
	public void updateIsSyn(String id) {
		this.imUserDao.executeSql("update IMUser v set v.isSyn=1 where v.id=" + Integer.parseInt(id));
	}
}
