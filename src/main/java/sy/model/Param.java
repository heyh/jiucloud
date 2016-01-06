package sy.model;

import javax.persistence.*;

/**
 * Created by heyh on 16/1/6.
 */
@Entity
@Table(name = "T_PARAM")
public class Param {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "PARAM_TYPE_CODE")
    private String paramTypeCode;

    @Column(name = "PARAM_TYPE_NAME")
    private String paramTypeName;

    @Column(name = "PARAM_CODE")
    private String paramCode;

    @Column(name = "PARAM_VALUE")
    private String paramValue;

    @Column(name = "PARENT_CODE")
    private String parentCode;

    @Column(name = "STATUS")
    private String status;

    public String getParamTypeCode() {
        return paramTypeCode;
    }

    public void setParamTypeCode(String paramTypeCode) {
        this.paramTypeCode = paramTypeCode;
    }

    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
