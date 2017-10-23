package com.gxuc.runfast.shop.activity;

import android.os.Bundle;

import com.gxuc.runfast.shop.R;

import butterknife.ButterKnife;

/**
 * 支付详情
 */
public class PayDetailActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
        ButterKnife.bind(this);
    }
}
