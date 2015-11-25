package sy.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志管理类
 * 
 * @author tcp
 * 
 */
@Entity
@Table(name = "Tlog")
public class Tlog {

	/**
	 * id
	 */
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	private int id;

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 描述
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 创建时间
	 */
	@Column(name = "ctime")
	private Date ctime;

	
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


	public Date getCtime() {
		return ctime;
	}


	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}


	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	
}