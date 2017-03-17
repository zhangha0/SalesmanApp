package com.wzj.work.salesmanapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.Dishes;
import com.wzj.work.salesmanapp.OtherClass.GroupBuy;
import com.wzj.work.salesmanapp.OtherClass.PictureData;
import com.wzj.work.salesmanapp.OtherClass.ShopDishes;
import com.wzj.work.salesmanapp.OtherClass.UploadFood;
import com.wzj.work.salesmanapp.OtherClass.UploadGroup;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ContentView(R.layout.activity_add_group_buy)
public class AddGroupBuyActivity extends AutoLayoutActivity {
    private ProgressDialog proDialog;
    private String shopId;
    private GroupBuy groupBuy;
    private final int GET_DISHES = 20;
    private final int CODE = 21;
    private final int CODE2 = 22;
    private final int CODE3 = 23;
    private ArrayList<Dishes> disheslist = new ArrayList<>();
    private ArrayList<Dishes> sweetList = new ArrayList<>();
    private ArrayList<Dishes> drankList = new ArrayList<>();
    private UploadFood firFood;
    private UploadFood secFood;
    private UploadFood thiFood;
    private UploadFood sweetFood;
    private UploadFood drankFood;
    private UploadGroup group;
    private String imageUrl;
    private String myJson;
    private String option;
    private String ingredientsIconArray = "";
    private String originalId="";
    ShopDishes shopDishes;
    ArrayList<String> pic = new ArrayList<>();
    @ViewInject(R.id.et_GroupCode)
    EditText et_code;
    @ViewInject(R.id.et_groupPrice)
    EditText et_price;
    @ViewInject(R.id.et_groupDiscountprice)
    EditText et_discountPrice;
    @ViewInject(R.id.et_groupTitle_ES)
    EditText et_titleES;
    @ViewInject(R.id.et_groupTitle_EN)
    EditText et_titleEN;
    @ViewInject(R.id.et_groupTitle_CN)
    EditText et_titleCN;
    @ViewInject(R.id.et_groupIntro_ES)
    EditText et_introES;
    @ViewInject(R.id.et_groupIntro_EN)
    EditText et_introEN;
    @ViewInject(R.id.et_groupIntro_CN)
    EditText et_introCN;
    @ViewInject(R.id.tv_firstFood)
    TextView tv_firstFood;
    @ViewInject(R.id.tv_secondFood)
    TextView tv_secFood;
    @ViewInject(R.id.tv_thirdFood)
    TextView tv_thiFood;
    @ViewInject(R.id.tv_group_sweet)
    TextView tv_sweet;
    @ViewInject(R.id.tv_group_drank)
    TextView tv_drank;
    @ViewInject(R.id.iv_group)
    ImageView iv_group;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_DISHES) {
                proDialog.dismiss();
                String dishes = msg.obj.toString();
                if (funNet.isOk(dishes, AddGroupBuyActivity.this)) {
                    Gson g = new Gson();
                    shopDishes = g.fromJson(dishes, ShopDishes.class);
                    for (int i = 0; i < shopDishes.data.size(); i++) {
                        switch (shopDishes.data.get(i).FoodTypeId) {
                            case "1":
                                disheslist.add(shopDishes.data.get(i));
                                break;
                            case "4":
                                sweetList.add(shopDishes.data.get(i));
                                break;
                            case "5":
                                drankList.add(shopDishes.data.get(i));
                                break;
                        }
                    }
                }
            } else if (msg.what == CODE2) {
                String pic = msg.obj.toString();
                Gson g = new Gson();
                try {
                    if (funNet.isOk(pic, AddGroupBuyActivity.this)) {
                        PictureData pictureData = g.fromJson(pic, PictureData.class);
                        imageUrl = pictureData.data;
                        getJson();
                    } else {
                        proDialog.dismiss();
                    }
                } catch (Exception e) {
                    proDialog.dismiss();
                    Toast.makeText(AddGroupBuyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == CODE3) {
                String result = msg.obj.toString();
                if (funNet.isOk(result, AddGroupBuyActivity.this)) {
                    finish();
                    Toast.makeText(AddGroupBuyActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    proDialog.dismiss();
                    Toast.makeText(AddGroupBuyActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 100) {
                proDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        option = intent.getStringExtra("option");
        if (option.equals("1")) {
            shopId = intent.getStringExtra("shopId");
        } else if (option.equals("2")) {
            groupBuy = (GroupBuy) intent.getSerializableExtra("groupBuy");
            shopId = groupBuy.ShopId;
            setView();
        }
        getDishes();
        initView();
        doSomething();
    }

    public void setView() {
        originalId=groupBuy.Id;
        et_code.setText(groupBuy.Id);
        et_price.setText(groupBuy.Price);
        et_discountPrice.setText(groupBuy.Discountprice);
        et_titleES.setText(groupBuy.Title_ES);
        et_titleEN.setText(groupBuy.Title_EN);
        et_titleCN.setText(groupBuy.Title_CN);
        et_introES.setText(groupBuy.Introduce_ES);
        et_introEN.setText(groupBuy.Introduce_EN);
        et_introCN.setText(groupBuy.Introduce_CN);
        x.image().bind(iv_group, getResources().getString(R.string.http_tu) + groupBuy.ImageUrl);
        imageUrl = groupBuy.ImageUrl;
        if (groupBuy.ComboList.get(0).Foods != null) {
            tv_firstFood.setText(groupBuy.ComboList.get(0).Foods.Title_CN);
            firFood = new UploadFood();
            firFood.Title_CN = "第一道菜";
            firFood.Title_EN = "第一道菜";
            firFood.Title_ES = "第一道菜";
            firFood.Foods = new ArrayList<String>();
            firFood.Foods.add(groupBuy.ComboList.get(0).Foods.Id);
            ingredientsIconArray += groupBuy.ComboList.get(0).Foods.IngredientsIconArray + ",";
        }
        if (groupBuy.ComboList.get(1).Foods != null) {
            tv_secFood.setText(groupBuy.ComboList.get(1).Foods.Title_CN);
            secFood = new UploadFood();
            secFood.Title_CN = "第二道菜";
            secFood.Title_EN = "第二道菜";
            secFood.Title_ES = "第二道菜";
            secFood.Foods = new ArrayList<String>();
            secFood.Foods.add(groupBuy.ComboList.get(1).Foods.Id);
            ingredientsIconArray += groupBuy.ComboList.get(1).Foods.IngredientsIconArray + ",";
        }
        if (groupBuy.ComboList.get(2).Foods != null) {
            tv_thiFood.setText(groupBuy.ComboList.get(2).Foods.Title_CN);
            thiFood = new UploadFood();
            thiFood.Title_CN = "第三道菜";
            thiFood.Title_EN = "第三道菜";
            thiFood.Title_ES = "第三道菜";
            thiFood.Foods = new ArrayList<String>();
            thiFood.Foods.add(groupBuy.ComboList.get(2).Foods.Id);
            ingredientsIconArray += groupBuy.ComboList.get(2).Foods.IngredientsIconArray + ",";
        }
        if (groupBuy.ComboList.get(3).Foods != null) {
            tv_sweet.setText(groupBuy.ComboList.get(3).Foods.Title_CN);
            sweetFood = new UploadFood();
            sweetFood.Title_CN = "甜品";
            sweetFood.Title_EN = "甜品";
            sweetFood.Title_ES = "甜品";
            sweetFood.Foods = new ArrayList<String>();
            sweetFood.Foods.add(groupBuy.ComboList.get(3).Foods.Id);
            ingredientsIconArray += groupBuy.ComboList.get(3).Foods.IngredientsIconArray + ",";
        }
        if (groupBuy.ComboList.get(4).Foods != null) {
            tv_drank.setText(groupBuy.ComboList.get(4).Foods.Title_CN);
            drankFood = new UploadFood();
            drankFood.Title_CN = "饮品";
            drankFood.Title_EN = "饮品";
            drankFood.Title_ES = "饮品";
            drankFood.Foods = new ArrayList<String>();
            drankFood.Foods.add(groupBuy.ComboList.get(4).Foods.Id);
            ingredientsIconArray += groupBuy.ComboList.get(4).Foods.IngredientsIconArray + ",";
        }

    }

    public String fun(String s) {
        String s2 = "";
        String[] str = s.split(",");
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < str.length; i++) {
            try {
                int t = Integer.parseInt(str[i]);
                set.add(t);
            } catch (Exception e) {

            }
        }
        for (Integer tt : set) {
            s2 += tt + ",";
        }
        String ss = s2.substring(0, s2.length() - 1);
        return ss;
    }

    public void isReady() {
        if ((!et_code.getText().toString().equals("")) && ((!et_price.getText().toString().equals("")))
                && ((!et_discountPrice.getText().toString().equals(""))) && ((!et_titleCN.getText().toString().equals("")))
                && ((!et_titleEN.getText().toString().equals(""))) && ((!et_titleES.getText().toString().equals("")))
                && ((!et_introCN.getText().toString().equals(""))) && ((!et_introEN.getText().toString().equals("")))
                && ((!et_introES.getText().toString().equals(""))) && firFood != null && secFood != null && thiFood != null
                && secFood != null && drankFood != null) {
            if (pic.size() > 0) {
                progressDialog("正在添加...");
                String url = getResources().getString(R.string.http_msg) + "/Common/UploadFoodImage";
                funNet.uploadfile(url, pic, handler, CODE2);
            } else if (imageUrl != null) {
                getJson();
            } else {
                Toast.makeText(AddGroupBuyActivity.this, "请填完整信息", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(AddGroupBuyActivity.this, "请填完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void getJson() {
        ingredientsIconArray = fun(ingredientsIconArray);
        group = new UploadGroup();
        group.GroupCode = et_code.getText().toString();
        group.Price = et_price.getText().toString();
        group.Discountprice = et_discountPrice.getText().toString();
        group.Title_CN = et_titleCN.getText().toString();
        group.Title_EN = et_titleEN.getText().toString();
        group.Title_ES = et_titleES.getText().toString();
        group.Introduce_CN = et_introCN.getText().toString();
        group.Introduce_EN = et_introEN.getText().toString();
        group.Introduce_ES = et_introES.getText().toString();
        group.FoodTypeId = "2";
        group.ImageUrl = imageUrl;
        group.IngredientsIconArray = ingredientsIconArray;
        group.ComboList = new ArrayList<>();
        group.ComboList.add(firFood);
        group.ComboList.add(secFood);
        group.ComboList.add(thiFood);
        group.ComboList.add(sweetFood);
        group.ComboList.add(drankFood);
        Gson g = new Gson();
        myJson = g.toJson(group);
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/AddOrUpdate-GroupFood");
        params.addQueryStringParameter("shopId", shopId);
        params.addQueryStringParameter("groupData", myJson);
        params.addQueryStringParameter("option", option);
        params.addQueryStringParameter("OriginalId", originalId);
        funNet.toolNet(params, handler, CODE3);
    }

    public void initView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_title);
        iv_back.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.my_title);
        tv_title.setText(R.string.add_groupBuy);
        TextView tv_title_add = (TextView) findViewById(R.id.tv_title_add);
        tv_title_add.setText(R.string.add);
        tv_title_add.setBackgroundResource(R.drawable.shape_kuang);
        tv_title_add.setTextColor(0xffd9d7d7);
        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReady();
            }
        });
    }

    public void doSomething() {
        iv_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGroupBuyActivity.this, ImagesSelectorActivity.class);
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, pic);
                startActivityForResult(intent, CODE);
            }
        });
        tv_firstFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] str = new String[disheslist.size()];
                final TextView tv = (TextView) v;
                for (int i = 0; i < disheslist.size(); i++) {
                    str[i] = disheslist.get(i).Title_CN;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
                builder.setTitle("选择一个菜品");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(str[which]);
                        firFood = new UploadFood();
                        firFood.Title_CN = "第一道菜";
                        firFood.Title_EN = "第一道菜";
                        firFood.Title_ES = "第一道菜";
                        firFood.Foods = new ArrayList<String>();
                        firFood.Foods.add(disheslist.get(which).Id);
                        ingredientsIconArray += disheslist.get(which).IngredientsIconArray + ",";
                    }
                });
                builder.show();
            }
        });
        tv_secFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] str = new String[disheslist.size()];
                final TextView tv = (TextView) v;
                for (int i = 0; i < disheslist.size(); i++) {
                    str[i] = disheslist.get(i).Title_CN;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
                builder.setTitle("选择一个菜品");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(str[which]);
                        secFood = new UploadFood();
                        secFood.Title_CN = "第二道菜";
                        secFood.Title_EN = "第二道菜";
                        secFood.Title_ES = "第二道菜";
                        secFood.Foods = new ArrayList<String>();
                        secFood.Foods.add(disheslist.get(which).Id);
                        ingredientsIconArray += disheslist.get(which).IngredientsIconArray + ",";
                    }
                });
                builder.show();
            }
        });
        tv_thiFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] str = new String[disheslist.size()];
                final TextView tv = (TextView) v;
                for (int i = 0; i < disheslist.size(); i++) {
                    str[i] = disheslist.get(i).Title_CN;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
                builder.setTitle("选择一个菜品");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(str[which]);
                        thiFood = new UploadFood();
                        thiFood.Title_CN = "第三道菜";
                        thiFood.Title_EN = "第三道菜";
                        thiFood.Title_ES = "第三道菜";
                        thiFood.Foods = new ArrayList<String>();
                        thiFood.Foods.add(disheslist.get(which).Id);
                        ingredientsIconArray += disheslist.get(which).IngredientsIconArray + ",";
                    }
                });
                builder.show();
            }
        });
        tv_sweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] str = new String[sweetList.size()];
                final TextView tv = (TextView) v;
                for (int i = 0; i < sweetList.size(); i++) {
                    str[i] = sweetList.get(i).Title_CN;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
                builder.setTitle("选择一个菜品");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(str[which]);
                        sweetFood = new UploadFood();
                        sweetFood.Title_CN = "甜品";
                        sweetFood.Title_EN = "甜品";
                        sweetFood.Title_ES = "甜品";
                        sweetFood.Foods = new ArrayList<String>();
                        sweetFood.Foods.add(sweetList.get(which).Id);
                        ingredientsIconArray += disheslist.get(which).IngredientsIconArray + ",";
                    }
                });
                builder.show();
            }
        });
        tv_drank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] str = new String[drankList.size()];
                final TextView tv = (TextView) v;
                for (int i = 0; i < drankList.size(); i++) {
                    str[i] = drankList.get(i).Title_CN;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
                builder.setTitle("选择一个菜品");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(str[which]);
                        drankFood = new UploadFood();
                        drankFood.Title_CN = "饮品";
                        drankFood.Title_EN = "饮品";
                        drankFood.Title_ES = "饮品";
                        drankFood.Foods = new ArrayList<String>();
                        drankFood.Foods.add(drankList.get(which).Id);
                        ingredientsIconArray += disheslist.get(which).IngredientsIconArray + ",";
                    }
                });
                builder.show();
            }
        });
    }

    public void getDishes() {
        progressDialog("加载中...");
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/GetShopFood");
        params.addBodyParameter("ShopId", shopId);
        funNet.toolNet(params, handler, GET_DISHES);
    }

    public void creatDialog(final String[] str, final TextView tv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupBuyActivity.this);
        builder.setTitle("选择一个菜品");
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv.setText(str[which]);
            }
        });
        builder.show();
    }

    public void progressDialog(String s) {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage(s);
        proDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CODE) {
                if (resultCode == RESULT_OK) {
                    pic = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    if (pic.size() > 0) {
                        Glide
                                .with(AddGroupBuyActivity.this)
                                .load(pic.get(0))
                                .into(iv_group);
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
