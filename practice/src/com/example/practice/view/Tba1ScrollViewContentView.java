package com.example.practice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by wang on 2015/3/21.
 */
public class Tba1ScrollViewContentView extends ScrollView{
    private boolean canScroll;
    /**
     * last y,last x;
     */
    private int mInitialMotionX;
    private int mInitialMotionY;
    private Callbacks mCallbacks;//���������Ļص�

    public Tba1ScrollViewContentView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("=======================", "Tba1ScrollViewContentView");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("PullToRefreshView","Tba1ScrollViewContentView:ACTION_DOWN");
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mInitialMotionY;
                int deltaX = x - mInitialMotionX;
                Log.d("PullToRefreshView","Tba1ScrollViewContentView:deltaY,deltaX:" + Math.abs(deltaY) + "," + Math.abs(deltaX));
                return Math.abs(deltaY) > Math.abs(deltaX);
            case MotionEvent.ACTION_UP:
                Log.d("PullToRefreshView","Tba1ScrollViewContentView:ACTION_UP");

                break;
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("PullToRefreshView","Tba1ScrollViewContentView_onTouchEvent:ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("PullToRefreshView","Tba1ScrollViewContentView_onTouchEvent:ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("PullToRefreshView","Tba1ScrollViewContentView_onTouchEvent:ACTION_UP");
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        int deltaY = t - oldt;

        // scrollview �ؼ����� ��λ ����������ֵ
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(deltaY);
        }

    }

    /**
     * �������¼���¶��ȥ
     * @param listener
     */
    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public interface Callbacks {
        void onScrollChanged(int deltaY);
    }
}

