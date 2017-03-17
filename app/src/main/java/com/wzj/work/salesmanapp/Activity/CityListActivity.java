package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.Area;
import com.wzj.work.salesmanapp.OtherClass.City;
import com.wzj.work.salesmanapp.OtherClass.CityManage;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/6.
 */
public class CityListActivity extends AutoLayoutActivity {
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Area> areas = new ArrayList<>();
    private ExpandableListAdapter elAdapter;
    private ExpandableListView eListView;
    private ProgressBar pb;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                pb.setVisibility(View.GONE);
                String str = msg.obj.toString();
                Log.e("zhanghao: ", str + "");
                Gson g = new Gson();
                CityManage cityManage = g.fromJson(str, CityManage.class);
                cities = cityManage.data.get(0).city;
                areas = cityManage.data.get(0).city.get(0).area;
                setElAdapter();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_citylist);
        eListView= (ExpandableListView) findViewById(R.id.el_cityList);
        pb= (ProgressBar) findViewById(R.id.pb_cityList);
        doNet();
    }

    public void doNet() {
        RequestParams params = new RequestParams("http://37.59.66.172:86/Shop/GetProvinceOrCity");
        funNet.toolNet(params, handler, 1);
    }
    public void setElAdapter(){
        elAdapter=new ExpandableListAdapter() {
            private TextView getTextView(){
                AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                TextView textView=new TextView(CityListActivity.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                textView.setPadding(80,0,0,0);
                textView.setTextSize(15);
                return textView;
            }
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return cities.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return cities.get(groupPosition).area.size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return cities.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return cities.get(groupPosition).area.get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LinearLayout ll=new LinearLayout(CityListActivity.this);
                ll.setOrientation(0);
                TextView textView=getTextView();
                textView.setText(((City)getGroup(groupPosition)).ProvinceCityName);
                textView.setTextSize(20);
                ll.addView(textView);
                return ll;
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView=getTextView();
                textView.setText(((Area)getChild(groupPosition,childPosition)).ProvinceCityName);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=getIntent();
                        intent.putExtra("area",cities.get(groupPosition).area.get(childPosition).ProvinceCityName);
                        intent.putExtra("city",cities.get(groupPosition).ProvinceCityName);
                        CityListActivity.this.setResult(100,intent);
                        CityListActivity.this.finish();
                    }
                });
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
//                Intent intent=getIntent();
//                intent.putExtra("area",cities.get(groupPosition).area.get(childPosition).ProvinceCityName);
//                intent.putExtra("city",cities.get(groupPosition).ProvinceCityName);
//                CityListActivity.this.setResult(100,intent);
//                CityListActivity.this.finish();
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        };
        eListView.setAdapter(elAdapter);
    }
}
