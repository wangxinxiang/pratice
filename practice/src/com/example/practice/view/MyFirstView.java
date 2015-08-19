package com.example.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.example.practice.R;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/6/28.
 */
public class MyFirstView extends View{
    private int src;
    private String text;

    public MyFirstView(Context context) {
        super(context);
    }

    public MyFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int textId = attrs.getAttributeResourceValue(null, "Text", 0);
        src = attrs.getAttributeResourceValue(null, "Src", 0);
        text = context.getResources().getText(textId).toString();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Path path = new Path();
        path.moveTo(0, 0);
        for (int i = 0; i < 10; i++) {
            path.lineTo(i * 30, (float) (Math.random() * 30));
        }
        InputStream is = getResources().openRawResource(src);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        int imgHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawText(text, 0 ,imgHeight, paint);

    }
}
