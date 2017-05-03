package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.ParamTrans;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2016/12/25.
 */
@Repository
public interface ParamTransServiceI {
    public List<ParamTrans> getCostTransList();

    public List<ParamTrans> getParamTransListByCid(String cid, String paramType);

    public DataGrid dataGrid(String cid, String paramType, PageHelper pageHelper);

    public ParamTrans add(ParamTrans paramTrans);

    public void update(ParamTrans info);

    public void delete(String id);

    public ParamTrans detail(String id);

    public DataGrid execlDataGrid(String cid, PageHelper pageHelper);

}
