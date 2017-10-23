package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.coupon.CouponBeans;
import com.gxuc.runfast.shop.config.IntentConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.CashCouponActivity;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.moneyadapter.CouponsAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 优惠券
 */
public class CouponActivity extends ToolBarActivity implements Callback<String> {

    @BindView(R.id.view_coupon_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_use_coupon_num)
    TextView mTvUseCouponNum;
    @BindView(R.id.tv_get_coupon)
    TextView mTvGetCoupon;
    @BindView(R.id.ll_no_coupon)
    LinearLayout mLlNoCoupon;

    private List<CouponBean> mCouponBeanList;
    private CouponsAdapter mAllAdapter;
    private boolean isChoose;
    private int bid;

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
        getNetData();
    }

    /**
     * 获取优惠券
     */
    private void getNetData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().GetMyCoupan(0).enqueue(this);
    }

    private void initData() {
        isChoose = getIntent().getBooleanExtra("isChoose", false);
        bid = getIntent().getIntExtra("bid", 0);

        mCouponBeanList = new ArrayList<>();
        mAllAdapter = new CouponsAdapter(mCouponBeanList, this);
        mAllAdapter.setOnItemClickListener(new CouponsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CouponBean couponBean) {
                if (isChoose) {
                    requestSelectCoupon(couponBean);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAllAdapter);
    }

    private void requestSelectCoupon(final CouponBean couponBean) {
        CustomApplication.getRetrofit().selectCoupon(bid, couponBean.getCouponId()).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent();
                        intent.putExtra("coupon", couponBean);
                        setResult(IntentConfig.COUPON_SELECT, intent);
                        finish();
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
    public void onResponse(Call<String> call, Response<String> response) {
        String data = response.body();
        Log.d("params", "isSuccessful = " + response.isSuccessful());
        if (response.isSuccessful()) {
            Log.d("params", "response = " + data);
            ResolveData(data);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        CustomToast.INSTANCE.showToast(this, "网络异常");
    }

    /**
     * 解析数据
     *
     * @param data
     */
    private void ResolveData(String data) {
        CouponBeans couponBeans = GsonUtil.parseJsonWithGson(data, CouponBeans.class);
        if (couponBeans.getRows() != null && couponBeans.getRows().size() > 0) {
            mCouponBeanList.clear();
            mTvUseCouponNum.setText(String.valueOf(couponBeans.getRows().size()));
            mCouponBeanList.addAll(couponBeans.getRows());
            mAllAdapter.notifyDataSetChanged();
            mLlNoCoupon.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            mLlNoCoupon.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_get_coupon)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_coupon:
//                startActivityForResult(new Intent(this, CashCouponActivity.class), 1001);
                startActivity(new Intent(this, CashCouponActivity.class));
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
