package sy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.ApplicationDaoI;
import sy.model.po.TApplication;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.ApplicationServiceI;
/**
 * ****************************************************************
 * 文件名称 : ApplicationServiceI.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:08:11
 * 文件描述 : 轻应用Service
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Service
public class ApplicationServiceImpl implements ApplicationServiceI {

	@Autowired
	private ApplicationDaoI appDao;

	/**
	 * 获得列表
	 */
	@Override
	public DataGrid dataGrid(AppSearch app, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TApplication t "+where(app,params);
		List<TApplication> l = appDao.find(hql, params,ph.getPage(), ph.getRows());
		for (TApplication p : l) {
			p.setRepDate(null);
			p.setIsRelease(null);
		}
		dg.setRows(l);
		dg.setTotal(appDao.count("select count(*) from TApplication t "+where(app,params),params));
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
				hql += " and t.appName like :name";
				params.put("name", "%%" + app.getAppcomp() + "%%");
			}
			if (app.getCreatedatetimeStart() != null) {
				hql += " and t.repTime >= :createdatetimeStart";
				params.put("createdatetimeStart", app.getCreatedatetimeStart());
			}
			if (app.getCreatedatetimeEnd() != null) {
				hql += " and t.repTime <= :createdatetimeEnd";
				params.put("createdatetimeEnd", app.getCreatedatetimeEnd());
			}
		}
		return hql;
	}
	
	/**
	 * 根据Id查询
	 * @return
	 */
	@Override
	public TApplication findById(String id){
		if(id==null){
			return null;
		}
		return appDao.get(TApplication.class, Integer.parseInt(id));
	}
	/**
	 * 删除信息
	 */
	@Override
	public void delete(String id) {
		try {
			if(id!=null){
				TApplication info = findById(id);
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
	public void add(TApplication info) {
		appDao.saveOrUpdate(info);
	}
	/**
	 * 发布
	 */
	@Override
	public void updateIsRep(String id) {
		if(id!=null){
			TApplication app = findById(id);
			if(app!=null){
				if(app.getIsRep()==1){
					app.setIsRep(0);
				}else{
					app.setIsRep(1);
					app.setRepTime(new Date());
				}
				add(app);
			}
		}
	}
	/**
	 * 查询所有(分页显示)
	 */
	@Override
	public List<TApplication> findAllApp(Integer page,Integer rows) {
		String hql = "from TApplication t where t.isDelete = 0 and t.isRep=1 ";
		return appDao.find(hql, page, rows);
	}
	

}
