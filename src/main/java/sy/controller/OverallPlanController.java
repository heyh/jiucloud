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
import sy.util.BeanUtils;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
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
        request.setAttribute("proName", request.getParameter("proName"));

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
}
