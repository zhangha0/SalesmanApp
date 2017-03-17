package com.wzj.work.salesmanapp.Tools;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.x;

/**
 * Created by 44967 on 2017/3/1.
 */
public class Until3 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        Fresco.initialize(getApplicationContext());
    }
}