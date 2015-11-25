package sy.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.OrderDaoI;
import sy.model.po.Order;
import sy.pageModel.DataGrid;
import sy.pageModel.OrderSearch;
import sy.pageModel.PageHelper;
import sy.service.OrderServiceI;

@Service
public class OrderServiceImpl implements OrderServiceI {

	@Autowired
	OrderDaoI orderDao;

	@Override
	public DataGrid dataGrid(OrderSearch app, PageHelper ph, String uid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Order o where zjsorder_customer=" + uid + " ";
		List<Order> l = orderDao.find(hql + whereHql(app, params), params,
				ph.getPage(), ph.getRows());
		for (Order tem : l) {
			tem.setTime();
		}
		dg.setRows(l);
		dg.setTotal(orderDao.count(
				"select count(*) " + hql + whereHql(app, params), params));
		return dg;
	}

	@Override
	public DataGrid warnDataGrid(OrderSearch app, PageHelper ph, String uid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		long timeStart = 0;
		try {
			timeStart = sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String hql = " from Order o where zjsorder_customer=" + uid
				+ " and zjsorder_time-" + timeStart
				+ " between 0 and 1000*60*60*24*30";
		System.out.println(hql);
		List<Order> l = orderDao.find(hql + whereHql(app, params), params,
				ph.getPage(), ph.getRows());
		for (Order tem : l) {
			tem.setTime();
		}
		dg.setRows(l);
		dg.setTotal(orderDao.count(
				"select count(*) " + hql + whereHql(app, params), params));
		return dg;
	}

	// 查询时使用的动态添加where条件
	private String whereHql(OrderSearch app, Map<String, Object> params) {
		StringBuffer hql = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 如果app中有值，则代表需要模糊查询
		if (null != app) {
			// 判断项目ID是否为空，否则填充查询参数
			if (null != app.getOrderid() && !("".equals(app.getOrderid()))) {
				hql.append(" and o.zjsorder_id like '%'||:oid||'%' ");
				params.put("oid", app.getOrderid());
			}
			// 判断项目名称是否为空，否则填充查询参数
			if (null != app.getOrderName() && !("".equals(app.getOrderName()))) {
				hql.append(" and o.zjsorder_name like '%'||:oName||'%' ");
				params.put("oName", app.getOrderName());
			}

			// 判断项目开工日期是否为空，否则填充查询参数
			if (null != app.getStartTime() && !("".equals(app.getStartTime()))) {
				long timeStart;
				try {
					timeStart = sdf.parse(app.getStartTime()).getTime();
					hql.append(" and o.zjsorder_time > :timeStart ");
					params.put("timeStart", timeStart);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 判断项目竣工日期是否为空，否则填充查询参数
			if (null != app.getEndTime() && !("".equals(app.getEndTime()))) {
				long endStart;
				try {
					endStart = sdf.parse(app.getEndTime()).getTime();
					hql.append(" and o.zjsorder_time < :endStart ");
					params.put("endStart", endStart);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		hql.append("  order by o.zjsorder_id asc");
		return hql.toString();

	}

	@Override
	public void add(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteOne(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Order findOneView(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Order order) {
		// TODO Auto-generated method stub

	}

}
