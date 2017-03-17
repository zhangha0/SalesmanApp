package com.wzj.work.salesmanapp.OtherClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/2.
 */
public class GroupBuy implements Serializable{
    public String Id;
    public String Title_CN;
    public String Title_EN;
    public String Title_ES;
    public String Price;
    public String Discountprice;
    public String ImageUrl;
    public String IsCN;
    public String IsEN;
    public String IsES;
    public String AddDateTime;
    public String IngredientsIconArray;
    public String Introduce_CN;
    public String Introduce_EN;
    public String Introduce_ES;
    public String ShopId;
    public ArrayList<GroupComboList> ComboList;
}
