package com.example.practice.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.practice.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 * 图案解锁
 */
public class LockPatternView extends View {

    private Point[][] points = new Point[3][3];     //9个点

    private Matrix matrix = new Matrix();       //进行缩放的矩阵

    private boolean isInit = false, isStart = false, movePoint = false;     //是否初始化过

    private int length, bitmapRadius;     //锁屏view 的边长

    private Bitmap pointNormal, pointError, pointPressed, linePressed, lineError;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ArrayList<Point> pressedPoints = new ArrayList<>();     //按下点的集合

    private float pressedX, pressedY;
    private Point pressedPoint = new Point();     //移动的点的坐标point

    private OnLockPatternListener listener;

    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initPoints();
            isInit = true;
        }
        points2Canvas(canvas);

        //画线
        int pressedPointsSize = pressedPoints.size();
        if (pressedPointsSize > 0) {
            Point pointA = pressedPoints.get(0);
            for (Point pointB : pressedPoints) {
                line2Canvas(canvas, pointA, pointB);
                pointA = pointB;
            }
            if (movePoint) {
                line2Canvas(canvas, pointA, pressedPoint);
            }
        }
    }

    /**
     * 初始化所有点
     */
    private void initPoints() {
        //根据横竖屏获取长度
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        length = Math.min(screenHeight, screenWidth);

        //图片资源
//        pointNormal = BitmapFactory.decodeResource(getResources(), R.drawable.lock_point_normal);
//        pointError = BitmapFactory.decodeResource(getResources(), R.drawable.lock_error);
//        pointPressed = BitmapFactory.decodeResource(getResources(), R.drawable.lock_point_pressed);
//        linePressed = BitmapFactory.decodeResource(getResources(), R.drawable.lock_line);
//        lineError = BitmapFactory.decodeResource(getResources(), R.drawable.lock_line_error);

//        bitmapRadius = pointError.getWidth() / 2;
        Point.r = length / 16;

        //点的坐标
        for (int i = 1; i <= points.length; i++) {
            for (int j = 1; j <= points[i - 1].length; j++) {
                points[i - 1][j - 1] = new Point(length / 4 * j, length / 4 * i);
            }
        }

        //设置密码样式
        int index = 1;
        for (Point[] points1 : points) {
            for (Point point1 : points1) {
                point1.index = index++;
            }
        }

    }

    /**
     * 对点进行绘制
     *
     * @param canvas 画布
     */
    private void points2Canvas(Canvas canvas) {
        for (Point[] point1 : points) {
            for (int j = 0; j < points.length; j++) {
                Point point = point1[j];
                if (point.state == Point.STATE_NOAMEL) {
                    paint.setColor(getResources().getColor(R.color.green_light));

//                    canvas.drawBitmap(pointNormal, point.x - bitmapRadius, point.y, paint);
                } else if (point.state == Point.STATE_ERROR) {
                    paint.setColor(getResources().getColor(R.color.red_light));
//                    canvas.drawBitmap(pointError, point.x - bitmapRadius, point.y, paint);
                } else {
                    paint.setColor(getResources().getColor(R.color.orange_light));
//                    canvas.drawBitmap(pointPressed, point.x - bitmapRadius, point.y, paint);
                }
                canvas.drawCircle(point.x, point.y, Point.r / 8, paint);
            }
        }
    }

    /**
     * 画线
     */
    private void line2Canvas(Canvas canvas, Point a, Point b) {
        paint.setStrokeWidth(Point.r / 16);
        if (a.state == Point.STATE_PRESSED) {
            paint.setColor(getResources().getColor(R.color.orange_light));
            canvas.drawLine(a.x, a.y,
                    b.x, b.y, paint);
        } else {
            paint.setColor(getResources().getColor(R.color.red_light));
            canvas.drawLine(a.x, a.y,
                    b.x, b.y, paint);
        }

//        float lineLength = (float) Point.distance(a, b);
//        float degress = getangle(a, b);
//        canvas.rotate(degress, a.x, a.y);           //进行旋转
//        if (a.state == Point.STATE_PRESSED) {
//            matrix.setScale(lineLength / linePressed.getWidth(), 1);
//            matrix.postTranslate(a.x - linePressed.getWidth()/2, a.y - linePressed.getHeight()/2);
//            canvas.drawBitmap(linePressed, matrix, paint);
//        } else {
//            matrix.setScale(lineLength / lineError.getWidth(), 1);
//            matrix.postTranslate(a.x - lineError.getWidth()/2, a.y - lineError.getHeight()/2);
//            canvas.drawBitmap(lineError, matrix, paint);
//        }
//        canvas.rotate(-degress, a.x, a.y);           //旋转回来

    }

    /**
     * 画线时获取线的角度
     */
    private float getangle(Point a, Point b) {
        return (float) Math.toDegrees(Math.atan2(b.y - a.y, b.x
                - a.x));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pressedX = event.getX();
        pressedY = event.getY();
        pressedPoint.x = pressedX;
        pressedPoint.y = pressedY;
        movePoint = false;

        Point point;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.startLockPattern();
                }
                resetPoint();       //重置
                point = pressedPoint();     //获取被点击的点
                if (point != null) {
                    isStart = true;         //如果按下的位置是点，就开始解锁，否则不开始
                    point.state = Point.STATE_PRESSED;
                    pressedPoints.add(point);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isStart) {
                    point = pressedPoint();
                    if (point != null) {
                        if (pressedPoints.contains(point)) {        //判断是否重复
                            movePoint = true;
                        } else {
                            point.state = Point.STATE_PRESSED;
                            pressedPoints.add(point);
                        }
                    } else {
                        movePoint = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStart) {
                    if (pressedPoints.size() < 2) {         //错误状态判断
                        errorPoint();
                        listener.onGestureEvent(null);
                    } else if (listener != null) {
                        StringBuilder pwd = new StringBuilder();
                        for (Point point1 : pressedPoints) {
                            pwd.append(point1.index);
                        }
                        listener.onGestureEvent(pwd.toString());
                    }

                }
                isStart = false;
                break;
        }

        postInvalidate();
        return true;
    }

    /**
     * 判断被点击的点
     */
    private Point pressedPoint() {
        for (Point[] point : points) {
            for (Point aPoint : point) {
                if (aPoint.isPressed(pressedX, pressedY)) {
                    return aPoint;
                }
            }
        }
        return null;
    }

    /**
     * 重置点
     */
    private void resetPoint() {
        for (Point point1 : pressedPoints) {
            point1.state = Point.STATE_NOAMEL;
        }
        pressedPoints.clear();
    }

    /**
     * 错误状态下的点
     */
    public void errorPoint() {
        for (Point point1 : pressedPoints) {
            point1.state = Point.STATE_ERROR;
        }
    }


    /**
     * 自定义点
     */
    public static class Point {
        public static int STATE_NOAMEL = 0;     //正常状态
        public static int STATE_PRESSED = 1;       //选中状态
        public static int STATE_ERROR = 2;      //错误状态
        public static float r;              //点的范围

        public float x, y;
        public int index = 0, state = 0;


        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * 两点之间的距离
         */
        public static double distance(Point a, Point b) {
            return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x)
                    + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
        }

        /**
         * 判断点是被触摸到
         */
        public boolean isPressed(float pressedX, float pressedY) {
            return Math.sqrt((pressedX - x) * (pressedX - x) + (pressedY - y) * (pressedY - y)) < r;
        }
    }

    /**
     * 监听器
     */
    public interface OnLockPatternListener {

        /**
         * 监听开始绘制图案
         */
        void startLockPattern();

        /**
         * 返回获取的密码
         */
        void onGestureEvent(String passWord);

        /**
         * 超过尝试次数
         */
//        void onUnmatchedExceedBoundary();
    }

    public void setListener(OnLockPatternListener listener) {
        this.listener = listener;
    }

    /**
     * 密码错误时的操作
     */
    public void errorPassword() {
        errorPoint();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                resetPoint();
                postInvalidate();
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);
    }
}
