package com.gxuc.runfast.shop.adapter.moneyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.R;

import java.util.List;

/**
 * 收支明细全部适配
 * Created by 天上白玉京 on 2017/8/5.
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponsViewHolder> implements View.OnClickListener {

    private List<CouponBean> data;

    private Context context;

    private OnItemClickListener mOnItemClickListener = null;


    public CouponsAdapter(List<CouponBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public CouponsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon_info, parent, false);
        CouponsViewHolder holder = new CouponsViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CouponsViewHolder holder, int position) {
        CouponBean couponBean = data.get(position);
        holder.itemView.setTag(couponBean);
        holder.mTvCouponName.setText(couponBean.activityName);
        holder.mTvCouponNumber.setText(couponBean.less.stripTrailingZeros().toPlainString() + "");
        holder.mTvCouponMin.setText("满" + String.valueOf(couponBean.full) + "可用");
        //TODO 无配送类型字段
//        holder.mTvCouponDeliver.setText(String.valueOf(couponBean.getPrice()));
        String startTime = couponBean.createTime;
        String endTime = couponBean.endTime;
//        if (startTime.contains(" ")) {
//            startTime = startTime.substring(0, startTime.indexOf(" "));
//        }
        if (endTime.contains(" ")) {
            endTime = endTime.substring(0, endTime.indexOf(" "));
        }
        holder.mTvCouponDate.setText("有效期至: " + endTime);
        //TODO 已过期需要服务器返回 用户端存在手机系统时间非国际时间
        holder.mTvCouponState.setText(couponBean.used ? "已使用" : "未使用");
        holder.mIvCouponType.setImageResource(couponBean.shared ? R.drawable.icon_share_coupon : R.drawable.icon_not_share_coupon);

//        if (TextUtils.isEmpty(couponBean.getEnduse())) {
//            holder.mTvCouponLimitDate.setText("限时段：无");
//        } else if (!TextUtils.isEmpty(couponBean.getStartuse())) {
//            holder.mTvCouponLimitDate.setText("限时段：" + couponBean.getStartuse() + " - " + couponBean.getEnduse());
//        } else {
//            holder.mTvCouponLimitDate.setText("限时段：截止时间" + couponBean.getEnduse());
//        }
//        switch (couponBean.getRange1()) {
//            case 1:
//                holder.mTvCouponType.setText("通用优惠券");
//                break;
//            case 2:
//                holder.mTvCouponType.setText("指定" + couponBean.getBusinessName());
//                break;
//            case 3:
//                holder.mTvCouponType.setText("指定" + couponBean.getAgentName());
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (CouponBean) v.getTag());
        }
    }

    public void setList(List<CouponBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class CouponsViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCouponNumber;
        TextView mTvCouponMin;
        TextView mTvCouponState;
        TextView mTvCouponDate;
        TextView mTvCouponName;
        ImageView mIvExpired;
        ImageView mIvCouponType;

        public CouponsViewHolder(View itemView) {
            super(itemView);
            mTvCouponNumber = (TextView) itemView.findViewById(R.id.tv_coupon_price);
            mTvCouponMin = (TextView) itemView.findViewById(R.id.tv_coupon_min);
            mTvCouponState = (TextView) itemView.findViewById(R.id.tv_coupon_state);
            mTvCouponDate = (TextView) itemView.findViewById(R.id.tv_coupon_date);
            mTvCouponName = (TextView) itemView.findViewById(R.id.tv_coupon_name);
            mIvCouponType = (ImageView) itemView.findViewById(R.id.iv_coupon_type);
            mIvExpired = (ImageView) itemView.findViewById(R.id.iv_expired);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CouponBean couponBean);
    }
}
