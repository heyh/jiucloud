package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.S_department;
import sy.model.po.Department;

import java.util.List;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface DepartmentServiceI {

	/**
	 * 查询一条纪录
	 * 
	 * @param id
	 */
	public Department findOneView(String id,String cid);

	/**
	 * 根据当前部门获得所有下辖部门
	 * 
	 * @param id
	 */
	public List<Integer> getListByDepartmentId(int id);

    /**
     * 根据所有下辖部门获得所有下辖员工
     * @param d
     * @param uid
     * @param cid
     * @return
     */
	List<Integer> getUserGroup(Department d, String uid,String cid);
	
	List<Department> getDepartments(String cid);

	S_department getDepartmentByUid(String uid,String cid);

    public List<S_department> getDepartmentsByUid(String uid,String cid);

    public List<Integer> getUsers(String cid, int uid);

    public boolean hasRight(String cid, int uid, String rightCode);

    public List<String> getAllRight(String cid, int uid);

    public int getParentId(String cid, int uid);
}
