package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ClockinginTimeDaoI;
import sy.model.Clockingin;
import sy.model.po.ClockinginTime;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.ClockinginTimeServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/10/9.
 */

@Service
public class ClockinginTimeServiceImpl implements ClockinginTimeServiceI {

    @Autowired
    private ClockinginTimeDaoI clockinginTimeDao;

    @Override
    public List<ClockinginTime> getClockinginTimeList(String cid) {
        List<ClockinginTime> clockinginTimeList = new ArrayList<ClockinginTime>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);

        String hql = " from ClockinginTime where cid = :cid ";

        clockinginTimeList = clockinginTimeDao.find(hql, params);
        return clockinginTimeList;
    }

    @Override
    public DataGrid dataGrid(String cid, PageHelper pageHelper) {
        DataGrid dg = new DataGrid();

        String hql = " from ClockinginTime where cid = :cid ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);

        List<ClockinginTime> clockinginTimeList = clockinginTimeDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());

        dg.setTotal(clockinginTimeDao.count("select count(*) " + hql, params));
        dg.setRows(clockinginTimeList);

        return dg;
    }

    @Override
    public ClockinginTime getClockinginTime(int id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        ClockinginTime c = clockinginTimeDao.get(" from ClockinginTime where id = :id", params);
        ClockinginTime clockinginTime = new ClockinginTime();
        if (c != null) {
            BeanUtils.copyProperties(c, clockinginTime);
        }
        return clockinginTime;
    }


    @Override
    public void update(ClockinginTime info) {
        ClockinginTime clockinginTime = clockinginTimeDao.get(ClockinginTime.class, info.getId());
        BeanUtils.copyProperties(info, clockinginTime);
    }
}
