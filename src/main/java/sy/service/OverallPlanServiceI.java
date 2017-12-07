package sy.service;

import sy.model.po.OverallPlan;
import sy.model.po.OverallPlanDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/4.
 */
public interface OverallPlanServiceI {

    public List<OverallPlanDetails> getOverallPlanList(String projectId);
    public void addOverallPlan(OverallPlan overallPlan);
    public int getId(OverallPlan overallPlan);

    public void addOverallPlanDetails(OverallPlanDetails overallPlanDetails);
}
