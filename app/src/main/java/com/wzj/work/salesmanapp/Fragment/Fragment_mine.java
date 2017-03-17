package com.wzj.work.salesmanapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzj.work.salesmanapp.Activity.Main2Activity;
import com.wzj.work.salesmanapp.Activity.MainActivity;
import com.wzj.work.salesmanapp.R;

/**
 * Created by 44967 on 2017/2/28.
 */
public class Fragment_mine extends Fragment{
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mine, container, false);
        pre = context.getSharedPreferences("loginMsg", context.MODE_PRIVATE);
        editor = pre.edit();
        initView(view);
        return view;
    }
    public void initView(View view){
        TextView tv= (TextView) view.findViewById(R.id.my_title);
        tv.setText(R.string.mine_title);
        TextView tvR= (TextView) view.findViewById(R.id.right_tv2);
        tvR.setText(R.string.mine_out);
        tvR.setVisibility(View.VISIBLE);
        tvR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isLogin", false);
                editor.commit();
                Intent intent=new Intent(context, MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        TextView tv_name= (TextView) view.findViewById(R.id.tv_mine_name);
        TextView tv_phone= (TextView) view.findViewById(R.id.tv_mine_phone);
        tv_name.setText(context.getResources().getString(R.string.saleman_name)+ Main2Activity.manData.Name);
        tv_phone.setText(context.getResources().getString(R.string.saleman_phone)+Main2Activity.manData.Mobile);
    }
}
