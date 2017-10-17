package sy.service;

import sy.model.Clockingin;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2017/9/16.
 */
public interface ClockinginServiceI {

    public Clockingin Clockingin(Clockingin clockingin);

    public List<Clockingin> getClockingins(List<Integer> ugroup);

    public int approveClockingin(String approveState);

    DataGrid dataGrid(String keyword, String cid, List<Integer> ugroup, Clockingin clockingin, PageHelper pageHelper, boolean hasApproveRight);

    public Clockingin getClockingin(int id);

    public void update(Clockingin info);

    void delete(int id);

    public List<Clockingin> hasSameClockingin(Clockingin clockingin);
}
