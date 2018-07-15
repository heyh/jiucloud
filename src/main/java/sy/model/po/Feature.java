package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by heyh on 2017/8/3.
 */

@Entity
@Table(name = "T_Feature")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Feature {
    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "itemCode")
    private String itemCode;

    @Column(name = "mc")
    private String mc;

    @Column(name = "count")
    private String count;

    @Column(name = "dw")
    private String dw;

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    private String costType;

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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }
}
