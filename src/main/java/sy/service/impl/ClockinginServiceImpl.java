package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ClockinginDaol;
import sy.model.Clockingin;
import sy.model.Item;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.User;
import sy.service.ClockinginServiceI;
import sy.service.UserServiceI;
import sy.util.DateKit;
import sy.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/9/16.
 */

@Service
public class ClockinginServiceImpl implements ClockinginServiceI {

    @Autowired
    private ClockinginDaol clockinginDao;

    @Autowired
    private UserServiceI userService;

    @Override
    public Clockingin Clockingin(Clockingin clockingin) {
        clockinginDao.save(clockingin);
        return clockingin;
    }

    @Override
    public List<Clockingin> getClockingins(List<Integer> ugroup) {
        return null;
    }

    @Override
    public int approveClockingin(String approveState) {
        return 0;
    }

    @Override
    public DataGrid dataGrid(String cid, List<Integer> ugroup, Clockingin clockingin, PageHelper pageHelper) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "";
        hql = "from Clockingin where cid=:cid and isDelete = '0' ";
        params.put("cid", cid);

        String uids = StringUtils.join(ugroup, ",");
        hql += " and uid in (" + uids + ") ";

        hql += " and clockinginTime >= :startTime";
        params.put("startTime", DateKit.strToDateOrTime(clockingin.getStartDate()));

        hql += " and clockinginTime <= :endTime";
        params.put("endTime", DateKit.strToDateOrTime(clockingin.getEndDate()));

        if (!StringUtil.trimToEmpty(clockingin.getUname()).equals("")) {
            hql += " and uname like :name ";
            params.put("name", "%%" + clockingin.getUname() + "%%");
        }

        if (!StringUtil.trimToEmpty(clockingin.getUid()).equals("")) {
            hql += " and uid = :uid ";
            params.put("uid", clockingin.getUid());
        }

        List<Clockingin> clockingins = clockinginDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        for (Clockingin c : clockingins) {
            if (!StringUtil.trimToEmpty(c.getUid()).equals("")) {
                User user = userService.getUser(c.getUid());
                String uname = user.getRealname().equals("") ? user.getUsername() : user.getRealname();
                c.setUname(uname);
            }
        }

        dg.setTotal(clockinginDao.count("select count(*) " + hql, params));
        dg.setRows(clockingins);
        return dg;
    }

    @Override
    public Clockingin getClockingin(int id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Clockingin c = clockinginDao.get(" from Clockingin where id = :id", params);
        Clockingin clockingin = new Clockingin();
        if (c != null) {
            BeanUtils.copyProperties(c, clockingin);
        }
        return clockingin;
    }

    @Override
    public void update(Clockingin info) {
        Clockingin clockingin = clockinginDao.get(Clockingin.class, info.getId());
        BeanUtils.copyProperties(info, clockingin);
    }


    @Override
    public void delete(int id) {
        try {
            Clockingin info = getClockingin(id);
            if (info != null) {
                info.setIsDelete("1");
                update(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
