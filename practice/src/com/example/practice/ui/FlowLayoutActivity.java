package com.example.practice.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.view.FlowLayout;

/**
 * Created by Administrator on 2015/11/5.
 */
public class FlowLayoutActivity extends Activity{

    private String[] values = new String[] {
      "hello", "android", "c++", "c#", "c", "php", "oc", "hello", "android", "c++", "c#", "c", "php", "oc"
    };

    private FlowLayout mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flowlayout_test);
        initView();
        initDate();
    }

    private void initView(){
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
    }

    private void initDate() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        for (String value : values) {
//            Button btn = new Button(this);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            btn.setLayoutParams(lp);
//            btn.setText(value);
//            mFlowLayout.addView(btn);
            TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_tv, mFlowLayout, false);
            tv.setText(value);
            mFlowLayout.addView(tv);
        }
    }
}
