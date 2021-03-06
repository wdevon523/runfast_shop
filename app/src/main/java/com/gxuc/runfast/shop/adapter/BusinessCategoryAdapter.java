package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.home.BusinessEvent;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.willy.ratingbar.BaseRatingBar;

import org.xutils.x;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessCategoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<NearByBusinessInfo> data;
    private OnNearByBusinessClickListener mOnNearByBusinessClickListener;
    private BigDecimal bigDecimal = new BigDecimal("1000");

    public BusinessCategoryAdapter(Context context, List<NearByBusinessInfo> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_business, parent, false);
        return new NearByBusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final NearByBusinessViewHolder nearByBusinessViewHolder = (NearByBusinessViewHolder) holder;
        final NearByBusinessInfo nearByBusinessInfo = data.get(position);
        BigDecimal distanceM = new BigDecimal(nearByBusinessInfo.distance);
        BigDecimal distanceKM = distanceM.divide(bigDecimal).setScale(2, RoundingMode.HALF_UP);

        x.image().bind(nearByBusinessViewHolder.ivHomeBusinessLogo, UrlConstant.ImageBaseUrl + nearByBusinessInfo.mini_imgPath, NetConfig.optionsLogoImage);
        nearByBusinessViewHolder.tvHomeBusinessName.setText(nearByBusinessInfo.name);
        nearByBusinessViewHolder.tvHomeBusinessLabel.setVisibility(nearByBusinessInfo.newBusiness ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.tvHomeBusinessCategory.setText(nearByBusinessInfo.saleRange);
        nearByBusinessViewHolder.tvHomeBusinessEvaluateSale.setText(nearByBusinessInfo.levelId + " 月售" + nearByBusinessInfo.salesnum);
//        nearByBusinessViewHolder.tvHomeBusinessDistanceTime.setText(nearByBusinessInfo.deliveryDuration + "分钟 | " + distanceKM + "km");
        nearByBusinessViewHolder.tvHomeBusinessDistance.setText(distanceKM + "km");
        nearByBusinessViewHolder.tvHomeBusinessTime.setText(nearByBusinessInfo.deliveryDuration + "分钟");

//        nearByBusinessViewHolder.tvHomeBusinessStartPriceAndShippingPrice.setText("起送 ¥ " + nearByBusinessInfo.startPay + " | 配送费 ¥ " + nearByBusinessInfo.deliveryFee);
        nearByBusinessViewHolder.tvHomeBusinessStartPrice.setText("起送 ¥ " + nearByBusinessInfo.startPay);
        nearByBusinessViewHolder.tvHomeBusinessShippingPrice.setText("配送费 ¥ " + (nearByBusinessInfo.deliveryFee.stripTrailingZeros().toPlainString()));
        nearByBusinessViewHolder.ivHomeBusinessGold.setVisibility(nearByBusinessInfo.goldBusiness ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.tvHomeBusinessIsCharge.setText(nearByBusinessInfo.isDeliver == 0 ? "快车专送" : "商家配送");
        nearByBusinessViewHolder.tvHomeBusinessIsCharge.setTextColor(nearByBusinessInfo.isDeliver == 0 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.text_666666));
        nearByBusinessViewHolder.tvHomeBusinessIsCharge.setBackgroundResource(nearByBusinessInfo.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_666666);
        nearByBusinessViewHolder.tvHomeBusinessToTake.setVisibility(nearByBusinessInfo.suportSelf ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.rbHomeBusinessEvaluate.setRating(nearByBusinessInfo.levelId);

        nearByBusinessViewHolder.viewClose.setVisibility(nearByBusinessInfo.isopen == 1 ? View.GONE : View.VISIBLE);
        nearByBusinessViewHolder.tvHomeBusinessClose.setVisibility(nearByBusinessInfo.isopen == 1 ? View.GONE : View.VISIBLE);
//        nearByBusinessViewHolder.tvHomeBusinessStatus.setVisibility(nearByBusinessInfo.isopen == 1 ? View.GONE : View.VISIBLE);

        if (nearByBusinessInfo.isopen != 1 && !TextUtils.isEmpty(nearByBusinessInfo.startwork)) {
//            String substring = nearByBusinessInfo.startwork.substring(11, 16);
//            int hour = Integer.valueOf(substring.substring(0, 2)) + 1;
//            if (hour < 10) {
//                nearByBusinessViewHolder.tvHomeBusinessStatus.setText("预定中 0" + hour + substring.substring(2, 5) + "配送");
//            } else if (hour == 24) {
//                nearByBusinessViewHolder.tvHomeBusinessStatus.setText("预定中 00" + substring.substring(2, 5) + " 配送");
//            } else {
//                nearByBusinessViewHolder.tvHomeBusinessStatus.setText("预定中 " + hour + substring.substring(2, 5) + "配送");
//
//            }
            long time = SystemUtil.date2TimeStamp(nearByBusinessInfo.startwork, SystemUtil.DATE_FORMAT) + 60 * 60 * 1000;
            nearByBusinessViewHolder.tvHomeBusinessStatus.setText("预定中" + SystemUtil.getTime(time).substring(11, 16) + "配送");
            nearByBusinessViewHolder.tvHomeBusinessStatus.setVisibility(View.VISIBLE);
        } else {
            nearByBusinessViewHolder.tvHomeBusinessStatus.setVisibility(View.GONE);
        }

        nearByBusinessViewHolder.llHomeBusinessContainAct.removeAllViews();
        nearByBusinessViewHolder.llHomeBusinessContainAct.setTag(false);
        if (nearByBusinessInfo.activityList != null && nearByBusinessInfo.activityList.size() > 0) {
            for (int i = 0; i < nearByBusinessInfo.activityList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                if (nearByBusinessInfo.activityList.get(i).ptype == 1) {
                    String actStr = "";
                    for (int j = 0; j < nearByBusinessInfo.activityList.get(i).fullLessList.size(); j++) {
                        actStr += ("满" + nearByBusinessInfo.activityList.get(i).fullLessList.get(j).full.stripTrailingZeros().toPlainString() +
                                "减" + nearByBusinessInfo.activityList.get(i).fullLessList.get(j).less.stripTrailingZeros().toPlainString() + ", ");
                    }
                    actStr = actStr.substring(0, actStr.length() - 2);
                    tvAct.setText(actStr);
                } else {
                    tvAct.setText(nearByBusinessInfo.activityList.get(i).name);
                }
                showActImage(ivAct, nearByBusinessInfo.activityList.get(i));
                if (i > 1) {
                    view.setVisibility(View.GONE);
                }
                nearByBusinessViewHolder.llHomeBusinessContainAct.addView(view);
            }

            if (nearByBusinessInfo.activityList.size() > 2) {
                nearByBusinessViewHolder.llHomeBusinessContainAct.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
            }

        }


        nearByBusinessViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNearByBusinessClickListener != null) {
                    mOnNearByBusinessClickListener.onNearByBusinessClickListener(position, nearByBusinessInfo);
                }
            }
        });

        nearByBusinessViewHolder.llHomeBusinessContainAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nearByBusinessInfo.activityList.size() > 2) {
                    boolean showStatus = false;
                    for (int i = 0; i < nearByBusinessInfo.activityList.size(); i++) {
                        nearByBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                        if (i > 1) {
                            if ((boolean) nearByBusinessViewHolder.llHomeBusinessContainAct.getTag()) {
                                nearByBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.GONE);
                                showStatus = false;
                            } else {
                                nearByBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                                showStatus = true;
                            }
                        }
                    }
                    nearByBusinessViewHolder.llHomeBusinessContainAct.setTag(showStatus);
                }

            }
        });
    }

    private void showActImage(ImageView ivAct, BusinessEvent businessEvent) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券,7免部分配送费,8新用户立减活动,9首单立减活动,10商户红包,11下单返红包,12 通用红包 代理商红包
        switch (businessEvent.ptype) {
            case 1:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
            case 2:
                ivAct.setImageResource(R.drawable.icon_fracture);
                break;
            case 3:
                ivAct.setImageResource(R.drawable.icon_give);
                break;
            case 4:
                ivAct.setImageResource(R.drawable.icon_special);
                break;
            case 5:
                ivAct.setImageResource(R.drawable.icon_free);
                break;
            case 6:
                ivAct.setImageResource(R.drawable.icon_coupon);
                break;
            case 7:
                ivAct.setImageResource(R.drawable.icon_free);
                break;
            case 8:
                ivAct.setImageResource(R.drawable.icon_new);
                break;
            case 9:
                ivAct.setImageResource(R.drawable.icon_new);
                break;
            case 10:
                ivAct.setImageResource(R.drawable.icon_coupon);
                break;
            case 11:
                ivAct.setImageResource(R.drawable.icon_return);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setList(List<NearByBusinessInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class NearByBusinessViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home_business_logo)
        RoundedImageView ivHomeBusinessLogo;
        @BindView(R.id.iv_home_business_gold)
        ImageView ivHomeBusinessGold;
        @BindView(R.id.tv_home_business_is_charge)
        TextView tvHomeBusinessIsCharge;
        @BindView(R.id.tv_home_business_to_take)
        TextView tvHomeBusinessToTake;
        @BindView(R.id.tv_home_business_label)
        TextView tvHomeBusinessLabel;
        @BindView(R.id.tv_home_business_name)
        TextView tvHomeBusinessName;
        @BindView(R.id.tv_home_business_evaluate_sale)
        TextView tvHomeBusinessEvaluateSale;
        //        @BindView(R.id.tv_home_business_distance_time)
//        TextView tvHomeBusinessDistanceTime;
        @BindView(R.id.tv_home_business_distance)
        TextView tvHomeBusinessDistance;
        @BindView(R.id.tv_home_business_time)
        TextView tvHomeBusinessTime;
        //        @BindView(R.id.tv_home_business_start_price_shipping_price)
//        TextView tvHomeBusinessStartPriceAndShippingPrice;
        @BindView(R.id.tv_home_business_start_price)
        TextView tvHomeBusinessStartPrice;
        @BindView(R.id.tv_home_business_shipping_price)
        TextView tvHomeBusinessShippingPrice;
        @BindView(R.id.tv_home_business_status)
        TextView tvHomeBusinessStatus;
        @BindView(R.id.tv_home_business_category)
        TextView tvHomeBusinessCategory;
        @BindView(R.id.rb_home_business_evaluate)
        BaseRatingBar rbHomeBusinessEvaluate;
        @BindView(R.id.ll_home_business_contain_act)
        LinearLayout llHomeBusinessContainAct;
        @BindView(R.id.view_close)
        View viewClose;
        @BindView(R.id.tv_home_business_close)
        TextView tvHomeBusinessClose;

        NearByBusinessViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnNearByBusinessClickListener {
        void onNearByBusinessClickListener(int position, NearByBusinessInfo nearByBusinessInfo);
    }

    public void setOnNearByBusinessClickListener(OnNearByBusinessClickListener mOnNearByBusinessClickListener) {
        this.mOnNearByBusinessClickListener = mOnNearByBusinessClickListener;
    }
}
