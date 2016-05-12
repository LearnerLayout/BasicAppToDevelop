package com.nb.mysafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.db.dao.QueryNumAddressDao;

/**
 * @author 侯紫睿
 * @time 2016/5/6 0006  下午 11:07
 * @desc ${这个服务开启后,在来电时,弹出自定义吐司,显示来电归属地,打电话时也弹出吐司显示归属地}
 * 来电是需要服务监听
 * 打电话是发送广播,需要广播接受者
 */
public class QueryPhoneAddressService extends Service {

    private MyCallReceiver mMyCallReceiver;
    private WindowManager mWm;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private WindowManager.LayoutParams mParams;
    private View mToastView;
    private TelephonyManager mTm;
    private MyPhoneListener mMyPhoneListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //动态注册打电话的广播接受者
        registerMyReceiver();
        //设置一个电话监听,电话状态改变都可以触发
        mTm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        mMyPhoneListener = new MyPhoneListener();
        mTm.listen(mMyPhoneListener,PhoneStateListener.LISTEN_CALL_STATE);
    }


    //1,写一个弹出吐司的方法,在去电,或来电时调用该方法
    public void showMyToast(String address) {
        //通过sp里的x,y值,设定当前吐司位置
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mEditor = mSp.edit();
        //首先通过系统服务获取一个windowManager
        mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        mParams.gravity = Gravity.LEFT + Gravity.TOP;
        //获取到当前x,y值
        mParams.x = mSp.getInt("lastX", 0);
        mParams.y = mSp.getInt("lastY", 0);
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//含义指不占据它窗口之外的焦点
        //创建一个自定义view
        mToastView = View.inflate(getApplicationContext(), R.layout.mytotast, null);

        //获取到内部textView
        TextView tvMyToast = (TextView) mToastView.findViewById(R.id.tv_myToast_address);
        tvMyToast.setText( address);
        mWm.addView(mToastView, mParams);
        mToastView.setOnTouchListener(new View.OnTouchListener() {

            private int mStartY;
            private int mStartX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        //手指按下时,获取到按下位置的坐标作为初始坐标
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //手指滑动时,时刻获取当前坐标,与开始坐标做差.将差值给param,实时更新toast位置
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        int dx = newX - mStartX;
                        int dy = newY - mStartY;
                        mParams.x += dx;
                        mParams.y += dy;
                        if (mParams.x < 0) {
                            mParams.x = 0;
                        }
                        if (mParams.y < 0) {
                            mParams.y = 0;
                        }
                        if (mParams.x > mWm.getDefaultDisplay().getWidth() - mToastView.getWidth()) {
                            mParams.x = mWm.getDefaultDisplay().getWidth() - mToastView.getWidth();
                        }
                        if (mParams.y > mWm.getDefaultDisplay().getWidth() - mToastView.getWidth()) {
                            mParams.y = mWm.getDefaultDisplay().getWidth() - mToastView.getWidth();
                        }

                        mWm.updateViewLayout(mToastView, mParams);
                        mStartX = newX;
                        mStartY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //手指离开时,将此刻获取的坐标,设为最终坐标,保存到sp中
                        int lastX = (int) event.getRawX();
                        int lastY = (int) event.getRawY();
                        mEditor.putInt("lastX", lastX);
                        mEditor.putInt("lastY", lastY);
                        mEditor.commit();
                        break;

                }

                return true;
            }
        });

    }
    //2,注册一个去电时的广播接受者,在往外打电话时接收到这个广播,显示出自定义吐司

    //自定义一个接受打电话广播的广播接受者
    class MyCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到这个广播后弹出吐司
            String phone = getResultData();
            String address = QueryNumAddressDao.queryNumAddress(getApplicationContext(),phone);
            showMyToast(address);
        }
    }

    //创建一个电话状态监听者
    class MyPhoneListener extends PhoneStateListener {
        //监听电话的状态

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                if(mToastView != null){
                   mWm.removeView(mToastView);
                }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = QueryNumAddressDao.queryNumAddress(getApplicationContext(), incomingNumber);
                    showMyToast(address);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mMyCallReceiver);
        mMyCallReceiver = null;
        mTm.listen(mMyPhoneListener,PhoneStateListener.LISTEN_NONE);
        mMyPhoneListener= null;
        super.onDestroy();
    }


    //提取出的方法
    private void registerMyReceiver() {
        mMyCallReceiver = new MyCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mMyCallReceiver, filter);
    }
}
