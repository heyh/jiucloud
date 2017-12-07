package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.pageModel.DataGrid;
import sy.pageModel.MaterialsTree;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.MaterialsServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heyh on 2017/12/2.
 */

@Controller
@RequestMapping("/materialsController")
public class MaterialsController {

    @Autowired
    private MaterialsServiceI materialsService;

    @RequestMapping("/securi_materialsTreeGrid")
    @ResponseBody
    public DataGrid materialsTreeGrid(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        DataGrid dataGrid =  materialsService.dataGrid(pageHelper, keyword, pid);

        return dataGrid;
    }

    @RequestMapping("/securi_materialsTreeGridChild")
    @ResponseBody
    public List<MaterialsTree> materialsTreeGridChild(PageHelper pageHelper, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        DataGrid dataGrid =  materialsService.dataGrid(pageHelper, keyword, pid);

        return dataGrid.getRows();
    }

}
