package sy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.Message;
import sy.pageModel.MessageSearch;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.MessageServiceI;
import sy.service.MessageTextServiceI;
import sy.service.UserServiceI;

/**
 * **************************************************************** 文件名称 :
 * ApplicationController.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:21:38
 * 文件描述 : Android 服务号通知控制器 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/messageController")
public class MessageController extends BaseController {

	@Autowired
	private MessageServiceI messsageService;

	@Autowired
	private MessageTextServiceI messageTextService;

	@Autowired
	private UserServiceI us;

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/sendMsgList")
	public String manager() {
		return "/app/msg/showsendmsg";
	}

	/**
	 * 获取数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(MessageSearch app, PageHelper ph,
			HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		return messageTextService.dataGrid(app, ph, uid);
	}

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/recMsgList")
	public String managerrec() {
		return "/app/msg/recvmsg";
	}

	/**
	 * 跳转回收站
	 * 
	 * @return
	 */
	@RequestMapping("/todelist")
	public String todelist(HttpServletRequest req) {
		// 1我发件删除的,2我收件删除的
		req.setAttribute("totype", req.getParameter("totype"));
		return "/app/msg/dellist";
	}

	/**
	 * 阅读信件
	 * 
	 * @return
	 */
	@RequestMapping("/recread")
	public String recread(HttpServletRequest request) {
		String msgid = request.getParameter("msgid");

		Message msg = this.messageTextService.read(msgid);
		request.setAttribute("messageObject", msg);
		return "/app/msg/recread";
	}

	/**
	 * 详情
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(HttpServletRequest request) {
		String msgid = request.getParameter("msgid");
		Message msg = this.messageTextService.detail(msgid);
		request.setAttribute("messageObject", msg);
		return "/app/msg/sendread";
	}

	/**
	 * 详情
	 * 
	 * @return
	 */
	@RequestMapping("/dtau")
	public String dtau(HttpServletRequest request) {
		String msgid = request.getParameter("msgid");
		Message msg = this.messageTextService.detail(msgid);
		request.setAttribute("messageObject", msg);
		return "/app/msg/dtau";
	}

	/**
	 * 获取收件信人数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/datarecGrid")
	@ResponseBody
	public DataGrid datarecGrid(MessageSearch app, PageHelper ph,
			HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		return messageTextService.datarecGrid(app, ph, uid);

	}

	/**
	 * 获取收件信人数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/datadelGrid")
	@ResponseBody
	public DataGrid datadelGrid(MessageSearch app, PageHelper ph,
			HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		return messageTextService.datadelGrid(app, ph, uid);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchDeleteSend")
	@ResponseBody
	public Json batchDelete(String ids) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delete(id);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 删除收信接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSend")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		if (id != null) {
			this.messsageService.delete(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchDeleteRec")
	@ResponseBody
	public Json batchDeleteRec(String ids) {
		System.out.println(ids + "  ===================================afsfs");
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.deleteInRec(id);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 删除收信接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteInRec")
	@ResponseBody
	public Json deleteInRec(String id) {
		Json j = new Json();
		if (id != null) {
			this.messsageService.deleteRec(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量恢复
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchhui")
	@ResponseBody
	public Json batchhui(String ids, HttpServletRequest request) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.huifu(id, request);
				}
			}
		}
		j.setMsg("批量恢复成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 恢复
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/huifu")
	@ResponseBody
	public Json huifu(String id, HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		Json j = new Json();
		if (id != null) {
			this.messsageService.huifu(id, uid);
		}
		j.setMsg("恢复成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量彻底删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchdelrgc")
	@ResponseBody
	public Json batchdelrgc(String ids, HttpServletRequest request) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delrgc(id, request);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 彻底删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/tosendmsg")
	public String securi_tosendmsg(String id, HttpServletRequest request) {
		return "/app/msg/sendmsg";
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/securi_toloadsduser")
	@ResponseBody
	public List<User> securi_toloadsduser(String id, HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String uid = sessionInfo.getId();
		List<User> u = us.findallUser(cid, uid);
		return u;
	}

	/**
	 * 发送消息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/securi_savemsg")
	@ResponseBody
	public Json savemsg(Message msg, HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();

		System.out.println("request.getParameter()" + msg.getSendName());
		
		Json j = new Json();
		if (msg.getSendName() == null) {
			j.setSuccess(false);
			j.setMsg("请选择接收者！");
			return j;
		}
		if (msg.getSendName().equals("")) {
			j.setSuccess(false);
			j.setMsg("请选择接收者！");
			return j;
		}
		if (msg.getTitle() == null) {
			j.setSuccess(false);
			j.setMsg("请输入标题！");
			return j;
		}
		if (msg.getTitle().equals("")) {
			j.setSuccess(false);
			j.setMsg("请输入标题！");
			return j;
		}

		if (msg.getMessage() == null) {
			j.setSuccess(false);
			j.setMsg("请输入消息内容！");
			return j;
		}
		if (msg.getMessage().equals("")) {
			j.setSuccess(false);
			j.setMsg("请输入消息内容！");
			return j;
		}
		try {
			this.messsageService.savemsg(msg, uid);
			j.setMsg("消息发送成功！");
			j.setSuccess(true);
		} catch (Exception ex) {
			j.setMsg("发送失败");
			j.setSuccess(false);
		}

		return j;
	}

	/**
	 * 彻底删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delrgc")
	@ResponseBody
	public Json delrgc(String id, HttpServletRequest request) {
		String uid = ((SessionInfo) request.getSession().getAttribute(
				sy.util.ConfigUtil.getSessionInfoName())).getId();
		Json j = new Json();
		if (id != null) {
			this.messsageService.cddelete(id, uid);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
}
