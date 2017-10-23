package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.example.supportv1.utils.LogUtil;
import com.example.supportv1.utils.ValidateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by huiliu on 2017/9/7.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class ChangeNameActivity extends ToolBarActivity {


    @BindView(R.id.et_change_info)
    EditText mEtChangeInfo;
    @BindView(R.id.tv_change_tips)
    TextView mTvChangeTips;
    @BindView(R.id.btn_save_name)
    Button mBtnSaveName;
    private User mUserInfo;
    private String mInfo;
    private int mFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        mFlags = intent.getFlags();
        mUserInfo = intent.getParcelableExtra("userInfo");
        LogUtil.d("用户信息", mUserInfo.toString());
        switch (mFlags) {
            case 0:
                mEtChangeInfo.setText(mUserInfo.getNickname());
                mTvChangeTips.setText("一个好的名字会吸引人");
                break;
            case 1:
                mEtChangeInfo.setText(mUserInfo.getEmail());
                mTvChangeTips.setText("请正确填写邮箱");
                break;
        }
    }


    @OnClick(R.id.btn_save_name)
    public void onViewClicked(View view) {
        mInfo = mEtChangeInfo.getText().toString();
        switch (mFlags) {
            case 0:
                if (!TextUtils.isEmpty(mInfo)) {
                    CustomApplication.getRetrofit().postChangeName(mInfo).enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealChangeName(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    CustomToast.INSTANCE.showToast(this, "昵称不可为空");
                }
                break;
            case 1:
                if (ValidateUtil.isEmail(mInfo)) {
                    CustomApplication.getRetrofit().postChangeEmail(mInfo).enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealChangeEmail(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    CustomToast.INSTANCE.showToast(this, "邮箱格式错误，请检查");
                }
                break;
        }
    }

    private void dealChangeName(String body) {
        try {
            JSONObject object = new JSONObject(body);
            boolean success = object.optBoolean("success");
            if (success) {
                mUserInfo.setNickname(mInfo);
                UserService.saveUserInfo(mUserInfo);
                CustomToast.INSTANCE.showToast(this, "保存成功");
                Intent intent = new Intent();
                intent.putExtra("nickname", mInfo);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                CustomToast.INSTANCE.showToast(this, "保存失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dealChangeEmail(String body) {
        try {
            JSONObject object = new JSONObject(body);
            boolean success = object.optBoolean("success");
            if (success) {
                mUserInfo.setEmail(mInfo);
                UserService.saveUserInfo(mUserInfo);
                CustomToast.INSTANCE.showToast(this, "保存成功");
                Intent intent = new Intent();
                intent.putExtra("email", mInfo);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                CustomToast.INSTANCE.showToast(this, "保存失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
