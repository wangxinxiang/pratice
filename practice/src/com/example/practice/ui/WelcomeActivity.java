package com.example.practice.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.dao.TimeDBHelper;
import com.example.practice.dao.domain.UserTime;
import com.example.practice.dao.impl.TimeDaoImpl;
import com.example.practice.util.Constant;
import com.example.practice.view.RefreshforListView;
import com.example.practice.view.ToggleButton;

/**
 * Created by Administrator on 2015/7/3.
 */
public class WelcomeActivity extends Activity {

    private ToggleButton toggleButton;
    private RefreshforListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom);
        saveUserTime();
        initView();
        initListener();
        setScreenSize();
    }

    private void initView() {
        listView = (RefreshforListView) findViewById(R.id.my_refresh_listview);
        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new RefreshforListView.OnRefreshListener() {
            @Override
            public void onTopRefresh() {
                refreshHandle.postDelayed(null, 3000);
            }

            @Override
            public void onFootRefresh() {
                refreshHandle.postDelayed(null, 2000);
            }
        });

        TextView welcome = (TextView) findViewById(R.id.welcome);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        welcome.startAnimation(animation);

        toggleButton = (ToggleButton) this.findViewById(R.id.toggle_button);

        String lastTime = getLastTime();
        Toast.makeText(WelcomeActivity.this, "上次登录时间为" + lastTime, Toast.LENGTH_LONG).show();
    }

    private void initListener() {
        toggleButton.setOnToggleButtonListener(new ToggleButton.ToggleButtonListener() {
            @Override
            public void onToggleButtonStateChanged(Boolean currentState) {
                if (currentState) {
                    handler.postDelayed(null, 3000);
                    Toast.makeText(WelcomeActivity.this, "3秒后进入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(WelcomeActivity.this, MyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    };


    Handler refreshHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            listView.overTopRefresh();
            listView.overFootRefresh();
        }
    };

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null;
            if (convertView == null) {
                tv = new TextView(WelcomeActivity.this);
            } else {
                tv = (TextView) convertView;
            }

            tv.setText("第" + position);
            tv.setTextSize(18);
            return tv;
        }
    }

    /**
     * 储存使用时间
     */
    private void saveUserTime() {
        TimeDaoImpl dao = new TimeDaoImpl(this);
        UserTime userTime = new UserTime();
        long time = System.currentTimeMillis();
        userTime.setTime(time);
        userTime.setContent("第一次储存");
        dao.insert(userTime);
    }

    private String getLastTime() {
        TimeDaoImpl dao = new TimeDaoImpl(this);
        return dao.getTime();
    }

    /**
     * 获取屏幕尺寸并储存
     */
    private void setScreenSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Constant.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Constant.SCREEN_WIDTH = displayMetrics.widthPixels;
    }
}
