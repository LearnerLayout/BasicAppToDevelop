package com.nb.mysafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nb.mysafe.bean.BlackNumberInfo;
import com.nb.mysafe.db.BlackNumberInfoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 侯紫睿
 * @time 2016/5/3 0003  下午 2:56
 * @desc ${TODD}
 */
public class BlackNumberDao {

    //构造方法中初始化helper创建出数据库,并且初始化表了
    private BlackNumberInfoHelper helper;
    private String table = "blackNumberInfo";

    public BlackNumberDao(Context context) {
        this.helper = new BlackNumberInfoHelper(context);
    }

    //添加黑名单的方法
    public boolean add(String phone, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        long insert = db.insert(table, null, values);
        db.close();
        if (insert == -1) {
            return false;
        }
        return true;
    }

    //根据号码删除的方法
    public boolean delete(String phone) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(table, "phone=?", new String[]{phone});
        db.close();
        if (delete == 0) {
            return false;
        }
        return true;
    }

    //根据号码修改mode
    public boolean update(String phone, String newMode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", newMode);
        int update = db.update(table, contentValues, "phone=?", new String[]{phone});
        db.close();
        if (update == 0) {
            return false;
        }
        return true;
    }

    //根据号码查找出该号码对应的黑名单状态
    public String find(String phone) {
        String mode = null;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(table, null, "phone=?", new String[]{phone}, null, null, null);
        while (cursor.moveToNext()) {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    //查询出所有的黑名单
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<BlackNumberInfo> list = new ArrayList<>();
        Cursor cursor = db.query(table, null, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            info.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    //根据指定索引,查询指定条数信息
    public List<BlackNumberInfo> findPart(int index, int maxCount) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blackNumberInfo order by _id desc limit ? offset ?", new
                String[]{String.valueOf(maxCount), String.valueOf(index)});
        ArrayList<BlackNumberInfo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            info.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            list.add(info);
        }
        //记得关闭cursor与db
        cursor.close();
        db.close();
        return list;
    }
    //创建一个获取这个表里的总数据条目
    public int getCount(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blackNumberInfo ", null);

        return 0;

    }
}
