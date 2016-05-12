package com.nb.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.nb.mysafe.R;
import com.nb.mysafe.utils.IntentUtil;

/**
 * @author 侯紫睿
 * @time 2016/5/10 0010  下午 9:39
 * @desc ${TODD}
 */
public class EnCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
    }
    //跳转到选择程序加密页面
    public void setAppEncode(View view ){
        IntentUtil.startIntent(this,LockAppActivity.class);
    }
}
