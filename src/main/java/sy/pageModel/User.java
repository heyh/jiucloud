package sy.pageModel;


public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4639211583956944465L;

	private String id;
	private String username;
	private String password;
	private String realname;
	private String corporation_id;
	private String corporation_creater;
    private String mobile_phone;
    private String email;

    private String isLogin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    /**
	 * 用户头像 upload/vod/user/id/uariva用户头像
	 */
	private String uariva;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCorporation_id() {
		return corporation_id;
	}

	public void setCorporation_id(String corporation_id) {
		this.corporation_id = corporation_id;
	}

	public String getCorporation_creater() {
		return corporation_creater;
	}

	public void setCorporation_creater(String corporation_creater) {
		this.corporation_creater = corporation_creater;
	}

	public String getUariva() {
		return uariva;
	}

	public void setUariva(String uariva) {
		this.uariva = uariva;
	}

	public String getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", realname=" + realname + ", corporation_id="
				+ corporation_id + ", corporation_creater="
				+ corporation_creater +"]";
	}

	/*
	 * //获得首字母 private static String getPinYinHeadChar(String str) { String
	 * convert = "";
	 * 
	 * char word = str.charAt(0); // 提取汉字的首字母
	 * 
	 * String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word); if
	 * (pinyinArray != null) { convert += pinyinArray[0].charAt(0); } else {
	 * convert += word; }
	 * 
	 * return convert; }
	 */
}
