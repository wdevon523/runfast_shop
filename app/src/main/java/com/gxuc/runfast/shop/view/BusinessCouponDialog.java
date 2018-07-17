package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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

    public void setData(final ArrayList<BusinessCouponInfo> businessCouponInfoList) {
        llContainCoupon.removeAllViews();
        for (int i = 0; i < businessCouponInfoList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cash_coupon_info, null);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            ImageView ivType = (ImageView) view.findViewById(R.id.iv_type);
            TextView tvFull = (TextView) view.findViewById(R.id.tv_full);
            TextView tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            TextView tvGetCoupon = (TextView) view.findViewById(R.id.tv_get_coupon);
            tvName.setText(businessCouponInfoList.get(i).name);
            tvPrice.setText(businessCouponInfoList.get(i).redAmount + "");
            ivType.setImageResource(businessCouponInfoList.get(i).shared ? R.drawable.icon_share_coupon : R.drawable.icon_not_share_coupon);
            tvFull.setText("满" + businessCouponInfoList.get(i).fulls + "元可用");
            tvEndTime.setText("有效期至" + businessCouponInfoList.get(i).endTime.substring(0, 10));
            tvGetCoupon.setBackgroundResource(businessCouponInfoList.get(i).picked ? R.drawable.coupon_use_background : R.drawable.coupon_background);
            tvGetCoupon.setTextColor(businessCouponInfoList.get(i).picked ? ContextCompat.getColor(context, R.color.white) : ContextCompat.getColor(context, R.color.white));
            tvGetCoupon.setText(businessCouponInfoList.get(i).picked ? "已领取" : "立即领取");
            tvGetCoupon.setTag(i);
            llContainCoupon.addView(view);

            tvGetCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && !businessCouponInfoList.get((int) v.getTag()).picked) {
                        listener.onDialogClick((int) v.getTag());
                    }
                }
            });
        }
    }
}
