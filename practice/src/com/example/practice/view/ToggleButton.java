package com.example.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.practice.R;

/**
 * Created by Administrator on 2015/7/5.
 */
public class ToggleButton extends View{
    private Bitmap backgroundBmp;       //背景图片
    private Bitmap buttonBmp;           //按钮图片
    private boolean currentState = false;       //按钮状态
    private boolean isTouch = false;        //是否触摸按钮
    private int downX;          //触摸的位置
    private ToggleButtonListener mOnToggleButtonStateChanged;

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.ToggleSwitch);
        int backgroundId = typedArray.getResourceId(R.styleable.ToggleSwitch_backgroundId, R.drawable.switch_background);
        int buttonId = typedArray.getResourceId(R.styleable.ToggleSwitch_buttonId, R.drawable.slide_button_background);
        isTouch = typedArray.getBoolean(R.styleable.ToggleSwitch_currentState, false);
        backgroundBmp = BitmapFactory.decodeResource(getResources(), backgroundId);
        buttonBmp = BitmapFactory.decodeResource(getResources(), buttonId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroundBmp, 0, 0, null);       //绘制背景
        int trueX = backgroundBmp.getWidth() - buttonBmp.getWidth();            //按钮是true时按钮的x位置

        //绘制按钮的位置

        if (isTouch) {  //当按钮被按下时
            int buttonX = downX - buttonBmp.getWidth() / 2;     //当触摸按钮时按钮应处的位置

            //按钮的位置范围
            if (buttonX < 0) {
                buttonX = 0;
            } else  if (buttonX > trueX) {
                buttonX = trueX;
            }

            canvas.drawBitmap(buttonBmp, buttonX, 0, null);
        } else {

            if (currentState) {
                canvas.drawBitmap(buttonBmp, trueX, 0, null);
            } else {
                canvas.drawBitmap(buttonBmp, 0, 0, null);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                downX = (int) event.getX();
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                downX = (int) event.getX();
                isTouch = false;

                currentState = downX > (backgroundBmp.getWidth() / 2);      //根据按钮位置判断是true还是false

                if (mOnToggleButtonStateChanged != null) {
                    mOnToggleButtonStateChanged.onToggleButtonStateChanged(currentState);
                }
        }

        invalidate();           //刷新界面,调用onDraw

        return  true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(backgroundBmp.getWidth(), backgroundBmp.getHeight());
    }

    public interface ToggleButtonListener {
        void onToggleButtonStateChanged(Boolean currentState);
    }
    public void setOnToggleButtonListener(ToggleButtonListener listener) {
        this.mOnToggleButtonStateChanged = listener;
    }
}
