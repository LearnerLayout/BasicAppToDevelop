package com.nb.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.db.dao.QueryNumAddressDao;

/**
 * @author 侯紫睿
 * @time 2016/5/6 0006  上午 9:37
 * @desc ${TODD}
 */
public class SelectAddresActivity_ATool extends Activity{
    private EditText et_phoneAddress;
    private TextView tv_phoneAddress;
    private QueryNumAddressDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectadd_atool);
        //初始化各个需要的组件
        et_phoneAddress= (EditText)findViewById(R.id.et_phoneAddress);
        tv_phoneAddress= (TextView)findViewById(R.id.tv_phoneAddress);
        mDao = new QueryNumAddressDao();
    }
    public void queryPhoneAddress(View view){
        //点击查询按钮后,获取到et里输入的号码.去查询数据库,将查到的结果返回给tv显示出来
        //需要将assets里的数据库在splash界面加载时拷贝到自己包名下的files文件夹中
        String phone = et_phoneAddress.getText().toString();
        String address = mDao.queryNumAddress(getApplicationContext(), phone);
        tv_phoneAddress.setText(address);
    }
}
