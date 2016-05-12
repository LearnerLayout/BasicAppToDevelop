package com.nb.mysafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.bean.BlackNumberInfo;
import com.nb.mysafe.db.dao.BlackNumberDao;
import com.nb.mysafe.service.BlackNumService;
import com.nb.mysafe.utils.ToastUtil;

import java.util.List;

public class CallNumInfoActivity extends AppCompatActivity {

    private ListView mLvcallInfo;
    private LinearLayout ll_num_load;
    private List<BlackNumberInfo> mList;
    private BlackNumberDao mDao;
    private CallAdapter mAdapter;
    private  int index = 0;
    private  int maxCount = 20;
    //刷新主线程UI界面放到handler处理,也就是刷新adapter放到这里
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
           // Log.v("nb","主线程回调了,隐藏界面");
            ll_num_load.setVisibility(View.INVISIBLE);
            if (mAdapter==null){
                mAdapter = new CallAdapter();
                mLvcallInfo.setAdapter(mAdapter);
            }else {
                mAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_num_info);
        mLvcallInfo = (ListView) findViewById(R.id.lv_callInfo);
        ll_num_load= (LinearLayout)findViewById(R.id.ll_num_load);
        //Log.v("nb","加载了listview");
        //引入dao的findAll将获取到的list给adapter
        mDao = new BlackNumberDao(getApplicationContext());
        //这条查询数据库的语句需要放到子线程运行
        // mList = mDao.findAll();.
        //Log.v("nb","开始调用fillData");
        fillData();
        Intent intent = new Intent(this, BlackNumService.class);
        startService(intent);


        //给listView设置一个滑动监听,当滑到底是加载新的数据
        mLvcallInfo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case  SCROLL_STATE_IDLE :
                        //静止时
                        int size = mList.size();
                        //listView最后可见条目位置
                        int lastVisiblePosition = mLvcallInfo.getLastVisiblePosition();
                        if(lastVisiblePosition==(size-1)){
                               //说明该加载了
                            fillData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    public void fillData(){
        //开启子线程时应该在页面显示出进度条表示正在加载数据
       // Log.v("nb","进入fillData");
     ll_num_load.setVisibility(View.VISIBLE);
       // Log.v("nb","将进度条显示出来");
        //开启子线程查询数据库
        new Thread(){
            @Override
            public void run() {
                if(mList==null){//说明是第一次查询
                   mList= mDao.findPart(index,maxCount);

                }else {//说明不是第一次查询
                    index += maxCount;
                    mList.addAll(mDao.findPart(index,maxCount));
                    //告诉主线程加载完毕
                }
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
                //Log.v("nb","穿个主线程调用");

            }
        }.start();
    }

    //添加黑名单的按钮的实现方法
    public void addBlackNum(View view) {
        //按下按钮后弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialogAddBlackNum = builder.create();
        View dialogAddView = View.inflate(getApplicationContext(), R.layout.activity_alertdialog_add_blacknum, null);
        alertDialogAddBlackNum.setView(dialogAddView);
        //将每个alertDialog中组件根据Id找出来

        Button btnOk = (Button) dialogAddView.findViewById(R.id.btn_alertDialog_add_ok);
        Button btnNoOk = (Button) dialogAddView.findViewById(R.id.btn_alertDialog_add_nook);
        final EditText et_addBlackNum = (EditText) dialogAddView.findViewById(R.id.et_addBlackNum);
        final RadioGroup rg_alertDialog_add = (RadioGroup) dialogAddView.findViewById(R.id.rg_alertDialog_add);
        alertDialogAddBlackNum.show();
        //给两个按钮设置监听
        btnNoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAddBlackNum.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_addBlackNum.getText().toString().trim();
                String mode = "3";
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show(CallNumInfoActivity.this, "不能为空");
                    return;
                } else {
                    int checkedRadioButtonId = rg_alertDialog_add.getCheckedRadioButtonId();
                    switch (checkedRadioButtonId) {
                        case R.id.rb_phone:
                            mode = "1";
                            break;
                        case R.id.rb_msg:
                            mode = "2";
                            break;
                        case R.id.rb_all:
                            mode = "3";
                            break;
                    }
                    boolean result = mDao.add(phone, mode);
                    if (result) {
                        BlackNumberInfo info = new BlackNumberInfo();
                        info.setPhone(phone);
                        info.setMode(mode);
                        mList.add(0, info);
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.show(CallNumInfoActivity.this, "添加成功");
                        alertDialogAddBlackNum.dismiss();
                    } else {
                        ToastUtil.show(CallNumInfoActivity.this, "添加失败");
                        return;
                    }
                }

            }
        });


    }




    //内部类;用来显示黑名单List的adapter
    class CallAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_phone, null);
                holder = new ViewHolder();
                holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
                holder.iv_deleter = (ImageView) view.findViewById(R.id.iv_deleter);
                view.setTag(holder);
            }
            final BlackNumberInfo info = mList.get(position);
            holder.tv_phone.setText(info.getPhone());
            if (info.getMode().equals("1")) {
                holder.tv_phone.setTextColor(Color.rgb(7, 18, 234));
                holder.tv_mode.setText("电话拦截");
            }
            if (info.getMode().equals("2")) {
                holder.tv_phone.setTextColor(Color.rgb(20, 193,11));
                holder.tv_mode.setText("短信拦截");
            }
            if (info.getMode().equals("3")) {
                holder.tv_phone.setTextColor(Color.rgb(255, 00, 00));
                holder.tv_mode.setText("全部拦截");
            }
            //holder.iv_deleter.setImageResource(R.drawable.ic_menu_delete);
            holder.iv_deleter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击后弹窗,点击确认后,根据号码删除
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CallNumInfoActivity.this);
                    final AlertDialog dialogDelete = builder.create();
                    builder.setTitle("是否真要删除");
                   // dialogDelete.show();

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // dialogDelete.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认删除后,根据当前条目的phone,
                            // 1,调用dao删除数据库中数据
                            boolean result = mDao.delete(info.getPhone());
                            // 2,调用list删除集合中数据
                            if(result){
                                boolean remove = mList.remove(info);
                                if(remove){
                                    mAdapter.notifyDataSetChanged();
                                    ToastUtil.show(CallNumInfoActivity.this,"删除成功");
                                    //dialogDelete.dismiss();

                                }
                            }else{
                                ToastUtil.show(CallNumInfoActivity.this,"删除失败");

                            }
                        }
                    });
                    //dialogDelete.show();
                    builder.show();
                }
            });
            return view;

        }
    }

    class ViewHolder {
        TextView tv_phone;
        TextView tv_mode;
        ImageView iv_deleter;

    }
}
