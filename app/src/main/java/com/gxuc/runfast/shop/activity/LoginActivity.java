package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.MD5Util;
import com.example.supportv1.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;

    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    private boolean isRelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isRelogin = getIntent().getBooleanExtra("isRelogin", false);
        initView();
    }

    private void initView() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo != null) {
            etUserName.setText(userInfo.getMobile());
            etUserPassword.setText(userInfo.getPassword());
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.tv_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1001);
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    /**
     * 账号注册
     */
    private void login() {
        String phone = etUserName.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            CustomToast.INSTANCE.showToast(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CustomToast.INSTANCE.showToast(this, "请输入密码");
            return;
        }
        LogUtil.d("password", MD5Util.MD5(password));
        CustomApplication.getRetrofit().postLogin(phone, MD5Util.MD5(password), 0).enqueue(new MyCallback<String>() {
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
            JSONObject object = new JSONObject(body);
            boolean success = object.optBoolean("success");
            String msg = object.optString("msg");
            CustomToast.INSTANCE.showToast(this, msg);
            CustomApplication.isRelogining = false;
            if (!success) {
                return;
            }
//            CustomApplication.isRelogining = false;

            JSONObject app_cuser = object.getJSONObject("app_cuser");
            User user = GsonUtil.parseJsonWithGson(app_cuser.toString(), User.class);
            user.setPassword(etUserPassword.getText().toString().trim());
            UserService.saveUserInfo(user);
            UserService.setAutoLogin("1");
            if (!isRelogin) {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        String mobile = data.getStringExtra("mobile");
        String password = data.getStringExtra("password");
        etUserName.setText(mobile);
        etUserPassword.setText(password);
    }


}
