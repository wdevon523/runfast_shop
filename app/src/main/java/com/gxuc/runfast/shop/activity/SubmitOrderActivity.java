package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.AMapUtils;
import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ordercenter.OrderRemarkActivity;
import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.activity.usercenter.CouponActivity;
import com.gxuc.runfast.shop.adapter.OrderGoodsNewAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.OrderTimeBean;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.ValidActivityListBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ColorUtil;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.CenteredImageSpan;
import com.gxuc.runfast.shop.view.CustomScrollView;
import com.gxuc.runfast.shop.view.OrderTimeChooseDialog;
import com.gxuc.runfast.shop.view.TimeChooseDialog;
import com.hedan.textdrawablelibrary.TextViewDrawable;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

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
    @BindView(R.id.text_take_yourself)
    TextView textTakeYourself;
    @BindView(R.id.tv_just_send_address_tag)
    TextView tvJustSendAddressTag;
    @BindView(R.id.tv_just_send_address_detail)
    TextViewDrawable tvJustSendAddressDetail;
    @BindView(R.id.tv_address_tag)
    TextView tvSendAddressTag;
    @BindView(R.id.tv_send_address_detail)
    TextViewDrawable tvSendAddressDetail;
    @BindView(R.id.tv_just_send_name_and_mobile)
    TextView tvJustSendNameAndMobile;
    @BindView(R.id.tv_send_name_and_mobile)
    TextView tvSendNameAndMobile;
    @BindView(R.id.ll_send_address)
    LinearLayout llSendAddress;
    @BindView(R.id.text_send_time)
    TextView textSendTime;
    @BindView(R.id.tv_just_send_time)
    TextView tvJustSendTime;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.rl_just_send_time)
    RelativeLayout rlJustSendTime;
    @BindView(R.id.rl_send_time)
    RelativeLayout rlSendTime;
    @BindView(R.id.ll_just_diver_send)
    LinearLayout llJustDiverSend;
    @BindView(R.id.ll_diver_send)
    LinearLayout llDiverSend;
    @BindView(R.id.tv_diver_send)
    TextView tvDiverSend;
    @BindView(R.id.text_diver_send)
    TextView textDiverSend;
    @BindView(R.id.tv_take_address)
    TextView tvTakeAddress;
    @BindView(R.id.tv_distance_to_you)
    TextView tvDistanceToYou;
    @BindView(R.id.tv_take_yourself_time)
    TextView tvTakeYourselfTime;
    @BindView(R.id.ll_take_yourself_time)
    LinearLayout llTakeYourselfTime;
    @BindView(R.id.et_take_yourself_mobile)
    EditText etTakeYourselfMobile;
    @BindView(R.id.tv_look_map)
    TextView tvLookMap;
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
    @BindView(R.id.tv_business_is_charge)
    TextView tvBusinessIsCharge;
    @BindView(R.id.tv_business_to_take)
    TextView tvBusinessToTake;
    @BindView(R.id.ll_contain_goods)
    LinearLayout llContainGoods;
    @BindView(R.id.tv_package_price)
    TextView tvPackagePrice;
    @BindView(R.id.tv_eat_save_pack_price)
    TextView tvEatSavePackPrice;
    @BindView(R.id.tv_send_price)
    TextView tvSendPrice;
    @BindView(R.id.ll_contain_act)
    LinearLayout llContainAct;
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
    private int businessId;
    private String lat;
    private String lng;
    private int REQUESTCODE_ADDRESS = 1001;
    private int REQUESTCODE_REMARK = 1002;
    private int REQUESTCODE_RED_PACKAGE = 1003;
    private int REQUESTCODE_COUPON = 1004;
    private ShopCartBean shopCartBean;
    private String nowTime = SystemUtil.getNowDateFormat();
    private String sendTime;
    private String takeTime;
    private OrderTimeChooseDialog sendTimeChooseDialog;
    private TimeChooseDialog takeimeChooseDialog;
    private String orderRemark;
    private boolean suportSelf;
    private int eatInBusiness;
    private HashMap<String, String> paramMap;
    private boolean isFromCart;
    private JSONObject paramJson;
    private String userRedPrice;
    private String userCouponPrice;
    private UserInfo userInfo;
    private boolean isSetText;
    private boolean isSuportSelf;
    private BigDecimal totalPackageFee;
    private BigDecimal deliveryFee;
    private boolean isFirst = true;
    private BusinessNewDetail businessInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_orders);
        ButterKnife.bind(this);
        initView();
        initData();
        initParam();
        setListener();
    }

    private void initParam() {
        paramJson = new JSONObject();
        paramMap = new HashMap<>();
        try {
            paramMap.put("businessId", businessId + "");
            paramJson.put("businessId", businessId + "");
            paramMap.put("userLng", lng);
            paramJson.put("userLng", lng);
            paramMap.put("userLat", lat);
            paramJson.put("userLat", lat);
            paramMap.put("toAddressId", "");
            paramJson.put("toAddressId", "");
            paramMap.put("userRedId", "");
            paramJson.put("userRedId", "");
            paramMap.put("suportSelf", "");
            paramJson.put("suportSelf", "");
            paramMap.put("bookTime", sendTime);
            paramJson.put("bookTime", sendTime);
//            paramMap.put("selfTime", takeTime);
//            paramJson.put("selfTime", takeTime);
            paramMap.put("selfMobile", userInfo.mobile);
            paramJson.put("selfMobile", userInfo.mobile);
            paramMap.put("eatInBusiness", "0");
            paramJson.put("eatInBusiness", "0");
            paramMap.put("userCouponId", "");
            paramJson.put("userCouponId", "");
            paramMap.put("remark", "");
            paramJson.put("remark", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isFromCart) {
            SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
            requestFromCartOrderInfo();
        } else {
            requestGetOrderInfo();
        }
    }

    private void requestFromCartOrderInfo() {

        CustomProgressDialog.startProgressDialog(this);
        CustomApplication.getRetrofitNew().fillInDiy().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealShopCartInfo(jsonObject);
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                CustomProgressDialog.stopProgressDialog();
            }
        });

    }

    private void initView() {
        StatusUtil.setUseStatusBarColor(this, getResources().getColor(R.color.bg_fba42a));
        StatusUtil.setSystemStatus(this, false, true);
    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);

        businessId = getIntent().getIntExtra("businessId", 0);
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lng = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        isFromCart = getIntent().getBooleanExtra("isFromCart", false);
        isSuportSelf = getIntent().getBooleanExtra("suportSelf", false);


        int mHour = Integer.valueOf(nowTime.substring(11, 13)) + 1;
        if (mHour < 10) {
            sendTime = nowTime.substring(0, 11) + "0" + mHour + ":00:00";
            takeTime = nowTime.substring(0, 11) + "0" + mHour + ":00:00";
        } else {
            sendTime = nowTime.substring(0, 11) + mHour + ":00:00";
            takeTime = nowTime.substring(0, 11) + mHour + ":00:00";
        }

        llJustDiverSend.setVisibility(isSuportSelf ? View.GONE : View.VISIBLE);
        llDiverSend.setVisibility(isSuportSelf ? View.VISIBLE : View.GONE);
        tvTakeYourselfTime.setText("大约 " + takeTime.substring(11, 16) + " 到店");
        etTakeYourselfMobile.setText(userInfo.mobile);
        requestBusinessDetail();
