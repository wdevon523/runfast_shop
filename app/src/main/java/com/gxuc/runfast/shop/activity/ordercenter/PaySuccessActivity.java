package com.gxuc.runfast.shop.activity.ordercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.order.OrderDetail;
import com.gxuc.runfast.shop.bean.order.OrderInfo;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Devon on 2017/11/14.
 */

public class PaySuccessActivity extends ToolBarActivity {

    @BindView(R.id.iv_business_avater)
    ImageView ivBusinessAvater;
    @BindView(R.id.tv_pay_business_name)
    TextView tvPayBusinessName;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.tv_pay_finish)
    TextView tvPayFinish;
    private int orderId;
    private double price;
    private String businessName;
    private String logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
//        orderInfo = getIntent().getParcelableExtra("orderInfo");
//        orderDetailInfo = (OrderDetail) getIntent().getSerializableExtra("orderDetail");
        orderId = getIntent().getIntExtra("orderId", 0);
        price = getIntent().getDoubleExtra("price", 0);
        businessName = getIntent().getStringExtra("businessName");
        logo = getIntent().getStringExtra("logo");

        x.image().bind(ivBusinessAvater,  UrlConstant.ImageBaseUrl + logo);
        tvPayBusinessName.setText(businessName);
        tvPayPrice.setText("¥" + (price));
    }

    @OnClick(R.id.tv_pay_finish)
    public void onViewClicked() {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("isFromePayFinish", true);
        startActivity(intent);
        finish();
    }
}
