package sy.service;

import sy.model.po.Feature;

import java.util.List;

/**
 * Created by heyh on 2017/8/3.
 */
public interface FeatureServiceI {

    List<Feature> getFeatures(String cid);

    Feature addFeature(String cid, String mc, String dw);

    void delFeature(String id);
}
