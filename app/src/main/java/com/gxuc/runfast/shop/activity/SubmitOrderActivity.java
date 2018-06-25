package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
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
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
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
import com.gxuc.runfast.shop.view.TimeChooseDialog;
import com.hedan.textdrawablelibrary.TextViewDrawable;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.Serializable;
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
    private int businessId;
    private String lat;
    private String lng;
    private int REQUESTCODE_ADDRESS = 1001;
    private int REQUESTCODE_REMARK = 1002;
    private int REQUESTCODE_RED_PACKAGE = 1003;
    private int REQUESTCODE_COUPON = 1004;
    private ShopCartBean shopCartBean;
    private String sendTime = SystemUtil.getNowDateFormat();
    private String takeTime = SystemUtil.getNowDateFormat();
    private TimeChooseDialog sendTimeChooseDialog;
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
            paramMap.put("selfTime", takeTime);
            paramJson.put("selfTime", takeTime);
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

        sendTimeChooseDialog = new TimeChooseDialog(this, true, new TimeChooseDialog.OnTimeDialogClickListener() {
            @Override
            public void onTimeDialogClick(String day, String hourMinute) {
                if (TextUtils.isEmpty(day)) {
                    sendTime = SystemUtil.getNowDateFormat();
                    tvSendTime.setText("立即取件");
                    tvJustSendTime.setText("立即取件");
                    return;
                }
                if (TextUtils.equals("TODAY", day)) {
                    String nowDateFormat = SystemUtil.getNowDateFormat();
                    String data = nowDateFormat.substring(0, 11);
                    sendTime = data + hourMinute + ":00";
                    tvSendTime.setText(sendTime);
                    tvJustSendTime.setText(sendTime);
                } else {
                    long l = System.currentTimeMillis() + 86400000;
                    String time = SystemUtil.getTime(l);
                    LogUtil.d("devon", "-----------" + time + "-------------");
                    String data = time.substring(0, 11);
                    sendTime = data + hourMinute + ":00";
                    tvSendTime.setText(sendTime);
                    tvJustSendTime.setText(sendTime);
                }
            }
        });

        takeimeChooseDialog = new TimeChooseDialog(this, false, new TimeChooseDialog.OnTimeDialogClickListener() {
            @Override
            public void onTimeDialogClick(String day, String hourMinute) {
                if (TextUtils.isEmpty(day)) {
                    takeTime = SystemUtil.getNowDateFormat();
                    tvTakeYourselfTime.setText("立即取件");
                    return;
                }
                if (TextUtils.equals("TODAY", day)) {
                    String nowDateFormat = SystemUtil.getNowDateFormat();
                    String data = nowDateFormat.substring(0, 11);
                    takeTime = data + hourMinute + ":00";
                    tvTakeYourselfTime.setText("大约 " + takeTime.substring(11, 16) + " 达到");
                } else {
                    long l = System.currentTimeMillis() + 86400000;
                    String time = SystemUtil.getTime(l);
                    LogUtil.d("devon", "-----------" + time + "-------------");
                    String data = time.substring(0, 11);
                    takeTime = data + hourMinute + ":00";
                    tvTakeYourselfTime.setText("大约" + takeTime.substring(11, 16) + "达到");
                }
            }
        });


    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);

        businessId = getIntent().getIntExtra("businessId", 0);
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lng = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        isFromCart = getIntent().getBooleanExtra("isFromCart", false);
        isSuportSelf = getIntent().getBooleanExtra("suportSelf", false);

        llJustDiverSend.setVisibility(isSuportSelf ? View.GONE : View.VISIBLE);
        llDiverSend.setVisibility(isSuportSelf ? View.VISIBLE : View.GONE);
//        SharePreferenceUtil.getInstance().putBooleanValue("isFromCart", false);
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


            if (shopCartBean.validActivityList != null && shopCartBean.validActivityList.size() > 0) {
                for (int i = 0; i < shopCartBean.validActivityList.size(); i++) {
                    if (shopCartBean.validActivityList.get(i).activityType == 1) {
                        tvEnjoyPrice.setText("- ¥ " + shopCartBean.validActivityList.get(i).less);
                        break;
                    }
                }
            }

