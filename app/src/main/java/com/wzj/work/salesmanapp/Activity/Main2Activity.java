package com.wzj.work.salesmanapp.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.wzj.work.salesmanapp.Fragment.FragmentDown;
import com.wzj.work.salesmanapp.Fragment.Fragment_add;
import com.wzj.work.salesmanapp.Fragment.Fragment_apply;
import com.wzj.work.salesmanapp.Fragment.Fragment_home;
import com.wzj.work.salesmanapp.Fragment.Fragment_mine;
import com.wzj.work.salesmanapp.OtherClass.ManData;
import com.wzj.work.salesmanapp.R;
import com.zhy.autolayout.AutoLayoutActivity;

public class Main2Activity extends AutoLayoutActivity implements FragmentDown.Callbacks{
    Fragment_home fUp1;
    Fragment_add fUp2;
    Fragment_apply fUp3;
    Fragment_mine fUp4;
    FragmentManager fm;
    FragmentTransaction ft;
    public static ManData manData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle=getIntent().getExtras();
        manData= (ManData) bundle.getSerializable("ManData");
        Selckted(1);
    }

    @Override
    public void Selckted(int index) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        hideFragments(ft);
        switch (index){
            case 1:
                if(fUp1==null){
                    fUp1=new Fragment_home();
                    ft.add(R.id.flUp, fUp1).commit();
                }else {
                    ft.show(fUp1).commit();
                }

                break;
            case 2:
                if(fUp2==null){
                    fUp2=new Fragment_add();
                    ft.add(R.id.flUp,fUp2).commit();
                }else{
                    ft.show(fUp2).commit();
                }
                break;
            case 3:
                if(fUp3==null){
                    fUp3=new Fragment_apply();
                    ft.add(R.id.flUp,fUp3).commit();
                }else{
                    ft.show(fUp3).commit();
                }

                break;
            case 4:
                if(fUp4==null){
                    fUp4=new Fragment_mine();
                    ft.add(R.id.flUp,fUp4).commit();
                }else{
                    ft.show(fUp4).commit();
                }
                break;
        }
    }
    public void hideFragments(FragmentTransaction ft) {
        if (fUp1 != null)
            ft.hide(fUp1);
        if (fUp2 != null)
            ft.hide(fUp2);
        if (fUp3 != null)
            ft.hide(fUp3);
        if (fUp4 != null)
            ft.hide(fUp4);
    }
}
