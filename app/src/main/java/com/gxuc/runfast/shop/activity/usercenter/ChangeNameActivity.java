package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.example.supportv1.utils.LogUtil;
import com.example.supportv1.utils.ValidateUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

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
    private UserInfo mUserInfo;
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
        mFlags = intent.getIntExtra(IntentFlag.KEY, -1);
        mUserInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        LogUtil.d("用户信息", mUserInfo.toString());
        switch (mFlags) {
            case 0:
                mEtChangeInfo.setText(mUserInfo.nickname);
                mTvChangeTips.setText("一个好的名字会吸引人");
                break;
            case 1:
                mEtChangeInfo.setText(mUserInfo.email);
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
                    CustomApplication.getRetrofitNew().updateUserNick(mInfo).enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealChange(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });

                } else {
                    ToastUtil.showToast("昵称不可为空");
                }
                break;
            case 1:
                if (ValidateUtil.isEmail(mInfo)) {
                    CustomApplication.getRetrofitNew().updateUserEmail(mInfo).enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealChange(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    ToastUtil.showToast("邮箱格式错误，请检查");
                }
                break;
        }
    }

    private void dealChange(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast("保存成功");
                if (mFlags == 0) {
                    mUserInfo.nickname = mInfo;
                } else {
                    mUserInfo.email = mInfo;
                }
                UserService.saveUserInfo(mUserInfo);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
