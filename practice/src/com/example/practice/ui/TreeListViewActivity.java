package com.example.practice.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.adapter.MyTreeListViewAdapter;
import com.example.practice.bean.FileBean;
import com.example.practice.bean.Node;
import com.example.practice.util.treeview.adapter.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class TreeListViewActivity extends Activity{
    private ListView tree;
    private MyTreeListViewAdapter<FileBean> adapter;
    private List<FileBean> dates = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDates();
        initView();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                refreshLayout.setRefreshing(false);
                return false;
            }
        });
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        refreshLayout.setColorSchemeResources(R.color.blue_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        });

        tree = (ListView) findViewById(R.id.lv_tree);

        try {
            adapter = new MyTreeListViewAdapter<>(tree, dates, this, 2);
            tree.setAdapter(adapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        adapter.setmListener(new TreeListViewAdapter.OnTreeItemOnClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(TreeListViewActivity.this, node.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // DialogFragment
                final EditText et = new EditText(TreeListViewActivity.this);
                new AlertDialog.Builder(TreeListViewActivity.this).setTitle("Add Node")
                        .setView(et)
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                if (TextUtils.isEmpty(et.getText().toString()))
                                    return;
                                adapter.addExtraNode(position, et.getText()
                                        .toString());
                            }
                        }).setNegativeButton("Cancel", null).show();
                return true;
            }
        });
    }

    private void initDates() {
        FileBean bean = new FileBean(1, 0, "根目录1");
        dates.add(bean);
        bean = new FileBean(2, 0, "根目录2");
        dates.add(bean);
        bean = new FileBean(3, 0, "根目录3");
        dates.add(bean);
        bean = new FileBean(4, 1, "根目录1-1");
        dates.add(bean);
        bean = new FileBean(5, 1, "根目录1-2");
        dates.add(bean);
        bean = new FileBean(6, 5, "根目录1-2-1");
        dates.add(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
