package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzj.work.salesmanapp.OtherClass.Area;
import com.wzj.work.salesmanapp.OtherClass.City;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/5.
 */
public class CityListAdapter implements ExpandableListAdapter {
    private Context context;
    private ArrayList<City> cities;
    private ArrayList<Area> areas;
    public CityListAdapter(Context context,ArrayList<City> cities,ArrayList<Area> areas){
        this.context=context;
        this.cities=cities;
        this.areas=areas;
    }
    private TextView getTextView(){
        AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        TextView textView=new TextView(context);
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
        LinearLayout ll=new LinearLayout(context);
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
            }
        });
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
//                Intent intent=getIntent();
//                intent.putExtra("city",cities[groupPosition][childPosition]);
//                Main2Activity.this.setResult(1,intent);
//                Main2Activity.this.finish();

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
}
