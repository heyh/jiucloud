package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 2017/7/16.
 */

@Entity
@Table(name = "t_task_push")
public class TaskPush {
    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    /* 任务号 哪条记录 */
    @Column(name = "task_id")
    private String taskId;

    /* 任务类型 */
    @Column(name = "task_type")
    private String taskType;

    /* 推送状态：0 未推送， 1 待推送，2 推送中， 3 推送成功， 4 推送失败 */
    @Column(name = "push_state")
    private String pushState;

    /* 推送类型：0 notification, 1 message */
    @Column(name = "push_type")
    private String pushType;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "push_time")
    private Date pushTime;

    @Column(name = "push_result")
    private String pushResult;

    @Column(name = "extra")
    private String extra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPushState() {
        return pushState;
    }

    public void setPushState(String pushState) {
        this.pushState = pushState;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getPushResult() {
        return pushResult;
    }

    public void setPushResult(String pushResult) {
        this.pushResult = pushResult;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
