package sy.service;

import sy.model.Clockingin;
import sy.model.po.ClockinginTime;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2017/10/9.
 */
public interface ClockinginTimeServiceI {

    public List<ClockinginTime> getClockinginTimeList(String cid);

    public DataGrid dataGrid(String cid, PageHelper pageHelper);

    ClockinginTime getClockinginTime(int id);

    void update(ClockinginTime c);
}
