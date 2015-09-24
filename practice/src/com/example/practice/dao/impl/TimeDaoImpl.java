package com.example.practice.dao.impl;

import android.content.Context;
import com.example.practice.dao.TimeDao;
import com.example.practice.dao.base.DAOSupport;
import com.example.practice.dao.domain.UserTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/9/21.
 */
public class TimeDaoImpl extends DAOSupport<UserTime> implements TimeDao{


    public TimeDaoImpl(Context context) {
        super(context);
    }

    @Override
    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        List<UserTime> lists =  findAll();
        String time;
        if (lists.size() > 0) {
            UserTime userTime = lists.get(lists.size() - 1);
            long lastTime = userTime.getTime();
            Date date = new Date(lastTime);
            time =  df.format(date);
        } else {
            time = "第一次登陆";
        }

        return time;
    }
}
