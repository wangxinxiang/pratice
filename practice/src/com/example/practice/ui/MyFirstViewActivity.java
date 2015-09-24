package com.example.practice.ui;

import android.app.Activity;
import android.os.Bundle;
import com.example.practice.R;

/**
 * Created by Administrator on 2015/6/28.
 */
public class MyFirstViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_view);
    }

}
