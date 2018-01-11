package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.UpdateMessageActivity;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.VaUtils;
import com.example.supportv1.utils.ValidateUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setListener();
    }

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
                getAuthCode();
                break;
            case R.id.btn_ok:
                resetPassword();
                break;
        }
    }

    /***
     * 获取验证码
     */
    private void getAuthCode() {
        String accountName = mEtUserName.getText().toString().trim();
        //|| !VaUtils.isMobileNo(accountName) 手机号正则验证
        if (TextUtils.isEmpty(accountName) || !VaUtils.isMobileNo(accountName)) {
            CustomToast.INSTANCE.showToast(this, getString(R.string.please_input_correct_phone));
            return;
        }
        CustomApplication.getRetrofit().getForgetCode(accountName).enqueue(new MyCallback<String>() {
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
        if (resultCode!= RESULT_OK){
            return;
        }
        SharePreferenceUtil.getInstance().putStringValue(CustomConstant.PASSWORD, "");
        setResult(RESULT_OK);
        finish();
    }
}
