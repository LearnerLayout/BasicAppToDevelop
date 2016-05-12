package com.nb.mysafe.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * @author 侯紫睿
 * @time 2016/5/2 0002  下午 3:06
 * @desc 创建一个跳转工具类
 */
public class IntentUtil {
    public static void startActivityForDelayAndFinish(final Activity activity,
                                                      final Class<?> clz, final long time) {
       new Thread(){
           @Override
           public void run() {
               try {
                   Thread.sleep(time);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               Intent intent = new Intent(activity, clz);
               activity.startActivity(intent);
               activity.finish();
           }
       }.start();
    }
    /**
     * 关闭当前的activity
     * @param activity
     * @param claz
     */
    public static  void startIntentAnFinish(final Activity activity, final Class<?> claz){
        Intent intent=new Intent(activity,claz);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 开启新的activity
     * @param activity
     * @param claz
     */
    public static  void startIntent(final Activity activity, final Class<?> claz){
        Intent intent=new Intent(activity,claz);
        activity.startActivity(intent);
    }
}
