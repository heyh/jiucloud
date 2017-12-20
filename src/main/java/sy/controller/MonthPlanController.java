package sy.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.*;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.service.MonthPlanServiceI;
import sy.service.ProjectServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by heyh on 2017/12/16.
 */

@Controller
@RequestMapping("/monthPlanController")
public class MonthPlanController {

    @Autowired
    private MonthPlanServiceI monthPlanService;

    @Autowired
    private ProjectServiceI projectService;

    @RequestMapping("/MonthPlanList")
    public String MonthPlanList(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/materials/monthplan/MonthPlanList";
    }

    @RequestMapping("/securi_monthPlanList")
    @ResponseBody
    public List<MonthPlanBean> overallPlanList(HttpServletRequest request) {
        List<MonthPlanBean> monthPlanBeanList = new ArrayList<MonthPlanBean>();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
        startDate = startDate.equals("") ? UtilDate.getshortFirst() + " 00:00:00" : startDate + " 00:00:00";
        String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
        endDate = endDate.equals("") ? UtilDate.getshortLast() + " 23:59:59" : endDate + " 23:59:59";

        monthPlanBeanList = monthPlanService.getMonthPlanList(projectId, startDate, endDate);

        return monthPlanBeanList;
    }

    @RequestMapping("/securi_monthPlanDetailsList")
    @ResponseBody
    public List<MonthPlanDetailsBean> monthPlanDetailsList(HttpServletRequest request) {
        String monthPlanId = StringUtil.trimToEmpty(request.getParameter("monthPlanId"));
        List<MonthPlanDetailsBean> monthPlanDetailsBeanList = monthPlanService.getMonthPlanDetailsBeanList(monthPlanId);
        return monthPlanDetailsBeanList;
    }

    @RequestMapping("/securi_toAddMonthPlan")
    public String toAddPage(HttpServletRequest request) {

        if (!StringUtil.trimToEmpty(request.getParameter("proId")).equals("")) {
            request.setAttribute("proId", request.getParameter("proId"));

            String proName = projectService.findOneView(Integer.parseInt(StringUtil.trimToEmpty(request.getParameter("proId")))).getProName();
            request.setAttribute("proName", proName);
        }
        return "/app/materials/monthplan/addMonthPlan";
    }

    @RequestMapping("/securi_geneMonthPlan")
    @ResponseBody
    public Json geneMonthPlan(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        String projectId = request.getParameter("projectId");
        JSONArray monthPlanInfo = JSONArray.fromObject(request.getParameter("monthPlanInfo"));
        String currentApprovedUser = StringUtil.trimToEmpty(request.getParameter("currentApprovedUser"));

        MonthPlan monthPlan = new MonthPlan();
        monthPlan.setCid(cid);
        monthPlan.setUid(uid);
        monthPlan.setProjectId(projectId);
        monthPlan.setCreateTime(new Date());
        monthPlan.setNeedApproved("1");
        monthPlan.setApprovedUser(currentApprovedUser);
        monthPlan.setCurrentApprovedUser(currentApprovedUser);
        monthPlan.setApprovedOption("");
        monthPlanService.addMonthPlan(monthPlan);

        int monthPlanId = monthPlanService.getId(monthPlan);

        if (monthPlanInfo!=null && monthPlanInfo.size()>0) {
            for (int i=0; i<monthPlanInfo.size(); i++) {
                JSONObject o = monthPlanInfo.getJSONObject(i);
                MonthPlanDetails monthPlanDetails = new MonthPlanDetails();
                monthPlanDetails.setMonthPlanId(monthPlanId);
                monthPlanDetails.setCount(o.getString("count"));
                monthPlanDetails.setMaterialsId(o.getString("materialsId"));
                monthPlanDetails.setCreateTime(new Date());
                monthPlanService.addMonthPlanDetails(monthPlanDetails);
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
}
