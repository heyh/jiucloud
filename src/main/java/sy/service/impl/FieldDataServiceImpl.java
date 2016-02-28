package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CostDaoI;
import sy.dao.FieldDataDaoI;
import sy.dao.ProjectDaoI;
import sy.model.po.Cost;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;
import sy.service.FieldDataServiceI;
import sy.util.DateKit;
import sy.util.StringUtil;

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

    @Override
    public TFieldData add(TFieldData tFieldData) {
        tFieldData.setCreatTime(new Date());
        fieldDataDaoI.save(tFieldData);
        return tFieldData;
    }

    @Override
    public DataGrid dataGrid(FieldData fieldData, PageHelper ph,
                             List<Integer> ugroup,String source) {
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

        hql += whereHql(fieldData, params, ugroup);

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
                hql + whereHql(null, params, ugroup), params, 0, 8);
        return l;
    }

    private String whereHql(FieldData cmodel, Map<String, Object> params,
                            List<Integer> ugroup) {
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
            if (cmodel.getCostType() != null
                    && cmodel.getCostType().length() > 0) {
                hql += " and (select costType from Cost where id=t.costType) like :costName ";
                params.put("costName", "%%" + cmodel.getCostType() + "%%");
            }
            if (cmodel.getStartTime() != null
                    && cmodel.getStartTime().length() > 0) {
                hql += " and t.creatTime >= :startTime";
                // modify by heyh
//                params.put("startTime", this.string2date(cmodel.getStartTime()));
                params.put("startTime", DateKit.strToDateOrTime(cmodel.getStartTime()));
            }
            if (cmodel.getEndTime() != null && cmodel.getEndTime().length() > 0) {
                hql += " and t.creatTime <= :endTime";
                // modify by heyh
//                params.put("endTime", this.string2date(cmodel.getEndTime()));
                params.put("endTime", DateKit.strToDateOrTime(cmodel.getEndTime()));
            }
        }
        if (ugroup != null && ugroup.size() > 0) {
            hql += " and uid in(" + ugroup.get(0).toString();
            for (int i = 1; i < ugroup.size(); i++) {
                hql += "," + ugroup.get(i).toString();
            }
            hql += ") ";
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
    public void approvedField(Integer id, String approvedState) {
        try {
            TFieldData t = fieldDataDaoI.get("from TFieldData t where t.id = " + id);
            if (!t.getApprovedUser().equals("")) {
                String[] approvedUser = t.getApprovedUser().split(",");
                if (approvedState.equals("2")) { // 需要查看是不是最终审批人，不是改为审批中
                    String currentApprovedUser = t.getCurrentApprovedUser();
                    if (!approvedUser[approvedUser.length-1].equals(currentApprovedUser)) { // 不是最终审批人
                        approvedState = "8";
                        for (int i=0; i<approvedUser.length; i++) {
                            if (approvedUser[i].equals(currentApprovedUser)) {
                                t.setCurrentApprovedUser(approvedUser[i+1]);
                            }
                        }
                    } else {
                        t.setCurrentApprovedUser("-1");
                    }
                }
            }


            t.setNeedApproved(approvedState);
            this.fieldDataDaoI.update(t);
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
