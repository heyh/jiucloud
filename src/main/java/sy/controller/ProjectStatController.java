package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Project;
import sy.pageModel.SessionInfo;
import sy.service.AnalysisServiceI;
import sy.service.ProjectServiceI;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heyh on 15/12/1.
 */
@Controller
@RequestMapping("/projectStatController")
public class ProjectStatController {

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private AnalysisServiceI analysisServiceI;

    @RequestMapping("/showProjects4Leader")
    public String showprolist4leader(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/pro/showProjects4Leader";
    }

    @RequestMapping("/showProjectDetail4Leader")
    public String showProjectDetail4Leader(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        String id = request.getParameter("id");
        System.out.println(id);
        Project pro = projectService.findOneView(Integer.parseInt(id));
        pro.setCompId(sessionInfo.getCompName());
        request.setAttribute("pro", pro);

        List<Integer> ugroup = sessionInfo.getUgroup();
        String cid = sessionInfo.getCompid();
        String project_id = id; // 项目名称

        return "/app/pro/showProjectDetail4Leader";
    }

    @RequestMapping("/docApprove")
    public String docApprove(HttpServletRequest request) {
        return "/app/pro/docApprove";
    }

    @RequestMapping("/dataApprove")
    public String dataApprove(HttpServletRequest request) {
        return "/app/pro/dataApprove";
    }

    /**
     * 获取附件管理页面数据
     */
    @RequestMapping("/securi_feeStat")
    @ResponseBody
    public String feeStat(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        List<Integer> ugroup = sessionInfo.getUgroup();
        String cid = sessionInfo.getCompid();
        String id = request.getParameter("id");
        return analysisServiceI.getFeeStatList(Integer.parseInt(id), cid, ugroup);
    }
}
