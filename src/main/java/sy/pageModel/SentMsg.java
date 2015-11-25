package sy.pageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ****************************************************************
 * 文件名称 : SentMsg.java
 * 作 者 :   Administrator
 * 创建时间 : 2015年1月8日 上午9:02:42
 * 文件描述 : 服务号发送消息实体类
 * 版权声明 : 
 * 修改历史 : 2015年1月8日 1.00 初始版本
 *****************************************************************
 */
public class SentMsg {

	private String target_type = "users"; // users 给用户发消息, chatgroups 给群发消息
	private List<String> target = new ArrayList<String>();//即使只有一个用户,也要用数组 ['u1'], 给用户发送时数组元素是用户名,给群组发送时   数组元素是groupid
	private String from;//表示这个消息是谁发出来的, 可以没有这个属性, 那么就会显示是admin, 如果有的话, 则会显示是这个用户发出的  
	private Messages msg;//消息
	
	public String getTarget_type() {
		return target_type;
	}
	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
	public List<String> getTarget() {
		return target;
	}
	public void setTarget(List<String> target) {
		this.target = target;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Messages getMsg() {
		return msg;
	}
	public void setMsg(Messages msg) {
		this.msg = msg;
	}
	

//    "ext" : { //扩展属性, 由app自己定义.可以没有这个字段，但是如果有，值不能是“ext:null“这种形式，否则出错
//        "attr1" : "v1",
//        "attr2" : "v2"
}
