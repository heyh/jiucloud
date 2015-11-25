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
 * 文件描述 : 服务号通知类
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Entity
@Table(name = "TInform")
public class TInform implements java.io.Serializable{

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name="GENERATOR_SEQ_BANK", sequenceName="PGMGRSYS_SEQ",initialValue=1,allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "title")
	private String title;//标题
	
	@Column(name = "content",columnDefinition="CLOB", nullable=true)
	private String content;//信息内容
	
	@Transient
	private String simpleCon;//简单的内容
	
	@Column(name = "picPath")
	private String picPath;//缩略图路径
	
	@Column(name = "htmlPath")
	private String htmlPath;//html路径
	
	@Column(name = "fwName")
	private String fwName;//服务号登录名称
	
	@Transient
	private String fwRealname;//服务名
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ctime", length = 19)
	private Date ctime = new Date();//创建日期
	
	@Transient
	private String createTime;//创建日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "isoktime", length = 19)
	private Date isoktime;//推送 时间
	
	@Column(name = "isok" , length = 2)
	private int isok;//是否推送  0待审核,1推送 ,2定时推送,3已推送
	
	@Column(name = "isDelete" , length = 2)
	private int isDelete;//是删除           0：正常，1：删除
	
	@Transient
	private String isOpen;//是否推送

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		this.createTime = df.format(ctime);
	}

	public Date getIsoktime() {
		return isoktime;
	}

	public void setIsoktime(Date isoktime) {
		this.isoktime = isoktime;
	}

	public int getIsok() {
		return isok;
	}

	public void setIsok(int isok) {
		this.isok = isok;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		switch(isok){
		case 0:
			this.isOpen = "待审核";
			break;
		case 1:
			this.isOpen = "推送";
			break;
		case 2:
			this.isOpen = "定时推送 ";
			break;	
		case 3:
			this.isOpen = "已推送";
			break;
		default:
			this.isOpen = "";
			break;
		}
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String getFwRealname() {
		return fwRealname;
	}

	public void setFwRealname(String fwRealname) {
		this.fwRealname = fwRealname;
	}

	public String getSimpleCon() {
		return simpleCon;
	}

	public void setSimpleCon(String simpleCon) {
		String[]str = this.content.split("</p>");
		this.simpleCon = str[0].replaceAll("<p>", "")+"...";
	}
	
	
	
}
