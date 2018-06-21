package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.UserCaptchaTask;
import com.gxuc.runfast.shop.util.MD5Util;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.R;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.et_user_password_again)
    EditText etUserPasswordAgain;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvGetCode;
    @BindView(R.id.btn_register)
    Button btnRegister;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private Captcha mCaptcha;
    /*验证码SDK,该Demo采用异步获取方式*/
    private UserCaptchaTask mUserCaptchaTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initMap();
        initCaptcha();
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

    private void initMap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (aMapLocation.getLatitude() != 0.0 && aMapLocation.getLongitude() != 0.0) {
//                        netPostAddress(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                        if (mLocationClient != null) {
                            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        }
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @OnClick({R.id.tv_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
//                tvGetCode.setEnabled(false);
//                getAuthCode();
                mCaptcha.start();
                mUserCaptchaTask = new UserCaptchaTask(mCaptcha);
                //关闭mUserCaptchaTask任务可以放在myCaptchaListener的onCancel接口中处理
                mUserCaptchaTask.execute();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
                //mCaptcha.Validate();
                break;
            case R.id.btn_register:
                registerUser();
                break;
        }
    }

    /***
     * 获取验证码
     * @param validate
     */
    private void getAuthCode(String validate) {
        String accountName = etUserName.getText().toString().trim();
        //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
        if (TextUtils.isEmpty(accountName) ||
                accountName.length() != 11) {
//            !VaUtils.isMobileNo(accountName)){
            ToastUtil.showToast(getString(R.string.please_input_correct_phone));
//            tvGetCode.setEnabled(true);
            return;
        }
        CustomApplication.getRetrofitNew().sendSms(accountName, "sms_register", validate).enqueue(new MyCallback<String>() {
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
    private void registerUser() {
        String phone = etUserName.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();
        String passwordAgain = etUserPasswordAgain.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("请输入密码");
            return;
        }

        if (password.length() <6 || password.length() > 16) {
            ToastUtil.showToast("请输入6-16位密码");
            return;
        }

        if (!TextUtils.equals(password, passwordAgain)) {
            ToastUtil.showToast("两次密码输入不相同");
            return;
        }

        CustomApplication.getRetrofitNew().register(phone, MD5Util.MD5(password), code, CustomApplication.alias).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                dealRegister(body);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealRegister(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast(jsonObject.optString("msg"));
                String phone = etUserName.getText().toString().trim();
                String password = etUserPassword.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("mobile", phone);
                intent.putExtra("password", password);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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


    /**
     * 上传经纬度
     */
    private void netPostAddress(final Double lon, final Double lat) {
        //TODO 经纬度
        CustomApplication.getRetrofit().postAddress(lon, lat, 1).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        UserLoginTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //可选：简单验证DeviceId、CaptchaId、Listener值
            return mCaptcha.checkParams();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //必填：开始验证
                mCaptcha.Validate();

            } else {
                ToastUtil.showToast("验证码SDK参数设置错误,请检查配置");
            }
        }

        @Override
        protected void onCancelled() {
            mUserCaptchaTask = null;
        }
    }

}
