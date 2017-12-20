package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

    private User userInfo;
    private String mobile;
    private String password;

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
        mobile = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.MOBILE);
        password = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.PASSWORD);
        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(password)) {
            handler.sendEmptyMessageDelayed(2001, 2000);
        } else {
            handler.sendEmptyMessageDelayed(2002, 2000);
        }
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
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    private void login() {
        CustomApplication.getRetrofit().postLogin(mobile, MD5Util.MD5(password), CustomApplication.alias, 0).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    CustomToast.INSTANCE.showToast(CustomApplication.getContext(), "网络数据异常");
                    return;
                }
                dealLogin(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            }

        });

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
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.MOBILE, mobile);
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, password);

                JSONObject app_cuser = object.getJSONObject("app_cuser");
                User user = GsonUtil.parseJsonWithGson(app_cuser.toString(), User.class);
                user.setPassword(password);
                UserService.saveUserInfo(user);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
