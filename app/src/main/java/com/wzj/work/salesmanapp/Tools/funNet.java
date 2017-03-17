package com.wzj.work.salesmanapp.Tools;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.IsOk;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 44967 on 2017/3/1.
 */
public class funNet {
    public static boolean isOk(String s, Context context){
        IsOk isOk=new Gson().fromJson(s,IsOk.class);
        if(isOk.statusCode.equals("1")){
            if(!isOk.statusMessage.equals("")){
                Toast.makeText(context,isOk.statusMessage,Toast.LENGTH_SHORT).show();
            }
            return true;
        }else {
            if(!isOk.statusMessage.equals("")){
                Toast.makeText(context,isOk.statusMessage,Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
    public static void toolNet(RequestParams params, final Handler handler, final int t) {

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                message.obj = result;
                message.what = t;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(100);
                Log.e("zh", "onError: ");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void toolNetGet(RequestParams params, final Handler handler, final int t) {

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                message.obj = result;
                message.what = t;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(100);
                Log.e("zh", "onError: ");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public static void uploadfile(String url, ArrayList<String> fileNames, final Handler handler, final int t) {
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        for (int i = 0; i < fileNames.size(); i++) {
            params.addBodyParameter("picture" + i, new File(fileNames.get(i)));
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                message.obj = result;
                message.what = t;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(101);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
