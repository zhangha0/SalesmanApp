package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.R;
import com.zhy.autolayout.AutoLayoutActivity;

public class StoreActivity extends AutoLayoutActivity {
    MyShopList store;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        bundle = getIntent().getExtras();
        store = (MyShopList) bundle.getSerializable("store");
        initView();
    }

    public void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.my_title);
        tvTitle.setText(store.ShopName_CN);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back_title);
        ivBack.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ll_store_dishes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, DishesManageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_store_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreActivity.this,BuyManageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_store_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreActivity.this,TakeOutManageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_store_cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreActivity.this,CashManageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
