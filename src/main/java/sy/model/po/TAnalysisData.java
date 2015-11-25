package sy.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TAnalysisData")
public class TAnalysisData {

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "stime", length = 19)
	private Date stime;
	private double money;
	private int project_id;
	private int cost_id;
	private int tcost_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getCost_id() {
		return cost_id;
	}

	public void setCost_id(int cost_id) {
		this.cost_id = cost_id;
	}

	public int getTcost_id() {
		return tcost_id;
	}

	public void setTcost_id(int tcost_id) {
		this.tcost_id = tcost_id;
	}
}
