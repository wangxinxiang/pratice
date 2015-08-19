package com.example.practice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.practice.bean.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ListViewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ContactInfo> contactInfos;

    public ListViewAdapter(Context context, List<ContactInfo> contactInfos) {
        this.context = context;
        this.contactInfos = (ArrayList<ContactInfo>) contactInfos;
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContactInfo contactInfo = contactInfos.get(i);
        TextView textView;
        if (view == null) {
            textView = new TextView(context);
        }else {
            textView = (TextView) view;
        }
        textView.setText(contactInfo.toString());
        return textView;
    }
}
