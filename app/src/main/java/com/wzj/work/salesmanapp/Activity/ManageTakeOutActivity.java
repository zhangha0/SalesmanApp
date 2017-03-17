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

import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wzj.work.salesmanapp.Adapter.RecycleMTOAdapter;
import com.wzj.work.salesmanapp.Adapter.RecycleTakeOutAdapter;
import com.wzj.work.salesmanapp.OtherClass.MtoFood;
import com.wzj.work.salesmanapp.OtherClass.MtoFoods;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class ManageTakeOutActivity extends AutoLayoutActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView recyclerView;
    private RecycleMTOAdapter RecycleAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    private String type;
    private String shopId;
    private String takeWayId;
    private String result;
    private MtoFoods mtoFoods;
    private ArrayList<MtoFood> foodList=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result=msg.obj.toString();
                if(funNet.isOk(result,ManageTakeOutActivity.this)){
                    try{
                        Gson g=new Gson();
                        mtoFoods=g.fromJson(result,MtoFoods.class);
                        foodList=mtoFoods.data;
                        recyclerView.setLayoutManager(mLayoutMannager);
                        recyclerView.setHasFixedSize(true);
                        RecycleAdapter = new RecycleMTOAdapter(ManageTakeOutActivity.this, foodList,shopId);
                        recyclerView.setAdapter(RecycleAdapter);
                    }catch (Exception e){

                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_take_out);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        shopId = intent.getStringExtra("shopId");
        takeWayId = intent.getStringExtra("takeWayId");
        initView();
        getData(1);
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
    public void initView() {
        ImageView tv_title_R = (ImageView) findViewById(R.id.iv_title_right);
        ImageView iv_title_L = (ImageView) findViewById(R.id.iv_back_title);
        ((TextView) findViewById(R.id.my_title)).setText(type);
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
                ArrayList<String> foods=new ArrayList<String>();
                for (int i = 0; i < foodList.size(); i++) {
                    foods.add(foodList.get(i).Id);
                }
                Bundle bundle=new Bundle();
                bundle.putSerializable("foodList",foods);
                Intent intent=new Intent(ManageTakeOutActivity.this,AddMtoFoodActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("shopId",shopId);
                intent.putExtra("takeWayId",takeWayId);
                startActivity(intent);
            }
        });
        mPullLoadMoreRecyclerView= (PullLoadMoreRecyclerView) findViewById(R.id.prv_mTakeOut);
        doSomething();
    }
    private void getData(int t) {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Food/GetTakeWayDetails");
        params.addQueryStringParameter("ShopId", shopId);
        params.addQueryStringParameter("takeWayId", takeWayId);
        funNet.toolNet(params, handler, t);
    }

    @Override
    public void onRefresh() {
        getData(1);
    }

    @Override
    public void onLoadMore() {
        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
    }
}
