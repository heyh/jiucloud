package sy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by heyh on 16/4/16.
 */
public class ParentNode {

    private List<Node> returnList = new ArrayList<Node>();

    public List<Node> getParentNodes(List<Node> list, int id) {
        if(list == null) return null;
        for (Iterator<Node> iterator = list.iterator(); iterator.hasNext();) {
            Node node = iterator.next();
            if (id==node.getId()) {
                recursionParentFn(list, node);
            }
        }

        int rootCount = 0;
        boolean hasRoot = false;
        for (Node node : returnList) {
            if (node.getId() == 0) {
                hasRoot = true;
            }
            if (node.getParentId() == 0) {
                rootCount++;
            }
        }
        if (rootCount>0 && hasRoot == false) {
            Node tmp = new Node();
            tmp.setId(0);
            tmp.setParentId(-1);
            tmp.setUserId(-1);
            tmp.setName("Root");
            returnList.add(tmp);
        }
        return returnList;
    }

    private void recursionParentFn(List<Node> list, Node node) {
        List<Node> parentList = getParentList(list, node); // 得到父节点列表
        if (!returnList.contains(node.getUserId())) {
            returnList.add(node);
        }
        if (hasParent(list, node)) { // 判断是否有父节点
            Iterator<Node> it = parentList.iterator();
            while (it.hasNext()) {
                Node n = (Node) it.next();
                recursionParentFn(list, n);
            }
        }
    }

    // 得到父节点列表
    private static List<Node> getParentList(List<Node> list, Node node) {
        List<Node> nodeList = new ArrayList<Node>();
        Iterator<Node> it = list.iterator();
        while (it.hasNext()) {
            Node n = (Node) it.next();
            if (n.getId() == node.getParentId()) {
                nodeList.add(n);
            }
        }
        return nodeList;
    }

    // 判断是否有父节点
    private static boolean hasParent(List<Node> list, Node node) {
        return getParentList(list, node).size() > 0 ? true : false;
    }
}
