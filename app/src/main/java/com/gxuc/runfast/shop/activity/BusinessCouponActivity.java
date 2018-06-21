package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.moneyadapter.CouponsAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class BusinessCouponActivity extends ToolBarActivity {
    @BindView(R.id.view_coupon_list)
    RecyclerView recyclerView;

    private CouponsAdapter mAdapter;

    private int businessId;
    private ArrayList<CouponBean> couponBeanList;
    private HashMap<String, String> paramMap;
    private boolean isFromCart;
    private JSONObject paramJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_coupon);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        businessId = getIntent().getIntExtra("businessId", 0);
        isFromCart = getIntent().getBooleanExtra("isFromCart", false);
        try {
            if (isFromCart) {
                paramJson = new JSONObject(getIntent().getStringExtra("paramJson"));
            } else {
                paramMap = (HashMap<String, String>) getIntent().getSerializableExtra("paramMap");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new CouponsAdapter(couponBeanList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        getNetData();

        mAdapter.setOnItemClickListener(new CouponsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CouponBean couponBean) {
                if (isFromCart) {
                    requestFromCartOrderInfo(couponBean);
                } else {
                    receiverCoupon(couponBean);
                }
            }
        });
    }

    /**
     * 获取优惠券
     */
    private void getNetData() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getBusinessCoupan(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        if (!TextUtils.isEmpty(data) || !TextUtils.equals("null", jsonObject.optString("data"))) {
                            dealCoupon(data);
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


    private void requestFromCartOrderInfo(final CouponBean couponBean) {

        final String userCouponId = paramJson.optString("userCouponId");
        try {
            paramJson.put("userCouponId", couponBean.id + "");
            paramJson.put("suportSelf", "");
            SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomApplication.getRetrofitNew().fillInDiy().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
                            ShopCartBean shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
                            Intent intent = new Intent();
                            intent.putExtra("shopCartBean", shopCartBean);
                            intent.putExtra("paramJson", paramJson.toString());
                            intent.putExtra("isFromCart", isFromCart);
                            intent.putExtra("userCouponPrice", couponBean.less + "");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramJson.put("userCouponId", userCouponId);
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
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
     *
     * @param couponBean
     */
    private void receiverCoupon(final CouponBean couponBean) {

        final String userCouponId = paramMap.get("userCouponId");

        paramMap.put("userCouponId", couponBean.id + "");
        paramMap.put("suportSelf", "");

        CustomApplication.getRetrofitNew().submitBusinessShopCart(paramMap).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
                            ShopCartBean shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
                            Intent intent = new Intent();
                            intent.putExtra("paramMap", (Serializable) paramMap);
                            intent.putExtra("shopCartBean", shopCartBean);
                            intent.putExtra("userCouponPrice", couponBean.less + "");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramMap.put("userCouponId", userCouponId);
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

        ArrayList<CouponBean> couponBeanList = JsonUtil.fromJson(data, new TypeToken<ArrayList<CouponBean>>() {
        }.getType());

        if (couponBeanList != null && couponBeanList.size() > 0) {
            mAdapter.setList(couponBeanList);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

}
