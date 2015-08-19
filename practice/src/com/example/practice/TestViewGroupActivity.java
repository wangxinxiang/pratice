package com.example.practice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.practice.R;

import java.io.*;
import java.util.Formatter;

/**
 * Created by Administrator on 2015/7/14.
 */
public class TestViewGroupActivity extends Activity implements View.OnClickListener{

    private TextView text1,text2;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_viewgroup);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_test);
        //网页显示
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("http://www.94.com/txwl/index.html");
//
//        text1 = (TextView) findViewById(R.id.test1);
////        text2 = (TextView) findViewById(R.id.test2);
//        setText();

    }

    @Override
    public void onClick(View v) {

    }

//    /**
//     * 获取SD卡容量
//     */
//    private void setText() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File file = Environment.getExternalStorageDirectory();
//            StatFs statFs = new StatFs(file.getPath());
//            long blockSize = statFs.getBlockSizeLong();                 //获取block的SIZE
//            long totalBlock = statFs.getBlockCountLong();               //获取BLOCK数量
//            long avaliaBlock = statFs.getAvailableBlocksLong();             //可用的block数量
//
//            text1.setText(formatSize(blockSize*avaliaBlock));
//            text2.setText(formatSize(blockSize*totalBlock));
//
//            try {
//                FileOutputStream fos = openFileOutput("info.txt", MODE_ENABLE_WRITE_AHEAD_LOGGING);
//                fos.write("私有模式".getBytes());
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            File file1 = new File("data/data/com.example.practice/files/info.txt");
//            try {
//                FileInputStream fis = new FileInputStream(file1);
//                BufferedReader bis = new BufferedReader(new InputStreamReader(fis));
//                String text = bis.readLine();
//                Toast.makeText(TestViewGroupActivity.this, text, Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String formatSize(long size) {
//        return size/1024/1024 + "";
//    }
}
