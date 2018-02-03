package sy.service;

import sy.model.po.Stock;
import sy.model.po.StockBean;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2017/12/23.
 */
public interface StockServiceI {
    void addStock(Stock stock);

    DataGrid dataGrid(PageHelper pageHelper, String projectId, String startDate, String endDate, List<Integer> ugroup, String keyword);

    Stock detail(String id);

    StockBean getStockBean(String id);

    Stock update(Stock info);

    void delete(String id);

    List<Stock> getStocks(String cid);
}
