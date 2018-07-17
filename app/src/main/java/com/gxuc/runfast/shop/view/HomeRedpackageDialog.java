package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.moneyadapter.CashCouponAdapter;
import com.gxuc.runfast.shop.bean.BusinessCouponInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HomeRedpackageDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private List<BusinessCouponInfo> data;
    private OnDialogClickListener listener;
    private RecyclerView recyclerView;
    private CashCouponAdapter mAdapter;
    private ImageView ivClose;
    private TextView tvTotalMoney;

    public HomeRedpackageDialog(@NonNull Context context, List<BusinessCouponInfo> data, OnDialogClickListener listener) {
        super(context);
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_home_redpackage);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (getWindow().getWindowManager().getDefaultDisplay().getWidth() * 0.9);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(false);

        mAdapter = new CashCouponAdapter(data, context, this, true);
        tvTotalMoney = (TextView) findViewById(R.id.tv_total_money);
        recyclerView = (RecyclerView) findViewById(R.id.redPack_recycle);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).redAmount.contains(",")) {
                String[] split = data.get(i).redAmount.split(",");
                totalMoney = new BigDecimal(split[1]).add(totalMoney);
            } else {
                totalMoney = new BigDecimal(data.get(i).redAmount).add(totalMoney);
            }
        }
        tvTotalMoney.setText("送你 " + totalMoney + " 元红包");
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            if (!data.get(position).picked) {
                listener.onDialogClick(position);
            }
        }
    }

    public interface OnDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param position
         */
        void onDialogClick(int position);
    }

    public void updateData(ArrayList<BusinessCouponInfo> data) {
        this.data = data;
        mAdapter.setList(data);
    }
}
