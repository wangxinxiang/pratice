package com.example.practice.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/11/30.
 */
public class CircleImageDrawable extends Drawable{

    private Paint mPaint;
    private Bitmap mBitmap;
    private int radius;

    public CircleImageDrawable(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);
        int width = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        radius = width / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return 2*radius;
    }

    @Override
    public int getIntrinsicHeight() {
        return 2*radius;
    }
}
