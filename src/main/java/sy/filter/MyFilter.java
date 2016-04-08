package sy.filter;

import org.java_websocket.WebSocketImpl;
import sy.plugin.websocketOnline.OnlineManageServer;

import javax.servlet.*;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by heyh on 16/4/8.
 */
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.startWebsocketOnline();
    }

    private void startWebsocketOnline() {
        WebSocketImpl.DEBUG = false;
        int port = 8889; //端口
        OnlineManageServer onlineManageServer;
        try {
            System.out.println("startup onlineManageServer...");
            onlineManageServer = new OnlineManageServer(port);
            onlineManageServer.start();
            System.out.println("startup onlineManageServer success...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
