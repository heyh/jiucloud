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
@Table(name = "tgc_MessageText")
public class MessageText {

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
	 * 标题q
	 */
	@Column(name = "title")
	private String title;
	
	/**
	 * 消息内容
	 */
	@Column(name = "message",columnDefinition="TEXT", nullable=true)
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "pdate", length = 19)
	private Date pdate;

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
	
	
}
