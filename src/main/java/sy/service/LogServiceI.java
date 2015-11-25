package sy.service;

import java.util.List;

import sy.model.po.Tlog;
import sy.pageModel.DataGrid;
import sy.pageModel.Log;
import sy.pageModel.PageHelper;

/**
 * 日志管理Service
 * 
 * @author tcp
 *
 */
public interface LogServiceI {

	/**
	 * 获得列表
	 */
	public DataGrid dataGrid(String sTime, String eTime, List<Integer> ugroup,
			PageHelper ph);

	/**
	 * 添加
	 */
	public void add(Tlog log);

	/**
	 * 根据Id查询
	 * 
	 * @return
	 */
	public Log findById(String id);

	List<Log> getfindList(List<Integer> ugroup);

}
