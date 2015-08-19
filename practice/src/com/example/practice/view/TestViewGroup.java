package com.example.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/7/14.
 */
public class TestViewGroup extends ViewGroup{

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View test1 = getChildAt(0);
        test1.layout(- test1.getMeasuredWidth(), 0, 0, i3);

        View test2 = getChildAt(1);
        test2.layout(i, i1, i2, i3);
    }
}
