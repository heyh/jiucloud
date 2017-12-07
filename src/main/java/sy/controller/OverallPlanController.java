package sy.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Province;
import sy.model.po.OverallPlan;
import sy.model.po.OverallPlanDetails;
import sy.model.po.Project;
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

    @RequestMapping("/securi_overallPlanList")
    @ResponseBody
    public List<OverallPlanDetails> materialsTreeGridChild(HttpServletRequest request) {
        List<OverallPlanDetails> overallPlanMap = new ArrayList<OverallPlanDetails>();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String projectId = StringUtil.trimToEmpty(request.getParameter("projectId"));
        overallPlanMap = overallPlanService.getOverallPlanList(projectId);

        return overallPlanMap;
    }

    @RequestMapping("/securi_toAddOverallPlan")
    public String toAddPage(HttpServletRequest request) {

        request.setAttribute("proId", request.getParameter("proId"));
        try {
            request.setAttribute("proName", new String(request.getParameter("proName").getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

        OverallPlan overallPlan = new OverallPlan();
        overallPlan.setCid(cid);
        overallPlan.setUid(uid);
        overallPlan.setProjectId(projectId);
        overallPlan.setCreateTime(new Date());
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
