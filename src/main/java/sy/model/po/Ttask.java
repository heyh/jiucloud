package sy.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 任务管理类
 * 
 * @author tcp
 * 
 */
@Entity
@Table(name = "Ttask")
public class Ttask {

	/**
	 * id
	 */
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "GENERATOR_SEQ_BANK")
	// @SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName =
	// "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	private int id;

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 开始时间
	 */
	@Column(name = "stime")
	private Date stime;

	/**
	 * 结束时间
	 */
	@Column(name = "etime")
	private Date etime;

	/**
	 * 附件路径
	 */
	@Column(name = "attachment")
	private String attachment;

	/**
	 * 用户id
	 */
	@Column(name = "uids")
	private String uids;

	@Column(name = "pids")
	private String pids;

	/**
	 * 负责人
	 * 
	 * @return
	 */
	@Column(name = "principal")
	private String principal;

	@Column(name = "sender")
	private String sender;

	/**
	 * 状态 0:未完成,1:完成
	 * 
	 * @return
	 */
	@Column(name = "status")
	private int status;

	/**
	 * 备注
	 * 
	 * @return
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 评论
	 * 
	 * @return
	 */
	@Column(name = "comments")
	private String comments;

	/**
	 * 部门
	 * 
	 * @return
	 */
	@Column(name = "department")
	private int department;

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
		return "Ttask [id=" + id + ", uids=" + uids + ", pids=" + pids
				+ ", principal=" + principal + ", sender=" + sender
				+ ", department=" + department + "]";
	}

}