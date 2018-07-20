package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.OrderTimeChooseAdapter;
import com.gxuc.runfast.shop.bean.DeliveryRange;
import com.gxuc.runfast.shop.bean.OrderTimeBean;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderTimeChooseDialog extends Dialog implements View.OnClickListener, OrderTimeChooseAdapter.OnOrderTimeChooseClickListener {

    private Context context;
    private boolean showTomorrow;
    private OrderTimeChooseDialog.OnTimeDialogClickListener listener;
    private List<OrderTimeBean> orderTimeList = new ArrayList<>();
    private TextView tvCancel;
    private TextView tvSure;
    private TextView tvToday;
    private TextView tvTomorrow;
    private RecyclerView recyclerHourMinute;
    private LinearLayoutManager linearLayoutManager;
    private String day = "TODAY";
    private String clickDay;
    private OrderTimeBean orderTimeBean;
    private OrderTimeChooseAdapter orderTimeChooseAdapter;

    public OrderTimeChooseDialog(@NonNull Context context, OrderTimeChooseDialog.OnTimeDialogClickListener listener) {
        super(context);
        this.context = context;
        this.showTomorrow = showTomorrow;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                listener.onTimeDialogClick("", null);
                break;
            case R.id.tv_sure:
                if (!TextUtils.equals(day, clickDay)) {
                    ToastUtil.showToast("请选择时间");
                    return;
                }
                dismiss();
                listener.onTimeDialogClick(day, orderTimeBean);
                break;
            case R.id.tv_today:
                day = "TODAY";
//                initTodayTime();
                tvToday.setBackgroundResource(R.color.white);
                tvTomorrow.setBackgroundResource(R.color.view_e5e5e5);
                break;
            case R.id.tv_tomorrow:
                day = "TOMORROW";
//                initTime();
                tvToday.setBackgroundResource(R.color.view_e5e5e5);
                tvTomorrow.setBackgroundResource(R.color.white);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, String clickDay) {
        orderTimeBean = orderTimeList.get(position);
        this.clickDay = clickDay;
    }

    public interface OnTimeDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param day
         * @param orderTimeBean
         */
        void onTimeDialogClick(String day, OrderTimeBean orderTimeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_choose_time);
        getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);

        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        tvToday = (TextView) findViewById(R.id.tv_today);
        tvToday.setOnClickListener(this);

        tvTomorrow = (TextView) findViewById(R.id.tv_tomorrow);
        tvTomorrow.setOnClickListener(this);
        tvTomorrow.setVisibility(View.GONE);

        recyclerHourMinute = (RecyclerView) findViewById(R.id.recycler_hour_minute);

        orderTimeChooseAdapter = new OrderTimeChooseAdapter(context, this, orderTimeList);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerHourMinute.setLayoutManager(linearLayoutManager);
        recyclerHourMinute.setAdapter(orderTimeChooseAdapter);

//        if (showTomorrow) {
//            initTodayTime();
//        }
    }


    public void initTodayTime(String disTime, List<DeliveryRange> deliveryRangeList) {
        orderTimeList.clear();
        orderTimeChooseAdapter.notifyDataSetChanged();
//        String nowDateFormat = SystemUtil.getNowDateFormat();
        int mHour = Integer.valueOf(disTime.substring(11, 13));
        int mMinuts = Integer.valueOf(disTime.substring(14, 16));
//        long time = SystemUtil.date2TimeStamp(disTime, SystemUtil.DATE_FORMAT) + 20 * 60 * 1000;
//        String startTime = SystemUtil.getTime(time);

        for (int i = 0; i < deliveryRangeList.size(); i++) {
            String start = deliveryRangeList.get(i).start;
            String end = deliveryRangeList.get(i).end;
            int startHour = Integer.valueOf(start.substring(0, 2));
            int startMin = Integer.valueOf(start.substring(3, 5));
            int endHour = Integer.valueOf(end.substring(0, 2));
            int endtMin = Integer.valueOf(end.substring(3, 5));
            if (mHour < endHour || (mHour == endHour && mMinuts <= endtMin)) {
                for (int j = mHour; j <= endHour; j++) {
                    for (int k = 0; k < 6; k++) {
                        if ((j == mHour && k > mMinuts / 10) || j > mHour) {
                            if (k % 2 == 0) {
                                OrderTimeBean orderTimeBean = new OrderTimeBean();
                                if (j < 10) {
                                    orderTimeBean.hourMinute = "0" + j + ":" + k + "0";
                                } else {
                                    orderTimeBean.hourMinute = j + ":" + k + "0";
                                }
                                orderTimeBean.deliveryFee = deliveryRangeList.get(i).deliveryFee;
                                orderTimeList.add(orderTimeBean);
                            }
                        }
                    }
                }
            }

        }
        orderTimeChooseAdapter.setList(orderTimeList, day);
    }
}
