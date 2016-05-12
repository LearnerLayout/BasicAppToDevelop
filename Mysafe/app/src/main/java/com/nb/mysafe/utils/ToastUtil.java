package com.nb.mysafe.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * @author 侯紫睿
 * @time 2016/5/2 0002  下午 11:58
 * @desc ${TODD}
 */
public class ToastUtil {
    public static void show(final Activity activity,final String text){
        if("main".equalsIgnoreCase(Thread.currentThread().getName())){
            Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
        }else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
