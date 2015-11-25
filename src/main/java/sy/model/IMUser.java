package sy.model;

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
 * 用户表
 * 
 * @author tcp
 * 
 */

@Entity
@Table(name = "IMUser")
public class IMUser {

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	// 账号
	@Column(name = "username")
	private String username;

	// 密码
	@Column(name = "pass")
	private String pass;

	// 创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ctime", length = 19)
	private Date ctime;
	
	@Transient
	private String createTime;//创建日期

	// 真实姓名
	@Column(name = "relname")
	private String relname;

	/**
	 * 用户头像 upload/vod/user/id/uariva用户头像
	 */
	@Column(name = "uariva")
	private String uariva;

	// 0 男 1女
	@Column(name = "sex")
	private Integer sex;

	// 省名
	@Column(name = "provinceName")
	private String provinceName;
	// 地市名
	@Column(name = "cityName")
	private String cityName;
	//区县
	@Column(name = "districtName")
	private String districtName;
	
	//手机号
	@Column(name = "dn")
	private String dn;
	
	//所属部门
	@Column(name = "department")
	private String department;
	
	//职位
	@Column(name = "job")
	private String job;
	
	//员工编号
	@Column(name = "empNum")
	private String empNum;
	
	//是否删除         0：未删除，1：删除
	@Column(name = "isDelete" , length = 2,columnDefinition="INT default 0")
	private int isDelete=0;
	
	//是否同步         0：未删除，1：删除
	@Column(name = "isSyn" , length = 2,columnDefinition="INT default 0")
	private int isSyn=0;
	
	@Transient
	private String isSynchro;//是否同步

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getRelname() {
		return relname;
	}

	public void setRelname(String relname) {
		this.relname = relname;
	}

	public String getUariva() {
		return uariva;
	}

	public void setUariva(String uariva) {
		this.uariva = uariva;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getEmpNum() {
		return empNum;
	}

	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		this.createTime = df.format(ctime);
	}

	public int getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(int isSyn) {
		this.isSyn = isSyn;
	}

	public String getIsSynchro() {
		return isSynchro;
	}

	public void setIsSynchro(String isSynchro) {
		switch(isSyn){
		case 0:
			this.isSynchro = "未同步";
			break;
		case 1:
			this.isSynchro = "已同步";
			break;
		default:
			this.isSynchro = "";
			break;
		}	
	}
	
	
}
