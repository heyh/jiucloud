package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by heyh on 2017/12/2.
 */

@Entity
@Table(name = "t_materials")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Materials {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "mc")
    private String mc;

    @Column(name = "dw")
    private String dw;

    @Column(name = "specifications")
    private String specifications;

    @Column(name = "pid")
    private String pid;

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

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
