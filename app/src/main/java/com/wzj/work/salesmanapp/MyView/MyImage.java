package com.wzj.work.salesmanapp.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 44967 on 2017/3/13.
 */
public class MyImage extends ImageView{
    public String id;
    public int index;
    public boolean isChoose=false;
    public MyImage(Context context) {
        super(context);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
