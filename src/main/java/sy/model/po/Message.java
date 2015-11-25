package sy.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tgc_Message")
public class Message {

	/**
	 * 编号
	 */
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/**
	 * 发送者ID
	 */
	@Column(name = "sendId")
	private String sendId;
	
	/**
	 * 接收者ID
	 */
	@Column(name = "recid")
	private String recid;
	
	/**
	 * 站内信编号
	 */
	@Column(name = "messageid")
	private int messageid;
	
	/**
	 * 站内信的查看状态
	 * 0 未读
	 * 1 已读
	 */
	@Column(name = "statue")
	private int statue;
	
	/**
	 * 已读时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "readtime", length = 19)
	private Date readtime;
	
	public Date getReadtime() {
		return readtime;
	}

	public void setReadtime(Date readtime) {
		this.readtime = readtime;
	}

	/**
	 * 接收者删除状态
	 * 0 删除
	 * 1 正常
	 */
	@Column(name = "isdel")
	private int isdel;
	
	/**
	 * 删除时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "isdeldate", length = 19)
	private Date isdeldate;
	
	
	/**
	 * 接收者删除状态
	 * 0 删除
	 * 1 正常
	 */
	@Column(name = "senderIsdel")
	private int senderIsdel;
	
	/**
	 * 删除时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "senderIsdeldate", length = 19)
	private Date senderIsdeldate;

	public Date getIsdeldate() {
		return isdeldate;
	}

	public void setIsdeldate(Date isdeldate) {
		this.isdeldate = isdeldate;
	}

	public Date getSenderIsdeldate() {
		return senderIsdeldate;
	}

	public void setSenderIsdeldate(Date senderIsdeldate) {
		this.senderIsdeldate = senderIsdeldate;
	}

	public int getSenderIsdel() {
		return senderIsdel;
	}

	public void setSenderIsdel(int senderIsdel) {
		this.senderIsdel = senderIsdel;
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

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public int getStatue() {
		return statue;
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
