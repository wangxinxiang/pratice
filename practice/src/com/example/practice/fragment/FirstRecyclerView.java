package com.example.practice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.adapter.RecyclerViewAdapter;
import com.example.practice.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public class FirstRecyclerView extends Fragment{
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> datas = new ArrayList<>();
    private boolean isListView = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.blue_light));
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDatas();
        initView();
    }

    private void initView() {
        adapter = new RecyclerViewAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);
        //设置布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int postion) {

            }

            @Override
            public void onItemLongClickListener(View view, int postion) {

                detele(postion);
            }
        });
    }

    private void setDatas() {
        for (int i = 'a'; i < 'z'; i++) {
            datas.add("" + (char)i);
        }
    }

    public void add(int i) {
        Toast.makeText(getActivity(), "增加", Toast.LENGTH_SHORT).show();;
       adapter.add(i);
    }

    public void detele(int i) {
        adapter.detele(i);
    }

    public void changeLayoutManage() {
        if (isListView) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            isListView = false;
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            isListView = true;
        }

    }


}
