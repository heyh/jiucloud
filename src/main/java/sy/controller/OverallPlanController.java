package sy.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Province;
import sy.model.po.*;
import sy.pageModel.*;
import sy.service.OverallPlanServiceI;
import sy.service.ProjectServiceI;
import sy.util.BeanUtils;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/4.
 */

@Controller
@RequestMapping("/overallPlanController")
public class OverallPlanController {

    @Autowired
    private OverallPlanServiceI overallPlanService;

    @Autowired
    private ProjectServiceI projectService;

    @RequestMapping("/OverallPlanList")
    public String OverallPlanList(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/materials/overallplan/OverallPlanList";
    }

    @RequestMapping("/securi_overallPlanList")
    @ResponseBody
    public List<OverallPlanBean> overallPlanList(HttpServletRequest request) {
        List<OverallPlanBean> overallPlanBeanList = new ArrayList<OverallPlanBean>();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        overallPlanBeanList = overallPlanService.getOverallPlanList(projectId);

        return overallPlanBeanList;
    }

    @RequestMapping("/securi_overallPlanDetailsList")
    @ResponseBody
    public List<OverallPlanDetailsBean> overallPlanDetailsList(HttpServletRequest request) {
        List<OverallPlanDetailsBean> overallPlanDetailsBeanList = new ArrayList<OverallPlanDetailsBean>();
        String overallPlanId = StringUtil.trimToEmpty(request.getParameter("overallPlanId"));
        overallPlanDetailsBeanList = overallPlanService.getOverallPlanDetailsList(overallPlanId);

        return overallPlanDetailsBeanList;
    }

    @RequestMapping("/securi_toAddOverallPlan")
    public String toAddPage(HttpServletRequest request) {

        request.setAttribute("proId", request.getParameter("proId"));

        String proName = projectService.findOneView(Integer.parseInt(StringUtil.trimToEmpty(request.getParameter("proId")))).getProName();
        request.setAttribute("proName", proName);

        return "/app/materials/overallplan/addOverallPlan";
    }

    @RequestMapping("/securi_geneOverallPlan")
    @ResponseBody
    public Json geneOverallPlan(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        String projectId = request.getParameter("projectId");
        JSONArray overallPlanInfo = JSONArray.fromObject(request.getParameter("overallPlanInfo"));
        String currentApprovedUser = StringUtil.trimToEmpty(request.getParameter("currentApprovedUser"));

        OverallPlan overallPlan = new OverallPlan();
        overallPlan.setCid(cid);
        overallPlan.setUid(uid);
        overallPlan.setProjectId(projectId);
        overallPlan.setCreateTime(new Date());
        overallPlan.setNeedApproved("1");
        overallPlan.setApprovedUser(currentApprovedUser);
        overallPlan.setCurrentApprovedUser(currentApprovedUser);
        overallPlan.setApprovedOption("");
        overallPlanService.addOverallPlan(overallPlan);

        int overallPlanId = overallPlanService.getId(overallPlan);

        if (overallPlanInfo!=null && overallPlanInfo.size()>0) {
            for (int i=0; i<overallPlanInfo.size(); i++) {
                JSONObject o = overallPlanInfo.getJSONObject(i);
                OverallPlanDetails overallPlanDetails = new OverallPlanDetails();
                overallPlanDetails.setOverallPlanId(overallPlanId);
                overallPlanDetails.setCount(o.getString("count"));
                overallPlanDetails.setMaterialsId(o.getString("materialsId"));
                overallPlanDetails.setCreateTime(new Date());
                overallPlanService.addOverallPlanDetails(overallPlanDetails);
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

    @RequestMapping("/securi_overallPlanDetailsAll")
    @ResponseBody
    public List<OverallPlanDetailsBean> overallPlanDetailsAll(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<OverallPlanDetailsBean> overallPlanDetailsBeanList = new ArrayList<OverallPlanDetailsBean>();
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        overallPlanDetailsBeanList = overallPlanService.overallPlanDetailsAll(cid, projectId);

        return overallPlanDetailsBeanList;
    }

    @RequestMapping("/securi_delOverallPlan")
    @ResponseBody
    public Json delOverallPlan(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        String overallPlanId = StringUtil.trimToEmpty(request.getParameter("overallPlanId"));

        overallPlanService.delOverallPlan(overallPlanId);

        Json j = new Json();
        try {
            j.setMsg("删除成功！");
            j.setSuccess(true);
        } catch (Exception ex) {
            j.setMsg("删除失败");
            j.setSuccess(false);
        }
        return j;
    }

    @RequestMapping("/securi_delOverallPlanDetails")
    @ResponseBody
    public Json delOverallPlanDetails(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        String overallPlanDetailsId = StringUtil.trimToEmpty(request.getParameter("overallPlanDetailsId"));

        overallPlanService.delOverallPlanDetails(overallPlanDetailsId);

        Json j = new Json();
        try {
            j.setMsg("删除成功！");
            j.setSuccess(true);
        } catch (Exception ex) {
            j.setMsg("删除失败");
            j.setSuccess(false);
        }
        return j;
    }

    @RequestMapping("/ApproveOverallPlan")
    public String ApproveOverallPlanList(HttpServletRequest request) {
        request.setAttribute("first", UtilDate.getshortFirst());
        request.setAttribute("last", UtilDate.getshortLast());
        return "/app/materials/approve/approveOverallPlan";
    }

    @RequestMapping("/securi_getApproveOverallPlanList")
    @ResponseBody
    public List<OverallPlanBean> getApproveOverallPlanList(HttpServletRequest request) {
        List<OverallPlanBean> overallPlanBeanList = new ArrayList<OverallPlanBean>();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();

        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));

        String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
        startDate = startDate.equals("") ? UtilDate.getshortFirst() + " 00:00:00" : startDate + " 00:00:00";

        String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
        endDate = endDate.equals("") ? UtilDate.getshortLast() + " 23:59:59" : endDate + " 23:59:59";

        overallPlanBeanList = overallPlanService.getApproveOverallPlanList(cid, uid, projectId, startDate, endDate);

        return overallPlanBeanList;
    }

    @RequestMapping("/securi_approveOverallPlan")
    @ResponseBody
    public Json approveOverallPlan(Integer overallplanId, String approvedState, String approvedOption, String currentApprovedUser, HttpServletResponse response, HttpServletRequest request) {
        Json j = new Json();
        if (overallplanId != null) {
            overallPlanService.approveOverallPlan(overallplanId, approvedState, approvedOption, currentApprovedUser);
        }
        j.setMsg("审批成功！");
        j.setSuccess(true);
        return j;
    }
}
