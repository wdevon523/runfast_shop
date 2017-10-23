package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.activity.usercenter.CouponActivity;
import com.gxuc.runfast.shop.adapter.moneyadapter.BalanceProductAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.address.AddressInfo;
import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.bean.coupon.CouponBeans;
import com.gxuc.runfast.shop.bean.order.GoodsSellRecordChildren;
import com.gxuc.runfast.shop.bean.order.ShoppingCartGoodsInfo;
import com.gxuc.runfast.shop.bean.redpackage.RedPackages;
import com.gxuc.runfast.shop.config.IntentConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.activity.ordercenter.OrderRemarkActivity;
import com.gxuc.runfast.shop.bean.BalanceInfo;
import com.gxuc.runfast.shop.bean.address.AddressInfos;
import com.gxuc.runfast.shop.bean.order.ShoppingCartInfo;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.order.OrderCodeInfo;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.util.CustomToast;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 确认订单界面
 */
public class ConfirmOrderActivity extends ToolBarActivity implements Callback<String> {

    private static final int COUPON_CODE = 1000;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_red_packet)
    TextView tvRedPacket;
    @BindView(R.id.tv_cash_coupon)
    TextView tvCashCoupon;
    @BindView(R.id.recycler_product_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @BindView(R.id.tv_sub_price)
    TextView tvSubPrice;
    @BindView(R.id.tv_order_remark)
    TextView mTvOrderRemark;
    @BindView(R.id.layout_address_user_info)
    LinearLayout layoutAddressUserInfo;
    @BindView(R.id.layout_coupons)
    LinearLayout layoutCoupons;
    @BindView(R.id.iv_business_icon)
    ImageView ivBusinessIcon;
    @BindView(R.id.tv_business_name)
    TextView tvBusinessName;

    private List<BalanceInfo> balanceInfoList = new ArrayList<>();
    private Integer mAgentId;
    private Integer mBusinessId;
    private int mNetType;
    private Integer mAddressId;
    private BigDecimal mDecimalCoupon;
    private GoodsSellRecordChildren mChildren;
    private List<GoodsSellRecordChildren> mGoodsSellRecordChildrens;
    private Intent mIntent;
    private String mSubtract;
    private int businessId;
    private ShoppingCartInfo shoppingCartInfo;
    private BalanceProductAdapter adapter;
    private List<ShoppingCartGoodsInfo> shoppingCartGoodsList;
    private ShoppingCartGoodsInfo shoppingCartGoodsInfo1;
    private ShoppingCartGoodsInfo shoppingCartGoodsInfo2;
    private ShoppingCartGoodsInfo shoppingCartGoodsInfo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initData();
        getRedData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestShoppingCart();
    }

    private void requestShoppingCart() {
        CustomApplication.getRetrofit().getShoppingCar(businessId).enqueue(new MyCallback<String>() {

            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    shoppingCartInfo = GsonUtil.fromJson(response.body(), ShoppingCartInfo.class);
                    if (shoppingCartInfo.rows != null && shoppingCartInfo.rows.size() > 0) {
                        shoppingCartGoodsList = shoppingCartInfo.rows;
                        fillInfo();
                    }
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void fillInfo() {
        shoppingCartGoodsInfo1 = new ShoppingCartGoodsInfo();
        shoppingCartGoodsInfo1.goodsSellName = "配送费";
        shoppingCartGoodsInfo1.num = "";
        shoppingCartGoodsInfo1.price = shoppingCartInfo.totalpay;
        shoppingCartGoodsList.add(shoppingCartGoodsInfo1);

        shoppingCartGoodsInfo2 = new ShoppingCartGoodsInfo();
        shoppingCartGoodsInfo2.goodsSellName = "包装费";
        shoppingCartGoodsInfo2.num = "";
        shoppingCartGoodsInfo2.price = shoppingCartInfo.tpacking;
        shoppingCartGoodsList.add(shoppingCartGoodsInfo2);

        shoppingCartGoodsInfo3 = new ShoppingCartGoodsInfo();
        shoppingCartGoodsInfo3.goodsSellName = "优惠券";
        shoppingCartGoodsInfo3.num = "";
        shoppingCartGoodsInfo3.price = shoppingCartInfo.disprice;
        shoppingCartGoodsList.add(shoppingCartGoodsInfo3);

        adapter.setData(shoppingCartGoodsList);

        tvOrderPrice.setText(shoppingCartInfo.totalpay);
        tvCouponPrice.setText(shoppingCartInfo.disprice);
        tvSubPrice.setText(shoppingCartInfo.prices);
        tvTotalPrice.setText(shoppingCartInfo.prices);
    }

    private void initData() {

        businessId = getIntent().getIntExtra("businessId", 0);
        adapter = new BalanceProductAdapter(shoppingCartGoodsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }


    private void getRedData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        mNetType = 1;
        CustomApplication.getRetrofit().postRedPackage(businessId).enqueue(this);
    }

    /**
     * 获取优惠券
     */
    private void getCouponData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        mNetType = 2;
        CustomApplication.getRetrofit().GetMyCoupan(0).enqueue(this);
    }

    private void getAddressData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        mNetType = 3;
        CustomApplication.getRetrofit().postListAddress().enqueue(this);
    }

    @OnClick({R.id.layout_user_address, R.id.layout_pay_mode, R.id.layout_red_packet, R.id.layout_cash_coupon, R.id.layout_flavor, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_user_address:
                Intent intent = new Intent(this, AddressSelectActivity.class);
                intent.putExtra("bid", businessId);
                intent.setFlags(IntentFlag.ORDER_ADDRESS);
                startActivityForResult(intent, IntentConfig.REQUEST_CODE);
                break;
            case R.id.layout_pay_mode:

                break;
            case R.id.layout_red_packet:
                Intent data = new Intent(this, CouponActivity.class);
                data.putExtra("isChoose", true);
                data.putExtra("bid", businessId);
                startActivityForResult(data, IntentConfig.REQUEST_CODE);
                break;
            case R.id.layout_cash_coupon:
//                startActivity(new Intent(this, CashCouponActivity.class));
                startActivity(new Intent(this, CouponActivity.class).putExtra("isChoose", true));
                break;
            case R.id.layout_flavor:
                Intent mIntent = new Intent(this, OrderRemarkActivity.class);
                if (mTvOrderRemark.getText().toString().equals("口味备注等要求（选填）")) {
                    startActivityForResult(mIntent, IntentConfig.REQUEST_CODE);
                } else {
                    mIntent.putExtra("remark_data", mTvOrderRemark.getText().toString());
                    startActivityForResult(mIntent, IntentConfig.REQUEST_CODE);
                }
                break;
            case R.id.tv_pay:
                toPay();
                break;
        }
    }

    /**
     *
     */
    private void toPay() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo != null) {
            mNetType = 4;
            mGoodsSellRecordChildrens.add(mChildren);
            Gson gson = new Gson();
            String goodsJson = gson.toJson(mGoodsSellRecordChildrens);
            mSubtract = shoppingCartInfo.prices;
            //TODO  参数含义
            CustomApplication.getRetrofit().createOrder(businessId, mAddressId, "0", "0",
                    mSubtract,

                    "0.01",
                    "", goodsJson).enqueue(this);
        }
    }

    /**
     * @param addr
     * @param isShow 是否有地址
     */
    private void updateAddr(AddressInfo addr, boolean isShow) {
        if (isShow) {
            tvUserAddress.setText(addr.getUserAddress());
            tvUserName.setText(addr.getName());
            tvUserPhone.setText(addr.getPhone());
            layoutAddressUserInfo.setVisibility(View.VISIBLE);
        } else {
            tvUserAddress.setText("还没有地址，点击添加");
            layoutAddressUserInfo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != IntentConfig.REQUEST_CODE) {
            return;
        }
        if (resultCode == IntentConfig.REMARK_RESULT_CODE) {
            if (data != null)
                mTvOrderRemark.setText(data.getStringExtra("order_remark"));
        } else if (resultCode == IntentConfig.ADDRESS_SELECT) {
            AddressInfo addressInfo = data.getParcelableExtra("addressInfo");
            updateAddr(addressInfo, true);
        } else if (resultCode == IntentConfig.COUPON_SELECT) {
            CouponBean couponBean = (CouponBean) data.getSerializableExtra("coupon");
            updateCoupon(couponBean);
        }
    }

    private void updateCoupon(CouponBean couponBean) {

    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        String data = response.body();
        if (response.isSuccessful()) {
            ResolveData(data);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        CustomToast.INSTANCE.showToast(this, "网络异常");
    }

    /**
     * 解析数据
     *
     * @param data
     */
    private void ResolveData(String data) {

        if (mNetType == 1) {
            if (!TextUtils.isEmpty(data)) {
                RedPackages redPackages = GsonUtil.parseJsonWithGson(data, RedPackages.class);
                if (redPackages.getRedPackets() != null) {
                    int mSize = redPackages.getRedPackets().size();
                    int number = mSize > 0 ? mSize : 0;
                    tvRedPacket.setText("可用红包" + number + "个");
                } else {
                    tvRedPacket.setText("暂无可用红包");
                }
            } else {
                tvRedPacket.setText("暂无可用红包");
            }
            getCouponData();
        }

        if (mNetType == 2) {
            if (!TextUtils.isEmpty(data)) {
                CouponBeans couponBeans = GsonUtil.parseJsonWithGson(data, CouponBeans.class);
                if (couponBeans.getRows() != null) {
                    int number = couponBeans.getRows().size() > 0 ? couponBeans.getRows().size() : 0;
                    tvCashCoupon.setText("可用优惠券" + number + "个");
                    return;
                }
                tvCashCoupon.setText("暂无可用优惠券");
            } else {
                tvCashCoupon.setText("暂无可用优惠券");
            }
            getAddressData();
        }

        if (mNetType == 3) {
            AddressInfos addressInfos = GsonUtil.parseJsonWithGson(data, AddressInfos.class);
            if (addressInfos != null) {
                if (addressInfos.getRows() != null && addressInfos.getRows().size() > 0) {
                    updateAddr(addressInfos.getRows().get(0), true);
                    mAddressId = addressInfos.getRows().get(0).getId();
                } else {
                    updateAddr(new AddressInfo(), false);
                }
            }
        }

        if (mNetType == 4) {
            OrderCodeInfo codeInfo = GsonUtil.parseJsonWithGson(data, OrderCodeInfo.class);
            if (codeInfo != null) {
                if (codeInfo.isSuccess()) {
                    mIntent = new Intent(this, PayChannelActivity.class);
                    mIntent.putExtra("orderId", codeInfo.getId());
                    mIntent.putExtra("price", codeInfo.getGoodsSellRecord().getTotalpay());
                    startActivity(mIntent);
                } else {
                    CustomToast.INSTANCE.showToast(this, codeInfo.getMsg());
                }
            } else {
                CustomToast.INSTANCE.showToast(this, "下单失败，请重新选择商品");
            }
        }
    }
}
