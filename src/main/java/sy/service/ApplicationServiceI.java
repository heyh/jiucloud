package sy.service;

import java.util.List;

import sy.model.po.TApplication;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;


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
public interface ApplicationServiceI {

	/**
	 * 获得列表
	 */
	public DataGrid dataGrid(AppSearch app, PageHelper ph);

	/**
	 * 
	 * 删除
	 * @param id
	 * void
	 */
	public void delete(String id);
	
	/**
	 * 添加
	 */
	public void add(TApplication info);
	/**
	 * 根据Id查询
	 * @return
	 */
	public TApplication findById(String id);
	/**
	 * 发布
	 * 方法表述
	 * @param id
	 * void
	 */
	public void updateIsRep(String id);
	/**
	 * 获得所有应用 (分页显示)
	 * 方法表述
	 * @param page
	 * @param rows
	 * @return
	 * List<TApplication>
	 */
	public List<TApplication> findAllApp(Integer page,Integer rows);
}
