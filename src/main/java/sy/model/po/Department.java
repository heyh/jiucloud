package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jsw_corporation_job")
public class Department {
	/*
	 * 员工职位表
	 */
	@Id
	@Column(name = "ID", nullable = false)
	private int id;

	private String name;

	private int parent_id;

	private int level;

	private int user_id;

	private int isend;

	private int company_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getIsend() {
		return isend;
	}

	public void setIsend(int isend) {
		this.isend = isend;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", parent_id="
				+ parent_id + ", level=" + level + ", user_id=" + user_id
				+ ", isend=" + isend + ", company_id=" + company_id + "]";
	}
}
