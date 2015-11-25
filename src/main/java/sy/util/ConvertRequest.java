package sy.util;

import javax.servlet.http.HttpServletRequest;

import syit.util.StreamUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author william.shao
 *
 */
public class ConvertRequest {
	
	/**
	 * 
	 * @param req
	 * @return jsonObject
	 */
	public JSONObject setReq(HttpServletRequest req){
		String element = "" ;
		try{
			element = StreamUtil.readText(req.getInputStream(), "utf8");
		}catch(Exception ex){}
		JSONObject jsonObject = JSONObject.parseObject(element) ;
		return jsonObject;
	} 

	
}
