package sy.service.impl;

import java.util.ArrayList;
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
import sy.util.StringUtil;

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
	public void initDefaultPriceCost(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", Integer.parseInt(cid));

		// TPrice
		long count = priceDao.count("select count(*) from Price where cid=:cid", params);
		List<Price> prices = new ArrayList<Price>();
		if (count == 0) {
			Price price = new Price();
			prices = priceDao.find("from Price where cid='195' ");
			for (Price p : prices) {
				price = new Price();
				price.setCid(Integer.parseInt(cid));
				price.setName(p.getName());
				this.add(price);

				params = new HashMap<String, Object>();
				params.put("cid", 195);
				params.put("name", price.getName());
				Price defaultPrice = priceDao.get(" from Price where cid=:cid and name=:name", params);

				params = new HashMap<String, Object>();
				params.put("price_id", defaultPrice.getId());
				List<Price_Cost> priceCosts = price_costDao.find(" from Price_Cost where price_id = :price_id ", params);

				// TCost
				for (Price_Cost priceCost : priceCosts) {

					params = new HashMap<String, Object>();
					params.put("cid", "195");
					params.put("id", priceCost.getCost_id());
					Cost c = costDao.get(" from Cost where cid=:cid and isdelete=0 and id= :id", params);
					if (c == null) {
						continue;
					}

					Cost cost = new Cost();
					cost.setCid(cid);
					cost.setCostType(c.getCostType());
					cost.setIsDelete(c.getIsDelete());
					cost.setFlag(c.getFlag());
					cost.setPid(c.getPid());
					cost.setSort(c.getSort());
					cost.setNid(c.getNid());
					cost.setIsend(c.getIsend());
					cost.setLevel(c.getLevel());
					cost.setItemCode(c.getItemCode());
					costDao.save(cost);

					params = new HashMap<String, Object>();
					params.put("cid", Integer.parseInt(cid));
					params.put("name", price.getName());
					Price priceTem = priceDao.get(" from Price where cid=:cid and name = :name", params);

					params = new HashMap<String, Object>();
					params.put("cid", cid);
					params.put("itemCode", c.getItemCode());
					Cost costTem = costDao.get(" from Cost where cid=:cid and isdelete=0 and itemCode = :itemCode", params);

					params = new HashMap<String, Object>();
					params.put("price_id", priceTem.getId());
					params.put("cost_id", costTem.getId());
					price_costDao.executeSql("insert into TPrice_Cost values(null, :price_id, :cost_id)", params);
				}

			}
		}

		// TCost
		List<Cost> costs = new ArrayList<Cost>();
		Cost cost = new Cost();
		params = new HashMap<String, Object>();
		params.put("cid", "195");
		params.put("isDelete", 0);
		costs = costDao.find(" from Cost where cid=:cid and isDelete=:isDelete", params);

		for (Cost c : costs) {
			params = new HashMap<String, Object>();
			params.put("cid", cid);
			params.put("itemCode", c.getItemCode());
			Cost hasCosts = costDao.get( " from Cost where cid=:cid and itemCode=:itemCode", params);
			if (hasCosts == null) {
				cost = new Cost();
				cost.setCid(cid);
				cost.setCostType(c.getCostType());
				cost.setIsDelete(c.getIsDelete());
				cost.setFlag(c.getFlag());
				cost.setPid(c.getPid());
				cost.setSort(c.getSort());
				cost.setNid(c.getNid());
				cost.setIsend(c.getIsend());
				cost.setLevel(c.getLevel());
				cost.setItemCode(c.getItemCode());
				costDao.save(cost);
			}
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
