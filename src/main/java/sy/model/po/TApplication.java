package sy.model.po;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * ****************************************************************
 * 文件名称 : TApplication.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午2:40:37
 * 文件描述 : 应用类
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Entity
@Table(name = "TApp")
public class TApplication implements java.io.Serializable{

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name="GENERATOR_SEQ_BANK", sequenceName="PGMGRSYS_SEQ",initialValue=1,allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "appName")
	private String appName;//应用名称
	
	@Column(name = "appSize" )
	private String appSize ;//大小
	
	@Column(name = "isRep" , length = 2)
	private int isRep=0;//是否发布           0：未发布，1：发布
	
	@Column(name = "isDelete" , length = 2)
	private int isDelete=0;//是否删除         0：未删除，1：删除
	
	@Transient
	private String isRelease;//是否发布
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "repTime", length = 19)
	private Date repTime;//发布日期
	
	@Transient
	private String repDate;//发布日期
	
	@Column(name = "icon")
	private String icon;//图标地址
	
	@Column(name = "versionNum")
	private String versionNum;//版本号
	
	@Column(name = "sketch")
	private String sketch;//包路径
	
	@Column(name = "content")
	private String content;//内容
	
	@Column(name = "upPath")
	private String upPath;//上传路径

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public Date getRepTime() {
		return repTime;
	}

	public void setRepTime(Date repTime) {
		this.repTime = repTime;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUpPath() {
		return upPath;
	}

	public void setUpPath(String upPath) {
		this.upPath = upPath;
	}

	public String getRepDate() {
		return repDate;
	}

	public void setRepDate(String repDate) {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if(repTime==null)
			this.repDate="";
		else
		    this.repDate = df.format(repTime);
	}
	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		switch (this.isRep) {
		case 0:
			this.isRelease = "未发布";
			break;
		case 1:
			this.isRelease = "发布";
			break;
		default:
			this.isRelease = "";
			break;
		}
	}

	public int getIsRep() {
		return isRep;
	}

	public void setIsRep(int isRep) {
		this.isRep = isRep;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
