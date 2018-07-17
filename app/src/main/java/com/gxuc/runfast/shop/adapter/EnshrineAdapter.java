package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.enshrien.Enshrine;
import com.gxuc.runfast.shop.bean.home.BusinessEvent;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.willy.ratingbar.BaseRatingBar;

import org.xutils.x;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Double.NaN;

/**
 * Created by huiliu on 2017/9/14.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class EnshrineAdapter extends RecyclerView.Adapter {

    private List<BusinessNewDetail> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;

    public EnshrineAdapter(List<BusinessNewDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public EnshrineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View businessView = LayoutInflater.from(context).inflate(R.layout.item_home_business, parent, false);
        return new EnshrineViewHolder(businessView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EnshrineViewHolder enshrineViewHolder = (EnshrineViewHolder) holder;
        final BusinessNewDetail businessNewDetail = data.get(position);
        if (businessNewDetail != null) {
            BigDecimal distanceM = new BigDecimal(businessNewDetail.distance);
            BigDecimal distanceKM = distanceM.divide(new BigDecimal("1000")).setScale(2, RoundingMode.HALF_UP);

            x.image().bind(enshrineViewHolder.ivHomeBusinessLogo, UrlConstant.ImageBaseUrl + businessNewDetail.mini_imgPath, NetConfig.optionsLogoImage);
            enshrineViewHolder.tvHomeBusinessName.setText(businessNewDetail.name);
            enshrineViewHolder.tvHomeBusinessLabel.setVisibility(businessNewDetail.newBusiness ? View.VISIBLE : View.GONE);
            enshrineViewHolder.tvHomeBusinessCategory.setText(businessNewDetail.saleRange);
            enshrineViewHolder.tvHomeBusinessEvaluateSale.setText(businessNewDetail.levelId + " 月售" + (businessNewDetail.salesnum == null ? 0 : businessNewDetail.salesnum));
//        enshrineViewHolder.tvHomeBusinessDistanceTime.setText(businessNewDetail.deliveryDuration + "分钟 | " + distanceKM + "km");
            enshrineViewHolder.tvHomeBusinessDistance.setText(distanceKM + "km");
            enshrineViewHolder.tvHomeBusinessTime.setText(businessNewDetail.deliveryDuration + "分钟");

//        enshrineViewHolder.tvHomeBusinessStartPriceAndShippingPrice.setText("起送 ¥ " + businessNewDetail.startPay + " | 配送费 ¥ " + businessNewDetail.deliveryFee);
            enshrineViewHolder.tvHomeBusinessStartPrice.setText("起送 ¥ " + businessNewDetail.startPay);
            enshrineViewHolder.tvHomeBusinessShippingPrice.setText("配送费 ¥ " + (businessNewDetail.deliveryFee.stripTrailingZeros().toPlainString()));
            enshrineViewHolder.ivHomeBusinessGold.setVisibility(businessNewDetail.goldBusiness ? View.VISIBLE : View.GONE);
            enshrineViewHolder.tvHomeBusinessIsCharge.setText(businessNewDetail.isDeliver == 0 ? "快车专送" : "商家配送");
            enshrineViewHolder.tvHomeBusinessIsCharge.setTextColor(businessNewDetail.isDeliver == 0 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.text_666666));
            enshrineViewHolder.tvHomeBusinessIsCharge.setBackgroundResource(businessNewDetail.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_666666);
            enshrineViewHolder.tvHomeBusinessToTake.setVisibility(businessNewDetail.suportSelf ? View.VISIBLE : View.GONE);
            enshrineViewHolder.rbHomeBusinessEvaluate.setRating(businessNewDetail.levelId);

            enshrineViewHolder.viewClose.setVisibility(businessNewDetail.isopen == 1 ? View.GONE : View.VISIBLE);
            enshrineViewHolder.viewClose.setBackgroundResource(businessNewDetail.bookable ? R.color.bg_33ffffff : R.color.bg_80ffffff);
            enshrineViewHolder.tvHomeBusinessClose.setVisibility(businessNewDetail.isopen == 1 ? View.GONE : View.VISIBLE);

            if (businessNewDetail.isopen != 1 && !TextUtils.isEmpty(businessNewDetail.startwork) && businessNewDetail.bookable) {
                long time = SystemUtil.date2TimeStamp(businessNewDetail.startwork, SystemUtil.DATE_FORMAT) + 60 * 60 * 1000;
                enshrineViewHolder.tvHomeBusinessStatus.setText("预定中" + SystemUtil.getTime(time).substring(11, 16) + "配送");
                enshrineViewHolder.tvHomeBusinessStatus.setVisibility(View.VISIBLE);
            } else {
                enshrineViewHolder.tvHomeBusinessStatus.setVisibility(View.GONE);
            }

            enshrineViewHolder.llHomeBusinessContainAct.removeAllViews();
            enshrineViewHolder.llHomeBusinessContainAct.setTag(false);
            if (businessNewDetail.activityList != null && businessNewDetail.activityList.size() > 0) {
                for (int i = 0; i < businessNewDetail.activityList.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                    ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                    TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                    if (businessNewDetail.activityList.get(i).ptype == 1) {
                        String actStr = "";
                        for (int j = 0; j < businessNewDetail.activityList.get(i).fullLessList.size(); j++) {
                            actStr += ("满" + businessNewDetail.activityList.get(i).fullLessList.get(j).full.stripTrailingZeros().toPlainString() +
                                    "减" + businessNewDetail.activityList.get(i).fullLessList.get(j).less.stripTrailingZeros().toPlainString() + ", ");
                        }
                        actStr = actStr.substring(0, actStr.length() - 2);
                        tvAct.setText(actStr);
                    } else {
                        tvAct.setText(businessNewDetail.activityList.get(i).name);
                    }

                    showActImage(ivAct, businessNewDetail.activityList.get(i));
                    if (i > 1) {
                        view.setVisibility(View.GONE);
                    }
                    enshrineViewHolder.llHomeBusinessContainAct.addView(view);
                }

                if (businessNewDetail.activityList.size() > 2) {
                    enshrineViewHolder.llHomeBusinessContainAct.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
                }

            }
            enshrineViewHolder.itemView.setTag(position);
            enshrineViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick((int) v.getTag(), businessNewDetail);
                    }
                }
            });

            enshrineViewHolder.llHomeBusinessContainAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (businessNewDetail.activityList.size() > 2) {
                        boolean showStatus = false;
                        for (int i = 0; i < businessNewDetail.activityList.size(); i++) {
                            enshrineViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean) enshrineViewHolder.llHomeBusinessContainAct.getTag()) {
                                    enshrineViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.GONE);
                                    showStatus = false;
                                } else {
                                    enshrineViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                                    showStatus = true;
                                }
                            }
                        }
                        enshrineViewHolder.llHomeBusinessContainAct.setTag(showStatus);
                    }

                }
            });
        }
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

    public void setList(ArrayList<BusinessNewDetail> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class EnshrineViewHolder extends RecyclerView.ViewHolder {
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

        EnshrineViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BusinessNewDetail enshrine);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
