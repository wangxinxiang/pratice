package com.example.practice.Volley;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/10.
 */
public class JosnRequest<T> extends Request<T>{
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    public Map<String, String> mParams = new HashMap<String, String>();         //要放的参数

    public JosnRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> clazz) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }


    @Override
    public Map<String, String> getParams() throws AuthFailureError
    {
        return mParams;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            /**
             * 得到返回的数据
             */
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            /**
             * 转化成对象
             */
            return Response.success(gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

    @Override
    protected String getParamsEncoding() {
        return super.getParamsEncoding();
    }
}
