package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.MaterialsDaoI;
import sy.model.po.JswZjsproject;
import sy.model.po.Materials;
import sy.pageModel.DataGrid;
import sy.pageModel.MaterialsTree;
import sy.pageModel.PageHelper;
import sy.service.MaterialsServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/2.
 */

@Service
public class MaterialsServiceImpl implements MaterialsServiceI {

    @Autowired
    private MaterialsDaoI materialsDao;

    @Override
    public DataGrid dataGrid(PageHelper pageHelper, String keyword, String pid) {
        DataGrid dataGrid = new DataGrid();
        List<MaterialsTree> materialsTreeList = new ArrayList<MaterialsTree>();
        MaterialsTree materialsTree = new MaterialsTree();

        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from Materials where";

        if (!keyword.equals("")) {
            hql += " mc like :mc ";
            params.put("mc", "%%" + keyword + "%%");
        } else {
            hql += " pid = :pid ";
            params.put("pid", pid);
        }

        hql += " order by id";

        List<Materials> materialsList = materialsDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        if (materialsList != null && materialsList.size()>0) {
            for (Materials materials : materialsList) {
                materialsTree = new MaterialsTree();
                materialsTree.setId(materials.getId());
                materialsTree.setCid(materials.getCid());
                materialsTree.setSpecifications(materials.getSpecifications());
                materialsTree.setMc(materials.getMc());
                materialsTree.setDw(materials.getDw());
                materialsTree.setPid(materials.getPid());
                materialsTree.setState(hasChild(materials.getId()) ? "closed" : "open");
                materialsTree.setIconCls("iconCls");

                materialsTreeList.add(materialsTree);
            }
        }

        dataGrid.setRows(materialsTreeList);
        dataGrid.setTotal(materialsDao.count("select count(*) " + hql, params));

        return dataGrid;

    }

    @Override
    public Materials findById(int id) {
        Materials materials = materialsDao.get("from Materials m where m.id = " + id);
        return materials;
    }

    @Override
    public Materials addNode(String cid, String pid, String mc, String specifications, String dw) {
        Materials materials = new Materials();
        materials.setCid(cid);
        materials.setPid(pid);
        materials.setMc(mc);
        materials.setSpecifications(specifications);
        materials.setDw(dw);
        materialsDao.save(materials);
        return materials;
    }

    @Override
    public void delNode(String id) {
        Map params = new HashMap<String, Object>();
        params.put("id", Integer.parseInt(id));
        Materials materials = materialsDao.get(" from Materials where id = :id ", params);
        materialsDao.delete(materials);
    }

    @Override
    public void updateNode(Materials info) {
        Materials materials = materialsDao.get(Materials.class, info.getId());
        BeanUtils.copyProperties(info, materials);
    }

    private boolean hasChild(int id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", String.valueOf(id));
        String hql = "from Materials where pid = :id ";
        List<Materials> materialsList = materialsDao.find(hql, params);
        return ( materialsList == null || materialsList.size() == 0 ) ? false : true;
    }
}
