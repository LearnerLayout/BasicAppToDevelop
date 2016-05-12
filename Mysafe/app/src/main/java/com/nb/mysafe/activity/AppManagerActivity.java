package com.nb.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.bean.AppInfo;
import com.nb.mysafe.engine.AppInfoProvide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 侯紫睿
 * @time 2016/5/8 0008  下午 7:19
 * @desc ${TODD}
 */
public class AppManagerActivity extends Activity {
    private ListView lv_appManager;
    private TextView tv_appManager_memory;
    private TextView tv_appManager_sdCard;
    private List<AppInfo> mMyAppInfos;
    private ArrayList<AppInfo> mUserAppInfos;
    private ArrayList<AppInfo> mSystemAppInfos;
    private LinearLayout ll_appManager;
    private TextView tv_appManager_onScroll;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ll_appManager.setVisibility(View.GONE);
            lv_appManager.setAdapter(new MyAdapter());
          //  lv_appManager.setOnScrollListener(){new AbsListView.OnScrollListener()};
        }
    };

    //1,获取到后台数据,将所有App信息获取到后,封装到一个集合里
    //2,将这个集合与ListView关联起来
    //3,给每一个ListView设置监听,点击后弹出PopUpWindow
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        //初始化各个控件
        initiateView();
        //获取后台数据封装到集合内
        getData();

       lv_appManager.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {

           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               if(mUserAppInfos!=null&&mSystemAppInfos!=null){

                   if(firstVisibleItem>=mUserAppInfos.size()+1){
                       tv_appManager_onScroll.setText("系统软件");
                   }else {
                       tv_appManager_onScroll.setText("用户软件");
                   }
               }
           }
       });
    }

    //初始化各个控件
    private void initiateView() {
        lv_appManager = (ListView) findViewById(R.id.lv_appManager);
        tv_appManager_memory = (TextView) findViewById(R.id.tv_appManager_memory);
        tv_appManager_sdCard = (TextView) findViewById(R.id.tv_appManager_sdCard);
        ll_appManager = (LinearLayout) findViewById(R.id.ll_appManager);
        tv_appManager_onScroll= (TextView)findViewById(R.id.tv_appManager_onScroll);
    }


    //查询信息操作放到子线程进行,
    //相当于模板,在查询时,要显示出加载页面,查询完毕后,去掉加载页面
    //涉及到正在加载页面的出现,就要用到FrameLayout
    public void getData() {
        //显示sd卡和内存剩余空间
        showStorage();
        ll_appManager.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取到所有app信息
                mMyAppInfos = AppInfoProvide.getMyAppInfo(AppManagerActivity.this);
                //将app信息进行分类,分成用户app与系统app
                mUserAppInfos = new ArrayList<>();
                mSystemAppInfos = new ArrayList<>();
                for (AppInfo appInfo : mMyAppInfos) {
                    if (appInfo.isUser()) {
                        mUserAppInfos.add(appInfo);
                    } else {
                        mSystemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    //显示sd卡和内存剩余空间
    private void showStorage() {
        File dataDirectory = Environment.getDataDirectory();
        long dataSpace = dataDirectory.getFreeSpace();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        long sdSpace = externalStorageDirectory.getFreeSpace();
        tv_appManager_memory.setText("内存剩余空间为:" + Formatter.formatFileSize(this, dataSpace));
        tv_appManager_sdCard.setText("SD卡剩余空间为:" + Formatter.formatFileSize(this, sdSpace));
    }

    //创建一个adapter内部类,将appInfo与listView相关联
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMyAppInfos.size()+2;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           //根据position,判断应该向当前position设置什么样的view
            AppInfo appInfo;
            if(position == 0){
             TextView textView= new TextView(getApplicationContext());
                textView.setText("用户软件:"+mUserAppInfos.size()+"个");
                return  textView;
            }else if(position == mUserAppInfos.size()+1){
                TextView textView= new TextView(getApplicationContext());
                textView.setText("系统软件:"+mUserAppInfos.size()+"个");
                return  textView;
            }else if(position<= mUserAppInfos.size()){
                appInfo = mUserAppInfos.get(position-1);
            }else{
                appInfo = mSystemAppInfos.get(position-2-mUserAppInfos.size());
            }
            View view;
            ViewHolder holder ;
            if(convertView !=null && convertView instanceof RelativeLayout){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                 view = View.inflate(getApplicationContext(), R.layout.item_appmanager, null);
                 holder = new ViewHolder();
                 holder.iv_itemManager =(ImageView) view.findViewById(R.id.iv_itemManager);
                 holder.tv_itemManager_appName =(TextView) view.findViewById(R.id.tv_itemManager_appName);
                 holder.tv_itemManager_appSize =(TextView) view.findViewById(R.id.tv_itemManager_appSize);
                 holder.tv_itemManager_appStorage =(TextView) view.findViewById(R.id.tv_itemManager_appStorage);
                 view.setTag(holder);
            }
            holder.iv_itemManager.setImageDrawable(appInfo.getIcon());
            holder.tv_itemManager_appName.setText(appInfo.getAppname());
            holder.tv_itemManager_appSize.setText(String.valueOf(Formatter.formatFileSize(getApplicationContext(),appInfo.getAppsize())));
            if(appInfo.isRom()){
                holder.tv_itemManager_appStorage.setText("手机内存");
            }else{
                holder.tv_itemManager_appStorage.setText("SD卡中");
            }
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
    //为了优化ListView,创建一个ViewHolder
    class ViewHolder {
       private ImageView iv_itemManager;
        private TextView tv_itemManager_appStorage;
        private TextView tv_itemManager_appSize;
        private TextView tv_itemManager_appName;

    }
}
