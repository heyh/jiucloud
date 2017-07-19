package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "UserDeviceRel")
public class UserDeviceRel {
    private static final long serialVersionUID = -3597462271927905098L;

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "registrationId")
    private String registrationId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "isPush")
    private String isPush;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "updateTime")
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDeviceRel that = (UserDeviceRel) o;

        if (id != that.id) return false;
        if (!registrationId.equals(that.registrationId)) return false;
        if (!userId.equals(that.userId)) return false;
        if (isPush != null ? !isPush.equals(that.isPush) : that.isPush != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return updateTime != null ? updateTime.equals(that.updateTime) : that.updateTime == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + registrationId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + (isPush != null ? isPush.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
