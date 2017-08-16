package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by heyh on 2017/8/16.
 */

@Entity
@Table(name = "t_location")
@DynamicInsert(true)
@DynamicUpdate(true)

public class Location {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "mc")
    private String mc;

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

}
