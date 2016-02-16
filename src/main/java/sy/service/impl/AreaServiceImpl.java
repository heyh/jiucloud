package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.AreaDaoI;
import sy.model.Area;
import sy.service.AreaServiceI;

import java.util.List;

/**
 * Created by heyh on 16/2/16.
 */
@Service
public class AreaServiceImpl implements AreaServiceI {

    @Autowired
    private AreaDaoI areaDao;

    @Override
    public List<Area> getAreas(String citycode) {
        String hql = "from Area where citycode=" + citycode;

        return areaDao.find(hql);
    }
}
