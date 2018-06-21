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

import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.HourMinuteChooseAdapter;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeChooseDialog extends Dialog implements View.OnClickListener, HourMinuteChooseAdapter.OnHourMinuteChooseClickListener {

    private Context context;
    private OnTimeDialogClickListener listener;
    private List<String> timesList = new ArrayList<>();
    private TextView tvCancel;
    private TextView tvSure;
    private TextView tvToday;
    private TextView tvTomorrow;
    private RecyclerView recyclerHourMinute;
    private LinearLayoutManager linearLayoutManager;
    private String day = "TODAY";
    private String clickDay;
    private String hourMinute;
    private HourMinuteChooseAdapter hourMinuteChooseAdapter;

    public TimeChooseDialog(@NonNull Context context, OnTimeDialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                listener.onTimeDialogClick("", "");
                break;
            case R.id.tv_sure:
                if (!TextUtils.equals(day, clickDay)) {
                    ToastUtil.showToast("请选择时间");
                    return;
                }
                dismiss();
                listener.onTimeDialogClick(day, hourMinute);
                break;
            case R.id.tv_today:
                day = "TODAY";
                initTodayTime();
                tvToday.setBackgroundResource(R.color.white);
                tvTomorrow.setBackgroundResource(R.color.view_e5e5e5);
                break;
            case R.id.tv_tomorrow:
                day = "TOMORROW";
                initTime();
                tvToday.setBackgroundResource(R.color.view_e5e5e5);
                tvTomorrow.setBackgroundResource(R.color.white);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, String clickDay) {
        hourMinute = timesList.get(position);
        this.clickDay = clickDay;
    }

    public interface OnTimeDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param day
         * @param hourMinute
         */
        void onTimeDialogClick(String day, String hourMinute);
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
        recyclerHourMinute = (RecyclerView) findViewById(R.id.recycler_hour_minute);

        hourMinuteChooseAdapter = new HourMinuteChooseAdapter(context, this, timesList, day);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerHourMinute.setLayoutManager(linearLayoutManager);
        recyclerHourMinute.setAdapter(hourMinuteChooseAdapter);

        initTodayTime();

    }

    private void initTime() {
        timesList.clear();
        hourMinuteChooseAdapter.notifyDataSetChanged();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 6; j++) {
                if (i < 10) {
                    timesList.add("0" + i + ":" + j + "0");
                } else {
                    timesList.add(i + ":" + j + "0");
                }
            }
        }
        hourMinuteChooseAdapter.setList(timesList, day);
    }

    private void initTodayTime() {
        timesList.clear();
        hourMinuteChooseAdapter.notifyDataSetChanged();
        String nowDateFormat = SystemUtil.getNowDateFormat();

        timesList.add("立即配送");

        int mHour = Integer.valueOf(nowDateFormat.substring(11, 13));
        int mMinuts = Integer.valueOf(nowDateFormat.substring(14, 16));

        for (int i = mHour; i < 24; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i == mHour && j > mMinuts / 10) || i > mHour) {
                    if (i < 10) {
                        timesList.add("0" + i + ":" + j + "0");
                    } else {
                        timesList.add(i + ":" + j + "0");
                    }
                }
            }
        }
        hourMinuteChooseAdapter.setList(timesList, day);
    }
}
