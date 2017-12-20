package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.VaUtils;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mFlags = intent.getFlags();
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
                getAuthCode();
                break;
            case R.id.btn_save_password:
                editPassword();
                break;
        }
    }

    /***
     * 获取验证码
     */
    private void getAuthCode() {
        if (mFlags != 1) {
            User userInfo = UserService.getUserInfo(this);
            if (userInfo == null) {
                return;
            }

            CustomApplication.getRetrofit().getEditPwdCode(userInfo.getMobile()).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealCode(response.body());
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        } else {
            //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
            if (TextUtils.isEmpty(mPhone) || !VaUtils.isMobileNo(mPhone)) {
                CustomToast.INSTANCE.showToast(this, getString(R.string.please_input_correct_phone));
                return;
            }
            CustomApplication.getRetrofit().getForgetCode(mPhone).enqueue(new MyCallback<String>() {
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
            CustomApplication.getRetrofit().updatePassword(code, newPwd, 1, newPwdAgain).enqueue(new MyCallback<String>() {
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
                    tvGetCode.setText(second + "秒后可发送");
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
