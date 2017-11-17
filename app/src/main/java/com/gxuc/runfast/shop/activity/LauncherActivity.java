package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        userInfo = UserService.getUserInfo(this);
//        if (userInfo != null) {
//            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//            finish();
//        }
    }

    private void initData() {
//        if (UserService.isAutoLogin()) {
            handler.sendEmptyMessageDelayed(2001, 2000);
//        } else {
////            handler.sendEmptyMessageDelayed(2002, 2000);
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2001:
                    login();
                    break;
                case 2002:
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    finish();
                    break;
            }
        }
    };

    private void login() {
        userInfo = UserService.getUserInfo(this);
        if (userInfo != null) {
            CustomApplication.getRetrofit().postLogin(userInfo.getMobile(), MD5Util.MD5(userInfo.getPassword()), 0).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealLogin(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {
//                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else {
//            startActivity(new Intent(this, LoginActivity.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void dealLogin(String body) {
        try {
            JSONObject object = new JSONObject(body);
            boolean success = object.optBoolean("success");
            String msg = object.optString("msg");
            CustomApplication.isRelogining = false;
            if (!success) {
                CustomToast.INSTANCE.showToast(this, msg);
//                startActivity(new Intent(this, LoginActivity.class));
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
//                CustomApplication.isRelogining = false;
                JSONObject app_cuser = object.getJSONObject("app_cuser");
                User user = GsonUtil.parseJsonWithGson(app_cuser.toString(), User.class);
                user.setPassword(userInfo.getPassword());
                UserService.saveUserInfo(user);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
