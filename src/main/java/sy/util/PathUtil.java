package sy.util;

import java.io.File;

public class PathUtil {
	
	
	public static String getRootPath() {
		/*String pathStr = PathUtil.class.getResource(
				"/sy/util/PathUtil.class").toString();
		String OS = System.getProperty("os.name").toLowerCase();
		//System.out.println(OS+"===="+pathStr);
		if (OS.indexOf("windows") > -1) {
			pathStr = pathStr.substring(0, pathStr
					.indexOf("PathUtil.class")).replaceAll("%20", " ").replaceAll("file:/","").replaceAll("file:","");	
		}
		else{
			pathStr = pathStr.substring(pathStr.indexOf("/"), pathStr
					.indexOf("PathUtil.class")).replaceAll("%20", " ");	
		}*/
		///return pathStr.substring(0,pathStr.indexOf("WEB-INF"));
		return "D:/workspacea/yuxt/WebContent/upload/chatsource/";
	}

	/*
	 * 获取classpath1
	 */
	public static String getClasspath(){
		String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))+"../../").replaceAll("file:/", "").replaceAll("%20", " ").trim();
		if(path.indexOf(":") != 1){
			path = File.separator + path;
		}
		return path;
	}

}
