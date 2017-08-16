package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.LocationDaoI;
import sy.model.po.Location;
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
    public Location addLocation(String cid, String mc) {
        Location location = new Location();
        location.setCid(cid);
        location.setMc(mc);
        locationDao.saveOrUpdate(location);

        return location;
    }

    @Override
    public void delLocation(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", Integer.parseInt(id));
        Location location = locationDao.get("from Location where id = :id ", params);
        locationDao.delete(location);
    }
}
