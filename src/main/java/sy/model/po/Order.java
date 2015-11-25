package sy.model.po;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 我的订单管理
 * 
 * @author prime
 *
 */
@Entity
@Table(name = "jsw_zjsorder")
public class Order {

	@Id
	@Column(name = "zjsorder_id", nullable = false, length = 255)
	private String zjsorder_id;

	@Column(name = "zjsorder_name", length = 255)
	private String zjsorder_name;

	@Column(name = "zjsorder_bz")
	private String zjsorder_bz;

	@Column(name = "zjsorder_money")
	private double zjsorder_money;

	@Column(name = "zjsorder_time")
	private long zjsorder_time;

	@Transient
	private String time;

	@Column(name = "zjsorder_state")
	private int zjsorder_state;

	@Column(name = "zjsinfo_id")
	private int zjsinfo_id;

	@Column(name = "zjsorder_customer")
	private int zjsorder_customer;

	public String getZjsorder_id() {
		return zjsorder_id;
	}

	public void setZjsorder_id(String zjsorder_id) {
		this.zjsorder_id = zjsorder_id;
	}

	public String getZjsorder_name() {
		return zjsorder_name;
	}

	public void setZjsorder_name(String zjsorder_name) {
		this.zjsorder_name = zjsorder_name;
	}

	public String getZjsorder_bz() {
		return zjsorder_bz;
	}

	public void setZjsorder_bz(String zjsorder_bz) {
		this.zjsorder_bz = zjsorder_bz;
	}

	public double getZjsorder_money() {
		return zjsorder_money;
	}

	public void setZjsorder_money(double zjsorder_money) {
		this.zjsorder_money = zjsorder_money;
	}

	public int getZjsorder_state() {
		return zjsorder_state;
	}

	public void setZjsorder_state(int zjsorder_state) {
		this.zjsorder_state = zjsorder_state;
	}

	public int getZjsinfo_id() {
		return zjsinfo_id;
	}

	public void setZjsinfo_id(int zjsinfo_id) {
		this.zjsinfo_id = zjsinfo_id;
	}

	public int getZjsorder_customer() {
		return zjsorder_customer;
	}

	public void setZjsorder_customer(int zjsorder_customer) {
		this.zjsorder_customer = zjsorder_customer;
	}

	public long getZjsorder_time() {
		return zjsorder_time;
	}

	public void setZjsorder_time(long zjsorder_time) {
		this.zjsorder_time = zjsorder_time;
	}

	public String getTime() {
		return time;
	}

	public void setTime() {
		Date date = new Date(zjsorder_time);
		this.time = DateFormat.getDateInstance().format(date);
	}
}