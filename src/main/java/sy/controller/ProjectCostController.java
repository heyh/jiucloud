package sy.controller;

import org.apache.commons.collections.IterableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.Cost;
import sy.model.po.TFieldData;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.service.CostServiceI;
import sy.service.FieldDataServiceI;
import sy.service.TemplateServiceI;
import sy.util.ConfigUtil;
import sy.util.PageData;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by heyh on 16/7/19.
 */

@Controller
@RequestMapping("/projectCostController")
public class ProjectCostController extends BaseController{

    @Autowired
    private TemplateServiceI templateService;

    @Autowired
    private CostServiceI costService;

    @Autowired
    private FieldDataServiceI fieldDataService;

    /**
     * 跳转项目成本页面
     * @param request
     * @return
     */
    @RequestMapping("/goAddProjectCost")
    public String goAddProjectCost(HttpServletRequest request) {
        return "/app/projectcost/addProjectCost";
    }

    /**
     * 添加成本
     * @param request
     * @return
     */
    @RequestMapping("/securi_addProjectCost")
    @ResponseBody
    public Json addProjectCost(HttpServletRequest request) {
        Json json = new Json();
        PageData pd = new PageData(request);

        TFieldData fieldData = new TFieldData();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        fieldData.setUid(sessionInfo.getId());
        fieldData.setCid(sessionInfo.getCompid());
        fieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
        fieldData.setCompany(sessionInfo.getCompName());

        fieldData.setProjectName(pd.getString("projectName"));
        fieldData.setUnit("元");
        fieldData.setCount("1");
        fieldData.setNeedApproved("0");

        pd.remove("projectName");
        Iterator it = pd.entrySet().iterator();
        try {
            while (it.hasNext()) {
                Map.Entry<String, String> entry= (Map.Entry<String, String>) it.next();
                String _nid = entry.getKey();
                String _price = entry.getValue();
                if (_price == null || _price.equals("") || _price.equals("0") || _price.equals("0.00")) {
                    continue;
                }

                Cost cost = costService.findOneView(_nid, sessionInfo.getCompid());
                fieldData.setCostType(String.valueOf(cost.getId()));
                fieldData.setItemCode(cost.getItemCode());
                fieldData.setPrice(_price);
                fieldData.setDataName(cost.getCostType());
                fieldDataService.add(fieldData);
            }
            json.setSuccess(true);
            json.setMsg("添加成功！");
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("添加失败,请联系管理员");
        }

        return json;
    }
}
