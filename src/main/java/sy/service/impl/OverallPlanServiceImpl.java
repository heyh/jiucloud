package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.OverallPlanDaoI;
import sy.dao.OverallPlanDetailsDaoI;
import sy.model.po.*;
import sy.pageModel.PageHelper;
import sy.pageModel.User;
import sy.service.*;
import sy.util.DateKit;
import sy.util.ObjectExcelRead;
import sy.util.StringUtil;
import sy.util.UtilDate;

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

    @Autowired
    private StockServiceI stockService;


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
                User user = userService.getUser(StringUtil.trimToEmpty(overallPlan.getUid()));
                overallPlanBean.setUname(user.getRealname().equals("") ? user.getUsername() : user.getRealname());
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

                if (!StringUtil.trimToEmpty(overallPlan.getCurrentApprovedUser()).equals("")) {
                    User currentApprovedUser = userService.getUser(StringUtil.trimToEmpty(overallPlan.getCurrentApprovedUser()));
                    overallPlanBean.setCurrentApprovedUser(currentApprovedUser.getRealname().equals("") ? currentApprovedUser.getUsername() : currentApprovedUser.getRealname());
                } else {
                    overallPlanBean.setCurrentApprovedUser("");
                }
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
                overallPlanDetailsBean.setId(overallPlanDetails.getId());
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
    public List<OverallPlanDetailsBean> overallPlanDetailsAll(String cid, String projectId) {
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
                            Double overallPlanDetailsBeanCount = 0.00;
                            if (!StringUtil.trimToEmpty(overallPlanDetailsBean.getCount()).equals("")) {
                                overallPlanDetailsBeanCount = Double.parseDouble(StringUtil.trimToEmpty(overallPlanDetailsBean.getCount()));
                            }
                            Double monthPlanDetailsBeanCount = 0.00;
                            if (!StringUtil.trimToEmpty(monthPlanDetailsBean.getCount()).equals("")) {
                                monthPlanDetailsBeanCount = Double.parseDouble(StringUtil.trimToEmpty(monthPlanDetailsBean.getCount()));
                            }
                            double remainCount = overallPlanDetailsBeanCount - monthPlanDetailsBeanCount;
                            overallPlanDetailsBean.setRemainCount(StringUtil.trimToEmpty(remainCount));
                        }
                    }
                }
            }

            for (OverallPlanDetailsBean overallPlanDetailsBean : overallPlanDetailsBeanAll) {
                if (overallPlanDetailsBean.getRemainCount() == null || overallPlanDetailsBean.getRemainCount().equals("")) {
                    overallPlanDetailsBean.setRemainCount(overallPlanDetailsBean.getCount());
                }
            }

            List<Stock> stockList = stockService.getStocks(cid);
            for (OverallPlanDetailsBean overallPlanDetailsBean : overallPlanDetailsBeanAll) {
                Double stockCount = 0.00;
                overallPlanDetailsBean.setStockCount(StringUtil.trimToEmpty(stockCount));
                for (Stock stock : stockList) {
                    if (Integer.parseInt(stock.getMaterialsId()) == Integer.parseInt(overallPlanDetailsBean.getMaterialsId())) {
                        overallPlanDetailsBean.setStockCount(stock.getStockCount());
                    }
                }
            }
        }

        return overallPlanDetailsBeanAll;
    }

    @Override
    public void updateOverallPlan(OverallPlan info) {
        OverallPlan overallPlan = overallPlanDao.get(OverallPlan.class, info.getId());
        BeanUtils.copyProperties(info, overallPlan);
    }

    @Override
    public void delOverallPlan(String overallPlanId) {
        if (overallPlanId != null) {
            try {
                OverallPlan overallPlan = overallPlanDao.get(" FROM OverallPlan where 1=1 and id = " + overallPlanId);
                if (overallPlan != null) {
                    overallPlan.setIsDelete("1");
                    updateOverallPlan(overallPlan);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updateOverallPlanDetails(OverallPlanDetails info) {
        OverallPlanDetails overallPlanDetails = overallPlanDetailsDao.get(OverallPlanDetails.class, info.getId());
        BeanUtils.copyProperties(info, overallPlanDetails);
    }

    @Override
    public void delOverallPlanDetails(String overallPlanDetailsId) {
        if (overallPlanDetailsId != null) {
            try {
                OverallPlanDetails overallPlanDetails = overallPlanDetailsDao.get(" FROM OverallPlanDetails where 1=1 and id = " + overallPlanDetailsId);
                if (overallPlanDetails != null) {
                    overallPlanDetails.setIsDelete("1");
                    updateOverallPlanDetails(overallPlanDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<OverallPlanBean> getApproveOverallPlanList(String cid, String currentApprovedUser, String projectId, String startDate, String endDate) {
        List<OverallPlanBean> overallPlanBeanList = new ArrayList<OverallPlanBean>();
        OverallPlanBean overallPlanBean = new OverallPlanBean();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String overallPlanHql = "from OverallPlan where isDelete = 0 and needApproved in ('1', '8') and cid = :cid ";
        if (!projectId.equals("")) {
            overallPlanHql += " and projectId = :projectId ";
            params.put("projectId", projectId);
        }
        if (!currentApprovedUser.equals("")) {
            overallPlanHql += " and currentApprovedUser = :currentApprovedUser ";
            params.put("currentApprovedUser", currentApprovedUser);
        }
        if (!startDate.equals("")) {
            overallPlanHql += " and createTime >= :startTime ";
            params.put("startTime", DateKit.strToDateOrTime(startDate));
        }
        if (!endDate.equals("")) {
            overallPlanHql += " and createTime <= :endTime";
            params.put("endTime", DateKit.strToDateOrTime(endDate));
        }

        List<OverallPlan> overallPlanList = overallPlanDao.find(overallPlanHql, params);

        if (overallPlanList != null && overallPlanList.size() > 0) {
            for (OverallPlan overallPlan : overallPlanList) {

                overallPlanBean = new OverallPlanBean();
                overallPlanBean.setId(overallPlan.getId());
                overallPlanBean.setCid(overallPlan.getCid());
                overallPlanBean.setProjectId(overallPlan.getProjectId());
                overallPlanBean.setProjectName(projectService.findOneView(Integer.parseInt(overallPlan.getProjectId())).getProName());
                overallPlanBean.setUid(overallPlan.getUid());

                User user = userService.getUser(StringUtil.trimToEmpty(overallPlan.getUid()));
                overallPlanBean.setUname(user.getRealname().equals("") ? user.getUsername() : user.getRealname());
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
    public void approveOverallPlan(Integer overallplanId, String approvedState, String approvedOption, String currentApprovedUser) {
        try {
            OverallPlan overallPlan =  overallPlanDao.get("from OverallPlan  where id = " + overallplanId);
            if (!overallPlan.getApprovedUser().equals("")) {
                String[] approvedUsers = overallPlan.getApprovedUser().split(",");
                if (approvedUsers != null && approvedUsers.length > 0) {
                    String lastApprovedUser = approvedUsers[approvedUsers.length - 1];
                    String userName = userService.get(lastApprovedUser).getUsername();
                    approvedOption = UtilDate.getDateFormatter() + "::" + userName + "::" + approvedOption;
                }

                if (approvedState.equals("8")) { // 需要查看是不是最终审批人，不是改为审批中
                    overallPlan.setApprovedUser(overallPlan.getApprovedUser() + "," + currentApprovedUser);
                    overallPlan.setCurrentApprovedUser(currentApprovedUser);

                    // 推送任务
//                    taskPushService.addFieldTaskPush(t);
                }
            }
            overallPlan.setNeedApproved(approvedState);


            overallPlan.setApprovedOption(StringUtil.trimToEmpty(overallPlan.getApprovedOption()).equals("") ? approvedOption : StringUtil.trimToEmpty(overallPlan.getApprovedOption()) + "|" + approvedOption);
            overallPlanDao.update(overallPlan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OverallPlanBean> getApproveOverallPlanListForApp(PageHelper ph, String currentApprovedUser) {
        List<OverallPlanBean> overallPlanBeanList = new ArrayList<OverallPlanBean>();
        OverallPlanBean overallPlanBean = new OverallPlanBean();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentApprovedUser", currentApprovedUser);
        String overallPlanHql = "from OverallPlan where isDelete = 0 and needApproved in ('1', '8') and and currentApprovedUser = :currentApprovedUser ";
        List<OverallPlan> overallPlanList = overallPlanDao.find(overallPlanHql, params, ph.getPage(), ph.getRows());

        if (overallPlanList != null && overallPlanList.size() > 0) {
            for (OverallPlan overallPlan : overallPlanList) {

                overallPlanBean = new OverallPlanBean();
                overallPlanBean.setId(overallPlan.getId());
                overallPlanBean.setCid(overallPlan.getCid());
                overallPlanBean.setProjectId(overallPlan.getProjectId());
                overallPlanBean.setProjectName(projectService.findOneView(Integer.parseInt(overallPlan.getProjectId())).getProName());
                overallPlanBean.setUid(overallPlan.getUid());

                User user = userService.getUser(StringUtil.trimToEmpty(overallPlan.getUid()));
                overallPlanBean.setUname(user.getRealname().equals("") ? user.getUsername() : user.getRealname());
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

    private List<OverallPlanDetailsBean> mergeList(List<OverallPlanDetailsBean> list) {
        HashMap<String, OverallPlanDetailsBean> map = new HashMap<String, OverallPlanDetailsBean>();
        for (OverallPlanDetailsBean bean : list) {
            if (map.containsKey(bean.getMaterialsId())) {
                String strOldCount = StringUtil.trimToEmpty(map.get(bean.getMaterialsId()).getCount());
                Double dOldCount = strOldCount.equals("") ? 0.00 : Double.parseDouble(strOldCount);

                String strNewCount = bean.getCount();
                Double dNewCount = strNewCount.equals("") ? 0.00 : Double.parseDouble(strNewCount);

                map.get(bean.getMaterialsId()).setCount(StringUtil.trimToEmpty(dOldCount + dNewCount));
            } else {
                map.put(bean.getMaterialsId(), bean);
            }
        }
        list.clear();
        list.addAll(map.values());

        return list;
    }
}
