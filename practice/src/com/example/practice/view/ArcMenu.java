package com.example.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.practice.R;

/**
 * Created by Administrator on 2015/11/6.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private Position mPosition = Position.LEFT_BOTTOM;

    private int mRadius;

    private Status mCurrentStatus = Status.CLOSE;   //菜单的当前状态

    private View mCButton;      //菜单的主按钮

    private OnArcItemClickListener mListener;


    /**
     * 菜单的状态
     */
    public enum Status{
        OPEN, CLOSE
    }
    /**
     * 菜单位置的枚举类
     */
    public enum Position{
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }


    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //赋值100dip
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        //获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);

        int pos = typedArray.getInt(R.styleable.ArcMenu_position, 3);
        switch (pos) {
            case 0:
                mPosition = Position.LEFT_TOP;
                break;
            case 1:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case 2:
                mPosition = Position.RIGHT_TOP;
                break;
            case 3:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }

        mRadius = (int) typedArray.getDimension(R.styleable.ArcMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            //测量子view
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            layoutMainButton();

            int count = getChildCount() - 1;    //要除去主button
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i + 1);

                child.setVisibility(GONE);

                int cl = (int) (mRadius*Math.sin(Math.PI / 2 * i / (count - 1) ));
                int ct = (int) (mRadius*Math.cos(Math.PI / 2 * i / (count - 1) ));

                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();

                //如果菜单位置在底部
                if (mPosition == Position.RIGHT_BOTTOM || mPosition == Position.LEFT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                //如果菜单在右方
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }

                child.layout(cl, ct, cl + cWidth, ct + cHeight);

            }
        }
    }

    @Override
    public void onClick(View v) {
        rotateCButton(v, 0f, 360f, 300);

        toggleMenu(300);
    }

    /**
     * 切换菜单
     * @param duration 动画事件
     */
    public void toggleMenu(int duration) {
        //为menu添加平移动画和旋转动画
        int count = getChildCount() - 1;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i + 1);
            child.setVisibility(VISIBLE);
            /**
             * 设置动画起始坐标
             */
            int cl = (int) (mRadius*Math.sin(Math.PI / 2 * i / (count - 1) ));
            int ct = (int) (mRadius*Math.cos(Math.PI / 2 * i / (count - 1) ));
            //如果在左边，动画X起始位置则为负
            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                cl = -cl;
            }
            //如果在上部，动画Y起始位置为负
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                ct = -ct;
            }

            /**
             * 设置动画
             */
            AnimationSet animationSet = new AnimationSet(true);
            //平移动画
            Animation tranAnmia = null;
            if (mCurrentStatus == Status.CLOSE) {   //open
                tranAnmia = new TranslateAnimation(cl, 0, ct, 0);
                child.setFocusable(true);
                child.setClickable(true);
            } else {            //close
                tranAnmia = new TranslateAnimation(0, cl, 0, ct);
                child.setFocusable(false);
                child.setClickable(false);
            }
            tranAnmia.setFillAfter(true);
            tranAnmia.setDuration(duration);
            tranAnmia.setStartOffset(i * 100 / count);
            tranAnmia.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        child.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            //旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);

            animationSet.addAnimation(rotateAnim);
            animationSet.addAnimation(tranAnmia);

            child.startAnimation(animationSet);

            /**
             * 设置点击事件
             */
        final int pos = i + 1;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v, pos);
                    }
                    menuItemAnim(pos);
                    changeStatus();
                }

            });
        }
        changeStatus();
    }

    /**
     * 添加item点击的动画
     * @param pos 被点击的item位置，从1开始
     */
    private void menuItemAnim(int pos) {
        for (int i = 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i == pos) {
                child.startAnimation(scaleBigAnim(300));
            } else {
                child.startAnimation(scaleSmallAnim(300));
            }

            child.setFocusable(false);
            child.setClickable(false);
        }
    }
    /**
     *为当前点击的缩小和透明度降低的动画
     */
    private Animation scaleSmallAnim(int i) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);
        animationSet.setDuration(i);
        return animationSet;
    }

    /**
     *为当前点击的变大和透明度降低的动画
     */
    private Animation scaleBigAnim(int i) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);
        animationSet.setDuration(i);
        return animationSet;
    }

    /**
     * 切换菜单状态
     */
    private void changeStatus() {
        mCurrentStatus = mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE;
    }

    /**
     * 主button的旋转动画
     */
    private void rotateCButton(View v, float start, float end, int duration) {
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    /**
     * 布局主菜单按钮
     */
    private void layoutMainButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);

        int l = 0;      //left位置
        int t = 0;      //top位置

        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }

        mCButton.layout(l, t, l + width, t + height);
    }


    /**
     * 点击事件接口
     */
    public interface OnArcItemClickListener{
        void onClick(View view, int pos);
    }

    public void setmListener(OnArcItemClickListener mListener) {
        this.mListener = mListener;
    }
}
