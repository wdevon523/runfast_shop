package com.gxuc.runfast.shop.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.ShopCartGoodAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.CartItemsBean;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.ReasonDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class RefundActivity extends ToolBarActivity {

    @BindView(R.id.rl_refund_reason)
    RelativeLayout rlRefundReason;
    @BindView(R.id.tv_refund_reason)
    TextView tvRefundReason;
    @BindView(R.id.rl_refund_type)
    RelativeLayout rlRefundType;
    @BindView(R.id.iv_shopping_cart_business_logo)
    ImageView ivShoppingCartBusinessLogo;
    @BindView(R.id.tv_shopping_cart_business_name)
    TextView tvShoppingCartBusinessName;
    @BindView(R.id.ll_business)
    LinearLayout llBusiness;
    @BindView(R.id.ll_shopping_cart_business_goods)
    LinearLayout llShoppingCartBusinessGoods;
    @BindView(R.id.cb_shoppingcart_business)
    CheckBox cbShoppingcartBusiness;
    @BindView(R.id.tv_refund_price)
    TextView tvRefundPrice;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_send_price)
    TextView tvSendPrice;
    @BindView(R.id.tv_send_price_note)
    TextView tvSendPriceNote;
    private List<CartItemsBean> cartItems;
    private ShopCartGoodAdapter shopCartGoodAdapter;
    private boolean isAllCheck;
    private int businessId;
    private int orderId;
    private IdentityHashMap<String, String> orderItemIdMap = new IdentityHashMap<>();
    private String deliveryFeeStr;
    private ArrayList<String> reasonList;
    private ReasonDialog reasonDialog;
    private String reason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        businessId = getIntent().getIntExtra("businessId", 0);
        orderId = getIntent().getIntExtra("orderId", 0);
        String deliveryFee = getIntent().getStringExtra("deliveryFee");
        String businessName = getIntent().getStringExtra("businessName");
        String businessImg = getIntent().getStringExtra("businessImg");

        tvShoppingCartBusinessName.setText(businessName);
        x.image().bind(ivShoppingCartBusinessLogo, UrlConstant.ImageBaseUrl + businessImg, NetConfig.optionsLogoImage);

        deliveryFeeStr = "¥" + deliveryFee;
        SpannableString span = new SpannableString(deliveryFeeStr);
        span.setSpan(new StrikethroughSpan(), 0, deliveryFeeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSendPrice.setText(span);

        cartItems = (List<CartItemsBean>) getIntent().getSerializableExtra("cartItems");
        llShoppingCartBusinessGoods.removeAllViews();
        shopCartGoodAdapter = new ShopCartGoodAdapter(this, cartItems, businessId, true);
        for (int i = 0; i < cartItems.size(); i++) {
            llShoppingCartBusinessGoods.addView(shopCartGoodAdapter.getView(i, null, null));
//            if (!cartItems.get(i).checked) {
//                isAllCheck = false;
//            }
        }

        shopCartGoodAdapter.setOnCheckListener(new ShopCartGoodAdapter.OnCheckListener() {
            @Override
            public void onChecked(int businessId, CartItemsBean cartItemsBean, int position) {
//                cartItems.get(position).checked = cartItemsBean.checked;
                cartItems.set(position, cartItemsBean);

                requstRefundMoney();
            }
        });

    }

    private void initData() {
        reasonList = new ArrayList<>();
        reasonList.add("商家少送商品");
        reasonList.add("商家错送商品");
        reasonList.add("商品质量有问题");
        reasonList.add("没有给承诺的优惠");
        reasonList.add("餐品撒漏");
        reasonList.add("其他");
        reasonDialog = new ReasonDialog(this, reasonList, new ReasonDialog.OnReasonDialogClickListener() {
            @Override
            public void onReasonDialogClick(String selectReason) {
                reason = selectReason;
                tvRefundReason.setText(reason);
            }
        });
    }


    private void requstRefundMoney() {
        orderItemIdMap.clear();
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).checked) {
                orderItemIdMap.put(new String("orderItemId"), cartItems.get(i).id);
            }
        }

        isAllCheck = orderItemIdMap.size() == cartItems.size();

        cbShoppingcartBusiness.setChecked(isAllCheck);

        if (orderItemIdMap.size() == 0) {
            SpannableString span = new SpannableString(deliveryFeeStr);
            span.setSpan(new StrikethroughSpan(), 0, deliveryFeeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSendPrice.setText(span);
            tvRefundPrice.setText("¥0");
            return;
        }


        CustomApplication.getRetrofitNew().getRefundPrice(orderId, orderItemIdMap).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    tvRefundPrice.setText("¥" + jsonObject.optString("data"));
                    if (isAllCheck) {
                        SpannableString span = new SpannableString(deliveryFeeStr);
                        span.setSpan(new StyleSpan(Typeface.NORMAL), 0, deliveryFeeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvSendPrice.setText(deliveryFeeStr);
                    } else {
                        SpannableString span = new SpannableString(deliveryFeeStr);
                        span.setSpan(new StrikethroughSpan(), 0, deliveryFeeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvSendPrice.setText(span);
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


    @OnClick({R.id.rl_refund_reason, R.id.rl_refund_type, R.id.cb_shoppingcart_business, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_refund_reason:
                reasonDialog.show();
                reasonDialog.setReason(reason);
                break;
            case R.id.rl_refund_type:
                break;
            case R.id.cb_shoppingcart_business:
//                isAllCheck = true;
                checkAll();
                break;
            case R.id.tv_submit:
                requestSubmitRefund();
                break;
        }
    }

    private void requestSubmitRefund() {

        if (TextUtils.isEmpty(reason)) {
            ToastUtil.showToast("请选择退款原因");
            return;
        }

        orderItemIdMap.clear();
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).checked) {
                orderItemIdMap.put(new String("orderItemId"), cartItems.get(i).id);
            }
        }

        if (orderItemIdMap.size() == 0) {
            ToastUtil.showToast("请选择要退款的商品");
            return;
        }


        CustomApplication.getRetrofitNew().sumbitRefund(orderId, orderItemIdMap, reason).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("退款申请已提交，等待商家处理");
                        finish();
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

    private void checkAll() {
        for (int i = 0; i < cartItems.size(); i++) {
            cartItems.get(i).checked = cbShoppingcartBusiness.isChecked();
            ((CheckBox) llShoppingCartBusinessGoods.getChildAt(i).findViewById(R.id.cb_shoppingcart_goods)).setChecked(cbShoppingcartBusiness.isChecked());
        }
        requstRefundMoney();
    }
}
