package sy.service;

import java.util.List;

import sy.model.po.Message;
import sy.model.po.MessageText;
import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Inform;
import sy.pageModel.MessageSearch;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:08:11
 * 文件描述 : 服务号通知Service 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
public interface MessageTextServiceI {

	/**
	 * 获得发件箱列表
	 */
	public DataGrid dataGrid(MessageSearch app, PageHelper ph, String uid);

	/**
	 * 获得发件箱列表
	 */
	public DataGrid datarecGrid(MessageSearch app, PageHelper ph, String uid);

	/**
	 * 获得回收站
	 */
	public DataGrid datadelGrid(MessageSearch app, PageHelper ph, String uid);

	/**
	 * 获得当前用户下所有推送消息 方法表述
	 * 
	 * @param fwName
	 * @return List<TInform>
	 */
	public List<TInform> getAllInfo(String fwName, String infoTitle);

	/**
	 * 
	 * 删除
	 * 
	 * @param id
	 *            void
	 */
	public void delete(String id);

	/**
	 * 添加
	 */
	public Integer add(TInform info);

	/**
	 * 读邮件
	 * 
	 * @return
	 */
	public sy.pageModel.Message read(String id);

	/**
	 * 发布 方法表述
	 * 
	 * @param id
	 *            void
	 */
	public TInform updateIsRep(String id);

	/**
	 * 定时发送服务号消息 方法表述 void
	 */
	public void sentMsgByTime();

	/**
	 * 发送服务号消息 方法表述
	 * 
	 * @param id
	 *            void
	 */
	public void sentFwMsg(String id);

	/**
	 * 发送消息给指定的人 方法表述
	 * 
	 * @param id
	 *            服务号消息id
	 * @param userName
	 *            要发送的人 void
	 */
	public void sentFwMsg(String id, String userName);

	/**
	 * 获得服务号消息 方法表述
	 * 
	 * @param id
	 * @return Inform
	 */
	public Inform getFwMsg(String id);

	public sy.pageModel.Message detail(String msgid);

	public List<MessageText> getfindList(String uid);

}
