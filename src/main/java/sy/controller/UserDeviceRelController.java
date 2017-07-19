package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.UserDeviceRel;
import sy.service.UserDeviceRelService;
import sy.util.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/userDeviceRelController")
public class UserDeviceRelController extends BaseController {
    @Autowired
    UserDeviceRelService userDeviceRelService;

    @RequestMapping("/securi_addOrUpdate")
    @ResponseBody
    public JSONObject addOrUpdateUserDeviceRel(@RequestParam(value = "registrationId", required = true) String registrationId,
                                               @RequestParam(value = "userId", required = false) String userId,
                                               HttpServletRequest request) throws Exception {
        UserDeviceRel userDeviceRel = new UserDeviceRel();
        userDeviceRel.setRegistrationId(registrationId);
        userDeviceRel.setUserId(userId);
        userDeviceRel.setIsPush("1");

        int result = 0;
        // 1、根据RegistrationID 查看同一机器是否已有 设备用户关系
        List<UserDeviceRel> userDeviceRelList4RegistrationID = userDeviceRelService.selectUserDeviceRelByRegistrationID(registrationId);
        if (null != userDeviceRelList4RegistrationID && userDeviceRelList4RegistrationID.size()>0) {
            String oldRegistrationId = userDeviceRelList4RegistrationID.get(0).getRegistrationId();
            String oldUserId = userDeviceRelList4RegistrationID.get(0).getUserId();
            if (oldRegistrationId.equals(registrationId) && oldUserId.equals(userId)) {
                logger.debug("设备、用户关系已存在!");
            } else {
                logger.debug("设备、用户关系已存在 - 不同用户用同一部手机登录，user_id发生变化!");
                result = userDeviceRelService.updateUserDeviceRelUserId(userDeviceRel);
            }
        } else {
            // 2、根据userId 查看同一机器是否已有 设备用户关系
            List<UserDeviceRel> userDeviceRel4UserId = userDeviceRelService.selectUserDeviceRelByUserId(userId);
            if (null != userDeviceRel4UserId && userDeviceRel4UserId.size()>0) {
                String oldRegistrationId = userDeviceRel4UserId.get(0).getRegistrationId();
                String oldUserId = userDeviceRel4UserId.get(0).getUserId();
                if (oldRegistrationId.equals(registrationId) && oldUserId.equals(userId)) {
                    logger.debug("设备、用户关系已存在!");
                } else {
                    logger.debug("设备、用户关系已存在 - 相同用户用多部手机登录，registrationId发生变化!");
                    result = userDeviceRelService.updateUserDeviceRelRegistrationID(userDeviceRel);
                }
            } else {
                logger.debug("device rel not exists,add now");
                userDeviceRelService.insertUserDeviceRel(userDeviceRel);
            }
        }

        return new WebResult().ok().setMessage("ok");
    }
}
