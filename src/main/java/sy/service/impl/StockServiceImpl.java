package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.StockDaoI;
import sy.model.po.Materials;
import sy.model.po.Project;
import sy.model.po.Stock;
import sy.model.po.StockBean;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.MaterialsServiceI;
import sy.service.ProjectServiceI;
import sy.service.StockServiceI;
import sy.service.UserServiceI;
import sy.util.DateKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/12/23.
 */

@Service
public class StockServiceImpl implements StockServiceI{

    @Autowired
    private StockDaoI stockDao;

    @Autowired
    private UserServiceI userService;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private MaterialsServiceI materialsService;

    @Override
    public void addStock(Stock stock) {
        stockDao.save(stock);
    }

    @Override
    public DataGrid dataGrid(PageHelper pageHelper, String projectId, String startDate, String endDate, List<Integer> ugroup, String keyword) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from Stock where isDelete='0' ";

        if (!projectId.equals("")) {
            hql += " and projectId=:projectId ";
            params.put("projectId", projectId);
        }
        if (startDate != null && startDate.length() > 0) {
            hql += " and createTime >= :startDate";
            params.put("startDate", DateKit.strToDateOrTime(startDate));
        }
        if (endDate != null && endDate.length() > 0) {
            hql += " and createTime <= :endDate";
            params.put("endDate", DateKit.strToDateOrTime(endDate));
        }

        if (!keyword.equals("")) {
            hql += " and ( ( select proName from Project where id = projectName) like :proName ";
            params.put("proName", "%%" + keyword + "%%");

            hql += " or ( select mc from Materials where id = materialsId) like :mc ";
            params.put("mc", "%%" + keyword + "%%");

            hql += " or ( select username from Tuser where id = uid) like :username ";
            params.put("username", "%%" + keyword + "%%");

            hql += " or ( select realname from Tuser where id = uid) like :realname ";
            params.put("realname", "%%" + keyword + "%%");
        }
        List<Stock> stockList = stockDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());

        List<StockBean> stockBeanList = new ArrayList<StockBean>();
        StockBean stockBean = new StockBean();
        if (stockList != null && stockList.size()>0) {
            for (Stock stock : stockList) {
                stockBean = new StockBean();
                stockBean.setId(stock.getId());
                stockBean.setCid(stock.getCid());
                stockBean.setUid(stock.getUid());
                stockBean.setUname(userService.getUser(stock.getUid()).getUsername());
                stockBean.setProjectId(stock.getProjectId());
                if (Integer.parseInt(stock.getProjectId()) == -1) {
                    stockBean.setProjectName("无项目采购");
                } else {
                    Project project = projectService.findOneView(Integer.parseInt(stock.getProjectId()));
                    if (project != null) {
                        stockBean.setProjectName(project.getProName());
                    }
                }
                stockBean.setMaterialsId(stock.getMaterialsId());
                Materials materials = materialsService.findById(Integer.parseInt(stock.getMaterialsId()));
                if (materials != null) {
                    stockBean.setMc(materials.getMc());
                    stockBean.setSpecifications(materials.getSpecifications());
                    stockBean.setDw(materials.getDw());
                }
                stockBean.setCount(stock.getCount());
                stockBean.setCreateTime(stock.getCreateTime());

                stockBeanList.add(stockBean);
            }
        }

        dg.setRows(stockBeanList);
        dg.setTotal(stockDao.count("select count(*) " + hql, params));

        return dg;
    }
}
