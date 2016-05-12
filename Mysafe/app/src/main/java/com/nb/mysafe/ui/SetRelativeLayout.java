package com.nb.mysafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.nb.mysafe.R;

/**
 * @author 侯紫睿
 * @time 2016/5/5 0005  下午 11:43
 * @desc ${创建一个自定义控件}
 */
public class SetRelativeLayout extends RelativeLayout {
    public SetRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = View.inflate(context, R.layout.tvcb, null);
    }
}
