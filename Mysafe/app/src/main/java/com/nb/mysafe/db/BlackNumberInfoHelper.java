package com.nb.mysafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 侯紫睿
 * @time 2016/5/3 0003  下午 2:54
 * @desc ${TODD}
 */
public class BlackNumberInfoHelper extends SQLiteOpenHelper {
    //此处创建数据库名称
    public BlackNumberInfoHelper(Context context) {
        super(context, "520it.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //此处创建表
        db.execSQL("CREATE TABLE blackNumberInfo (_id integer primary key autoincrement,phone varchar(20),mode " +
                "varchar(5))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
