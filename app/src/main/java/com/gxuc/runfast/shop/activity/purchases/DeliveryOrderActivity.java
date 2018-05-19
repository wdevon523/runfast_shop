package com.gxuc.runfast.shop.activity.purchases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.DeliveryOrderAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.DeliveryOrderInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class DeliveryOrderActivity extends ToolBarActivity {
    @BindView(R.id.fl_back_but)
    FrameLayout flBackBut;
    @BindView(R.id.fl_right)
    FrameLayout flRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private List<DeliveryOrderInfo> deliveryOrderList = new ArrayList<>();
    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private DeliveryOrderAdapter deliveryOrderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void initData() {

    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        requestDeliveryOrders();
    }

    private void requestDeliveryOrders() {

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getDeliveryOrder(currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")){
                        String data = jsonObject.optString("data");
                        List<DeliveryOrderInfo> OrderList = JsonUtil.fromJson(data, new TypeToken<ArrayList<DeliveryOrderInfo>>() {
                        }.getType());
                        if (OrderList != null && OrderList.size() > 0) {
                            deliveryOrderList.addAll(OrderList);
                            deliveryOrderAdapter.setdeliveryOrderList(deliveryOrderList);
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

    private void clearRecyclerViewData() {
        deliveryOrderList.clear();
        deliveryOrderAdapter.setdeliveryOrderList(deliveryOrderList);
    }

    private void initView() {

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        deliveryOrderAdapter = new DeliveryOrderAdapter(this, deliveryOrderList);
        recyclerView.setAdapter(deliveryOrderAdapter);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {

                refreshlayout.getLayout().postDelayed(new Runnable() {
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
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage++;
                        requestDeliveryOrders();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });

        deliveryOrderAdapter.setOnRefreshListener(new DeliveryOrderAdapter.onRefreshListener() {
            @Override
            public void onRef() {
                refreshData();
            }
        });
    }

    @OnClick({R.id.fl_back_but, R.id.fl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_back_but:
                finish();
                break;
            case R.id.fl_right:
                break;
        }
    }
}
