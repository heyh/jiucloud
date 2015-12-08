package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CostDaoI;
import sy.dao.CostModelDaoI;
import sy.dao.Department_CostDaoI;
import sy.model.S_department;
import sy.model.po.Cost;
import sy.model.po.CostModel;
import sy.model.po.Department_Cost;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.CostServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CostServiceImpl implements CostServiceI {

	@Autowired
	private CostDaoI costDaoI;

	@Autowired
	private CostModelDaoI costModelDaoI;

	@Autowired
	private Department_CostDaoI department_CostDao;

	@Override
	public DataGrid dataGrid(int department_id, String cid,String source) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql="";
		if (source!=null && source.equals("data"))
		{
			hql = " from Cost t  where cid=:cid and isdelete=0 and (itemcode is null or itemcode='' or substring(itemcode,1,3)!='000' and substring(itemcode,1,3)<=900) ";
		} else if (source!=null && source.equals("doc")) {
			hql = " from Cost t  where cid=:cid and isdelete=0 and itemcode is not null and itemcode!='' and  (substring(itemcode,1,3)='000' or substring(itemcode,1,3)>900) ";
		} else {
            hql = " from Cost t  where cid=:cid and isdelete=0";
        }
		if (department_id != 0) {
			params.put("department_id", department_id);
			hql += " and id in (select cost_id from Department_Cost where department_id=:department_id)";
		}
		hql += " order by t.sort,t.itemCode asc ";
		List<Cost> l = costDaoI.find(hql, params);
		if (l.size() == 0) {
			params.remove("department_id");
			if (source.equals("data"))
			{
				hql = "from Cost t  where cid=:cid and isdelete=0 and (itemcode is null or itemcode='' or substring(itemcode,1,3)!='000' and substring(itemcode,1,3)<=900)  order by t.sort,t.itemCode asc";
			} else if (source.equals("doc")) {
				hql = "from Cost t  where cid=:cid and isdelete=0 and itemcode is not null and itemcode!='' and  (substring(itemcode,1,3)='000' or substring(itemcode,1,3)>900) order by t.sort,t.itemCode asc";
			} else {
                hql = "from Cost t  where cid=:cid and isdelete=0 order by t.sort,t.itemCode asc";
            }
			l = costDaoI.find(hql, params);
		}

		dg.setRows(l);
		dg.setTotal(costDaoI.count("select count(*) " + hql, params));
		return dg;
	}

	@Override
	public DataGrid dataGrid(String title, String code, PageHelper ph,
			String cid) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql = " from Cost t  where cid=:cid and isdelete=0 ";
		List<Cost> l = costDaoI.find(hql + whereHql(title, code, params),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(costDaoI.count(
				"select count(*) " + hql + whereHql(title, code, params),
				params));
		return dg;
	}

	private String whereHql(String costtype, String code,
			Map<String, Object> params) {
		String hql = "";
		if (costtype != null && costtype.length() > 0) {
			hql += " and t.costType like :costtype";
			params.put("costtype", "%" + costtype + "%");
		}
		if (code != null && code.length() > 0) {
			hql += " and t.itemCode like :itemCode";
			params.put("itemCode", code + "%");
		}
		hql += " order by itemCode asc";
		return hql;
	}

	/**
	 * 根据Id查询
	 * 
	 * @return
	 */
	@Override
	public Cost findById(String id) {
		if (id == null) {
			return null;
		}
		return costDaoI.get(Cost.class, Integer.parseInt(id));
	}

	@Override
	public Cost findOneView(String nid, String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nid", nid);
		params.put("cid", cid);
		return costDaoI.get("from Cost where nid=:nid and cid=:cid", params);
	}

	/**
	 * 添加
	 */
	@Override
	public void add(Cost info) {
		costDaoI.saveOrUpdate(info);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Cost info) {
		Cost c = costDaoI.get(Cost.class, info.getId());
		BeanUtils.copyProperties(info, c);

	}

	@Override
	public int getMaxSortByPid(String pid, String cid) {
		if (pid == null || pid.length() == 0) {
			pid = "-1";
		}
		Integer sum = (Integer) costDaoI
				.getObject("select max(sort) from Cost where pid=" + pid
						+ " and cid=" + cid);
		return sum == null ? 0 : sum;
	}

	@Override
	public String getMaxNidByCid(String cid) {
		String sum = (String) costDaoI
				.getObject("select max(nid) from Cost where cid=" + cid);
		sum = (sum == null ? "0" : sum);
		return String.valueOf(Integer.parseInt(sum) + 1);
	}

	// @Override
	// public boolean upSort(String id) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("id", Integer.parseInt(id));
	// Cost c = costDaoI.get("from Cost where ID=:id", map);
	// map.remove("id");
	// map.put("pid", c.getPid());
	// map.put("sort", c.getSort());
	// map.put("cid", c.getCid());
	// Cost tem = costDaoI
	// .get("from Cost where pid=:pid and cid=:cid and sort=(select MAX(sort) from Cost where pid=:pid and cid=:cid and sort<:sort)",
	// map);
	// if (tem == null) {
	// return false;
	// }
	// int ttem = c.getSort();
	// c.setSort(tem.getSort());
	// tem.setSort(ttem);
	// costDaoI.update(c);
	// costDaoI.update(tem);
	// return true;
	// }
	//
	// @Override
	// public boolean downSort(String id) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("id", Integer.parseInt(id));
	// Cost c = costDaoI.get("from Cost where ID=:id", map);
	// map.remove("id");
	// map.put("pid", c.getPid());
	// map.put("sort", c.getSort());
	// map.put("cid", c.getCid());
	// Cost tem = costDaoI
	// .get("from Cost where pid=:pid and sort=(select MIN(sort) from Cost where pid=:pid and cid=:cid and sort>:sort)",
	// map);
	// if (tem == null) {
	// return false;
	// }
	// int ttem = c.getSort();
	// c.setSort(tem.getSort());
	// tem.setSort(ttem);
	// costDaoI.update(c);
	// costDaoI.update(tem);
	// return true;
	// }

	@Override
	public void init(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		long count = costDaoI.count(
				"select count(*) from Cost where cid=:cid and flag=1", params);
		if (count != 0)
			return;
		List<Cost> dlist = costDaoI.find("from CostModel where cid=:cid",
				params);
		for (Cost tem : dlist) {
			this.delete(tem);
		}
		Integer sum = (Integer) costDaoI.getObject("select max(id) from Cost");
		if (sum == null) {
			sum = 0;
		}
		List<CostModel> list = costModelDaoI.find("from CostModel");
		for (CostModel tem : list) {
			Cost cost = new Cost(tem);
			cost.setCid(cid);
			cost.setId(++sum);
			this.add(cost);
		}
	}

	@Override
	public List<Cost> getFamily(String nid, String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("id", nid);
		List<Cost> list = new ArrayList<Cost>();
		String hql = "from Cost where nid=:id and cid=:cid order by id";
		Cost cost = costDaoI.get(hql, params);
		list.add(cost);
		this.getGroup(cost, list, cid);
		return list;
	}

	private void getGroup(Cost cost, List<Cost> list, String cid) {
		List<Cost> dlist = costDaoI.find("from Cost where pid=" + cost.getNid()
				+ " and cid=" + cid + " order by id");
		for (Cost tem : dlist) {
			list.add(tem);
			this.getGroup(tem, list, cid);
		}
	}

	@Override
	public List<Cost> getEndCosts(String title, String code, PageHelper ph,
			String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql = " from Cost t where cid=:cid and isdelete=0";
		if (title != null && title.length() > 0) {
			hql += " and costType like '%" + title + "%' ";
		}
		if (code != null && code.length() > 0) {
			hql += " and itemCode like '" + code + "%' ";
		}
		hql += " order by level asc";
		System.out.println(hql);
		return costDaoI.find(hql, params, ph.getPage(), ph.getRows());
	}

	@Override
	public List<Cost> getprices(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		List<Cost> costs = costDaoI.find(
				"from Cost where and isDelete=0 and cid=:cid order by id",
				params);
		return costs;
	}

	/**
	 * 
	 * 删除
	 * 
	 * @param id
	 *            void
	 */

	@Override
	public void delete(Cost cost) {
		costDaoI.delete(cost);
	}

	@Override
	public void treedelete(Cost cost) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cost.getCid());
		params.put("nid", cost.getNid());
		List<Cost> costs = costDaoI.find(
				"from Cost where isDelete=0 and cid=:cid and pid=:nid", params);
		for (Cost tem : costs) {
			System.out.println(tem);
			treedelete(tem);
			this.delete(tem);
		}
	}

	@Override
	public DataGrid dataGridWithPrice(String title, String code, PageHelper ph,
			String cid, int price_id) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("price_id", price_id);
		String hql = " from Cost t  where cid=:cid and isdelete=0 and id not in(select cost_id from Price_Cost where price_id=:price_id) ";
		List<Cost> l = costDaoI.find(hql + whereHql(title, code, params),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(costDaoI.count(
				"select count(*) " + hql + whereHql(title, code, params),
				params));
		return dg;
	}

	@Override
	public DataGrid dataGridInPrice(String title, String code, PageHelper ph,
			String cid, int price_id) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("price_id", price_id);
		String hql = " from Cost t  where cid=:cid and isdelete=0 and id in(select cost_id from Price_Cost where price_id=:price_id) ";
		List<Cost> l = costDaoI.find(hql + whereHql(title, code, params),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(costDaoI.count(
				"select count(*) " + hql + whereHql(title, code, params),
				params));
		return dg;
	}

	@Override
	public Cost getParentByCode(String itemCode, String cid) {
		Cost parent = null;
		for (int i = itemCode.length() - 1; i > 0; i--) {
			parent = this.getCostByCode(itemCode.substring(0, i), cid);
			if (parent != null) {
				return parent;
			}
		}
		return null;
	}

	@Override
	public Cost getCostByCode(String itemCode, String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("itemCode", itemCode);
		params.put("cid", cid);
		return costDaoI.get("from Cost where itemCode=:itemCode and cid=:cid ",
				params);
	}

	@Override
	public List<Cost> getLikeCostByCode(String itemCode, String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("itemCode", itemCode + "%");
		params.put("cid", cid);
		return costDaoI
				.find("from Cost where itemCode like :itemCode and cid=:cid ",
						params);
	}

	@Override
	public DataGrid departmentGrid(String cid) {
		DataGrid dg = new DataGrid();
//		String sql = "select id,name from jsw_corporation_department where user_id=0 and endnode=0 and company_id=" + cid;
        String sql = "select id,name from jsw_corporation_department where endnode=0 and company_id=" + cid;
		List<Object[]> result = costDaoI.findBySql(sql);
		List<S_department> departments = new ArrayList<S_department>();
		for (Object[] tem : result) {
			System.out.println(tem);
			S_department department = new S_department();
			department.setId((Integer) tem[0]);
			department.setName((String) tem[1]);
			departments.add(department);
		}

		dg.setRows(departments);
		dg.setTotal((long) departments.size());
		return dg;
	}

	@Override
	public void add2(Department_Cost tem) {
		department_CostDao.save(tem);
	}

	@Override
	public void delete2(Department_Cost tem) {
		department_CostDao.delete(tem);
	}

	@Override
	public Department_Cost findoneview2(int cost_id, int department_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cost_id", cost_id);
		params.put("department_id", department_id);
		return department_CostDao
				.get("from Department_Cost where cost_id=:cost_id and department_id=:department_id",
						params);
	}

	@Override
	public DataGrid dataGridWithDepartment(String title, String code,
			PageHelper ph, String cid, int department_id) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("department_id", department_id);
		String hql = " from Cost t  where cid=:cid and isdelete=0 and id not in(select cost_id from Department_Cost where department_id=:department_id) ";
		List<Cost> l = costDaoI.find(hql + whereHql(title, code, params),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(costDaoI.count(
				"select count(*) " + hql + whereHql(title, code, params),
				params));
		return dg;
	}

	@Override
	public DataGrid dataGridInDepartment(String title, String code,
			PageHelper ph, String cid, int department_id) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("department_id", department_id);
		String hql = " from Cost t  where cid=:cid and isdelete=0 and id in(select cost_id from Department_Cost where department_id=:department_id) ";
		List<Cost> l = costDaoI.find(hql + whereHql(title, code, params),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(costDaoI.count(
				"select count(*) " + hql + whereHql(title, code, params),
				params));
		return dg;
	}
}
