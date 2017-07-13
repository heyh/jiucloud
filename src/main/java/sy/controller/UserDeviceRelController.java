package sy.controller;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.UserDeviceRel;
import sy.service.UserDeviceRelService;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/userDeviceRel")
public class UserDeviceRelController extends BaseController {
    @Autowired
    UserDeviceRelService userDeviceRelService;

    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public JSONObject addOrUpdateUserDeviceRel(@RequestParam(value = "registrationId", required = true) String registrationId,
                                      @RequestParam(value = "userId", required = false) String userId,
                                      HttpServletRequest request) throws Exception{
        UserDeviceRel userDeviceRel = new UserDeviceRel();
        userDeviceRel.setRegistrationId(registrationId);
        userDeviceRel.setUserId(userId);
        List<UserDeviceRel> userDeviceRelList = userDeviceRelService.selectUserDeviceRelByRegistrationID(userDeviceRel.getRegistrationId());
        int result = 0;
        if(null != userDeviceRelList && userDeviceRelList.size() > 0){
            userDeviceRelList = userDeviceRelService.selectUserDeviceRel(userDeviceRel);
            if((null == userDeviceRelList || userDeviceRelList.size() == 0) && !StringUtils.isEmpty(userId)){
                logger.debug("device rel exists,update now");
                result = userDeviceRelService.updateUserDeviceRel(userDeviceRel);
            }else{
                logger.debug("device rel exists,update ignored");
            }
        }else{
            logger.debug("device rel not exists,add now");
            userDeviceRelService.insertUserDeviceRel(userDeviceRel);
        }
        return new WebResult().ok().setMessage("ok");
    }
}
