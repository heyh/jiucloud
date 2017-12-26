package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CostDaoI;
import sy.dao.ProjectDaoI;
import sy.model.Param;
import sy.model.po.Cost;
import sy.model.po.Project;
import sy.service.CacheServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by heyh on 2017/12/26.
 */

@Service
public class CacheServiceImpl implements CacheServiceI{

    private ConcurrentMap<String, Object> pMap = new ConcurrentHashMap<String, Object>();

    @Autowired
    private CostDaoI costDao;

    @Autowired
    private ProjectDaoI projectDao;

    @Override
    public Object getCache(String key, String value) {
        if (!pMap.containsKey(key + "|" + value)) {
            Map<String,Object> paramMap=new HashMap<String,Object>();
            List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
            Object result = new Object();
            if (key.equals("cost")) {
                result = getCost(value);
            } else if (key.equals("project")) {
                result = getProject(value);
            }
            if(result != null) {
                pMap.put(key + "|" + value, result);
            }
        }
        return pMap.containsKey(key + "|" + value) ? pMap.get(key + "|" + value) : null;
    }

    private Cost getCost(String itemCode) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("itemCode", itemCode);
        String hql = " from Cost where isDelete=0 and itemCode=:itemCode ";
        Cost cost = costDao.get(hql, paramMap);
        return cost;
    }

    private Project getProject(String projectId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("projectId", Integer.parseInt(projectId));
        String hql = " from Project where isdel=0 and id=:projectId ";
        Project project = projectDao.get(hql, paramMap);
        return project;
    }
}
