package sy.service;

import sy.model.po.OverallPlan;
import sy.model.po.OverallPlanBean;
import sy.model.po.OverallPlanDetails;
import sy.model.po.OverallPlanDetailsBean;

import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/4.
 */
public interface OverallPlanServiceI {

    public List<OverallPlanBean> getOverallPlanList(String projectId);

    List<OverallPlanDetailsBean> getOverallPlanDetailsList(String overallPlanId);

    public void addOverallPlan(OverallPlan overallPlan);

    public int getId(OverallPlan overallPlan);

    public void addOverallPlanDetails(OverallPlanDetails overallPlanDetails);

    List<OverallPlanDetailsBean> overallPlanDetailsAll(String projectId);

    void updateOverallPlan(OverallPlan info);

    void delOverallPlan(String overallPlanId);

    void updateOverallPlanDetails(OverallPlanDetails info);

    void delOverallPlanDetails(String overallPlanDetailsId);

    List<OverallPlanBean> getApproveOverallPlanList(String cid, String currentApprovedUser, String projectId, String startDate, String endDate);

    void approveOverallPlan(Integer overallplanId, String approvedState, String approvedOption, String currentApprovedUser);
}
