package sy.pageModel;

public class OrderSearch {

	private String orderid;// 根据订单编号查询
	private String orderName;// 根据订单名称查询
	private String startTime;// 开工日期
	private String endTime;// 结束日期

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "OrderSearch [orderid=" + orderid + ", orderName=" + orderName
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
