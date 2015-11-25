package sy.pageModel;

import java.util.Date;
/**
 * ****************************************************************
 * 文件名称 : AppSearch.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年11月20日 下午12:50:15
 * 文件描述 : 查询帮助类
 * 版权声明 : 
 * 修改历史 : 2014年11月20日 1.00 初始版本
 *****************************************************************
 */
public class AppSearch {
	
	private String pid;//根据id查询
	private String appcomp;//平台名
	private Date createdatetimeStart;//创建的开始时间
	private Date createdatetimeEnd;	//结束时间
	
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getAppcomp() {
		return appcomp;
	}
	public void setAppcomp(String appcomp) {
		this.appcomp = appcomp;
	}
	public Date getCreatedatetimeStart() {
		return createdatetimeStart;
	}
	public void setCreatedatetimeStart(Date createdatetimeStart) {
		this.createdatetimeStart = createdatetimeStart;
	}
	public Date getCreatedatetimeEnd() {
		return createdatetimeEnd;
	}
	public void setCreatedatetimeEnd(Date createdatetimeEnd) {
		this.createdatetimeEnd = createdatetimeEnd;
	}
	
	
}
