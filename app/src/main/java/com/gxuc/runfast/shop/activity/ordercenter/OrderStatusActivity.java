package com.gxuc.runfast.shop.activity.ordercenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.order.DeliveryOrderStatus;
import com.gxuc.runfast.shop.bean.order.OrderStatus;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Devon on 2017/11/30.
 */

public class OrderStatusActivity extends ToolBarActivity {

    @BindView(R.id.ll_order_stutas_contain)
    LinearLayout llOrderStutasContain;
    private int orderId;
    private ArrayList<OrderStatus> orderStatusList;
    private ArrayList<DeliveryOrderStatus> deliveryOrderStatusList;
    private boolean isDelivery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
        orderId = getIntent().getIntExtra("orderId", 0);
        isDelivery = getIntent().getBooleanExtra("isDelivery", false);
        if (isDelivery) {
            requestDeliveryOrderStatus();
        } else {
            requestOrderStatus();
        }
    }

    private void requestDeliveryOrderStatus() {
        CustomApplication.getRetrofitNew().getDeliveryOrderStatus(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optJSONArray("data").toString();
                        deliveryOrderStatusList = GsonUtil.fromJson(data, new TypeToken<ArrayList<DeliveryOrderStatus>>() {
                        }.getType());
                        if (deliveryOrderStatusList != null && deliveryOrderStatusList.size() > 0) {
                            fillView();
                        }
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

    private void requestOrderStatus() {
        CustomApplication.getRetrofitNew().getOrderStatus(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data != null && data.length() > 0) {
                            orderStatusList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<OrderStatus>>() {
                            }.getType());
                            if (orderStatusList != null && orderStatusList.size() > 0) {
                                fillView();
                            }
                        }
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
        llOrderStutasContain.removeAllViews();
        if (isDelivery) {
            for (int i = 0; i < deliveryOrderStatusList.size(); i++) {
                addOrderStatusItemView(i);
            }
        } else {
            for (int i = 0; i < orderStatusList.size(); i++) {
                addOrderStatusItemView(i);
            }
        }

    }

    private void addOrderStatusItemView(int position) {

        View view = getLayoutInflater().inflate(R.layout.item_order_status, null);
        View viewTopLine = view.findViewById(R.id.view_top_line);
        View viewBottomLine = view.findViewById(R.id.view_bottom_line);
        ImageView ivStatus = (ImageView) view.findViewById(R.id.iv_status);

        TextView tvOrderStatus = (TextView) view.findViewById(R.id.tv_order_status);
        TextView tvOrderTime = (TextView) view.findViewById(R.id.tv_order_time);
        if (position == 0) {
            viewTopLine.setVisibility(View.INVISIBLE);
            tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.text_ff9f14));
            ivStatus.setImageResource(R.drawable.circle_hollow_fb7d30);
        } else {
            tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.text_666666));
            ivStatus.setImageResource(R.drawable.circle_soild_e5e5e5);
        }

        if (isDelivery) {
            DeliveryOrderStatus deliveryOrderStatus = deliveryOrderStatusList.get(position);
            tvOrderStatus.setText(CustomUtils.getStatusStr(deliveryOrderStatus.status));
            tvOrderTime.setText(deliveryOrderStatus.createTime);
        } else {
            OrderStatus orderStatus = orderStatusList.get(position);
            tvOrderStatus.setText(orderStatus.statStr);
            tvOrderTime.setText(orderStatus.createTime);
        }

//        tvOrderStatus.setText(isDelivery ? CustomUtils.getStatusStr(deliveryOrderStatus.status) : orderStatus.statStr);
//        tvOrderTime.setText(isDelivery ? deliveryOrderStatus.createTime : orderStatus.createTime);

        llOrderStutasContain.addView(view);
    }
}
