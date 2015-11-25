package sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.ProvinceDaol;
import sy.model.S_province;
import sy.service.ProvinceServiceI;

@Service
public class ProvinceServiceImpl implements ProvinceServiceI {

	@Autowired
	private ProvinceDaol provinceDao;

	@Override
	public List<S_province> getProvinces() {
		String hql = "from S_province";
		return provinceDao.find(hql);
	}

	@Override
	public S_province getProvinceByName(String name) {
		String hql = "from S_province where province_name='" + name + "'";
		List<S_province> tem = provinceDao.find(hql);
		if (tem.size() > 0)
			return tem.get(0);
		return null;
	}
}
