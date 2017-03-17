package com.wzj.work.salesmanapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzj.work.salesmanapp.OtherClass.Login;
import com.wzj.work.salesmanapp.OtherClass.ManData;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.MD5;
import com.wzj.work.salesmanapp.Tools.funNet;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.http.RequestParams;

public class MainActivity extends AutoLayoutActivity {
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    private boolean flag = false;
    private EditText et_loginNum;
    private EditText et_logiName;
    private ProgressBar pb_ok;
    private TextView tv_ok;
    private String str = null;
    private Login login;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                str = msg.obj.toString();
                Gson g = new Gson();
                login = g.fromJson(str, Login.class);
                if (login != null) {
                    if (login.statusCode.equals("1")) {
                        editor.putBoolean("isLogin", true);
                        editor.putString("id", login.data.get(0).Id);
                        editor.putString("name", login.data.get(0).Name);
                        editor.putString("mobile", login.data.get(0).Mobile);
                        editor.commit();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ManData", login.data.get(0));
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        pb_ok.setVisibility(View.GONE);
                        tv_ok.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (msg.what == 100) {
                pb_ok.setVisibility(View.GONE);
                tv_ok.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pre = getSharedPreferences("loginMsg", MODE_PRIVATE);
        editor = pre.edit();
        if (pre.getBoolean("isLogin", false)) {
            ManData man = new ManData();
            man.Id = pre.getString("id", "");
            man.Name = pre.getString("name", "");
            man.Mobile = pre.getString("mobile", "");
            Bundle bundle = new Bundle();
            bundle.putSerializable("ManData", man);
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            initView();
        }
//        test();
    }

    public void initView() {
        TextView tv = (TextView) findViewById(R.id.my_title);
        tv.setText(R.string.login);
        et_loginNum = (EditText) findViewById(R.id.et_login_num);
        et_logiName = (EditText) findViewById(R.id.et_login_name);
        pb_ok = (ProgressBar) findViewById(R.id.pb_ok);
        TextView tv_is = (TextView) findViewById(R.id.tv_is);
        tv_is.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    et_loginNum.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    v.setBackgroundResource(R.drawable.no_look);
                    flag = false;
                } else {
                    et_loginNum.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    v.setBackgroundResource(R.drawable.yes_look);
                    flag = true;
                }
            }
        });
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_logiName.getText().toString().equals("")) {
                    if (!et_loginNum.getText().toString().equals("")) {
                        v.setVisibility(View.GONE);
                        pb_ok.setVisibility(View.VISIBLE);
                        login();
                    }else {
                        Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login() {
        RequestParams params = new RequestParams(getResources().getString(R.string.http_msg)+"/Saleman/Login");
//        params.addQueryStringParameter("Name", "小浣熊业务员");
//        params.addQueryStringParameter("PassWord", MD5.GetMD5Code("123456"));
        Log.e( "login: ",MD5.GetMD5Code(et_loginNum.getText().toString()));
        Log.e( "login: ",et_logiName.getText().toString());
        params.addQueryStringParameter("Name", et_logiName.getText().toString());
        params.addQueryStringParameter("PassWord", MD5.GetMD5Code(et_loginNum.getText().toString()));
        funNet.toolNet(params, handler, 1);
    }
}
