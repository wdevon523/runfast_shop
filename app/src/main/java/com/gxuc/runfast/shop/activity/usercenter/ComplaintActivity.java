package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.example.supportv1.utils.ValidateUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.lljjcoder.citylist.Toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 投诉
 */
public class ComplaintActivity extends ToolBarActivity {

    @BindView(R.id.layout_right_title)
    RelativeLayout mLayoutRightTitle;
    @BindView(R.id.et_complaint_count)
    EditText mEtComplaintCount;
    @BindView(R.id.et_complaint_email)
    EditText mEtComplaintEmail;
    @BindView(R.id.btn_commit_info)
    Button mBtnCommitInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layout_right_title, R.id.btn_commit_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_right_title:
                ToastUtils.showShortToast(this, "开发中。。。");
                break;
            case R.id.btn_commit_info:
                if (!TextUtils.isEmpty(mEtComplaintCount.getText()) &&
                        mEtComplaintCount.getText().toString().length() >= 5) {
                    if (ValidateUtil.isEmail(mEtComplaintEmail.getText().toString())) {
                        toCommitComments();
                    } else {
                        ToastUtils.showShortToast(this, "请填写正确的邮箱");
                    }
                } else {
                    ToastUtils.showShortToast(this, "投诉内容少于五个字");
                }
                break;
        }
    }

    /**
     * 提交投诉
     */
    private void toCommitComments() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        String email = mEtComplaintEmail.getText().toString();
        String content = mEtComplaintCount.getText().toString();


        CustomApplication.getRetrofitNew().complain(content, email).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealMineComplaint(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealMineComplaint(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("success")) {
                ToastUtil.showToast("投诉已提交");
                finish();
            } else {
                ToastUtil.showToast(jsonObject.optString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}