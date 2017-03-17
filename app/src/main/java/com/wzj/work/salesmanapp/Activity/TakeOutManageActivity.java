package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wzj.work.salesmanapp.Adapter.RecycleBuyAdapter;
import com.wzj.work.salesmanapp.Adapter.RecycleTakeOutAdapter;
import com.wzj.work.salesmanapp.Adapter.RecyclerDishesAdapter;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.OtherClass.StoreBuy;
import com.wzj.work.salesmanapp.OtherClass.TakeOut;
import com.wzj.work.salesmanapp.OtherClass.TakeOutList;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class TakeOutManageActivity extends AutoLayoutActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView recyclerView;
    private RecycleTakeOutAdapter recyclerTakeOutAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    private ArrayList<TakeOutList> TakeWayList;
    MyShopList store;
    Bundle bundle;
    String result = null;
    TakeOut takeOut;
    int count = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                if (funNet.isOk(result,TakeOutManageActivity.this)) {
                    Gson g = new Gson();
                    try{
                        takeOut = g.fromJson(result, TakeOut.class);
//                    Log.e("handleMessage: ", takeOut.data.TakeWayList.size() + "");
                        TakeWayList=takeOut.data.TakeWayList;
                        recyclerView.setLayoutManager(mLayoutMannager);
                        recyclerView.setHasFixedSize(true);
                        recyclerTakeOutAdapter = new RecycleTakeOutAdapter(TakeOutManageActivity.this, TakeWayList);
                        recyclerView.setAdapter(recyclerTakeOutAdapter);
                    }catch (Exception e){

                    }
                } else {

                }
            } else if (msg.what == 2) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                takeOut = g.fromJson(result, TakeOut.class);
                if (takeOut.data.TakeWayList.size() > 0) {
                    TakeWayList.addAll(takeOut.data.TakeWayList);
                    recyclerTakeOutAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TakeOutManageActivity.this, R.string.no_more, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out_manage);
        bundle = getIntent().getExtras();
        store = (MyShopList) bundle.getSerializable("store");
        initView();
        doSomething();
    }

    public void initView() {
        TextView tv_title_R = (TextView) findViewById(R.id.right_tv2);
        ImageView iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(R.string.take_out_manager);
        tv_title_R.setText(R.string.choose);
        iv_title_L.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.prv_takeout);
        findViewById(R.id.tv_addType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TakeOutManageActivity.this,AddTakeoutTypeActivity.class);
                intent.putExtra("shopId",store.Id);
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
        params.addQueryStringParameter("TypeId", "3");
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
        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        count++;
        getData(2);
    }
}
