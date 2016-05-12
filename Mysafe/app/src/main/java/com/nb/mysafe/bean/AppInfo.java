package com.nb.mysafe.bean;

import android.graphics.drawable.Drawable;

/**
 * @author 侯紫睿
 * @time 2016/5/8 0008  下午 6:58
 * @desc ${TODD}
 */
public class AppInfo {
    //程序的图标
    private Drawable icon;
    //程序的名字
    private String appname;
    //程序的包名
    private String apppackage;
    //程序占用空间
    private long  appsize;
    // 是否是用户软件
    private boolean isUser;
    private boolean isRom;

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApppackage() {
        return apppackage;
    }

    public void setApppackage(String apppackage) {
        this.apppackage = apppackage;
    }

    public long getAppsize() {
        return appsize;
    }

    public void setAppsize(long appsize) {
        this.appsize = appsize;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
