package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.MonthPlanBean;
import sy.model.po.MonthPlanDetails;
import sy.model.po.MonthPlanDetailsBean;
import sy.pageModel.SessionInfo;
import sy.service.MonthPlanServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyh on 2017/12/16.
 */

@Controller
@RequestMapping("/monthPlanController")
public class MonthPlanController {

    @Autowired
    private MonthPlanServiceI monthPlanService;

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

        request.setAttribute("proId", request.getParameter("proId"));
        request.setAttribute("proName", request.getParameter("proName"));

        return "/app/materials/monthplan/addMonthPlan";
    }
}
