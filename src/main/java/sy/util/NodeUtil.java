package sy.util;

import java.util.*;

/**
 * Created by heyh on 15/12/6.
 */
public class NodeUtil {

    private List<Integer> returnList = new ArrayList<Integer>();

    public List<Integer> getChildNodes(List<Node> list, int id) {
        if(list == null) return null;
        for (Iterator<Node> iterator = list.iterator(); iterator.hasNext();) {
            Node node = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (id==node.getId()) {
                recursionFn(list, node);
            }
            // 二、遍历所有的父节点下的所有子节点
        /*if (node.getParentId()==0) {
            recursionFn(list, node);
        }*/
        }
        return returnList;
    }

    private void recursionFn(List<Node> list, Node node) {
        List<Node> childList = getChildList(list, node);// 得到子节点列表
        if (!returnList.contains(node.getUserId())) {
            returnList.add(node.getUserId());
        }
        if (hasChild(list, node)) {// 判断是否有子节点
            Iterator<Node> it = childList.iterator();
            while (it.hasNext()) {
                Node n = (Node) it.next();
                recursionFn(list, n);
            }
        }
    }

    // 得到子节点列表
    private static List<Node> getChildList(List<Node> list, Node node) {
        List<Node> nodeList = new ArrayList<Node>();
        Iterator<Node> it = list.iterator();
        while (it.hasNext()) {
            Node n = (Node) it.next();
            if (n.getParentId() == node.getId()) {
                nodeList.add(n);
            }
        }
        return nodeList;
    }

    // 判断是否有子节点
    private static boolean hasChild(List<Node> list, Node node) {
        return getChildList(list, node).size() > 0 ? true : false;
    }


    public List<Map<String, Node>> getAllParents(List<Node> nodes, int userId) {
        List<Map<String, Node>> parentNodeList = new ArrayList<Map<String, Node>>();
        Iterator<Node> it = nodes.iterator();
        Node currentNode = new Node();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getUserId() == userId) {
                currentNode = node;
                break;
            }
        }

        if (currentNode.getParentId() == 0) {
            Map<String, Node> nodeMap = new HashMap<String, Node>();
            nodeMap.put(String.valueOf(userId), currentNode);
            parentNodeList.add(nodeMap);
            return parentNodeList;
        } else {
            parentNodeList.addAll(getAllParents(nodes, currentNode.getParentId()));
        }
        return parentNodeList;
    }
}

