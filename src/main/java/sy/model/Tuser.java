package sy.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jsw_user")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tuser implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4084905430828749325L;
	/**
	 * 描述
	 */
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	private String username;
	private String password;
	private String realname;
	private String safecode;
	private String corporation_id;
	private String corporation_creater;
    private String mobile_phone;
    private String email;

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

	public String getSafecode() {
		return safecode;
	}

	public void setSafecode(String safecode) {
		this.safecode = safecode;
	}

	@Override
	public String toString() {
		return "Tuser [id=" + id + ", username=" + username + ", password="
				+ password + ", realname=" + realname + ", safecode="
				+ safecode + "]";
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
}
