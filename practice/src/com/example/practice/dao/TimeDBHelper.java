package com.example.practice.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/21.
 */
public class TimeDBHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "historytime";    //表名
    private static final int VERSION = 1;       //数据库版本号

    public static final String TABLE_ID = "_id";
    public static final String TABLE_TIME = "usertime";    //存入的时间是int型,所以存毫秒就行了
    public static final String TABLE_CONTENT = "content";

    public TimeDBHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_TIME + " INTEGER," + TABLE_CONTENT + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
