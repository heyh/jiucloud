package sy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.AnalysisDaoI;
import sy.dao.CostDaoI;
import sy.model.po.Cost;
import sy.model.po.Price;
import sy.model.po.Project;
import sy.pageModel.AnalysisData;
import sy.pageModel.AnalysisSearch;
import sy.service.AnalysisServiceI;

/**
 * **************************************************************** 文件名称 :
 * IMUserServiceImpl.java 作 者 : Administrator 创建时间 : 2015年6月4日11:23:24 文件描述 :
 * 轻应用Service 版权声明 : 修改历史 : 2015年6月4日 1.00 初始版本
 *****************************************************************
 */

@Service
public class AnalysisServiceImpl implements AnalysisServiceI {

	@Autowired
	private AnalysisDaoI analysisDao;

	@Autowired
	private CostDaoI costDaoI;

	@Override
	public List<AnalysisData> getList(String date, String date2, int price_id,
			int project_id, List<Integer> ugroup) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (date == null || date.length() == 0) {
			date = "now()";
		}
		params.put("price_id", price_id);
		params.put("project_id", project_id);

		String hql = "select c.costType,sum(f.count*f.price),c.isend,c.itemCode,f.price,f.unit,sum(f.count) from TCost c LEFT JOIN TFieldData f on f.itemCode=c.itemCode and f.projectName=:project_id  and f.isDelete=0";
		if (date != null && date.length() > 0) {
			hql += " and f.creatTime>='" + date + "'";
		}
		if (date2 != null && date2.length() > 0) {
			hql += " and f.creatTime<='" + date2 + "'";
		}
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and f.uid in (0";
			for (int i = 0; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ") ";
		}

		hql += " where c.isDelete=0 and c.ID in (select cost_id from TPrice_Cost where price_id=:price_id) GROUP BY c.ID,f.price,f.unit";

		List<Object[]> list = analysisDao.findBySql(hql, params);
		List<AnalysisData> result = new ArrayList<AnalysisData>();
		for (Object[] tem : list) {
			AnalysisData a = new AnalysisData();
			a.setCostType((String) tem[0]);
			if (tem[1] != null) {
				a.setMoney((Double) tem[1]);
			}
			a.setIsend((Integer) tem[2]);
			a.setItemCode((String) tem[3]);
			a.setPrice((String) tem[4]);
			a.setUnit((String) tem[5]);
			if (tem[6] == null) {
				a.setCount(0);
			} else {
				a.setCount((Double) tem[6]);
			}
			result.add(a);
		}
		return result;
	}
	
