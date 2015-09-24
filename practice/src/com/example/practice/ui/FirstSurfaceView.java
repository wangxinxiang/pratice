package com.example.practice.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import com.example.practice.R;
import com.example.practice.service.GetAllAppService;
import com.example.practice.view.MySlideMenu;
import com.example.practice.view.ToggleButton;

import java.util.List;

/**
 * Created by Administrator on 2015/7/1.
 */
public class FirstSurfaceView extends Activity{

    private Paint paint;
    private SurfaceHolder holder;
    private MySlideMenu slideMenu;
    private Button mirror, meitu, test1_draw, test1_girl, app_txwl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_surface_view);
//        initSurface();
        initSlideMenu();

        initView();
    }

    private void initView() {
        mirror = (Button) findViewById(R.id.test1_mirror);
        meitu = (Button) findViewById(R.id.test1_meitu);
        test1_draw = (Button) findViewById(R.id.test1_draw);
        test1_girl = (Button) findViewById(R.id.test1_girl);
        app_txwl = (Button) findViewById(R.id.app_txwl);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.take_photo);
        toggleButton.setOnToggleButtonListener(new ToggleButton.ToggleButtonListener() {
            @Override
            public void onToggleButtonStateChanged(Boolean currentState) {
                Intent intent = new Intent(FirstSurfaceView.this, MainActivity.class);
                startActivity(intent);
            }
        });

        test1_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstSurfaceView.this, BeautifulGirl.class);
                startActivity(intent);
            }
        });

        mirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstSurfaceView.this, MirrorSurface.class);
                startActivity(intent);
            }
        });
        meitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstSurfaceView.this, MeiTuXiuXiu.class);
                startActivity(intent);
            }
        });
        test1_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstSurfaceView.this, DrawSurface.class);
                startActivity(intent);
            }
        });
        app_txwl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLive = false;
                PackageManager packageManager = FirstSurfaceView.this.getPackageManager();
                List<PackageInfo> apps = GetAllAppService.getAllApps(FirstSurfaceView.this);
                for (PackageInfo packageInfo : apps) {
                    if ("com.example.txwl_first".equals(packageInfo.packageName)) {
                        isLive = true;
                        Intent intent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);
                        startActivity(intent);
                        break;
                    }
                }
                if (!isLive) {
                    Intent intent = new Intent(FirstSurfaceView.this, TestViewGroupActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 通知栏提醒
     */
    public void notification(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(FirstSurfaceView.this, MyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(FirstSurfaceView.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Notification notification = new Notification(R.drawable.after19, "通知栏信息", System.currentTimeMillis());
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(this, "标题", "内容", pendingIntent);

        //如果设置了RemoteViews，则不用再设置界面信息，只要设置RemoteViews界面
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.activity_main);
        Notification notification1 = new Notification.Builder(this)
                .setContentTitle("标题")
                .setContentText("内容")
                .setSmallIcon(R.drawable.pre19)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.a))
                .setVibrate(new long[] {100L, 200L, 100L})
                .setTicker("通知栏信息")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
//                .setContent(remoteViews)
                .build();

        notificationManager.notify(0, notification1);
    }


    private void initSlideMenu() {
        slideMenu = (MySlideMenu) findViewById(R.id.slidemenu);

    }

//    /**
//     * 初始化surfaceView
//     */
//    private void initSurface() {
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
//        holder = surfaceView.getHolder();
//        paint = new Paint();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                Canvas canvas = holder.lockCanvas();
//                Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background);       //获取背景图片
//                canvas.drawBitmap(background, 0, 0,null);
//                holder.unlockCanvasAndPost(canvas);
//                //重新锁一次，“持久化”上次的所绘制的内容,因为区域为0，所以对上面的绘制没有影响
//                holder.lockCanvas(new Rect(0,0,0,0));
//                holder.unlockCanvasAndPost(canvas);
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//
//            }
//        });
//
//        surfaceView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    int x = (int) motionEvent.getX();
//                    int y = (int) motionEvent.getY();
//                    Canvas canvas = holder.lockCanvas(new Rect(x - 50, y - 50, x + 50, y + 50));
//                    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background);       //获取背景图片
//                    canvas.drawBitmap(background, 0, 0,null);
//                    canvas.save();      //保存canvas当前状态.平移、缩放、旋转等操作只对save（）和restore（）作用域之间的代码有效。
//                    canvas.rotate(30, x, y);
//                    paint.setColor(Color.GREEN);
//                    canvas.drawRect(x - 40, y - 40, x, y, paint);       //绘制绿色方块
//                    canvas.restore();       //回复先前的状态
//                    paint.setColor(Color.BLUE);
//                    canvas.drawRect(x, y, x + 40, y + 40, paint);       //绘制蓝色方块
//                    holder.unlockCanvasAndPost(canvas);
//                }
//                return false;
//            }
//        });
//    }
}
