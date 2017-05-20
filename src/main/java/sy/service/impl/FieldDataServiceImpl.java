package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.*;
import sy.model.Item;
import sy.model.po.Cost;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;
import sy.service.CostServiceI;
import sy.service.FieldDataServiceI;
import sy.service.ItemServiceI;
import sy.util.DateKit;
import sy.util.ObjectExcelRead;
import sy.util.StringUtil;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FieldDataServiceImpl implements FieldDataServiceI {

    @Autowired
    private FieldDataDaoI fieldDataDaoI;

    @Autowired
    private CostDaoI costDao;

    @Autowired
    private ProjectDaoI projectDao;

    @Autowired
    private UserDaoI userDao;

    @Autowired
    private ItemServiceI itemService;

    @Autowired
    private CostServiceI costService;

    @Override
    public TFieldData add(TFieldData tFieldData) {
        tFieldData.setCreatTime(new Date());
        fieldDataDaoI.save(tFieldData);
        return tFieldData;
    }

    @Override
    public DataGrid dataGrid(FieldData fieldData, PageHelper ph,
                             List<Integer> ugroup,String source, String keyword) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql="";
        if (source.equals("data"))
        {
            hql = " from TFieldData t  where isDelete=0 and (itemcode is null or itemcode='' or substring(itemcode,1,3)!='000' and substring(itemcode,1,3)<=900) ";
        }
        else if(source.equals("doc"))
        {
            hql = " from TFieldData t  where isDelete=0 and itemcode is not null and itemcode!='' and  (substring(itemcode,1,3)='000' or substring(itemcode,1,3)>900) ";
        }
        else
        {
            hql = " from TFieldData t  where isDelete=0  ";
        }

        hql += whereHql(fieldData, params, ugroup, keyword);

        List<TFieldData> l = fieldDataDaoI.find(hql, params, ph.getPage(), ph.getRows());
        dg.setTotal(fieldDataDaoI.count("select count(*) " + hql, params));
        List<FieldData> list = new ArrayList<FieldData>();
        for (TFieldData tem : l) {
            FieldData f = new FieldData();
            f.setId(tem.getId());
            f.setUid(tem.getUid());
            f.setCreatTime(tem.getCreatTime());
            f.setDataName(tem.getDataName());
            f.setPrice(tem.getPrice());
            f.setCompany(tem.getCompany());
            f.setCount(tem.getCount());
            f.setUnit(tem.getUnit());
            f.setSpecifications(tem.getSpecifications());
            f.setRemark(tem.getRemark());
            f.setUname(tem.getUname());
            f.setCost_id(Integer.parseInt(tem.getCostType()));
            f.setNeedApproved(tem.getNeedApproved());
            f.setItemCode(tem.getItemCode());
            f.setApprovedUser(tem.getApprovedUser());
            f.setCurrentApprovedUser(tem.getCurrentApprovedUser());
            f.setApprovedOption(tem.getApprovedOption());
            Item sectionItem = itemService.getSingleItem(tem.getCid(), tem.getProjectName(), tem.getSection());
            if (sectionItem == null) {
                f.setSectionName("标段1");
            } else {
                f.setSectionName(sectionItem.getName());
            }

            f.setSupplier(tem.getSupplier());

            Cost cost = costDao.get("from Cost where isDelete=0 and id='" + tem.getCostType() + "'");
            if (cost == null) {
                f.setCostType("该费用类型可能已经被删除");
            } else {
                f.setCostType(cost.getCostType());
            }
            f.setProject_id(Integer.parseInt(tem.getProjectName()));
            Project project = projectDao.get("from Project where isdel=0 and id='" + tem.getProjectName() + "'");
            if (project == null) {
                f.setProjectName("该工程可能已经被删除");
                continue; // 工程删除，记录不显示
            } else {
                f.setProjectName(project.getProName());
                f.setIsLock(project.getIsLock());
            }
            list.add(f);
        }
        // 0 不需要审批； 1 需要审批（显示未审批）
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("1") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        // 9 审批不通过
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("9") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        // footer
        if (source.equals("data")) {
            List<TFieldData> fields = fieldDataDaoI.find(hql, params);
            Double totalMoney = new Double(0.00);
            for (TFieldData tmpField : fields) {
                if (tmpField.getCount() != null && !tmpField.getCount().equals("") && StringUtil.isNum(tmpField.getCount()) &&
                        tmpField.getPrice() != null && !tmpField.getPrice().equals("") && StringUtil.isNum(tmpField.getPrice())) {
                    totalMoney += Double.parseDouble(tmpField.getCount()) * Double.parseDouble(tmpField.getPrice());
                }
            }
            List<FieldData> footerList = new ArrayList<FieldData>();
            FieldData fFooter = new FieldData();
            fFooter.setMoney(totalMoney == null ? 0.00 : totalMoney);
            fFooter.setCount("合计:");
            fFooter.setAction(true);
            footerList.add(fFooter);
            dg.setFooter(footerList);
        }

        dg.setRows(list);
        return dg;
    }

    @Override
    public List<TFieldData> getfindList(List<Integer> ugroup) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from TFieldData t  where isDelete=0 ";
        List<TFieldData> l = fieldDataDaoI.find(
                hql + whereHql(null, params, ugroup, ""), params, 0, 8);
        return l;
    }

    private String whereHql(FieldData cmodel, Map<String, Object> params,
                            List<Integer> ugroup, String keyword) {
        String hql = " ";
        if (cmodel != null) {
            if (cmodel.getUname() != null && cmodel.getUname().length() > 0) {
                hql += " and uname like :name ";
                params.put("name", "%%" + cmodel.getUname() + "%%");
            }
            if (cmodel.getProjectName() != null
                    && cmodel.getProjectName().length() > 0) {
                hql += " and (select proName from Project where id=t.projectName) like :proName ";
                params.put("proName", "%%" + cmodel.getProjectName() + "%%");
            }
//            if (cmodel.getCostType() != null
//                    && cmodel.getCostType().length() > 0) {
//                hql += " and (select costType from Cost where id=t.costType) like :costName ";
//                params.put("costName", "%%" + cmodel.getCostType() + "%%");
//            }
            if (cmodel.getItemCode() != null) {
                hql += " and itemCode like :itemCode ";
                params.put("itemCode", cmodel.getItemCode() + "%%");
            }
            if (cmodel.getStartTime() != null && cmodel.getStartTime().length() > 0) {
                hql += " and t.creatTime >= :startTime";
                params.put("startTime", DateKit.strToDateOrTime(cmodel.getStartTime()));
            }
            if (cmodel.getEndTime() != null && cmodel.getEndTime().length() > 0) {
                hql += " and t.creatTime <= :endTime";
                params.put("endTime", DateKit.strToDateOrTime(cmodel.getEndTime()));
            }
        }

        // add by heyh begin 模糊查询
        if (!keyword.equals("")) {
            hql += " and ( uname like :name ";
            params.put("name", "%%" + keyword + "%%");

            hql += " or (select proName from Project where id=t.projectName) like :proName ";
            params.put("proName", "%%" + keyword + "%%");

            hql += " or (select costType from Cost where id=t.costType) like :costName ";
            params.put("costName", "%%" + keyword + "%%");

            hql += " or dataName like :dataName ";
            params.put("dataName", "%%" + keyword + "%%");

            hql += " or (select name from Item where cid = t.cid and projectId = t.projectName and value=t.section) like :section ";
            params.put("section", "%%" + keyword + "%%");

            hql += " or supplier like :supplier )";
            params.put("supplier", "%%" + keyword + "%%");

            hql += " or remark like :remark ";
            params.put("remark", "%%" + keyword + "%%");

            hql += " or specifications like :specifications ";
            params.put("specifications", "%%" + keyword + "%%");

        }
        // add by heyh end

//        if (ugroup != null && ugroup.size() > 0) {
//            hql += " and uid in(" + ugroup.get(0).toString();
//            for (int i = 1; i < ugroup.size(); i++) {
//                hql += "," + ugroup.get(i).toString();
//            }
//            hql += ") ";
//        }
        if (null == cmodel.getNeedApproved() ) {
            String uids = StringUtils.join(ugroup, ",");
            hql += " and (uid in (" + uids + ") or ( (substring(itemcode , 1 , 3) = '700' or substring(itemcode , 1 , 3) = '800') and cid = :cid))";
            params.put("cid", String.valueOf(cmodel.getCid()));
        }

        if ( cmodel.getId() != 0) {
            hql += " and projectName = :id ";
            params.put("id", String.valueOf(cmodel.getId()));
        }
        if (null != cmodel.getNeedApproved() ) {
            hql += " and needApproved in ('1', '8')"; // 未审批、审批中
//            params.put("needApproved", cmodel.getNeedApproved());
        }
        hql += " order by t.id desc";
        return hql;
    }

    @Override
    public void delete(String id) {
        try {
            if (id != null) {
                TFieldData info = detail(id);
                if (info != null) {
                    info.setIsDelete(1);
                    update(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public TFieldData detail(String id) {
        if (id == null) {
            return null;
        }
        TFieldData tFieldData = fieldDataDaoI
                .get(" FROM TFieldData t  where 1=1 and isDelete=0 and id="
                        + id);
        return tFieldData;
    }

    @Override
    public void update(TFieldData info) {
        TFieldData fieldData = fieldDataDaoI
                .get(TFieldData.class, info.getId());
        BeanUtils.copyProperties(info, fieldData);
    }

    @Override
    public Integer getId(TFieldData fieldData) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("costType", fieldData.getCostType());
        List<Object[]> list = fieldDataDaoI.findBySql(
                "select id,dataName from TFieldData where costType="
                        + fieldData.getCostType() + " and dataName='"
                        + fieldData.getDataName() + "' and projectName="
                        + fieldData.getProjectName()
                        + " and isDelete=0 and uid=" + fieldData.getUid()
                        + " and cid=" + fieldData.getCid()
                        + " order by id desc", 1, 1);
        if (list != null) {
            Object[] o = list.get(0);
            System.out.println(o);
            return (Integer) o[0];
        }
        return 0;
    }

    /**
     * 审批
     * @param id
     */
    @Override
    public void approvedField(Integer id, String approvedState, String approvedOption, String currentApprovedUser) {
        try {
            TFieldData t = fieldDataDaoI.get("from TFieldData t where t.id = " + id);
            if (!t.getApprovedUser().equals("")) {
                String[] approvedUser = t.getApprovedUser().split(",");
//                if (approvedState.equals("8")) { // 需要查看是不是最终审批人，不是改为审批中
//                    String currentApprovedUser = t.getCurrentApprovedUser();
//                    if (!approvedUser[approvedUser.length-1].equals(currentApprovedUser)) { // 不是最终审批人
//                        for (int i=0; i<approvedUser.length; i++) {
//                            if (approvedUser[i].equals(currentApprovedUser)) {
//                                t.setCurrentApprovedUser(approvedUser[i+1]);
//                                break;
//                            }
//                        }
//                    } else {
//                        approvedState = "2";
//                    }
//                }
                if (approvedState.equals("8")) { // 需要查看是不是最终审批人，不是改为审批中
                    t.setApprovedUser(t.getApprovedUser() + "," + currentApprovedUser);
                    t.setCurrentApprovedUser(currentApprovedUser);
                }
            }

            t.setNeedApproved(approvedState);
            t.setApprovedOption(approvedOption);
            this.fieldDataDaoI.update(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TFieldData> getNeedApproveList(String currentApprovedUser) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentApprovedUser", currentApprovedUser);
        String hql = " from TFieldData t  where isDelete=0 and  needApproved in ('1','8') and currentApprovedUser = :currentApprovedUser";
        List<TFieldData> fieldDataList = fieldDataDaoI.find(hql, paramMap);
        return fieldDataList;
    }

    @Override
    public boolean hasSameFieldData(TFieldData fieldData) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cid", fieldData.getCid());
        paramMap.put("projectName", fieldData.getProjectName());
        paramMap.put("dataName", fieldData.getDataName());
        paramMap.put("price", fieldData.getPrice());
        paramMap.put("unit", fieldData.getUnit());
        paramMap.put("itemCode", fieldData.getItemCode());
        paramMap.put("section", fieldData.getSection());
        String hql = "from TFieldData where isDelete=0 and cid = :cid and projectName = :projectName and dataName = :dataName and price = :price and unit = :unit and itemCode = :itemCode and section = :section";
        List<TFieldData> fieldDataList = fieldDataDaoI.find(hql, paramMap);
        if (fieldDataList != null && fieldDataList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public DataGrid approveDataGrid(PageHelper ph, String currentApprovedUser) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql= " from TFieldData t  where isDelete=0 and  needApproved in ('1','8') and currentApprovedUser = :currentApprovedUser order by t.id desc";
        params.put("currentApprovedUser", currentApprovedUser);
        List<TFieldData> l = fieldDataDaoI.find(hql, params, ph.getPage(), ph.getRows());
        dg.setTotal(fieldDataDaoI.count("select count(*) " + hql, params));
        List<FieldData> list = new ArrayList<FieldData>();
        for (TFieldData tem : l) {
            FieldData f = new FieldData();
            f.setId(tem.getId());
            f.setUid(tem.getUid());
            f.setCreatTime(tem.getCreatTime());
            f.setDataName(tem.getDataName());
            f.setPrice(tem.getPrice());
            f.setCompany(tem.getCompany());
            f.setCount(tem.getCount());
            f.setUnit(tem.getUnit());
            f.setSpecifications(tem.getSpecifications());
            f.setRemark(tem.getRemark());
            f.setUname(tem.getUname());
            f.setCost_id(Integer.parseInt(tem.getCostType()));
            f.setNeedApproved(tem.getNeedApproved());
            f.setItemCode(tem.getItemCode());
            f.setApprovedUser(tem.getApprovedUser());
            f.setCurrentApprovedUser(tem.getCurrentApprovedUser());
            f.setApprovedOption(tem.getApprovedOption());

            Cost cost = costDao.get("from Cost where isDelete=0 and id='" + tem.getCostType() + "'");
            if (cost == null) {
                f.setCostType("该费用类型可能已经被删除");
            } else {
                f.setCostType(cost.getCostType());
            }
            f.setProject_id(Integer.parseInt(tem.getProjectName()));
            Project project = projectDao.get("from Project where isdel=0 and id='" + tem.getProjectName() + "'");
            if (project == null) {
                f.setProjectName("该工程可能已经被删除");
                continue; // 工程删除，记录不显示
            } else {
                f.setProjectName(project.getProName());
                f.setIsLock(project.getIsLock());
            }
            list.add(f);
        }
        // 0 不需要审批； 1 需要审批（显示未审批）
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("1") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        // 9 审批不通过
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("9") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        dg.setRows(list);
        return dg;
    }

    @Override
    public DataGrid myApproveDataGrid(PageHelper ph, String uid) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql= " from TFieldData t  where isDelete=0 and  needApproved != '0' and uid = :uid order by t.id desc";
        params.put("uid", uid);
        List<TFieldData> l = fieldDataDaoI.find(hql, params, ph.getPage(), ph.getRows());
        dg.setTotal(fieldDataDaoI.count("select count(*) " + hql, params));
        List<FieldData> list = new ArrayList<FieldData>();
        for (TFieldData tem : l) {
            FieldData f = new FieldData();
            f.setId(tem.getId());
            f.setUid(tem.getUid());
            f.setCreatTime(tem.getCreatTime());
            f.setDataName(tem.getDataName());
            f.setPrice(tem.getPrice());
            f.setCompany(tem.getCompany());
            f.setCount(tem.getCount());
            f.setUnit(tem.getUnit());
            f.setSpecifications(tem.getSpecifications());
            f.setRemark(tem.getRemark());
            f.setUname(tem.getUname());
            f.setCost_id(Integer.parseInt(tem.getCostType()));
            f.setNeedApproved(tem.getNeedApproved());
            f.setItemCode(tem.getItemCode());
            f.setApprovedUser(tem.getApprovedUser());
            f.setCurrentApprovedUser(tem.getCurrentApprovedUser());
            f.setApprovedOption(tem.getApprovedOption());

            Cost cost = costDao.get("from Cost where isDelete=0 and id='" + tem.getCostType() + "'");
            if (cost == null) {
                f.setCostType("该费用类型可能已经被删除");
            } else {
                f.setCostType(cost.getCostType());
            }
            f.setProject_id(Integer.parseInt(tem.getProjectName()));
            Project project = projectDao.get("from Project where isdel=0 and id='" + tem.getProjectName() + "'");
            if (project == null) {
                f.setProjectName("该工程可能已经被删除");
//                continue; // 工程删除，记录不显示
            } else {
                f.setProjectName(project.getProName());
                f.setIsLock(project.getIsLock());
            }
            list.add(f);
        }
        // 0 不需要审批； 1 需要审批（显示未审批）
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("1") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        // 9 审批不通过
        Collections.sort(list, new Comparator<FieldData>(){
            @Override
            public int compare(FieldData o1, FieldData o2) {
                String needApproved1 = o1.getNeedApproved() == null ? "0" : o1.getNeedApproved();
                String needApproved2 = o2.getNeedApproved() == null ? "0" : o2.getNeedApproved();
                if (needApproved1.equals("9") && !needApproved1.equals(needApproved2)) {
                    return -1;
                }
                return 0;
            }
        });

        dg.setRows(list);
        return dg;
    }

    @Override
    public TFieldData getMaxFieldByCidUid(String cid, String uid) {
        TFieldData rtnFieldData = new TFieldData();

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cid", cid);
        paramMap.put("uid", uid);

        String hql= "FROM TFieldData where id = (select max(id) from TFieldData a where isDelete=0 and cid = :cid and uid = :uid)";
        List<TFieldData> fieldDataList = fieldDataDaoI.find(hql, paramMap);

        if (fieldDataList != null && fieldDataList.size()>0) {
            rtnFieldData = fieldDataList.get(0);
        }
        return rtnFieldData;
    }

    @Override
    public TFieldData getFieldByMaxId(String cid, String uid, String projectId) {
        TFieldData rtnFieldData = new TFieldData();

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cid", cid);
        paramMap.put("uid", uid);
        paramMap.put("projectName",projectId);

        String hql= "FROM TFieldData where id = (select max(id) from TFieldData a where isDelete=0 and cid = :cid and uid = :uid and projectName = :projectName)";
        List<TFieldData> fieldDataList = fieldDataDaoI.find(hql, paramMap);

        if (fieldDataList != null && fieldDataList.size()>0) {
            rtnFieldData = fieldDataList.get(0);
        }
        return rtnFieldData;
    }

    @Override
    public List<TFieldData> getOutFieldByRelId(String relId) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("relId", relId);

        String hql= "FROM TFieldData where isDelete = '0' and relId = :relId";
        List<TFieldData> outFieldDataList = fieldDataDaoI.find(hql, paramMap);
        return  outFieldDataList;
    }

    @Override
    public List<Map<String, Object>> getMaterialDatas(String cid, String statDate, List<Integer> ugroup, Integer selDepartmentId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cid", cid);
        param.put("statDate", statDate);
        String uids = StringUtils.join(ugroup, ",");
        String sql1 = " SELECT a.itemCode, " +
                " SUM(if(a.count > 0, a.count * IF(ISNULL(a.price) or a.price='', 0, a.price), 0)) as inComeMonthly, " +
                " ABS(SUM(if(a.count < 0, a.count * IF(ISNULL(a.price) or a.price='', 0, a.price), 0))) as consumeMonthly " +
                " FROM TFieldData a " +
                " WHERE a.cid = :cid  " +
                " AND a.isDelete = '0' " +
                " AND LEFT(a.itemCode , 3) = '800'  " +
                " AND date_format(a.creatTime , '%Y-%m') = :statDate " +
                " AND uid in (" + uids + ")" +
                " GROUP BY a.itemCode ";
        List<Object[]> list1 = fieldDataDaoI.findBySql(sql1, param);

        String sql2 = " SELECT a.itemCode, " +
                " SUM(a.count * IF(ISNULL(a.price) or a.price='', 0, a.price)) as monthEndCarry " +
                " FROM TFieldData a " +
                " WHERE a.cid = :cid " +
                " AND a.isDelete = '0' " +
                " AND LEFT(a.itemCode , 3) = '800' " +
                " AND date_format(a.creatTime , '%Y-%m') <= :statDate " +
                " AND uid in (" + uids + ")" +
                " GROUP BY a.itemCode";
        List<Object[]> list2 = fieldDataDaoI.findBySql(sql2, param);


        String sql3 = " SELECT a.itemCode, " +
                " SUM(a.count * IF(ISNULL(a.price) or a.price='', 0, a.price)) as lastMonthCarryover " +
                " FROM TFieldData a " +
                " WHERE a.cid = :cid " +
                " AND a.isDelete = '0' " +
                " AND LEFT(a.itemCode , 3) = '800' " +
                " AND date_format(a.creatTime , '%Y-%m') < :statDate " +
                " AND uid in (" + uids + ")" +
                " GROUP BY a.itemCode";
        List<Object[]> list3 = fieldDataDaoI.findBySql(sql3, param);

        List<Map<String, Object>> materialStatInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> materialStatInfo = new HashMap<String, Object>();
        List<Cost> costList = costService.getMatrialsCostList(cid, selDepartmentId);
        if (costList != null && costList.size() > 0) {
            for (Cost cost : costList) {
                materialStatInfo = new HashMap<String, Object>();
                materialStatInfo.put("costType", cost.getCostType());
                for (Object[] temp : list1) {
                    if (cost.getItemCode().equals(temp[0])) {
                        materialStatInfo.put("inComeMonthly", temp[1]);
                        materialStatInfo.put("consumeMonthly", temp[2]);
                    }
                }

                for (Object[] temp : list2) {
                    if (cost.getItemCode().equals(temp[0])) {
                        materialStatInfo.put("monthEndCarry", temp[1]);
                    }
                }

                for (Object[] temp : list3) {
                    if (cost.getItemCode().equals(temp[0])) {
                        materialStatInfo.put("lastMonthCarryover", temp[1]);
                    }
                }

                materialStatInfos.add(materialStatInfo);
            }
        }
        return materialStatInfos;
    }

    @Override
    public List<FieldData> getBoq(String cid, String startDate, String endDate, List<Integer> ugroup, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "";
        if (type.equals("show")) {
            sql = "select id, itemCode, dataName, unit, remark, specifications, projectName, count, needApproved from TFieldData t  where isDelete=0 and substring(itemcode,1,3)='700' and cid=:cid ";
        } else {
            sql = "select id, itemCode, dataName, unit, remark, specifications, projectName, count, needApproved from TFieldData t  where isDelete=0 and substring(itemcode,1,3)='700' and cid=:cid and (needApproved ='0' or needApproved ='2') ";
        }
        params.put("cid", cid);

        if (!StringUtil.trimToEmpty(startDate).equals("")) {
            sql += " and t.creatTime >= :startTime ";
            params.put("startTime", DateKit.strToDateOrTime(startDate));
        }
        if (!StringUtil.trimToEmpty(endDate).equals("")) {
            sql += " and t.creatTime <= :endTime ";
            params.put("endTime", DateKit.strToDateOrTime(endDate));
        }

        String uids = StringUtils.join(ugroup, ",");
        sql += " and  uid in (" + uids + ") ";

        List<Object[]> l = fieldDataDaoI.findBySql(sql, params);
        List<FieldData> boq = new ArrayList<FieldData>();
        for (Object[] tem : l) {
            FieldData f = new FieldData();
            f.setId(Integer.parseInt(StringUtil.trimToEmpty(tem[0])));
            f.setItemCode(StringUtil.trimToEmpty(tem[1]));
            Cost cost = costDao.get("from Cost where isDelete=0 and itemCode='" + StringUtil.trimToEmpty(tem[1]) + "'");
            if (cost == null) {
                f.setCostType("该费用类型可能已经被删除");
            } else {
                f.setCostType(cost.getCostType());
            }
            String dataName = StringUtil.trimToEmpty(tem[2]);
            if (!dataName.contains(f.getCostType())) {
                dataName += dataName + "-" + f.getCostType();
            }
            f.setDataName(dataName);

            f.setUnit(StringUtil.trimToEmpty(tem[3]));
            f.setRemark(StringUtil.trimToEmpty(tem[4]));
            f.setSpecifications(StringUtil.trimToEmpty(tem[5]));
            f.setProject_id(Integer.parseInt(StringUtil.trimToEmpty(tem[6])));
            f.setCount(StringUtil.trimToEmpty(tem[7]));

            f.setProject_id(Integer.parseInt(StringUtil.trimToEmpty(tem[6])));
            Project project = projectDao.get("from Project where isdel=0 and id='" + Integer.parseInt(StringUtil.trimToEmpty(tem[6])) + "'");
            if (project == null) {
                continue; // 工程删除，记录不显示
            } else {
                f.setProjectName(project.getProName());
            }
            f.setNeedApproved(StringUtil.trimToEmpty(tem[8]));

            boq.add(f);
        }
        return boq;
    }

    @Override
    public void backFill(Integer id, String price, String feeType) {
        try {
            TFieldData tFieldData = fieldDataDaoI.get("from TFieldData t where t.id = " + id);
            if (tFieldData != null ) {
                if (feeType.equals("0")) {
                    tFieldData.setPrice_sj(price);
                } else {
                    tFieldData.setPrice_ys(price);
                }
                fieldDataDaoI.update(tFieldData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date string2date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2string(Date date) {
        return DateFormat.getDateInstance().format(date);
    }
}