//        SharePreferenceUtil.getInstance().putBooleanValue("isFromCart", false);
    }


    private void requestBusinessDetail() {

        CustomApplication.getRetrofitNew().getBusinessDetail(businessId, Double.valueOf(lng), Double.valueOf(lat)).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        businessInfo = JsonUtil.fromJson(data, BusinessNewDetail.class);
                        initTimeDialog();
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


    private void requestGetOrderInfo() {
        CustomProgressDialog.startProgressDialog(this);

        CustomApplication.getRetrofitNew().submitBusinessShopCart(paramMap).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {

                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealShopCartInfo(jsonObject);
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                CustomProgressDialog.stopProgressDialog();
            }
        });
    }

    private void dealShopCartInfo(JSONObject jsonObject) {
        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
            shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
            fillOrderView();
            if (shopCartBean.cartItems != null && shopCartBean.cartItems.size() > 0) {
                dealOrderGoods();
            }

            try {
                paramMap.put("bookTime", sendTime);
                paramJson.put("bookTime", sendTime);
                paramMap.put("selfTime", takeTime);
                paramJson.put("selfTime", takeTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (shopCartBean.toAddressId != null) {
//                paramMap.put("toAddressId", shopCartBean.toAddressId + "");
//            }
        }

    }

    private void initTimeDialog() {
        sendTimeChooseDialog = new OrderTimeChooseDialog(this, new OrderTimeChooseDialog.OnTimeDialogClickListener() {
            @Override
            public void onTimeDialogClick(String day, OrderTimeBean orderTimeBean) {
                if (TextUtils.isEmpty(day) || TextUtils.equals("立即配送", orderTimeBean.hourMinute)) {
                    sendTime = SystemUtil.getNowDateFormat();
                    tvSendTime.setText("立即配送");
                    tvJustSendTime.setText("立即配送");
                    paramMap.put("bookTime", shopCartBean.disTime);
                    try {
                        paramJson.put("bookTime", shopCartBean.disTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (TextUtils.equals("TODAY", day)) {
                    String nowDateFormat = SystemUtil.getNowDateFormat();
                    String data = nowDateFormat.substring(0, 11);
                    sendTime = data + orderTimeBean.hourMinute + ":00";
                    tvSendTime.setText("大约 " + sendTime.substring(11, 16) + " 送达");
                    tvJustSendTime.setText("大约 " + sendTime.substring(11, 16) + " 送达");
                } else {
                    long l = System.currentTimeMillis() + 86400000;
                    String time = SystemUtil.getTime(l);
                    LogUtil.d("devon", "-----------" + time + "-------------");
                    String data = time.substring(0, 11);
                    sendTime = data + orderTimeBean.hourMinute + ":00";
                    tvSendTime.setText("大约 " + sendTime.substring(11, 16) + " 送达");
                    tvJustSendTime.setText("大约 " + sendTime.substring(11, 16) + " 送达");
                }
                paramMap.put("bookTime", sendTime);
                try {
                    paramJson.put("bookTime", sendTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isFromCart) {
                    SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                    requestFromCartOrderInfo();
                } else {
                    requestGetOrderInfo();
                }

            }
        });

        takeimeChooseDialog = new TimeChooseDialog(this, false, new TimeChooseDialog.OnTimeDialogClickListener() {
            @Override
            public void onTimeDialogClick(String day, String hourMinute) {
                if (TextUtils.isEmpty(day) || TextUtils.equals("立即配送", hourMinute)) {
                    takeTime = SystemUtil.getNowDateFormat();
                    tvTakeYourselfTime.setText("大约 " + takeTime.substring(11, 16) + " 到店");
                    paramMap.put("selfTime", takeTime);
                    try {
                        paramJson.put("selfTime", takeTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (TextUtils.equals("TODAY", day)) {
                    String nowDateFormat = SystemUtil.getNowDateFormat();
                    String data = nowDateFormat.substring(0, 11);
                    takeTime = data + hourMinute + ":00";
                    tvTakeYourselfTime.setText("大约 " + takeTime.substring(11, 16) + " 到店");
                } else {
                    long l = System.currentTimeMillis() + 86400000;
                    String time = SystemUtil.getTime(l);
                    LogUtil.d("devon", "-----------" + time + "-------------");
                    String data = time.substring(0, 11);
                    takeTime = data + hourMinute + ":00";
                    tvTakeYourselfTime.setText("大约" + takeTime.substring(11, 16) + "到店");
                }

                paramMap.put("selfTime", takeTime);
                try {
                    paramJson.put("selfTime", takeTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showActImage(TextView tvEnjoyAct, ValidActivityListBean validActivityListBean) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券,7免部分配送费,8新用户立减活动,9首单立减活动,10商户红包,11下单返红包,12 通用红包 代理商红包
        switch (validActivityListBean.activityType) {
            case 1:
                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_reduce), null, null, null);
                tvEnjoyAct.setText("满减优惠");
                break;
//            case 2:
//                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_fracture), null, null, null);
//                tvEnjoyAct.setText("打折优惠");
//                break;
//            case 4:
//                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_special), null, null, null);
//                tvEnjoyAct.setText("特价优惠");
//                break;
            case 5:
                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_free), null, null, null);
                tvEnjoyAct.setText("减免运费");
                break;
            case 7:
                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_free), null, null, null);
                tvEnjoyAct.setText("减免运费");
                break;
//            case 8:
//                tvEnjoyAct.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_new), null, null, null);
//                tvEnjoyAct.setText("首单优惠");
//                break;
        }
    }

    private void fillOrderView() {
        if (TextUtils.isEmpty(shopCartBean.userAddressId)) {
            if (isSuportSelf) {
                tvSendAddressDetail.setText("请选择收货地址");
                tvSendNameAndMobile.setVisibility(View.GONE);
            } else {
                tvJustSendAddressDetail.setText("请选择收货地址");
                tvJustSendNameAndMobile.setVisibility(View.GONE);
            }
        } else {
            String gender;
            if (shopCartBean.userAddressGender == null) {
                gender = "先生";
            } else {
                gender = shopCartBean.userAddressGender == 0 ? "女士" : "先生";
            }

            if (isSuportSelf) {
                tvSendAddressDetail.setText(shopCartBean.userAddress + shopCartBean.address);
                tvSendNameAndMobile.setText(shopCartBean.userName + " " + gender + "     " + shopCartBean.userPhone);
                tvSendNameAndMobile.setVisibility(View.VISIBLE);
            } else {
                tvJustSendAddressDetail.setText(shopCartBean.userAddress + shopCartBean.address);
                tvJustSendNameAndMobile.setText(shopCartBean.userName + " " + gender + "     " + shopCartBean.userPhone);
                tvJustSendNameAndMobile.setVisibility(View.VISIBLE);
            }
        }

        tvPrompt.setVisibility(TextUtils.isEmpty(shopCartBean.cartTips) ? View.GONE : View.VISIBLE);
        tvPrompt.setText(shopCartBean.cartTips);
//        if (isSuportSelf) {
//            tvSendAddressTag.setVisibility(shopCartBean.userAddressTag == null || shopCartBean.userAddressTag < 0 || shopCartBean.userAddressTag > 3 ? View.GONE : View.VISIBLE);
//        } else {
//            tvJustSendAddressTag.setVisibility(shopCartBean.userAddressTag == null || shopCartBean.userAddressTag < 0 || shopCartBean.userAddressTag > 3 ? View.GONE : View.VISIBLE);
//        }

        if (shopCartBean.userAddressTag != null) {
            if (shopCartBean.userAddressTag == 1) {
                CenteredImageSpan span = new CenteredImageSpan(this, R.drawable.icon_home);
                SpannableString ss = new SpannableString("  " + shopCartBean.userAddress + shopCartBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                tvSendAddressDetail.setText(ss);
                tvJustSendAddressDetail.setText(ss);

//                tvSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_home), null, null, null);
//                tvSendAddressDetail.setCompoundDrawablePadding(8);

//                tvJustSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_home), null, null, null);
//                tvJustSendAddressDetail.setCompoundDrawablePadding(8);
            } else if (shopCartBean.userAddressTag == 2) {
                CenteredImageSpan span = new CenteredImageSpan(this, R.drawable.icon_company);
                SpannableString ss = new SpannableString("  " + shopCartBean.userAddress + shopCartBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                tvSendAddressDetail.setText(ss);
                tvJustSendAddressDetail.setText(ss);

//                tvSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_company), null, null, null);
//                tvSendAddressDetail.setCompoundDrawablePadding(8);

//                tvJustSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_company), null, null, null);
//                tvJustSendAddressDetail.setCompoundDrawablePadding(8);
            } else if (shopCartBean.userAddressTag == 3) {
                CenteredImageSpan span = new CenteredImageSpan(this, R.drawable.icon_school);
                SpannableString ss = new SpannableString("  " + shopCartBean.userAddress + shopCartBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                tvSendAddressDetail.setText(ss);
                tvJustSendAddressDetail.setText(ss);

//                tvSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_school), null, null, null);
//                tvSendAddressDetail.setCompoundDrawablePadding(8);

//                tvJustSendAddressDetail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_school), null, null, null);
//                tvJustSendAddressDetail.setCompoundDrawablePadding(8);
            }
        }

        if (!suportSelf) {
            deliveryFee = shopCartBean.deliveryFee;
        }

//        tvBusinessToTake.setVisibility(suportSelf ? View.VISIBLE : View.GONE);
        tvBusinessIsCharge.setVisibility(suportSelf ? View.GONE : View.VISIBLE);
        tvBusinessIsCharge.setText(shopCartBean.isDeliver == 0 ? "快车专送" : "商家配送");
        tvBusinessIsCharge.setTextColor(shopCartBean.isDeliver == 0 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_666666));
        tvBusinessIsCharge.setBackgroundResource(shopCartBean.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_666666);

        x.image().bind(ivBusinessLogo, UrlConstant.ImageBaseUrl + shopCartBean.businessImg, NetConfig.optionsLogoImage);
        tvBusinessName.setText(shopCartBean.businessName);
        tvPackagePrice.setText("¥" + shopCartBean.totalPackageFee.stripTrailingZeros().toPlainString());
        tvSendPrice.setText("¥" + shopCartBean.deliveryFee.stripTrailingZeros().toPlainString());
        tvCouponPrice.setText("¥" + shopCartBean.offAmount.stripTrailingZeros().toPlainString());
        tvSubPrice.setText("¥" + shopCartBean.totalPay.stripTrailingZeros().toPlainString());
        tvTotalPrice.setText("¥" + shopCartBean.totalPay.stripTrailingZeros().toPlainString());

        tvRedPacket.setText(TextUtils.isEmpty(userRedPrice) ? "" : "¥" + userRedPrice);
        tvCashCoupon.setText(TextUtils.isEmpty(userCouponPrice) ? "" : "¥" + userCouponPrice);
        tvTakeAddress.setText(shopCartBean.businessAddr);
        float distancM = AMapUtils.calculateLineDistance(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), new LatLng(Double.valueOf(shopCartBean.businessAddressLat), Double.valueOf(shopCartBean.businessAddressLng)));
        tvDistanceToYou.setText("商家距离当前位置" + (distancM < 1000 ? String.format("%.2f", distancM) + "m" : String.format("%.2f", distancM / 1000f) + "km"));
        if (suportSelf) {
            if (eatInBusiness == 0) {
                totalPackageFee = shopCartBean.totalPackageFee;
                tvEatSavePackPrice.setText("为您节省餐盒费" + shopCartBean.totalPackageFee.stripTrailingZeros().toPlainString() + "元");
                tvDiscountPriceDetail.setText("优惠¥" + shopCartBean.offAmount.stripTrailingZeros().toPlainString() + "+ 省配送费¥" + deliveryFee.stripTrailingZeros().toPlainString());
                tvDiscountPrice.setText("为您节省¥" + shopCartBean.offAmount.add(deliveryFee).stripTrailingZeros().toPlainString());
            } else {
                tvDiscountPriceDetail.setText("优惠¥" + shopCartBean.offAmount.stripTrailingZeros().toPlainString() + "+ 省配送费¥" + deliveryFee.stripTrailingZeros().toPlainString() + "+ 省餐盒费¥" + totalPackageFee.stripTrailingZeros().toPlainString());
                tvDiscountPrice.setText("为您节省¥" + shopCartBean.offAmount.add(deliveryFee).add(totalPackageFee).stripTrailingZeros().toPlainString());
            }
        } else {
            tvDiscountPrice.setText("为您节省¥" + shopCartBean.offAmount.stripTrailingZeros().toPlainString());
        }

        isSetText = true;
//        etTakeYourselfMobile.setText(shopCartBean.selfMobile);
        etTakeYourselfMobile.setCursorVisible(false);

        llContainAct.removeAllViews();
        if (shopCartBean.validActivityList != null && shopCartBean.validActivityList.size() > 0) {
            for (int i = 0; i < shopCartBean.validActivityList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_order_act, null);
                TextView tvEnjoyAct = view.findViewById(R.id.tv_enjoy_act);
                TextView tvEnjoyPrice = view.findViewById(R.id.tv_enjoy_price);
                if (shopCartBean.validActivityList.get(i).activityType == 1 ||
                        shopCartBean.validActivityList.get(i).activityType == 5 ||
                        shopCartBean.validActivityList.get(i).activityType == 7) {
                    showActImage(tvEnjoyAct, shopCartBean.validActivityList.get(i));
                    tvEnjoyPrice.setText("- ¥ " + shopCartBean.validActivityList.get(i).less.stripTrailingZeros().toPlainString());
                    llContainAct.addView(view);
                }
            }
        }


    }

    private void dealOrderGoods() {
        llContainGoods.removeAllViews();
        OrderGoodsNewAdapter orderGoodsNewAdapter = new OrderGoodsNewAdapter(this, shopCartBean.cartItems);
        for (int i = 0; i < shopCartBean.cartItems.size(); i++) {
            if (isFromCart) {
                if (shopCartBean.cartItems.get(i).checked) {
                    llContainGoods.addView(orderGoodsNewAdapter.getView(i, null, null));
                }
            } else {
                llContainGoods.addView(orderGoodsNewAdapter.getView(i, null, null));
            }
        }
    }


    private void setListener() {
        scrollView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
//                LogUtil.d("wdevon", "-------scrollY-------" + scrollY);
                handleTitleBarColorEvaluate(-scrollY);
            }
        });

        etTakeYourselfMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSetText) {
                    isSetText = false;
                } else {
                    dealPhone(s.toString());
                }
            }
        });

        etTakeYourselfMobile.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    etTakeYourselfMobile.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }

        });
    }

    private void dealPhone(String phone) {
        if (phone.length() == 11) {
            if (isFromCart) {
                try {
                    paramJson.put("selfMobile", phone);
                    paramJson.put("suportSelf", true + "");
                    SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                    requestFromCartOrderInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                paramMap.put("selfMobile", phone);
                paramMap.put("suportSelf", true + "");
                requestGetOrderInfo();
            }
        }

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
            StatusUtil.setUseStatusBarColor(this, getResources().getColor(R.color.white));
//            llTopAddress.setVisibility(View.GONE);
        } else {
//            viewTitleBg.setAlpha(1f - fraction);
//            viewActionMoreBg.setAlpha(1f - fraction);
            rlTitle.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.bg_fba42a, R.color.bg_f3f2f2));
            viewBackground.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.bg_fba42a, R.color.bg_f3f2f2));
            StatusUtil.setUseStatusBarColor(this, ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.bg_fba42a, R.color.white));
