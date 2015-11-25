package sy.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.Json;
import sy.util.ConvertRequest;
import sy.util.Param;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * 接口包
 * 
 * @author william.shao
 * 
 */
@Controller
@RequestMapping("/api")
public class ApiDispachController {

	// 控制器
	public static final String CONTRROLER = "Controller";
	public static final String APIPACKET = "sy.controller.api";

	/**
	 * 接口映射方法
	 * 
	 * @param req
	 * @param res
	 */
	@RequestMapping("/{packet}/{classes}/{method}")
	@ResponseBody
	public Json methods(@PathVariable("packet") String packet,
			@PathVariable("classes") String classes ,
			@PathVariable("method") String method, HttpServletRequest req, HttpServletResponse res) {
	    
		Json json = new Json();
		JSONObject jsonObject = new ConvertRequest().setReq(req);
		//@PathVariable("classes") String classes;
		Class beanClass = null;
		Object object = null;
		try {
			beanClass = Class.forName(APIPACKET + "." + packet + "." + classes
					+ CONTRROLER);
			object = beanClass.newInstance();
		} catch (Exception ex) {
			json.setSuccess(false);
			json.setCode(1);
			json.setMsg("reuquest msg error ...");
			return json;
		}

		Method[] ms = beanClass.getMethods();
		for (Method m : ms) {
			String mName = m.getName();
			if (StringUtils.equals(method, mName)) {
				Class<?>[] pt = m.getParameterTypes();
				Object[] objectParam = new Object[pt.length];
				Annotation[][] an = m.getParameterAnnotations();
				if (an.length > 0) {
					for (int k = 0; k < an.length; k++) {
						for (int j = 0; j < an[k].length; j++) {
							Param t = (Param) an[k][j];
							if (pt[k].getName().equals("java.lang.String")) {
								objectParam[k] = null; 
								try{
									objectParam[k] = jsonObject.getString(t.name());
								}catch(Exception ex){}
							}
							if (pt[k].getName().equals("java.lang.Integer")) {
								objectParam[k] = null;
								try{
									objectParam[k] = Integer.parseInt(jsonObject.getString(t.name()));
								}catch(Exception ex){}
							}
						}
					}
					int i = 0;
					for(Class cl : pt){
						if (pt[i].getName().equals("javax.servlet.http.HttpServletRequest")) {
							objectParam[i] = req;
						}
						if (pt[i].getName().equals("javax.servlet.http.HttpServletResponse")) {
							objectParam[i] = res;
						}
						i++;
					}
				}
				
				try {
					json = (Json) m.invoke(object, objectParam);
					return json;
				} catch (Exception e) {
					json.setSuccess(false);
					json.setMsg("invoke request method error");
					json.setCode(2);
					return json;

				}
			}
		}
		return json;
	}
}
