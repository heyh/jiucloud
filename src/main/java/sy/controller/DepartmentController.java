package sy.controller;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.service.DepartmentServiceI;
import sy.util.ConfigUtil;
import sy.util.Node;
import sy.util.NodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by heyh on 2016/12/4.
 */

@Controller
@RequestMapping("/departmentController")
public class DepartmentController {

    @Autowired
    private DepartmentServiceI departmentService;

    @RequestMapping("/getDepartmentTree")
    @ResponseBody
    public Json getDepartmentTree(HttpServletRequest request, HttpSession session){
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<Object[]> departmentList = departmentService.getAllDepartmentList(cid);
        Json json = new Json();
        json.setObj(JSONArray.fromObject(departmentList));
        json.setSuccess(true);
        return json;
    }
}
