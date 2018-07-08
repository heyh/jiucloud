package sy.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import sy.pageModel.SessionInfo;
import sy.service.UserServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * Created by heyh on 2018/6/14.
 */
public class SessionListener implements HttpSessionListener {

    @Autowired
    private UserServiceI userService;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //在session销毁的时候 把loginMap中保存的键值对清除
        SessionInfo sessionInfo = (SessionInfo) httpSessionEvent.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String userId = sessionInfo.getId();
        if (!userId.equals("")) {
            userService.updateLoginStatus(userId, "");
        }
    }
}
