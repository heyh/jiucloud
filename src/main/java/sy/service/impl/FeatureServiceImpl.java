package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.FeatureDaoI;
import sy.model.po.Feature;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.FeatureServiceI;
import sy.util.StringUtil;

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
    public List<Feature> getFeatures(String cid, List<Integer> ugroup, String keyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Feature where cid = :cid ";
        if (!StringUtil.trimToEmpty(keyword).equals("")) {
            hql += " and (mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");

            hql += " or dw like :dw )";
            params.put("dw", "%%" + keyword + "%%");
        }

        hql += " ORDER BY ID ASC";
        List<Feature> features = featureDao.find(hql, params);
        return features;
    }

    @Override
    public List<Feature> getFeatures(String cid, String keyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Feature where cid = :cid ";
        if (!StringUtil.trimToEmpty(keyword).equals("")) {
            hql += " and (mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");

            hql += " or dw like :dw )";
            params.put("dw", "%%" + keyword + "%%");
        }
        hql += " ORDER BY ID ASC";

        List<Feature> features = featureDao.find(hql, params);
        return features;
    }

    @Override
    public Feature addFeature(String cid, String itemCode, String mc, String dw) {
        Feature feature = new Feature();
        feature.setCid(cid);
        feature.setItemCode(itemCode);
        feature.setMc(mc);
        feature.setDw(dw);
        featureDao.save(feature);

        return feature;
    }

    @Override
    public void delFeature(String id) {
        Map params = new HashMap<String, Object>();
        params.put("id", Integer.parseInt(id));
        Feature feature = featureDao.get("from Feature where id=:id", params);
        featureDao.delete(feature);
    }

    @Override
    public DataGrid dataGrid(PageHelper ph, String cid, String keyword, String itemCode) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Feature where cid = :cid";

        if (!keyword.equals("")) {
            hql += " and ( mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");

            hql += " and dw like :dw) ";
            params.put("dw", "%%" + keyword + "%%");
        }
        if (!itemCode.equals("")) {
            hql += "and itemCode = :itemCode ";
            params.put("itemCode", itemCode);
        }
        hql += " order by id desc";

        List<Feature> l = featureDao.find(hql, params, ph.getPage(), ph.getRows());
        dg.setRows(l);
        dg.setTotal(featureDao.count("select count(*) " + hql, params));

        return dg;
    }

    @Override
    public List<Feature> getFeatureList(String cid, String mc, String dw) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("mc", mc);
        params.put("dw", dw);
        String hql = "from Feature where cid = :cid and mc = :mc and dw = :dw ";
        List<Feature> featureList = featureDao.find(hql, params);
        return featureList;
    }
}
