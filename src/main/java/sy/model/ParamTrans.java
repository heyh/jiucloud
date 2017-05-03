package sy.model;

import javax.persistence.*;

/**
 * Created by heyh on 2016/12/25.
 */
@Entity
@Table(name = "T_ParamTrans")
public class ParamTrans {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PARAM_TYPE")
    private String paramType;

    @Column(name = "PARAM_CODE")
    private String paramCode;

    @Column(name = "PARAM_NAME")
    private String paramName;

    @Column(name = "TRANS_PARAM_CODE")
    private String transParamCode;

    @Column(name = "TRANS_PARAM_NAME")
    private String transParamName;

    @Column(name = "CID")
    private String cid;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getTransParamCode() {
        return transParamCode;
    }

    public void setTransParamCode(String transParamCode) {
        this.transParamCode = transParamCode;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTransParamName() {
        return transParamName;
    }

    public void setTransParamName(String transParamName) {
        this.transParamName = transParamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
