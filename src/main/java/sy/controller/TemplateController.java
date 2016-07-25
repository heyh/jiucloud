package sy.controller;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.service.TemplateServiceI;
import sy.util.ConfigUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by heyh on 16/7/17.
 */

@Controller
@RequestMapping("/templateController")
public class TemplateController {

    @Autowired
    private TemplateServiceI templateService;

    @RequestMapping("/securi_getCostTemplate")
    @ResponseBody
    public Json getCostTemplate(HttpServletRequest request) {
        Json rtnJson = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String projectId = request.getParameter("projectId");
        JSONArray costTemplate = templateService.getCostTemplate(cid, projectId);
        rtnJson.setObj(costTemplate);
        rtnJson.setSuccess(true);
        rtnJson.setMsg("查询成功！");

        return rtnJson;
    }
}
