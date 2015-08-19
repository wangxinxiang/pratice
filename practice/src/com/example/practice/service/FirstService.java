package com.example.practice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Administrator on 2015/6/21.
 */
public class FirstService extends Service{

    private int count = 1;
    private boolean quite = true;
    private MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public int getCount() {
            return count;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (quite) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }.start();

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quite = false;
    }


}
