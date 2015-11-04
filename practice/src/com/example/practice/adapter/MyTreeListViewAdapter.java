package com.example.practice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.bean.Node;
import com.example.practice.util.treeview.TreeHelper;
import com.example.practice.util.treeview.adapter.TreeListViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class MyTreeListViewAdapter<T> extends TreeListViewAdapter<T>{
    public MyTreeListViewAdapter(ListView tree, List<T> dates, Context context, int defaultExpendLevel) throws IllegalAccessException {
        super(tree, dates, context, defaultExpendLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tree_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.label = (ImageView) convertView.findViewById(R.id.iv_tree_item_label);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_tree_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            viewHolder.label.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.label.setVisibility(View.VISIBLE);
            viewHolder.label.setImageResource(node.getIcon());      //如果图标不存在则隐藏
        }

        viewHolder.name.setText(node.getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView label;
        TextView name;
    }

    /**
     * 动态插入节点
     *
     * @param position
     * @param string
     */
    public void addExtraNode(int position, String string)
    {
        Node node = visibleNodes.get(position);
        int indexOf = allNodes.indexOf(node);
        // Node
        Node extraNode = new Node(-1, node.getId(), string);
        extraNode.setParent(node);
        node.getChilds().add(extraNode);
        allNodes.add(indexOf + 1, extraNode);

        visibleNodes = TreeHelper.filterVisibleNodes(allNodes);
        notifyDataSetChanged();

    }
}
