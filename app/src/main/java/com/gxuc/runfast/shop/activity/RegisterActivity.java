package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.VaUtils;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.tv_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                Message message = handler.obtainMessage();
                message.what = 1002;
                message.arg1 = 59;
                message.sendToTarget();
                getAuthCode();
                break;
            case R.id.btn_register:
                registerUser();
                break;
        }
    }

    /***
     * 获取验证码
     */
    private void getAuthCode() {
        String accountName = etUserName.getText().toString().trim();
        //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
        if (TextUtils.isEmpty(accountName) || !VaUtils.isMobileNo(accountName)) {
            CustomToast.INSTANCE.showToast(this, getString(R.string.please_input_correct_phone));
            return;
        }
        CustomApplication.getRetrofit().getCode(accountName).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealCode(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });


    }

    private void dealCode(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            if (!success) {
                CustomToast.INSTANCE.showToast(this, msg);
                return;
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
            CustomToast.INSTANCE.showToast(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            CustomToast.INSTANCE.showToast(this, "请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CustomToast.INSTANCE.showToast(this, "请输入密码");
            return;
        }
        if (!TextUtils.equals(password, passwordAgain)) {
            CustomToast.INSTANCE.showToast(this, "两次密码输入不相同");
            return;
        }

        CustomApplication.getRetrofit().postRegister(phone, password, code).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealRegister(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealRegister(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            if (!success) {
                CustomToast.INSTANCE.showToast(this, msg);
                return;
            }

            CustomToast.INSTANCE.showToast(this, msg);
            String phone = etUserName.getText().toString().trim();
            String password = etUserPassword.getText().toString().trim();
            Intent intent = new Intent();
            intent.putExtra("mobile", phone);
            intent.putExtra("password", password);
            setResult(RESULT_OK, intent);
            finish();

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
