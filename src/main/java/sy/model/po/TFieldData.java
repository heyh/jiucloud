package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
 * 现场数据管理表
 * 
 * */
@Entity
@Table(name = "TFieldData")
public class TFieldData implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3597462271927905098L;

	public TFieldData() {
	}

	public TFieldData(String projectName, String uid, Date creatTime,
			String costType, String dataName, String price, String company,
			String count, String specifications, String remark, String cid,
			String uname, String unit, String needApproved, String approvedUser,
            String currentApprovedUser, String itemCode, String approvedOption, String section, String supplier, String relId, String price_sj,
					  String price_ys, String payAmount) {
		super();
		this.projectName = projectName;
		this.uid = uid;
		this.creatTime = creatTime;
		this.costType = costType;
		this.dataName = dataName;
		this.price = price;
		this.company = company;
		this.count = count;
		this.specifications = specifications;
		this.remark = remark;
		this.cid = cid;
		this.uname = uname;
		this.unit = unit;
        this.needApproved = needApproved;
        this.approvedUser = approvedUser;
        this.currentApprovedUser = currentApprovedUser;
        this.itemCode = itemCode;
        this.approvedOption = approvedOption;
        this.section = section;
        this.supplier = supplier;
        this.relId = relId;
        this.price_sj = price_sj;
        this.price_ys = price_ys;
        this.payAmount = payAmount;

	}

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	private int id;

	// 工程名称
	@Column(name = "projectName")
	private String projectName;

	@Column(name = "uid")
	private String uid;

	// 单位
	@Column(name = "unit")
	private String unit;

	// 入库时间
	@Column(name = "creatTime")
	private Date creatTime;

	// 费用类型id
	@Column(name = "costType")
	private String costType;

	// 费用编码
	@Column(name = "itemCode")
	private String itemCode;

	// 是否删除
	@Column(name = "isDelete")
	private int isDelete;

	// 名称
	@Column(name = "dataName")
	private String dataName;

	// 单价
	@Column(name = "price")
	private String price;

	// 单位
	@Column(name = "company")
	private String company;

	// 数量
	@Column(name = "count")
	private String count;

	// 规格
	@Column(name = "specifications")
	private String specifications;

	// 备注
	@Column(name = "remark")
	private String remark;

	// 公司id
	@Column(name = "cid")
	private String cid;

	// 用户姓名
	@Column(name = "uname")
	private String uname;

    @Column(name = "needApproved")
    private String needApproved;

    @Column(name = "approvedUser")
    private String approvedUser;

    @Column(name = "currentApprovedUser")
    private String currentApprovedUser;


    @Column(name = "approvedOption")
    private String approvedOption;

    @Column(name = "section")
	private String section;

    @Column(name = "supplier")
	private String supplier;

    @Column(name = "relId")
	private String relId;

	@Column(name = "price_ys")
	private String price_ys;

	@Column(name = "price_sj")
	private String price_sj;

	@Column(name = "payAmount")
	private String payAmount;

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPrice_ys() {
		return price_ys;
	}

	public void setPrice_ys(String price_ys) {
		this.price_ys = price_ys;
	}

	public String getPrice_sj() {
		return price_sj;
	}

	public void setPrice_sj(String price_sj) {
		this.price_sj = price_sj;
	}

	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getApprovedOption() {
		return approvedOption;
	}

	public void setApprovedOption(String approvedOption) {
		this.approvedOption = approvedOption;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getCurrentApprovedUser() {
        return currentApprovedUser;
    }

    public void setCurrentApprovedUser(String currentApprovedUser) {
        this.currentApprovedUser = currentApprovedUser;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public String getNeedApproved() {
        return needApproved;
    }

    public void setNeedApproved(String needApproved) {
        this.needApproved = needApproved;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "TFieldData [id=" + id + ", projectName=" + projectName
				+ ", uid=" + uid + ", unit=" + unit + ", creatTime="
				+ creatTime + ", costType=" + costType + ", itemCode="
				+ itemCode + ", isDelete=" + isDelete + ", dataName="
				+ dataName + ", price=" + price + ", company=" + company
				+ ", count=" + count + ", specifications=" + specifications
				+ ", remark=" + remark + ", cid=" + cid + ", uname=" + uname
				+ "]";
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

}
