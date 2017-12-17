package sy.model.po;

import java.util.Date;

/**
 * Created by heyh on 2017/12/4.
 */

public class MonthPlanBean {

    private int id;

    private String projectId;

    private String projectName;

    private String isDelete;

    private String needApproved;

    private String approvedUser;

    private String currentApprovedUser;

    private String approvedOption;

    private String cid;

    private String uid;

    private String uname;

    private Date createTime;

    private int overallPlanId;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getOverallPlanId() {
        return overallPlanId;
    }

    public void setOverallPlanId(int overallPlanId) {
        this.overallPlanId = overallPlanId;
    }
}
