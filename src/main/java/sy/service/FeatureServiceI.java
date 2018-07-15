package sy.service;

import sy.model.po.Feature;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2017/8/3.
 */
public interface FeatureServiceI {

    List<Feature> getFeatures(String cid, List<Integer> ugroup, String keyword);

    List<Feature> getFeatures(String cid, String keyword);

    Feature addFeature(String cid, String uid, String mc, String count, String dw);

    void delFeature(String id);

    DataGrid dataGrid(PageHelper ph, String cid, String keyword, String itemCode);

    List<Feature> getFeatureList(String cid, String mc, String dw);

    Feature detail(String id);

    void update(Feature info);
}
