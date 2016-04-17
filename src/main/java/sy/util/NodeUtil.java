package sy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;

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




    /////////////////////////////////////////////////////////////////////

    public static List<Node> buildListToTree(List<Node> dirs) {
        List<Node> roots = findRoots(dirs);
        List<Node> notRoots = (List<Node>) CollectionUtils.subtract(dirs, roots);
        for (Node root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    public static List<Node> findRoots(List<Node> allNodes) {
        List<Node> results = new ArrayList<Node>();
        for (Node node : allNodes) {
            boolean isRoot = true;
            for (Node comparedOne : allNodes) {
                if (node.getParentId() == comparedOne.getId()) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                node.setLevel(0);
                results.add(node);
                node.setRootId(node.getId());
            }
        }
        return results;
    }

    public static List<Node> findChildren(Node root, List<Node> allNodes) {
        List<Node> children = new ArrayList<Node>();

        for (Node comparedOne : allNodes) {
            if (comparedOne.getParentId() == root.getId()) {
                comparedOne.setParent(root);
                comparedOne.setLevel(root.getLevel() + 1);
                children.add(comparedOne);
            }
        }
        List<Node> notChildren = (List<Node>) CollectionUtils.subtract(allNodes, children);
        for (Node child : children) {
            List<Node> tmpChildren = findChildren(child, notChildren);
            if (tmpChildren == null || tmpChildren.size() < 1) {
                child.setLeaf(true);
            } else {
                child.setLeaf(false);
            }
            child.setChildren(tmpChildren);
        }
        return children;
    }

    public static void fillChildren(JSONObject o, List<Node> childs) {
        JSONArray array = new JSONArray();
        for (Node node : childs) {
            JSONObject oo = new JSONObject();
            List<Node> child = node.getChildren();
            oo.put("id", node.getUserId());
            oo.put("name", node.getName());
            oo.put("userId", node.getUserId());
            if (node.getParent() != null) {
                oo.put("parentId", node.getParent().getId());
            } else {
                oo.put("parentId", "-1");
            }

            System.out.println(node.getName());
            array.add(oo);
            if (child != null && child.size() > 0) {
                fillChildren(oo, child);
            }
        }
        o.put("childrens", array);
    }

    public static JSONArray transJson(List<Node> nodes) {
        JSONArray array = new JSONArray();
        for (Node node : nodes) {
            System.out.println(node.getName());
            JSONObject o = new JSONObject();
            o.put("id", node.getUserId());
            o.put("name", node.getName());
            o.put("userId", node.getUserId());
            if (node.getParent() != null) {
                o.put("parentId", node.getParent().getId());
            } else {
                o.put("parentId", "-1");
            }
            List<Node> child = node.getChildren();
            array.add(o);

            if (child != null && child.size() > 0) {
                fillChildren(o, child);
            }
        }

        return array;
    }
/////////////////////////////////////////////////////////////////////
    public List<Integer> getParentNodes(List<Node> list, int id) {
        if(list == null) return null;
        for (Iterator<Node> iterator = list.iterator(); iterator.hasNext();) {
            Node node = iterator.next();
            if (id==node.getId()) {
                recursionParentFn(list, node);
            }
        }
        return returnList;
    }

    private void recursionParentFn(List<Node> list, Node node) {
        List<Node> parentList = getParentList(list, node);// 得到子节点列表
        if (!returnList.contains(node.getUserId())) {
            returnList.add(node.getUserId());
        }
        if (hasParent(list, node)) {// 判断是否有子节点
            Iterator<Node> it = parentList.iterator();
            while (it.hasNext()) {
                Node n = (Node) it.next();
                recursionParentFn(list, n);
            }
        }
    }

    // 得到子节点列表
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

    // 判断是否有子节点
    private static boolean hasParent(List<Node> list, Node node) {
        return getParentList(list, node).size() > 0 ? true : false;
    }



    public static void main(String[] args) {


        Node node2 = new Node();
        node2.setId(100);
        node2.setUserId(100);
        node2.setParentId(10);

        Node node4 = new Node();
        node4.setId(10000);
        node4.setUserId(10000);
        node4.setParentId(1000);

        Node node3 = new Node();
        node3.setId(1000);
        node3.setUserId(1000);
        node3.setParentId(100);

        Node node1 = new Node();
        node1.setId(10);
        node1.setUserId(10);
        node1.setParentId(0);

        Node node5 = new Node();
        node5.setId(100000);
        node5.setUserId(100000);
        node5.setParentId(10000);



        List<Node> list = new ArrayList<Node>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        list.add(node5);
        NodeUtil nodeUtil = new NodeUtil();
        System.out.print(nodeUtil.getParentNodes(list, 10000));

        List<Node> allNodes = new ArrayList<Node>();
        allNodes.add(new Node(1, 0, 1, "节点1"));
        allNodes.add(new Node(2, 0, 2, "节点2"));
        allNodes.add(new Node(3, 0, 3, "节点3"));
        allNodes.add(new Node(11, 7, 11, "节点11"));
        allNodes.add(new Node(4, 1, 4, "节点4"));
        allNodes.add(new Node(5, 1, 5, "节点5"));
        allNodes.add(new Node(6, 1, 6, "节点6"));
        allNodes.add(new Node(7, 4, 7,"节点7"));
        allNodes.add(new Node(8, 4, 8,"节点8"));
        allNodes.add(new Node(9, 5, 9, "节点9"));
        allNodes.add(new Node(10, 100, 10,"节点10"));
        List<Node> roots = buildListToTree(allNodes);
        System.out.println(transJson(roots));

    }

}

