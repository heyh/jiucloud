package sy.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Stock;
import sy.model.po.StockBean;
import sy.pageModel.*;
import sy.service.StockServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
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
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        List<Integer> ugroup = sessionInfo.getUgroup();

        String startDate = "";
        String endDate = "";
        if (!StringUtil.trimToEmpty(request.getParameter("startDate")).equals("")) {
            startDate = StringUtil.trimToEmpty(request.getParameter("startDate")) + " 00:00:00";
        } else {
            startDate = UtilDate.getshortFirst() + " 00:00:00";
        }

        if (!StringUtil.trimToEmpty(request.getParameter("endDate")).equals("")) {
            endDate = StringUtil.trimToEmpty(request.getParameter("endDate")) + " 23:59:59";
        } else {
            endDate = UtilDate.getshortLast() + " 23:59:59";
        }
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        DataGrid dataGrid = stockService.dataGrid(pageHelper, projectId, startDate, endDate, ugroup, keyword);

        return dataGrid;
    }

    @RequestMapping("/securi_toAddStockPage")
    public String oAddStockPage(HttpServletRequest request) {
        return "/app/materials/stock/addStock";
    }

    @RequestMapping("/securi_AddStock")
    @ResponseBody
    public Json addStock(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId")).equals("") ? "-1" : StringUtil.trimToEmpty(request.getParameter("projectId"));
        JSONArray addStockInfo = JSONArray.fromObject(request.getParameter("addStockInfo"));

        if (addStockInfo!=null && addStockInfo.size()>0) {
            for (int i=0; i<addStockInfo.size(); i++) {
                JSONObject o = addStockInfo.getJSONObject(i);
                Stock stock = new Stock();
                stock.setProjectId(projectId);
                stock.setCid(cid);
                stock.setUid(uid);
                stock.setCount(o.getString("count"));
                stock.setMaterialsId(o.getString("materialsId"));
                stock.setCreateTime(new Date());
                stock.setType("0"); // 普通材料
                stockService.addStock(stock);
            }
        }

        Json j = new Json();
        try {
            j.setMsg("新增成功！");
            j.setSuccess(true);
        } catch (Exception ex) {
            j.setMsg("新增失败");
            j.setSuccess(false);
        }
        return j;
    }

    @RequestMapping("/securi_toUpdateStockPage")
    public String toUpdateStockPage(HttpServletRequest request) {
        String stockId = request.getParameter("stockId");
        StockBean stockBean = stockService.getStockBean(stockId);
        request.setAttribute("stockBean", stockBean);
        return "/app/materials/stock/updateStock";
    }

    @RequestMapping("/securi_updateStock")
    @ResponseBody
    public Json updateStock(Stock updstock, HttpSession session) {
        Json j = new Json();
        try {
            Stock stock = stockService.detail(StringUtil.trimToEmpty(updstock.getId()));
            stock.setCount(updstock.getCount());
            stockService.update(stock);
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_delStock")
    @ResponseBody
    public Json delStock(String id) {
        Json json = new Json();
        try {
            stockService.delete(id);
            json.setSuccess(true);
            json.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg(e.getMessage());
        }
        return json;
    }
}
