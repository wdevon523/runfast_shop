package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.UserCaptchaTask;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.VaUtils;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 短信验证码修改
 */
public class UpdateMessageActivity extends ToolBarActivity {

    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_new_password_again)
    EditText etNewPasswordAgain;
    @BindView(R.id.btn_save_password)
    Button btnSavePassword;

    private boolean codeIsEmpty;
    private boolean newIsEmpty;
    private boolean newAgainIsEmpty;
    private int mFlags;
    private String mPhone;
    private Captcha mCaptcha;
    /*验证码SDK,该Demo采用异步获取方式*/
    private UserCaptchaTask mUserCaptchaTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);
        ButterKnife.bind(this);
        initData();
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

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mFlags = intent.getIntExtra(IntentFlag.KEY, -1);
            if (mFlags != 0) {
                mPhone = intent.getStringExtra("phone");
            }
        }
    }

    private void setListener() {
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    codeIsEmpty = true;
                } else {
                    codeIsEmpty = false;
                }
                if (codeIsEmpty && newIsEmpty && newAgainIsEmpty) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    newIsEmpty = true;
                } else {
                    newIsEmpty = false;
                }
                if (codeIsEmpty && newIsEmpty && newAgainIsEmpty) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });
        etNewPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    newAgainIsEmpty = true;
                } else {
                    newAgainIsEmpty = false;
                }
                if (codeIsEmpty && newIsEmpty && newAgainIsEmpty) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });
    }

    @OnClick({R.id.tv_get_code, R.id.btn_save_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
//                getAuthCode();
                mCaptcha.start();
                mUserCaptchaTask = new UserCaptchaTask(mCaptcha);
                //关闭mLoginTask任务可以放在myCaptchaListener的onCancel接口中处理
                mUserCaptchaTask.execute();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
                //mCaptcha.Validate();
                break;
            case R.id.btn_save_password:
                editPassword();
                break;
        }
    }

    /***
     * 获取验证码
     * @param validate
     */
    private void getAuthCode(String validate) {
        if (mFlags != 1) {
            User userInfo = UserService.getUserInfo(this);
            if (userInfo == null) {
                return;
            }

            CustomApplication.getRetrofit().getEditPwdCode(userInfo.getMobile(), validate).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealCode(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {
                    tvGetCode.setEnabled(true);
                }
            });
        } else {
            //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
            if (TextUtils.isEmpty(mPhone) ||
                    mPhone.length() != 11) {
//                !VaUtils.isMobileNo(mPhone)){
                CustomToast.INSTANCE.showToast(this, getString(R.string.please_input_correct_phone));
                return;
            }
            CustomApplication.getRetrofit().getForgetCode(mPhone, validate).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealCode(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void dealCode(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            ToastUtil.showToast(msg);
            Message message = handler.obtainMessage();
            message.what = 1002;
            message.arg1 = 59;
            message.sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 密码修改
     */
    private void editPassword() {
        String code = etCode.getText().toString().trim();
        String newPwd = etNewPassword.getText().toString().trim();
        String newPwdAgain = etNewPasswordAgain.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            CustomToast.INSTANCE.showToast(this, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            CustomToast.INSTANCE.showToast(this, "新密码不能为空");
            return;
        }
        if (!TextUtils.equals(newPwd, newPwdAgain)) {
            CustomToast.INSTANCE.showToast(this, "两次新密码输入不一致");
            return;
        }
        if (mFlags == 1) {
            CustomApplication.getRetrofit().updateForgotPwd(mPhone, code, newPwd).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealPwd(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        } else {
            User userInfo = UserService.getUserInfo(this);
            if (userInfo == null) {
                return;
            }
            CustomApplication.getRetrofit().updatePassword("", newPwd, 1, code).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealPwd(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void dealPwd(String body) {
        try {
            JSONObject object = new JSONObject(body);
            boolean success = object.optBoolean("success");
            String msg = object.optString("msg");
            CustomToast.INSTANCE.showToast(this, msg);
            if (success) {
                setResult(RESULT_OK);
                finish();
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

}
