package com.wzj.work.salesmanapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

public class AddTakeoutTypeActivity extends AutoLayoutActivity {
    private String shopId;
    private EditText etTypeName;
    private ProgressDialog proDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String result = msg.obj.toString();
                Log.e("handleMessage: ", result + "");
                if (funNet.isOk(result, AddTakeoutTypeActivity.this)) {
                    finish();
                    Toast.makeText(AddTakeoutTypeActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                proDialog.dismiss();
            } else if (msg.what == 100) {
                proDialog.dismiss();
                Toast.makeText(AddTakeoutTypeActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_takeout_type);
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        initView();
    }

    public void initView() {
        TextView tv_title_R = (TextView) findViewById(R.id.right_tv2);
        ImageView iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(R.string.take_out_manager);
        tv_title_R.setText(R.string.sure);
        iv_title_L.setBackgroundResource(R.drawable.back);
        etTypeName = (EditText) findViewById(R.id.et_typeName);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTypeName.getText().toString().equals("")) {
                    progressDialog();
                    RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/AddTakeWayTitle");
                    params.addQueryStringParameter("ShopId", shopId);
                    params.addQueryStringParameter("Title", etTypeName.getText().toString());
                    funNet.toolNet(params, handler, 1);
                } else {
                    Toast.makeText(AddTakeoutTypeActivity.this, "请填名称", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void progressDialog() {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage("正在添加分类");
        proDialog.show();
    }
}
