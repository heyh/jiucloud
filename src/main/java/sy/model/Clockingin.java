package sy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 16/2/16.
 */

@Entity
@Table(name = "t_clockingin")
public class Clockingin {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "uid")
    private String uid;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "address")
    private String address;

    @Column(name = "clockinginFlag")
    private String clockinginFlag;

    @Column(name = "clockinginDate")
    private Date clockinginDate;

    @Column(name = "clockinginTime")
    private Date clockinginTime;

    @Column(name = "approveState")
    private String approveState;

    @Column(name = "approveDesc")
    private String approveDesc;

    @Column(name = "approveUser")
    private String approveUser;

    @Column(name = "approveTime")
    private Date approveTime;

    @Column(name = "isDelete")
    private String isDelete;

    @Column(name = "reasonDesc")
    private String reasonDesc;

    private String startDate;
    private String endDate;
    private String uname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClockinginFlag() {
        return clockinginFlag;
    }

    public void setClockinginFlag(String clockinginFlag) {
        this.clockinginFlag = clockinginFlag;
    }

    public Date getClockinginDate() {
        return clockinginDate;
    }

    public void setClockinginDate(Date clockinginDate) {
        this.clockinginDate = clockinginDate;
    }

    public Date getClockinginTime() {
        return clockinginTime;
    }

    public void setClockinginTime(Date clockinginTime) {
        this.clockinginTime = clockinginTime;
    }

    public String getApproveState() {
        return approveState;
    }

    public void setApproveState(String approveState) {
        this.approveState = approveState;
    }

    public String getApproveDesc() {
        return approveDesc;
    }

    public void setApproveDesc(String approveDesc) {
        this.approveDesc = approveDesc;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
