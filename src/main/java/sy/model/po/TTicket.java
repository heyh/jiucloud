package sy.model.po;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by heyh on 16/5/17.
 */

@Entity
@Table(name = "TTicket")
public class TTicket {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    // 发票类型(销项发票、进项发票)
    @Column(name = "ticketType")
    private String ticketType;

    // 发票名称(货物名称)
    @Column(name = "ticketName")
    private String ticketName;

    // 开票日期
    @Temporal(TemporalType.DATE)
    @Column(name = "ticketDate")
    private Date ticketDate;

    // 合同
    @Column(name = "contract")
    private String contract;

    // 供应商名称
    @Column(name = "supplier")
    private String supplier;

    // 客户名称
    @Column(name = "consumer")
    private String consumer;

    // 纳税识别号
    @Column(name = "taxNo")
    private String taxNo;

    // 地址
    @Column(name = "address")
    private String address;

    // 纳税账户开户银行
    @Column(name = "taxBank")
    private String taxBank;

    // 纳税账户开户账号
    @Column(name = "taxAccount")
    private String taxAccount;

    // 纳税资格状况
    @Column(name = "taxStatus")
    private String taxStatus;

    // 单位
    @Column(name = "unit")
    private String unit;

    // 数量
    @Column(name = "count")
    private String count;

    // 单价
    @Column(name = "price")
    private String price;

    // 规格型号
    @Column(name = "specifications")
    private String specifications;

    // 金额
    @Column(name = "money")
    private String money;

    // 联系人
    @Column(name = "linkPerson")
    private String linkPerson;

    // 联系电话
    @Column(name = "linkPhone")
    private String linkPhone;

    // 入库时间
    @Column(name = "createTime")
    private Date createTime;

    // 修改时间
    @Column(name = "updateTIme")
    private Date updateTime;

    // 操作人
    @Column(name = "uid")
    private String uid;

    // 操作人名
    @Column(name = "uname")
    private String uname;

    // 公司Id
    @Column(name = "cid")
    private String cid;

    // 公司名称
    @Column(name = "company")
    private String company;

    // 是否已删除
    @Column(name = "isDelete")
    private int isDelete;

    // 发票状态
    @Column(name = "ticketStatus")
    private String ticketStatus;

    // 认证状态
    @Column(name = "authStatus")
    private String authStatus;

    // 接收状态
    @Column(name = "reciveStatus")
    private String reciveStatus;

    public TTicket() {
    }

    public TTicket(String ticketType, String ticketName, Date ticketDate, String contract, String supplier, String consumer, String taxNo, String address, String taxBank, String taxAccount, String taxStatus, String unit, String count, String price, String specifications, String money, String linkPerson, String linkPhone, Date createTime, Date updateTime, String uid, String uname, String cid, String company, int isDelete, String ticketStatus, String authStatus, String reciveStatus) {
        super();
        this.ticketType = ticketType;
        this.ticketName = ticketName;
        this.ticketDate = ticketDate;
        this.contract = contract;
        this.supplier = supplier;
        this.consumer = consumer;
        this.taxNo = taxNo;
        this.address = address;
        this.taxBank = taxBank;
        this.taxAccount = taxAccount;
        this.taxStatus = taxStatus;
        this.unit = unit;
        this.count = count;
        this.price = price;
        this.specifications = specifications;
        this.money = money;
        this.linkPerson = linkPerson;
        this.linkPhone = linkPhone;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.uid = uid;
        this.uname = uname;
        this.cid = cid;
        this.company = company;
        this.isDelete = isDelete;
        this.ticketStatus = ticketStatus;
        this.authStatus = authStatus;
        this.reciveStatus = reciveStatus;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxBank() {
        return taxBank;
    }

    public void setTaxBank(String taxBank) {
        this.taxBank = taxBank;
    }

    public String getTaxAccount() {
        return taxAccount;
    }

    public void setTaxAccount(String taxAccount) {
        this.taxAccount = taxAccount;
    }

    public String getTaxStatus() {
        return taxStatus;
    }

    public void setTaxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLinkPerson() {
        return linkPerson;
    }

    public void setLinkPerson(String linkPerson) {
        this.linkPerson = linkPerson;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getReciveStatus() {
        return reciveStatus;
    }

    public void setReciveStatus(String reciveStatus) {
        this.reciveStatus = reciveStatus;
    }
}
