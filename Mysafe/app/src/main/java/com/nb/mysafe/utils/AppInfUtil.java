package com.nb.mysafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 *
 * @author 侯紫睿
 * @time 2016/5/2 0002  上午 12:25
 * @desc ${TODD}
 */
public class AppInfUtil {
    /**
     * 获取版本名
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String versionName=null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
             versionName = info.versionName;//版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int versionCode=0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;//版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
