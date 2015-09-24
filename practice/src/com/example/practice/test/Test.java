package com.example.practice.test;

import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.Toast;

import com.example.practice.ui.WelcomeActivity;
import com.example.practice.util.Constant;

/**
 * Created by Administrator on 2015/9/24.
 */
public class Test extends AndroidTestCase{

    public void test(){
        String test = Constant.SERVICE_ACTION;
        System.out.print(test);
    }
}
