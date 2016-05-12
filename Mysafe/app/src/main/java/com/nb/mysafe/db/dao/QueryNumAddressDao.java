package com.nb.mysafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author 侯紫睿
 * @time 2016/5/6 0006  下午 10:26
 * @desc ${拷贝完成address.db文件后,需要查询数据库里信息}
 */
public class QueryNumAddressDao {
    //根据号码查询归属地
    public static String queryNumAddress(Context context,String phone){
        Log.v("nb",context.getFilesDir().toString());
        Log.v("nb",context.getFilesDir().getAbsolutePath());
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().toString()+"/address.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        String location = "";
       if(phone.length()>=7) {
           phone = phone.substring(0, 7);
           Cursor cursor = db.rawQuery("select location from data2 where id =(select outkey from data1 where id =?);",
                   new String[]{phone});

           if (cursor.moveToNext()) {
               location = cursor.getString(0);
           }
           cursor.close();
       }
        db.close();
        return location;
    }
}
