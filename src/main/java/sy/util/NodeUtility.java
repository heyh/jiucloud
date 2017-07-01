package sy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by heyh on 15/12/6.
 */
public class NodeUtility {

    private List<Node> returnList = new ArrayList<Node>();

    public List<Node> getDepartmentNodeList(List<Node> dirs) {
        List<Node> roots = findRoots(dirs);
        List<Node> notRoots = (List<Node>) CollectionUtils.subtract(dirs, roots);
        returnList.addAll(roots);

        for (Node root : roots) {
            findChildren(root, notRoots);
        }

        return returnList;
    }

    public  List<Node> findRoots(List<Node> allNodes) {
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

    public  List<Node> findChildren(Node root, List<Node> allNodes) {
        List<Node> children = new ArrayList<Node>();

        for (Node comparedOne : allNodes) {
            if (comparedOne.getParentId() == root.getId()) {
                comparedOne.setParent(root);
                comparedOne.setLevel(root.getLevel() + 1);
                children.add(comparedOne);
                returnList.add(comparedOne);
            }
        }
        List<Node> notChildren = (List<Node>) CollectionUtils.subtract(allNodes, children);
        for (Node child : children) {
            List<Node> tmpChildren = findChildren(child, notChildren);
        }
        return children;
    }

    public static void main(String[] args) {

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
        NodeUtility nodeUtility = new NodeUtility();
        nodeUtility.getDepartmentNodeList(allNodes);
        System.out.println(nodeUtility.returnList);

    }

}

