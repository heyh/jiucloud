package sy.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import sy.model.po.GCPo;
import sy.model.po.Order;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface GCPoServiceI {
	/**
	 * 新增
	 */
	void add(GCPo gc);

	/**
	 * 删除一个
	 * 
	 * @param id
	 */
	void deleteOne(Integer id);

	/**
	 * 查询一个
	 * 
	 * @param proId
	 * @return
	 */
	GCPo findOneView(Integer id);

	/**
	 * 修改
	 * 
	 * @param pro
	 * @param uid
	 */
	void update(GCPo gc);

	DataGrid dataGrid(String id, String filename, String filetype);

	long getFieldCount(String mid);

	List<GCPo> getGcpoListFile(String type, String mpid);
}
