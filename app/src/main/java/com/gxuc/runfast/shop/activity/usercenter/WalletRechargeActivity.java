package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.activity.PayDetailActivity;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.WeiXinPayBean;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
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

/**
 * 钱包充值
 */
public class WalletRechargeActivity extends ToolBarActivity {

    @BindView(R.id.tv_pay_mode)
    TextView tvPayMode;
    @BindView(R.id.et_account_money)
    EditText etMoney;

    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_recharge);
        ButterKnife.bind(this);
        wxapi = WeChatPayHandle.createWXAPI(this, Contants.WEI_XIN_ID);
    }

    @OnClick({R.id.tv_pay_mode, R.id.btn_pay_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pay_mode:
                showPayMode();
                break;
            case R.id.btn_pay_mode:
                String monetary = etMoney.getText().toString().trim();
                if (Double.valueOf(monetary) < 1) {
                    ToastUtils.showShortToast(this, "充值金额不能小于1元");
                    return;
                }
//                startActivity(new Intent(this,PayDetailActivity.class));
                if (TextUtils.equals("微信支付", tvPayMode.getText().toString())) {
                    requestWeiXinPay(monetary);
                } else {
                    requestAliPay(monetary);
                }


                break;
        }
    }

    private void requestWeiXinPay(String monetary) {
        CustomApplication.getRetrofit().weiXinRecharge(monetary).enqueue(new MyCallback<String>() {
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

            }
        });
    }

    private void requestSign(final WeiXinPayBean weiXinPayBean) {
        CustomApplication.getRetrofit().weiXintSign(weiXinPayBean.prepay_id, weiXinPayBean.map.nonceStr, weiXinPayBean.map.timeStamp, "Sign=WXPay").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
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

            }
        });
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
    }

    private void requestAliPay(String monetary) {
        CustomApplication.getRetrofit().aliPayRecharge(monetary, "2", "alipay").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
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

            }
        });
    }

    // 支付宝支付
    public void alipay(final String orderInfo) {
        LogUtil.d("devon", "orderInfo-----" + orderInfo);
        AliPayHandle aliPayHandle = new AliPayHandle(this);
        try {
            aliPayHandle.alipay(orderInfo);

        } catch (AliPayHandle.APliPaySetingInfoNullException e) {
            e.printStackTrace();
        }

    }

    /**
     * 弹出选择方式
     */
    private void showPayMode() {
        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(16)
                .setData(new String[]{"微信支付", "支付宝支付"})
                .title("支付方式")
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#333333")
                .backgroundPop(0xa0333333)
                .confirTextColor("#fc9153")
                .cancelTextColor("#999999")
                .textColor(Color.parseColor("#333333"))
                .itemPadding(10)
                .onlyShowProvinceAndCity(true)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                tvPayMode.setText(province);
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
