package com.example.practice.ui;

import android.app.Service;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.example.practice.R;
import com.example.practice.adapter.FragmentAdapter;
import com.example.practice.fragment.FirstFragment;
import com.example.practice.service.FirstIntentService;
import com.example.practice.service.FirstService;
import com.example.practice.util.Constant;
import com.example.practice.view.Tba1ScrollViewContentView;

import java.util.ArrayList;

public class MyActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */

    private Tba1ScrollViewContentView scrollView;
    private View headView;
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentAdapter adapter;
    private TextView cursor;
    private int screenW;
    private Button startService,stopService,result,menu;
    private FirstService.MyBinder binder;
    private MessageReceive receiver;
    private ArrayList<String> phoneList = new ArrayList<String>();      //存放手机号码
    private int height, topScrollY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initFragment();
        initView();
        initService();
        setIndicate();
//        initSlidingMenu();

//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        FirstFragment firstFragment = new FirstFragment();
//        transaction.add(R.id.second_fragment, firstFragment);
//        transaction.commit();
    }

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.main_linear);
        headView = View.inflate(MyActivity.this, R.layout.head_view, null);
        linearLayout.addView(headView, 0);
        menu = (Button) headView.findViewById(R.id.btn4);
        scrollView = (Tba1ScrollViewContentView) findViewById(R.id.scrollView);

        //intentService
        Intent intent = new Intent(this, FirstIntentService.class);
        intent.putExtra("msg", "现在时间为:");
        receiveBroadcast();
        startService(intent);


        //service
        startService = (Button) headView.findViewById(R.id.btn1);
        stopService = (Button) headView.findViewById(R.id.btn2);
        result = (Button) headView.findViewById(R.id.btn3);

        viewPager = (ViewPager) this.findViewById(R.id.viewpager);

        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        initCurosr();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {
                int x = (position * screenW + positionOffsetPixels)/3;
                cursor.setX(x);

            }

            @Override
            public void onPageSelected(int position) {

                //实现SlidingMenu与viewpager兼容
                switch (position) {
                    case 2:
//                        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    default:
//                        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSlidingMenu().toggle();
//            }
//        });
    }

    private void initFragment() {
        for (int i = 0; i < 3; i++) {
            FirstFragment firstFragment = new FirstFragment();
            fragments.add(firstFragment);
        }
    }

    /**
     * 初始化指示
     */
    private void initCurosr() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        cursor = (TextView) findViewById(R.id.cursor);
        screenW = displayMetrics.widthPixels;   //获取屏幕分辨率宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW, displayMetrics.heightPixels);
        viewPager.setLayoutParams(params);
        cursor.setLayoutParams(new LinearLayout.LayoutParams(screenW / 3, 5));
    }

    /**
     * 设置indicate的滚动时动画效果
     */
    private void setIndicate() {
        final Button main_top = (Button) findViewById(R.id.main_top);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayout.setTranslationY(scrollView.getScrollY());
                height = main_top.getMeasuredHeight();
                Log.d("OnScrollChangedListener", scrollView.getScrollY() + "" + ";height=" + height);
            }
        });

        scrollView.setCallbacks(new Tba1ScrollViewContentView.Callbacks() {
            @Override
            public void onScrollChanged(int deltaY) {
                Log.d("onScrollChanged ----->", " linearLayout.getScrollY():" + (linearLayout.getScrollY()) + ";scrollView:" + scrollView.getScrollY());
                topScrollY -= deltaY;
                if (topScrollY > (scrollView.getScrollY()) || topScrollY < 0)  {
                    topScrollY = scrollView.getScrollY();
                } else if (topScrollY < (scrollView.getScrollY() - height)) {
                    topScrollY = scrollView.getScrollY() - height;
                }
                Log.d("onScrollChanged---->", "topScrollY:" + topScrollY);
                linearLayout.setTranslationY(topScrollY);

            }
        });
    }

    /**
     * 启动service
     */
    private void initService() {
        final Intent intent = new Intent(this, FirstService.class);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
            }
        });
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(conn);
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binder.getCount() + "";
                Toast.makeText(MyActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (FirstService.MyBinder) iBinder;
            Log.d("conn", "已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("conn", "未连接");
        }
    };

    /**
     * 接收广播
     */
    private class MessageReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Toast.makeText(MyActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
    private void receiveBroadcast() {
        IntentFilter filter = new IntentFilter(Constant.SERVICE_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MessageReceive();
        registerReceiver(receiver, filter);
    }

    /**
     * 后退返回桌面不退出程序
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 获取手机通讯号码
     */
    private void getPhone() {
        final Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        BaseAdapter adapter1 = new BaseAdapter() {
            @Override
            public int getCount() {
                return cursor.getCount();
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
            public View getView(int i, View view, ViewGroup viewGroup) {
                cursor.moveToPosition(i);
                CheckBox checkBox = new CheckBox(MyActivity.this);
                //获取号码并去掉中间的划线
                String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "");
                checkBox.setText(num);

                return checkBox;
            }
        };
    }


//    /**
//     * SlidingMenu的使用
//     */
//    private void initSlidingMenu() {
//        Fragment fragment = new FirstFragment();
//        setBehindContentView(R.layout.slidingmenu_fragment);
//        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, fragment).commit();
//        SlidingMenu slidingMenu = getSlidingMenu();
//        //设置侧滑菜单的位置，可选值LEFT , RIGHT , LEFT_RIGHT （两边都有菜单时设置）
//        slidingMenu.setMode(SlidingMenu.RIGHT);
//        // 设置触摸屏幕的模式，可选只MARGIN , CONTENT
//        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        //根据dimension资源文件的ID来设置阴影的宽度
//        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        //设置SlidingMenu离屏幕的偏移量
//        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        // 设置渐入渐出效果的值
//        slidingMenu.setFadeDegree(0.85f);
//
//        //设置右边（二级）侧滑菜单
//        slidingMenu.setSecondaryMenu(R.layout.slidingmenu_fragment);
//        Fragment secondFragment = new FirstFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, secondFragment).commit();
//    }
}
