package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.MonthPlanDaoI;
import sy.dao.MonthPlanDetailsDaoI;
import sy.model.po.*;
import sy.pageModel.User;
import sy.service.*;
import sy.util.DateKit;
import sy.util.StringUtil;
import sy.util.UtilDate;

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

    @Autowired
    private SupplierServiceI supplierService;

    @Override
    public List<MonthPlanBean> getMonthPlanList(String projectId, String startDate, String endDate, String cid, List<Integer> ugroup) {
        List<MonthPlanBean> monthPlanBeanList = new ArrayList<MonthPlanBean>();
        MonthPlanBean monthPlanBean = new MonthPlanBean();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);

        String monthPlanHql = "from MonthPlan where isDelete = 0 and cid=:cid ";

        String uids = StringUtils.join(ugroup, ",");
        monthPlanHql += " and uid in (" + uids + ") " ;

        monthPlanHql += " and projectId = :projectId ";
        params.put("projectId", projectId);

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
                User user = userService.getUser(StringUtil.trimToEmpty(monthPlan.getUid()));
                monthPlanBean.setUname(user.getRealname().equals("") ? user.getUsername() : user.getRealname());
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

                if (!StringUtil.trimToEmpty(monthPlan.getCurrentApprovedUser()).equals("")) {
                    User currentApprovedUser = userService.getUser(StringUtil.trimToEmpty(monthPlan.getCurrentApprovedUser()));
                    monthPlanBean.setCurrentApprovedUser(currentApprovedUser.getRealname().equals("") ? currentApprovedUser.getUsername() : currentApprovedUser.getRealname());
                } else {
                    monthPlanBean.setCurrentApprovedUser("");
                }
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
                monthPlanDetailsBean.setId(monthPlanDetails.getId());
                monthPlanDetailsBean.setMaterialsId(monthPlanDetails.getMaterialsId());

                Materials materials = materialsService.findById(Integer.parseInt(monthPlanDetails.getMaterialsId()));
                if (materials != null) {
                    monthPlanDetailsBean.setMc(materials.getMc());
                    monthPlanDetailsBean.setSpecifications(materials.getSpecifications());
                    monthPlanDetailsBean.setDw(materials.getDw());
                }

                monthPlanDetailsBean.setCount(monthPlanDetails.getCount());
                monthPlanDetailsBean.setPrice(monthPlanDetails.getPrice());
                monthPlanDetailsBean.setTotal(monthPlanDetails.getTotal());

                if (!StringUtil.trimToEmpty(monthPlanDetails.getSupplier()).equals("")) {
                    monthPlanDetailsBean.setSupplierId(StringUtil.trimToEmpty(monthPlanDetails.getSupplier()));
                    Supplier supplierInfo = supplierService.detail(StringUtil.trimToEmpty(monthPlanDetails.getSupplier()));
                    if (supplierInfo != null) {
                        monthPlanDetailsBean.setSupplierName(StringUtil.trimToEmpty(supplierInfo.getName()));
                    }
                } else {
                    monthPlanDetailsBean.setSupplierId("");
                    monthPlanDetailsBean.setSupplierName("");
                }
                monthPlanDetailsBeanList.add(monthPlanDetailsBean);

            }
        }

        return monthPlanDetailsBeanList;
    }

    @Override
    public List<MonthPlanDetailsBean> getMonthPlanDetailsMerge(String projectId) {
        List<MonthPlanDetailsBean> monthPlanDetailsBeanList = new ArrayList<MonthPlanDetailsBean>();
        MonthPlanDetailsBean monthPlanDetailsBean = new MonthPlanDetailsBean();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        String monthPlanHql = "from MonthPlan where isDelete = 0 and projectId = :projectId ";
        List<MonthPlan> monthPlanList = monthPlanDao.find(monthPlanHql, params);
        if (monthPlanList != null && monthPlanList.size() > 0) {
            for (MonthPlan monthPlan : monthPlanList) {
                params = new HashMap<String, Object>();
                params.put("monthPlanId", monthPlan.getId());
                String monthDetailsHql = " from MonthPlanDetails where monthPlanId = :monthPlanId and isDelete = 0 ";
                List<MonthPlanDetails> monthPlanDetailsList = monthPlanDetailsDao.find(monthDetailsHql, params);
                if (monthPlanDetailsList != null && monthPlanDetailsList.size() > 0) {
                    for (MonthPlanDetails monthPlanDetails : monthPlanDetailsList) {
                        monthPlanDetailsBean = new MonthPlanDetailsBean();
                        monthPlanDetailsBean.setId(monthPlanDetails.getId());
                        monthPlanDetailsBean.setMaterialsId(monthPlanDetails.getMaterialsId());
                        monthPlanDetailsBean.setCount(monthPlanDetails.getCount());
                        monthPlanDetailsBeanList.add(monthPlanDetailsBean);
                    }
                }
            }
        }
        return mergeList(monthPlanDetailsBeanList);
    }

    @Override
    public void addMonthPlan(MonthPlan monthPlan) {
        monthPlanDao.save(monthPlan);
    }

    @Override
    public int getId(MonthPlan monthPlan) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", monthPlan.getCid());
        params.put("uid", monthPlan.getUid());
        params.put("projectId", monthPlan.getProjectId());
        params.put("createTime", monthPlan.getCreateTime());
        List<Object[]> monthPlans = monthPlanDao.findBySql("select * from t_monthplan where cid=:cid and uid=:uid and projectId=:projectId and createTime=:createTime and isDelete='0' order by id desc", params);
        if (monthPlans != null && monthPlans.size()>0) {
            return Integer.parseInt(StringUtil.trimToEmpty(monthPlans.get(0)[0]));
        }

        return 0;
    }

    @Override
    public void addMonthPlanDetails(MonthPlanDetails monthPlanDetails) {
        monthPlanDetailsDao.save(monthPlanDetails);
    }

    @Override
    public void updateMonthPlan(MonthPlan info) {
        MonthPlan monthPlan = monthPlanDao.get(MonthPlan.class, info.getId());
        BeanUtils.copyProperties(info, monthPlan);
    }

    @Override
    public void delMonthPlan(String monthplanId) {
        if (monthplanId != null) {
            try {
                MonthPlan monthPlan = monthPlanDao.get(" FROM MonthPlan where 1=1 and id = " + monthplanId);
                if (monthPlan != null) {
                    monthPlan.setIsDelete("1");
                    updateMonthPlan(monthPlan);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateMonthPlanDetails(MonthPlanDetails info) {
        MonthPlanDetails monthPlanDetails = monthPlanDetailsDao.get(MonthPlanDetails.class, info.getId());
        BeanUtils.copyProperties(info, monthPlanDetails);
    }

    @Override
    public void delMonthPlanDetails(String monthPlanDetailsId) {
        if (monthPlanDetailsId != null) {
            try {
                MonthPlanDetails monthPlanDetails = monthPlanDetailsDao.get(" FROM MonthPlanDetails where 1=1 and id = " + monthPlanDetailsId);
                if (monthPlanDetails != null) {
                    monthPlanDetails.setIsDelete("1");
                    updateMonthPlanDetails(monthPlanDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MonthPlanBean> getApproveMonthPlanList(String cid, String currentApprovedUser, String projectId, String startDate, String endDate) {
        List<MonthPlanBean> monthPlanBeanList = new ArrayList<MonthPlanBean>();
        MonthPlanBean monthPlanBean = new MonthPlanBean();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String monthPlanHql = "from MonthPlan where isDelete = 0 and needApproved in ('1', '8') and cid = :cid ";
        if (!projectId.equals("")) {
            monthPlanHql += " and projectId = :projectId ";
            params.put("projectId", projectId);
        }
        if (!currentApprovedUser.equals("")) {
            monthPlanHql += " and currentApprovedUser = :currentApprovedUser ";
            params.put("currentApprovedUser", currentApprovedUser);
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
                User user = userService.getUser(StringUtil.trimToEmpty(monthPlan.getUid()));
                monthPlanBean.setUname(user.getRealname().equals("") ? user.getUsername() : user.getRealname());
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
    public void approveMonthPlan(Integer monthPlanId, String approvedState, String approvedOption, String currentApprovedUser) {
        try {
            MonthPlan monthPlan =  monthPlanDao.get("from MonthPlan  where id = " + monthPlanId);
            if (!monthPlan.getApprovedUser().equals("")) {
                String[] approvedUsers = monthPlan.getApprovedUser().split(",");
                if (approvedUsers != null && approvedUsers.length > 0) {
                    String lastApprovedUser = approvedUsers[approvedUsers.length - 1];
                    String userName = userService.get(lastApprovedUser).getUsername();
                    approvedOption = UtilDate.getDateFormatter() + "::" + userName + "::" + approvedOption;
                }

                if (approvedState.equals("8")) { // 需要查看是不是最终审批人，不是改为审批中
                    monthPlan.setApprovedUser(monthPlan.getApprovedUser() + "," + currentApprovedUser);
                    monthPlan.setCurrentApprovedUser(currentApprovedUser);

                    // 推送任务
//                    taskPushService.addFieldTaskPush(t);
                }
            }
            monthPlan.setNeedApproved(approvedState);


            monthPlan.setApprovedOption(StringUtil.trimToEmpty(monthPlan.getApprovedOption()).equals("") ? approvedOption : StringUtil.trimToEmpty(monthPlan.getApprovedOption()) + "|" + approvedOption);
            monthPlanDao.update(monthPlan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<MonthPlanDetailsBean> mergeList(List<MonthPlanDetailsBean> list) {
        HashMap<String, MonthPlanDetailsBean> map = new HashMap<String, MonthPlanDetailsBean>();
        for (MonthPlanDetailsBean bean : list) {
            if (map.containsKey(bean.getMaterialsId())) {
                map.get(bean.getMaterialsId()).setCount(StringUtil.trimToEmpty(Integer.parseInt(StringUtil.trimToEmpty(map.get(bean.getMaterialsId()).getCount()))  + Integer.parseInt(StringUtil.trimToEmpty(bean.getCount()))));
            } else {
                map.put(bean.getMaterialsId(), bean);
            }
        }
        list.clear();
        list.addAll(map.values());

        return list;
    }

}
