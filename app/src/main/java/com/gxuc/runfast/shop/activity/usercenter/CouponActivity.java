package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.CashCouponActivity;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.moneyadapter.CouponsAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 优惠券
 */
public class CouponActivity extends ToolBarActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_coupon_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_use_coupon_num)
    TextView mTvUseCouponNum;
    @BindView(R.id.tv_get_coupon)
    TextView mTvGetCoupon;
    @BindView(R.id.ll_no_coupon)
    LinearLayout mLlNoCoupon;

    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;

    private List<CouponBean> mCouponBeanList;
    private CouponsAdapter mAllAdapter;
    private boolean isChoose;
    private int bid;
    private String totalPrice;
    private int businessId;
    private HashMap<String, String> paramMap;
    private boolean isFromCart;
    private JSONObject paramJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void initData() {
        businessId = getIntent().getIntExtra("businessId", 0);
        isChoose = getIntent().getBooleanExtra("isChoose", false);
        bid = getIntent().getIntExtra("bid", 0);
        totalPrice = getIntent().getStringExtra("totalPrice");

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


        mCouponBeanList = new ArrayList<>();
        mAllAdapter = new CouponsAdapter(mCouponBeanList, this);
        mAllAdapter.setOnItemClickListener(new CouponsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CouponBean couponBean) {
                if (isChoose) {
                    if (isFromCart) {
                        requestFromCartOrderInfo(couponBean);
                    } else {
                        requestSelectRedPackage(couponBean);
                    }
//                    if (Double.valueOf(totalPrice) >= couponBean.full) {
//                        Intent intent = new Intent();
//                        intent.putExtra("coupon", couponBean);
//                        setResult(IntentConfig.COUPON_SELECT, intent);
//                        finish();
//                    } else {
//                        ToastUtils.showShortToast(CouponActivity.this, "暂不能使用该优惠券");
//                    }
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAllAdapter);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearRecyclerViewData();
                        refreshData();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.setEnableLoadMore(true);//恢复上拉状态
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage++;
                        getNetData();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
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

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        if (isChoose) {

            CustomApplication.getRetrofitNew().getMyRedPackValid(businessId, currentPage, 10).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            dealMyRedPack(jsonObject.optJSONArray("data"));
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

        } else {
            CustomApplication.getRetrofitNew().getMyRedPack(currentPage, 10).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            dealMyRedPack(jsonObject.optJSONArray("data"));
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
    }

    private void dealMyRedPack(JSONArray data) {
        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        ArrayList<CouponBean> couponBeanList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<CouponBean>>() {
        }.getType());

        mTvUseCouponNum.setText(couponBeanList == null ? "0" : couponBeanList.size() + "");

        if (couponBeanList != null && couponBeanList.size() > 0) {
            mCouponBeanList.addAll(couponBeanList);
        }
        mAllAdapter.setList(mCouponBeanList);
        recyclerView.setVisibility(mCouponBeanList.size() > 0 ? View.VISIBLE : View.GONE);
        mLlNoCoupon.setVisibility(mCouponBeanList.size() > 0 ? View.GONE : View.VISIBLE);


//        CouponBeans couponBeans = GsonUtil.parseJsonWithGson(data, CouponBeans.class);
//        if (couponBeans.getRows() != null && couponBeans.getRows().size() > 0) {
//            mCouponBeanList.clear();
//            mTvUseCouponNum.setText(String.valueOf(couponBeans.getRows().size()));
//            mCouponBeanList.addAll(couponBeans.getRows());
//            mAllAdapter.notifyDataSetChanged();
//            mLlNoCoupon.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//            mLlNoCoupon.setVisibility(View.VISIBLE);
//        }
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        getNetData();
    }

    private void clearRecyclerViewData() {
        mCouponBeanList.clear();
        mAllAdapter.setList(mCouponBeanList);
    }

    private void requestFromCartOrderInfo(final CouponBean couponBean) {

        final String userRedId = paramJson.optString("userRedId");
        try {
            paramJson.put("userRedId", couponBean.id + "");
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
                            intent.putExtra("userRedPrice", couponBean.less + "");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramJson.put("userRedId", userRedId);
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


    private void requestSelectRedPackage(final CouponBean couponBean) {
        final String userRedId = paramMap.get("userRedId");

        paramMap.put("userRedId", couponBean.id + "");
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
                            intent.putExtra("userRedPrice", couponBean.less + "");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramMap.put("userRedId", userRedId);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });


//        CustomApplication.getRetrofit().selectCoupon(bid, couponBean.redId).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body());
//                    if (jsonObject.optBoolean("success")) {
//                        Intent intent = new Intent();
//                        intent.putExtra("coupon", couponBean);
//                        setResult(IntentConfig.COUPON_SELECT, intent);
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

    @OnClick(R.id.tv_get_coupon)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_coupon:
//                startActivityForResult(new Intent(this, CashCouponActivity.class), 1001);
                Intent intent = new Intent(this, CashCouponActivity.class);
//                intent.putExtra("businessId", businessId);
                startActivity(intent);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        getNetData();
//    }
}
