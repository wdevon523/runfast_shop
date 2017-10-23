package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 原密码验证修改
 */
public class UpdateOldPwdActivity extends ToolBarActivity {

    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_new_password_again)
    EditText etNewPasswordAgain;
    @BindView(R.id.btn_save_password)
    Button btnSavePassword;

    private boolean oldIsEmpty;
    private boolean newIsEmpty;
    private boolean newAgainIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_old_pwd);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    oldIsEmpty = true;
                } else {
                    oldIsEmpty = false;
                }
                if (oldIsEmpty && newIsEmpty && newAgainIsEmpty) {
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
                if (oldIsEmpty && newIsEmpty && newAgainIsEmpty) {
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
                if (oldIsEmpty && newIsEmpty && newAgainIsEmpty) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });
    }

    /**
     * 密码修改
     */
    private void editPassword() {
        String oldPwd = etOldPassword.getText().toString().trim();
        String newPwd = etNewPassword.getText().toString().trim();
        String newPwdAgain = etNewPasswordAgain.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            CustomToast.INSTANCE.showToast(this, "旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            CustomToast.INSTANCE.showToast(this, "旧密码不能为空");
            return;
        }
        if (!TextUtils.equals(newPwd, newPwdAgain)) {
            CustomToast.INSTANCE.showToast(this, "两次新密码输入不一致");
            return;
        }
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().updatePassword(oldPwd, newPwd, 0, newPwdAgain).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealUpdatePassWord(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealUpdatePassWord(String body) {
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

    @OnClick(R.id.btn_save_password)
    public void onViewClicked() {
        editPassword();
    }

}
