package com.wzj.work.salesmanapp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wzj.work.salesmanapp.MyView.MyImage;
import com.wzj.work.salesmanapp.OtherClass.Food;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.OtherClass.PictureData;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_add_dishes)
public class AddDishesActivity extends AutoLayoutActivity {
    private PopupWindow mPopupWindow;
    private ScrollView sv_add;
    private ImageView iv_title;
    private ArrayList<String> mResults = new ArrayList<>();
    private final static int REQUEST_CODE_FOOD = 600;
    private final static int GIT_PIC = 601;
    private final static int UPLOAD_PIC = 602;
    private final static int UPLOAD_MSG = 603;
    private ArrayList<ImageView> foodImageView = new ArrayList<>();
    private ArrayList<MyImage> myFoods = new ArrayList<>();
    private String picUrl;
    private int foodTypeId = 1;
    private ProgressDialog proDialog;
    private String state;
    private String originalId = "";
    ArrayList<String> foodId = new ArrayList<>();
    int[] pic1 = {R.mipmap.food1, R.mipmap.food2, R.mipmap.food3, R.mipmap.food4, R.mipmap.food5,
            R.mipmap.food6, R.mipmap.food7, R.mipmap.food8, R.mipmap.food9};
    int[] pic2 = {R.mipmap.food11, R.mipmap.food22, R.mipmap.food33, R.mipmap.food44, R.mipmap.food55,
            R.mipmap.food66, R.mipmap.food77, R.mipmap.food88, R.mipmap.food99};
    String store;
    Food food;
    @ViewInject(R.id.iv_upFood0)
    ImageView iv_upFood0;
    @ViewInject(R.id.iv_upFood1)
    ImageView iv_upFood1;
    @ViewInject(R.id.iv_upFood2)
    ImageView iv_upFood2;
    @ViewInject(R.id.iv_upFood3)
    ImageView iv_upFood3;
    @ViewInject(R.id.iv_upFood4)
    ImageView iv_upFood4;
    @ViewInject(R.id.iv_upFood5)
    ImageView iv_upFood5;
    @ViewInject(R.id.iv_upFood6)
    ImageView iv_upFood6;
    @ViewInject(R.id.iv_upFood7)
    ImageView iv_upFood7;
    @ViewInject(R.id.iv_upFood8)
    ImageView iv_upFood8;
    @ViewInject(R.id.iv_food1)
    MyImage food1;
    @ViewInject(R.id.iv_food2)
    MyImage food2;
    @ViewInject(R.id.iv_food3)
    MyImage food3;
    @ViewInject(R.id.iv_food4)
    MyImage food4;
    @ViewInject(R.id.iv_food5)
    MyImage food5;
    @ViewInject(R.id.iv_food6)
    MyImage food6;
    @ViewInject(R.id.iv_food7)
    MyImage food7;
    @ViewInject(R.id.iv_food8)
    MyImage food8;
    @ViewInject(R.id.iv_food9)
    MyImage food9;
    @ViewInject(R.id.et_dishes_num)
    EditText et_num;
    @ViewInject(R.id.et_dishes_price)
    EditText et_price;
    @ViewInject(R.id.et_dishes_discountPrice)
    EditText et_discountPrice;
    @ViewInject(R.id.et_dishes_nameES)
    EditText et_titleES;
    @ViewInject(R.id.et_dishes_nameEN)
    EditText et_titleEN;
    @ViewInject(R.id.et_dishes_nameCN)
    EditText et_titleCN;
    @ViewInject(R.id.et_dishes_introduceES)
    EditText et_introES;
    @ViewInject(R.id.et_dishes_introduceEN)
    EditText et_introEN;
    @ViewInject(R.id.et_dishes_introduceCN)
    EditText et_introCN;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GIT_PIC) {
                for (int i = 0; i < foodImageView.size(); i++) {
                    foodImageView.get(i).setVisibility(View.GONE);
                }
                for (int j = 0; j < mResults.size(); j++) {
                    Glide
                            .with(AddDishesActivity.this)
                            .load(mResults.get(j))
                            .into(foodImageView.get(j));
                    foodImageView.get(j).setVisibility(View.VISIBLE);
                }
                if (mResults.size() < 9) {
                    foodImageView.get(mResults.size()).setVisibility(View.VISIBLE);
                    Glide
                            .with(AddDishesActivity.this)
                            .load(R.drawable.up_picture)
                            .into(foodImageView.get(mResults.size()));
                }
            } else if (msg.what == UPLOAD_PIC) {
                String pic = msg.obj.toString();
                if (funNet.isOk(pic, AddDishesActivity.this)) {
                    Gson g = new Gson();
                    PictureData picData = g.fromJson(pic, PictureData.class);
                    if (!picData.data.equals("")) {
                        picUrl = picData.data;
                        upLoad();
                    } else {
                        proDialog.dismiss();
                    }
                }
            } else if (msg.what == UPLOAD_MSG) {
                proDialog.dismiss();
                String isOk = msg.obj.toString();
                if (funNet.isOk(isOk, AddDishesActivity.this)) {
                    finish();
                    Toast.makeText(AddDishesActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddDishesActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 100) {
                Toast.makeText(AddDishesActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        doSomething();
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        if (state.equals("0")) {
            Bundle bundle = intent.getExtras();
            store = ((MyShopList) bundle.getSerializable("store")).Id;
            foodImageView.get(0).setVisibility(View.VISIBLE);
            Glide
                    .with(AddDishesActivity.this)
                    .load(R.drawable.up_picture)
                    .into(foodImageView.get(0));
        } else if (state.equals("1")) {
            Bundle bundle = intent.getExtras();
            food = (Food) bundle.getSerializable("food");
            store = food.ShopId;
            setView();
        }
    }

    public void initView() {
        sv_add = (ScrollView) findViewById(R.id.sv_add);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_title);
        iv_back.setBackgroundResource(R.drawable.back);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        iv_title.setBackgroundResource(R.drawable.arrow_up);
        TextView tv_title = (TextView) findViewById(R.id.my_title);
        tv_title.setText(R.string.add_dishes);
        TextView tv_title_add = (TextView) findViewById(R.id.tv_title_add);
        tv_title_add.setText(R.string.add);
        tv_title_add.setBackgroundResource(R.drawable.shape_kuang);
        tv_title_add.setTextColor(0xffd9d7d7);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(AddDishesActivity.this, 0.5f);
                mPopupWindow.showAsDropDown(v);
            }
        });
        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReady();
            }
        });
        setPopupWindow();
    }

    public void doSomething() {
        foodImageView.add(iv_upFood0);
        foodImageView.add(iv_upFood1);
        foodImageView.add(iv_upFood2);
        foodImageView.add(iv_upFood3);
        foodImageView.add(iv_upFood4);
        foodImageView.add(iv_upFood5);
        foodImageView.add(iv_upFood6);
        foodImageView.add(iv_upFood7);
        foodImageView.add(iv_upFood8);
        for (int i = 0; i < foodImageView.size(); i++) {
            foodImageView.get(i).setOnClickListener(new ChooseP());
        }

        myFoods.add(food1);
        myFoods.add(food2);
        myFoods.add(food3);
        myFoods.add(food4);
        myFoods.add(food5);
        myFoods.add(food6);
        myFoods.add(food7);
        myFoods.add(food8);
        myFoods.add(food9);
        for (int i = 0; i < myFoods.size(); i++) {
            myFoods.get(i).index = i + 1;
            myFoods.get(i).setOnClickListener(new FoodChoose());
        }

    }

    public void setView() {
        String str = food.IngredientsIconArray;
        String[] foodArray = null;
        if (!str.equals("null")) {
            foodArray = str.split(",");
            for (int i = 0; i < foodArray.length; i++) {
                if (!foodArray[i].equals("null") && !foodArray[i].equals("0")) {
                    try {
                        int t = Integer.parseInt(foodArray[i]);
                        MyImage myImage = myFoods.get(t - 1);
                        foodId.add(t + "");
                        myImage.isChoose = true;
                        myImage.setBackgroundResource(pic2[myImage.index - 1]);
                    } catch (Exception e) {

                    }
                }
            }
        }
        originalId = food.Id;
        et_num.setText(food.Id);
        et_price.setText(food.Price);
        et_discountPrice.setText(food.Discountprice);
        et_titleES.setText(food.Title_ES);
        et_titleEN.setText(food.Title_EN);
        et_titleCN.setText(food.Title_CN);
        et_introES.setText(food.Introduce_ES);
        et_introEN.setText(food.Introduce_EN);
        et_introCN.setText(food.Introduce_CN);
        String imageUrl = food.ImageUrl;
        if (imageUrl != null) {
            imageUrl = (imageUrl.split(","))[0];
            foodImageView.get(0).setVisibility(View.VISIBLE);
            foodImageView.get(1).setVisibility(View.VISIBLE);
            x.image().bind(foodImageView.get(0), getResources().getString(R.string.http_tu) + imageUrl);
            Glide
                    .with(AddDishesActivity.this)
                    .load(R.drawable.up_picture)
                    .into(foodImageView.get(1));
            picUrl = food.ImageUrl;
        }
    }

    TextView tvlast;

    public void setPopupWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        popupView.findViewById(R.id.tv_dish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (tvlast != null) {
                    tvlast.setTextAppearance(getApplicationContext(), R.style.black30);
                }
                tvlast = textView;
                textView.setTextAppearance(getApplicationContext(), R.style.blue30);
                foodTypeId = 1;
            }
        });
        popupView.findViewById(R.id.tv_sweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (tvlast != null) {
                    tvlast.setTextAppearance(getApplicationContext(), R.style.black30);
                }
                tvlast = textView;
                textView.setTextAppearance(getApplicationContext(), R.style.blue30);
                foodTypeId = 4;
            }
        });
        popupView.findViewById(R.id.tv_drank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (tvlast != null) {
                    tvlast.setTextAppearance(getApplicationContext(), R.style.black30);
                }
                tvlast = textView;
                textView.setTextAppearance(getApplicationContext(), R.style.blue30);
                foodTypeId = 5;
            }
        });
        mPopupWindow = new PopupWindow(popupView, AutoFrameLayout.LayoutParams.MATCH_PARENT, AutoFrameLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(AddDishesActivity.this, 1f);
            }
        });
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        if (bgAlpha == 1f) {
            sv_add.setAlpha(bgAlpha);
            sv_add.setBackgroundColor(0xffffff);
            iv_title.setBackgroundResource(R.drawable.arrow_up);
        } else {
            sv_add.setAlpha(bgAlpha);
            sv_add.setBackgroundColor(0xffe2e2e2);
            iv_title.setBackgroundResource(R.drawable.arrow_down);
        }
    }

    public void isReady() {
        if ((!et_num.getText().toString().equals("")) && (!et_price.getText().toString().equals(""))
                && (!et_discountPrice.getText().toString().equals("")) && (!et_titleES.getText().toString().equals(""))
                && (!et_titleEN.getText().toString().equals("")) && (!et_titleCN.getText().toString().equals(""))
                && (foodId.size() > 0) && (!et_introES.getText().toString().equals("")) && (!et_introEN.getText().toString().equals(""))
                && (!et_introCN.getText().toString().equals(""))) {
            progressDialog();
            if (mResults.size() > 0) {
                String url = getResources().getString(R.string.http_msg) + "/Common/UploadFoodImage";
                funNet.uploadfile(url, mResults, handler, UPLOAD_PIC);
            } else if (picUrl != null) {
                upLoad();
            } else {
                Toast.makeText(AddDishesActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(AddDishesActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void upLoad() {
        String foodIds = "";
        for (int i = 0; i < foodId.size(); i++) {
            foodIds += foodId.get(i) + ",";
        }
        if (!foodIds.equals("")) {
            foodIds = foodIds.substring(0, foodIds.length() - 1);
        }
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg) + "/Food/UpdateFood");
        params.addBodyParameter("ShopId", store);
        params.addBodyParameter("Id", et_num.getText().toString() + "");
        params.addBodyParameter("Title_CN", et_titleCN.getText().toString() + "");
        params.addBodyParameter("Title_ES", et_introES.getText().toString() + "");
        params.addBodyParameter("Title_EN", et_titleEN.getText().toString() + "");
        params.addBodyParameter("Introduce_CN", et_introCN.getText().toString() + "");
        params.addBodyParameter("Introduce_EN", et_introEN.getText().toString() + "");
        params.addBodyParameter("Introduce_ES", et_introES.getText().toString() + "");
        params.addBodyParameter("Price", et_price.getText().toString() + "");
        params.addBodyParameter("Discountprice", et_discountPrice.getText().toString() + "");
        params.addBodyParameter("IngredientsIconArray", foodIds);
        params.addBodyParameter("FoodTypeId", foodTypeId + "");
        params.addBodyParameter("ImageUrl", picUrl);
        params.addBodyParameter("State", state);
        params.addBodyParameter("OriginalId", originalId);
        funNet.toolNet(params, handler, UPLOAD_MSG);
    }

    public void progressDialog() {
        proDialog = new ProgressDialog(this);
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        proDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("");
        proDialog.setMessage("正在添加菜品");
        proDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_CODE_FOOD) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                if (mResults != null) {
                    handler.sendEmptyMessage(GIT_PIC);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    class FoodChoose implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MyImage myImage = (MyImage) v;
            if (myImage.isChoose) {
                myImage.isChoose = false;
                myImage.setBackgroundResource(pic1[myImage.index - 1]);
                foodId.remove(myImage.index + "");
            } else {
                foodId.add(myImage.index + "");
                myImage.isChoose = true;
                myImage.setBackgroundResource(pic2[myImage.index - 1]);
            }
        }
    }

    class ChooseP implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddDishesActivity.this, ImagesSelectorActivity.class);
            intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
            intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
            intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
            intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
            startActivityForResult(intent, REQUEST_CODE_FOOD);
        }
    }
}
