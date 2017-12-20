package com.gxuc.runfast.shop.activity.ordercenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.order.OrderStatus;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;

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
        requestOrderStatus();
    }

    private void requestOrderStatus() {
        CustomApplication.getRetrofit().getOrderStatus(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String outStatuslist = jsonObject.optJSONArray("outStatuslist").toString();
                    orderStatusList = GsonUtil.fromJson(outStatuslist, new TypeToken<ArrayList<OrderStatus>>() {
                    }.getType());

                    if (orderStatusList != null && orderStatusList.size() > 0) {
                        fillView();
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
        for (int i = 0; i < orderStatusList.size(); i++) {
            addOrderStatusItemView(i);
        }

    }

    private void addOrderStatusItemView(int position) {
        OrderStatus orderStatus = orderStatusList.get(position);

        View view = getLayoutInflater().inflate(R.layout.item_order_status, null);
        View viewTopLine = view.findViewById(R.id.view_top_line);
        View viewBottomLine = view.findViewById(R.id.view_bottom_line);
        ImageView ivStatus = (ImageView) view.findViewById(R.id.iv_status);

        TextView tvOrderStatus = (TextView) view.findViewById(R.id.tv_order_status);
        TextView tvOrderTime = (TextView) view.findViewById(R.id.tv_order_time);
        if (position == 0) {
            viewTopLine.setVisibility(View.INVISIBLE);
            tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.text_fb7d30));
            ivStatus.setImageResource(R.drawable.circle_hollow_fb7d30);
        } else {
            tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.text_666666));
            ivStatus.setImageResource(R.drawable.circle_soild_e5e5e5);
        }
        tvOrderStatus.setText(orderStatus.statStr);
        tvOrderTime.setText(orderStatus.createTime);

        llOrderStutasContain.addView(view);
    }
}
