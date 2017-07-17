package sy.service.impl;

import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.TaskPushDaoI;
import sy.model.Item;
import sy.model.PushExtra;
import sy.model.po.TFieldData;
import sy.model.po.TaskPush;
import sy.service.TaskPushServiceI;
import sy.util.Constant;
import sy.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heyh on 2017/7/16.
 */

@Service
public class TaskPushServiceImpl implements TaskPushServiceI {

    @Autowired
    private TaskPushDaoI taskPushDao;

    @Override
    public TaskPush addTaskPush(TaskPush taskPush) {

        taskPushDao.save(taskPush);
        return taskPush;
    }

    @Override
    public void updateTaskPushByPushId(TaskPush taskPush) {

        TaskPush oldTaskPush = taskPushDao.get(TaskPush.class, taskPush.getId());
        BeanUtils.copyProperties(taskPush, oldTaskPush);
    }

    @Override
    public TaskPush addFieldTaskPush(TFieldData fieldData) {
        TaskPush taskPush = new TaskPush();
        taskPush.setTaskId(StringUtil.trimToEmpty(fieldData.getId()));
        taskPush.setUserId(fieldData.getCurrentApprovedUser());
        taskPush.setTaskType(Constant.TaskType.APPROVE);
        taskPush.setPushType(Constant.PushType.NOTIFICATION_PUSH);
        taskPush.setPushState(Constant.PushState.PUSH_INIT);

        net.sf.json.JSONObject jsonExtra = new net.sf.json.JSONObject();
        PushExtra pushExtra = new PushExtra();
        pushExtra.setAlert("新增审批任务,请您及时处理");
        jsonExtra.put("taskId",StringUtil.trimToEmpty(fieldData.getId()));
        pushExtra.setExtra(jsonExtra);
        pushExtra.setMessage("新增审批任务,请您及时处理");
        taskPush.setExtra(StringUtil.trimToEmpty(net.sf.json.JSONObject.fromObject(pushExtra)));

        return addTaskPush(taskPush);
    }
}
