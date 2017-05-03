package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ParamDaoI;
import sy.model.Param;
import sy.service.ParamService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by heyh on 16/1/6.
 */
@Service
public class ParamServiceImpl implements ParamService{

    @Autowired
    private ParamDaoI paramDao;

    private ConcurrentMap<String, Object> pMap = new ConcurrentHashMap<String, Object>();

    @Override
    public Object getParams(String key, String value) {
        if (!pMap.containsKey(key + "|" + value)) {
            Map<String,Object> paramMap=new HashMap<String,Object>();
            List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
//            if(key.equals("UP")){
                Object result = getUnitParams(key);
                if(result != null) {
                    pMap.put(key + "|" + value, result);
                }
//            }
        }
        return pMap.containsKey(key + "|" + value) ? pMap.get(key + "|" + value) : null;
    }

    private List<Param> getUnitParams(String key) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("paramTypeCode", key);
        String hql = "from Param where paramTypeCode =:paramTypeCode order by id";
        List<Param> ups = paramDao.find(hql, paramMap);
        return ups;
    }
}
