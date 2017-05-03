package sy.model.po;

import javax.persistence.*;

@Entity
@Table(name = "TPrice")
public class Price {

	public Price(PriceModel priceModel) {
		this.name = priceModel.getName();
		this.cid = priceModel.getCid();
	}

	@Id
	@Column(name = "id", nullable = false, length = 36)
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	private String name;
	private int cid;

	public Price() {

	}


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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
