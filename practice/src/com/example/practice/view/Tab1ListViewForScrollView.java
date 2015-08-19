package com.example.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by wang on 2015/3/20.
 */
public class Tab1ListViewForScrollView extends ListView{
    public Tab1ListViewForScrollView(Context context) {
        super(context);
    }

    public Tab1ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Tab1ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("PullToRefreshView","ListView:dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("PullToRefreshView","ListView:onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("PullToRefreshView","ListView:onTouchEvent");
        return super.onTouchEvent(ev);
    }
}
