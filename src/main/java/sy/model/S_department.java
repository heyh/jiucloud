package sy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 部门表
 * 
 */
@Entity
@Table(name = "jsw_corporation_department")
public class S_department {
	@Id
	@Column(name = "id", nullable = false)
	private int id;

	private String name;

	private int parent_id;

	private int endnode;

	private int user_id;

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

	public int getEndnode() {
		return endnode;
	}

	public void setEndnode(int endnode) {
		this.endnode = endnode;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	@Override
	public String toString() {
		return "S_department [id=" + id + ", name=" + name + ", parent_id="
				+ parent_id + ", endnode=" + endnode + ", user_id=" + user_id
				+ ", company_id=" + company_id + "]";
	}
}
