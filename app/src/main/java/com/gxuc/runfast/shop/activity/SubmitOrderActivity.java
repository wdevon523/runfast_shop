package com.gxuc.runfast.shop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supportv1.utils.DeviceUtil;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.ColorUtil;
import com.gxuc.runfast.shop.view.CustomScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitOrderActivity extends ToolBarActivity {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_background)
    View viewBackground;
    @BindView(R.id.scroll_view)
    CustomScrollView scrollView;
    @BindView(R.id.tv_take_yourself)
    TextView tvTakeYourself;
    @BindView(R.id.tv_send_address_detail)
    TextView tvSendAddressDetail;
    @BindView(R.id.tv_send_name_and_mobile)
    TextView tvSendNameAndMobile;
    @BindView(R.id.ll_send_address)
    LinearLayout llSendAddress;
    @BindView(R.id.text_send_time)
    TextView textSendTime;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.rl_send_time)
    RelativeLayout rlSendTime;
    @BindView(R.id.ll_diver_send)
    LinearLayout llDiverSend;
    @BindView(R.id.tv_diver_send)
    TextView tvDiverSend;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.tv_distance_to_you)
    TextView tvDistanceToYou;
    @BindView(R.id.tv_take_yourself_time)
    TextView tvTakeYourselfTime;
    @BindView(R.id.ll_take_yourself_time)
    LinearLayout llTakeYourselfTime;
    @BindView(R.id.tv_take_yourself_mobile)
    TextView tvTakeYourselfMobile;
    @BindView(R.id.ll_take_yourself_mobile)
    LinearLayout llTakeYourselfMobile;
    @BindView(R.id.cb_agree_deal)
    CheckBox cbAgreeDeal;
    @BindView(R.id.ll_take_yourself)
    LinearLayout llTakeYourself;
    @BindView(R.id.cb_eat_here)
    CheckBox cbEatHere;
    @BindView(R.id.rl_eat_here)
    RelativeLayout rlEatHere;
    @BindView(R.id.cb_take_out)
    CheckBox cbTakeOut;
    @BindView(R.id.rl_eat_take_out)
    RelativeLayout rlEatTakeOut;
    @BindView(R.id.iv_business_logo)
    ImageView ivBusinessLogo;
    @BindView(R.id.tv_business_name)
    TextView tvBusinessName;
    @BindView(R.id.iv_business_to_take)
    ImageView ivBusinessToTake;
    @BindView(R.id.ll_contain_goods)
    LinearLayout llContainGoods;
    @BindView(R.id.tv_send_price)
    TextView tvSendPrice;
    @BindView(R.id.tv_enjoy_act)
    TextView tvEnjoyAct;
    @BindView(R.id.tv_enjoy_price)
    TextView tvEnjoyPrice;
    @BindView(R.id.tv_red_packet)
    TextView tvRedPacket;
    @BindView(R.id.rl_red_packet)
    RelativeLayout rlRedPacket;
    @BindView(R.id.tv_cash_coupon)
    TextView tvCashCoupon;
    @BindView(R.id.rl_cash_coupon)
    RelativeLayout rlCashCoupon;
    @BindView(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @BindView(R.id.tv_sub_price)
    TextView tvSubPrice;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.rl_remark)
    RelativeLayout rlRemark;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_discount_price_detail)
    TextView tvDiscountPriceDetail;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_orders);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void setListener() {
        scrollView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                LogUtil.d("wdevon", "-------scrollY-------" +  scrollY);
                handleTitleBarColorEvaluate(-scrollY);
            }
        });
    }

    private void handleTitleBarColorEvaluate(int scrollY) {
        float fraction;
        if (scrollY > 0) {
            fraction = 1f - scrollY * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlTitle.setAlpha(fraction);
            viewBackground.setAlpha(fraction);
            return;
        }

        float space = Math.abs(scrollY) * 1f;
//        fraction = space / (homeMarginHeight - titleViewHeight);
        fraction = space / 100;
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlTitle.setAlpha(1f);
        viewBackground.setAlpha(1f);

//        if (fraction >= 1f || isStickyTop) {
        if (fraction >= 1f) {
//            isStickyTop = true;
//            viewTitleBg.setAlpha(0f);
//            viewActionMoreBg.setAlpha(0f);
            rlTitle.setBackgroundColor(getResources().getColor(R.color.bg_f3f2f2));
            viewBackground.setBackgroundColor(getResources().getColor(R.color.bg_f3f2f2));
//            llTopAddress.setVisibility(View.GONE);
        } else {
//            viewTitleBg.setAlpha(1f - fraction);
//            viewActionMoreBg.setAlpha(1f - fraction);
            rlTitle.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.bg_fba42a, R.color.bg_f3f2f2));
            viewBackground.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.bg_fba42a, R.color.bg_f3f2f2));
//            llTopAddress.setVisibility(View.VISIBLE);
        }


    }

    @OnClick({R.id.tv_take_yourself, R.id.tv_diver_send, R.id.tv_send_address_detail, R.id.ll_send_address, R.id.rl_send_time, R.id.ll_take_yourself_time, R.id.cb_agree_deal, R.id.ll_take_yourself, R.id.cb_eat_here, R.id.rl_eat_here, R.id.cb_take_out, R.id.rl_eat_take_out, R.id.rl_red_packet, R.id.rl_cash_coupon, R.id.rl_remark, R.id.tv_submit_order, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_take_yourself:
                llDiverSend.setVisibility(View.GONE);
                llTakeYourself.setVisibility(View.VISIBLE);
                rlEatHere.setVisibility(View.VISIBLE);
                rlEatTakeOut.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_diver_send:
                llDiverSend.setVisibility(View.VISIBLE);
                llTakeYourself.setVisibility(View.GONE);
                rlEatHere.setVisibility(View.GONE);
                rlEatTakeOut.setVisibility(View.GONE);
                break;
            case R.id.tv_send_address_detail:
                break;
            case R.id.ll_send_address:
                break;
            case R.id.rl_send_time:
                break;
            case R.id.ll_take_yourself_time:
                break;
            case R.id.cb_agree_deal:
                break;
            case R.id.ll_take_yourself:
                break;
            case R.id.cb_eat_here:
                break;
            case R.id.rl_eat_here:
                break;
            case R.id.cb_take_out:
                break;
            case R.id.rl_eat_take_out:
                break;
            case R.id.rl_red_packet:
                break;
            case R.id.rl_cash_coupon:
                break;
            case R.id.rl_remark:
                break;
            case R.id.tv_submit_order:
                break;
            case R.id.iv_back:
                break;
        }
    }
}
