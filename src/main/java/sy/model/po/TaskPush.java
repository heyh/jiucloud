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

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "push_state")
    private String pushState;

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
    private Date pushResult;
}
