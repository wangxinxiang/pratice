package com.example.practice.Volley;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class VolleyInterface<T>
{
    public Response.Listener<T> listener;
    public Response.ErrorListener errorListener;

    public abstract void onMySuccess(T result);
    public abstract  void onMyError(VolleyError error);

//    public VolleyInterface (Response.Listener<String> listener, Response.ErrorListener errorListener)
//    {
//        this.listener = listener;
//        this.errorListener = errorListener;
//    }

    public Response.Listener<T> loadingListener()
    {
        listener = new Response.Listener<T>() {
            @Override
            public void onResponse(T t) {
                onMySuccess(t);
            }
        };

        return listener;
    }

    public Response.ErrorListener errorListener()
    {
//        ProgressDialog.Dismiss();
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ZQBApplication.getInstance().showTextToast("网络错误");             //网络请求失败直接打印网络错误，不需要再处理
                onMyError(error);
            }
        };
        return errorListener;
    }
}
