package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CityDaol;
import sy.model.City;
import sy.service.CityServiceI;

import java.util.List;

@Service
public class CityServiceImpl implements CityServiceI {

	@Autowired
	private CityDaol cityDao;

	@Override
//	public List<S_city> getCities(int province_id) {
//		String hql = "from S_city";
//		if (province_id != 0) {
//			hql += " where province_id=" + province_id;
//		} else {
//			hql += " where province_id!=1";
//		}
//		return cityDao.find(hql);
//	}

    public List<City> getCities(String provincecode) {
        String hql = "from City where provincecode=" + provincecode;

        return cityDao.find(hql);
    }

    @Override
    public City getCityByName(String cityName) {
        String hql = "from City where name like '%" + cityName+"%'";
        List<City> tem = cityDao.find(hql);
        if (tem.size() > 0)
            return tem.get(0);
        return null;
    }
}
