package sy.service;

import org.springframework.stereotype.Repository;

import sy.model.po.Order;
import sy.pageModel.DataGrid;
import sy.pageModel.OrderSearch;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface OrderServiceI {

	/**
	 * 获取项目名称列表
	 */
	DataGrid dataGrid(OrderSearch app, PageHelper ph, String uid);

	/**
	 * 获取预警项目名称列表
	 */
	DataGrid warnDataGrid(OrderSearch app, PageHelper ph, String uid);

	/**
	 * 获取项目预览，查询一个
	 */

	/**
	 * 新增
	 */
	void add(Order order);

	/**
	 * 删除一个，软删除
	 * 
	 * @param id
	 */
	void deleteOne(Integer id);

	/**
	 * 查询一个（当执行编辑功能，预览功能调用此方法返回一个Order对象）
	 * 
	 * @param proId
	 * @return
	 */
	Order findOneView(Integer id);

	/**
	 * 修改
	 * 
	 * @param pro
	 * @param uid
	 */
	void update(Order order);

}
