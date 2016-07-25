package sy.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 16/7/9.
 */
public class Utility {

    /**
     * list 转 树
     *
     * @param treeList
     * @param parentId
     * @return
     */
    public static JSONArray treeList(List<Map<String, Object>> treeList, String parentId)
    {
        JSONArray children = new JSONArray();
        for (Map<String, Object> nodeMap : treeList)
        {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            String id = nodeJson.getString("id");
            if (nodeJson.getString("pid").equals(parentId))
            {
                JSONArray nodeJsonArr = treeList(treeList, id);
                nodeJson.put("children", nodeJsonArr);
                children.add(nodeJson);
            }
        }
        return children;
    }
}
