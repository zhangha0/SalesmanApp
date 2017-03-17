package com.wzj.work.salesmanapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.Adapter.RecycleAddMtoAdapter;
import com.wzj.work.salesmanapp.MyView.MyTextView;
import com.wzj.work.salesmanapp.OtherClass.Dishes;
import com.wzj.work.salesmanapp.OtherClass.ShopDishes;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class AddMtoFoodActivity extends AutoLayoutActivity {
    private RecyclerView recyclerView;
    private RecycleAddMtoAdapter recycleAdapter;
    private RecyclerView.LayoutManager mLayoutMannager;
    private ProgressDialog proDialog;
    private String shopId;
    private String takeWayId;
    private ArrayList<Dishes> dishesList=new ArrayList<>();
    private ShopDishes shopDishes;
    private BaseAdapter ba;
    private ListView lv;
    private ArrayList<String> foodList=new ArrayList<>();
    int[] picFood = {R.mipmap.food11, R.mipmap.food22, R.mipmap.food33, R.mipmap.food44, R.mipmap.food55,
            R.mipmap.food66, R.mipmap.food77, R.mipmap.food88, R.mipmap.food99};
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                proDialog.dismiss();
                String result=msg.obj.toString();
                if(funNet.isOk(result,AddMtoFoodActivity.this)){
                    Gson g=new Gson();
                    shopDishes=g.fromJson(result,ShopDishes.class);
                    if(shopDishes.data.size()>0){
                        dishesList=shopDishes.data;
                        setAdapter();
//                        recyclerView.setLayoutManager(mLayoutMannager);
//                        recyclerView.setHasFixedSize(true);
//                        recycleAdapter=new RecycleAddMtoAdapter(AddMtoFoodActivity.this,dishesList,shopId);
//                        recyclerView.setAdapter(recycleAdapter);
                    }
                }
            }else if(msg.what==2){
                proDialog.dismiss();
                String result=msg.obj.toString();
                if(funNet.isOk(result,AddMtoFoodActivity.this)){
                    finish();
                    Toast.makeText(AddMtoFoodActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddMtoFoodActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mto_food);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        foodList= (ArrayList<String>) bundle.getSerializable("foodList");
        shopId=intent.getStringExtra("shopId");
        takeWayId=intent.getStringExtra("takeWayId");
        initView();
    }

    public void initView() {
        GradientDrawable gd_n;
        gd_n = new GradientDrawable();
        gd_n.setColor(Color.parseColor("#ffffff"));
        gd_n.setStroke(2, Color.parseColor("#00aaee"));
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_title);
        iv_back.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodArray="";
                if(foodList.size()>0){
                    for (int i = 0; i < foodList.size(); i++) {
                        foodArray+=foodList.get(i)+",";
                    }
                    foodArray=foodArray.substring(0,foodArray.length()-1);
                }
                progressDialog("添加中...");
                RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/AddTakeaway");
                params.addBodyParameter("ShopId", shopId);
                params.addBodyParameter("takeWayId", takeWayId);
                params.addBodyParameter("FoodArray", foodArray);
                funNet.toolNet(params, handler, 2);
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.my_title);
        tv_title.setText(R.string.add);
        TextView tv_title_add = (TextView) findViewById(R.id.tv_title_add);
        tv_title_add.setText(R.string.sure);
        tv_title_add.setTextColor(0xff00aaee);
        tv_title_add.setBackground(gd_n);
        lv= (ListView) findViewById(R.id.lv_addMto);
        doSomething();
    }
    public void doSomething(){
        mLayoutMannager = new LinearLayoutManager(this);
//        recyclerView= (RecyclerView) findViewById(R.id.rv_addMto);
        progressDialog("加载中...");
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/GetShopFood");
        params.addBodyParameter("ShopId", shopId);
        funNet.toolNet(params, handler, 1);
    }
    public void progressDialog(String s) {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage(s);
        proDialog.show();
    }
    public void setAdapter(){
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return dishesList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder vh=null;
                if(convertView==null){
                    convertView= LayoutInflater.from(AddMtoFoodActivity.this).inflate(R.layout.layout_mtakeout_item,null);
                    vh=new ViewHolder();
                    vh.iv_mto= (ImageView) convertView.findViewById(R.id.iv_mto);
                    vh.tv_title= (TextView) convertView.findViewById(R.id.tv_mto_title);
                    vh.ll_array= (LinearLayout) convertView.findViewById(R.id.ll_array);
                    vh.tv_price= (TextView) convertView.findViewById(R.id.tv_mto_price);
                    vh.tv_discountPrice= (TextView) convertView.findViewById(R.id.tv_mto_discountprice);
                    vh.tv_ok= (MyTextView) convertView.findViewById(R.id.tv_ok);
                    convertView.setTag(vh);
                }else {
                    vh= (ViewHolder) convertView.getTag();
                }
                x.image().bind(vh.iv_mto, getResources().getString(R.string.http_tu) + dishesList.get(position).ImageUrl);
                vh.tv_title.setText(dishesList.get(position).Title_CN);
                vh.tv_discountPrice.setText("€" + dishesList.get(position).Discountprice);
                vh.tv_price.setText(dishesList.get(position).Price);
                vh.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                MyTextView mtv=vh.tv_ok;
                mtv.setVisibility(View.VISIBLE);
                mtv.isOk=false;
                mtv.setBackgroundResource(R.drawable.choose_no);
                for (int i = 0; i < foodList.size(); i++) {
                    if(foodList.get(i).equals(dishesList.get(position).Id)){
                        mtv.isOk=true;
                        mtv.setBackgroundResource(R.drawable.choose_yes);
                    }
                }
                mtv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyTextView mtv= (MyTextView) v;
                        if(mtv.isOk){
                            mtv.setBackgroundResource(R.drawable.choose_no);
                            mtv.isOk=false;
                            foodList.remove(dishesList.get(position).Id);
                        }else {
                            mtv.setBackgroundResource(R.drawable.choose_yes);
                            mtv.isOk=true;
                            foodList.add(dishesList.get(position).Id);
                        }
                    }
                });
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1, -1);
                llp.width = 70;
                llp.height = 70;
                String str = dishesList.get(position).IngredientsIconArray;
                String[] foodArray = null;
                if (!str.equals("null")) {
                    foodArray = str.split(",");
                    vh.ll_array.removeAllViews();
                    for (int i = 0; i < foodArray.length; i++) {
                        if (!foodArray[i].equals("null")&&!foodArray[i].equals("0")) {
                            int t = Integer.parseInt(foodArray[i]);
                            TextView tv = new TextView(AddMtoFoodActivity.this);
                            tv.setLayoutParams(llp);
                            tv.setBackgroundResource(picFood[t-1]);
                            vh.ll_array.addView(tv);
                        }

                    }
                }
                return convertView;
            }
        };
        lv.setAdapter(ba);
    }
}
class ViewHolder{
    ImageView iv_mto;
    TextView tv_title;
    LinearLayout ll_array;
    TextView tv_discountPrice;
    TextView tv_price;
    MyTextView tv_ok;
}