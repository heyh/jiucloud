package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TPrice_Cost")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Price_Cost {

	@Id
	@Column(name = "id", nullable = false, length = 36)
	private int id;
	private int price_id;
	private int cost_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice_id() {
		return price_id;
	}

	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}

	public int getCost_id() {
		return cost_id;
	}

	public void setCost_id(int cost_id) {
		this.cost_id = cost_id;
	}

	@Override
	public String toString() {
		return "Price_Cost [id=" + id + ", price_id=" + price_id + ", cost_id="
				+ cost_id + "]";
	}

}
