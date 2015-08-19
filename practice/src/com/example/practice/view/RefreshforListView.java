package com.example.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.example.practice.R;

import java.util.jar.Attributes;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RefreshforListView extends ListView implements AbsListView.OnScrollListener {

    private int downY;
    //头部
    private View headView;
    private ImageView arrow;        //箭头
    private ProgressBar progressBar;        //旋转的进度条
    private int headHeight;
    private Animation upAnimation, downAnimation;       //头部箭头动画
    private int firstItem = -1;        //位于listView顶端的item是第几个
    private OnRefreshListener listener;

    private final int PULL_DOWN_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 释放刷新
    private final int REFRESHING = 2; // 正在刷新中

    private int currentState = PULL_DOWN_REFRESH;

    //尾部
    private View footView;
    private int footHeight;
    private boolean isFootRefresh = false;

    public RefreshforListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHead(context);
        initFoot(context);
        setOnScrollListener(this);
    }


    /**
     * 初始化头部刷新
     */
    private void initHead(Context context) {
        headView = View.inflate(context, R.layout.refresh_header, null);
        arrow = (ImageView) headView.findViewById(R.id.refresh_allow);
        progressBar = (ProgressBar) headView.findViewById(R.id.refresh_progress);

        //获取高度并隐藏刷新头部
        headView.measure(0, 0);
        // getHeight 只能在布局已经显示在屏幕上之后才可以获取到值.
        // getMeasuredHeight 可以在measure方法执行之后, 得到高度
        headHeight = headView.getMeasuredHeight();
        Log.d("onTouchEvent ---->", "headHeight:" + headHeight);
        headView.setPadding(0, -headHeight, 0 ,0);

        addHeaderView(headView);

        initHeadAnimation();
    }
    /**
     * 初始化底部刷新
     */
    private void initFoot(Context context) {
        footView = View.inflate(context, R.layout.refresh_foot, null);

        footView.measure(0, 0);
        footHeight = footView.getMeasuredHeight();
        footView.setPadding(0, -footHeight, 0, 0);
        addFooterView(footView);
    }

    /**
     * 初始化头部动画
     */
    private void initHeadAnimation() {
        //当下拉高度高于头部时箭头转为向上
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        //当从上面情况再往回收缩时，箭头方向再次转回
        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                Log.d("onTouchEvent ---->", "ACTION_DOWN:" + downY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentState == REFRESHING) {           //正在刷新就不用再进行了
                    break;
                }

                int moveY = (int) ev.getY();
                Log.d("onTouchEvent ---->", "ACTION_MOVE:" + moveY);

                int diff = moveY - downY;           //移动距离
                if (diff > 0 && firstItem == 0) {           //如果位于顶部且向下滑动
                    int paddingTop = diff - headHeight;
                    Log.d("onTouchEvent ---->", "paddingTop:" + paddingTop);
                    if (paddingTop > 0) {
                        currentState = RELEASE_REFRESH;         //释放刷新
                        refreshHead();
                    } else {
                        currentState = PULL_DOWN_REFRESH;
                        refreshHead();
                    }

                    headView.setPadding(0, paddingTop, 0, 0);
                    return true;                                                //自己响应事件
                }

                case MotionEvent.ACTION_UP:
                    if (currentState == RELEASE_REFRESH) {            //如果是要刷新
                        currentState = REFRESHING;
                        headView.setPadding(0, 0, 0, 0);
                        refreshHead();

                        if (listener != null) {
                            listener.onTopRefresh();
                        }
                    } else if (currentState == PULL_DOWN_REFRESH) {
                        headView.setPadding(0, -headHeight, 0, 0);
                    }

        }

        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态改变
     */
    private void refreshHead() {
        switch (currentState) {
            case PULL_DOWN_REFRESH:
                arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                arrow.startAnimation(upAnimation);
                break;
            case REFRESHING:
                arrow.setVisibility(INVISIBLE);
                arrow.clearAnimation();
                progressBar.setVisibility(VISIBLE);
        }
    }

    /**
     * 当刷新结束时调用该方法结束刷新
     */
    public void overTopRefresh() {
        currentState = PULL_DOWN_REFRESH;
        headView.setPadding(0, -headHeight, 0 ,0);
        progressBar.setVisibility(INVISIBLE);
        arrow.setVisibility(VISIBLE);
    }

    public void overFootRefresh() {
        isFootRefresh = false;
//        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -footHeight);
//        animation.setFillAfter(true);
//        animation.setDuration(1000);
//        footView.startAnimation(animation);
            footView.setPadding(0, -footHeight, 0, 0);
    }



    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }


    /**
     * 当滚动状态改变时, 回调此方法
     * scrollState 当前滚动的状态
     *
     * SCROLL_STATE_IDLE 停滞状态
     * SCROLL_STATE_TOUCH_SCROLL 手指按下移动的状态
     * SCROLL_STATE_FLING 猛地一滑
     */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == SCROLL_STATE_FLING  && !isFootRefresh) {
            isFootRefresh = true;
            footView.setPadding(0, 0,0, 0);

            //滑到底部
            setSelection(getCount() - 1);

            if (listener != null) {
                listener.onFootRefresh();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        firstItem = i;                   //获取顶部item的排位
        Log.d("onTouchEvent ---->", "firstItem:" + firstItem);
    }

    public interface OnRefreshListener {
        void onTopRefresh();
        void onFootRefresh();
    }

}
