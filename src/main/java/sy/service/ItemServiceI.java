package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.Item;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.Section;

import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/2/18.
 */

@Repository
public interface ItemServiceI {

    public DataGrid dataGrid(String cid, String keyword, PageHelper pageHelper);
    public Item add(Item item);
    public void update(Item info);
    public void delete(String id);
    public Item detail(String id);
    public List<Item> getDefaultItems();
    public List<Item> getItems(String cid, String projectId);
    public List<Map<String, Object>> getSelectItems(String cid, String projectId);
    public Item getSingleItem(String cid, String projectId, String value);
    public List<String> getSupInfos(String cid, String projectId, String value);


}
