package com.example.practice.dao.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.practice.dao.TimeDBHelper;
import com.example.practice.dao.annotation.Column;
import com.example.practice.dao.annotation.ID;
import com.example.practice.dao.annotation.TableName;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public abstract class DAOSupport<M> implements Dao<M> {

    protected TimeDBHelper helper;
    protected Context context;
    protected SQLiteDatabase db;

    public DAOSupport(Context context) {
        helper = new TimeDBHelper(context);
        this.context = context;
        db = helper.getWritableDatabase();
    }

    @Override
    public long insert(M m) {
        ContentValues values = new ContentValues();
        fillColumn(m, values);
        return db.insert(getTableName(), null, values);
    }


    @Override
    public int delete(Serializable id) {
        return 0;
    }

    @Override
    public int update(M m) {
        ContentValues values = new ContentValues();
        fillColumn(m, values);
        return db.update(getTableName(), values, "_id=?", new String[]{getId(m)});
    }


    @Override
    public List<M> findAll() {
        List<M> lists = null;
        Cursor cursor = db.query(getTableName(), null, null, null, null, null, null);
        if (cursor != null) {
            lists = new ArrayList<M>();
            while (cursor.moveToNext()) {
                M m = getInstance();
                fillField(cursor, m);
                lists.add(m);
            }
        }
        return lists;
    }

    @Override
    public List<M> findByContidion(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<M> lists = null;
        Cursor cursor = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor != null) {
            lists = new ArrayList<M>();
            while (cursor.moveToNext()) {
                M m = getInstance();
                fillField(cursor, m);
                lists.add(m);
            }
        }
        return lists;
    }

    /**
     * 获取表名
     */
    private String getTableName() {
        M m = getInstance();
        if (m == null) {
            return "";
        }
        TableName tableName = m.getClass().getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return "";
    }

    private M getInstance() {
        Class clazz = getClass();       //获取继承此DAOSupport的TimeDaoImpl类
        Type superclass = clazz.getGenericSuperclass();     //获取支持泛型的孩子的父类,也就是本身DAOSupport
        if (superclass != null && superclass instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();   //获取泛型
            try {
                return (M) ((Class) types[0]).newInstance(); //将type进行强转成Class，然后将其实例化成M
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 将对象中的信息插入数据库中
     */
    private void fillColumn(M m, ContentValues values) {
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);      //必须设置，否则无法获得属性的值

            ID id = field.getAnnotation(ID.class);      //如果是id，则不用进行插入，因为id是自增的
            if (id != null && id.autoIncrement()) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);

            if (column != null) {
                String key = column.value();
                try {
                    String value = field.get(m).toString();
                    if (field.getType() == int.class) {             //如果是整型就把它强转
                        values.put(key, Integer.parseInt(value));
                    } else {
                        values.put(key, value);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取数据库的主键
     */
    private String getId(M m) {
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);      //必须设置，否则无法获得属性的值

            ID id = field.getAnnotation(ID.class);
            if (id != null) {
                try {
                    return field.get(m).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据cursor填充数据
     *
     * @param cursor 数据库指针
     * @param m      要存储的对象
     */
    private void fillField(Cursor cursor, M m) {
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);      //必须设置，否则无法获得属性的值

            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                String key = column.value();
                try {
                    int columnIndex = cursor.getColumnIndex(key);
                    String values = cursor.getString(columnIndex);
                    if (field.getType() == int.class) {
                        field.set(m, Integer.parseInt(values));
                    } else if (field.getType() == long.class){
                        field.set(m, Long.parseLong(values));
                    } else if (field.getType() == String.class) {
                        field.set(m, values);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
