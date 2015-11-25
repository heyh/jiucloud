package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tpcost_cost")
public class Tpcost {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	private int pcost_id;
	private int cost_id;
	private int department_id;

	public Tpcost() {
	}

	public Tpcost(String department_id, String pcost_id, String cost_id) {
		this.pcost_id = Integer.parseInt(pcost_id);
		this.department_id = Integer.parseInt(department_id);
		this.cost_id = Integer.parseInt(cost_id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPcost_id() {
		return pcost_id;
	}

	public void setPcost_id(int pcost_id) {
		this.pcost_id = pcost_id;
	}

	public int getCost_id() {
		return cost_id;
	}

	public void setCost_id(int cost_id) {
		this.cost_id = cost_id;
	}

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	@Override
	public String toString() {
		return "Tpcost [id=" + id + ", pcost_id=" + pcost_id + ", cost_id="
				+ cost_id + ", department_id=" + department_id + "]";
	}
}
