package sy.service;

import sy.model.po.Materials;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * Created by heyh on 2017/12/2.
 */
public interface MaterialsServiceI {
    DataGrid dataGrid(PageHelper pageHelper, String keyword, String pid);
    Materials findById(int id);

    Materials addNode(String cid, String pid, String mc, String specifications, String dw);

    void delNode(String id);

    void updateNode(Materials info);
}
