package com.example.practice.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class Node {

    private int id;
    //根节点id
    private int pId;

    private String name;
    //所在的层级
    private int level;
    //是否已经展开
    private boolean isExpand = false;
    private int icon;

    private Node parent;
    private List<Node> childs = new ArrayList<>();


    public Node() {
    }

    public Node(int id, int pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    /**
     * 判断是否为根节点
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     */
    public boolean isParentExpand() {
        return parent != null && parent.isExpand;
    }

    /**
     * 判断是否为叶子节点
     */
    public boolean isLeaf() {
        return childs.size() == 0;
    }

    /**
     * 获取层级，本地判断
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 当收缩时，子node也要收缩
     */
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;

        if (!isExpand) {
            for (Node node : childs) {
                node.setIsExpand(false);
            }
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void setChilds(List<Node> childs) {
        this.childs = childs;
    }
}
