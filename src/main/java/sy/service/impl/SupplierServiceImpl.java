package sy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.SupplierDaoI;
import sy.model.po.Supplier;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.SupplierServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/29.
 */

@Service
public class SupplierServiceImpl implements SupplierServiceI {

    @Autowired
    private SupplierDaoI supplierDao;

    @Override
    public DataGrid dataGrid(String cid, PageHelper pageHelper, String keyword, String supplierId) {
        DataGrid dg = new DataGrid();
        List<Supplier> supplierList = new ArrayList<Supplier>();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from Supplier where isDelete='0' and cid=:cid ";
        params.put("cid", cid);

        if (!supplierId.equals("")) {
            hql += " and id=:supplierId";
            params.put("supplierId", Integer.parseInt(supplierId));
        }
        if (!keyword.equals("")) {
            hql += " and ( name like :keyword or addr like :keyword  or linkman like :keyword ) ";
            params.put("keyword", "%%" + keyword + "%%");
        }

        hql += " order by ID desc ";
        supplierList = supplierDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());

        dg.setRows(supplierList);
        dg.setTotal(supplierDao.count("select count(*) " + hql, params));

        return dg;
    }

    @Override
    public List<Supplier> supplierList(String cid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = "from Supplier where isDelete='0' and cid=:cid ";
        List<Supplier> supplierList = supplierDao.find(hql, params);

        return supplierList;
    }

    @Override
    public void add(Supplier supplier) {
        supplierDao.save(supplier);
    }

    @Override
    public Supplier detail(String supplierId) {
        if (supplierId == null) {
            return null;
        }
        Supplier supplier = supplierDao.get(" FROM Supplier where 1=1 and isDelete=0 and id=" + Integer.parseInt(supplierId));
        return supplier;
    }

    @Override
    public void update(Supplier info) {
        Supplier supplier = supplierDao.get(Supplier.class, info.getId());
        BeanUtils.copyProperties(info, supplier);
    }

    @Override
    public void delete(String supplierId) {
        if (supplierId != null) {
            try {
                Supplier supplier = supplierDao.get(" FROM Supplier where 1=1 and id = " + supplierId);
                if (supplier != null) {
                    supplier.setIsDelete("1");
                    update(supplier);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
