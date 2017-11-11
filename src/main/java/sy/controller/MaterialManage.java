package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sy.model.po.Feature;
import sy.model.po.Location;
import sy.model.po.TFieldData;
import sy.pageModel.SessionInfo;
import sy.service.DepartmentServiceI;
import sy.service.FeatureServiceI;
import sy.service.FieldDataServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heyh on 2017/11/11.
 */
@Controller
@RequestMapping("/materialManageController")
public class MaterialManage {

    @Autowired
    private FieldDataServiceI fieldDataService;

    @Autowired
    private DepartmentServiceI departmentService;

    @RequestMapping("/OverallPlanList")
    public String fieldDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/materials/overallplan/OverallPlanList";
    }

    @RequestMapping("/securi_addPage")
    public String addPage(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();

        TFieldData tFieldData = fieldDataService.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        String maxNeedApproved = null;
        String maxApprovedUser = null;
        if (tFieldData != null) {
            maxProjectId = StringUtil.trimToEmpty(tFieldData.getProjectName());
            maxNeedApproved = StringUtil.trimToEmpty(tFieldData.getNeedApproved());
            if (!maxNeedApproved.equals("0")) {
                if (tFieldData.getApprovedUser() != null && !StringUtil.trimToEmpty(tFieldData.getApprovedUser()).equals("")) {
                    maxApprovedUser = tFieldData.getApprovedUser().split(",")[0];
                }
            }
        }

        List<String> firstLevelParentDepartments = departmentService.getFirstLevelParentDepartmentsByUid(cid, uid);
        String firstLevelParentDepartment = firstLevelParentDepartments.size() > 0 ? firstLevelParentDepartments.get(0) : "";

        List<Integer> allParents = departmentService.getAllParents(cid, Integer.parseInt(uid));
        allParents.add(Integer.parseInt(uid));
        request.setAttribute("maxProjectId", maxProjectId);
        request.setAttribute("maxNeedApproved", maxNeedApproved);
        request.setAttribute("maxApprovedUser", maxApprovedUser);
        request.setAttribute("firstLevelParentDepartment", firstLevelParentDepartment);

        return "/app/materials/overallplan/addOverallPlan";
    }
}
