package sy.service;

import java.util.List;

import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
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
public interface InformServiceI {

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
	public Integer add(TInform info);
	/**
	 * 根据Id查询
	 * @return
	 */
	public TInform findById(String id);
	/**
	 * 发布
	 * 方法表述
	 * @param id
	 * void
	 */
	public TInform updateIsRep(String id);
	
	/**
	 * 定时发送服务号消息
	 * 方法表述
	 * void
	 */
	public void sentMsgByTime();
	/**
	 * 发送服务号消息
	 * 方法表述
	 * @param id
	 * void
	 */
	public void sentFwMsg(String id);
	/**
	 * 发送消息给指定的人
	 * 方法表述
	 * @param id 服务号消息id
	 * @param userName 要发送的人
	 * void
	 */
	public void sentFwMsg(String id,String userName);
	
	/**
	 * 获得服务号消息
	 * 方法表述
	 * @param id
	 * @return
	 * Inform
	 */
	public Inform getFwMsg(String id);
	
	/**
	 * 修改为已发送
	 * 方法表述
	 * @param id
	 * void
	 */
	public void updateSent(String id);

}
