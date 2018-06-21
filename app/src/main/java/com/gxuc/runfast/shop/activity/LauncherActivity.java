package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.supportv1.utils.JsonUtil;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

    private User userInfo;
    private String mobile;
    private String password;
    private String thirdLoginId;
    private String thirdLoginType;

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

        thirdLoginId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.THIRD_LOGIN_ID);
        thirdLoginType = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.THIRD_LOGIN_TYPR);
//        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(password)) {
//            handler.sendEmptyMessage(2001);
        handler.sendEmptyMessageDelayed(2003, 3000);
//        } else if (!TextUtils.isEmpty(thirdLoginId)) {
//            handler.sendEmptyMessage(2002);
//            handler.sendEmptyMessageDelayed(2003,3000);
//        } else {
//            handler.sendEmptyMessageDelayed(2003,2000);
//        }


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
                    thirdLogin();
                    break;
                case 2003:
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    private void thirdLogin() {
        CustomApplication.getRetrofitNew().postThirdLogin(thirdLoginId, thirdLoginType, CustomApplication.alias, "third").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }
                dealThirdLogin(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//                finish();
            }
        });
    }

    private void dealThirdLogin(String body) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
//            CustomApplication.isRelogining = false;

                JSONObject object = jsonObject.optJSONObject("data");
                UserInfo userInfo = JsonUtil.fromJson(object.optString("user"), UserInfo.class);
                UserService.saveUserInfo(userInfo);
                String token = object.optString("token");
                SharePreferenceUtil.getInstance().putStringValue("token", token);
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_ID, thirdLoginId);
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_TYPR, thirdLoginType);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        CustomApplication.getRetrofitNew().login(mobile, MD5Util.MD5(password), CustomApplication.alias, "mobile_pwd").enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }
                dealLogin(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//                finish();
            }

        });

    }

    private void dealLogin(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast(jsonObject.optString("msg"));
                JSONObject object = jsonObject.optJSONObject("data");
                UserInfo userInfo = JsonUtil.fromJson(object.optString("user"), UserInfo.class);
                UserService.saveUserInfo(userInfo);
                String token = object.optString("token");
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.MOBILE, mobile);
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, password);
                SharePreferenceUtil.getInstance().putStringValue("token", token);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
