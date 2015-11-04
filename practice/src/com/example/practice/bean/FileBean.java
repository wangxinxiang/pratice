package com.example.practice.bean;

import com.example.practice.util.treeview.annotation.TreeNodeId;
import com.example.practice.util.treeview.annotation.TreeNodeLabel;
import com.example.practice.util.treeview.annotation.TreeNodePid;

/**
 * Created by Administrator on 2015/11/2.
 */
public class FileBean {

    @TreeNodeId
    private int id;
    @TreeNodePid
    private int Pid;
    @TreeNodeLabel
    private String label;
    private String desc;

    public FileBean() {
    }

    public FileBean(int id, int pid, String label) {
        this.id = id;
        Pid = pid;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
