package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.BusinessCouponInfo;

import java.util.ArrayList;

public class BusinessCouponDialog extends Dialog {

    private Context context;
    private OnDialogClickListener listener;
    private LinearLayout llContainCoupon;

    public BusinessCouponDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public interface OnDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param position
         */
        void onDialogClick(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_business_coupon);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        llContainCoupon = (LinearLayout) findViewById(R.id.ll_contain_coupon);

    }

    public void setData(ArrayList<BusinessCouponInfo> businessCouponInfoList) {
        llContainCoupon.removeAllViews();
        for (int i = 0; i < businessCouponInfoList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cash_coupon_info, null);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvFull = (TextView) view.findViewById(R.id.tv_full);
            TextView tvGetCoupon = (TextView) view.findViewById(R.id.tv_get_coupon);
            tvName.setText(businessCouponInfoList.get(i).name);
            tvPrice.setText(businessCouponInfoList.get(i).redAmount);
            tvFull.setText("满" + businessCouponInfoList.get(i).fulls + "元可用");
            tvGetCoupon.setTag(i);
            llContainCoupon.addView(view);

            tvGetCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDialogClick((int) v.getTag());
                    }
                }
            });

        }


    }
}
