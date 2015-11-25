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

import sy.dao.LogDaoI;
import sy.model.po.Tlog;
import sy.pageModel.DataGrid;
import sy.pageModel.Log;
import sy.pageModel.PageHelper;
import sy.service.LogServiceI;

@Service
public class LogServiceImpl implements LogServiceI {

	@Autowired
	private LogDaoI logDao;

	/**
	 * 数据列表
	 */
	@Override
	public DataGrid dataGrid(String sTime, String eTime, List<Integer> ugroup,
			PageHelper ph) {
		DataGrid dg = new DataGrid();
		List<sy.pageModel.Log> bl = new ArrayList<sy.pageModel.Log>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t.id,t.title,t.content,t.attachment,t.ctime,j.user_name from Tlog t,jsw_user j";
		// + ",(select realname from tuser where id = uids) from Tlog ";
		List<Object[]> l = logDao.findBySql(
				hql + whereHql(sTime, eTime, ugroup, params)
						+ " and j.user_id=t.uids " + orderHql(ph), params,
				ph.getPage(), ph.getRows());
		if (l != null && l.size() > 0) {
			for (Object[] t : l) {
				sy.pageModel.Log b = new sy.pageModel.Log();
				b.setId((Integer) t[0]);
				b.setTitle((String) t[1]);
				b.setContent((String) t[2]);
				b.setAttachment((String) t[3]);
				b.setCtime((Date) t[4]);
				b.setUid((String) t[5]);
				bl.add(b);
			}
		}
		dg.setRows(bl);
		dg.setTotal(logDao.count(
				"select count(*) from Tlog t "
						+ whereHql(sTime, eTime, ugroup, params), params));
		return dg;
	}

	@Override
	public List<Log> getfindList(List<Integer> ugroup) {
		List<sy.pageModel.Log> bl = new ArrayList<sy.pageModel.Log>();
		String hql = "select t.id,t.title,t.content,t.attachment,t.ctime,j.user_name from Tlog t,jsw_user j where 1=1";
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and uids in(" + ugroup.get(0).toString();
			for (int i = 1; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ") ";
		}
		List<Object[]> l = logDao.findBySql(hql
				+ " and j.user_id=t.uids order by id desc limit 0,8");
		if (l != null && l.size() > 0) {
			for (Object[] t : l) {
				sy.pageModel.Log b = new sy.pageModel.Log();
				b.setId((Integer) t[0]);
				b.setTitle((String) t[1]);
				b.setContent((String) t[2]);
				b.setAttachment((String) t[3]);
				b.setCtime((Date) t[4]);
				b.setUid((String) t[5]);
				bl.add(b);
			}
		}
		return bl;
	}

	private String whereHql(String sTime, String eTime, List<Integer> ugroup,
			Map<String, Object> params) {
		String whereHql = " where 1=1 ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (sTime != null && !sTime.equals("")) {
			whereHql += "  and t.ctime >= :sTime";
			try {
				params.put("sTime", sdf.parse(sTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (eTime != null && !eTime.equals("")) {
			whereHql += " and t.ctime <= :eTime";
			try {
				params.put("eTime", sdf.parse(eTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (ugroup != null && ugroup.size() > 0) {
			whereHql += " and uids in(" + ugroup.get(0).toString();
			for (int i = 1; i < ugroup.size(); i++) {
				whereHql += "," + ugroup.get(i).toString();
			}
			whereHql += ") ";
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
	 * 增加日志
	 */
	@Override
	public void add(Tlog log) {
		logDao.save(log);
	}

	/**
	 * 根据id查日志
	 */
	@Override
	public Log findById(String id) {
		Tlog t = logDao.get("from Tlog where id=" + Integer.parseInt(id));
		sy.pageModel.Log l = new sy.pageModel.Log();
		l.setAttachment(t.getAttachment());
		String str = t.getAttachment().substring(
				t.getAttachment().lastIndexOf("/") + 1);
		l.setAttachmentname(str);
		l.setContent(t.getContent());
		l.setTitle(t.getTitle());
		return l;
	}

}
