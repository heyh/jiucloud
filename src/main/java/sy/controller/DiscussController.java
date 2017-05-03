package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sy.model.po.Discuss;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.DiscussServiceI;
import sy.service.FieldDataServiceI;
import sy.service.ProjectServiceI;
import sy.service.UserServiceI;
import sy.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by heyh on 2017/4/6.
 */

@Controller
@RequestMapping("/discussController")
public class DiscussController {

    @Autowired
    private DiscussServiceI discussService;

    @Autowired
    private UserServiceI userService;

    @Autowired
    private FieldDataServiceI fieldDataService;

    @Autowired
    private ProjectServiceI projectService;

    @RequestMapping("/securi_discussShow")
    public String discussShow(@RequestParam(value = "discussType", required = true) String discussType,
                              @RequestParam(value = "discussId", required = true) String discussId,
                              HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();

        List<Discuss> discussList = discussService.getDiscussList(discussId);
        for (Discuss discuss : discussList) {
            User user = userService.getUser(discuss.getCreateUser());
            if (user != null) {
                String isMyself = discuss.getCreateUser().equals(uid) ? "1" : "0";
                discuss.setIsMyself(isMyself);
                String name = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();
                discuss.setCreateUser(name);
            }
        }

        String varTitle = "";
        String varName = "";
        Date varDate = new Date();
        if(discussType.equals("0")) {
            TFieldData tempFieldData = fieldDataService.detail(discussId);

            String projectId = tempFieldData.getProjectName();

            Project project = projectService.findOneView(Integer.parseInt(projectId));
            varTitle = project.getProName() + ":    " + tempFieldData.getDataName();

            User user = userService.getUser(StringUtil.trimToEmpty(tempFieldData.getId()));
            varName = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();

            varDate = tempFieldData.getCreatTime();
        } else if(discussType.equals("1")) {
            Project project = projectService.findOneView(Integer.parseInt(discussId));

            varTitle = project.getProName();

            User user = userService.getUser(StringUtil.trimToEmpty(project.getUid()));
            varName = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();
        }

        request.setAttribute("varTitle", varTitle);
        request.setAttribute("varName", varName);
        request.setAttribute("varDate", varDate);

        request.setAttribute("discussType", discussType);
        request.setAttribute("discussId", discussId);
        request.setAttribute("discussList", discussList);
        request.setAttribute("discussCount", discussList.size());
        return "/app/discuss/discussShow";
    }

    @RequestMapping("/securi_getDiscussList")
    @ResponseBody
    public JSONObject getDiscussList(@RequestParam(value = "discussId", required = true) String discussId,
                                     HttpServletRequest request, HttpServletResponse response) {
        List<Discuss> discussList = discussService.getDiscussList(discussId);
        for (Discuss discuss : discussList) {
            User user = userService.getUser(discuss.getCreateUser());
            if (user != null) {
                String name = !StringUtil.trimToEmpty(user.getRealname()).equals("") ? user.getRealname() : user.getUsername();
                discuss.setCreateUser(name);
            }
        }

        return new WebResult().ok().set("discussList", discussList);
    }

    @RequestMapping("/securi_addDiscuss")
    @ResponseBody
    public JSONObject addDiscuss(@RequestParam(value = "discussType", required = true) String discussType,
                                 @RequestParam(value = "discussId", required = true) String discussId,
                                 @RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "content", required = false) String content,
                                 @RequestParam(value = "createUser", required = false) String createUser,
                                 HttpServletRequest request, HttpServletResponse response) {
        Discuss discuss = new Discuss();
        discuss.setDiscussType(discussType);
        discuss.setDiscussId(discussId);
        discuss.setTitle(title);
        discuss.setContent(content);
        if (StringUtil.trimToEmpty(createUser).equals("")) {
            SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
            createUser = sessionInfo.getId();
        }
        discuss.setCreateUser(createUser);
        discussService.addDiscuss(discuss);

        return new WebResult().ok();
    }

    @RequestMapping("/securi_delDiscuss")
    @ResponseBody
    public JSONObject delDiscuss(@RequestParam(value = "id", required = true) String id,
                                 HttpServletRequest request, HttpServletResponse response) {

        discussService.delDiscuss(id);
        return new WebResult().ok();
    }

}
