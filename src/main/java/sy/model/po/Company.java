package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 公司信息表
 * 
 * */
@Entity
//@Table(name = "jsw_user_company")
@Table(name = "jsw_corporation")
public class Company {
	@Id
	@Column(name = "id", nullable = false, length = 11)
	private int id;
	@Column(name = "user_id")
	private String user_id;
	@Column(name = "name")
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", user_id=" + user_id + ", name=" + name
				+ "]";
	}
}
