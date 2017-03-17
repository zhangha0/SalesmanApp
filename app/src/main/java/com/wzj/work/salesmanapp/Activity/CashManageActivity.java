package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wzj.work.salesmanapp.Adapter.RecycleCashAdapter;
import com.wzj.work.salesmanapp.OtherClass.CashCoupon;
import com.wzj.work.salesmanapp.OtherClass.CashManage;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class CashManageActivity extends AutoLayoutActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView recyclerView;
    private RecycleCashAdapter recyclerCashAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    private ArrayList<CashCoupon> cashList = new ArrayList<>();
    Bundle bundle;
    String result = null;
    int count = 1;
    MyShopList store;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                if (funNet.isOk(result, CashManageActivity.this)) {
                    try {
                        Gson g=new Gson();
                        CashManage cashManage=g.fromJson(result,CashManage.class);
                        cashList=cashManage.data.CouponList;
                        recyclerView.setLayoutManager(mLayoutMannager);
                        recyclerView.setHasFixedSize(true);
                        recyclerCashAdapter = new RecycleCashAdapter(CashManageActivity.this, cashList, store.Id);
                        recyclerView.setAdapter(recyclerCashAdapter);
                    } catch (Exception e) {

                    }
                }

            } else if (msg.what == 2) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                try {
                    Gson g = new Gson();
                    CashManage cashManage=g.fromJson(result,CashManage.class);
                    if(cashManage.data.CouponList.size()>0){
                        cashList.addAll(cashManage.data.CouponList);
                        recyclerCashAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(CashManageActivity.this,R.string.no_more,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_manage);
        bundle = getIntent().getExtras();
        store = (MyShopList) bundle.getSerializable("store");
        initView();
        doSomething();
    }

    public void initView() {
        ImageView tv_title_R = (ImageView) findViewById(R.id.iv_title_right);
        ImageView iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(R.string.cash_manager);
        tv_title_R.setBackgroundResource(R.drawable.add);
        iv_title_L.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CashManageActivity.this, AddCashActivity.class);
                intent.putExtra("shopId", store.Id);
                startActivity(intent);
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.prv_cashManage);
    }

    private void getData(int t) {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Food/FoodManagement");
        params.addQueryStringParameter("ShopId", store.Id);
        params.addQueryStringParameter("TypeId", "4");
        params.addQueryStringParameter("PageIndex", "" + count);
        params.addQueryStringParameter("PageSize", "10");
        funNet.toolNet(params, handler, t);
    }

    public void doSomething() {
        mLayoutMannager = new LinearLayoutManager(this);
        recyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        recyclerView.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setFooterViewText("loading");
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        getData(1);
    }

    @Override
    public void onRefresh() {
        count = 1;
        getData(1);
    }

    @Override
    public void onLoadMore() {
        count++;
        getData(2);
    }
}
