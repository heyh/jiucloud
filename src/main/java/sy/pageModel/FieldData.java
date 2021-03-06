package sy.pageModel;

import java.util.Date;

public class FieldData {

	private int id;

	private String projectName;

	private String uid;

	private Date creatTime;

	private int cost_id;
	private int project_id;

	private String costType;

	private String dataName;

	private String price;

	private String company;

	private String unit;

	private String count;

	private String specifications;

	private String remark;

	private String cid;

	private String uname;

	private String startTime;
	private String endTime;

    private Double money;

    private boolean action;

    private int isLock;

    private String needApproved;

    private String itemCode;
    private String approvedUser;
    private String currentApprovedUser;
    private String approvedOption;
    private String section;

    private String supplier;

    private String relId;

    private String price_ys;
    private String price_sj;

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

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	private String sectionName;

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getApprovedOption() {
        return approvedOption;
    }

    public void setApprovedOption(String approvedOption) {
        this.approvedOption = approvedOption;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public String getCurrentApprovedUser() {
        return currentApprovedUser;
    }

    public void setCurrentApprovedUser(String currentApprovedUser) {
        this.currentApprovedUser = currentApprovedUser;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getNeedApproved() {
        return needApproved;
    }

    public void setNeedApproved(String needApproved) {
        this.needApproved = needApproved;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
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
		return "FieldData [id=" + id + ", projectName=" + projectName
				+ ", uid=" + uid + ", creatTime=" + creatTime + ", costType="
				+ costType + ", dataName=" + dataName + ", price=" + price
				+ ", company=" + company + ", count=" + count
				+ ", specifications=" + specifications + ", remark=" + remark
				+ ", cid=" + cid + ", uname=" + uname + ", startTime="
				+ startTime + ", endTime=" + endTime + "]";
	}

	public int getCost_id() {
		return cost_id;
	}

	public void setCost_id(int cost_id) {
		this.cost_id = cost_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
