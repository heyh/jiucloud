package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Feature;
import sy.model.po.Location;
import sy.model.po.UserDeviceRel;
import sy.pageModel.*;
import sy.service.DepartmentServiceI;
import sy.service.FeatureServiceI;
import sy.service.UserDeviceRelService;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/featureController")
public class FeatureController extends BaseController {
    @Autowired
    FeatureServiceI featureService;

    @Autowired
    private DepartmentServiceI departmentService;

    @RequestMapping("/FeatureList")
    public String FeatureList(HttpServletRequest request) {
        return "/app/feature/FeatureList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper ph, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));
        String itemCode = StringUtil.trimToEmpty(request.getParameter("itemCode"));
        DataGrid dataGrid = featureService.dataGrid(ph, cid, keyword, itemCode);

        return dataGrid;
    }

    @RequestMapping("/securi_addPage")
    public String addPage(HttpServletRequest request) throws IOException {

        return "/app/feature/addFeature";
    }

    @RequestMapping("/securi_add")
    @ResponseBody
    public Json add(Feature info, HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        try {
            Feature feature = featureService.addFeature(cid, info.getItemCode(), info.getMc(), info.getCount(), info.getDw());
            j.setSuccess(true);
            j.setMsg("添加成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_updatePage")
    public String updatePage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String id = request.getParameter("id");
        Feature feature = featureService.detail(id);
        if (feature == null) {
            response.getWriter().write("1");
            return null;
        }

        request.setAttribute("feature", feature);

        return "/app/feature/updateFeature";
    }

    @RequestMapping("/securi_update")
    @ResponseBody
    public Json update(Feature feature, HttpSession session) {
        Json j = new Json();
        try {
            Feature info = featureService.detail(StringUtil.trimToEmpty(feature.getId()));
            info.setMc(feature.getMc());
            info.setCount(feature.getCount());
            info.setDw(feature.getDw());
            featureService.update(info);
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_del")
    @ResponseBody
    public Json del(@RequestParam(value = "cid", required = false) String cid,
                                 @RequestParam(value = "uid", required = false) String uid,
                                 @RequestParam(value = "id", required = false) String id,
                                 HttpServletRequest request) throws Exception {
        Json j = new Json();
        featureService.delFeature(id);

        j.setSuccess(true);
        j.setMsg("操作成功！");
        return j;
    }

//    ========================================================================================================================
    @RequestMapping("/securi_getFeatures")
    @ResponseBody
    public JSONObject getFeatures(@RequestParam(value = "cid", required = true) String cid,
                                  @RequestParam(value = "uid", required = false) String uid,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  HttpServletRequest request) throws Exception {

        List<Feature> features = new ArrayList<Feature>();

        if (!StringUtil.trimToEmpty(uid).equals("")) {
            List<Integer> ugroup = departmentService.getAllParents(cid, Integer.parseInt(uid));
            ugroup.add(Integer.parseInt(uid));
            features = featureService.getFeatures(cid, ugroup, keyword);
        } else {
            features = featureService.getFeatures(cid, keyword);
        }

        return new WebResult().ok().set("features", features);
    }

    @RequestMapping("/securi_addFeature")
    @ResponseBody
    public JSONObject addFeature(@RequestParam(value = "cid", required = false) String cid,
                                 @RequestParam(value = "itemCode", required = false) String itemCode,
                                 @RequestParam(value = "mc", required = false) String mc,
                                 @RequestParam(value = "count", required = false) String count,
                                 @RequestParam(value = "dw", required = false) String dw,
                                 HttpServletRequest request) throws Exception {
        Feature feature = featureService.addFeature(cid, itemCode, mc, count, dw);
        return new WebResult().ok();
    }

    @RequestMapping("/securi_delFeature")
    @ResponseBody
    public JSONObject delFeature(@RequestParam(value = "cid", required = true) String cid,
                                 @RequestParam(value = "uid", required = false) String uid,
                                 @RequestParam(value = "id", required = true) String id,
                                 HttpServletRequest request) throws Exception {

        featureService.delFeature(id);

        List<Feature> features = new ArrayList<Feature>();
        if (!StringUtil.trimToEmpty(uid).equals("")) {
            List<Integer> ugroup = departmentService.getAllParents(cid, Integer.parseInt(uid));
            ugroup.add(Integer.parseInt(uid));
            features = featureService.getFeatures(cid, ugroup, "");
        } else {
            features = featureService.getFeatures(cid, "");
        }

        return new WebResult().ok().set("features", features);
    }
}
