package com.nb.mysafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nb.mysafe.R;
import com.nb.mysafe.service.QueryPhoneAddressService;
import com.nb.mysafe.utils.IntentUtil;

/**
 * @author 侯紫睿
 * @time 2016/5/6 0006  上午 9:30
 * @desc ${显示高级查询,里面有查询号码归属地button,点击后跳到查询界面}
 */
public class AToolActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_atool);
    }
    public void select(View view){
        IntentUtil.startIntent(this,SelectAddresActivity_ATool.class);
    }
    public void serviceStart(View view){
        Intent intent = new Intent(this, QueryPhoneAddressService.class);
        startService(intent);
    }

}
