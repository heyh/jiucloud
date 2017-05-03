package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.*;
import sy.model.po.*;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.PriceServiceI;

@Service
public class PriceServiceImpl implements PriceServiceI {

	@Autowired
	PriceDaoI priceDao;

	@Autowired
	Price_CostDaoI price_costDao;

	@Autowired
	CostDaoI costDao;

	@Autowired
	PriceModelDaoI priceModelDao;

	@Override
	public DataGrid dataGrid(int cid, String name, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql = " from Price where cid=:cid ";
		if (name != null && !("".equals(name))) {
			hql += " and name like '%'||:name||'%' ";
			params.put("name", name);
		}
		List<Price> l = priceDao.find(hql, params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(priceDao.count("select count(*) " + hql, params));
		return dg;
	}

	@Override
	public void delete(int id) {
		Price tem = findoneview(id);
		priceDao.delete(tem);
	}

	@Override
	public Price findoneview(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return priceDao.get("from Price where id=:id", params);
	}
	
	@Override
	public List<Price> findListview(String id,String cid) {
		List<Price> list = null;
		return list = priceDao.find("from Price where name like '%%"+id+"%%' and cid="+cid);
	}


	@Override
	public Price add(Price price) {
		priceDao.save(price);
		return price;
	}

	@Override
	public Price update(Price price) {
		priceDao.update(price);
		return price;
	}

	@Override
	public void add2(Price_Cost tem) {
		price_costDao.save(tem);
		// Cost cost = costDao.get("from Cost where id=" + tem.getCost_id());
		// if (cost.getIsend() == 0) {
		// List<Integer> list = (List<Integer>) costDao
		// .getList("select id from TCost where pid=" + cost.getNid()
		// + " and cid=" + cost.getCid());
		// System.out.println(list);
		// for (int integer : list) {
		// Price_Cost pc = new Price_Cost();
		// pc.setPrice_id(tem.getPrice_id());
		// pc.setCost_id(integer);
		// this.add2(pc);
		// }
		// }
	}

	@Override
	public void delete2(Price_Cost tem) {
		price_costDao.delete(tem);
	}

	@Override
	public Price_Cost findoneview2(int cost_id, int price_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cost_id", cost_id);
		params.put("price_id", price_id);
		return price_costDao
				.get("from Price_Cost where cost_id=:cost_id and price_id=:price_id",
						params);
	}

	@Override
	public void initPrice(String cid) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", Integer.parseInt(cid));
		long count = priceDao.count("select count(*) from Price where cid=:cid", params);
		if (count != 0) {
			return;
		}

		Price price = new Price();
		List<PriceModel> list = priceModelDao.find("from PriceModel");
		for (PriceModel tem : list) {
			price = new Price(tem);
			price.setCid(Integer.parseInt(cid));
			this.add(price);
		}

	}

	@Override
	public List<Price> getpPrices(int cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql = " from Price where cid=:cid";
		return priceDao.find(hql, params);
	}


}
