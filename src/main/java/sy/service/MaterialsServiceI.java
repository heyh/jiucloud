package sy.service;

import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

/**
 * Created by heyh on 2017/12/2.
 */
public interface MaterialsServiceI {
    DataGrid dataGrid(PageHelper pageHelper, String keyword, String pid);
}