//            if (shopCartBean.toAddressId != null) {
//                paramMap.put("toAddressId", shopCartBean.toAddressId + "");
//            }
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

            if (isSuportSelf) {
                tvSendAddressDetail.setText(shopCartBean.userAddress + shopCartBean.address);
                tvSendNameAndMobile.setText(shopCartBean.userName + "     " + shopCartBean.userMobile);
                tvSendNameAndMobile.setVisibility(View.VISIBLE);
            } else {
                tvJustSendAddressDetail.setText(shopCartBean.userAddress + shopCartBean.address);
                tvJustSendNameAndMobile.setText(shopCartBean.userName + "     " + shopCartBean.userMobile);
                tvJustSendNameAndMobile.setVisibility(View.VISIBLE);
            }
        }


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

                CenteredImageSpan span = new CenteredImageSpan(this, R.drawable.icon_home);
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

//        tvBusinessToTake.setVisibility(suportSelf ? View.VISIBLE : View.GONE);
        tvBusinessIsCharge.setVisibility(suportSelf ? View.GONE : View.VISIBLE);
        tvBusinessIsCharge.setText(shopCartBean.isDeliver == 0 ? "快车转送" : "商家配送");
        tvBusinessIsCharge.setTextColor(shopCartBean.isDeliver == 0 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.bg_44be99));
        tvBusinessIsCharge.setBackgroundResource(shopCartBean.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_44be99);

        x.image().bind(ivBusinessLogo, UrlConstant.ImageBaseUrl + shopCartBean.businessImg);
        tvBusinessName.setText(shopCartBean.businessName);
        tvPackagePrice.setText("¥" + shopCartBean.totalPackageFee);
        tvSendPrice.setText("¥" + shopCartBean.deliveryFee);
        tvCouponPrice.setText("¥" + shopCartBean.offAmount);
        tvSubPrice.setText("¥" + shopCartBean.totalPay);
        tvTotalPrice.setText("¥" + shopCartBean.totalPay);
        tvDiscountPrice.setText("为您节省¥" + shopCartBean.offAmount);


        tvRedPacket.setText(TextUtils.isEmpty(userRedPrice) ? "" : "¥" + userRedPrice);
        tvCashCoupon.setText(TextUtils.isEmpty(userCouponPrice) ? "" : "¥" + userCouponPrice);
        tvTakeAddress.setText(shopCartBean.businessAddr);
        float distancM = AMapUtils.calculateLineDistance(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), new LatLng(Double.valueOf(shopCartBean.businessAddressLat), Double.valueOf(shopCartBean.businessAddressLng)));
        tvDistanceToYou.setText("商家距离当前位置" + (distancM < 1000 ? String.format("%.2f", distancM) + "m" : String.format("%.2f", distancM / 1000f) + "km"));
        tvEatSavePackPrice.setText("为您节省餐盒费" + shopCartBean.totalPackageFee + "元");

        isSetText = true;
        etTakeYourselfMobile.setText(shopCartBean.selfMobile);
        etTakeYourselfMobile.setCursorVisible(false);
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
                tvDiscountPriceDetail.setVisibility(View.GONE);
                if (isFromCart) {
                    try {
                        paramJson.put("suportSelf", suportSelf + "");
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("suportSelf", suportSelf + "");
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
                tvDiscountPriceDetail.setVisibility(View.VISIBLE);

                if (isFromCart) {
                    try {
                        paramJson.put("suportSelf", suportSelf + "");
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
                        requestFromCartOrderInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    paramMap.put("suportSelf", suportSelf + "");
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
                break;
            case R.id.rl_send_time:
                sendTimeChooseDialog.show();
                break;
            case R.id.ll_take_yourself_time:
                takeimeChooseDialog.show();
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
                if (TextUtils.isEmpty(shopCartBean.userAddressId)) {
                    ToastUtil.showToast("请先选择地址");
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
