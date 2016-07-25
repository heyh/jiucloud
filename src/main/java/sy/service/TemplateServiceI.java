package sy.service;

import net.sf.json.JSONArray;

/**
 * Created by heyh on 16/7/17.
 */
public interface TemplateServiceI {

    JSONArray getCostTemplate(String cid, String projectId);
}
