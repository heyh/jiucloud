package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ParamDaoI;
import sy.model.Param;
import sy.service.ParamService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 16/1/6.
 */
@Service
public class ParamServiceImpl implements ParamService{

    @Autowired
    private ParamDaoI paramDao;

    @Override
    public List<Param> getUnitParams() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("paramTypeCode", "UP");
        String hql = "from Param where paramTypeCode =:paramTypeCode";
        List<Param> ups = paramDao.find(hql, paramMap);
        return ups;
    }
}
