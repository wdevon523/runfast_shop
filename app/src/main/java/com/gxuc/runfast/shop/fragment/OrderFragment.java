package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.activity.LoginQucikActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.OrderInformation;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.activity.ordercenter.OrderDetailActivity;
import com.gxuc.runfast.shop.adapter.OrderListAdapter;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单页
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements OrderListAdapter.OnClickListener {

//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycler_order)
    RecyclerView recyclerView;

    @BindView(R.id.ll_not_order)
    LinearLayout llNotOrder;
    @BindView(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    Unbinder unbinder;

    private List<OrderInformation> mOrderInfos = new ArrayList<>();
    private OrderListAdapter orderListAdapter;
    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private UserInfo userInfo;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
//        toolbarTitle.setText("订单");
        initData();
        initEvent();

        return view;
    }

    private void initData() {
//        if (mOrderInfos.size() <= 0) {
//            llNotOrder.setVisibility(View.VISIBLE);
//        }
        orderListAdapter = new OrderListAdapter(mOrderInfos, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(orderListAdapter);

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
                        getOrderList();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });
    }


    private void initEvent() {
//        recyclerView.addon
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        getOrderList();
    }

    private void clearRecyclerViewData() {
        mOrderInfos.clear();
        orderListAdapter.setOrderList(mOrderInfos);
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = UserService.getUserInfo(getActivity());
        if (!isHidden()) {
            refreshData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            StatusUtil.setUseStatusBarColor(getActivity(), getResources().getColor(R.color.white));
            StatusUtil.setSystemStatus(getActivity(), true, true);
            userInfo = UserService.getUserInfo(getActivity());
            if (userInfo == null) {
                mOrderInfos.clear();
                orderListAdapter.notifyDataSetChanged();
                llNotLogin.setVisibility(View.VISIBLE);
                llNotOrder.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                return;
            }
            refreshData();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getOrderList();
        }
    };

    /**
     *
     */
    private void getOrderList() {

        if (userInfo == null) {
            return;
        }

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getOrderList(currentPage, 10).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }

                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (!body.contains("{\"relogin\":1}") && jsonObject.optBoolean("success")) {
                        dealOrderList(jsonObject);
                        llNotLogin.setVisibility(View.GONE);
                    } else {
                        mOrderInfos.clear();
                        orderListAdapter.notifyDataSetChanged();
                        llNotLogin.setVisibility(View.VISIBLE);
                        llNotOrder.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtil.showToast("网络数据异常");
            }
        });

//        CustomApplication.getRetrofit().postOrderList(currentPage, 10).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful()) {
//                    ToastUtil.showToast("网络数据异常");
//                    return;
//                }
//
//                String body = response.body().toString();
//                if (!body.contains("{\"relogin\":1}")) {
//                    dealOrderList(response.body());
//                    llNotLogin.setVisibility(View.GONE);
//                } else {
//                    mOrderInfos.clear();
//                    orderListAdapter.notifyDataSetChanged();
//                    llNotLogin.setVisibility(View.VISIBLE);
//                    llNotOrder.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                ToastUtil.showToast("网络数据异常");
//            }
//
//        });
    }

    private void dealOrderList(JSONObject jsonObject) {
//        OrderInfos orderInfos = GsonUtil.parseJsonWithGson(body, OrderInfos.class);
//        if (orderInfos.getRows() != null && orderInfos.getRows().size() > 0) {
//            layoutNotOrder.setVisibility(View.GONE);
//        }
        JSONArray data = jsonObject.optJSONArray("data");
        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        ArrayList<OrderInformation> orderInformationList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<OrderInformation>>() {
        }.getType());


        mOrderInfos.addAll(orderInformationList);
        orderListAdapter.setOrderList(mOrderInfos);
        recyclerView.setVisibility(mOrderInfos.size() > 0 ? View.VISIBLE : View.GONE);
        llNotOrder.setVisibility(mOrderInfos.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderId", mOrderInfos.get(position).orderId);
        startActivity(intent);
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginQucikActivity.class));
    }
}
