package sy.service;

import org.springframework.stereotype.Repository;

import sy.model.Collaboration;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface CollaborationServiceI {

	public DataGrid dataGrid(String cid, String pid, PageHelper ph);

	public void delete(Collaboration tem);

	public Collaboration update(Collaboration tem);

	public Collaboration findoneview(int id);

	public Collaboration add(Collaboration tem);

}
