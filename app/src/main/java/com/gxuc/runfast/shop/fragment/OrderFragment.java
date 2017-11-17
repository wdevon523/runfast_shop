package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.LoginActivity;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.order.OrderInfo;
import com.gxuc.runfast.shop.bean.order.OrderInfos;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.activity.ordercenter.OrderDetailActivity;
import com.gxuc.runfast.shop.adapter.OrderListAdapter;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 订单页
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements OrderListAdapter.OnClickListener, LoadMoreAdapter.LoadMoreApi {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    Unbinder unbinder;
    @BindView(R.id.recycler_order)
    RecyclerView recyclerView;

    @BindView(R.id.layout_not_order)
    RelativeLayout layoutNotOrder;

    private List<OrderInfo> mOrderInfos = new ArrayList<>();
    private LoadMoreAdapter moreAdapter;
    private OrderListAdapter adapter;
    private int page = 1;
    private User userInfo;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("订单");
        initData();
        initEvent();

        return view;
    }

    private void initData() {
        if (mOrderInfos.size() <= 0) {
            layoutNotOrder.setVisibility(View.VISIBLE);
        }
        adapter = new OrderListAdapter(mOrderInfos, getActivity(), this);
        moreAdapter = new LoadMoreAdapter(getActivity(), adapter);
        moreAdapter.setLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(moreAdapter);
    }


    private void initEvent() {
//        recyclerView.addon
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = UserService.getUserInfo(getActivity());
        getOrderList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (userInfo == null) {
                mOrderInfos.clear();
                moreAdapter.loadCompleted();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return;
            }
            getOrderList();
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


        CustomApplication.getRetrofit().postOrderList(page, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealOrderList(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealOrderList(String body) {
        OrderInfos orderInfos = GsonUtil.parseJsonWithGson(body, OrderInfos.class);
        if (orderInfos.getRows() != null && orderInfos.getRows().size() > 0) {
            layoutNotOrder.setVisibility(View.GONE);
        }
        if (page == 1) {
            mOrderInfos.clear();
            moreAdapter.resetLoadState();
        }

        mOrderInfos.addAll(orderInfos.getRows());
        adapter.notifyDataSetChanged();
        moreAdapter.loadCompleted();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_place_order)
    public void onViewClicked() {

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderInfo", mOrderInfos.get(position));
        startActivity(intent);
    }

    @Override
    public void loadMore() {
        page += 1;
        getOrderList();
    }
}
