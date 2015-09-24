package com.example.practice.dao;

import com.example.practice.dao.base.Dao;
import com.example.practice.dao.domain.UserTime;

/**
 * Created by Administrator on 2015/9/21.
 * 可以放特有的接口
 */
public interface TimeDao extends Dao<UserTime>{

    String getTime();        //获取多久前登录过
}
