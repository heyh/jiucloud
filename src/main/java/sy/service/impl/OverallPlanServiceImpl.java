package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.OverallPlanDaoI;
import sy.dao.OverallPlanDetailsDaoI;
import sy.model.po.OverallPlan;
import sy.model.po.OverallPlanDetails;
import sy.service.OverallPlanServiceI;
import sy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/4.
 */

@Service
public class OverallPlanServiceImpl implements OverallPlanServiceI {

    @Autowired
    private OverallPlanDaoI overallPlanDao;

    @Autowired
    private OverallPlanDetailsDaoI overallPlanDetailsDao;


    @Override
    public List<OverallPlanDetails> getOverallPlanList(String projectId) {
        List<OverallPlanDetails> overallPlanDetailsList = new ArrayList<OverallPlanDetails>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        String overallPlanHql = "from OverallPlan where projectId = :projectId and isDelete = 0 ";
        List<OverallPlan> overallPlanList = overallPlanDao.find(overallPlanHql, params);
        if (overallPlanList != null && overallPlanList.size()>0) {
            for (OverallPlan overallPlan : overallPlanList) {
                int overallPlanId = overallPlan.getId();
                params = new HashMap<String, Object>();
                params.put("overallPlanId", overallPlanId);
                String overallDetailsHql = "from OverallPlanDetails where overallPlanId = :overallPlanId and isDelete = 0 ";
                overallPlanDetailsList.addAll(overallPlanDetailsDao.find(overallDetailsHql, params));
            }
        }
        return overallPlanDetailsList;
    }

    @Override
    public void addOverallPlan(OverallPlan overallPlan) {
        overallPlanDao.save(overallPlan);
    }

    @Override
    public int getId(OverallPlan overallPlan) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", overallPlan.getCid());
        params.put("uid", overallPlan.getUid());
        params.put("projectId", overallPlan.getProjectId());
        params.put("createTime", overallPlan.getCreateTime());
        List<Object[]> overallPlans = overallPlanDao.findBySql("select * from t_overallplan where cid=:cid and uid=:uid and projectId=:projectId and createTime=:createTime and isDelete='0' order by id desc", params);
        if (overallPlans != null && overallPlans.size()>0) {
            return Integer.parseInt(StringUtil.trimToEmpty(overallPlans.get(0)[0]));
        }
        return 0;
    }

    @Override
    public void addOverallPlanDetails(OverallPlanDetails overallPlanDetails) {
        overallPlanDetailsDao.save(overallPlanDetails);
    }
}
