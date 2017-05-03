package sy.controller;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Item;
import sy.model.ParamTrans;
import sy.model.S_department;
import sy.model.po.TFieldData;
import sy.pageModel.*;
import sy.service.FieldDataServiceI;
import sy.service.ItemServiceI;
import sy.service.ProjectServiceI;
import sy.util.ConfigUtil;
import sy.util.ObjectExcelRead;
import sy.util.StringUtil;
import sy.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/2/18.
 */

@Controller
@RequestMapping("/itemController")
public class ItemController {

    @Autowired
    private ItemServiceI itemService;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private FieldDataServiceI fieldDataService;

    @RequestMapping("/SectionList")
    public String SectionList(HttpServletRequest request) {
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        request.setAttribute("projectId", projectId);
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        List<Map<String, Object>> itemList = itemService.getSelectItems(cid, projectId);
        if (itemList.size()<=0) {
            List<Item> defaultItemList = itemService.getDefaultItems();
            for (Item defaultItem : defaultItemList) {
                defaultItem.setCid(cid);
                defaultItem.setProjectId(projectId);
                defaultItem.setOperator(uid);
                itemService.add(defaultItem);
            }
            itemList = itemService.getSelectItems(cid, projectId);
        }

        return "/app/section/SectionList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        String keyword = StringUtil.trimToEmpty(request.getParameter("projectId"));
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();

        DataGrid dataGrid = itemService.dataGrid(cid, keyword, pageHelper);
        return dataGrid;
    }

    /**
     * 跳转新增页
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_addSectionPage")
    public String addParamTransPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());

        String compId = sessionInfo.getCompid();
        List<S_department> departmentList  = sessionInfo.getDepartmentIds();
        List<Integer> departmentIds = new ArrayList<Integer>();
        if (departmentList != null && departmentList.size() > 0) {
            for (S_department department : departmentList) {
                departmentIds.add(department.getId());
            }
        }
        List<Map<String, Object>> projects = projectService.getProjects(compId, departmentIds);
        request.setAttribute("projects", projects);
        return "/app/section/addSection";
    }

    @RequestMapping("/securi_addSection")
    @ResponseBody
    public Json addSection(Item item, HttpSession session) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        item.setCid(cid);
        item.setOperator(uid);
        item.setValue(UuidUtil.get32UUID());
        try {
            itemService.add(item);
            j.setSuccess(true);
            j.setMsg("添加成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 跳转修改页
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_updateSectionPage")
    public String updateSectionPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String id = request.getParameter("id");
        Item item = itemService.detail(id);
        if (item == null) {
            response.getWriter().write("1");
            return null;
        }

        request.setAttribute("item", item);

        return "/app/section/updateSection";
    }

    /**
     * 修改
     *
     * @param item
     * @param session
     * @return
     */
    @RequestMapping("/securi_updateSection")
    @ResponseBody
    public Json updateParamTrans(Item item, HttpSession session) {
        Json j = new Json();
        try {
            Item info = itemService.detail(StringUtil.trimToEmpty(item.getId()));
            info.setName(item.getName());
            info.setSupInfo(item.getSupInfo());
            itemService.update(info);
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

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/securi_delSection")
    @ResponseBody
    public Json delSection(String id) {
        Json j = new Json();
        try {
            itemService.delete(id);
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     *
     * @param request
     * @return
     */
    @RequestMapping("/securi_bunchdelete")
    @ResponseBody
    public Json bunchdelete(HttpServletRequest request) {
        Json j = new Json();
        String ids = request.getParameter("ids");
        try {
            String[] id = ids.split(",");
            for (String tem : id) {
                itemService.delete(tem);
            }
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_getSelectItems")
    @ResponseBody
    public Json getSelectItems(String projectId, HttpSession session) {
        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        List<Map<String, Object>> itemList = itemService.getSelectItems(cid, projectId);
        if (itemList.size()<=0) {
            List<Item> defaultItemList = itemService.getDefaultItems();
            for (Item defaultItem : defaultItemList) {
                defaultItem.setCid(cid);
                defaultItem.setProjectId(projectId);
                defaultItem.setOperator(uid);
                itemService.add(defaultItem);
            }
            itemList = itemService.getSelectItems(cid, projectId);
        }

        String section = null;
        TFieldData tFieldData = fieldDataService.getFieldByMaxId(cid,uid, projectId);
        if (tFieldData != null) {
            section = tFieldData.getSection();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("itemList", itemList);
        data.put("section", section);

//        json.setObj(itemList);
        json.setObj(data);
        json.setSuccess(true);
        return json;
    }

    @RequestMapping("/securi_getSupInfo")
    @ResponseBody
    public Json getSupInfo(@RequestParam(value = "projectId", required = true) String projectId,
                           @RequestParam(value = "section", required = true) String section,
                           HttpServletRequest request, HttpServletResponse response) {
        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<String> supInfos = itemService.getSupInfos(cid, projectId, section);
        json.setObj(supInfos);
        json.setSuccess(true);
        return json;
    }
}
