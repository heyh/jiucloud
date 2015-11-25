package sy.pageModel;

import java.util.List;

public class AnalysisData {

	private double money;
	private String costType;
	private String project_name;
	private String itemCode;
	private String unit;
	private double count;
	private String price;
	private int project_id;
	private int cost_id;
	private int isend;
	private List<Double> moneys;

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public int getCost_id() {
		return cost_id;
	}

	public void setCost_id(int cost_id) {
		this.cost_id = cost_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public List<Double> getMoneys() {
		return moneys;
	}

	public void setMoneys(List<Double> moneys) {
		this.moneys = moneys;
	}

	public int getIsend() {
		return isend;
	}

	public void setIsend(int isend) {
		this.isend = isend;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "AnalysisData [money=" + money + ", costType=" + costType
				+ ", project_name=" + project_name + ", itemCode=" + itemCode
				+ ", unit=" + unit + ", count=" + count + ", price=" + price
				+ ", project_id=" + project_id + ", cost_id=" + cost_id
				+ ", isend=" + isend + ", moneys=" + moneys + "]";
	}

}
