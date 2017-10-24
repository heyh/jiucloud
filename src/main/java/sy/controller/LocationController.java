package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Feature;
import sy.model.po.Location;
import sy.service.DepartmentServiceI;
import sy.service.LocationServiceI;
import sy.util.StringUtil;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public JSONObject delFeature(@RequestParam(value = "cid", required = true) String cid,
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
