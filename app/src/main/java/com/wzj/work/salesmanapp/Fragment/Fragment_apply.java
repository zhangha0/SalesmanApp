package com.wzj.work.salesmanapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzj.work.salesmanapp.R;

/**
 * Created by 44967 on 2017/2/28.
 */
public class Fragment_apply extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_apply, container, false);
        TextView tv= (TextView) view.findViewById(R.id.my_title);
        tv.setText(R.string.apply_title);
        return view;
    }
}
