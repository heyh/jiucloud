package sy.util;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;

/**
 * Date: 13-7-25
 * Time: 下午4:17
 */
public class GetRealPath {

    ServletContext sc = null;

    public GetRealPath(ServletContext sc) {
        this.sc = sc;
    }

    /**
     * 通过上下文来取工程路径
     *
     * @return
     * @throws Exception
     */
    private String getAbsolutePathByContext() throws Exception {
        String webPath = sc.getRealPath("/");
        webPath = webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");
        webPath = webPath.replaceAll("%20", " ");

        if (webPath.matches("^[a-zA-Z]:.*?$")) {

        } else {
            webPath = "/" + webPath;
        }

        webPath += "/";
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");
        return webPath;

    }

    /**
     * 通过类路径来取工程路径
     *
     * @return
     * @throws Exception
     */
    private String getAbsolutePathByClass() throws Exception {
        String webPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "");
        webPath = webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");
        webPath = webPath.replaceAll("%20", " ");

        if (webPath.matches("^[a-zA-Z]:.*?$")) {

        } else {
            webPath = "/" + webPath;
        }

        webPath += "/";
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");

        return webPath;
    }

    private String getAbsolutePathByResource() throws Exception {
        URL url = sc.getResource("/");
        String path = new File(url.toURI()).getAbsolutePath();
        if (!path.endsWith("\\") && !path.endsWith("/")) {
            path += File.separator;
        }
        return path;
    }

    public String getRealPath(){
        String webPath = null;
        try {
            webPath = getAbsolutePathByContext();
        } catch (Exception e) {
        }

        // 在weblogic 11g 上可能无法从上下文取到工程物理路径，所以改为下面的
        if (webPath == null) {
            try {
                webPath = getAbsolutePathByClass();
            } catch (Exception e) {
            }
        }

        if (webPath == null) {
            try {
                webPath = getAbsolutePathByResource();
            } catch (Exception e) {
            }
        }
        return webPath;
    }
}
