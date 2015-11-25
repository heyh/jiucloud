package sy.pageModel;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Message {
	

	/**
	 * 编号
	 */
	private int id;
	
	/**
	 * 发送者ID
	 * 
	 */
	private String sendId;
	
	/**
	 * 发送姓名
	 */
	private String sendName;
	
	/**
	 * 接收者ID
	 */
	private String recid;
	/**
	 * 接收者姓名
	 */
	private String recName;
	
	/**
	 * 站内信编号
	 */
	private String messageid;
	
	/**
	 * 站内信的查看状态
	 * 0 未读
	 * 1 已读
	 */
	private int statue;
	
	/**
	 * 已读时间
	 */
	private Date readtime;
	
	/**
	 * 是否删除
	 * 0 删除
	 * 1 正常
	 */
	private int isdel;
	
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 消息内容
	 */
	private String message;

	/**
	 * 发送时间
	 */
	private Date pdate;

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public Date getReadtime() {
		return readtime;
	}

	public void setReadtime(Date readtime) {
		this.readtime = readtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getPdate() {
		return pdate;
	}

	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getRecid() {
		return recid;
	}

	public void setRecid(String recid) {
		this.recid = recid;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public int getStatue() {
		return statue;
	}

	private String statuevo;
	
	public String getStatuevo() {
		if(this.statue==0){
			return "未读";
		}else if(this.statue==1){
			return "已读";
		}
		return "未知";
	}

	public void setStatuevo(String statuevo) {
		if(this.statue==0){
			this.statuevo  = "未读";
		}else if(this.statue==1){
			this.statuevo  = "已读";
		}
	}

	public void setStatue(int statue) {
		this.statue = statue;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

}
