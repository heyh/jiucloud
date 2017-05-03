package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CompanyDaoI;
import sy.model.po.Company;
import sy.service.CompanyServiceI;
import sy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyServiceI {

	@Autowired
	CompanyDaoI companyDao;

//	@Override
//	public Company findOneView(String uid,String cid1) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		if(cid1==null){
////			List<Object[]> object_value = this.companyDao
////			.findBySql("select user_id,company_id from jsw_corporation_department where user_id='"
////					+ uid + "'");
//            List<Object[]> object_value = this.companyDao.findBySql("select user_id,company_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
//			if (object_value != null && object_value.size() != 0) {
//				int cid = Integer.parseInt(object_value.get(0)[1].toString()); // ovObjects[0];
//				params.put("cid", cid);
//				Company company = companyDao.get("from Company where id=:cid", params);
//				return company;
//			}
//		}
//			int cid = Integer.parseInt(cid1); // ovObjects[0];
//			params.put("cid", cid);
//			Company company = companyDao.get("from Company where id=:cid",
//					params);
//			return company;
//	}

    @Override
    public Company findOneView(String uid,String cid1) {
        Company company = new Company();
        Map<String, Object> params = new HashMap<String, Object>();
        if(cid1==null){
            List<Object[]> object_value = this.companyDao.findBySql("select user_id,company_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
            if (object_value != null && object_value.size() > 0) {
                for (Object[] object : object_value) {
                    int cid = Integer.parseInt(String.valueOf(object[1])); // ovObjects[0];
                    params.put("cid", cid);
                    company = companyDao.get("from Company where id=:cid", params);
                    if (company != null) {
                        return company;
                    }
                }
            }
        } else {
            int cid = Integer.parseInt(cid1); // ovObjects[0];
            params.put("cid", cid);
            company = companyDao.get("from Company where id=:cid",params);
        }

        return company;
    }

    @Override
    public List<Company> getCompanyInfos(String uid, String cid) {
        List<Company> companyList = new ArrayList<Company>();
        Company company = new Company();
        Map<String, Object> params = new HashMap<String, Object>();
        if (cid == null || cid.equals("")) {
            List<Object[]> object_value = this.companyDao.findBySql("select distinct company_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
            if (object_value != null && object_value.size() > 0) {
                for (Object object : object_value) {
                    if(!StringUtil.trimToEmpty(object).equals("")) {
                        int _cid = Integer.parseInt(String.valueOf(object)); // ovObjects[0];
                        params = new HashMap<String, Object>();
                        company = new Company();
                        params.put("cid", _cid);
                        company = companyDao.get("from Company where id=:cid", params);
                        if (company != null) {
                            companyList.add(company);
                        }
                    }
                }
            }
        } else {
            params.put("cid", Integer.parseInt(cid));
            company = new Company();
            company = companyDao.get("from Company where id=:cid", params);
            companyList.add(company);
        }
        return companyList;
    }
}
