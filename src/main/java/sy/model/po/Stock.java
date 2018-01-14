package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 2017/12/23.
 */

@Entity
@Table(name = "t_stock")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Stock {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "uid")
    private String uid;

    @Column(name = "projectId")
    private String projectId;

    @Column(name = "materialsId")
    private String materialsId;

    @Column(name = "count")
    private String count;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "isDelete")
    private String isDelete;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "type")
    private String type;

    @Column(name = "relId")
    private String relId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMaterialsId() {
        return materialsId;
    }

    public void setMaterialsId(String materialsId) {
        this.materialsId = materialsId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }
}
