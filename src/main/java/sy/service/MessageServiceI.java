package sy.service;

import java.util.List;

import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
import sy.pageModel.Message;
import sy.pageModel.PageHelper;


/**
 * ****************************************************************
 * 文件名称 : ApplicationServiceI.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午3:08:11
 * 文件描述 : 服务号通知Service
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
public interface MessageServiceI {

	/**
	 * 获得列表
	 */
	public DataGrid dataGrid(AppSearch app, PageHelper ph,String fwName);
	
	/**
	 *	获得当前用户下所有推送消息 
	 * 方法表述
	 * @param fwName
	 * @return
	 * List<TInform>
	 */
	public List<TInform> getAllInfo(String fwName,String infoTitle);

	
	public void delete(String id);
	
	public void deleteRec(String id);

	public void huifu(String id, String uid);

	public void cddelete(String id, String uid);

	public void savemsg(Message msg, String uid);
	
}
