package com.wzj.work.salesmanapp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.PictureData;
import com.wzj.work.salesmanapp.OtherClass.UpShopBack;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_add_store)
public class AddStoreActivity extends AutoLayoutActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE = 731;
    private static final int REQUEST_CODE_PIUTURE = 730;
    private static final int REQUEST_CODE_CITY = 123;
    private static final int UP_LOGO = 20;
    private static final int UP_PIC = 21;
    private static final int UP_MSG = 22;
    private static final int UP_OK = 23;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private ArrayList<String> mResults_logo = new ArrayList<>();
    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<ImageView> shopImageList = new ArrayList<>();
    private String pictureList ="";
    private String type;
    private LocationManager locationManager;
    private String provider;
    private String logo;
    private String provinceId;
    private String cityId;
    private String areaId;
    private String shopTypeId;
    private boolean selected[] = {false, false, false, false, false, false, false};
    ImageOptions options;
    ProgressDialog proDialog;

    @ViewInject(R.id.im_storeLogo)
    ImageView im_storeLogo;
    @ViewInject(R.id.tv_Address)
    TextView tv_Address;
    @ViewInject(R.id.iv_upShop0)
    ImageView iv_upShop0;
    @ViewInject(R.id.iv_upShop1)
    ImageView iv_upShop1;
    @ViewInject(R.id.iv_upShop2)
    ImageView iv_upShop2;
    @ViewInject(R.id.iv_upShop3)
    ImageView iv_upShop3;
    @ViewInject(R.id.iv_upShop4)
    ImageView iv_upShop4;
    @ViewInject(R.id.iv_upShop5)
    ImageView iv_upShop5;
    @ViewInject(R.id.iv_upShop6)
    ImageView iv_upShop6;
    @ViewInject(R.id.iv_upShop7)
    ImageView iv_upShop7;
    @ViewInject(R.id.iv_upShop8)
    ImageView iv_upShop8;
    @ViewInject(R.id.tv_Shop_time)
    TextView tv_Shop_time;
    @ViewInject(R.id.tv_title_add)
    TextView tv_title_add;
    @ViewInject(R.id.tv_Shop_time_am1)
    TextView time_am1;
    @ViewInject(R.id.tv_Shop_time_am2)
    TextView time_am2;
    @ViewInject(R.id.tv_Shop_time_pm1)
    TextView time_pm1;
    @ViewInject(R.id.tv_Shop_time_pm2)
    TextView time_pm2;
    @ViewInject(R.id.et_ShopName_ES)
    EditText shopName_ES;
    @ViewInject(R.id.et_ShopName_EN)
    EditText shopName_EN;
    @ViewInject(R.id.et_ShopName_CN)
    EditText shopName_CN;
    @ViewInject(R.id.et_Remark_ES)
    EditText remark_ES;
    @ViewInject(R.id.et_Remark_EN)
    EditText remark_EN;
    @ViewInject(R.id.et_Remark_CN)
    EditText remark_CN;
    @ViewInject(R.id.et_Shop_ZipCode)
    EditText shop_zip;
    @ViewInject(R.id.et_Shop_Email)
    EditText email;
    @ViewInject(R.id.et_Shop_Mobile)
    EditText mobile;
    @ViewInject(R.id.et_Address_2)
    EditText address2;
    @ViewInject(R.id.et_StarTake_money)
    EditText money;
    @ViewInject(R.id.et_Freight)
    EditText freight;
    @ViewInject(R.id.et_Arrive_time)
    EditText arriveTime;
    @ViewInject(R.id.et_CIF)
    EditText CIF;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                for (int i = 0; i < shopImageList.size(); i++) {
                    shopImageList.get(i).setVisibility(View.GONE);
                }
                for (int j = 0; j < mResults.size(); j++) {
                    Glide
                            .with(AddStoreActivity.this)
                            .load(mResults.get(j))
                            .into(shopImageList.get(j));
                    shopImageList.get(j).setVisibility(View.VISIBLE);
                }
                if (mResults.size() < 9) {
                    shopImageList.get(mResults.size()).setVisibility(View.VISIBLE);
                    Glide
                            .with(AddStoreActivity.this)
                            .load(R.drawable.up_picture)
                            .into(shopImageList.get(mResults.size()));

                }
            } else if (msg.what == UP_LOGO) {
                String pic = msg.obj.toString();
                Gson g = new Gson();
                PictureData pictureData = g.fromJson(pic, PictureData.class);
                if (!pictureData.data.equals("")) {
                    logo = pictureData.data;
                } else {
                    logo = "";
                }
                if (mResults.size() > 0) {
                    String url = getResources().getString(R.string.http_msg) + "/Common/UploadShopImage";
                    funNet.uploadfile(url, mResults_logo, handler, UP_PIC);
                } else {
                    doUpload_map();
                }
            } else if (msg.what == UP_PIC) {
                String pic = msg.obj.toString();
                Gson g = new Gson();
                PictureData pictureData = g.fromJson(pic, PictureData.class);
                pictureList = pictureData.data;
                doUpload_map();
            } else if (msg.what == UP_MSG) {
                doUpload_map();
            } else if (msg.what == UP_OK) {
                String str = msg.obj.toString();
                Gson g = new Gson();
                UpShopBack back = g.fromJson(str, UpShopBack.class);
                if (back.statusCode.equals("1")) {
                    finish();
                    Toast.makeText(AddStoreActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddStoreActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        shopTypeId = intent.getStringExtra("id");
        initView();
        doSomething();
        setListener();
        doLocation();
    }

    public void initView() {
        options = new ImageOptions.Builder().setFadeIn(true).setCircular(true).build();
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_title);
        iv_back.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.my_title);
        tv_title.setText(R.string.add_title);
        tv_title_add.setText(R.string.add);
        tv_title_add.setBackgroundResource(R.drawable.shape_kuang);
        tv_title_add.setTextColor(0xffd9d7d7);
        ((TextView) findViewById(R.id.tv_ShopType)).setText(type);
    }

    public void doSomething() {
        shopImageList.add(iv_upShop0);
        shopImageList.add(iv_upShop1);
        shopImageList.add(iv_upShop2);
        shopImageList.add(iv_upShop3);
        shopImageList.add(iv_upShop4);
        shopImageList.add(iv_upShop5);
        shopImageList.add(iv_upShop6);
        shopImageList.add(iv_upShop7);
        shopImageList.add(iv_upShop8);
        shopImageList.get(0).setVisibility(View.VISIBLE);
        Glide
                .with(AddStoreActivity.this)
                .load(R.drawable.up_picture)
                .into(shopImageList.get(0));
    }

    public void setListener() {
        im_storeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoreActivity.this, ImagesSelectorActivity.class);
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        tv_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoreActivity.this, CityListActivity2.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
            }
        });
        for (int i = 0; i < shopImageList.size(); i++) {
            shopImageList.get(i).setOnClickListener(new ChooseP());
        }
        tv_Shop_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        tv_title_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReady();
            }
        });
        time_am1.setOnClickListener(new TimePicke());
        time_am2.setOnClickListener(new TimePicke());
        time_pm1.setOnClickListener(new TimePicke());
        time_pm2.setOnClickListener(new TimePicke());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    mResults_logo = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    assert mResults_logo != null;
                    x.image().bind(im_storeLogo, mResults_logo.get(0), options);
                }
            } else if (requestCode == REQUEST_CODE_PIUTURE) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                if (mResults != null) {
                    handler.sendEmptyMessage(10);
                }
            } else if (requestCode == REQUEST_CODE_CITY) {
                if (resultCode == 100) {
                    provinceId = data.getStringExtra("provinceId");
                    cityId = data.getStringExtra("cityId");
                    areaId = data.getStringExtra("areaId");
                    tv_Address.setText(data.getStringExtra("area"));
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    public void doLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //所要申请的权限
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //已获取权限
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }

        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(AddStoreActivity.this);
            normalDialog.setTitle("information message");
            normalDialog.setMessage("定位失败，请重试...");
            normalDialog.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doLocation();
                }
            });
            normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddStoreActivity.this.finish();
                }
            });
            normalDialog.create();
            normalDialog.show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            String currentLocation = "getLongitude:" + location.getLongitude() + ",\n"
                    + "getLatitude:" + location.getLatitude();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            Toast.makeText(this, currentLocation + "", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(AddStoreActivity.this);
            normalDialog.setTitle("information message");
            normalDialog.setMessage("定位失败，请到手机设置，同意定位权限...");
            normalDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddStoreActivity.this.finish();
                }
            });
            normalDialog.create();
            normalDialog.show();
        }
    }

    public void isReady() {
        Toast toast = Toast.makeText(AddStoreActivity.this, "", Toast.LENGTH_SHORT);
        if ((!shopName_CN.getText().toString().equals("")) || (!shopName_EN.getText().toString().equals("")) || (!shopName_ES.getText().toString().equals(""))) {
            if ((!remark_CN.getText().toString().equals("")) || (!remark_EN.getText().toString().equals("")) || (!remark_ES.getText().toString().equals(""))) {
                if (!shop_zip.getText().toString().equals("")) {
                    if (!email.getText().toString().equals("")) {
                        if ((!address2.getText().toString().equals("")) && (!tv_Address.getText().toString().equals(""))) {
                            if ((!tv_Shop_time.getText().toString().equals("")) && (!time_am1.getText().toString().equals("")) && (!time_am2.getText().toString().equals(""))
                                    && (!time_pm1.getText().toString().equals("")) && (!time_pm2.getText().toString().equals(""))) {
                                if (mResults_logo.size() > 0) {
                                    progressDialog();
                                    String url = getResources().getString(R.string.http_msg) + "/Common/UploadShopImage";
                                    funNet.uploadfile(url, mResults_logo, handler, UP_LOGO);
                                } else {
                                    handler.sendEmptyMessage(UP_MSG);
                                }
                            } else {
                                toast.setText("店铺营业时间不能为空");
                                toast.show();
                            }
                        } else {
                            toast.setText("店铺地址不能为空");
                            toast.show();
                        }
                    } else {
                        toast.setText("店铺邮箱不能为空");
                        toast.show();
                    }
                } else {
                    toast.setText("店铺邮编不能为空");
                    toast.show();
                }
            } else {
                toast.setText("店铺介绍不能为空");
                toast.show();
            }
        } else {
            toast.setText("店铺名不能为空");
            toast.show();
        }
    }

    public void progressDialog() {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage("正在添加商家");
        proDialog.show();
    }

    private void dialog() {
        final String items[] = {getResources().getString(R.string.MON), getResources().getString(R.string.TUE),
                getResources().getString(R.string.WED), getResources().getString(R.string.THU),
                getResources().getString(R.string.FRI), getResources().getString(R.string.SAT),
                getResources().getString(R.string.SUN)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle(R.string.Shop_time);
        builder.setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // dialog.dismiss();
//                Toast.makeText(AddStoreActivity.this, items[which] + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String str = "";
                //android会自动根据你选择的改变selected数组的值。
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        str += items[i] + ",";
                    }
                }

                tv_Shop_time.setText("" + str);
            }
        });
        builder.create().show();
    }

    public void doUpload() {
        String backURL = "";
        for (int i = 0; i < mResults.size(); i++) {
            backURL += mResults.get(i) + ",";
        }
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Shop/AddShop");
        params.addBodyParameter("Mobile", mobile.getText().toString() + "");
        params.addBodyParameter("CIF", CIF.getText().toString() + "");
        params.addBodyParameter("LogoImageUrl", logo + "");
        params.addBodyParameter("BackgroundImageUrl", backURL);
        params.addBodyParameter("StartTime1", time_am1.getText().toString() + "");
        params.addBodyParameter("StartTime2", time_pm1.getText().toString() + "");
        params.addBodyParameter("EndTime1", time_am2.getText().toString() + "");
        params.addBodyParameter("EndTime2", time_pm2.getText().toString() + "");
        params.addBodyParameter("StartTakeawayMoney", money.getText().toString() + "");
        params.addBodyParameter("Freight", freight.getText().toString() + "");
        params.addBodyParameter("DeliveryTime", arriveTime.getText().toString() + "");
        params.addBodyParameter("EmployeeId", Main2Activity.manData.Id);
        params.addBodyParameter("ProvinceId", provinceId);
        params.addBodyParameter("CityId", cityId);
        params.addBodyParameter("ST", address2.getText().toString() + "");
        params.addBodyParameter("lat", latitude + "");
        params.addBodyParameter("lng", longitude + "");
        params.addBodyParameter("ZipCode", shop_zip.getText().toString() + "");
        params.addBodyParameter("ShopName_CN", shopName_CN.getText().toString() + "");
        params.addBodyParameter("ShopName_ES", shopName_ES.getText().toString() + "");
        params.addBodyParameter("ShopName_EN", shopName_EN.getText().toString() + "");
        params.addBodyParameter("Remark_CN", remark_CN.getText().toString() + "");
        params.addBodyParameter("Remark_ES", remark_ES.getText().toString() + "");
        params.addBodyParameter("Remark_EN", remark_EN.getText().toString() + "");
        params.addBodyParameter("Email", email.getText().toString() + "");
        params.addBodyParameter("AreaId", areaId);
        params.addBodyParameter("ShopTypeId", shopTypeId);
        params.addBodyParameter("Week1", selected[0] + "");
        params.addBodyParameter("Week2", selected[1] + "");
        params.addBodyParameter("Week3", selected[2] + "");
        params.addBodyParameter("Week4", selected[3] + "");
        params.addBodyParameter("Week5", selected[4] + "");
        params.addBodyParameter("Week6", selected[5] + "");
        params.addBodyParameter("Week7", selected[6] + "");

        funNet.toolNet(params, handler, UP_OK);
    }

    public void doUpload_map() {
        String backURL = pictureList;
//        for (int i = 0; i < pictureList.size(); i++) {
//            backURL += pictureList.get(i) + ",";
//        }
//        backURL = backURL.substring(0, backURL.length() - 1);
        HashMap<String, String> map = new HashMap<>();
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Shop/AddShop");
        map.put("Mobile", mobile.getText().toString() + "");
        map.put("CIF", CIF.getText().toString() + "");
        map.put("LogoImageUrl", logo + "");
        map.put("BackgroundImageUrl", backURL);
        map.put("StartTime1", time_am1.getText().toString() + "");
        map.put("StartTime2", time_pm1.getText().toString() + "");
        map.put("EndTime1", time_am2.getText().toString() + "");
        map.put("EndTime2", time_pm2.getText().toString() + "");
        map.put("StartTakeawayMoney", money.getText().toString() + "");
        map.put("Freight", freight.getText().toString() + "");
        map.put("DeliveryTime", arriveTime.getText().toString() + "");
        map.put("EmployeeId", Main2Activity.manData.Id);
        map.put("ProvinceId", provinceId);
        map.put("CityId", cityId);
        map.put("ST", address2.getText().toString() + "");
        map.put("lat", latitude + "");
        map.put("lng", longitude + "");
        map.put("ZipCode", shop_zip.getText().toString() + "");
        map.put("ShopName_CN", shopName_CN.getText().toString() + "");
        map.put("ShopName_ES", shopName_ES.getText().toString() + "");
        map.put("ShopName_EN", shopName_EN.getText().toString() + "");
        map.put("Remark_CN", remark_CN.getText().toString() + "");
        map.put("Remark_ES", remark_ES.getText().toString() + "");
        map.put("Remark_EN", remark_EN.getText().toString() + "");
        map.put("Email", email.getText().toString() + "");
        map.put("AreaId", areaId);
        map.put("ShopTypeId", shopTypeId);
        map.put("Week1", selected[0] + "");
        map.put("Week2", selected[1] + "");
        map.put("Week3", selected[2] + "");
        map.put("Week4", selected[3] + "");
        map.put("Week5", selected[4] + "");
        map.put("Week6", selected[5] + "");
        map.put("Week7", selected[6] + "");
        map.put("isClient", "yes");
        Iterator iter = map.entrySet().iterator();
        String url = getResources().getString(R.string.http_msg) + "/Shop/AddShop?";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            url += key.toString() + "=" + val.toString() + "&";
        }
        url = url.substring(0, url.length() - 1);
        RequestParams params2 = new RequestParams(url);
        funNet.toolNetGet(params2, handler, UP_OK);
    }

    class ChooseP implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddStoreActivity.this, ImagesSelectorActivity.class);
            intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
            intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
            intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
            intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
            startActivityForResult(intent, REQUEST_CODE_PIUTURE);
        }
    }

    class TimePicke implements View.OnClickListener {
        TextView tv;
        TimePicker timePicke;
        View view;
        AlertDialog.Builder dialog1;

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            view = getLayoutInflater().inflate(R.layout.layout_time_picker, null);
            timePicke = (TimePicker) view.findViewById(R.id.timePicker);
            timePicke.setIs24HourView(true);
            dialog1 = new AlertDialog.Builder(AddStoreActivity.this);
            tv = (TextView) v;
            dialog1.setView(view);
            dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int hour = 0;
                    int min = 0;
                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        hour = timePicke.getHour();
                        min = timePicke.getMinute();
                    } else {
                        hour = timePicke.getCurrentHour();
                        min = timePicke.getCurrentMinute();
                    }
                    tv.setText(hour + ":" + min);
                }
            });
            dialog1.setNegativeButton("cancal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog1.create();
            dialog1.show();
        }
    }
}
