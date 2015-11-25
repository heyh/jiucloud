package sy.pageModel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Synchronize;

import sy.model.IMUser;

/**
 * ****************************************************************
 * 文件名称 : IMUser.java
 * 作 者 :   Administrator
 * 创建时间 : 2014年12月26日 上午11:13:25
 * 文件描述 : 同步IM
 * 版权声明 : 
 * 修改历史 : 2014年12月26日 1.00 初始版本
 *****************************************************************
 */
public class SynIMUser {
	private String username;
	private String password;
	private String nickname;
	
	private String department;//部门
	private List<IMUser> imuser = new ArrayList<IMUser>();//部门下所有用户信息
	
	public SynIMUser() {
		super();
	}
	public SynIMUser(String username, String password, String nickname) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public List<IMUser> getImuser() {
		return imuser;
	}
	public void setImuser(List<IMUser> imuser) {
		this.imuser = imuser;
	}
	
}
