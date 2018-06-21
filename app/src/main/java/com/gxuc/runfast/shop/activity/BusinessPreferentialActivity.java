package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.BusinessCategoryAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class BusinessPreferentialActivity extends ToolBarActivity {

    @BindView(R.id.recycler_order)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private String lat;
    private String lon;
    private String agentId;

    private ArrayList<NearByBusinessInfo> businessInfos = new ArrayList<>();
    private BusinessCategoryAdapter businessCategoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_preferential);
        ButterKnife.bind(this);
        initView();
        initData();
        refreshData();
    }

    private void initView() {

    }

    private void initData() {
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        agentId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.AGENTID);

        businessCategoryAdapter = new BusinessCategoryAdapter(this, businessInfos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(businessCategoryAdapter);

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
                        requestPreferentialBusiness();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });

        businessCategoryAdapter.setOnNearByBusinessClickListener(new BusinessCategoryAdapter.OnNearByBusinessClickListener() {
            @Override
            public void onNearByBusinessClickListener(int position, NearByBusinessInfo nearByBusinessInfo) {
                Intent intent = new Intent(BusinessPreferentialActivity.this, BusinessNewActivity.class);
                intent.putExtra("businessId", nearByBusinessInfo.id);
                startActivity(intent);
            }
        });

    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        requestPreferentialBusiness();
    }

    private void clearRecyclerViewData() {
        businessInfos.clear();
        businessCategoryAdapter.setList(businessInfos);
    }

    private void requestPreferentialBusiness() {

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getPreferentialBusiness(agentId, lon, lat, currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            dealNearByBusinessData(jsonArray.toString());
                        } else {
                            isLastPage = true;
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

    private void dealNearByBusinessData(String data) {
        if (TextUtils.isEmpty(data)) {
            isLastPage = true;
            return;
        }

        ArrayList<NearByBusinessInfo> nearByBusinessInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<NearByBusinessInfo>>() {
        }.getType());
        businessInfos.addAll(nearByBusinessInfoList);
        businessCategoryAdapter.setList(businessInfos);
    }

}
