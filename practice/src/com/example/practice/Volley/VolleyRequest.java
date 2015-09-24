package com.example.practice.Volley;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.util.Map;

public class VolleyRequest<T> {
    public JosnRequest<T> stringRequest;
    public Context context;

    public void RequestGet(Context context, String url, String tag, VolleyInterface<T> vif, Class<T> clazz) {

//        ZQBApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new JosnRequest<T>(Request.Method.GET, url, vif.loadingListener(), vif.errorListener(), clazz);
        stringRequest.setTag(tag);
//        ZQBApplication.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }

    public void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyInterface<T> vif, Class<T> clazz) {
//        ProgressDialog.createDialog(context);
//        ProgressDialog.setMessage("正在与服务器连接....");
//        ZQBApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new JosnRequest<T>(Request.Method.POST, url, vif.loadingListener(), vif.errorListener(), clazz);
        stringRequest.mParams.putAll(params);
        stringRequest.setTag(tag);
//        ZQBApplication.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }
}
