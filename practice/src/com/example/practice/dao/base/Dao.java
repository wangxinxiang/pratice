package com.example.practice.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public interface Dao<M> {

    long insert(M m);

    int delete(Serializable id);    //int long String

    int update(M m);

    List<M> findAll();

    /**
     * 按条件查找
     */
    List<M> findByContidion(String table, String[] columns, String selection,
                    String[] selectionArgs, String groupBy, String having,
                    String orderBy);
}
