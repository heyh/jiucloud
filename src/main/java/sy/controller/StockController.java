package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Stock;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.StockServiceI;
import sy.util.ConfigUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyh on 2017/12/23.
 */

@Controller
@RequestMapping("/stockController")
public class StockController {

    @Autowired
    private StockServiceI stockService;

    @RequestMapping("/StockList")
    public String fieldDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/materials/stock/StockList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        List<Stock> stocks = new ArrayList<Stock>();
        DataGrid dataGrid = new DataGrid();
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        dataGrid.setRows(stocks);
        dataGrid.setTotal((long) 0);

        return dataGrid;

    }
}
