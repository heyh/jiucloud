package sy.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ItemDaol;
import sy.dao.ProjectDaoI;
import sy.model.Item;
import sy.model.ParamTrans;
import sy.model.po.Project;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.Section;
import sy.service.ItemServiceI;

import java.util.*;

/**
 * Created by heyh on 2017/2/18.
 */

@Service("itemService")
public class ItemServiceImpl implements ItemServiceI {

    @Autowired
    private ItemDaol itemDao;

    @Autowired
    private ProjectDaoI projectDao;

    @Override
    public DataGrid dataGrid(String cid, String keyword, PageHelper pageHelper) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Item where cid = :cid";
        if (!keyword.equals("")) {
            hql += " and projectId = :projectId ";
            params.put("projectId", keyword);
        }
        hql += " order by projectId, id";
        List<Item> itemList = itemDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        for (Item item : itemList) {
           if (item.getProjectName() == null || item.getProjectName().equals("")) {
               Project project = projectDao.get("from Project where isdel=0 and id='" + item.getProjectId() + "'");
               item.setProjectName(project.getProName());
           }
        }
        dg.setTotal(itemDao.count("select count(*) " + hql, params));
        dg.setRows(itemList);
        return dg;
    }

    @Override
    public Item add(Item item) {
        itemDao.save(item);
        return item;
    }

    @Override
    public void update(Item info) {
        Item item = itemDao.get(Item.class, info.getId());
        BeanUtils.copyProperties(info, item);
    }

    @Override
    public void delete(String id) {
        try {
            Item item = itemDao.get(" FROM Item t  where 1=1 and id=" + id);
            itemDao.delete(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item detail(String id) {
        Item item = new Item();
        try {
            item = itemDao.get(" FROM Item t  where 1=1 and id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Section section = new Section();
//        if (item != null) {
//            section.setId(item.getId());
//            section.setCid(item.getCid());
//            section.setName(item.getName());
//            section.setValue(item.getValue());
//            section.setProjectId(item.getProjectId());
//            Project project = projectDao.get("from Project where isdel=0 and id='" + item.getProjectId() + "'");
//            section.setProjectName(project.getProName());
//
//        }
        return item;
    }

    @Override
    public List<Item> getDefaultItems() {
        String hql = "from Item where cid='-1' and projectId='-1'";
        return itemDao.find(hql);
    }

    @Override
    public List<Item> getItems(String cid, String projectId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("projectId", projectId);

        String hql = "from Item where cid=:cid and projectId=:projectId";
        return itemDao.find(hql, params);
    }

    @Override
    public List<Map<String, Object>> getSelectItems(String cid, String projectId) {

        List<Map<String, Object>> _items = new ArrayList<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("projectId", projectId);

        String hql = "from Item where cid=:cid and projectId=:projectId";
        List<Item> itemList = itemDao.find(hql, params);

        if (itemList.size() > 0) {
            Map<String, Object> _item = new HashMap<String, Object>();
            for (Item item : itemList) {
                _item = new HashMap<String, Object>();
                _item.put("id", item.getValue());
                _item.put("text", item.getName());
                _items.add(_item);
            }
        }

        return _items;
    }

    @Override
    public Item getSingleItem(String cid, String projectId, String value) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("projectId", projectId);
        params.put("value", value);

        String hql = "from Item where cid=:cid and projectId=:projectId and value=:value";
        List<Item> itemList = itemDao.find(hql, params);
        if (itemList.size()>0) {
            return itemList.get(0);
        }

        return null;
    }

    @Override
    public List<String> getSupInfos(String cid, String projectId, String value) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("projectId", projectId);
        params.put("value", value);

        String hql = "from Item where cid=:cid and projectId=:projectId and value=:value";
        List<Item> itemList = itemDao.find(hql, params);
        if (itemList.size()>0) {
            String supInfo = itemList.get(0).getSupInfo();
            if (supInfo != null && !supInfo.equals("")) {
                return Arrays.asList(itemList.get(0).getSupInfo().split("\r\n"));
            }
        }

        return null;
    }
}
