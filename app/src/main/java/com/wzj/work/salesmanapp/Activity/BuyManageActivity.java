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
import com.wzj.work.salesmanapp.Adapter.RecycleBuyAdapter;
import com.wzj.work.salesmanapp.OtherClass.GroupBuy;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.OtherClass.StoreBuy;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class BuyManageActivity extends AutoLayoutActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView recyclerView;
    private RecycleBuyAdapter recyclerBuyAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    ImageView tv_title_R;
    ImageView iv_title_L;
    Bundle bundle;
    String result = null;
    int count = 1;
    StoreBuy myStore;
    MyShopList store;
    ArrayList<GroupBuy> groupBuyList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                if (funNet.isOk(result, BuyManageActivity.this)) {
                    Gson g = new Gson();
                    try {
                        myStore = g.fromJson(result, StoreBuy.class);
                        groupBuyList = myStore.data.GroupList;
                        recyclerView.setLayoutManager(mLayoutMannager);
                        recyclerView.setHasFixedSize(true);
                        recyclerBuyAdapter = new RecycleBuyAdapter(BuyManageActivity.this, groupBuyList);
                        recyclerView.setAdapter(recyclerBuyAdapter);
                    } catch (Exception e) {

                    }
                }
            } else if (msg.what == 2) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                myStore = g.fromJson(result, StoreBuy.class);
                if (myStore.data.GroupList.size() > 0) {
                    groupBuyList.addAll(myStore.data.GroupList);
                    recyclerBuyAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BuyManageActivity.this, R.string.no_more, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_manage);
        bundle = getIntent().getExtras();
        store = (MyShopList) bundle.getSerializable("store");
        initView();
        doSomething();
    }

    public void initView() {
        tv_title_R = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(R.string.buy_manager);
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
                Intent intent=new Intent(BuyManageActivity.this,AddGroupBuyActivity.class);
                intent.putExtra("option","1");
                intent.putExtra("shopId",store.Id);
                startActivity(intent);
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.prv_buyManage);
    }

    private void getData(int t) {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/FoodManagement");
        params.addQueryStringParameter("ShopId", store.Id);
        params.addQueryStringParameter("TypeId", "2");
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
