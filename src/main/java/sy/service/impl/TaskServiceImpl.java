package sy.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.TaskDaoI;
import sy.dao.UserDaoI;
import sy.model.S_department;
import sy.model.Tuser;
import sy.model.po.Ttask;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.User;
import sy.service.TaskServiceI;

@Service
public class TaskServiceImpl implements TaskServiceI {

	@Autowired
	private TaskDaoI taskDao;

	@Autowired
	private UserDaoI userDao;

	@Override
	public DataGrid dataGrid(String sTime, String eTime, List<Integer> ugroup,
			String principal, PageHelper ph) {
		DataGrid dg = new DataGrid();
		List<sy.pageModel.Task> bl = new ArrayList<sy.pageModel.Task>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t.id,t.title,t.content,t.attachment,t.stime,t.etime,t.remark,t.status,t.pids,t.comments,t.uids,j.realname,t.sender from Ttask t,jsw_user j ";
		System.out.println(hql
				+ whereHql(sTime, eTime, ugroup, principal, params)
				+ " and j.id=t.pids " + orderHql(ph));
		List<Object[]> l = taskDao.findBySql(
				hql + whereHql(sTime, eTime, ugroup, principal, params)
						+ " and j.id=t.pids " + orderHql(ph), params,
				ph.getPage(), ph.getRows());
		if (l != null && l.size() > 0) {
			for (Object[] t : l) {
				sy.pageModel.Task b = new sy.pageModel.Task();
				b.setId((Integer) t[0]);
				b.setTitle((String) t[1]);
				b.setContent((String) t[2]);
				b.setAttachment((String) t[3]);
				b.setStime(((Date) t[4]));
				b.setEtime(((Date) t[5]));
				String fsuffix = (t[4] == null || t[4].toString().length() == 0) ? "未定义"
						: String.valueOf(((Date) t[4]));
				String tsuffix = (t[5] == null || t[5].toString().length() == 0) ? "未定义"
						: String.valueOf(((Date) t[5]));
				b.setTime(fsuffix + "-" + tsuffix);
				b.setRemark((String) t[6]);
				if ((Integer) t[7] == 0) {
					b.setStatus("完成");
				} else {
					b.setStatus("已完成");
				}
				b.setPids((String) t[8]);
				b.setComment((String) t[9]);
				b.setUids((String) t[10]);
				b.setPrincipal((String) t[11]);
				b.setSender((String) t[12]);
				bl.add(b);
			}
		}
		dg.setRows(bl);
		dg.setTotal((long)l.size());
		return dg;
	}

	@Override
	public List<sy.pageModel.Task> getfindList(List<Integer> ugroup) {
		List<sy.pageModel.Task> bl = new ArrayList<sy.pageModel.Task>();
		String hql = "select t.id,t.title,t.content,t.attachment,t.stime,t.etime,t.remark,t.status,t.principal,t.comments,j.user_name from Ttask t,jsw_user j where 1=1 ";
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and uids in(" + ugroup.get(0).toString();
			for (int i = 1; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ") ";
		}
		List<Object[]> l = taskDao.findBySql(hql
				+ " and uids=user_id  order by id desc limit 0,8");
		if (l != null && l.size() > 0) {
			for (Object[] t : l) {
				sy.pageModel.Task b = new sy.pageModel.Task();
				b.setId((Integer) t[0]);
				b.setTitle((String) t[1]);
				bl.add(b);
			}
		}
		return bl;
	}

	private String whereHql(String sTime, String eTime, List<Integer> ugroup,
			String principal, Map<String, Object> params) {
		String whereHql = " where 1=1 ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (sTime != null && !sTime.equals("")) {
			whereHql += "  and t.stime >= :sTime ";
			try {
				params.put("sTime", sdf.parse(sTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (eTime != null && !eTime.equals("")) {
			whereHql += " and t.etime <= :eTime";
			try {
				params.put("eTime", sdf.parse(eTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (principal != null && !"".equals(principal)) {
			whereHql += " and (t.pids in (select id from jsw_user where realname like :principal) or t.uids in (select id from jsw_user where realname like :principal))";
			params.put("principal", "%" + principal + "%");
		}
		if (ugroup != null && ugroup.size() > 0) {
			whereHql += " and (uids in(" + ugroup.get(0).toString();
			for (int i = 1; i < ugroup.size(); i++) {
				whereHql += "," + ugroup.get(i).toString();
			}
			whereHql += ") or pids in(" + ugroup.get(0).toString();
			for (int i = 1; i < ugroup.size(); i++) {
				whereHql += "," + ugroup.get(i).toString();
			}
			whereHql += ")) ";
		}
		return whereHql;
	}

	private String orderHql(PageHelper ph) {
		String orderString = "";
		if (ph.getSort() != null && ph.getOrder() != null) {
			orderString = " order by " + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	/**
	 * 增加任务
	 */
	@Override
	public void add(Ttask task) {
		taskDao.save(task);
	}

	/**
	 * 根据id查询
	 */
	@Override
	public sy.pageModel.Task findById(String id) {
		Ttask t = taskDao.get("from Ttask where id=" + Integer.parseInt(id));
		sy.pageModel.Task l = new sy.pageModel.Task();
		l.setAttachment(t.getAttachment());
		String str = t.getAttachment().substring(
				t.getAttachment().lastIndexOf("/") + 1);
		l.setAttachmentname(str);
		l.setContent(t.getContent());
		l.setTitle(t.getTitle());
		if (t.getStatus() == 0) {
			l.setStatus("未完成");
		} else {
			l.setStatus("完成");
		}
		l.setStime(t.getStime());
		l.setEtime(t.getEtime());
		// 发布人，责任人
		Tuser u = userDao.get("from Tuser where id='" + t.getUids() + "'");
		l.setPrincipal(t.getPrincipal());
		l.setUids(u.getUsername());
		return l;
	}

	/**
	 * 修改状态
	 */
	@Override
	public void updateStatus(String id) {
		Ttask t = taskDao.get("from Ttask where id=" + Integer.parseInt(id));
		t.setStatus(1);
		taskDao.update(t);
	}

	/**
	 * 评论
	 */
	@Override
	public void updateComment(String id, String comment) {
		Ttask t = taskDao.get("from Ttask where id=" + Integer.parseInt(id));
		t.setComments(comment);
		taskDao.update(t);
	}

	@Override
	public List<S_department> getDepsByCompanyId(int cid) {
		List<S_department> list = new ArrayList<S_department>();
		String sql = "select id,name from jsw_corporation_department where endnode=0 and parent_id=0 and company_id="
				+ cid;
		List<Object[]> deps = taskDao.findBySql(sql);
		System.out.println(deps);
		if (deps != null && deps.size() > 0) {
			for (Object[] tem : deps) {
				S_department department = new S_department();
				department.setId((Integer) tem[0]);
				department.setName((String) tem[1]);
				list.add(department);
			}
		}
		return list;
	}

	@Override
	public List<User> getUserByDepId(int id) {
		List<User> list = new ArrayList<User>();
		String sql = "select user_id,name from jsw_corporation_department where parent_id="
				+ id + " and endnode=1";
		List<Object[]> us = taskDao.findBySql(sql);
		if (us != null && us.size() > 0) {
			for (Object[] tem : us) {
				User user = new User();
				user.setId(String.valueOf(tem[0]));
				user.setUsername((String) tem[1]);
				list.add(user);
			}
		}
		return list;
	}
}
