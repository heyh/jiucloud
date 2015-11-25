package sy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 城市表
 * 
 * @author tcp
 *
 */
@Entity
@Table(name = "jsw_city")
public class S_city {
	// 城市编号
	@Id
	@Column(name = "city_id", nullable = false)
	private int cityid;

	// 城市名称
	@Column(name = "city_name")
	private String cityname;

	// 省份编号
	@Column(name = "province_id")
	private int provinceid;

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public int getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(int provinceid) {
		this.provinceid = provinceid;
	}

}
