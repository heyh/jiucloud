package sy.pageModel;
/**
 * ****************************************************************
 * 文件名称 : Messages.java
 * 作 者 :   Administrator
 * 创建时间 : 2015年1月8日 上午9:07:53
 * 文件描述 : 服务号发送消息类
 * 版权声明 : 
 * 修改历史 : 2015年1月8日 1.00 初始版本
 *****************************************************************
 */
public class Messages {
	private String type = "txt";
	private Object msg;////消息内容，
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	
}
