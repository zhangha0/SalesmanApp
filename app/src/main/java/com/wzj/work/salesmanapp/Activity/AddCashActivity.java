package com.wzj.work.salesmanapp.Activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

public class AddCashActivity extends AutoLayoutActivity {
    private EditText et_id;
    private EditText et_price;
    private EditText et_discountPrice;
    private EditText et_titleES;
    private EditText et_titleEN;
    private EditText et_titleCN;
    private EditText et_indroES;
    private EditText et_indroEN;
    private EditText et_indroCN;
    private String shopId;
    private ProgressDialog proDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String result = msg.obj.toString();
                if(funNet.isOk(result,AddCashActivity.this)){
                    finish();
                    Toast.makeText(AddCashActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                }else {
                    proDialog.dismiss();
                }
            }else if(msg.what==100){
                proDialog.dismiss();
                Toast.makeText(AddCashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash);
        shopId = getIntent().getStringExtra("shopId");
        initView();
    }

    public void initView() {
        et_id = (EditText) findViewById(R.id.et_cash_id);
        et_price = (EditText) findViewById(R.id.et_cash_Price);
        et_discountPrice = (EditText) findViewById(R.id.et_cash_Discountprice);
        et_titleES = (EditText) findViewById(R.id.et_cash_Title_ES);
        et_titleEN = (EditText) findViewById(R.id.et_cash_Title_EN);
        et_titleCN = (EditText) findViewById(R.id.et_cash_Title_CN);
        et_indroES = (EditText) findViewById(R.id.et_cash_Introduce_ES);
        et_indroEN = (EditText) findViewById(R.id.et_cash_Introduce_EN);
        et_indroCN = (EditText) findViewById(R.id.et_cash_Introduce_CN);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_title);
        iv_back.setBackgroundResource(R.drawable.back);

        TextView tv_title = (TextView) findViewById(R.id.my_title);
        tv_title.setText(R.string.add_cash);
        TextView tv_title_add = (TextView) findViewById(R.id.tv_title_add);
        tv_title_add.setText(R.string.add);
        tv_title_add.setBackgroundResource(R.drawable.shape_kuang);
        tv_title_add.setTextColor(0xffd9d7d7);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReady();
            }
        });
    }

    public void isReady() {
        if (!et_id.getText().toString().equals("")) {
            if (!et_price.getText().toString().equals("")) {
                if (!et_discountPrice.getText().toString().equals("")) {
                    if (!et_titleCN.getText().toString().equals("") && !et_titleEN.getText().toString().equals("") && !et_titleES.getText().toString().equals("")) {
                        if (!et_indroCN.getText().toString().equals("") && !et_indroES.getText().toString().equals("") && !et_indroEN.getText().toString().equals("")) {
                            progressDialog();
                            addCashPost();
                        } else {
                            Toast.makeText(AddCashActivity.this, "请填写代金券介绍", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddCashActivity.this, "请填写代金券名称", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddCashActivity.this, "请填写代金券价格", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddCashActivity.this, "请填写代金券面值", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddCashActivity.this, "请填写代金券编码", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCashPost() {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Food/AddCoupon");
        params.addBodyParameter("ShopId", shopId + "");
        params.addBodyParameter("Id", et_id.getText().toString() + "");
        params.addBodyParameter("Title_CN", et_indroCN.getText().toString() + "");
        params.addBodyParameter("Title_EN", et_indroEN.getText().toString() + "");
        params.addBodyParameter("Title_ES", et_titleES.getText().toString() + "");
        params.addBodyParameter("Introduce_EN", et_indroEN.getText().toString() + "");
        params.addBodyParameter("Introduce_CN", et_indroCN.getText().toString() + "");
        params.addBodyParameter("Introduce_ES", et_titleES.getText().toString() + "");
        params.addBodyParameter("Price", et_price.getText().toString() + "");
        params.addBodyParameter("Discountprice", et_discountPrice.getText().toString() + "");
        funNet.toolNet(params, handler, 1);
    }
    public void progressDialog() {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage("正在添加代金券");
        proDialog.show();
    }
}