	@Override
	public List<AnalysisData> getList(String date, String date2, String price_id,
			String project_id, List<Integer> ugroup,String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (date == null || date.length() == 0) {
			date = "now()";
		}
//		String hql = "select c.costType,sum(f.count*f.price),c.isend,c.itemCode,f.price,f.unit,sum(f.count) from TCost c LEFT JOIN TFieldData f on f.itemCode=c.itemCode and f.projectName=:project_id  and f.isDelete=0";
		String hql = "select c.costType,sum(f.count*f.price),c.isend,c.itemCode,f.price,f.unit,sum(f.count) from TCost c LEFT JOIN TFieldData f on f.itemCode=c.itemCode where  f.isDelete=0";
		if(project_id!=null&&!"".equals(project_id)){
			hql += " and f.projectName in (select p.id from tgc_Project p where p.proName like :project_id and p.compId="+cid+") ";
			params.put("project_id", "%%"+project_id+"%%");
		}
		if (date != null && date.length() > 0) {
			hql += " and f.creatTime>='" + date + "'";
		}
		if (date2 != null && date2.length() > 0) {
			hql += " and f.creatTime<='" + date2 + "'";
		}
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and f.uid in (0";
			for (int i = 0; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ") ";
		}

		hql += " and c.isDelete=0 ";
		if(price_id!=null&&!"".equals(price_id)){
			hql += "and c.ID in (select cost_id from TPrice_Cost where price_id in (select tp.id from TPrice tp where tp.name like :price_id and tp.cid="+cid+"))";
			params.put("price_id", "%%"+price_id+"%%");
		}
		hql += " GROUP BY c.ID,f.price,f.unit";

		List<Object[]> list = analysisDao.findBySql(hql, params);
		List<AnalysisData> result = new ArrayList<AnalysisData>();
		for (Object[] tem : list) {
			AnalysisData a = new AnalysisData();
			a.setCostType((String) tem[0]);
			if (tem[1] != null) {
				a.setMoney((Double) tem[1]);
			}
			a.setIsend((Integer) tem[2]);
			a.setItemCode((String) tem[3]);
			a.setPrice((String) tem[4]);
			a.setUnit((String) tem[5]);
			if (tem[6] == null) {
				a.setCount(0);
			} else {
				a.setCount((Double) tem[6]);
			}
			result.add(a);
		}
		return result;
	}

	@Override
	public List<AnalysisData> getTable(AnalysisSearch model,
			List<Integer> ugroup, List<Project> projects, List<Price> prices,String cid) {
		List<AnalysisData> datas = new ArrayList<AnalysisData>();
		for (Project ptem : projects) {
			AnalysisData tem = new AnalysisData();
			List<Double> moneys = new ArrayList<Double>();
			tem.setProject_name(ptem.getProName());
			for (Price price : prices) {
				moneys.add(getTotalByname(model, ugroup, price.getId(), ptem.getId(),cid));
			}
			tem.setMoneys(moneys);
			datas.add(tem);
		}
		return datas;
	}

	private double getTotal(AnalysisSearch model, List<Integer> ugroup,
			int price_id, int project_id) {
		// String hql =
		// "SELECT f.id,SUM(f.count*f.price) FROM TFieldData f,tgc_Project p where f.projectName=p.ID and f.isDelete=0 and p.id="
		// + project_id
		// +
		// " and f.costType in(select a.cost_id from TPrice_Cost a,TCost b where a.cost_id=b.id and b.isDelete=0 and a.price_id="
		// + price_id + ")";

		String hql = "select c.costType,sum(f.count*f.price) from TCost c LEFT JOIN TFieldData f on f.itemCode like CONCAT(c.itemCode,'%') and f.projectName="
				+ project_id + " and f.isDelete=0";

		hql += " where c.isDelete=0 and c.ID in (select cost_id from TPrice_Cost where price_id="
				+ price_id + ")";
		if (model.getStartTime() != null && model.getStartTime().length() > 0) {
			hql += " and f.creatTime>='" + model.getStartTime() + "'";
		}
		if (model.getEndTime() != null && model.getEndTime().length() > 0) {
			hql += " and f.creatTime<='" + model.getEndTime() + "'";
		}
		if (model.getCost_id() != null && model.getCost_id().length() > 0) {
			Cost cost = costDaoI
					.get("from Cost where id=" + model.getCost_id());
			System.out.println(cost.getItemCode());
			hql += " and f.itemCode like '" + cost.getItemCode() + "%' ";
		}
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and f.uid in (0";
			for (int i = 0; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ")  GROUP BY c.ID ";
		}
		List<Object[]> list = analysisDao.findBySql(hql);
		double result = 0;
		for (Object[] tem : list) {
			if (tem[1] != null) {
				result += (Double) tem[1];
			}
		}
		return result;
	}
	
	private double getTotalByname(AnalysisSearch model, List<Integer> ugroup,
			int price_id, int project_id,String cid) {
		List<Cost> listco = null;
		// String hql =
		// "SELECT f.id,SUM(f.count*f.price) FROM TFieldData f,tgc_Project p where f.projectName=p.ID and f.isDelete=0 and p.id="
		// + project_id
		// +
		// " and f.costType in(select a.cost_id from TPrice_Cost a,TCost b where a.cost_id=b.id and b.isDelete=0 and a.price_id="
		// + price_id + ")";

		String hql = "select c.costType,sum(f.count*f.price) from TCost c LEFT JOIN TFieldData f on f.itemCode like CONCAT(c.itemCode,'%') and f.projectName="
				+ project_id + " and f.isDelete=0";

		hql += " where c.isDelete=0 and c.ID in (select cost_id from TPrice_Cost where price_id="
				+ price_id + ")";
		if (model.getStartTime() != null && model.getStartTime().length() > 0) {
			hql += " and f.creatTime>='" + model.getStartTime() + "'";
		}
		if (model.getEndTime() != null && model.getEndTime().length() > 0) {
			hql += " and f.creatTime<='" + model.getEndTime() + "'";
		}
		if (model.getCostTypeName() != null && model.getCostTypeName().length() > 0) {
			listco = costDaoI.find("from Cost where costType like '%%" + model.getCostTypeName()+"%%' and cid="+cid);
			for(Cost cost:listco){
				hql += " and f.itemCode like '" + cost.getItemCode() + "%' ";
			}
		}
		if (ugroup != null && ugroup.size() > 0) {
			hql += " and f.uid in (0";
			for (int i = 0; i < ugroup.size(); i++) {
				hql += "," + ugroup.get(i).toString();
			}
			hql += ")  GROUP BY c.ID ";
		}
		List<Object[]> list = analysisDao.findBySql(hql);
		double result = 0;
		for (Object[] tem : list) {
			if (tem[1] != null) {
				result += (Double) tem[1];
			}
		}
		return result;
	}
}
