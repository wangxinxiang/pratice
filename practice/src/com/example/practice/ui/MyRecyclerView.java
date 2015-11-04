package com.example.practice.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.fragment.FirstRecyclerView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/29.
 */
public class MyRecyclerView extends FragmentActivity implements View.OnClickListener{

    private FirstRecyclerView firstRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_test);
        initView();
        initListener();
    }

    private void initView() {
        showFragment(0);

    }


    /**
     * 显示fragment
     */
    private void showFragment(int position) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //隐藏
        if (firstRecyclerView != null) {
            transaction.hide(firstRecyclerView);
        }
        //显示
        switch (position) {
            case 0:
                if (firstRecyclerView == null) {
                    firstRecyclerView = new FirstRecyclerView();
                    transaction.add(R.id.fl_recycler, firstRecyclerView);
                } else {
                    transaction.show(firstRecyclerView);
                }
                break;
        }

        transaction.commit();
    }

    private void initListener() {
        findViewById(R.id.recycler_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstRecyclerView.add(5);
            }
        });

        findViewById(R.id.recycler_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstRecyclerView.detele(5);
            }
        });

        findViewById(R.id.recycler_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstRecyclerView.changeLayoutManage();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "增加", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.recycler_first:
                showFragment(0);
                break;
            case R.id.recycler_delete:
                firstRecyclerView.detele(1);
                break;
            case R.id.recycler_add:
                Toast.makeText(this, "增加", Toast.LENGTH_SHORT).show();
                firstRecyclerView.add(1);
                break;
        }
    }
}
