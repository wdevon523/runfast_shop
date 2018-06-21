package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.example.supportv1.utils.ValidateUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.BindMobileActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.UserCaptchaTask;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaListener;
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

/**
 * Created by Devon on 2018/3/27.
 */

public class LoginQucikActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_code)
    EditText etUserCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    private boolean isRelogin;
    private String phone;
    private String code;
    private String uid;
    private String thirdLoginType;
    /*验证码SDK,该Demo采用异步获取方式*/
    private UserCaptchaTask mUserCaptchaTask = null;
    private Captcha mCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_quick);
        ButterKnife.bind(this);
        setRightMsg("快速注册");
        isRelogin = getIntent().getBooleanExtra("isRelogin", false);
        initView();
        initCaptcha();
    }

    private void initView() {
        etUserName.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.MOBILE));
//        etUserCode.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.PASSWORD));
    }

    private void initCaptcha() {
        //初始化验证码SDK相关参数，设置CaptchaId、Listener最后调用start初始化。
        if (mCaptcha == null) {
            mCaptcha = new Captcha(this);
        }
        mCaptcha.setCaptchaId("2a05ddcc43e648fd9ad48a08de7dcb11");
        mCaptcha.setCaListener(myCaptchaListener);
        //可选：开启debug
        mCaptcha.setDebug(false);
        //可选：设置超时时间
        mCaptcha.setTimeout(10000);
    }

    CaptchaListener myCaptchaListener = new CaptchaListener() {

        @Override
        public void onValidate(String result, String validate, String message) {
            //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
            if (validate.length() > 0) {
//                ToastUtil.showToast("验证成功，validate = " + validate);
//                tvGetCode.setEnabled(false);
                getAuthCode(validate);
            } else {
//                ToastUtil.showToast("验证失败：result = " + result + ", validate = " + validate + ", message = " + message);
                ToastUtil.showToast("验证失败");
            }
        }

        @Override
        public void closeWindow() {
            //请求关闭页面
//            ToastUtil.showToast("关闭页面");
        }

        @Override
        public void onError(String errormsg) {
            //出错
            ToastUtil.showToast("错误信息：" + errormsg);
        }

        @Override
        public void onCancel() {
//            ToastUtil.showToast("取消线程");
            //用户取消加载或者用户取消验证，关闭异步任务，也可根据情况在其他地方添加关闭异步任务接口
            if (mUserCaptchaTask != null) {
                if (mUserCaptchaTask.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.i(TAG, "stop mUserCaptchaTask");
                    mUserCaptchaTask.cancel(true);
                }
            }
        }

        @Override
        public void onReady(boolean ret) {
            //该为调试接口，ret为true表示加载Sdk完成
            if (ret) {
//                ToastUtil.showToast("验证码sdk加载成功");
            }
        }

    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1002:
                    int second = msg.arg1;
                    tvGetCode.setText("重新获取" + second);
                    second -= 1;
                    if (second < 1) {
                        tvGetCode.setText("获取验证码");
                        //tvGetCode.setBackgroundResource(R.drawable.shape_button_orange);
                        tvGetCode.setEnabled(true);
                        return;
                    } else {
                        tvGetCode.setEnabled(false);
                    }
                    Message secondMsg = obtainMessage();
                    secondMsg.what = 1002;
                    secondMsg.arg1 = second;
                    sendMessageDelayed(secondMsg, 1000);
                    break;
            }
        }
    };

    @OnClick({R.id.tv_right_title, R.id.btn_login, R.id.btn_register, R.id.tv_get_code, R.id.iv_weixin_login, R.id.iv_qq_login, R.id.iv_password_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1001);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1001);
                break;
            case R.id.tv_get_code:
//                startActivityForResult(new Intent(this, ForgotPasswordActivity.class), 1002);
                mCaptcha.start();
                mUserCaptchaTask = new UserCaptchaTask(mCaptcha);
                //关闭mUserCaptchaTask任务可以放在myCaptchaListener的onCancel接口中处理
                mUserCaptchaTask.execute();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
                //mCaptcha.Validate();
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
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
                break;
            case R.id.iv_password_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void getAuthCode(String validate) {

        String accountName = etUserName.getText().toString().trim();
        // 手机号正则验证
        //|| !ValidateUtil.isMobileNo(accountName)
        if (TextUtils.isEmpty(accountName) ||
                accountName.length() != 11) {
            ToastUtil.showToast(getString(R.string.please_input_correct_phone));
//            tvGetCode.setEnabled(true);
            return;
        }

        CustomApplication.getRetrofitNew().sendSms(accountName, "sms_login", validate).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                dealCode(body);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                tvGetCode.setEnabled(true);
            }
        });
    }

    private void dealCode(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast(jsonObject.optString("msg"));
                Message message = handler.obtainMessage();
                message.what = 1002;
                message.arg1 = 59;
                message.sendToTarget();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
                tvGetCode.setText("获取验证码");
                //tvGetCode.setBackgroundResource(R.drawable.shape_button_orange);
                tvGetCode.setEnabled(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 账号注册
     */
    private void login() {
        phone = etUserName.getText().toString().trim();
        code = etUserCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast("请输入验证码");
            return;
        }

        CustomApplication.getRetrofitNew().postLogiQuick(phone, code, CustomApplication.alias, "mobile_sms_code").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealLogin(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

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
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.MOBILE, phone);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1001) {
            String mobile = data.getStringExtra("mobile");
//            String password = data.getStringExtra("password");
            etUserName.setText(mobile);
//            etUserCode.setText(password);
        } else if (requestCode == 1002) {
            etUserCode.setText(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.PASSWORD));
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (platform.compareTo(SHARE_MEDIA.QQ) == 0) {
                thirdLoginType = "QQ";
            } else if (platform.compareTo(SHARE_MEDIA.WEIXIN) == 0) {
                thirdLoginType = "WEIXIN";
            }
            ToastUtil.showToast("授权成功");
            uid = data.get("uid");
            String openid = data.get("openid");//微博没有
            String unionid = data.get("unionid");//微博没有
            String access_token = data.get("access_token");
            String refresh_token = data.get("refresh_token");//微信,qq,微博都没有获取到
            String expires_in = data.get("expires_in");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            Log.d("wdevon", "uid=" + uid +
                    ",openid=" + openid +
                    ",access_token=" + access_token +
                    ",refresh_token=" + refresh_token +
                    ",expires_in=" + expires_in +
                    ",name=" + name +
                    ",gender=" + gender +
                    ",iconurl=" + iconurl);

            requestThirdLogin(uid, thirdLoginType);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            ToastUtil.showToast("失败：" + t.getMessage());

        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtil.showToast("授权取消");
        }
    };

    private void requestThirdLogin(String uid, String thirdLoginType) {
        CustomApplication.getRetrofitNew().postThirdLogin(uid, thirdLoginType, CustomApplication.alias, "third").enqueue(new Callback<String>() {
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

            }
        });
    }

    private void dealThirdLogin(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast(jsonObject.optString("msg"));

                JSONObject object = jsonObject.optJSONObject("data");
                UserInfo userInfo = JsonUtil.fromJson(object.optString("user"), UserInfo.class);
                UserService.saveUserInfo(userInfo);
                String token = object.optString("token");

                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_ID, uid);
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.THIRD_LOGIN_TYPR, thirdLoginType);
                SharePreferenceUtil.getInstance().putStringValue("token", token);
//                user.setPassword(user.getPassword());
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Intent intent = new Intent(this, BindMobileActivity.class);
                intent.putExtra("thirdLoginId", uid);
                intent.putExtra("thirdLoginType", thirdLoginType);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
