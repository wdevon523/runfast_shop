package com.gxuc.runfast.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.geocoder.RegeocodeAddress;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressManagerActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.Address;
import com.gxuc.runfast.shop.bean.PurchaseInfo;
import com.gxuc.runfast.shop.bean.PurchaseOrderInfo;
import com.gxuc.runfast.shop.bean.address.AddressInfo;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.TipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class PurchaseFragment extends Fragment implements TipDialog.OnDialogClickListener {

    @BindView(R.id.et_goods)
    EditText etGoods;
    @BindView(R.id.estimate_goods_price)
    TextView estimateGoodsPrice;
    @BindView(R.id.ll_choose_destination)
    LinearLayout llChooseDestination;
    @BindView(R.id.tv_buy_nearby)
    TextView tvBuyNearby;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.tv_service_agreement)
    TextView tvServiceAgreement;
    @BindView(R.id.tv_estimate_time)
    TextView tvEstimateTime;
    @BindView(R.id.tv_confirm_order)
    TextView tvConfirmOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_destination)
    RelativeLayout rlDestination;
    @BindView(R.id.rl_base_price)
    RelativeLayout rlBasePrice;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.cb_agree_deal)
    CheckBox cbAgreeDeal;
    @BindView(R.id.tv_to_address)
    TextView tvToAddress;
    @BindView(R.id.tv_to_name)
    TextView tvToName;
    @BindView(R.id.tv_to_mobile)
    TextView tvToMobile;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_send_price)
    TextView tvSendPrice;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    Unbinder unbinder;

    private String fromType = "SPECIFIED";
    private RegeocodeAddress mRegeocodeAddress;
    private Address mdestinationAddrLat;
    private String destinationAddr = "";
    private int tip = 0;
    private AddressInfo addressInfo;
    private String goodsDescription;
    private TipDialog tipDialog;
    private String lat;
    private String lng;
    private PurchaseInfo purchaseInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        unbinder = ButterKnife.bind(this, view);
        tipDialog = new TipDialog(getContext(), this);
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lng = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvConfirmOrder.setBackgroundResource(cbAgreeDeal.isChecked() ? R.color.text_ff9f14 : R.color.text_cccccc);
        tvConfirmOrder.setEnabled(cbAgreeDeal.isChecked());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_choose_destination, R.id.tv_buy_nearby, R.id.tv_destination, R.id.tv_address, R.id.rl_address, R.id.rl_coupon, R.id.rl_tip, R.id.tv_service_agreement, R.id.tv_confirm_order, R.id.cb_agree_deal, R.id.rl_base_price, R.id.rl_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_destination:
                fromType = "SPECIFIED";
                llChooseDestination.setBackgroundResource(R.drawable.round_biankuang_fc9153);
                tvBuyNearby.setBackgroundResource(R.drawable.round_biankuang_cccccc);
                rlDestination.setVisibility(View.VISIBLE);
                requestPurchaseInfo(TextUtils.equals(fromType, "SPECIFIED"));
                break;
            case R.id.tv_buy_nearby:
                fromType = "NEARBY";
                llChooseDestination.setBackgroundResource(R.drawable.round_biankuang_cccccc);
                tvBuyNearby.setBackgroundResource(R.drawable.round_biankuang_fc9153);
                rlDestination.setVisibility(View.GONE);
                requestPurchaseInfo(TextUtils.equals(fromType, "SPECIFIED"));
                break;
            case R.id.tv_destination:
                Intent intent = new Intent(getContext(), AddressManagerActivity.class);
                startActivityForResult(intent, 1001);
                break;
            case R.id.tv_address:
                Intent data = new Intent(getContext(), AddressSelectActivity.class);
                data.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                startActivityForResult(data, 1002);
                break;
            case R.id.rl_address:
                Intent intentAddr = new Intent(getContext(), AddressSelectActivity.class);
                intentAddr.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                startActivityForResult(intentAddr, 1002);
                break;
            case R.id.rl_coupon:
                break;
            case R.id.rl_tip:
                tipDialog.show();
                break;
            case R.id.tv_service_agreement:
                break;
            case R.id.tv_confirm_order:
                confirmOrder();
                break;
            case R.id.cb_agree_deal:
                tvConfirmOrder.setBackgroundResource(cbAgreeDeal.isChecked() ? R.color.text_ff9f14 : R.color.text_cccccc);
                tvConfirmOrder.setEnabled(cbAgreeDeal.isChecked());
                break;
            case R.id.rl_base_price:
                rlBasePrice.setVisibility(View.GONE);
                break;
            case R.id.rl_bottom:
                rlBasePrice.setVisibility(rlBasePrice.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void confirmOrder() {
        goodsDescription = etGoods.getText().toString().trim();
        if (TextUtils.isEmpty(goodsDescription)) {
            ToastUtil.showToast("请输入你的商品要求");
            return;
        }
        goodsDescription = etGoods.getText().toString();

        if (TextUtils.equals(fromType, "SPECIFIED")) {
            if (mdestinationAddrLat == null) {
                ToastUtil.showToast("请指定地址购买");
                return;
            }
        }

        if (addressInfo == null) {
            ToastUtil.showToast("请选择收货地址");
            return;
        }

        requestConfirmPurchase(TextUtils.equals(fromType, "SPECIFIED"));
    }

    private void requestPurchaseInfo(boolean iSspecified) {

//        goodsDescription = etGoods.getText().toString().trim();
//        if (TextUtils.isEmpty(goodsDescription)) {
//            return;
//        }
        goodsDescription = etGoods.getText().toString();

        if (TextUtils.equals(fromType, "SPECIFIED")) {
            if (mdestinationAddrLat == null) {
                return;
            }
        }

        if (addressInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getPurchaseOrderInfo(fromType, iSspecified ? mdestinationAddrLat.latLng.longitude : 0, iSspecified ? mdestinationAddrLat.latLng.latitude : 0,
                destinationAddr, goodsDescription, addressInfo.getId(), "DAIGOU", lng, lat).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        purchaseInfo = GsonUtil.fromJson(data, PurchaseInfo.class);
                        tvSendPrice.setText("¥ " + purchaseInfo.baseFee / 100);
                        tvTotalPrice.setText(purchaseInfo.deliveryFee / 100 + "元");
                        tvDistance.setText(purchaseInfo.distance + "米");
                        tvEstimateTime.setText("预计" + purchaseInfo.deliveryDuration + "分钟内送达");
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

    private void requestConfirmPurchase(boolean iSspecified) {

        CustomApplication.getRetrofitNew().confirmPurchase(goodsDescription, fromType, iSspecified ? mdestinationAddrLat.latLng.longitude : 0, iSspecified ? mdestinationAddrLat.latLng.latitude : 0, destinationAddr, "DAIGOU", tip, addressInfo.getId(), lng, lat).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        PurchaseOrderInfo purchaseOrderInfo = GsonUtil.fromJson(data, PurchaseOrderInfo.class);
                        Intent intent = new Intent(getContext(), PayChannelActivity.class);
                        intent.putExtra("orderId", purchaseOrderInfo.id);
                        intent.putExtra("price", purchaseOrderInfo.amountPayable / 100);
                        intent.putExtra("orderCode", purchaseOrderInfo.orderNo);
                        intent.putExtra("isPaotui", true);
                        startActivity(intent);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1001) {
            destinationAddr = data.getStringExtra("address");
            mRegeocodeAddress = data.getParcelableExtra("addressInfo");
            mdestinationAddrLat = data.getParcelableExtra("addressLat");
            tvDestination.setText(destinationAddr);
        } else if (requestCode == 1002) {
            addressInfo = data.getParcelableExtra("addressInfo");
            tvToName.setText(addressInfo.getName());
            tvToMobile.setText(addressInfo.getMobile());
            tvToAddress.setText(addressInfo.getUserAddress() + addressInfo.getAddress());
            tvAddress.setVisibility(View.GONE);
            rlAddress.setVisibility(View.VISIBLE);
        }
        requestPurchaseInfo(TextUtils.equals(fromType, "SPECIFIED"));
    }

    @Override
    public void onDialogClick(int tip, boolean isPositive) {
        if (isPositive) {
            this.tip = tip;
        } else {
            this.tip = 0;
        }
        tvTip.setText(tip == 0 ? "加小费抢单更快" : "¥ " + tip);
    }

}
