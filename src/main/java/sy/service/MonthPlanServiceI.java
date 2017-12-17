package sy.service;

import sy.model.po.MonthPlanBean;
import sy.model.po.MonthPlanDetailsBean;

import java.util.List;

/**
 * Created by heyh on 2017/12/16.
 */
public interface MonthPlanServiceI {
    List<MonthPlanBean> getMonthPlanList(String projectId, String startDate, String endDate);
    List<MonthPlanDetailsBean> getMonthPlanDetailsBeanList(String monthPlanId);
}
