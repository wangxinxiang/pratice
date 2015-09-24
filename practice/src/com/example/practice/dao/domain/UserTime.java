package com.example.practice.dao.domain;

import com.example.practice.dao.TimeDBHelper;
import com.example.practice.dao.annotation.Column;
import com.example.practice.dao.annotation.ID;
import com.example.practice.dao.annotation.TableName;

/**
 * Created by Administrator on 2015/9/21.
 */
@TableName(TimeDBHelper.TABLE_NAME)
public class UserTime {

    @ID(autoIncrement = true)
    @Column(TimeDBHelper.TABLE_ID)
    private int id;
    @Column(TimeDBHelper.TABLE_CONTENT)
    private String content;
    @Column(TimeDBHelper.TABLE_TIME)
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
