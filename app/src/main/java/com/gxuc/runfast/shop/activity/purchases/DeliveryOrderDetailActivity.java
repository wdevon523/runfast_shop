package com.gxuc.runfast.shop.activity.purchases;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.activity.ordercenter.OrderStatusActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.DeliveryOrderDetailInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class DeliveryOrderDetailActivity extends ToolBarActivity {

    @BindView(R.id.tv_delivery_order_status)
    TextView tvDeliveryOrderStatus;
    @BindView(R.id.tv_delivery_order_goods)
    TextView tvDeliveryOrderGoods;
    @BindView(R.id.tv_buy_name)
    TextView tvBuyAddress;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tv_send_name)
    TextView tvSendName;
    @BindView(R.id.tv_send_mobile)
    TextView tvSendMobile;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.rl_question)
    RelativeLayout rlQuestion;
    @BindView(R.id.tv_delivery_price)
    TextView tvDeliveryPrice;
    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.tv_tip_price)
    TextView tvTipPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_actual_pay)
    TextView tvActualPay;
    @BindView(R.id.tv_expected_time)
    TextView tvExpectedTime;
    @BindView(R.id.tv_delivery_distance)
    TextView tvDeliveryDistance;
    @BindView(R.id.tv_delivery_driver)
    TextView tvDeliveryDriver;
    @BindView(R.id.tv_delivery_order_num)
    TextView tvDeliveryOrderNum;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_delivery_order_time)
    TextView tvDeliveryOrderTime;
    @BindView(R.id.tv_delivery_order_remark)
    TextView tvDeliveryOrderRemark;
    @BindView(R.id.rl_delivery_order_status)
    RelativeLayout rlDeliveryOrderStatus;
    private int orderId;
    private DeliveryOrderDetailInfo deliveryOrderDetailInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requesrDeliveryOrderDetail();
    }

    private void requesrDeliveryOrderDetail() {

        CustomApplication.getRetrofitNew().getDeliveryOrderDetail(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        deliveryOrderDetailInfo = JsonUtil.fromJson(data, DeliveryOrderDetailInfo.class);
                        fillView();
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

    private void fillView() {
        tvDeliveryOrderStatus.setText(CustomUtils.getStatusStr(deliveryOrderDetailInfo.status));
        tvDeliveryOrderGoods.setText(TextUtils.equals("DAIGOU", deliveryOrderDetailInfo.type) ? deliveryOrderDetailInfo.goodsDescription : deliveryOrderDetailInfo.goodsType);
        tvBuyAddress.setText(TextUtils.equals("NEARBY", deliveryOrderDetailInfo.fromType) ? "就近购买" : deliveryOrderDetailInfo.fromAddress);
        tvSendAddress.setText(deliveryOrderDetailInfo.toAddress);
        tvSendName.setText(deliveryOrderDetailInfo.toName);
        tvSendMobile.setText(deliveryOrderDetailInfo.toMobile);
        tvDeliveryPrice.setText("¥ " + deliveryOrderDetailInfo.deliveryCost.divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());
        rlTip.setVisibility(deliveryOrderDetailInfo.tip == null || deliveryOrderDetailInfo.tip == 0 ? View.GONE : View.VISIBLE);
        tvTipPrice.setText("¥ " + deliveryOrderDetailInfo.tip);

        tvTotalPrice.setText("总计 ¥" + deliveryOrderDetailInfo.amountPayable.divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());

        String totalPay = "实付 ¥" + (deliveryOrderDetailInfo.amountPayable == null ? "0" : deliveryOrderDetailInfo.amountPayable.divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());
        SpannableStringBuilder builder = new SpannableStringBuilder(totalPay);
        ForegroundColorSpan graySpan = new ForegroundColorSpan(getResources().getColor(R.color.text_fb4e44));
        builder.setSpan(graySpan, 3, totalPay.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvActualPay.setText(builder);
        tvExpectedTime.setText(TextUtils.isEmpty(deliveryOrderDetailInfo.pickTime) ? "立即配送" : deliveryOrderDetailInfo.pickTime);
        tvDeliveryDistance.setText(deliveryOrderDetailInfo.distance + "米");

        tvDeliveryOrderNum.setText(deliveryOrderDetailInfo.orderNo);
        tvDeliveryOrderTime.setText(deliveryOrderDetailInfo.createTime);
        tvDeliveryDriver.setText(deliveryOrderDetailInfo.driverName);
        tvDeliveryOrderRemark.setText(deliveryOrderDetailInfo.remark);
    }

    private void initView() {

    }

    @OnClick({R.id.rl_delivery_order_status, R.id.rl_question, R.id.tv_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_delivery_order_status:
                Intent orderIntent = new Intent(this, OrderStatusActivity.class);
                orderIntent.putExtra("orderId", orderId);
                orderIntent.putExtra("isDelivery", true);
                startActivity(orderIntent);
                break;
            case R.id.rl_question:
                break;
            case R.id.tv_copy:
                ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                systemService.setPrimaryClip(ClipData.newPlainText("text", deliveryOrderDetailInfo.orderNo));
                ToastUtil.showToast("已复制到剪贴板");
                break;
        }
    }
}
