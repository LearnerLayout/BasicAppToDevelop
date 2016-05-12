package com.nb.mysafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.nb.mysafe.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 侯紫睿
 * @time 2016/5/8 0008  下午 9:04
 * @desc ${TODD}
 */
public class AppInfoProvide {
    public static  List<AppInfo> getMyAppInfo(Context context){
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> defAppInfoList = pm.getInstalledApplications(0);
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        for(ApplicationInfo defAppInfo : defAppInfoList){
            //获取app包名
            String appPackageName = defAppInfo.packageName;
            //获取app图标
            Drawable appIcon = defAppInfo.loadIcon(pm);
            //获取app名称
            String appName = defAppInfo.loadLabel(pm).toString();
            //获取app资源大小
            String sourceDir = defAppInfo.sourceDir;
            File file = new File(sourceDir);
            long appSize = file.length();

            AppInfo appInfo = new AppInfo();
            appInfo.setAppname(appName);
            appInfo.setApppackage(appPackageName);
            appInfo.setIcon(appIcon);
            appInfo.setAppsize(appSize);

            if((defAppInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1){
                //说明是系统软件
                appInfo.setUser(false);
            }else{
                appInfo.setUser(true);
            }
            if((defAppInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) ==1){
                //说明安装在sd卡里
                appInfo.setRom(false);
            }else{
                appInfo.setRom(true);
            }
            appInfos.add(appInfo);

        }
        return appInfos;
    }
}
