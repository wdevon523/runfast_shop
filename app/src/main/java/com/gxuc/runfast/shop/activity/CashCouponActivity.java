package com.gxuc.runfast.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.coupon.CouponInfo;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.IntentConfig;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.moneyadapter.CashCouponAdapter;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private List<CouponInfo> couponInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_coupon);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mAdapter = new CashCouponAdapter(couponInfoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        getNetData();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            CouponInfo couponInfo = couponInfoList.get(position);
            if (couponInfo != null) {
                getCoupon(couponInfo.getId());
            }
        }
    }

    /**
     * 获取优惠券
     */
    private void getNetData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().receiveCoupan(IntentConfig.AGENT_ID, 0).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                ResolveData(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * 领取优惠券
     */
    private void getCoupon(Integer id) {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().getCoupan(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(data);
                    String msg = object.optString("ale");
                    CustomToast.INSTANCE.showToast(getApplication(), msg);
//                    Intent intent = new Intent();
//                    setResult(RESULT_OK,intent);
                    finish();
                    return;
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
    private void ResolveData(String data) {

        try {
            JSONObject object = new JSONObject(data);
            JSONArray list = object.getJSONArray("list");
            if (list == null || list.length() <= 0) {
                recyclerView.setVisibility(View.GONE);
                return;
            }
            int length = list.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = list.getJSONObject(i);
                CouponInfo couponInfo = GsonUtil.parseJsonWithGson(jsonObject.toString(), CouponInfo.class);
                couponInfoList.add(couponInfo);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
