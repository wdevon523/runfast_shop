package com.gxuc.runfast.shop.activity.ordercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.supportv1.widget.CircleImageView;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.order.OrderInfo;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Devon on 2017/11/14.
 */

public class PaySuccessActivity extends ToolBarActivity {

    @BindView(R.id.iv_business_avater)
    CircleImageView ivBusinessAvater;
    @BindView(R.id.tv_pay_business_name)
    TextView tvPayBusinessName;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.tv_pay_finish)
    TextView tvPayFinish;
    private OrderInfo orderInfo;

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
        orderInfo = getIntent().getParcelableExtra("orderInfo");
        x.image().bind(ivBusinessAvater, orderInfo.getLogo());
        tvPayBusinessName.setText(orderInfo.getBusinessName());
        tvPayPrice.setText("¥" + orderInfo.getPrice());
    }

    @OnClick(R.id.tv_pay_finish)
    public void onViewClicked() {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderInfo", orderInfo);
        startActivity(intent);
        finish();
    }
}
