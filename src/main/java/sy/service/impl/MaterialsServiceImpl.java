package sy.service.impl;

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
        String hql = "from Materials where pid = :pid";
        params.put("pid", pid);

        if (!keyword.equals("")) {
            hql += " and name like :name ";
            params.put("name", "%%" + keyword + "%%");
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

    private boolean hasChild(int id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", String.valueOf(id));
        String hql = "from Materials where pid = :id ";
        List<Materials> materialsList = materialsDao.find(hql, params);
        return ( materialsList == null || materialsList.size() == 0 ) ? false : true;
    }
}
