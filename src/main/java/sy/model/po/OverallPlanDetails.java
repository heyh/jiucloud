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
@Table(name = "t_overallplan_details")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OverallPlanDetails {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @Column(name = "overallPlanId")
    private int overallPlanId;

    @Column(name = "materialsId")
    private String materialsId;

    @Column(name = "count")
    private String count;

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

    public int getOverallPlanId() {
        return overallPlanId;
    }

    public void setOverallPlanId(int overallPlanId) {
        this.overallPlanId = overallPlanId;
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
