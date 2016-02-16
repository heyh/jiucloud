package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ProvinceDaoI;
import sy.model.Province;
import sy.service.ProvinceServiceI;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceServiceI {

	@Autowired
	private ProvinceDaoI provinceDao;

	@Override
	public List<Province> getProvinces() {
		String hql = "from Province order by id asc";
		return provinceDao.find(hql);
	}

	@Override
	public Province getProvinceByName(String name) {
		String hql = "from Province where name like '%" + name+"%'";
		List<Province> tem = provinceDao.find(hql);
		if (tem.size() > 0)
			return tem.get(0);
		return null;
	}
}
