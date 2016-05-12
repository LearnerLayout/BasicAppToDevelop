package com.nb.mysafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;

import com.nb.mysafe.db.dao.BlackNumberDao;

/**
 * @author 侯紫睿
 * @time 2016/5/5 0005  下午 5:48
 * @desc ${TODD}
 * <meta-data
android:name="com.google.android.gms.version"
android:value="@integer/google_play_services_version"/>
 */
public class BlackNumService extends Service {

    private BlackNumberDao mDao;
    private BlackNumSmsReceiver mBlackNumSmsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
          //创建一个receiver用来接受短信广播,从而调用dao中方法判断是否拦截
        mDao = new BlackNumberDao(getApplicationContext());
        //BroadcastReceiver是抽象的,不能new,创建一个内部类继承它
        mBlackNumSmsReceiver = new BlackNumSmsReceiver();
        //动态注册自定义receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mBlackNumSmsReceiver,filter);
    }
    //BroadcastReceiver是抽象的,不能new,创建一个内部类继承它
    class BlackNumSmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus =(Object[]) intent.getExtras().get("pdus");
            for(Object obj :pdus){
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                String phone = message.getOriginatingAddress();
                String mode = mDao.find(phone);
                if(mode.equals("2") || mode.equals("3")){
                    abortBroadcast();
                    Log.v("nb","chenggong");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBlackNumSmsReceiver);
        mBlackNumSmsReceiver=null;
        super.onDestroy();
    }
}

