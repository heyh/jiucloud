package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Feature;
import sy.model.po.Materials;
import sy.pageModel.DataGrid;
import sy.pageModel.MaterialsTree;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.MaterialsServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyh on 2017/11/11.
 */
@Controller
@RequestMapping("/materialManageController")
public class MaterialManageController {

    @Autowired
    private MaterialsServiceI materialsService;

    @RequestMapping("/Materials")
    public String materials(HttpServletRequest request) {
        return "/app/materials/materials";
    }

    @RequestMapping("/securi_materialsTreeGrid")
    @ResponseBody
    public DataGrid materialsTreeGrid(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        if (pageHelper.getPage() == 0 && pageHelper.getRows() == 0) {
            pageHelper.setPage(1);
            pageHelper.setRows(300);
        }

        DataGrid dataGrid =  materialsService.dataGrid(pageHelper, keyword, pid);

        return dataGrid;
    }

    @RequestMapping("/securi_materialsTreeGridChild")
    @ResponseBody
    public List<MaterialsTree> materialsTreeGridChild(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        DataGrid dataGrid =  materialsService.dataGrid(pageHelper, keyword, pid);

        return dataGrid.getRows();
    }

    @RequestMapping("/securi_addNode")
    @ResponseBody
    public JSONObject addNode(@RequestParam(value = "pid", required = true) String pid,
                               @RequestParam(value = "mc", required = true) String mc,
                               @RequestParam(value = "specifications", required = true) String specifications,
                               @RequestParam(value = "dw", required = true) String dw,
                               HttpServletRequest request) throws Exception {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        Materials materials = materialsService.addNode(cid, pid, mc, specifications, dw);
        return new WebResult().ok();
    }

    @RequestMapping("/securi_delNode")
    @ResponseBody
    public JSONObject delNode(@RequestParam(value = "id", required = true) String id,
                              HttpServletRequest request) throws Exception {
        materialsService.delNode(id);
        return new WebResult().ok();
    }

    @RequestMapping("/securi_getMaterialsById")
    @ResponseBody
    public JSONObject getMaterialsById(@RequestParam(value = "id", required = true) String id,
                                  HttpServletRequest request) throws Exception {

        Materials materials = materialsService.findById(Integer.parseInt(id));

        return new WebResult().ok().set("materials", materials);
    }

    @RequestMapping("/securi_editNode")
    @ResponseBody
    public JSONObject editNode(@RequestParam(value = "id", required = true) String id,
                              @RequestParam(value = "mc", required = true) String mc,
                              @RequestParam(value = "specifications", required = true) String specifications,
                              @RequestParam(value = "dw", required = true) String dw,
                              HttpServletRequest request) throws Exception {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        Materials materials = materialsService.findById(Integer.parseInt(id));
        materials.setMc(mc);
        materials.setSpecifications(specifications);
        materials.setDw(dw);
        materialsService.updateNode(materials);
        return new WebResult().ok();
    }
}
