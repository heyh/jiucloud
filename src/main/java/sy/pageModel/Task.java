package sy.pageModel;

import java.util.Date;

/**
 * 任务管理类
 * 
 * @author tcp
 * 
 */
public class Task {

	/**
	 * id
	 */
	private int id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 开始时间
	 */
	private Date stime;

	/**
	 * 结束时间
	 */
	private Date etime;

	/**
	 * 附件路径
	 */
	private String attachment;

	/**
	 * 附件名称
	 */
	private String attachmentname;

	/**
	 * 用户id
	 */
	private String uids;

	private String pids;

	/**
	 * 负责人
	 * 
	 * @return
	 */
	private String principal;

	private String sender;

	/**
	 * 状态 0:未完成,1:完成
	 * 
	 * @return
	 */
	private String status;

	/**
	 * 备注
	 * 
	 * @return
	 */
	private String remark;

	/**
	 * 评论
	 * 
	 * @return
	 */
	private String comment;

	/**
	 * 日期
	 */
	private String time;

	private String department; // 部门

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUids() {
		return uids;
	}

	public void setUids(String uids) {
		this.uids = uids;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAttachmentname() {
		return attachmentname;
	}

	public void setAttachmentname(String attachmentname) {
		this.attachmentname = attachmentname;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", uids=" + uids + ", pids=" + pids
				+ ", principal=" + principal + ", sender=" + sender
				+ ", department=" + department + "]";
	}

}