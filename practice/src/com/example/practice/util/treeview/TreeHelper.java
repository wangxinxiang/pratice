package com.example.practice.util.treeview;

import android.util.Log;

import com.example.practice.R;
import com.example.practice.bean.Node;
import com.example.practice.util.treeview.annotation.TreeNodeId;
import com.example.practice.util.treeview.annotation.TreeNodeLabel;
import com.example.practice.util.treeview.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class TreeHelper {

    /**
     * 将数据转化为node
     * @param datas 数据
     * @param <T>   类型：FileBean
     * @return  node
     */
    public static <T>List<Node> converDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node;
        for (T t:datas) {
            int id = -1;
            int pid = -1;
            String lable = null;

            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    lable = (String) field.get(t);
                }
            }
            node = new Node(id, pid, lable);
            nodes.add(node);
        }

        /**
         * 添加父子节点关系
         * node1 : 父节点
         * node2 : 子节点
         */
        for (int i = 0; i < nodes.size(); i ++) {
            Node node1 = nodes.get(i);
            for (int j = i; j < nodes.size(); j ++) {
                Node node2 = nodes.get(j);
                if (node1.getId() == node2.getpId()) {
                    node2.setParent(node1);
                    node1.getChilds().add(node2);
                } else if (node1.getpId() == node2.getId()) {
                    node1.setParent(node2);
                    node2.getChilds().add(node1);
                }
            }
        }

        return nodes;
    }

    /**
     * 将节点进行排序
     */
    public static <T>List<Node> getSortedNodes(List<T> dates, int expendLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = converDatas2Nodes(dates);
        //获取根节点
        List<Node> rootNode = getRootNode(nodes);

        for (Node root : rootNode) {
            addNode(result, root, expendLevel, 1);
        }
        return result;
    }

    /**
     * 把一个节点的所有孩子都放入result中，迭代添加子node
     * @param result    存放结果
     * @param root 根节点
     * @param expendLevel 默认展开层次
     * @param i 层级 1为根节点
     */
    private static void addNode(List<Node> result, Node root, int expendLevel, int i) {
        result.add(root);
        if (expendLevel > i) {
            root.setIsExpand(true);
        }
        if (root.isLeaf()) return;

        for (Node child : root.getChilds()) {
            addNode(result, child, expendLevel, i + 1);
        }
    }


    /**
     * 获取要显示的node
     * @param nodes 所有的node
     * @return 过滤后的result
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setIcon(node);
                result.add(node);
            }
        }
        return result;
    }


    /**
     * 设置图标
     */
    private static void setIcon(Node node) {
        if (node.getChilds().size() > 0 && node.isExpand()) {
            node.setIcon(R.drawable.tree_ex);
        } else if (node.getChilds().size() > 0 && !node.isExpand()) {
            node.setIcon(R.drawable.tree_ec);
        }else {
            node.setIcon(-1);
        }
    }

    /**
     * 获取根节点
     */
    public static List<Node> getRootNode(List<Node> nodes) {
        List<Node> rootNode = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                rootNode.add(node);
            }
        }
        return rootNode;
    }
}
