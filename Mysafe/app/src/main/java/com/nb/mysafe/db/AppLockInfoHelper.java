package com.nb.mysafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 侯紫睿
 * @time 2016/5/10 0010  下午 10:36
 * @desc ${TODD}
 */
public class AppLockInfoHelper extends SQLiteOpenHelper {
    public AppLockInfoHelper(Context context) {
        super(context, "appLock.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE appLocked (_id INTEGER PRIMARY KEY AUTOINCREMENT,packageName varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
