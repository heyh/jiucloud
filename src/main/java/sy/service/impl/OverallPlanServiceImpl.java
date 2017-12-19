package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.OverallPlanDaoI;
import sy.dao.OverallPlanDetailsDaoI;
import sy.model.po.*;
import sy.service.*;
import sy.util.ObjectExcelRead;
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

    @Autowired
    private MaterialsServiceI materialsService;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private UserServiceI userService;

    @Autowired
    private MonthPlanServiceI monthPlanService;


    @Override
    public List<OverallPlanBean> getOverallPlanList(String projectId) {
        List<OverallPlanBean> overallPlanBeanList = new ArrayList<OverallPlanBean>();
        OverallPlanBean overallPlanBean = new OverallPlanBean();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        String overallPlanHql = "from OverallPlan where projectId = :projectId and isDelete = 0 ";
        List<OverallPlan> overallPlanList = overallPlanDao.find(overallPlanHql, params);
        if (overallPlanList != null && overallPlanList.size()>0) {
            for (OverallPlan overallPlan : overallPlanList) {

                overallPlanBean = new OverallPlanBean();
                overallPlanBean.setId(overallPlan.getId());
                overallPlanBean.setCid(overallPlan.getCid());
                overallPlanBean.setProjectId(overallPlan.getProjectId());
                overallPlanBean.setProjectName(projectService.findOneView(Integer.parseInt(overallPlan.getProjectId())).getProName());
                overallPlanBean.setUid(overallPlan.getUid());
                overallPlanBean.setUname(userService.getUser(StringUtil.trimToEmpty(overallPlan.getUid())).getUsername());
                overallPlanBean.setCreateTime(overallPlan.getCreateTime());
                String approvedState = "";
                int needApproved = Integer.parseInt(overallPlan.getNeedApproved());
                switch (needApproved) {
                    case 0:
                        approvedState = "无需审批";
                        break;
                    case 1:
                        approvedState = "未审批";
                        break;
                    case 2:
                        approvedState = "审批通过";
                        break;
                    case 8:
                        approvedState = "审批中";
                        break;
                    case 9:
                        approvedState = "审批未通过";
                        break;
                    default:
                        approvedState = "未知";
                }
                overallPlanBean.setNeedApproved(approvedState);

                overallPlanBean.setApprovedOption(overallPlan.getApprovedOption());
                overallPlanBean.setCurrentApprovedUser(userService.getUser(StringUtil.trimToEmpty(overallPlan.getCurrentApprovedUser())).getUsername());

                overallPlanBeanList.add(overallPlanBean);
            }
        }
        return overallPlanBeanList;
    }

    @Override
    public List<OverallPlanDetailsBean> getOverallPlanDetailsList(String overallPlanId) {
        List<OverallPlanDetailsBean> overallPlanDetailsBeanList = new ArrayList<OverallPlanDetailsBean>();
        OverallPlanDetailsBean overallPlanDetailsBean = new OverallPlanDetailsBean();

        Map<String, Object> params = new HashMap<String, Object>();
        params = new HashMap<String, Object>();
        params.put("overallPlanId", Integer.parseInt(overallPlanId));

        String overallDetailsHql = "from OverallPlanDetails where overallPlanId = :overallPlanId and isDelete = 0 ";
        List<OverallPlanDetails> overallPlanDetailsList = overallPlanDetailsDao.find(overallDetailsHql, params);
        if (overallPlanDetailsList != null && overallPlanDetailsList.size()>0) {
            for (OverallPlanDetails overallPlanDetails : overallPlanDetailsList) {
                overallPlanDetailsBean = new OverallPlanDetailsBean();
                overallPlanDetailsBean.setMaterialsId(overallPlanDetails.getMaterialsId());

                Materials materials = materialsService.findById(Integer.parseInt(overallPlanDetails.getMaterialsId()));
                if (materials != null) {
                    overallPlanDetailsBean.setMc(materials.getMc());
                    overallPlanDetailsBean.setSpecifications(materials.getSpecifications());
                    overallPlanDetailsBean.setDw(materials.getDw());
                }

                overallPlanDetailsBean.setCount(overallPlanDetails.getCount());

                overallPlanDetailsBeanList.add(overallPlanDetailsBean);

            }
        }

        return overallPlanDetailsBeanList;
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

    @Override
    public List<OverallPlanDetailsBean> overallPlanDetailsAll(String projectId) {
        List<OverallPlanDetailsBean> overallPlanDetailsBeanAll = new ArrayList<OverallPlanDetailsBean>();
        List<OverallPlanBean> overallPlanList = getOverallPlanList(projectId);
        if (overallPlanList != null && overallPlanList.size() > 0) {
            for (OverallPlanBean overallPlanBean : overallPlanList) {
                List<OverallPlanDetailsBean> overallPlanDetailsBeanList = getOverallPlanDetailsList(StringUtil.trimToEmpty(overallPlanBean.getId()));
                if (overallPlanDetailsBeanList != null && overallPlanDetailsBeanList.size() > 0) {
                    overallPlanDetailsBeanAll.addAll(overallPlanDetailsBeanList);
                }
            }
        }
        overallPlanDetailsBeanAll = mergeList(overallPlanDetailsBeanAll);

        List<MonthPlanDetailsBean> monthPlanDetailsBeanList = monthPlanService.getMonthPlanDetailsMerge(projectId);

        if (overallPlanDetailsBeanAll != null && overallPlanDetailsBeanAll.size() > 0) {
            if (monthPlanDetailsBeanList != null && monthPlanDetailsBeanList.size() > 0) {
                for (OverallPlanDetailsBean overallPlanDetailsBean : overallPlanDetailsBeanAll) {
                    for (MonthPlanDetailsBean monthPlanDetailsBean : monthPlanDetailsBeanList) {
                        if (Integer.parseInt(overallPlanDetailsBean.getMaterialsId()) == Integer.parseInt(monthPlanDetailsBean.getMaterialsId())) {
                            int remainCount = Integer.parseInt(overallPlanDetailsBean.getCount()) - Integer.parseInt(monthPlanDetailsBean.getCount());
                            overallPlanDetailsBean.setRemainCount(StringUtil.trimToEmpty(remainCount));
                        }
                    }
                }
            }
        }
        return overallPlanDetailsBeanAll;
    }

    private List<OverallPlanDetailsBean> mergeList(List<OverallPlanDetailsBean> list) {
        HashMap<String, OverallPlanDetailsBean> map = new HashMap<String, OverallPlanDetailsBean>();
        for (OverallPlanDetailsBean bean : list) {
            if (map.containsKey(bean.getMaterialsId())) {
                map.get(bean.getMaterialsId()).setCount(StringUtil.trimToEmpty(Integer.parseInt(map.get(bean.getMaterialsId()).getCount()) + Integer.parseInt(bean.getCount())));
            } else {
                map.put(bean.getMaterialsId(), bean);
            }
        }
        list.clear();
        list.addAll(map.values());

        return list;
    }
}
