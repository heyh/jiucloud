package sy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import sy.model.IMUser;
import sy.pageModel.Messages;
import sy.pageModel.SentMsg;
import sy.pageModel.SynIMUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * ****************************************************************
 * 文件名称 : SynchronizationController.java
 * 作 者 :   tcp
 * 创建时间 : 2014年12月23日 下午1:06:30
 * 文件描述 : 同步im注册用户
 * 版权声明 : 
 * 修改历史 : 2014年12月23日 1.00 初始版本
 *****************************************************************
 */
public class SynchronizationController {

	static final String GETIMUSERURL = "param.im.GETIMUSERURL";
	static final String GETIMTOKENURL = "param.im.GETIMTOKENURL";
	static final String SENTMSG = "param.im.SENTMSG";
	static final String CLIENTID = "sys.client_id";
	static final String CLIENTSECRET = "sys.client_secret";
	
	/**
	 * 
	 * 方法表述  或得配置中数据（url/key）
	 * @param key
	 * @param pcs 0 :param, 1 config
	 * @return
	 * String
	 */
	public static String getBuldKey(String key ,int pcs){
//		String pc = pcs == 0 ?"param":"config";
		return "";
	}
	
	
	/**
	 * 发送消息
	 * 方法表述
	 * @param sentMsg 服务号发送消息实体类 
	 * @param token
	 * @return
	 * Integer
	 */
	public static Integer sentMsg(SentMsg sentMsg,String token){
		String uri = getBuldKey(SENTMSG, 0 );//获得配置中配置的url
		HttpPost httppost = new HttpPost(uri);  
		Integer code = 0;
//		List<String> str = new ArrayList<String>();
//		JSONArray arr = getFriend(sentMsg.getFrom(),token); //获得好友
//		for (Object obj : arr) {
//			str.add((String)obj);
//		}
//		sentMsg.setTarget(str);//要发送的人
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httppost.addHeader("Authorization", "Bearer "+token); //认证token  
		    httppost.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONObject obj = (JSONObject) JSONObject.toJSON(sentMsg);
		    httppost.setEntity(new StringEntity(obj.toString(),"UTF-8"));     
		    HttpResponse response;  
		    response = httpclient.execute(httppost);  
		    //检验状态码，如果成功接收数据  
		    code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity(),"UTF-8");//返回json格式       
		        System.out.println(rev);
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
		}
		return code;
	}
	
	/**
	 * 上传多个im用户账号密码到环信
	 * 方法表述
	 * @param username 用户名
	 * @param password 密码
	 * @param token  
	 * void
	 */
	public static Integer saveIMUser(List<SynIMUser> users,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 );//获得配置中配置的url
		HttpPost httppost = new HttpPost(uri);  
		Integer code = 0;
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httppost.addHeader("Authorization", "Bearer "+token); //认证token  
		    httppost.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONArray obj = (JSONArray) JSONArray.toJSON(users);
		    httppost.setEntity(new StringEntity(obj.toString(),"UTF-8"));     
		    HttpResponse response;  
		    response = httpclient.execute(httppost);  
		    //检验状态码，如果成功接收数据  
		    code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity(),"UTF-8");//返回json格式       
		        System.out.println(rev);
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
		}
		return code;
	}
	
	
	/**
	 * 上传单个im用户账号密码到环信
	 * 方法表述
	 * @param username 用户名
	 * @param password 密码
	 * @param token  
	 * void
	 */
	public static Integer saveIMUser(String username,String password,String nickname,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 );//获得配置中配置的url
		HttpPost httppost = new HttpPost(uri);  
		Integer code = 0;
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httppost.addHeader("Authorization", "Bearer "+token); //认证token  
		    httppost.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONObject obj = new JSONObject();  
		    obj.put("username", username);  
		    obj.put("password", password);  
		    obj.put("nickname", nickname);  
		    httppost.setEntity(new StringEntity(obj.toString(),"UTF-8"));     
		    HttpResponse response;  
		    response = httpclient.execute(httppost);  
		    //检验状态码，如果成功接收数据  
		    code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity(),"UTF-8");//返回json格式       
		        System.out.println(rev);
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
		}
		return code;
	}
	
	/**
	 * 同步删除im用户账号到环信
	 * 方法表述
	 * @param username
	 * @param token
	 * void
	 */
	public static Integer deleteIMUser(String username,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 )+"/"+username;//获得配置中配置的url
		MyHttpDelete HttpDel = new MyHttpDelete(uri); 
		Integer code = 0;
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    HttpDel.addHeader("Authorization", "Bearer "+token); //认证token  
		    HttpDel.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONObject obj = new JSONObject();  
		    obj.put("username", username);  
		    HttpDel.setEntity(new StringEntity(obj.toString()));     
		    HttpResponse response;  
		    response = httpclient.execute(HttpDel);  
		    //检验状态码，如果成功接收数据  
		    code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity());//返回json格式：      
		        System.out.println(rev);
		    }else{
		    	System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HttpDel.releaseConnection();
		}
		return code;
	}
	
	/**
	 * 重置用户密码
	 * 方法表述
	 * @param username
	 * @param password
	 * @param token
	 * void
	 */
	public static Integer putIMUserPwd(String username,String newpassword,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 )+"/"+username+"/password";//获得配置中配置的url
		HttpPut httpPut = new HttpPut(uri);
		Integer code = 0;
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httpPut.addHeader("Authorization", "Bearer "+token); //认证token  
		    httpPut.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONObject obj = new JSONObject();  
		    obj.put("newpassword", newpassword);  
		    httpPut.setEntity(new StringEntity(obj.toString()));     
		    HttpResponse response;  
		    response = httpclient.execute(httpPut);  
		    //检验状态码，如果成功接收数据  
		    code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity());//返回json格式：      
		        System.out.println(rev);
		    }else{
		    	System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpPut.releaseConnection();
		}
		return code;
	}
	
	
	/**
	 * 修改昵称
	 * 方法表述
	 * @param username
	 * @param nickname
	 * @param token
	 * void
	 */
	public static Integer putIMNickname(String username,String nickname,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 )+"/"+username;//获得配置中配置的url
		HttpPut httpPut = new HttpPut(uri);
		Integer code = 0;
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httpPut.addHeader("Authorization", "Bearer "+token); //认证token  
		    httpPut.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    JSONObject obj = new JSONObject();  
		    obj.put("nickname", nickname);  
		    httpPut.setEntity(new StringEntity(obj.toString(),"UTF-8"));     
		    HttpResponse response ; 
		    response = httpclient.execute(httpPut);  
		    //检验状态码，如果成功接收数据  
		     code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity(),"UTF-8");//返回json格式：      
		        System.out.println(rev);
		    }else{
		    	System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpPut.releaseConnection();
		}
		return code;
	}
	/**
	 * 获得某个好友信息(感觉返回信息太少 data:该用户的好友)
	 * 方法表述
	 * @param username
	 * @param token
	 * @return
	 * Integer
	 */
	public static JSONArray getFriend(String username,String token){
		String uri = getBuldKey(GETIMUSERURL, 0 )+"/"+username+"/contacts/users";//获得配置中配置的url
		HttpGet httpGet = new HttpGet(uri);
		JSONArray arr = new JSONArray();
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httpGet.addHeader("Authorization", "Bearer "+token); //认证token  
		    httpGet.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式：  
		    HttpResponse response ; 
		    response = httpclient.execute(httpGet);  
		    //检验状态码，如果成功接收数据  
		    Integer code = response.getStatusLine().getStatusCode();  
		    if (code == 200) {   
		        String rev = EntityUtils.toString(response.getEntity(),"UTF-8");//返回json格式：      
		        System.out.println(rev);
		        String data = JSONObject.parseObject(rev).get("data").toString();
		        System.out.println(data);
		        arr = JSONArray.parseArray(data);
		        System.out.println("======");
		        System.out.println(arr);
		    }else{
		    	System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();
		}
		return arr;
	}
	
	/**
	 * 获得  token
	 * 方法表述
	 * @return token
	 * String
	 */
	public static String  getToken(){
		String uri = getBuldKey(GETIMTOKENURL, 0 );//获得配置中配置的url
		HttpPost httppost = new HttpPost(uri);  
		String token = "";
		try {  
		    HttpClient httpclient = HttpClients.createDefault(); 
		    //添加http头信息  
		    httppost.addHeader("Content-Type", "application/json");  
		    //http post的json数据格式
		    JSONObject obj = new JSONObject();  
		    obj.put("grant_type", "client_credentials");  
		    obj.put("client_id",getBuldKey(CLIENTID,1));  
		    obj.put("client_secret", getBuldKey(CLIENTSECRET,1));
		    httppost.setEntity(new StringEntity(obj.toString()));     
		    HttpResponse response;  
//		    response = httpclient.execute(httppost);  
//		    //检验状态码，如果成功接收数据  
//		    int code = response.getStatusLine().getStatusCode();  
//		    if (code == 200) {   
//		        String rev = EntityUtils.toString(response.getEntity());//返回json格式       
//		        System.out.println(rev);
//		        token = JSONObject.parseObject(rev).get("access_token").toString();
//		    }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
		}
		return token;
	}
	 
	
	 public static void main(String[] args) {
//		SynchronizationController.getToken();
//		SynchronizationController.saveIMUser("zflzfl","123456","YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
//		SynchronizationController.deleteIMUser("zflzfl", "YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
//		SynchronizationController.putIMUserPwd("zflzfl","147147","YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
//		SynchronizationController.putIMNickname("zflzfl","李师师","YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
		
//		List<SynIMUser> u = new ArrayList<SynIMUser>();
//		SynIMUser im1 = new SynIMUser("aaa1", "123456","小一");
//		SynIMUser im2 = new SynIMUser("aaa2", "123456","小二");
//		u.add(im1);u.add(im2);
//		SynchronizationController.saveIMUser(u,"YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
//		 SynchronizationController.getFriend("fw1baoweichu","YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw"); 
		 
		 SentMsg sentMsg = new SentMsg();
		 sentMsg.setFrom("fw1baoweichu");
		 List<String> str = new ArrayList<String>();
		 JSONArray arr = SynchronizationController.getFriend("fw1baoweichu","YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw"); 
		 
			for (Object obj : arr) {
				str.add((String)obj);
			}
		 sentMsg.setTarget(str);
		 Messages msg = new Messages();
		 msg.setMsg("测试1");
		 sentMsg.setMsg(msg);
		 
		 SynchronizationController.sentMsg(sentMsg, "YWMtqtxvnovzEeSJROX6DQD5ZwAAAUu004_gIrps6r4JZ7MmzSXbl-xf6Vuffmw");
	 }
 
	
}
