package com.wzj.work.salesmanapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzj.work.salesmanapp.MyView.MyGroup;
import com.wzj.work.salesmanapp.R;

/**
 * Created by 44967 on 2017/2/28.
 */
public class FragmentDown extends Fragment implements View.OnClickListener {
    Callbacks back;
    View lastView;
    Context context;
    @Override
    public void onClick(View v) {
        if (lastView != null) {
            changeColor((MyGroup) lastView,true);
        }
        changeColor((MyGroup) v,false);
        lastView = v;
        back.Selckted(((MyGroup)v).index);
    }
   public void changeColor(MyGroup view,boolean flag){
       switch (view.index){
           case 1:
               if(flag){
                   view.getChildAt(0).setBackgroundResource(R.drawable.home_n);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.gray20);
               }else {
                   view.getChildAt(0).setBackgroundResource(R.drawable.home_y);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.blue20);
               }
               break;
           case 2:
               if(flag){
                   view.getChildAt(0).setBackgroundResource(R.drawable.add_n);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.gray20);
               }else {
                   view.getChildAt(0).setBackgroundResource(R.drawable.add_y);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.blue20);
               }
               break;
           case 3:
               if(flag){
                   view.getChildAt(0).setBackgroundResource(R.drawable.apply_n);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.gray20);
               }else {
                   view.getChildAt(0).setBackgroundResource(R.drawable.apply_y);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.blue20);
               }
               break;
           case 4:
               if(flag){
                   view.getChildAt(0).setBackgroundResource(R.drawable.mine_n);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.gray20);
               }else {
                   view.getChildAt(0).setBackgroundResource(R.drawable.mine_y);
                   ((TextView)view.getChildAt(1)).setTextAppearance(context,R.style.blue20);
               }
               break;

       }
   }
    public interface Callbacks {
        void Selckted(int viewID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_down, container, false);
        MyGroup myGroup1=((MyGroup) view.findViewById(R.id.myGroup1));
        MyGroup myGroup2=((MyGroup) view.findViewById(R.id.myGroup2));
        MyGroup myGroup3=((MyGroup) view.findViewById(R.id.myGroup3));
        MyGroup myGroup4=((MyGroup) view.findViewById(R.id.myGroup4));
        myGroup1.index=1;
        myGroup2.index=2;
        myGroup3.index=3;
        myGroup4.index=4;
        myGroup1.setOnClickListener(this);
        myGroup2.setOnClickListener(this);
        myGroup3.setOnClickListener(this);
        myGroup4.setOnClickListener(this);
        myGroup1.setBackgroundColor(0xfff);
        lastView=myGroup1;
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
        }
        back = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

