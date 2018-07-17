package com.gxuc.runfast.shop.adapter.moneyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.BusinessCouponInfo;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 代金券适配
 * Created by 天上白玉京 on 2017/8/5.
 */

public class CashCouponAdapter extends RecyclerView.Adapter<CashCouponAdapter.CashCouponViewHolder> {

    private List<BusinessCouponInfo> data;
    private Context context;
    private View.OnClickListener listener;
    private boolean isFromHome;

    public CashCouponAdapter(List<BusinessCouponInfo> data, Context context, View.OnClickListener listener, boolean isFromHome) {
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.isFromHome = isFromHome;
    }

    @Override
    public CashCouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cash_coupon_info, parent, false);
        CashCouponViewHolder holder = new CashCouponViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CashCouponViewHolder holder, int position) {
        BusinessCouponInfo businessCouponInfo = data.get(position);
        if (businessCouponInfo != null) {
            holder.tv_price.setText(businessCouponInfo.redAmount + "");
            holder.tv_name.setText(businessCouponInfo.ptype == 12 ? "通用红包" : businessCouponInfo.busname);
            holder.tv_full.setText("满" + businessCouponInfo.fulls + "元可用");
            holder.tv_end_time.setText("有效期至: " + businessCouponInfo.endTime);
            holder.iv_type.setImageResource(businessCouponInfo.shared ? R.drawable.icon_share_coupon : R.drawable.icon_not_share_coupon);
            if (businessCouponInfo.picked) {
                holder.tv_get_coupon.setText("已领取");
            } else {
                if (isFromHome) {
                    holder.tv_get_coupon.setText(businessCouponInfo.ptype == 12 ? "立即领取" : "进店使用");
                } else {
                    holder.tv_get_coupon.setText("立即领取");
                }
            }
            holder.tv_get_coupon.setBackgroundResource(businessCouponInfo.picked ? R.drawable.coupon_use_background : R.drawable.coupon_background);
            holder.tv_get_coupon.setTag(position);
            holder.tv_get_coupon.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setList(ArrayList<BusinessCouponInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class CashCouponViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price, tv_name, tv_full, tv_get_coupon, tv_end_time;
        ImageView iv_type;

        public CashCouponViewHolder(View itemView) {
            super(itemView);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_full = (TextView) itemView.findViewById(R.id.tv_full);
            tv_end_time = (TextView) itemView.findViewById(R.id.tv_end_time);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_type);
            tv_get_coupon = (TextView) itemView.findViewById(R.id.tv_get_coupon);
        }
    }
}
