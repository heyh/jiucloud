package sy.service;

import java.util.List;

import sy.model.IMUser;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.SynIMUser;


/**
 * ****************************************************************
 * 文件名称 : IMUserServiceI.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:08:11
 * 文件描述 : IMuser Service
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
public interface IMUserServiceI {

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
	public void add(IMUser info);
	/**
	 * 根据Id查询
	 * @return
	 */
	public IMUser findById(String id);

	/**
	 * 查看个人资料 
	 * @param dn
	 * @param pwd
	 * @return
	 */
	public IMUser findPuserByNameAndPwd(String dn, String pwd);
	/**
	 * 修改用户姓名(昵称)
	 */
	public boolean updateRelName(IMUser vu);
	/**
	 * 修改用户性别
	 */
	public boolean updateSex(IMUser vu);
	/**
	 * 修改省份城市区县
	 */
	public boolean updateArea(IMUser vu);
	/**
	 * 上传头像
	 * 
	 * @param req
	 * @param rt
	 * @return
	 */
	public void updateUariva(String string, String fileName);
	/**
	 * 修改密码
	 */
	public boolean updatePwd(IMUser vu, String newPwd);
	/**
	 * 查询好友信息
	 * @param vu
	 * @param relname
	 * @return
	 */
	public IMUser findIMUserInfoByName(String relname);

	/**
	 * 查询所有好友信息(通讯录)
	 * 方法表述
	 * @return
	 * Map<String,Object>
	 */
	public List<SynIMUser> findAll(String username);
	/**
	 * 修改同步
	 * 方法表述
	 * @param id
	 * void
	 */
	public void updateIsSyn(String id);
	
	
}
