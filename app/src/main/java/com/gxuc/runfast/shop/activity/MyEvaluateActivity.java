package com.gxuc.runfast.shop.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.MyEvaluateAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessEvaluationInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.AndroidBug5497Workaround;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

public class MyEvaluateActivity extends ToolBarActivity {

    @BindView(R.id.evaluate_recycleview)
    RecyclerView evaluateRecycleview;
    //    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_logo)
    CircleImageView ivHeadLogo;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_evaluate_num)
    TextView tvEvaluateNum;
    @BindView(R.id.rl_evalute_more)
    RelativeLayout rlEvaluteMore;
    @BindView(R.id.et_evaluate_more)
    EditText etEvaluateMore;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private ArrayList<BusinessEvaluationInfo> evaluationInfos;
    private MyEvaluateAdapter myEvaluateAdapter;
    private UserInfo userInfo;
    private int evaluateMorePosition;
    private BusinessEvaluationInfo evaluationInfoMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluate);
        ButterKnife.bind(this);

        initView();
        initData();
        setListener();
    }


    private void initView() {
        StatusUtil.setSystemStatus(this, true, true);
        AndroidBug5497Workaround.assistActivity(this);

        userInfo = UserService.getUserInfo(this);
        x.image().bind(ivHeadLogo, UrlConstant.ImageBaseUrl + userInfo.pic, NetConfig.optionsLogoImage);
        tvUserName.setText(TextUtils.isEmpty(userInfo.nickname) ? userInfo.mobile : userInfo.nickname);
        myEvaluateAdapter = new MyEvaluateAdapter(this, evaluationInfos, userInfo);
        evaluateRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        evaluateRecycleview.setAdapter(myEvaluateAdapter);
    }

    private void initData() {

        requestMyEvaluate();
    }

    private void requestMyEvaluate() {

        CustomApplication.getRetrofitNew().getMyEvaluate().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        tvEvaluateNum.setText("已贡献" + data.optString("commentCount") + "条评价");
                        JSONArray jsonArray = data.optJSONArray("commentList");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            evaluationInfos = JsonUtil.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BusinessEvaluationInfo>>() {
                            }.getType());
                            myEvaluateAdapter.setList(evaluationInfos);
                        }

                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void setListener() {
        myEvaluateAdapter.setOnDeleteListener(new MyEvaluateAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                BusinessEvaluationInfo evaluationInfo = evaluationInfos.get(position);
                requestDeleteEvaluation(position, evaluationInfo);

            }
        });

        myEvaluateAdapter.setOnEvaluateListener(new MyEvaluateAdapter.OnEvaluateListener() {

            @Override
            public void onEvaluate(int position) {
                evaluationInfoMore = evaluationInfos.get(evaluateMorePosition);
                rlEvaluteMore.setVisibility(View.VISIBLE);
                SystemUtil.showSoftKeyboard(etEvaluateMore);
            }
        });
    }

    private void requestDeleteEvaluation(final int position, BusinessEvaluationInfo evaluationInfo) {


        CustomApplication.getRetrofitNew().deleteEvaluate(evaluationInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("删除成功");
                        evaluationInfos.remove(position);
                        myEvaluateAdapter.notifyItemRemoved(position);
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(rlEvaluteMore, ev) ) {
                rlEvaluteMore.setVisibility(View.GONE);
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
//        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
//        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etEvaluateMore.getText().toString())) {
                    ToastUtil.showToast("请填写追评内容");
                    return;
                }

                CustomApplication.getRetrofitNew().evaluateMore(evaluationInfoMore.id, etEvaluateMore.getText().toString()).enqueue(new MyCallback<String>() {
                    @Override
                    public void onSuccessResponse(Call<String> call, Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.optBoolean("success")) {
                                rlEvaluteMore.setVisibility(View.GONE);
                                requestMyEvaluate();
                            } else {
                                ToastUtil.showToast(jsonObject.optString("errorMsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailureResponse(Call<String> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
