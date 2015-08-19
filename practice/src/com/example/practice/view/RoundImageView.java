package com.example.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.practice.R;

/**
 * Created by Administrator on 2015/6/29.
 */
public class RoundImageView extends ImageView{
    private int mBorderThickness = 0;       //边框边的宽
    private int defaultColor = 0xFFF;          //边框的默认颜色
    private int mBorderInsideColor = 0;         //内边框的颜色
    private int mBorderOutsideColor = 0;        //外边框的颜色
    private int width,height;       //图片的宽，高

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.RoundImageView);
        mBorderThickness = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_border_thickness, 0);
        mBorderInsideColor = typedArray.getColor(R.styleable.RoundImageView_border_inside_color, defaultColor);
        mBorderOutsideColor = typedArray.getColor(R.styleable.RoundImageView_border_outsize_color, defaultColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //获取图片
        Drawable drawable = getDrawable();
        if (drawable == null) {
            Log.d("-------------->", "没有drawable");
            return;
        }
        if (getHeight() == 0 || getWidth() == 0) {
            return;
        }
        this.measure(0, 0);
        Bitmap bitmap = ( (BitmapDrawable) drawable).getBitmap();
        Log.d("-------------->", bitmap.toString());

        //获取图片参数
        if (width == 0) {
            width = bitmap.getWidth();
            Log.d("-------------->", "width:" + width);
        }
        if (height == 0){
            height = bitmap.getHeight();
            Log.d("-------------->", "height:" + height);
        }

        //获取圆形图片的半径
        int radius = (width > height ? height : width) / 2;
        Log.d("-------------->", "radius:" + radius);

        //保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）
        if (width != 0 && height != 0) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        width, height);
        setLayoutParams(params);
        }

        //绘制边框
        drawCircleBorder(canvas, radius, mBorderInsideColor);
        drawCircleBorder(canvas, radius + mBorderThickness, mBorderOutsideColor);

        //绘制圆图
        Bitmap roundImage = drawRoundImage(bitmap, radius);
        canvas.drawBitmap(roundImage, mBorderThickness, mBorderThickness, null);
    }

    /**
     * 绘制圆形图片
     */
    private Bitmap drawRoundImage(Bitmap bitmap, int radius) {
        Bitmap scaledBmp;
        int diameter = radius * 2;      //直径

        //截取正方形图片，以便截取圆形图片
        int bmpWidth = bitmap.getWidth();
        int bmpHieght = bitmap.getHeight();
        int squareSide;         //截取的正方形的边长
        int x,y;                //被截取图片的截取位置
        if (bmpWidth > bmpHieght) {     //宽大于高
            squareSide = bmpHieght;
            x = (bmpWidth - bmpHieght) / 2;
            y = 0;
        } else {            //高大于宽
            squareSide = bmpWidth;
            x = 0;
            y = (bmpHieght - bmpWidth) / 2;
        }
        Bitmap squareBmp = Bitmap.createBitmap(bitmap, x, y ,squareSide, squareSide);       //截取出正方形

        //对正方形图片缩放到所需圆的直径大小
        scaledBmp = Bitmap.createScaledBitmap(squareBmp, diameter, diameter, true);

        //使用canvas绘制出圆形图片
        Bitmap roundBmp = Bitmap.createBitmap(scaledBmp.getWidth(), scaledBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBmp);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);        //它会将原始颜色的过渡处根据两边的色值进行一些改变，从而让颜色过渡更加的柔和，让人觉得是平滑的过渡；
        paint.setDither(true);              //图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示速度
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));      //设置图层混合模式

//        Rect rect = new Rect(0, 0, scaledBmp.getWidth(), scaledBmp.getHeight());
//        canvas.drawBitmap(scaledBmp, rect, rect, paint);        //将图片画出

        //利用位图渲染画出图片
        BitmapShader bitmapShader = new BitmapShader(scaledBmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);

        canvas.drawCircle(scaledBmp.getWidth() / 2, scaledBmp.getHeight() / 2, radius, paint);      //绘制圆形图片

        scaledBmp = null;
        squareBmp = null;
        bitmap = null;
        return roundBmp;
    }

    /**
     * 绘制边框
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);         //空心
        paint.setStrokeWidth(mBorderThickness);         //设置框的宽度
        canvas.drawCircle(width / 2, height / 2, radius, paint);
    }
}
