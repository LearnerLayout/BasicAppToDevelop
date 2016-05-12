package com.nb.mysafe;

import android.test.AndroidTestCase;

import com.nb.mysafe.db.dao.BlackNumberDao;

import java.util.Random;

/**
 * @author 侯紫睿
 * @time 2016/5/3 0003  下午 11:26
 * @desc ${TODD}
 */
public class TestAdd extends AndroidTestCase{
    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(getContext());
        String phone = "666666666";
        Random random = new Random();
        for(int i = 0;i<100;i++){
            dao.add(phone+i,String.valueOf(random.nextInt(3)+1));
        }
    }
}
