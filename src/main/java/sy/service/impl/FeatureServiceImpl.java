package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.CostDaoI;
import sy.dao.FeatureDaoI;
import sy.model.po.Cost;
import sy.model.po.Feature;
import sy.model.po.Location;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.CostServiceI;
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

    @Autowired
    private CostDaoI costDao;

    @Autowired
    private CostServiceI costService;

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
    public Feature addFeature(String cid, String itemCode, String mc, String count, String dw) {
        Feature feature = new Feature();
        feature.setCid(cid);
        feature.setItemCode(itemCode);
        feature.setMc(mc);
        feature.setCount(count);
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

        List<Feature> featureList = featureDao.find(hql, params, ph.getPage(), ph.getRows());

        for (Feature feature : featureList) {
            if (!StringUtil.trimToEmpty(feature.getItemCode()).equals("")) {
                Cost cost = costService.getCostByCode(feature.getItemCode(), cid);
//                Cost cost = costDao.get("from Cost where cid = " + cid + " and isDelete=0 and itemCode='" + feature.getItemCode() + "'");
                if (cost == null) {
                    feature.setCostType("该费用类型可能已经被删除");
                } else {
                    feature.setCostType(cost.getCostType());
                }
            }
        }
        dg.setRows(featureList);
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

    @Override
    public Feature detail(String id) {
        Feature feature = new Feature();
        try {
            feature = featureDao.get(" FROM Feature t  where 1=1 and id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feature;
    }

    @Override
    public void update(Feature info) {
        Feature feature = featureDao.get(Feature.class, info.getId());
        BeanUtils.copyProperties(info, feature);
    }

    @Override
    public List<Feature> getFeaturesByItemCode(String cid, String itemCode, String keyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Feature where cid = :cid ";
        if (!StringUtil.trimToEmpty(itemCode).equals("")) {
            hql += " and itemCode = :itemCode";
            params.put("itemCode", itemCode);
        }

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
}
