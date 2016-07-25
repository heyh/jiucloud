package sy.service.impl;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.TemplateDaoI;
import sy.model.po.Template;
import sy.service.TemplateServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 16/7/17.
 */

@Service("templateService")
public class TemplateServiceImpl implements TemplateServiceI{

    @Autowired
    private TemplateDaoI templateDao;

    @Override
    public JSONArray getCostTemplate(String cid, String projectId) {
        JSONArray costTemplate = new JSONArray();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cid", cid);
        paramMap.put("projectId", projectId);
        String hql = "from Template t  where cid=:cid and projectId=:projectId and isDel='0'";
        List<Template> templateList = templateDao.find(hql, paramMap);
        if (templateList.size() == 0) {
            paramMap.put("projectId", "-1");
            templateList = new ArrayList<Template>();
            templateList = templateDao.find(hql, paramMap);
        }
        if (templateList != null && templateList.size()>0) {
            String template = templateList.get(0).getTemplateDesc().replaceAll("\n", "").replaceAll("\t","");
            costTemplate = JSONArray.fromObject(template);
        }
        return costTemplate;
    }
}
