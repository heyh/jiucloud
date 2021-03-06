package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by heyh on 2017/12/4.
 */

@Entity
@Table(name = "t_monthplan_details")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MonthPlanDetails {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "monthPlanId")
    private int monthPlanId;

    @Column(name = "materialsId")
    private String materialsId;

    @Column(name = "count")
    private String count;

    @Column(name = "price")
    private String price;

    @Column(name = "total")
    private String total;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "isDelete")
    private String isDelete;

    @Column(name = "createTime")
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonthPlanId() {
        return monthPlanId;
    }

    public void setMonthPlanId(int monthPlanId) {
        this.monthPlanId = monthPlanId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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
}
