package com.example.practice.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import com.example.practice.util.Constant;

/**
 * Created by Administrator on 2015/6/22.
 */
public class FirstIntentService extends IntentService{


    public FirstIntentService() {
        super("yyyyyyyyyyy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String msg = intent.getStringExtra("msg");

        for (int i = 0; i < 10; i++) {
            String resultTxt = msg + " " + DateFormat.format("MM/dd/yy hh:mm:ss", System.currentTimeMillis());
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Constant.SERVICE_ACTION);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("msg", resultTxt);
            sendBroadcast(broadcastIntent);
            SystemClock.sleep(1000);
        }
    }
}
