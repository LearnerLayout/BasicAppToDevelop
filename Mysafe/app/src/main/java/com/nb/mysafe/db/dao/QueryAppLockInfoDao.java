package com.nb.mysafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nb.mysafe.db.AppLockInfoHelper;

/**
 * @author 侯紫睿
 * @time 2016/5/10 0010  下午 10:42
 * @desc ${TODD}
 */
public class QueryAppLockInfoDao {

    private final AppLockInfoHelper mHelper;
    private String table = "appLocked";

    public QueryAppLockInfoDao(Context context) {
        mHelper = new AppLockInfoHelper(context);
    }


    public boolean add(String packageName){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packageName",packageName);
        long insert = db.insert(table, null, values);
        if(insert!=-1){
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public boolean delete(String packageName){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete(table, "packageName=?", new String[]{packageName});
        if(delete!=0){
            db.close();
            return true;
        }
        db.close();
        return  false;
    }


    public boolean queryWhetherLocked(String packageName) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(table, new String[]{"packageName"}, "packageName=?", new String[]{packageName}, null,
                null, null);
        if(cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
