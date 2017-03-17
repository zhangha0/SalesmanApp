package com.wzj.work.salesmanapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.Activity.AddStoreActivity;
import com.wzj.work.salesmanapp.MyView.MyTextView;
import com.wzj.work.salesmanapp.OtherClass.SellerData;
import com.wzj.work.salesmanapp.OtherClass.SellerTypes;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/2/28.
 */
public class Fragment_add extends Fragment implements View.OnClickListener{
    private String result = null;
    private SellerTypes types;
    private LinearLayout ll_add;
    private Context context;
    private TextView lastView;
    GradientDrawable gd_n;
    GradientDrawable gd_y;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                result = msg.obj.toString();
                Gson g = new Gson();
                types = g.fromJson(result, SellerTypes.class);
                addView(types.data);
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
        View view = inflater.inflate(R.layout.layout_fragment_add, container, false);
        TextView tv = (TextView) view.findViewById(R.id.my_title);
        tv.setText(R.string.add_title);
        ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
        doNet();
        seting();
        return view;
    }

    public void doNet() {
        RequestParams params = new RequestParams("http://37.59.66.172:86/Shop/GetShopType");
        funNet.toolNet(params, handler, 1);
    }

    public void addView(ArrayList<SellerData> data) {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -2);
        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(0, -2);
        llp1.setMargins(0, 30, 0, 30);
        llp1.weight = 1;
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(-2, -2);
        llp2.height = 110;
        llp2.width = 230;
        for (int i = 0; i < (data.size() / 3); i++) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(llp);
            LinearLayout ll1 = new LinearLayout(context);
            ll1.setLayoutParams(llp1);
            ll1.setGravity(Gravity.CENTER);
            MyTextView tv1 = new MyTextView(context);
            tv1.setText(data.get(i * 3 + 0).Title_CN);
            tv1.tvName=data.get(i * 3 + 0).Title_CN;
            tv1.tvId=data.get(i * 3 + 0).Id;
            setView(tv1);
            ll1.addView(tv1);

            LinearLayout ll2 = new LinearLayout(context);
            ll2.setLayoutParams(llp1);
            ll2.setGravity(Gravity.CENTER);
            MyTextView tv2 = new MyTextView(context);
            tv2.setText(data.get(i * 3 + 1).Title_CN);
            tv2.tvName=data.get(i * 3 + 1).Title_CN;
            tv2.tvId=data.get(i * 3 + 1).Id;
            setView(tv2);
            ll2.addView(tv2);

            LinearLayout ll3 = new LinearLayout(context);
            ll3.setLayoutParams(llp1);
            ll3.setGravity(Gravity.CENTER);
            MyTextView tv3 = new MyTextView(context);
            tv3.setText(data.get(i * 3 + 2).Title_CN);
            tv3.tvName=data.get(i * 3 + 2).Title_CN;
            tv3.tvId=data.get(i * 3 + 2).Id;
            setView(tv3);
            ll3.addView(tv3);

            ll.addView(ll1);
            ll.addView(ll2);
            ll.addView(ll3);
            ll_add.addView(ll);
        }
        if(data.size() % 3!=0){
            int count=data.size() % 3;
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(llp);
            LinearLayout ll1 = new LinearLayout(context);
            ll1.setLayoutParams(llp1);
            ll1.setGravity(Gravity.CENTER);
            LinearLayout ll2 = new LinearLayout(context);
            ll2.setLayoutParams(llp1);
            ll2.setGravity(Gravity.CENTER);
            LinearLayout ll3 = new LinearLayout(context);
            ll3.setLayoutParams(llp1);
            ll3.setGravity(Gravity.CENTER);
            ll.addView(ll1);
            ll.addView(ll2);
            ll.addView(ll3);
            ll_add.addView(ll);
            if(count==1){
                MyTextView tv1 = new MyTextView(context);
                tv1.setText(data.get(data.size()-1).Title_CN);
                tv1.tvName=data.get(data.size()-1).Title_CN;
                tv1.tvId=data.get(data.size()-1).Id;
                setView(tv1);
                ll1.addView(tv1);
            }else if(count==2){
                MyTextView tv1 = new MyTextView(context);
                tv1.setText(data.get(data.size()-2).Title_CN);
                tv1.tvName=data.get(data.size()-2).Title_CN;
                tv1.tvId=data.get(data.size()-2).Id;
                setView(tv1);
                ll1.addView(tv1);
                MyTextView tv2 = new MyTextView(context);
                tv2.setText(data.get(data.size()-1).Title_CN);
                tv2.tvName=data.get(data.size()-1).Title_CN;
                tv2.tvId=data.get(data.size()-1).Id;
                setView(tv2);
                ll2.addView(tv2);
            }
        }
    }

    public void setView(TextView v) {
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(-2, -2);
        llp2.height = 110;
        llp2.width = 230;
        v.setGravity(Gravity.CENTER);
        v.setLayoutParams(llp2);
        v.setTextAppearance(context, R.style.black36);
        v.setBackground(gd_n);
        v.setOnClickListener(this);
    }
    public void seting(){
        gd_n = new GradientDrawable();
        gd_n.setColor(Color.parseColor("#ffffff"));
        gd_n.setStroke(2, Color.parseColor("#00aaee"));
        gd_y = new GradientDrawable();
        gd_y.setColor(Color.parseColor("#00aaee"));
        gd_y.setStroke(2, Color.parseColor("#00aaee"));
    }

    @Override
    public void onClick(View v) {
        MyTextView tv=(MyTextView) v;
        if(lastView!=null){
            lastView.setBackground(gd_n);
            lastView.setTextAppearance(context, R.style.black36);
        }
        lastView= tv;
        tv.setBackground(gd_y);
        tv.setTextAppearance(context, R.style.white36);
        Intent intent=new Intent(context, AddStoreActivity.class);
        intent.putExtra("type",tv.tvName);
        intent.putExtra("id",tv.tvId);
        startActivity(intent);
    }
}
