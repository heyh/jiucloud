package sy.model.po;

import java.util.Date;

/**
 * Created by heyh on 2017/12/4.
 */


public class OverallPlanDetailsBean {
    private int id;
    private int overallPlanId;
    private String materialsId;
    private String count;
    private String isDelete;
    private Date createTime;
    private String mc;
    private String dw;
    private String specifications;
    private String remainCount;
    private String stockCount;

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

    public String getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(String remainCount) {
        this.remainCount = remainCount;
    }

    public String getStockCount() {
        return stockCount;
    }

    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }
}
