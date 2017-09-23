package sy.controller;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Clockingin;
import sy.model.Item;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.ClockinginServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by heyh on 2017/9/17.
 */

@Controller
@RequestMapping("/clockinginController")
public class ClockinginController extends BaseController  {

    @Autowired
    private ClockinginServiceI clockinginService;

    @RequestMapping("/ClockinginList")
    public String ClockinginList(HttpServletRequest request) {
        return "/app/clockingin/ClockinginList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        String keyword = StringUtil.trimToEmpty(request.getParameter("projectId"));
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<Integer> ugroup = sessionInfo.getUgroup();
        Clockingin clockingin = new Clockingin();
        clockingin.setCid(cid);

        String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
        startDate = startDate.equals("") ? UtilDate.getshortFirst() + " 00:00:00" : startDate + " 00:00:00";
        clockingin.setStartDate(startDate);

        String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
        endDate = endDate.equals("") ? UtilDate.getshortLast() + " 23:59:59" : endDate + " 23:59:59";
        clockingin.setEndDate(endDate);

        String searchUid = StringUtil.trimToEmpty(request.getParameter("searchUid"));
        clockingin.setUid(searchUid);

        String clockinginFlag = StringUtil.trimToEmpty(request.getParameter("clockinginFlag"));
        clockingin.setClockinginFlag(clockinginFlag);

        DataGrid dataGrid = clockinginService.dataGrid(cid, ugroup, clockingin, pageHelper);
        return dataGrid;
    }

    @RequestMapping("/securi_updateClockinginPage")
    public String updateClockinginPage(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Clockingin clockingin = clockinginService.getClockingin(id);
        request.setAttribute("clockingin", clockingin);
        return "/app/clockingin/updateClockingin";
    }

    @RequestMapping("/securi_updateClockingin")
    @ResponseBody
    public Json updateClockingin(Clockingin clockingin, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        Json j = new Json();
        try {
            Clockingin c = clockinginService.getClockingin(clockingin.getId());
            c.setApproveState(clockingin.getApproveState());
            c.setApproveDesc(clockingin.getApproveDesc());
            c.setApproveTime(new Date());
            c.setApproveUser(sessionInfo.getUsername());
            clockinginService.update(c);

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

    @RequestMapping("/securi_delClockingin")
    @ResponseBody
    public Json delClockingin(int id) {
        Json j = new Json();
        try {
            clockinginService.delete(id);
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }
}
