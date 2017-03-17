package com.wzj.work.salesmanapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wzj.work.salesmanapp.Activity.Main2Activity;
import com.wzj.work.salesmanapp.Activity.StoreActivity;
import com.wzj.work.salesmanapp.Adapter.RecycleViewHome;
import com.wzj.work.salesmanapp.OtherClass.ManData;
import com.wzj.work.salesmanapp.OtherClass.MerchantList;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.MD5;
import com.wzj.work.salesmanapp.Tools.funNet;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 44967 on 2017/2/28.
 */
public class Fragment_home extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private Context context;
    private RecyclerView recyclerView;
    private RecycleViewHome recycleViewHome;
    private RecyclerView.LayoutManager mLayoutMannager;
    String result = null;
    int count = 1;
    MerchantList merList;
    ArrayList<MyShopList> shopLists = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                merList = g.fromJson(result, MerchantList.class);
                shopLists = merList.data.ShopList;
                recyclerView.setLayoutManager(mLayoutMannager);
                recyclerView.setHasFixedSize(true);
                recycleViewHome = new RecycleViewHome(context, shopLists);
                recyclerView.setAdapter(recycleViewHome);
                recycleViewHome.setOnItemClickListener(new RecycleViewHome.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("store", merList.data.ShopList.get(position));
                        Intent intent = new Intent(context, StoreActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } else if (msg.what == 2) {
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                result = msg.obj.toString();
                Gson g = new Gson();
                MerchantList merList = g.fromJson(result, MerchantList.class);
                if (merList.data.ShopList.size() == 0) {
                    Toast.makeText(context, R.string.no_more, Toast.LENGTH_SHORT).show();
                } else {
                    shopLists.addAll(merList.data.ShopList);
                    recycleViewHome.notifyDataSetChanged();
                }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);
        TextView tv = (TextView) view.findViewById(R.id.my_title);
        tv.setText(R.string.home_title);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.prv_home);
        mLayoutMannager = new LinearLayoutManager(context);
        recyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        recyclerView.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setFooterViewText("loading");
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        getData(1);
        return view;
    }

    private void getData(int t) {
        RequestParams params = new RequestParams("http://37.59.66.172:86/Shop/GetShopList");
        params.addQueryStringParameter("EmployeeId", Main2Activity.manData.Id);
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

