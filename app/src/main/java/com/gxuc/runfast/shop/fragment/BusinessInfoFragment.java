package com.gxuc.runfast.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 商家
 * A simple {@link Fragment} subclass.
 */
public class BusinessInfoFragment extends LazyFragment {

    TextView tvBusinessAddress;

    TextView tvBusinessPhone;

    TextView tvBusinessRemark;

    TextView tvDistributionTime;

    Unbinder unbinder;

    public BusinessInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateViewLazy(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_business_info);
        iniView();
        getBusinessEvaluate(((BusinessActivity) getActivity()).getBusinessId());
    }

    private void iniView() {
        tvBusinessAddress = (TextView) findViewById(R.id.tv_business_address);
        tvBusinessPhone = (TextView) findViewById(R.id.tv_business_phone);
        tvBusinessRemark = (TextView) findViewById(R.id.tv_business_remark);
        tvDistributionTime = (TextView) findViewById(R.id.tv_distribution_time);
    }

    /**
     * 获取商家评价
     */
    private void getBusinessEvaluate(int id) {
        CustomApplication.getRetrofit().getBusinessInfo(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealBusinessInfo(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealBusinessInfo(String body) {
        try {
            JSONObject object = new JSONObject(body);
            JSONObject business = object.getJSONObject("business");
            tvBusinessAddress.setText(business.optString("address"));
            tvBusinessPhone.setText(business.optString("mobile"));
            tvBusinessRemark.setText(business.optString("content"));
            tvDistributionTime.setText(business.optString("wordTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
