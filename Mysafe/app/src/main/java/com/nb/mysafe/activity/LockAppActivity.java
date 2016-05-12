package com.nb.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.bean.AppInfo;
import com.nb.mysafe.db.dao.QueryAppLockInfoDao;
import com.nb.mysafe.engine.AppInfoProvide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 侯紫睿
 * @time 2016/5/10 0010  下午 9:45
 * @desc ${程序加密界面}
 */
public class LockAppActivity extends Activity {
    private TextView tv_appLock_left;
    private TextView tv_appLock_right;
    private LinearLayout ll_locked;
    private TextView tv_locked;
    private ListView lv_locked;
    private LinearLayout ll_unlocked;
    private TextView tv_unlocked;
    private ListView lv_unlocked;
    private QueryAppLockInfoDao mDao;
    private List<AppInfo> mMyAppInfos;
    private ArrayList<AppInfo> mLockedList;
    private ArrayList<AppInfo> mUnlockedList;
    private boolean mWhetherInLockPage;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mLockedAdapter = new MyAdapter(true);
            mUnlockedAdapter = new MyAdapter(false);
            lv_locked.setAdapter(mLockedAdapter);
            lv_unlocked.setAdapter(mUnlockedAdapter);
            //            mUnlockedAdapter.notifyDataSetChanged();
            //            mLockedAdapter.notifyDataSetChanged();
        }
    };
    private MyAdapter mLockedAdapter;
    private MyAdapter mUnlockedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        //初始化各个控件
        initiateView();
        //获取未加锁软件集合,与加锁软件集合
        getData();
    }


    //初始化各个数据
    private void initiateView() {
        tv_appLock_left = (TextView) findViewById(R.id.tv_appLock_left);
        tv_appLock_right = (TextView) findViewById(R.id.tv_appLock_right);
        ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
        tv_locked = (TextView) findViewById(R.id.tv_locked);
        lv_locked = (ListView) findViewById(R.id.lv_locked);
        ll_unlocked = (LinearLayout) findViewById(R.id.ll_unlocked);
        tv_unlocked = (TextView) findViewById(R.id.tv_unlocked);
        lv_unlocked = (ListView) findViewById(R.id.lv_unlocked);
        //获取到所有已安装软件
        mMyAppInfos = AppInfoProvide.getMyAppInfo(getApplicationContext());
        mLockedList = new ArrayList<>();
        mUnlockedList = new ArrayList<>();
        mWhetherInLockPage = false;
        //给两个textView设置监听,进行listView的跳转
        tv_appLock_left.setOnClickListener(new TextClickListener());
        tv_appLock_right.setOnClickListener(new TextClickListener());
    }

    //获取未加锁软件集合,与加锁软件集合
    public void getData() {
        //查询操作放到子线程中
        new Thread() {
            @Override
            public void run() {

                //需要创建一个数据库,用来保存已经加锁的软件,在此可以将总集合分开
                mDao = new QueryAppLockInfoDao(getApplicationContext());
                for (AppInfo myAppInfo : mMyAppInfos) {
                    if (mDao.queryWhetherLocked(myAppInfo.getApppackage())) {
                        //说明该软件已经加锁,应该加入到lockList中
                        mLockedList.add(myAppInfo);
                    } else {
                        mUnlockedList.add(myAppInfo);
                    }
                    //查询工作完成后需要将页面显示出来,传给UI线程

                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    //自定义一个内部类,继承Adapter
    class MyAdapter extends BaseAdapter {
        private boolean whetherLockList;

        public MyAdapter(boolean flag) {
            this.whetherLockList = flag;
        }

        @Override
        public int getCount() {
            int count;
            if (whetherLockList) {
                count = mLockedList.size();
                tv_locked.setText("已加锁的软件数目为" + count);
            } else {
                count = mUnlockedList.size();
                tv_unlocked.setText("尚未加锁的软件数目为" + count);

            }
            return count;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            ViewHolder holder;
            final AppInfo appInfo;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_lock_unlock_app, null);
                holder = new ViewHolder();
                holder.tv_appName = (TextView) view.findViewById(R.id.tv_lockApp_appName);
                holder.iv_whetherLock = (ImageView) view.findViewById(R.id.iv_lockUnlock_app);
                holder.iv_appIcon = (ImageView) view.findViewById(R.id.iv_lockApp);
                view.setTag(holder);
            }
            //判断是哪个list显示
            if (whetherLockList) {
                appInfo = mLockedList.get(position);
                holder.iv_whetherLock.setImageResource(R.drawable.lock);


            } else {
                appInfo = mUnlockedList.get(position);
                holder.iv_whetherLock.setImageResource(R.drawable.unlock);
            }
            holder.tv_appName.setText(appInfo.getAppname());
            holder.iv_appIcon.setImageDrawable(appInfo.getIcon());
            //给item里的unlockView设置监听,
            // 然后刷新各个adapter
            holder.iv_whetherLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这是已上锁的list,点击后将当前position的appInfo从lockList移除,并添加到unLockList中
                    if(whetherLockList){
                        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                                0, Animation.RELATIVE_TO_SELF, 1.0f,
                                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        ta.setDuration(2000l);
                        view.startAnimation(ta);
                        ta.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mDao.add(appInfo.getApppackage());
                                mUnlockedList.remove(position);
                                mLockedList.add(appInfo);
                                mLockedAdapter.notifyDataSetChanged();
                                mUnlockedAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }else { TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                            0, Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        ta.setDuration(2000l);
                        view.startAnimation(ta);
                        ta.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mDao.delete(appInfo.getApppackage());
                                mLockedList.remove(position);
                                mUnlockedList.add(appInfo);
                                mLockedAdapter.notifyDataSetChanged();
                                mUnlockedAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                    }
                }
            });
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }

    //Adapter缓存内部类
    class ViewHolder {
        private TextView tv_appName;
        private ImageView iv_appIcon;
        private ImageView iv_whetherLock;
    }

    //textView的clickListener
    class TextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_appLock_left:
                    tv_appLock_left.setBackgroundResource(R.drawable.tab_left_pressed);
                    tv_appLock_right.setBackgroundResource(R.drawable.tab_right_default);
                    ll_locked.setVisibility(View.GONE);
                    ll_unlocked.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_appLock_right:
                    tv_appLock_left.setBackgroundResource(R.drawable.tab_left_default);
                    tv_appLock_right.setBackgroundResource(R.drawable.tab_right_pressed);
                    ll_locked.setVisibility(View.VISIBLE);
                    ll_unlocked.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
