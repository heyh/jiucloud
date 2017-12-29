package sy.service;

import org.springframework.beans.factory.annotation.Autowired;
import sy.dao.SupplierDaoI;
import sy.model.po.Supplier;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2017/12/29.
 */
public interface SupplierServiceI {
    DataGrid dataGrid(String cid, PageHelper pageHelper, String keyword, String supplierId);
    List<Supplier> supplierList(String cid);

    void add(Supplier supplier);

    Supplier detail(String supplierId);

    void update(Supplier info);

    void delete(String supplierId);
}
