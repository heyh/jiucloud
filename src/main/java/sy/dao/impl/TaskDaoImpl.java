package sy.dao.impl;

import org.springframework.stereotype.Repository;

import sy.dao.TaskDaoI;
import sy.model.po.Ttask;
/**
 * 任务DAO实现
 * @author tcp
 *
 */
@Repository
public class TaskDaoImpl extends BaseDaoImpl<Ttask> implements TaskDaoI {

}
