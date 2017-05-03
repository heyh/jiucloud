package sy.model;

import sy.util.StringEscapeEditor;

import javax.persistence.*;

/**
 * Created by heyh on 2017/2/18.
 */

@Entity
@Table(name = "t_item")
public class Item {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "supInfo")
    private String supInfo;

    public String getSupInfo() {
        return supInfo;
    }

    public void setSupInfo(String supInfo) {
        this.supInfo = supInfo;
    }

    @Column(name = "cid")
    private String cid;

    @Column(name = "projectId")
    private String projectId;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "operator")
    private String operator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
