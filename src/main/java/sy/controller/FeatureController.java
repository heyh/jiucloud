package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Feature;
import sy.model.po.UserDeviceRel;
import sy.service.FeatureServiceI;
import sy.service.UserDeviceRelService;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/featureController")
public class FeatureController extends BaseController {
    @Autowired
    FeatureServiceI featureService;

    @RequestMapping("/securi_getFeatures")
    @ResponseBody
    public JSONObject getFeatures(@RequestParam(value = "cid", required = true) String cid,
                                  HttpServletRequest request) throws Exception {


        List<Feature> features = featureService.getFeatures(cid);

        return new WebResult().ok().set("features", features);
    }

    @RequestMapping("/securi_addFeature")
    @ResponseBody
    public JSONObject addFeature(@RequestParam(value = "cid", required = true) String cid,
                                 @RequestParam(value = "mc", required = true) String mc,
                                 @RequestParam(value = "dw", required = true) String dw,
                                 HttpServletRequest request) throws Exception {
        Feature feature = featureService.addFeature(cid, mc, dw);
        return new WebResult().ok();
    }

    @RequestMapping("/securi_delFeature")
    @ResponseBody
    public JSONObject delFeature(@RequestParam(value = "cid", required = true) String cid,
                                 @RequestParam(value = "id", required = true) String id,
                                 HttpServletRequest request) throws Exception {
        featureService.delFeature(id);
        List<Feature> features = featureService.getFeatures(cid);
        return new WebResult().ok().set("features", features);
    }
}
