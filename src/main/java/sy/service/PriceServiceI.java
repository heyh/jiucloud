package sy.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import sy.model.po.Price;
import sy.model.po.Price_Cost;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface PriceServiceI {

	public DataGrid dataGrid(int cid, String name, PageHelper ph);

	public List<Price> getpPrices(int cid);

	public void delete(int id);

	public Price update(Price price);

	public Price findoneview(int id);

	public Price add(Price price);

	void add2(Price_Cost tem);

	public List<Price> findListview(String id,String cid);
	
	void delete2(Price_Cost tem);

	Price_Cost findoneview2(int cost_id, int price_id);
}
