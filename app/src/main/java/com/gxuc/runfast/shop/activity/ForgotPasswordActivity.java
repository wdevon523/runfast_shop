package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.UpdateMessageActivity;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.UserCaptchaTask;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.VaUtils;
import com.example.supportv1.utils.ValidateUtil;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.btn_ok)
    Button mBtnOk;
    private Intent mIntent;
    private int UPDATE_PASSWORD = 1000;
    private Captcha mCaptcha;

    /*验证码SDK,该Demo采用异步获取方式*/
    private UserCaptchaTask mUserCaptchaTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        initCaptcha();
        setListener();
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


    private void setListener() {
        mEtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && ValidateUtil.isMobileNo(s.toString())) {
                    mBtnOk.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    mBtnOk.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }

            }
        });

    }

    @OnClick({R.id.tv_code, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
//                getAuthCode();
                mCaptcha.start();
                mUserCaptchaTask = new UserCaptchaTask(mCaptcha);
                //关闭mUserCaptchaTask任务可以放在myCaptchaListener的onCancel接口中处理
                mUserCaptchaTask.execute();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
                //mCaptcha.Validate();
                break;
            case R.id.btn_ok:
                resetPassword();
                break;
        }
    }

    /***
     * 获取验证码
     * @param validate
     */
    private void getAuthCode(String validate) {
        String accountName = mEtUserName.getText().toString().trim();
        //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
        if (TextUtils.isEmpty(accountName) ||
                accountName.length() != 11) {
//                !VaUtils.isMobileNo(accountName)) {
            CustomToast.INSTANCE.showToast(this, getString(R.string.please_input_correct_phone));
            return;
        }
        CustomApplication.getRetrofit().getForgetCode(accountName, validate).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealForgetCode(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealForgetCode(String body) {
        //验证码
        try {
            JSONObject object = new JSONObject(body);
            boolean success = object.getBoolean("success");
            String msg = object.getString("msg");
            if (success) {
                CustomToast.INSTANCE.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置密码
     */
    private void resetPassword() {
        String phone = mEtUserName.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();

//        if (TextUtils.isEmpty(phone)) {
//            CustomToast.INSTANCE.showToast(this, "请输入手机号");
//            return;
//        }
//        if (TextUtils.isEmpty(code)) {
//            CustomToast.INSTANCE.showToast(this, "请输入验证码");
//            return;
//        }
        mIntent = new Intent(this, UpdateMessageActivity.class);
        mIntent.putExtra(IntentFlag.KEY, IntentFlag.FORGOT_PWD);
        mIntent.putExtra("phone", phone);
        startActivityForResult(mIntent, UPDATE_PASSWORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, "");
        setResult(RESULT_OK);
        finish();
    }
}
