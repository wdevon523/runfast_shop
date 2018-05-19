package com.gxuc.runfast.shop.activity.ordercenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.WeiXinPayInfo;
import com.gxuc.runfast.shop.bean.order.OrderInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.WeiXinPayBean;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.ViewUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shopex.pay.AliPayHandle;
import cn.shopex.pay.Contants;
import cn.shopex.pay.WeChatPayHandle;
import cn.shopex.pay.http.WeiXinPayOutput;
import retrofit2.Call;
import retrofit2.Response;

public class PayChannelActivity extends ToolBarActivity {

    @BindView(R.id.cb_weixin_pay)
    ImageView mCbWeixinPay;
    @BindView(R.id.cb_wallet_pay)
    ImageView mCbWalletPay;
    @BindView(R.id.cb_ali_pay)
    ImageView mCbAliPay;
    @BindView(R.id.btn_to_pay)
    Button mBtnToPay;
    @BindView(R.id.rl_wallet_pay)
    RelativeLayout rlWalletPay;
    @BindView(R.id.rl_weixin_pay)
    RelativeLayout rlWeixinPay;
    @BindView(R.id.rl_ali_pay)
    RelativeLayout rlAliPay;

    private int payType = 2;//0支付宝  1微信 2钱包
    private boolean isPayBack = false;
    private User userInfo;
    private IWXAPI wxapi;
    private AlertDialog alertDialog;
    private Dialog mPayInputDialog;
    private OrderInfo orderInfo;
    private int orderId;
    private String orderCode;
    private double price;
    private String businessName;
    private String logo;
    private boolean isPaotui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_channel);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPayBack) {
            if (isPaotui) {
                requestQueryPaoTuiStatus();
            } else {
                requsetCheckPayStatus();
            }
        }

    }

    private void initView() {
        wxapi = WeChatPayHandle.createWXAPI(this, Contants.WEI_XIN_ID);
        userInfo = UserService.getUserInfo(this);
//        mOrderId = getIntent().getIntExtra("orderId", 0);
//        mPrice = getIntent().getDoubleExtra("price", 0.0);
//        businessName = getIntent().getStringExtra("businessName");
        isPaotui = getIntent().getBooleanExtra("isPaotui", false);
        orderInfo = getIntent().getParcelableExtra("orderInfo");
//        orderDetailInfo = (OrderDetail) getIntent().getSerializableExtra("orderDetail");
        orderId = getIntent().getIntExtra("orderId", 0);
        orderCode = getIntent().getStringExtra("orderCode");
        price = getIntent().getDoubleExtra("price", 0);
        businessName = getIntent().getStringExtra("businessName");
        logo = getIntent().getStringExtra("logo");
        if (orderInfo != null) {
            orderId = orderInfo.getId();
            orderCode = orderInfo.getOrderCode();
            price = orderInfo.getTotalpay();
            businessName = orderInfo.getBusinessName();
            logo = orderInfo.getLogo();
        }
        mBtnToPay.setText("确认支付 ¥ " + price);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                } else {
                    showWarnDialog();
                }
            }
        });
    }

    private void showWarnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("订单已生成，是否取消支付");
        tv.setTextColor(Color.parseColor("#757575"));
        tv.setTextSize(14);
        tv.setPadding(ViewUtils.dip2px(this, 16), ViewUtils.dip2px(this, 16), 0, 0);
        alertDialog = builder
                .setNegativeButton("取消", null)
                .setCustomTitle(tv)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();

    }

    private void requsetCheckPayStatus() {

        CustomApplication.getRetrofit().getOrderPayStatus(orderCode, payType).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(PayChannelActivity.this, PaySuccessActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("logo", logo);
                        intent.putExtra("price", price);
                        intent.putExtra("businessName", businessName);

                        startActivity(intent);
                        finish();
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

    private void requestQueryPaoTuiStatus() {
        CustomApplication.getRetrofitNew().queryPaoTuiStatus(orderCode).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(PayChannelActivity.this, PaySuccessActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("price", price);
                        intent.putExtra("isPaotui", isPaotui);

                        startActivity(intent);
                        finish();
                    }
//                    else {
//                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void requestAliPay() {
        CustomProgressDialog.startProgressDialog(this);

        CustomApplication.getRetrofit().aliPay(orderId, "1").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String orderInfo = jsonObject.optString("orderInfo");
                    alipay(orderInfo);

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

    private void requestWeiXinPay() {
        CustomProgressDialog.startProgressDialog(this);

        CustomApplication.getRetrofit().weiXintPay(orderId, "0").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                WeiXinPayBean weiXinPayBean = GsonUtil.fromJson(data, WeiXinPayBean.class);
                if (weiXinPayBean.success) {
                    requestSign(weiXinPayBean);
//                    wechatpay(weiXinPayBean);
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                CustomProgressDialog.stopProgressDialog();
            }
        });
    }

    private void requestSign(final WeiXinPayBean weiXinPayBean) {
        CustomApplication.getRetrofit().weiXintSign(weiXinPayBean.prepay_id, weiXinPayBean.map.nonceStr, weiXinPayBean.map.timeStamp, "Sign=WXPay").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String sign = jsonObject.optString("sign");
                    wechatpay(weiXinPayBean, sign);
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

    @OnClick({R.id.rl_wallet_pay, R.id.rl_weixin_pay, R.id.rl_ali_pay, R.id.btn_to_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_wallet_pay:
                payType = 2;
                mCbWalletPay.setImageResource(R.drawable.pay_type_check);
                mCbWeixinPay.setImageResource(R.drawable.pay_type_nocheck);
                mCbAliPay.setImageResource(R.drawable.pay_type_nocheck);
                break;
            case R.id.rl_weixin_pay:
                payType = 1;
                mCbWeixinPay.setImageResource(R.drawable.pay_type_check);
                mCbWalletPay.setImageResource(R.drawable.pay_type_nocheck);
                mCbAliPay.setImageResource(R.drawable.pay_type_nocheck);
                break;
            case R.id.rl_ali_pay:
                payType = 0;
                mCbAliPay.setImageResource(R.drawable.pay_type_check);
                mCbWeixinPay.setImageResource(R.drawable.pay_type_nocheck);
                mCbWalletPay.setImageResource(R.drawable.pay_type_nocheck);
                break;
            case R.id.btn_to_pay:
                if (userInfo == null) {
                    return;
                }
                switch (payType) {
                    case 2:
                        showPayInputDialog();
                        break;
                    case 1:
                        if (isPaotui) {
                            requestPaoTuiPay("WXPAY_APP");
                        } else {
                            requestWeiXinPay();
                        }
//                        requestWeiXinSign();
//                        wechatpay();
                        break;
                    case 0:
                        if (isPaotui) {
                            requestPaoTuiPay("ALIPAY_APP");
                        } else {
                            requestAliPay();
                        }
                        break;
                }
                break;
        }
    }

    private void requestPaoTuiPay(final String channel) {

        CustomApplication.getRetrofitNew().paoTuiPay(orderCode, channel).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        if (TextUtils.equals("ALIPAY_APP", channel)) {
                            alipay(data);
                        } else {
                            WeiXinPayInfo weiXinPayInfo = GsonUtil.fromJson(data, WeiXinPayInfo.class);
                            wechatPaoTuipay(weiXinPayInfo);
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorCode"));
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

    private void wechatPaoTuipay(WeiXinPayInfo weiXinPayInfo) {
        WeChatPayHandle weChatPayHandle = new WeChatPayHandle(this);
        WeiXinPayOutput weiXinPayOutput = new WeiXinPayOutput();
        weiXinPayOutput.setAppid(weiXinPayInfo.appid);
        weiXinPayOutput.setPay_sign(weiXinPayInfo.sign);
        weiXinPayOutput.setApi_key(Contants.WEI_XIN_SECRET);
        weiXinPayOutput.setNonce_str(weiXinPayInfo.noncestr);
        weiXinPayOutput.setTimestamp(weiXinPayInfo.timestamp);
        weiXinPayOutput.setPartnerid(Contants.WEI_XIN_BUSINESS_ID);
        weiXinPayOutput.setPrepayid(weiXinPayInfo.prepayid);
        weChatPayHandle.pay(weiXinPayOutput);
        isPayBack = true;
    }

    private void requestWalletPay(String password) {
        CustomProgressDialog.startProgressDialog(this);
        CustomApplication.getRetrofit().walletPay(orderId, password).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                mPayInputDialog.dismiss();
                CustomProgressDialog.stopProgressDialog();
                dealWalletPay(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                CustomProgressDialog.stopProgressDialog();
            }
        });
    }

    private void showPayInputDialog() {

        if (mPayInputDialog != null) {
            mPayInputDialog.dismiss();
        }

        mPayInputDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.dialog_pay_inputting, null);
        ImageView ivClose = (ImageView) root.findViewById(R.id.iv_close);
        TextView tvBusinessName = (TextView) root.findViewById(R.id.tv_business_name);
        TextView tvPayPrice = (TextView) root.findViewById(R.id.tv_pay_price);
        final EditText etPayPassword = (EditText) root.findViewById(R.id.et_pay_password);
        TextView tvPay = (TextView) root.findViewById(R.id.tv_pay);
        tvBusinessName.setText(businessName);
        tvPayPrice.setText("¥" + price);

        mPayInputDialog.setContentView(root);
        Window dialogWindow = mPayInputDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = (int) getResources().getDimension(R.dimen.dimen_300_dp);
        lp.height = lp.WRAP_CONTENT;
        lp.x = 0;
        lp.y = 250;
        lp.alpha = 5f;
        dialogWindow.setAttributes(lp);
        mPayInputDialog.setCanceledOnTouchOutside(false);
        mPayInputDialog.show();


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayInputDialog != null) {
                    mPayInputDialog.dismiss();
                }
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPayPassword.getText().toString();
                requestWalletPay(password);
            }
        });
    }

    private void dealWalletPay(String body) {
        try {
            JSONObject object = new JSONObject(body);
            CustomToast.INSTANCE.showToast(this, object.optString("msg"));
            if (object.optBoolean("success")) {
                Intent intent = new Intent(PayChannelActivity.this, PaySuccessActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("logo", logo);
                intent.putExtra("price", price);
                intent.putExtra("businessName", businessName);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 微信支付
    public void wechatpay(WeiXinPayBean weiXinPayBean, String sign) {

        WeChatPayHandle weChatPayHandle = new WeChatPayHandle(this);
        /**
         * @param orderid      订单号
         * @param total_amount 订单总金额
         * @param pay_app_id   服务端返回的预支付交易会话ID
         * @param token        应用token  用于发送至服务器的数据签名用
         * @param method       请求获取服务器prepayid方法名,如:mobileapi.pay.weixinpay
         * @param url          请求URL前缀 如:http://fenxiao.wyaopeng.com/index.php/api
         * @return
         */
        WeiXinPayOutput weiXinPayOutput = new WeiXinPayOutput();
        weiXinPayOutput.setAppid(weiXinPayBean.map.appId);
        weiXinPayOutput.setPay_sign(sign);
        weiXinPayOutput.setApi_key(Contants.WEI_XIN_SECRET);
        weiXinPayOutput.setNonce_str(weiXinPayBean.map.nonceStr);
        weiXinPayOutput.setTimestamp(weiXinPayBean.map.timeStamp);
        weiXinPayOutput.setPartnerid(Contants.WEI_XIN_BUSINESS_ID);
        weiXinPayOutput.setPrepayid(weiXinPayBean.prepay_id);
        weChatPayHandle.pay(weiXinPayOutput);
        isPayBack = true;
    }

    // 支付宝支付
    public void alipay(final String orderInfo) {
        LogUtil.d("devon", "orderInfo-----" + orderInfo);
        AliPayHandle aliPayHandle = new AliPayHandle(this);
        try {
            aliPayHandle.alipay(orderInfo);
            isPayBack = true;

        } catch (AliPayHandle.APliPaySetingInfoNullException e) {
            e.printStackTrace();
        }
//        AliPayInfo aliPayInfo = new AliPayInfo();
//        aliPayInfo.setPartner("2088121635967741");
//        aliPayInfo.setSeller("shopexlanlian@shopex.cn");
//        aliPayInfo.setRsa_private("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANrcJWntLzth1WrTATvFWEpgZbGr0GB87dmfCX2X5k6xY8YyrX/7yn2ku+2O3RqU2JzbHRn80LOneXv6vW5CwPn87IgDZm+Jlm1a4MXGUdLFK5AxnKnWPn8/pn2d0q36f+ffCZFz5vvttIiFxL1DK/FEBihbucEILjsTflzxLTEfAgMBAAECgYAQTanz1BlaoRUW2hUTcMX89GVe8N+a8HN/bJt2YaRkZE58azYFfKo+5dCiB6xs2H2yYZgvYWfimi0wkSMfXZWmZlPB2H5jSFpVc3Hm3x7P8v62Q++2TPx50w92YW3XkRcgb/yxPcEnFtVhySYpKgjN+xPZAFc/eIw/HFqAezT40QJBAP/NRhuelKQA4Q4pB/pV8u8NUYdwW3XVw1A5TXdbAVLR2ZRgY5nnMsWlnQK6fttO7IqgT6J3MgkaeDTksVzNkKMCQQDbB4vvHBTal6fktzNO4nBCa13iLXKFmVqH2OyQYS3P6mI898VfYSuihxMFnLzf8mmD66m60e7CDgLP1UzkhtlVAkEApJ22c0m/QDesnU88ahZrqvk4MV/WC+PPuE3YE2pGVMTlL35EOqO1YcotW7cmsf19bcyy0svAMGmPWAKgPSew3wJBAKzdi+evdhX/05aDsI710DvbmUFFNTmUCwtkfXCGAi4ygk40DyZz/ohLqwum5LqrC/P+LvsvbQzjGf0GD6Xdd/ECQCOpH8Hli51DwGzCzXLYju52Dhk94C6A7ChvJnhk4Syew/g/k4F4ES5nZWpgQ1toxvGKe/CXS3LVnbbEAnRj9ok=");
//        aliPayInfo.setNotify_url("http://shop.wyaopeng.com/index.php/openapi/ectools_payment/parse/wap/wap_payment_plugin_malipay_server/callback1/");
//        aliPayInfo.setSubject("测试商品");
//        aliPayInfo.setBody("测试商品");
//        aliPayInfo.setPrice("0.01");
//        try {
//            AliPayHandle aliPayHandle = new AliPayHandle(this, aliPayInfo);
//        } catch (AliPayHandle.APliPaySetingInfoNullException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mPayInputDialog != null) {
                if (mPayInputDialog.isShowing()) {
                    mPayInputDialog.dismiss();
                    return true;
                }
            }

            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
            showWarnDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

}
