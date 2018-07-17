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

import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.LoginQucikActivity;
import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.activity.purchases.PurchasesActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.PurchaseInfo;
import com.gxuc.runfast.shop.bean.PurchaseOrderInfo;
import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.GoodsTypeDialog;
import com.gxuc.runfast.shop.view.TimeChooseDialog;
import com.gxuc.runfast.shop.view.TipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Devon on 2018/2/8.
 */

public class DeliveryFragment extends Fragment implements TipDialog.OnDialogClickListener, TimeChooseDialog.OnTimeDialogClickListener, GoodsTypeDialog.OnTypeDialogClickListener {


    @BindView(R.id.tv_purchase)
    TextView tvPurchase;
    @BindView(R.id.tv_nearly_driver)
    TextView tvNearlyDriver;
    @BindView(R.id.tv_where_from)
    TextView tvWhereFrom;
    @BindView(R.id.tv_where_to)
    TextView tvWhereTo;
    @BindView(R.id.tv_choose_time)
    TextView tvChooseTime;
    @BindView(R.id.rl_choose_time)
    RelativeLayout rlChooseTime;
    @BindView(R.id.rl_choose_cargo)
    RelativeLayout rlChooseCargo;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.rl_more_service)
    RelativeLayout rlMoreService;
    @BindView(R.id.tv_cargo_damage)
    TextView tvCargoDamage;
    @BindView(R.id.rl_cargo_damage)
    RelativeLayout rlCargoDamage;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.cb_agree_deal)
    CheckBox cbAgreeDeal;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    Unbinder unbinder;
    @BindView(R.id.tv_from_address)
    TextView tvFromAddress;
    @BindView(R.id.tv_from_name)
    TextView tvFromName;
    @BindView(R.id.tv_from_mobile)
    TextView tvFromMobile;
    @BindView(R.id.rl_where_from)
    RelativeLayout rlWhereFrom;
    @BindView(R.id.rl_base_price)
    RelativeLayout rlBasePrice;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_to_address)
    TextView tvToAddress;
    @BindView(R.id.tv_to_name)
    TextView tvToName;
    @BindView(R.id.tv_to_mobile)
    TextView tvToMobile;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rl_where_to)
    RelativeLayout rlWhereTo;
    @BindView(R.id.tv_send_price)
    TextView tvSendPrice;

    private TipDialog tipDialog;
    private int tip = 0;
    private AddressBean addressInfoFrom;
    private AddressBean addressInfoTo;
    private TimeChooseDialog timeChooseDialog;
    private GoodsTypeDialog goodsTypeDialog;
    private String goodsType = "";
    private String pickTime = SystemUtil.getNowDateFormat();
    private int goodsWeight = 5;
    private String lat;
    private String lng;
    private String addressType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        unbinder = ButterKnife.bind(this, view);
        tipDialog = new TipDialog(getContext(), this);
        timeChooseDialog = new TimeChooseDialog(getContext(), true, this);
        goodsTypeDialog = new GoodsTypeDialog(getContext(), this);
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lng = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvSubmitOrder.setBackgroundResource(cbAgreeDeal.isChecked() ? R.color.text_ff9f14 : R.color.text_cccccc);
        tvSubmitOrder.setEnabled(cbAgreeDeal.isChecked());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_purchase, R.id.tv_where_from, R.id.rl_where_from, R.id.tv_where_to, R.id.rl_where_to, R.id.rl_choose_time, R.id.rl_choose_cargo, R.id.rl_coupon, R.id.rl_more_service, R.id.rl_cargo_damage, R.id.rl_tip, R.id.cb_agree_deal, R.id.tv_submit_order, R.id.rl_base_price, R.id.rl_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_purchase:
                SystemUtil.hideSoftKeyboard(etRemark);
                ((PurchasesActivity) getActivity()).setViewPagerCurrentItem(0);
                break;
            case R.id.tv_where_from:
                if (UserService.getUserInfo(getContext()) == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class).putExtra("isRelogin", true));
                } else {
                    Intent intent = new Intent(getContext(), AddressSelectActivity.class);
                    intent.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                    startActivityForResult(intent, 1001);
                }
                break;
            case R.id.rl_where_from:
                if (UserService.getUserInfo(getContext()) == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class).putExtra("isRelogin", true));
                } else {
                    Intent intentFrom = new Intent(getContext(), AddressSelectActivity.class);
                    intentFrom.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                    startActivityForResult(intentFrom, 1001);
                }
                break;
            case R.id.tv_where_to:
                if (UserService.getUserInfo(getContext()) == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class).putExtra("isRelogin", true));
                } else {
                    Intent data = new Intent(getContext(), AddressSelectActivity.class);
                    data.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                    startActivityForResult(data, 1002);
                }
                break;
            case R.id.rl_where_to:
                if (UserService.getUserInfo(getContext()) == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class).putExtra("isRelogin", true));
                } else {
                    Intent intentTo = new Intent(getContext(), AddressSelectActivity.class);
                    intentTo.putExtra(IntentFlag.KEY, IntentFlag.PURCHASE);
                    startActivityForResult(intentTo, 1002);
                }
                break;
            case R.id.rl_choose_time:
                timeChooseDialog.show();
                break;
            case R.id.rl_choose_cargo:
                goodsTypeDialog.show();
                break;
            case R.id.rl_coupon:
                break;
            case R.id.rl_more_service:
                break;
            case R.id.rl_cargo_damage:
                break;
            case R.id.rl_tip:
                tipDialog.show();
                break;
            case R.id.cb_agree_deal:
                tvSubmitOrder.setBackgroundResource(cbAgreeDeal.isChecked() ? R.color.text_ff9f14 : R.color.text_cccccc);
                tvSubmitOrder.setEnabled(cbAgreeDeal.isChecked());
                break;
            case R.id.tv_submit_order:
                submitOrder();
                break;
            case R.id.rl_base_price:
                rlBasePrice.setVisibility(View.GONE);
                break;
            case R.id.rl_bottom:
                rlBasePrice.setVisibility(rlBasePrice.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void submitOrder() {
        if (addressInfoFrom == null) {
            ToastUtil.showToast("请选择物品发出地");
            return;
        }

        if (addressInfoTo == null) {
            ToastUtil.showToast("请选择物品送达地");
            return;
        }

        if (TextUtils.isEmpty(goodsType)) {
            ToastUtil.showToast("请选择物品类型");
            return;
        }

        requestsubmitOrder();
    }

    private void requestsubmitOrder() {
        String remark = etRemark.getText().toString();
        CustomApplication.getRetrofitNew().submitOrder(goodsType, goodsWeight, pickTime,
                addressInfoFrom.id, addressInfoTo.id, "QUSONGJIAN", tip, lng, lat, remark).enqueue(new MyCallback<String>() {
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
                        intent.putExtra("createTime", purchaseOrderInfo.createTime);
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

    private void requestDeliverInfo() {

        if (addressInfoFrom == null || addressInfoTo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getDeliveryOrderInfo(goodsType, goodsWeight, pickTime, addressInfoFrom.id, addressInfoTo.id, "QUSONGJIAN", lng, lat).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        PurchaseInfo purchaseInfo = GsonUtil.fromJson(data, PurchaseInfo.class);
                        tvSendPrice.setText("¥ " + purchaseInfo.baseFee / 100);
                        tvTotalPrice.setText(purchaseInfo.deliveryFee.divide(new BigDecimal("100")).stripTrailingZeros().toPlainString() + "元");
                        tvDistance.setText(purchaseInfo.distance + "米");
                        tvSendTime.setText("预计" + purchaseInfo.deliveryDuration + "分钟内送达");
                    } else {
                        if (!TextUtils.isEmpty(addressType)) {
                            if (TextUtils.equals("FROM", addressType)) {
                                addressInfoFrom = null;
                                tvWhereFrom.setVisibility(View.VISIBLE);
                                rlWhereFrom.setVisibility(View.GONE);
                            } else {
                                addressInfoTo = null;
                                tvWhereTo.setVisibility(View.VISIBLE);
                                rlWhereTo.setVisibility(View.GONE);
                            }
                        }
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
            addressType = "FROM";
            addressInfoFrom = (AddressBean) data.getSerializableExtra("addressInfo");
            tvWhereFrom.setVisibility(View.GONE);
            rlWhereFrom.setVisibility(View.VISIBLE);
            tvFromName.setText(addressInfoFrom.name);
            tvFromMobile.setText(addressInfoFrom.phone);
            tvFromAddress.setText(addressInfoFrom.userAddress + addressInfoFrom.address);
        } else if (requestCode == 1002) {
            addressType = "TO";
            addressInfoTo = (AddressBean) data.getSerializableExtra("addressInfo");
            tvWhereTo.setVisibility(View.GONE);
            rlWhereTo.setVisibility(View.VISIBLE);
            tvToName.setText(addressInfoTo.name);
            tvToMobile.setText(addressInfoTo.phone);
            tvToAddress.setText(addressInfoTo.userAddress + addressInfoTo.address);
        }
        requestDeliverInfo();
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

    @Override
    public void onTimeDialogClick(String day, String hourMinute) {
        if (TextUtils.isEmpty(day)) {
            pickTime = SystemUtil.getNowDateFormat();
            tvChooseTime.setText("立即取件");
            return;
        }
        if (TextUtils.equals("TODAY", day)) {
            if (TextUtils.equals("立即配送", hourMinute)) {
                pickTime = SystemUtil.getNowDateFormat();
                tvChooseTime.setText("立即取件");
            } else {
                String nowDateFormat = SystemUtil.getNowDateFormat();
                String data = nowDateFormat.substring(0, 11);
                pickTime = data + hourMinute + ":00";
                tvChooseTime.setText(pickTime);
            }
        } else {
            long l = System.currentTimeMillis() + 86400000;
            String time = SystemUtil.getTime(l);
            LogUtil.d("devon", "-----------" + time + "-------------");
            String data = time.substring(0, 11);
            pickTime = data + hourMinute + ":00";
            tvChooseTime.setText(pickTime);
        }
        addressType = "";
        requestDeliverInfo();
    }

    @Override
    public void onTypeDialogClick(String goodsType, int goodsWeight) {
        this.goodsType = goodsType;
        this.goodsWeight = goodsWeight;
        tvGoodsType.setText(TextUtils.isEmpty(goodsType) ? getString(R.string.get_default) : goodsType + " " + goodsWeight + "公斤");
        addressType = "";
        requestDeliverInfo();
    }


}
