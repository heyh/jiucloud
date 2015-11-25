package sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.CityDaol;
import sy.model.S_city;
import sy.service.CityServiceI;

@Service
public class CityServiceImpl implements CityServiceI {

	@Autowired
	private CityDaol cityDao;

	@Override
	public List<S_city> getCities(int province_id) {
		String hql = "from S_city";
		if (province_id != 0) {
			hql += " where province_id=" + province_id;
		} else {
			hql += " where province_id!=1";
		}
		return cityDao.find(hql);
	}
}
