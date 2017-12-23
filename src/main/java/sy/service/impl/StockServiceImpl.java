package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.StockDaoI;
import sy.service.StockServiceI;

/**
 * Created by heyh on 2017/12/23.
 */

@Service
public class StockServiceImpl implements StockServiceI{

    @Autowired
    private StockDaoI stockDao;


}
