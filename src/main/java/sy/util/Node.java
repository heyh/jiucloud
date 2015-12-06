package sy.util;

/**
 * Created by heyh on 15/12/6.
 */
/**
 * 无限级节点模型
 */
public class Node {
    /**
     * 节点id
     */
    private int id;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 父节点id
     */
    private Integer parentId;

    private Integer userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
