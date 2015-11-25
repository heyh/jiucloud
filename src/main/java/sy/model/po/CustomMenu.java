package sy.model.po;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * 文件名称 : CustomMenu.java
 * 作 者 :   tcp
 * 创建时间 : 2015年1月9日 下午2:22:53
 * 文件描述 : 服务号自定义菜单
 * 版权声明 : 
 * 修改历史 : 2015年1月9日 1.00 初始版本
 *****************************************************************
 */
@Entity
@Table(name = "CustomMenu")
public class CustomMenu {

	//菜单编号
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	//一级菜单
	@Column(name = "primaryMenu")
	private String primaryMenu;//id,id
	
	@Column(name = "fwName")
	private String fwName;//服务号名称
	
	@Transient
	private List<Button> buttion = new ArrayList<Button>();//一级菜单
	
	@Column(name = "isInput")
	private String isInput;//标识服务号自定义菜单信息是否存入缓存  值为空表示否,0表示是
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ctime", length = 19)
	private Date ctime = new Date();//创建日期
	
	@Transient
	private String createTime;//创建日期
	
	
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrimaryMenu() {
		return primaryMenu;
	}

	public void setPrimaryMenu(String primaryMenu) {
		this.primaryMenu = primaryMenu;
	}

	public List<Button> getButtion() {
		return buttion;
	}

	public void setButtion(List<Button> buttion) {
		this.buttion = buttion;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public String getIsInput() {
		return isInput;
	}

	public void setIsInput(String isInput) {
		this.isInput = isInput;
	}

	
}
