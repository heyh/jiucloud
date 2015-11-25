package sy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 省份表
 * 
 * @author tcp
 * 
 */
@Entity
@Table(name = "jsw_province")
public class S_province {
	// 省份编号
	@Id
	@Column(name = "province_id", nullable = false)
	private int provinceid;

	// 城市名称
	@Column(name = "province_name")
	private String provincename;

	// 创建时间
	@Column(name = "province_type")
	private String provincetype;//0全国 1直辖市 2普通省份 3自治区 4特别行政区 5国外

	public int getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(int provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	public String getProvincetype() {
		return provincetype;
	}

	public void setProvincetype(String provincetype) {
		this.provincetype = provincetype;
	}

}
