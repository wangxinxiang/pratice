package com.example.practice.view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/30.
 */
public class TestImageView extends ImageView{
    public TestImageView(Context context) {
        super(context);
    }

    public TestImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(200, 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Drawable drawable = getDrawable();
        Bitmap img = ((BitmapDrawable) drawable).getBitmap();
        Bitmap scaleImg = Bitmap.createScaledBitmap(img, 200, 200 ,true);
        BitmapShader bitmapShader = new BitmapShader(scaleImg, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);
        canvas.drawCircle(100, 100, 100, paint);
    }
}
