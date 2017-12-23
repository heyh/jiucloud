package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sy.service.StockServiceI;

/**
 * Created by heyh on 2017/12/23.
 */

@Controller
@RequestMapping("/stockController")
public class StockController {

    @Autowired
    private StockServiceI stockService;


}
