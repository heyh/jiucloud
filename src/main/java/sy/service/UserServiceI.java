package sy.service;

import java.util.List;

import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;

/**
 * 用户Service
 * 
 * 
 */
public interface UserServiceI {

	/**
	 * 用户登录
	 * 
	 * @param user
	 *            里面包含登录名和密码
	 * @return 用户对象
	 */
	public User login(String name, String pwd);

	public User getUser(String id);

	/**
	 * 获得用户对象
	 * 
	 * @param id
	 * @return
	 */
	public User get(String id);

	/**
	 * 获得用户能访问的资源地址
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	public List<String> resourceList(String id, int isadmin);

	public List<User> findallUser(String uid, String cid);

    public String findUnderlingUsers(List<Integer> uids);

}
