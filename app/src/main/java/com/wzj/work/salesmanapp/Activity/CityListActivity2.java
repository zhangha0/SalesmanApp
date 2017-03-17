package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.Area;
import com.wzj.work.salesmanapp.OtherClass.City;
import com.wzj.work.salesmanapp.OtherClass.CityData;
import com.wzj.work.salesmanapp.OtherClass.CityManage;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public class CityListActivity2 extends AutoLayoutActivity {
    private ArrayList<CityData> provinces=new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Area> areas = new ArrayList<>();
    private String area;
    private String provinceId;
    private String cityId;
    private String areaId;
    private ProgressBar pb;
    private BaseAdapter ba1;
    private BaseAdapter ba2;
    private BaseAdapter ba3;
    private ListView lv_province;
    private ListView lv_city;
    private ListView lv_area;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                pb.setVisibility(View.GONE);
                String str = msg.obj.toString();
                Gson g = new Gson();
                CityManage cityManage = g.fromJson(str, CityManage.class);
                provinces=cityManage.data;
                initBa();
                lv_province.setAdapter(ba1);
                lv_city.setAdapter(ba2);
                lv_area.setAdapter(ba3);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list2);
        pb= (ProgressBar) findViewById(R.id.pb_cityList2);
        lv_area= (ListView) findViewById(R.id.lv_area);
        lv_city= (ListView) findViewById(R.id.lv_city);
        lv_province= (ListView) findViewById(R.id.lv_province);
        doNet();
    }
    public void doNet() {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Shop/GetProvinceOrCity");
        funNet.toolNet(params, handler, 1);
    }
    public void initBa(){
        ba1=new BaseAdapter() {
            @Override
            public int getCount() {
                return provinces.size();
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
                TextView tv;
                if(convertView==null){
                    tv=new TextView(CityListActivity2.this);
                    convertView=tv;
                    convertView.setTag(tv);
                }else {
                    tv= (TextView) convertView.getTag();
                }
                tv.setGravity(Gravity.CENTER);
                tv.setText(provinces.get(position).province);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        provinceId=provinces.get(position).Id;
                        cityId=provinces.get(position).city.get(0).Id;
                        cities=provinces.get(position).city;
                        areas=provinces.get(position).city.get(0).area;
                        ba2.notifyDataSetChanged();
                        ba3.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        };

        ba2=new BaseAdapter() {
            @Override
            public int getCount() {
                return cities.size();
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
                TextView tv;
                if(convertView==null){
                    tv=new TextView(CityListActivity2.this);
                    convertView=tv;
                    convertView.setTag(tv);
                }else {
                    tv= (TextView) convertView.getTag();
                }
                tv.setGravity(Gravity.CENTER);
                tv.setText(cities.get(position).ProvinceCityName);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cityId=cities.get(position).Id;
                        areas=cities.get(position).area;
                        ba3.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        };
        ba3=new BaseAdapter() {
            @Override
            public int getCount() {
                return areas.size();
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
                TextView tv;
                if(convertView==null){
                    tv=new TextView(CityListActivity2.this);
                    convertView=tv;
                    convertView.setTag(tv);
                }else {
                    tv= (TextView) convertView.getTag();
                }
                tv.setGravity(Gravity.CENTER);
                tv.setText(areas.get(position).ProvinceCityName);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areaId=areas.get(position).Id;
                        area=areas.get(position).ProvinceCityName;
                        Intent intent=getIntent();
                        intent.putExtra("area",area);
                        intent.putExtra("areaId",areaId);
                        intent.putExtra("cityId",cityId);
                        intent.putExtra("provinceId",provinceId);
                        CityListActivity2.this.setResult(100,intent);
                        CityListActivity2.this.finish();
                    }
                });
                return convertView;
            }
        };
    }
}
