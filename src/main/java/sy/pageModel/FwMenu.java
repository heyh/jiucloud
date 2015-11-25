package sy.pageModel;

import java.util.ArrayList;
import java.util.List;


/**
 * ****************************************************************
 * 文件名称 : FwMenu.java
 * 作 者 :   Administrator
 * 创建时间 : 2015年1月13日 上午9:19:20
 * 文件描述 : 用于显示菜单
 * 版权声明 : 
 * 修改历史 : 2015年1月13日 1.00 初始版本
 *****************************************************************
 */
public class FwMenu {
	
	private int id;
	
	private String firstName; //第1个菜单标题
	
	private String secondName;//第2个菜单标题
	
	private String thirdName;//第3个菜单标题
	
	private String fwName;//服务号名
	
	private String createTime;//创建日期
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	
}
