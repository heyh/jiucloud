package sy.service;

import java.util.List;

import sy.model.S_department;
import sy.model.po.Ttask;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.User;

/**
 * 任务管理Service
 * 
 * @author tcp
 *
 */
public interface TaskServiceI {

	/**
	 * 获得列表
	 */
	public DataGrid dataGrid(String sTime, String eTime, List<Integer> ugroup,
			String principal, PageHelper ph);

	/**
	 * 添加
	 */
	public void add(Ttask task);

	/**
	 * 根据Id查询
	 * 
	 * @return
	 */
	public sy.pageModel.Task findById(String id);

	/**
	 * 根据id修改状态
	 * 
	 * @param id
	 */
	public void updateStatus(String id);

	/**
	 * 根据id修改评论
	 * 
	 * @param id
	 */
	public void updateComment(String id, String comment);

	List<sy.pageModel.Task> getfindList(List<Integer> ugroup);

	public List<S_department> getDepsByCompanyId(int cid);

	public List<User> getUserByDepId(int id);
}
