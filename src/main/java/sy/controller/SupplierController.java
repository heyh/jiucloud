package sy.controller;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Stock;
import sy.model.po.StockBean;
import sy.model.po.Supplier;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.SupplierServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by heyh on 2017/12/29.
 */

@Controller
@RequestMapping("/supplierController")
public class SupplierController {

    @Autowired
    private SupplierServiceI supplierService;

    @RequestMapping("/SupplierList")
    public String SupplierList(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<Supplier> supplierList = supplierService.supplierList(cid);
        List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        if (supplierList != null && supplierList.size()>0) {
            for (Supplier supplier : supplierList) {
                tmpMap = new HashMap<String, Object>();
                tmpMap.put("id", supplier.getId());
                tmpMap.put("text", supplier.getName());
                tmpList.add(tmpMap);
            }
        }
        request.setAttribute("supplierInfos", JSON.toJSONString(tmpList));
        return "/app/supplier/SupplierList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));
        String supplierId = StringUtil.trimToEmpty(request.getParameter("supplierId"));
        DataGrid dataGrid = supplierService.dataGrid(cid, pageHelper, keyword, supplierId);
        return dataGrid;
    }

    @RequestMapping("/securi_toAddPage")
    public String toAddPage(HttpServletRequest request) {
        return "/app/supplier/addSupplier";
    }

    @RequestMapping("/securi_add")
    @ResponseBody
    public Json add(Supplier supplier, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        supplier.setCid(cid);
        supplier.setCreateTime(new Date());
        supplierService.add(supplier);
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

    @RequestMapping("/securi_toUpdatePage")
    public String toUpdatePage(HttpServletRequest request) {
        String supplierId = request.getParameter("supplierId");
        Supplier supplier = supplierService.detail(supplierId);
        request.setAttribute("supplier", supplier);
        return "/app/supplier/updateSupplier";
    }

    @RequestMapping("/securi_update")
    @ResponseBody
    public Json update(Supplier updSupplier, HttpSession session) {
        Json j = new Json();

        Supplier supplier = supplierService.detail(StringUtil.trimToEmpty(updSupplier.getId()));
        supplier.setName(updSupplier.getName());
        supplier.setTel(updSupplier.getTel());
        supplier.setAddr(updSupplier.getAddr());
        supplier.setLinkman(updSupplier.getLinkman());
        supplier.setLinkphone(updSupplier.getLinkphone());
        supplier.setRemark(updSupplier.getRemark());

        try {
            supplierService.update(supplier);
            j.setSuccess(true);
            j.setMsg("修改成功！");
        } catch (Exception ex) {
            j.setMsg("新增失败");
            j.setSuccess(false);
        }
        return j;
    }

    @RequestMapping("/securi_del")
    @ResponseBody
    public Json del(String supplierId) {
        Json json = new Json();
        try {
            supplierService.delete(supplierId);
            json.setSuccess(true);
            json.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg(e.getMessage());
        }
        return json;
    }
}
