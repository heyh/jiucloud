package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 16/7/16.
 */

@Entity
@Table(name = "t_template")
public class Template {

    @Id
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "templateType")
    private String templateType;

    @Column(name = "templateName")
    private String templateName;

    @Column(name = "templateDesc")
    private String templateDesc;

    @Column(name = "projectId")
    private String projectId;

    @Column(name = "cid")
    private String cid;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "updateTime")
    private Date updateTime;

    @Column(name = "operater")
    private String operater;

    @Column(name = "isDel")
    private String isDel;

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", templateType='" + templateType + '\'' +
                ", templateName='" + templateName + '\'' +
                ", templateDesc='" + templateDesc + '\'' +
                ", projectId='" + projectId + '\'' +
                ", cid='" + cid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operater='" + operater + '\'' +
                '}';
    }

    public Template(int id, String templateType, String templateName, String templateDesc, String projectId, String cid, Date createTime, Date updateTime, String operater) {
        this.id = id;
        this.templateType = templateType;
        this.templateName = templateName;
        this.templateDesc = templateDesc;
        this.projectId = projectId;
        this.cid = cid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.operater = operater;
    }

    public Template() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
}
