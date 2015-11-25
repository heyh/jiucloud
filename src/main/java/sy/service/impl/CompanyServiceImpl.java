package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.CompanyDaoI;
import sy.model.po.Company;
import sy.service.CompanyServiceI;

@Service
public class CompanyServiceImpl implements CompanyServiceI {

	@Autowired
	CompanyDaoI companyDao;

	@Override
	public Company findOneView(String uid,String cid1) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(cid1==null){
			List<Object[]> object_value = this.companyDao
			.findBySql("select user_id,company_id from jsw_corporation_department where user_id='"
					+ uid + "'");
			if (object_value != null && object_value.size() != 0) {
				int cid = Integer.parseInt(object_value.get(0)[1].toString()); // ovObjects[0];
				params.put("cid", cid);
				Company company = companyDao.get("from Company where id=:cid",
						params);
				return company;
			}
		}
			int cid = Integer.parseInt(cid1); // ovObjects[0];
			params.put("cid", cid);
			Company company = companyDao.get("from Company where id=:cid",
					params);
			return company;
	}
}
