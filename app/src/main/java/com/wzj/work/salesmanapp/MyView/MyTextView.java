package com.wzj.work.salesmanapp.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 44967 on 2017/3/8.
 */
public class MyTextView extends TextView{
    public String tvName;
    public String tvId;
    public boolean isOk=false;
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
