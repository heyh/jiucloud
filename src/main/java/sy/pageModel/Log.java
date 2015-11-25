package sy.pageModel;

import java.util.Date;

/**
 * 日志类
 * @author tcp
 *
 */
public class Log implements java.io.Serializable{

	
	private int id;					//日志编号
	private String title;			//标题
	private String content;			//内容
	private Date ctime;				//创建时间
	private String attachment;		//附件路径
	private String uid;				//用户名
	private String attachmentname;	//文件名
	private String realname;		//姓名
	private int department;		//部门
	
	
	
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}
	public String getAttachmentname() {
		return attachmentname;
	}
	public void setAttachmentname(String attachmentname) {
		this.attachmentname = attachmentname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
