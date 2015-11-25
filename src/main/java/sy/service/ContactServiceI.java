package sy.service;

import org.springframework.stereotype.Repository;

import sy.model.Contact;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface ContactServiceI {

	public DataGrid dataGrid(int pid, PageHelper ph);

	public void delete(Contact tem);

	public Contact update(Contact tem);

	public Contact findoneview(int id);

	public Contact add(Contact tem);

}
