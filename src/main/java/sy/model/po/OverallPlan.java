package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 2017/12/4.
 */

@Entity
@Table(name = "t_overallplan")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OverallPlan {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "projectId")
    private String projectId;

    @Column(name = "isDelete")
    private String isDelete;

    @Column(name = "needApproved")
    private String needApproved;

    @Column(name = "approvedUser")
    private String approvedUser;

    @Column(name = "currentApprovedUser")
    private String currentApprovedUser;

    @Column(name = "approvedOption")
    private String approvedOption;

    @Column(name = "cid")
    private String cid;

    @Column(name = "uid")
    private String uid;

    @Column(name = "createTime")
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getNeedApproved() {
        return needApproved;
    }

    public void setNeedApproved(String needApproved) {
        this.needApproved = needApproved;
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

    public String getApprovedOption() {
        return approvedOption;
    }

    public void setApprovedOption(String approvedOption) {
        this.approvedOption = approvedOption;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
