package sy.interceptors;

import sy.util.StringUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * Created by heyh on 2018/6/14.
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //在session销毁的时候 把loginMap中保存的键值对清除
        String userId = StringUtil.trimToEmpty(httpSessionEvent.getSession().getAttribute("PC-userId"));
        if (!userId.equals("")) {
            Map<String, String> loginMap = (Map<String, String>) httpSessionEvent.getSession().getServletContext().getAttribute("loginMap");
            loginMap.remove("PC-" + userId);
            httpSessionEvent.getSession().getServletContext().setAttribute("loginMap", loginMap);
            System.out.println(userId + " 用户注销！");
        }
    }
}
