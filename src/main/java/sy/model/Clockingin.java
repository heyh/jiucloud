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

    @Column(name = "clockinginTime")
    private Date clockinginTime;

    @Column(name = "isRight")
    private String isRight;

    @Column(name = "approveUser")
    private String approveUser;

    @Column(name = "approveTime")
    protected Date approveTime;

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

    public Date getClockinginTime() {
        return clockinginTime;
    }

    public void setClockinginTime(Date clockinginTime) {
        this.clockinginTime = clockinginTime;
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
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
}
