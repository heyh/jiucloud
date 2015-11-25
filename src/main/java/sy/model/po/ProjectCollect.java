package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 工程采集
 * @author apple
 *
 */
@Entity
@Table(name = "tgc_ProjectCollect")
public class ProjectCollect {
	
	
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	public String getMpid() {
		return mpid;
	}

	public void setMpid(String mpid) {
		this.mpid = mpid;
	}

	//关联附件的ID，此插入前采用日期时分秒，毫秒生成唯一值
	@Column(name = "mpid")
	private String mpid;
	
	//公司ID
	@Column(name = "compId")
	private String compId;
	
	//用户ID
	@Column(name = "uids")
	private String uid;
	
	//工程名称
	@Column(name = "proName")
	private String proName;
	
	//费用类型 关联ID
	@Column(name = "fyType")
	private String fyType;
	
	//名称
	@Column(name = "name")
	private String name;
	
	//单位
	@Column(name = "dw")
	private String dw;
	
	//单价
	@Column(name = "dj")
	private float dj;
	
	//数量
	@Column(name = "sl")
	private int sl;
	
	//规格型号
	@Column(name = "ggxh")
	private String ggxh;
	
	//备注说明
	@Column(name = "remark")
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getFyType() {
		return fyType;
	}

	public void setFyType(String fyType) {
		this.fyType = fyType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public float getDj() {
		return dj;
	}

	public void setDj(float dj) {
		this.dj = dj;
	}

	public int getSl() {
		return sl;
	}

	public void setSl(int sl) {
		this.sl = sl;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
