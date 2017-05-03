package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ParamTransDaoI;
import sy.model.ParamTrans;
import sy.model.po.Cost;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;
import sy.service.ParamTransServiceI;
import sy.util.StringUtil;

import java.util.*;

/**
 * Created by heyh on 2016/12/25.
 */
@Service("paramTransService")
public class ParamTransServiceImpl implements ParamTransServiceI {

    @Autowired
    private ParamTransDaoI paramTransDao;

    @Override
    public List<ParamTrans> getCostTransList() {
        String hql = "from ParamTrans where param_type='costTrans'";
        return paramTransDao.find(hql);
    }

    @Override
    public List<ParamTrans> getParamTransListByCid(String cid, String paramType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("paramType", paramType);
        String hql = "from ParamTrans where param_type=:paramType and cid=:cid";
        return paramTransDao.find(hql, params);
    }

    @Override
    public DataGrid dataGrid(String cid, String paramType, PageHelper pageHelper) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("paramType", paramType);
        String hql = "from ParamTrans where param_type=:paramType and cid = :cid order by trans_param_code asc";


        List<ParamTrans> paramTranss = paramTransDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        dg.setTotal(paramTransDao.count("select count(*) " + hql, params));
        dg.setRows(paramTranss);
        return dg;
    }

    @Override
    public ParamTrans add(ParamTrans paramTrans) {
        paramTransDao.save(paramTrans);
        return paramTrans;
    }

    @Override
    public void update(ParamTrans info) {
        ParamTrans paramTrans = paramTransDao.get(ParamTrans.class, info.getId());
        BeanUtils.copyProperties(info, paramTrans);
    }

    @Override
    public void delete(String id) {
        try {
            ParamTrans paramTrans = paramTransDao.get(" FROM ParamTrans t  where 1=1 and id=" + id);
            paramTransDao.delete(paramTrans);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ParamTrans detail(String id) {
        ParamTrans paramTrans = new ParamTrans();
        try {
            paramTrans = paramTransDao.get(" FROM ParamTrans t  where 1=1 and id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramTrans;
    }

    @Override
    public DataGrid execlDataGrid(String cid, PageHelper pageHelper) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from ParamTrans where param_type='execlCostTrans' and cid = :cid order by trans_param_code asc";
        params.put("cid", cid);

        List<ParamTrans> paramTranss = paramTransDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        dg.setTotal(paramTransDao.count("select count(*) " + hql, params));
        dg.setRows(paramTranss);
        return dg;
    }

}
