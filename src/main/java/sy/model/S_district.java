package sy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 区县表
 * @author tcp
 *
 */
@Entity
@Table(name = "S_district")
public class S_district {
	//区县编号
	@Id
	@Column(name = "districtid", nullable = false)
	private int districtid;
	
	//区县名称
	@Column(name = "districtname")
	private String districtname;
	
	//城市编号
	@Column(name = "cityid")
	private int cityid;
	
	//创建时间
	@Column(name = "datecreated")
	private Date datecreated;
	
	//修改时间
	@Column(name = "dateupdated")
	private Date dateupdated;

	

	public int getDistrictid() {
		return districtid;
	}

	public void setDistrictid(int districtid) {
		this.districtid = districtid;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public Date getDateupdated() {
		return dateupdated;
	}

	public void setDateupdated(Date dateupdated) {
		this.dateupdated = dateupdated;
	}
	
	
}
