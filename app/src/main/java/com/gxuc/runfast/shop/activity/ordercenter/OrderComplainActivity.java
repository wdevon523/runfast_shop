package com.gxuc.runfast.shop.activity.ordercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.order.OrderInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.example.supportv1.utils.ValidateUtil;
import com.lljjcoder.citylist.Toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class OrderComplainActivity extends ToolBarActivity {

    @BindView(R.id.et_order_complaint)
    EditText mEtOrderComplaint;
    @BindView(R.id.et_order_complaint_email)
    EditText mEtOrderComplaintEmail;
    @BindView(R.id.btn_order_commit_info)
    Button mBtnOrderCommitInfo;
    private OrderInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complain);
        ButterKnife.bind(this);
        getData();
    }

    public void getData() {
        mInfo = getIntent().getParcelableExtra("orderInfo");
    }

    @OnClick(R.id.btn_order_commit_info)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_order_commit_info:
                if (!TextUtils.isEmpty(mEtOrderComplaint.getText()) &&
                        mEtOrderComplaint.getText().toString().length() > 5) {
                    if (ValidateUtil.isEmail(mEtOrderComplaintEmail.getText().toString())) {
                        toCommitComments();
                    } else {
                        ToastUtils.showShortToast(this, "请填写正确的邮箱");
                    }
                } else {
                    ToastUtils.showShortToast(this, "投诉内容少于五个字");
                }
        }
    }

    private void toCommitComments() {
        Integer id = mInfo.getId();
        Integer businessId = mInfo.getBusinessId();
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        String content = mEtOrderComplaint.getText().toString();
        String email = mEtOrderComplaintEmail.getText().toString();

        CustomApplication.getRetrofit().postOrderComplaint(businessId, id,
                email, content).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealOrderComplaint(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealOrderComplaint(String body) {
        try {
            JSONObject object = new JSONObject(body);
            CustomToast.INSTANCE.showToast(this, object.optString("msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
