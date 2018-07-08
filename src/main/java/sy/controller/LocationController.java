package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Item;
import sy.model.S_department;
import sy.model.po.Feature;
import sy.model.po.Location;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.DepartmentServiceI;
import sy.service.LocationServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UuidUtil;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/8/16.
 */
@Controller
@RequestMapping("locationController")
public class LocationController {

    @Autowired
    private LocationServiceI locationService;

    @Autowired
    private DepartmentServiceI departmentService;

    @RequestMapping("/LocationList")
    public String LocationList(HttpServletRequest request) {

        return "/app/location/LocationList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        List<Integer> ugroup = sessionInfo.getUgroup();

        DataGrid dataGrid = locationService.dataGrid(cid, ugroup);
        return dataGrid;
    }

    /**
     * 跳转新增页
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_addPage")
    public String addPage(HttpServletRequest request) throws IOException {

        return "/app/location/addLocation";
    }

    @RequestMapping("/securi_add")
    @ResponseBody
    public Json add(Location info, HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        try {
            Location location = locationService.addLocation(cid, uid, info.getMc());
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

    /**
     * 跳转修改页
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_updatePage")
    public String updatePage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String id = request.getParameter("id");
        Location location = locationService.detail(id);
        if (location == null) {
            response.getWriter().write("1");
            return null;
        }

        request.setAttribute("location", location);

        return "/app/location/updateLocation";
    }

    /**
     * 修改
     *
     * @param location
     * @param session
     * @return
     */
    @RequestMapping("/securi_update")
    @ResponseBody
    public Json update(Location location, HttpSession session) {
        Json j = new Json();
        try {
            Location info = locationService.detail(StringUtil.trimToEmpty(location.getId()));
            info.setMc(location.getMc());
            locationService.update(info);
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

//    ================================================================================================================


    @RequestMapping("/securi_getLocations")
    @ResponseBody
    public JSONObject getLocations(@RequestParam(value = "cid", required = true) String cid,
                                   @RequestParam(value = "uid", required = false) String uid,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   HttpServletRequest request) throws Exception {

        List<Location> locations;

        if (!StringUtil.trimToEmpty(uid).equals("")) {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            locations = locationService.getLocations(cid, ugroup, keyword);
        } else {
            locations = locationService.getLocations(cid, keyword);
        }

        return new WebResult().ok().set("locations", locations);
    }

    @RequestMapping("/securi_addLocation")
    @ResponseBody
    public JSONObject addLocation(@RequestParam(value = "cid", required = true) String cid,
                                  @RequestParam(value = "uid", required = false) String uid,
                                  @RequestParam(value = "mc", required = true) String mc,
                                  HttpServletRequest request) throws Exception {
        Location location = locationService.addLocation(cid, uid, mc);
        return new WebResult().ok();
    }

    @RequestMapping("/securi_delLocation")
    @ResponseBody
    public JSONObject delLocation(@RequestParam(value = "cid", required = false) String cid,
                                 @RequestParam(value = "uid", required = false) String uid,
                                 @RequestParam(value = "id", required = true) String id,
                                 HttpServletRequest request) throws Exception {
        locationService.delLocation(id);

        List<Location> locations;

        if (!StringUtil.trimToEmpty(uid).equals("")) {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            locations = locationService.getLocations(cid, ugroup, "");
        } else {
            locations = locationService.getLocations(cid,"");
        }

        return new WebResult().ok().set("locations", locations);
    }

}
