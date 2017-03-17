package com.wzj.work.salesmanapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wzj.work.salesmanapp.Adapter.RecycleViewHome;
import com.wzj.work.salesmanapp.Adapter.RecyclerDishesAdapter;
import com.wzj.work.salesmanapp.OtherClass.Food;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.OtherClass.Store;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class DishesManageActivity extends AutoLayoutActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView recyclerView;
    private RecyclerDishesAdapter recyclerDishesAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    MyShopList store;
    Bundle bundle;
    String result = null;
    int count = 1;
    ImageView tv_title_R;
    ImageView iv_title_L;
    Store myStore;
    ArrayList<Food> foodList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                myStore = g.fromJson(result, Store.class);
                foodList = myStore.data.FoodList;
                recyclerView.setLayoutManager(mLayoutMannager);
                recyclerView.setHasFixedSize(true);
                recyclerDishesAdapter = new RecyclerDishesAdapter(DishesManageActivity.this, foodList);
                recyclerView.setAdapter(recyclerDishesAdapter);
            } else if (msg.what == 2) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                myStore = g.fromJson(result, Store.class);
                if(myStore.data.FoodList.size()>0){
                    foodList.addAll(myStore.data.FoodList);
                    recyclerDishesAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(DishesManageActivity.this,R.string.no_more,Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_manage);
        bundle = getIntent().getExtras();
        store = (MyShopList) bundle.getSerializable("store");
        initView();
        doSomething();
    }

    public void initView() {
        tv_title_R = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(R.string.dishes_manager);
        tv_title_R.setBackgroundResource(R.drawable.add);
        iv_title_L.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.prv_dishesManager);

        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DishesManageActivity.this,AddDishesActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("state","0");
                startActivity(intent);
            }
        });
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

    private void getData(int t) {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Food/FoodManagement");
        params.addQueryStringParameter("ShopId", store.Id);
        params.addQueryStringParameter("TypeId", "1");
        params.addQueryStringParameter("PageIndex", "" + count);
        params.addQueryStringParameter("PageSize", "10");
        funNet.toolNet(params, handler, t);
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
