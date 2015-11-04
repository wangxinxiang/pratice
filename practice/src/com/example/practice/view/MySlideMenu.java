package com.example.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2015/7/13.
 */
public class MySlideMenu extends ViewGroup{

    private Scroller mScroller;
    private int lastTouchX;
    private int menuWidth = 240;

    private final int MENU_SCREEN = 0; // 菜单界面
    private final int MAIN_SCREEN = 1; // 主界面
    private int currentScreen = MAIN_SCREEN;

    public MySlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    /**
     * 此方法是SlideMenu控件测量宽和高时回调.
     * widthMeasureSpec 宽度测量规格: 整个屏幕的宽
     * heightMeasureSpec 高度测量规格: 整个屏幕的高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //菜单的高宽
        View menu = getChildAt(0);
        menu.measure(menuWidth, heightMeasureSpec);
        Log.d("--------------", "menu.getLayoutParams().width:" + menu.getLayoutParams().width);
        //主界面的高宽
        View main = getChildAt(1);
        main.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // 布置菜单界面的位置: left=-菜单的宽度, top=0, right=0, bottom=整个屏幕的高度
        View menuView = getChildAt(0);
        menuView.layout(-menuWidth, 0, 0, i3);
        Log.d("--------------", "menuView.getMeasuredWidth():" +  menuView.getMeasuredWidth());
        Log.d("--------------", "menuView.getWidth():" +  menuView.getWidth());

        // 布置主界面的位置: left=0, top=0, right=整个屏幕的宽度, bottom=整个屏幕的高度;
        View mainView = getChildAt(1);
        mainView.layout(i, i1, i2, i3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX =  (int) event.getX();
                int diff =lastTouchX - currentX;

                //判断滚动后的位置是否超出边界
                int nextScrollX = getScrollX() + diff;
                if (nextScrollX < -menuWidth) {
                    scrollTo( -menuWidth, 0);
                } else if (nextScrollX > 0) {
                    scrollTo(0, 0);
                }else {
                    scrollBy(diff, 0);
                }

                lastTouchX = currentX;
                break;
            case MotionEvent.ACTION_CANCEL:     //当触摸屏幕边缘时
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();

                if (scrollX > (-menuWidth / 2)) {
                    currentScreen = MAIN_SCREEN;
                }else {
                    currentScreen = MENU_SCREEN;
                }

                switchScreen();
        }
        return true;
    }

    private void switchScreen() {
        int startX = getScrollX();
        int diff = 0;

        switch (currentScreen) {
            case MAIN_SCREEN:
                diff = 0 - startX;
                break;
            case MENU_SCREEN:
                diff = -menuWidth - startX;
                break;
        }

        mScroller.startScroll(startX, 0, diff, 0, 500);
        invalidate();           // invalidate -> drawChild -> child.draw -> computeScroll
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {      //当startScroll执行过程中即在duration时间内，
                                                    // computeScrollOffset 方法会一直返回false，但当动画执行完成后会返回返加true.
            // 当前正在模拟数据, 取出x轴模拟的值, 设置给scrollTo方法.
            int curr = mScroller.getCurrX();
            scrollTo(curr, 0);
            invalidate();
        }
    }

    /**
     * 隐藏菜单
     */
    public void hideMenu() {
        currentScreen = MAIN_SCREEN;
        switchScreen();
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
        currentScreen = MENU_SCREEN;
        switchScreen();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) ev.getX();
                int diff = lastTouchX - currentX;
                if (diff > 10) {                    //当偏移量超过一定时自己处理事件
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }


}
