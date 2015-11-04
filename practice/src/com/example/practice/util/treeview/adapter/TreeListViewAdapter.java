package com.example.practice.util.treeview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.practice.bean.Node;
import com.example.practice.util.treeview.TreeHelper;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter{

    protected List<Node> allNodes;
    protected List<Node> visibleNodes;
    protected Context context;
    protected LayoutInflater mInflater;
    protected ListView mTree;
    protected OnTreeItemOnClickListener mListener;

    public TreeListViewAdapter(ListView tree, List<T> dates, Context context, int defaultExpendLevel) throws IllegalAccessException {
        mInflater = LayoutInflater.from(context);
        this.allNodes = TreeHelper.getSortedNodes(dates, defaultExpendLevel);
        this.context = context;
        visibleNodes = TreeHelper.filterVisibleNodes(allNodes);

        mTree = tree;
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expendOrCollapse(position);

                if (mListener != null) {
                    mListener.onClick(visibleNodes.get(position), position);
                }
            }
        });
    }

    /**
     * listView的点击效果：  展开或收缩
     * @param position
     */
    private void expendOrCollapse(int position) {
        Node node = visibleNodes.get(position);
        if (node != null) {
            if (node.isLeaf()) return;
            node.setIsExpand(!node.isExpand());

            visibleNodes = TreeHelper.filterVisibleNodes(allNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return visibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return visibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = visibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置内边距
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);


    /**
     * 设置item的点击事件
     */
    public interface OnTreeItemOnClickListener{
        void onClick(Node node, int position);
    }

    public void setmListener(OnTreeItemOnClickListener mListener) {
        this.mListener = mListener;
    }
}