//            llTopAddress.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.tv_take_yourself, R.id.tv_diver_send, R.id.ll_just_send_address, R.id.ll_send_address, R.id.rl_just_send_time, R.id.rl_send_time, R.id.ll_take_yourself_time, R.id.tv_look_map, R.id.cb_agree_deal, R.id.ll_take_yourself, R.id.cb_eat_here, R.id.rl_eat_here, R.id.cb_take_out, R.id.rl_eat_take_out, R.id.rl_red_packet, R.id.rl_cash_coupon, R.id.rl_remark, R.id.tv_submit_order, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_take_yourself:

                textTakeYourself.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                llDiverSend.setVisibility(View.GONE);
                llTakeYourself.setVisibility(View.VISIBLE);
                rlEatHere.setVisibility(View.VISIBLE);
                rlEatTakeOut.setVisibility(View.VISIBLE);
                suportSelf = true;
                tvBusinessToTake.setVisibility(View.VISIBLE);
                tvDiscountPriceDetail.setVisibility(View.VISIBLE);
                if (isFromCart) {
                    try {
                        paramJson.put("suportSelf", suportSelf + "");
                        paramJson.put("selfTime", takeTime);
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("suportSelf", suportSelf + "");
                    paramMap.put("selfTime", takeTime);
                    requestGetOrderInfo();
                }

                break;
            case R.id.tv_diver_send:

                textDiverSend.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                llDiverSend.setVisibility(View.VISIBLE);
                llTakeYourself.setVisibility(View.GONE);
                rlEatHere.setVisibility(View.GONE);
                rlEatTakeOut.setVisibility(View.GONE);
                suportSelf = false;
                tvBusinessToTake.setVisibility(View.GONE);
                tvDiscountPriceDetail.setVisibility(View.GONE);

                if (isFromCart) {
                    try {
                        paramJson.put("suportSelf", TextUtils.isEmpty(shopCartBean.userAddressId) ? "" : suportSelf + "");
                        paramJson.put("selfTime", "");
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("suportSelf", TextUtils.isEmpty(shopCartBean.userAddressId) ? "" : suportSelf + "");
                    paramMap.put("selfTime", "");
                    requestGetOrderInfo();
                }
                break;
//            case R.id.tv_send_address_detail:
//                break;
            case R.id.ll_just_send_address:
                Intent justSendIntent = new Intent(this, AddressSelectActivity.class);
                justSendIntent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_ADDRESS);
                justSendIntent.putExtra("paramMap", (Serializable) paramMap);
                if (isFromCart) {
                    justSendIntent.putExtra("isFromCart", isFromCart);
                    justSendIntent.putExtra("paramJson", paramJson.toString());
                }
                startActivityForResult(justSendIntent, REQUESTCODE_ADDRESS);
                break;
            case R.id.ll_send_address:
                Intent sendIntent = new Intent(this, AddressSelectActivity.class);
                sendIntent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_ADDRESS);
                sendIntent.putExtra("paramMap", (Serializable) paramMap);
                if (isFromCart) {
                    sendIntent.putExtra("isFromCart", isFromCart);
                    sendIntent.putExtra("paramJson", paramJson.toString());
                }
                startActivityForResult(sendIntent, REQUESTCODE_ADDRESS);
                break;
            case R.id.rl_just_send_time:
                sendTimeChooseDialog.show();
                sendTimeChooseDialog.initTodayTime(shopCartBean.disTime, businessInfo.deliveryRange);
                break;
            case R.id.rl_send_time:
                sendTimeChooseDialog.show();
                sendTimeChooseDialog.initTodayTime(shopCartBean.disTime, businessInfo.deliveryRange);
                break;
            case R.id.ll_take_yourself_time:
                takeimeChooseDialog.show();
                takeimeChooseDialog.initTodayTime(shopCartBean.disTime);
                break;
            case R.id.cb_agree_deal:
                break;
            case R.id.ll_take_yourself:
                break;
            case R.id.tv_look_map:
                Intent businessIntent = new Intent(this, WalkRouteActivity.class);
                businessIntent.putExtra("lat", shopCartBean.businessAddressLat);
                businessIntent.putExtra("lng", shopCartBean.businessAddressLng);
                startActivity(businessIntent);
                break;
            case R.id.cb_eat_here:
                eatInBusiness = 1;
                cbEatHere.setChecked(true);
                cbTakeOut.setChecked(false);

                if (isFromCart) {
                    try {
                        paramJson.put("eatInBusiness", eatInBusiness + "");
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("eatInBusiness", eatInBusiness + "");
                    requestGetOrderInfo();
                }

                break;
            case R.id.rl_eat_here:
                break;
            case R.id.cb_take_out:
                eatInBusiness = 0;
                cbTakeOut.setChecked(true);
                cbEatHere.setChecked(false);
                if (isFromCart) {
                    try {
                        paramJson.put("eatInBusiness", eatInBusiness + "");
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("eatInBusiness", eatInBusiness + "");
                    requestGetOrderInfo();
                }

                break;
            case R.id.rl_eat_take_out:
                break;
            case R.id.rl_red_packet:
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra("businessId", businessId);
                intent.putExtra("paramMap", (Serializable) paramMap);
                if (isFromCart) {
                    intent.putExtra("isFromCart", isFromCart);
                    intent.putExtra("paramJson", paramJson.toString());
                }
                intent.putExtra("isChoose", true);
                startActivityForResult(intent, REQUESTCODE_RED_PACKAGE);
                break;
            case R.id.rl_cash_coupon:
                Intent data = new Intent(this, BusinessCouponActivity.class);
                data.putExtra("businessId", businessId);
                data.putExtra("paramMap", (Serializable) paramMap);
                if (isFromCart) {
                    data.putExtra("isFromCart", isFromCart);
                    data.putExtra("paramJson", paramJson.toString());
                }
                startActivityForResult(data, REQUESTCODE_COUPON);
                break;
            case R.id.rl_remark:
                Intent mIntent = new Intent(this, OrderRemarkActivity.class);
                if (TextUtils.equals("口味、偏好等要求", tvRemark.getText().toString())) {
                } else {
                    mIntent.putExtra("remark_data", tvRemark.getText().toString());
                }
                mIntent.putExtra("businessId", businessId);
                mIntent.putExtra("paramMap", (Serializable) paramMap);
                if (isFromCart) {
                    mIntent.putExtra("isFromCart", isFromCart);
                    mIntent.putExtra("paramJson", paramJson.toString());
                }
                startActivityForResult(mIntent, REQUESTCODE_REMARK);
                break;
            case R.id.tv_submit_order:
                if (TextUtils.isEmpty(shopCartBean.userAddressId) && !suportSelf) {
                    ToastUtil.showToast("请先选择地址");
                    return;
                }

                if (TextUtils.isEmpty(etTakeYourselfMobile.getText().toString().trim()) && suportSelf) {
                    ToastUtil.showToast("请先填写自取电话");
                    return;
                }

                requestCreateOrder();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void requestCreateOrder() {
        CustomApplication.getRetrofitNew().creatOrder(businessId, orderRemark).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        jumpToPay(jsonObject);
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

    private void jumpToPay(JSONObject jsonObject) {
        JSONObject data = jsonObject.optJSONObject("data");
        int orderId = data.optInt("orderId", 0);
        String orderCode = data.optString("orderNo");
        Intent intent = new Intent(this, PayChannelActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("price", shopCartBean.totalPay.doubleValue());
        intent.putExtra("businessName", shopCartBean.businessName);
        intent.putExtra("logo", shopCartBean.businessImg);
        intent.putExtra("createTime", data.optString("createTime"));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUESTCODE_ADDRESS) {
            boolean isFromCart = data.getBooleanExtra("isFromCart", false);
            if (isFromCart) {
                try {
                    paramJson = new JSONObject(data.getStringExtra("paramJson"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            paramMap = (HashMap<String, String>) data.getSerializableExtra("paramMap");
            shopCartBean = (ShopCartBean) data.getSerializableExtra("shopCartBean");
            fillOrderView();
        } else if (requestCode == REQUESTCODE_COUPON) {
            boolean isFromCart = data.getBooleanExtra("isFromCart", false);
            if (isFromCart) {
                try {
                    paramJson = new JSONObject(data.getStringExtra("paramJson"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            paramMap = (HashMap<String, String>) data.getSerializableExtra("paramMap");
            shopCartBean = (ShopCartBean) data.getSerializableExtra("shopCartBean");
            userCouponPrice = data.getStringExtra("userCouponPrice");
            fillOrderView();
        } else if (requestCode == REQUESTCODE_RED_PACKAGE) {
            boolean isFromCart = data.getBooleanExtra("isFromCart", false);
            if (isFromCart) {
                try {
                    paramJson = new JSONObject(data.getStringExtra("paramJson"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            paramMap = (HashMap<String, String>) data.getSerializableExtra("paramMap");
            shopCartBean = (ShopCartBean) data.getSerializableExtra("shopCartBean");
            userRedPrice = data.getStringExtra("userRedPrice");
            fillOrderView();
        } else if (requestCode == REQUESTCODE_REMARK) {
            orderRemark = data.getStringExtra("order_remark");
            tvRemark.setText(TextUtils.isEmpty(orderRemark) ? "口味、偏好等要求" : orderRemark);
        }
    }

}
