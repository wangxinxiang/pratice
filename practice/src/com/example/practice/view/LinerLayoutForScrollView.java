package com.example.practice.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/7/7.
 */
public class LinerLayoutForScrollView extends LinearLayout{
    private WindowManager wm;

    public LinerLayoutForScrollView(Context context) {
        super(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = wm.getDefaultDisplay().getHeight(); //屏幕高度
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }

}
