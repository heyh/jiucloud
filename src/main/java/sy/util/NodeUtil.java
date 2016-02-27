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

    }

}

