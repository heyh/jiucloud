package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.MonthPlanDaoI;
import sy.dao.MonthPlanDetailsDaoI;
import sy.model.po.*;
import sy.service.MaterialsServiceI;
import sy.service.MonthPlanServiceI;
import sy.service.ProjectServiceI;
import sy.service.UserServiceI;
import sy.util.DateKit;
import sy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/16.
 */

@Service
public class MonthPlanServiceImpl implements MonthPlanServiceI {

    @Autowired
    private MonthPlanDaoI monthPlanDao;

    @Autowired
    private MonthPlanDetailsDaoI monthPlanDetailsDao;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private UserServiceI userService;

    @Autowired
    private MaterialsServiceI materialsService;

    @Override
    public List<MonthPlanBean> getMonthPlanList(String projectId, String startDate, String endDate) {
        List<MonthPlanBean> monthPlanBeanList = new ArrayList<MonthPlanBean>();
        MonthPlanBean monthPlanBean = new MonthPlanBean();

        Map<String, Object> params = new HashMap<String, Object>();

        String monthPlanHql = "from MonthPlan where isDelete = 0 ";
        if (!projectId.equals("")) {
            monthPlanHql += "projectId = :projectId ";
            params.put("projectId", projectId);
        }
        if (!startDate.equals("")) {
            monthPlanHql += " and createTime >= :startTime ";
            params.put("startTime", DateKit.strToDateOrTime(startDate));
        }
        if (!endDate.equals("")) {
            monthPlanHql += " and createTime <= :endTime";
            params.put("endTime", DateKit.strToDateOrTime(endDate));
        }
        List<MonthPlan> monthPlanList = monthPlanDao.find(monthPlanHql, params);

        if (monthPlanList != null && monthPlanList.size()>0) {
            for (MonthPlan monthPlan : monthPlanList) {
                monthPlanBean = new MonthPlanBean();
                monthPlanBean.setId(monthPlan.getId());
                monthPlanBean.setCid(monthPlan.getCid());
                monthPlanBean.setOverallPlanId(monthPlan.getOverallPlanId());
                monthPlanBean.setProjectId(monthPlan.getProjectId());
                if (!StringUtil.trimToEmpty(monthPlan.getProjectId()).equals("")) {
                    monthPlanBean.setProjectName(projectService.findOneView(Integer.parseInt(monthPlan.getProjectId())).getProName());
                } else {
                    monthPlanBean.setProjectName("无项目采购申请");

                }
                monthPlanBean.setUid(monthPlan.getUid());
                monthPlanBean.setUname(userService.getUser(StringUtil.trimToEmpty(monthPlan.getUid())).getUsername());
                monthPlanBean.setCreateTime(monthPlan.getCreateTime());
                String approvedState = "";
                int needApproved = Integer.parseInt(monthPlan.getNeedApproved());
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
                monthPlanBean.setNeedApproved(approvedState);

                monthPlanBean.setApprovedOption(monthPlan.getApprovedOption());
                monthPlanBean.setCurrentApprovedUser(userService.getUser(StringUtil.trimToEmpty(monthPlan.getCurrentApprovedUser())).getUsername());

                monthPlanBeanList.add(monthPlanBean);
            }
        }
        return monthPlanBeanList;
    }

    @Override
    public List<MonthPlanDetailsBean> getMonthPlanDetailsBeanList(String monthPlanId) {
        List<MonthPlanDetailsBean> monthPlanDetailsBeanList = new ArrayList<MonthPlanDetailsBean>();
        MonthPlanDetailsBean monthPlanDetailsBean = new MonthPlanDetailsBean();

        Map<String, Object> params = new HashMap<String, Object>();
        params = new HashMap<String, Object>();
        params.put("monthPlanId", Integer.parseInt(monthPlanId));

        String monthDetailsHql = "from MonthPlanDetails where monthPlanId = :monthPlanId and isDelete = 0 ";
        List<MonthPlanDetails> monthPlanDetailsList = monthPlanDetailsDao.find(monthDetailsHql, params);
        if (monthPlanDetailsList != null && monthPlanDetailsList.size()>0) {
            for (MonthPlanDetails monthPlanDetails : monthPlanDetailsList) {
                monthPlanDetailsBean = new MonthPlanDetailsBean();
                monthPlanDetailsBean.setMaterialsId(monthPlanDetails.getMaterialsId());

                Materials materials = materialsService.findById(Integer.parseInt(monthPlanDetails.getMaterialsId()));
                if (materials != null) {
                    monthPlanDetailsBean.setMc(materials.getMc());
                    monthPlanDetailsBean.setSpecifications(materials.getSpecifications());
                    monthPlanDetailsBean.setDw(materials.getDw());
                }

                monthPlanDetailsBean.setCount(monthPlanDetails.getCount());
                monthPlanDetailsBean.setSupplier("");

                monthPlanDetailsBeanList.add(monthPlanDetailsBean);

            }
        }

        return monthPlanDetailsBeanList;
    }
}
