package sy.pageModel;

import java.util.Date;

/**
 * ****************************************************************
 * 文件名称 : TApplication.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月22日 下午2:40:37
 * 文件描述 : 服务号通知类
 * 版权声明 : 
 * 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
public class Inform implements java.io.Serializable{

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	private String title;//标题
	
	private String content;//信息内容
	
	private String picPath;//缩略图路径
	
	private String htmlPath;//html路径
	
	private String fwName;//服务号名称
	
	private String isoktime;//推送 时间

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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public String getIsoktime() {
		return isoktime;
	}

	public void setIsoktime(String isoktime) {
		this.isoktime = isoktime;
	}

}
