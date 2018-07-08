package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.LocationDaoI;
import sy.model.Item;
import sy.model.po.Location;
import sy.model.po.Project;
import sy.pageModel.DataGrid;
import sy.service.LocationServiceI;
import sy.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/8/16.
 */

@Service
public class LocationServiceImpl implements LocationServiceI {

    @Autowired
    private LocationDaoI locationDao;

    @Override
    public List<Location> getLocations(String cid, List<Integer> ugroup, String keyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Location where cid = :cid ";
        if (!StringUtil.trimToEmpty(keyword).equals("")) {
            hql += " and mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");
        }

        String uids = StringUtils.join(ugroup, ",");
        hql += " and uid in (" + uids + ")";

        List<Location> locations = locationDao.find(hql, params);
        return locations;
    }

    @Override
    public List<Location> getLocations(String cid, String keyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Location where cid = :cid ";
        if (!StringUtil.trimToEmpty(keyword).equals("")) {
            hql += " and mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");
        }

        List<Location> locations = locationDao.find(hql, params);
        return locations;
    }

    @Override
    public List<Location> getLocationsByName(String cid, String mc) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("mc", mc);
        String hql = "from Location where cid = :cid and mc = :mc ";
        List<Location> locations = locationDao.find(hql, params);

        return locations;
    }

    @Override
    public Location addLocation(String cid, String uid, String mc) {
        Location location = new Location();
        location.setCid(cid);
        location.setUid(uid);
        location.setMc(mc);
        locationDao.save(location);

        return location;
    }

    @Override
    public void delLocation(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", Integer.parseInt(id));
        Location location = locationDao.get("from Location where id = :id ", params);
        locationDao.delete(location);
    }

    @Override
    public DataGrid dataGrid(String cid, List<Integer> ugroup) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Location where cid = :cid ";
        String uids = StringUtils.join(ugroup, ",");
        hql += " and uid in (" + uids + ")";

        List<Location> locations = locationDao.find(hql, params);
        dg.setTotal(locationDao.count("select count(*) " + hql, params));
        dg.setRows(locations);
        return dg;
    }

    @Override
    public Location detail(String id) {
        Location location = new Location();
        try {
            location = locationDao.get(" FROM Location t  where 1=1 and id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void update(Location info) {
        Location location = locationDao.get(Location.class, info.getId());
        BeanUtils.copyProperties(info, location);
    }
}
