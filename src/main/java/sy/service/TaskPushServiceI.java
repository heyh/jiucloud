package sy.service;

import sy.model.PushExtra;
import sy.model.po.TFieldData;
import sy.model.po.TaskPush;

/**
 * Created by heyh on 2017/7/16.
 */
public interface TaskPushServiceI {

    public TaskPush addTaskPush(TaskPush taskPush);

    public void updateTaskPushByPushId(TaskPush taskPush);

    public TaskPush addFieldTaskPush(TFieldData fieldData);
}
