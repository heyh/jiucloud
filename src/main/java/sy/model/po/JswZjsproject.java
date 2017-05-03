package sy.model.po;

import javax.persistence.*;

/**
 * Created by heyh on 2016/10/21.
 */
@Entity
@Table(name = "jsw_zjsproject")
public class JswZjsproject {
    private int id;
    private int isUpdate;
    private String parentId;
    private int level;
    private String userId;
    private String name;
    private String filePath;
    private int addTime;
    private byte zjsdwgcIsopen;
    private double zjsdwgcZzj;
    private String zjsdwgcBh;
    private String zjsdwgcFilename;
    private String provinceName;
    private String temporaryId;
    private int isShare;
    private int isShow;
    private int isOnLine;
    private String cityName;
    private int updateTime;
    private String gcid;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "is_update")
    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "add_time")
    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "zjsdwgc_isopen")
    public byte getZjsdwgcIsopen() {
        return zjsdwgcIsopen;
    }

    public void setZjsdwgcIsopen(byte zjsdwgcIsopen) {
        this.zjsdwgcIsopen = zjsdwgcIsopen;
    }

    @Basic
    @Column(name = "zjsdwgc_zzj")
    public double getZjsdwgcZzj() {
        return zjsdwgcZzj;
    }

    public void setZjsdwgcZzj(double zjsdwgcZzj) {
        this.zjsdwgcZzj = zjsdwgcZzj;
    }

    @Basic
    @Column(name = "zjsdwgc_bh")
    public String getZjsdwgcBh() {
        return zjsdwgcBh;
    }

    public void setZjsdwgcBh(String zjsdwgcBh) {
        this.zjsdwgcBh = zjsdwgcBh;
    }

    @Basic
    @Column(name = "zjsdwgc_filename")
    public String getZjsdwgcFilename() {
        return zjsdwgcFilename;
    }

    public void setZjsdwgcFilename(String zjsdwgcFilename) {
        this.zjsdwgcFilename = zjsdwgcFilename;
    }

    @Basic
    @Column(name = "province_name")
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Basic
    @Column(name = "temporary_id")
    public String getTemporaryId() {
        return temporaryId;
    }

    public void setTemporaryId(String temporaryId) {
        this.temporaryId = temporaryId;
    }

    @Basic
    @Column(name = "is_share")
    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    @Basic
    @Column(name = "is_show")
    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    @Basic
    @Column(name = "isOnLine")
    public int getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(int isOnLine) {
        this.isOnLine = isOnLine;
    }

    @Basic
    @Column(name = "city_name")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "update_time")
    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "gcid")
    public String getGcid() {
        return gcid;
    }

    public void setGcid(String gcid) {
        this.gcid = gcid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JswZjsproject that = (JswZjsproject) o;

        if (id != that.id) return false;
        if (level != that.level) return false;
        if (addTime != that.addTime) return false;
        if (zjsdwgcIsopen != that.zjsdwgcIsopen) return false;
        if (Double.compare(that.zjsdwgcZzj, zjsdwgcZzj) != 0) return false;
        if (isShare != that.isShare) return false;
        if (isShow != that.isShow) return false;
        if (isOnLine != that.isOnLine) return false;
        if (updateTime != that.updateTime) return false;
        if (isUpdate != that.isUpdate) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
        if (zjsdwgcBh != null ? !zjsdwgcBh.equals(that.zjsdwgcBh) : that.zjsdwgcBh != null) return false;
        if (zjsdwgcFilename != null ? !zjsdwgcFilename.equals(that.zjsdwgcFilename) : that.zjsdwgcFilename != null)
            return false;
        if (provinceName != null ? !provinceName.equals(that.provinceName) : that.provinceName != null) return false;
        if (temporaryId != null ? !temporaryId.equals(that.temporaryId) : that.temporaryId != null) return false;
        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null) return false;
        if (gcid != null ? !gcid.equals(that.gcid) : that.gcid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + isUpdate;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + level;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + addTime;
        result = 31 * result + (int) zjsdwgcIsopen;
        temp = Double.doubleToLongBits(zjsdwgcZzj);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (zjsdwgcBh != null ? zjsdwgcBh.hashCode() : 0);
        result = 31 * result + (zjsdwgcFilename != null ? zjsdwgcFilename.hashCode() : 0);
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        result = 31 * result + (temporaryId != null ? temporaryId.hashCode() : 0);
        result = 31 * result + isShare;
        result = 31 * result + isShow;
        result = 31 * result + isOnLine;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + updateTime;
        result = 31 * result + (gcid != null ? gcid.hashCode() : 0);
        return result;
    }
}
