package com.gxuc.runfast.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessCouponInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.moneyadapter.CashCouponAdapter;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 代金券
 */
public class CashCouponActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.view_coupon_list)
    RecyclerView recyclerView;

    private CashCouponAdapter mAdapter;

    private int businessId;
    private ArrayList<BusinessCouponInfo> businessCouponInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_coupon);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
//        businessId = getIntent().getIntExtra("businessId", 0);
        mAdapter = new CashCouponAdapter(businessCouponInfoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        getNetData();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            BusinessCouponInfo businessCouponInfo = businessCouponInfoList.get(position);
            if (businessCouponInfo != null) {
                receiverCoupon(businessCouponInfo.id);
            }
        }
    }

    /**
     * 获取优惠券
     */
    private void getNetData() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        String agantId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.AGENTID);

        CustomApplication.getRetrofitNew().getCoupan(agantId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data != null && data.length() > 0) {
                            dealCoupon(data.toString());
                        } else {
                            recyclerView.setVisibility(View.GONE);
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

    /**
     * 领取优惠券
     */
    private void receiverCoupon(Integer id) {

        CustomApplication.getRetrofitNew().receiverBusinessCoupon(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("领取成功");
                        finish();
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

    /**
     * 解析数据
     *
     * @param data
     */
    private void dealCoupon(String data) {

        businessCouponInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<BusinessCouponInfo>>() {
        }.getType());

        if (businessCouponInfoList != null && businessCouponInfoList.size() > 0) {
            mAdapter.setList(businessCouponInfoList);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

}
