package com.nb.mysafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.utils.IntentUtil;

/**
 * @author 侯紫睿
 * @time 2016/5/2 0002  上午 10:48
 * @desc ${TODD}
 */
public class HomeActivity extends Activity {
    private GridView gv_home;
    private SharedPreferences sp;
    private String[] names = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
            "手机杀毒", "缓存清理", "高级工具", "手机设置"};
    private int[] icons = {R.drawable.a, R.drawable.b, R.drawable.app, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机防盗
                        String password = sp.getString("password", null);
                        if (!TextUtils.isEmpty(password)) {
                            //弹出输入密码对话框
                            //showEnterPasswordDialog();

                        } else {
                            //第一次进去,弹出设置密码对话框
                            // showSetupPasswordDialog();
                        }

                        break;
                    case 1://通讯卫士
                        IntentUtil.startIntent(HomeActivity.this, CallNumInfoActivity.class);
                        break;
                    case 2://软件管理
                        IntentUtil.startIntent(HomeActivity.this, AppManagerActivity.class);
                        break;
                    case 4://流量统计(为程序加密)
                        IntentUtil.startIntent(HomeActivity.this, EnCodeActivity.class);
                        break;
                    case 7://高级查询
                        IntentUtil.startIntent(HomeActivity.this, AToolActivity.class);
                        break;
                    case 8://手机设置
                        // IntentUtil.startIntent(HomeActivity.this,SettingActivity.class);
                        break;
                }
            }
        });

    }


    class HomeAdapter extends BaseAdapter {
        //列表的显示个数
        @Override
        public int getCount() {
            return names.length;
        }
        //每一个item的布局

        /**
         * @param position    每个item的位置
         * @param convertView 缓存
         * @param parent
         * @return 布局转换成的view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_name);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_icon);
            tv_name.setText(names[position]);
            iv_icon.setImageResource(icons[position]);
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
}
