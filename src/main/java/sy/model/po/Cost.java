package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/*
 * 费用管理表
 * 
 * */
@Entity
@Table(name = "TCost", uniqueConstraints = { @UniqueConstraint(columnNames = "itemCode") })
@DynamicInsert(true)
@DynamicUpdate(true)
public class Cost {
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	private int id;

	// 费用类型名称
	@Column(name = "costType")
	private String costType;

	// 公司id
	@Column(name = "cid")
	private String cid;

	// 数据在数据库状态标识 0删除，1正常
	@Column(name = "isDelete")
	private int isDelete;

	// 父级编码
	@Column(name = "pid")
	private String pid;

	// 代表是否为蓝本数据
	@Column(name = "flag")
	private int flag;

	// 代表是否为蓝本数据
	@Column(name = "nid")
	private String nid;

	private int level;
	private int isend;
	private String itemCode;

	public Cost() {
	}

	public Cost(CostModel tem) {
		this.costType = tem.getCostType();
		this.flag = tem.getFlag();
		this.isDelete = tem.getIsDelete();
		this.pid = tem.getPid();
		this.sort = tem.getSort();
		this.nid = tem.getId();
		this.level = tem.getLevel();
		this.isend = tem.getIsend();
		this.itemCode = tem.getItemCode();
	}

	/**
	 * 排序值
	 */
	@Column(name = "sort")
	private int sort;

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	@Override
	public String toString() {
		return "Cost [id=" + id + ", costType=" + costType + ", cid=" + cid
				+ ", isDelete=" + isDelete + ", pid=" + pid + ", flag=" + flag
				+ ", nid=" + nid + ", level=" + level + ", isend=" + isend
				+ ", itemCode=" + itemCode + ", sort=" + sort + "]";
	}

}
