package sy.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.TpcostDaoI;
import sy.model.po.Tpcost;
import sy.service.TpcostServiceI;

@Service
public class TpcostServiceImpl implements TpcostServiceI {

	@Autowired
	TpcostDaoI tpcostDao;

	@Override
	public void add(Tpcost tpcost) {
		tpcostDao.save(tpcost);
	}

	@Override
	public void delete(Tpcost tpcost) {
		tpcostDao.delete(tpcost);
	}

	@Override
	public Tpcost findOneView(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tpcost tem = tpcostDao.get("from Tpcost where id=:id", params);
		return tem;
	}

	@Override
	public Tpcost findOneView(Tpcost tpcost) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pcost_id", tpcost.getPcost_id());
		params.put("department_id", tpcost.getDepartment_id());
		params.put("cost_id", tpcost.getCost_id());
		Tpcost tem = tpcostDao
				.get("from Tpcost where department_id=:department_id and cost_id=:cost_id and pcost_id=:pcost_id",
						params);
		return tem;
	}
}
