package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.DiscussDaoI;
import sy.model.po.Discuss;
import sy.service.DiscussServiceI;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/4/6.
 */
@Service
public class DiscussServiceImpl implements DiscussServiceI {

    @Autowired
    private DiscussDaoI discussDao;

    @Override
    public List<Discuss> getDiscussList(String discussId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("discussId", discussId);
        String hql = "from Discuss where discussId = :discussId and isDel = '0' order by createTime asc";
        return discussDao.find(hql, param);
    }

    @Override
    public Discuss addDiscuss(Discuss discuss) {
        discuss.setCreateTime(new Date());
        discussDao.save(discuss);
        return discuss;
    }

    @Override
    public void delDiscuss(String id) {
        try {
            if (id != null) {
                Discuss info = detail(id);
                if (info != null) {
                    info.setIsDel(1);
                    update(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Discuss detail(String id) {
        if (id == null) {
            return null;
        }
        Discuss discuss = discussDao.get("FROM Discuss t where 1 = 1 and isDel = 0 and id= " + id);
        return discuss;
    }

    @Override
    public void update(Discuss info) {
        Discuss discuss = discussDao.get(Discuss.class, info.getId());
        BeanUtils.copyProperties(info, discuss);
    }
}

