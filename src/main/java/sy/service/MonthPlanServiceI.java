package sy.service;

import sy.model.po.MonthPlan;
import sy.model.po.MonthPlanBean;
import sy.model.po.MonthPlanDetails;
import sy.model.po.MonthPlanDetailsBean;

import java.util.List;

/**
 * Created by heyh on 2017/12/16.
 */
public interface MonthPlanServiceI {
    List<MonthPlanBean> getMonthPlanList(String projectId, String startDate, String endDate);

    List<MonthPlanDetailsBean> getMonthPlanDetailsBeanList(String monthPlanId);

    List<MonthPlanDetailsBean> getMonthPlanDetailsMerge(String projectId);

    void addMonthPlan(MonthPlan monthPlan);

    int getId(MonthPlan monthPlan);

    void addMonthPlanDetails(MonthPlanDetails monthPlanDetails);

    void updateMonthPlan(MonthPlan info);

    void delMonthPlan(String monthplanId);

    void updateMonthPlanDetails(MonthPlanDetails info);

    void delMonthPlanDetails(String monthPlanDetailsId);

    List<MonthPlanBean> getApproveMonthPlanList(String cid, String currentApprovedUser, String projectId, String startDate, String endDate);

    void approveMonthPlan(Integer monthPlanId, String approvedState, String approvedOption, String currentApprovedUser);
}
