package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supportv1.utils.JsonUtil;
import com.gxuc.runfast.shop.activity.usercenter.BindMobileActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;

    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    private boolean isRelogin;
    private String phone;
    private String password;
    private String uid;
    private String thirdLoginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isRelogin = getIntent().getBooleanExtra("isRelogin", false);
        initView();
    }

    private void initView() {
//        User userInfo = UserService.getUserInfo(this);
//        if (userInfo != null) {
        etUserName.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.MOBILE));
        etUserPassword.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.PASSWORD));
//        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.tv_forgot_password, R.id.iv_weixin_login, R.id.iv_qq_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1001);
                break;
            case R.id.tv_forgot_password:
                startActivityForResult(new Intent(this, ForgotPasswordActivity.class), 1002);
                break;

            case R.id.iv_weixin_login:
                ToastUtil.showToast("微信登录稍后开通");
//                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
//                SendAuth.Req req = new Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "wechat_sdk_demo_test";
//                CustomApplication.getIWXAPI().sendReq(req);
                break;

            case R.id.iv_qq_login:
//                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
                break;
        }
    }

    /**
     * 账号登陆
     */
    private void login() {
        phone = etUserName.getText().toString().trim();
        password = etUserPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("请输入密码");
            return;
        }
        LogUtil.d("password", MD5Util.MD5(password));

        CustomApplication.getRetrofitNew().login(phone, MD5Util.MD5(password), CustomApplication.alias, "mobile_pwd").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast(jsonObject.optString("msg"));
                        JSONObject object = jsonObject.optJSONObject("data");
                        UserInfo userInfo = JsonUtil.fromJson(object.optString("user"), UserInfo.class);
                        UserService.saveUserInfo(userInfo);
                        String token = object.optString("token");
                        SharePreferenceUtil.getInstance().putStringValue(CustomConstant.MOBILE, phone);
                        SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, password);
                        SharePreferenceUtil.getInstance().putStringValue("token", token);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });

//        CustomApplication.getRetrofit().postLogin(phone, MD5Util.MD5(password), CustomApplication.alias, 0).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                dealLogin(response.body());
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

//    private void dealLogin(String body) {
//        try {
//            JSONObject object = new JSONObject(body);
//            boolean success = object.optBoolean("success");
//            String msg = object.optString("msg");
//            ToastUtil.showToast(msg);
//            CustomApplication.isRelogining = false;
//            if (!success) {
//                return;
//            }
////            CustomApplication.isRelogining = false;
//            SharePreferenceUtil.getInstance().putStringValue(CustomConstant.MOBILE, phone);
//            SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, password);
//            JSONObject app_cuser = object.getJSONObject("app_cuser");
//            User user = GsonUtil.parseJsonWithGson(app_cuser.toString(), User.class);
//            user.setPassword(etUserPassword.getText().toString().trim());
//            UserService.saveUserInfo(user);
//            UserService.setAutoLogin("1");
//            if (!isRelogin) {
//                startActivity(new Intent(this, MainActivity.class));
//            }
//            finish();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1001) {
            String mobile = data.getStringExtra("mobile");
            String password = data.getStringExtra("password");
            etUserName.setText(mobile);
            etUserPassword.setText(password);
        } else if (requestCode == 1002) {
            etUserPassword.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.PASSWORD));
        }
    }

//    UMAuthListener authListener = new UMAuthListener() {
//        /**
//         * @desc 授权开始的回调
//         * @param platform 平台名称
//         */
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//
//        }
//
//        /**
//         * @desc 授权成功的回调
//         * @param platform 平台名称
//         * @param action 行为序号，开发者用不上
//         * @param data 用户资料返回
//         */
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            if (platform.compareTo(SHARE_MEDIA.QQ) == 0) {
//                thirdLoginType = "QQ";
//            } else if (platform.compareTo(SHARE_MEDIA.WEIXIN) == 0) {
//                thirdLoginType = "WEIXIN";
//            }
//            ToastUtil.showToast("授权成功");
//            uid = data.get("uid");
//            String openid = data.get("openid");//微博没有
//            String unionid = data.get("unionid");//微博没有
//            String access_token = data.get("access_token");
//            String refresh_token = data.get("refresh_token");//微信,qq,微博都没有获取到
//            String expires_in = data.get("expires_in");
//            String name = data.get("name");
//            String gender = data.get("gender");
//            String iconurl = data.get("iconurl");
//            Log.d("wdevon", "uid=" + uid +
//                    ",openid=" + openid +
//                    ",access_token=" + access_token +
//                    ",refresh_token=" + refresh_token +
//                    ",expires_in=" + expires_in +
//                    ",name=" + name +
//                    ",gender=" + gender +
//                    ",iconurl=" + iconurl);
//
//            requestThirdLogin(uid, thirdLoginType);
//        }
//
//        /**
//         * @desc 授权失败的回调
//         * @param platform 平台名称
//         * @param action 行为序号，开发者用不上
//         * @param t 错误原因
//         */
//        @Override
//        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//
//            ToastUtil.showToast("失败：" + t.getMessage());
//
//        }
//
//        /**
//         * @desc 授权取消的回调
//         * @param platform 平台名称
//         * @param action 行为序号，开发者用不上
//         */
//        @Override
//        public void onCancel(SHARE_MEDIA platform, int action) {
//            ToastUtil.showToast("授权取消");
//        }
//    };

//    private void requestThirdLogin(String uid, String thirdLoginType) {
//        CustomApplication.getRetrofit().postThirdLogin(uid, thirdLoginType, CustomApplication.alias, "").enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful()) {
//                    ToastUtil.showToast("网络数据异常");
//                    return;
//                }
//                dealThirdLogin(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

//    private void dealThirdLogin(String body) {
//        try {
//            JSONObject jsonObject = new JSONObject(body);
//            if (jsonObject.optBoolean("success")) {
//                ToastUtil.showToast(jsonObject.optString("msg"));
//                CustomApplication.isRelogining = false;
//                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_ID, uid);
//                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_TYPR, thirdLoginType);
//                JSONObject app_cuser = jsonObject.getJSONObject("app_cuser");
//                UserInfo user = GsonUtil.parseJsonWithGson(app_cuser.toString(), UserInfo.class);
//                UserService.saveUserInfo(user);
//                UserService.setAutoLogin("1");
//                if (!isRelogin) {
//                    startActivity(new Intent(this, MainActivity.class));
//                }
//                finish();
//            } else {
//                Intent intent = new Intent(this, BindMobileActivity.class);
//                intent.putExtra("thirdLoginId", uid);
//                intent.putExtra("thirdLoginType", thirdLoginType);
//                startActivity(intent);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


}
