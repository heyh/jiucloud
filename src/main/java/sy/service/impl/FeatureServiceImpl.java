package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.FeatureDaoI;
import sy.model.po.Feature;
import sy.service.FeatureServiceI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/8/3.
 */

@Service
public class FeatureServiceImpl implements FeatureServiceI {

    @Autowired
    private FeatureDaoI featureDao;

    @Override
    public List<Feature> getFeatures(String cid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Feature where cid = :cid";
        List<Feature> features = featureDao.find(hql, params);
        return features;
    }

    @Override
    public Feature addFeature(String cid, String mc, String dw) {
        Feature feature = new Feature();
        feature.setCid(cid);
        feature.setMc(mc);
        feature.setDw(dw);
        featureDao.saveOrUpdate(feature);

        return feature;
    }

    @Override
    public void delFeature(String id) {
        Map params = new HashMap<String, Object>();
        params.put("id", Integer.parseInt(id));
        Feature feature = featureDao.get("from Feature where id=:id", params);
        featureDao.delete(feature);
    }
}
