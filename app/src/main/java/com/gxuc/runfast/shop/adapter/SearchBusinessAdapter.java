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
import com.gxuc.runfast.shop.bean.SearchBusinessInfo;
import com.gxuc.runfast.shop.bean.home.BusinessEvent;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.willy.ratingbar.BaseRatingBar;

import org.xutils.x;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class SearchBusinessAdapter extends RecyclerView.Adapter {

    private List<SearchBusinessInfo> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;

    public SearchBusinessAdapter(List<SearchBusinessInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public SearchBusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business_info, parent, false);
        return new SearchBusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SearchBusinessViewHolder searchBusinessViewHolder = (SearchBusinessViewHolder) holder;
        final SearchBusinessInfo searchBusinessInfo = data.get(position);
        if (searchBusinessInfo != null) {
            BigDecimal distanceM = new BigDecimal(searchBusinessInfo.distance);
            BigDecimal distanceKM = distanceM.divide(new BigDecimal("1000")).setScale(2, RoundingMode.HALF_UP);

            x.image().bind(searchBusinessViewHolder.ivHomeBusinessLogo, UrlConstant.ImageBaseUrl + searchBusinessInfo.mini_imgPath, NetConfig.optionsLogoImage);
            searchBusinessViewHolder.tvHomeBusinessName.setText(searchBusinessInfo.name);
            searchBusinessViewHolder.tvHomeBusinessLabel.setVisibility(searchBusinessInfo.newBusiness ? View.VISIBLE : View.GONE);
            searchBusinessViewHolder.tvHomeBusinessCategory.setText(searchBusinessInfo.saleRange);
            searchBusinessViewHolder.tvHomeBusinessEvaluateSale.setText(searchBusinessInfo.levelId + " 月售" + (searchBusinessInfo.salesnum == null ? 0 : searchBusinessInfo.salesnum));
//        searchBusinessViewHolder.tvHomeBusinessDistanceTime.setText(searchBusinessInfo.deliveryDuration + "分钟 | " + distanceKM + "km");
            searchBusinessViewHolder.tvHomeBusinessDistance.setText(distanceKM + "km");
            searchBusinessViewHolder.tvHomeBusinessTime.setText(searchBusinessInfo.deliveryDuration + "分钟");

//        searchBusinessViewHolder.tvHomeBusinessStartPriceAndShippingPrice.setText("起送 ¥ " + searchBusinessInfo.startPay + " | 配送费 ¥ " + searchBusinessInfo.deliveryFee);
            searchBusinessViewHolder.tvHomeBusinessStartPrice.setText("起送 ¥ " + searchBusinessInfo.startPay);
            searchBusinessViewHolder.tvHomeBusinessShippingPrice.setText("配送费 ¥ " + (searchBusinessInfo.deliveryFee.stripTrailingZeros().toPlainString()));
            searchBusinessViewHolder.ivHomeBusinessGold.setVisibility(searchBusinessInfo.goldBusiness ? View.VISIBLE : View.GONE);
            searchBusinessViewHolder.tvHomeBusinessIsCharge.setText(searchBusinessInfo.isDeliver == 0 ? "快车专送" : "商家配送");
            searchBusinessViewHolder.tvHomeBusinessIsCharge.setTextColor(searchBusinessInfo.isDeliver == 0 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.text_666666));
            searchBusinessViewHolder.tvHomeBusinessIsCharge.setBackgroundResource(searchBusinessInfo.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_666666);
            searchBusinessViewHolder.tvHomeBusinessToTake.setVisibility(searchBusinessInfo.suportSelf ? View.VISIBLE : View.GONE);
            searchBusinessViewHolder.rbHomeBusinessEvaluate.setRating(searchBusinessInfo.levelId);

            searchBusinessViewHolder.viewClose.setVisibility(searchBusinessInfo.isopen == 1 ? View.GONE : View.VISIBLE);
            searchBusinessViewHolder.viewClose.setBackgroundResource(searchBusinessInfo.bookable ? R.color.bg_33ffffff : R.color.bg_80ffffff);
            searchBusinessViewHolder.tvHomeBusinessClose.setVisibility(searchBusinessInfo.isopen == 1 ? View.GONE : View.VISIBLE);

            if (searchBusinessInfo.isopen != 1 && !TextUtils.isEmpty(searchBusinessInfo.startwork) && searchBusinessInfo.bookable) {
                long time = SystemUtil.date2TimeStamp(searchBusinessInfo.startwork, SystemUtil.DATE_FORMAT) + 60 * 60 * 1000;
                searchBusinessViewHolder.tvHomeBusinessStatus.setText("预定中" + SystemUtil.getTime(time).substring(11, 16) + "配送");
                searchBusinessViewHolder.tvHomeBusinessStatus.setVisibility(View.VISIBLE);
            } else {
                searchBusinessViewHolder.tvHomeBusinessStatus.setVisibility(View.GONE);
            }

            searchBusinessViewHolder.llHomeBusinessContainAct.removeAllViews();
            searchBusinessViewHolder.llHomeBusinessContainAct.setTag(false);
            if (searchBusinessInfo.activityList != null && searchBusinessInfo.activityList.size() > 0) {
                for (int i = 0; i < searchBusinessInfo.activityList.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                    ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                    TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                    if (searchBusinessInfo.activityList.get(i).ptype == 1) {
                        String actStr = "";
                        for (int j = 0; j < searchBusinessInfo.activityList.get(i).fullLessList.size(); j++) {
                            actStr += ("满" + searchBusinessInfo.activityList.get(i).fullLessList.get(j).full.stripTrailingZeros().toPlainString() +
                                    "减" + searchBusinessInfo.activityList.get(i).fullLessList.get(j).less.stripTrailingZeros().toPlainString() + ", ");
                        }
                        actStr = actStr.substring(0, actStr.length() - 2);
                        tvAct.setText(actStr);
                    } else {
                        tvAct.setText(searchBusinessInfo.activityList.get(i).name);
                    }

                    showActImage(ivAct, searchBusinessInfo.activityList.get(i));
                    if (i > 1) {
                        view.setVisibility(View.GONE);
                    }
                    searchBusinessViewHolder.llHomeBusinessContainAct.addView(view);
                }

                if (searchBusinessInfo.activityList.size() > 2) {
                    searchBusinessViewHolder.llHomeBusinessContainAct.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
                }

            }
            searchBusinessViewHolder.itemView.setTag(position);
            searchBusinessViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick((int) v.getTag(), searchBusinessInfo);
                    }
                }
            });

            searchBusinessViewHolder.llHomeBusinessContainAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchBusinessInfo.activityList.size() > 2) {
                        boolean showStatus = false;
                        for (int i = 0; i < searchBusinessInfo.activityList.size(); i++) {
                            searchBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean) searchBusinessViewHolder.llHomeBusinessContainAct.getTag()) {
                                    searchBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.GONE);
                                    showStatus = false;
                                } else {
                                    searchBusinessViewHolder.llHomeBusinessContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                                    showStatus = true;
                                }
                            }
                        }
                        searchBusinessViewHolder.llHomeBusinessContainAct.setTag(showStatus);
                    }

                }
            });
        }


        searchBusinessViewHolder.llContainProduct.removeAllViews();
        searchBusinessViewHolder.llContainProduct.setTag(false);
        if (searchBusinessInfo.goodsSellList != null && searchBusinessInfo.goodsSellList.size() > 0) {

            for (int i = 0; i < searchBusinessInfo.goodsSellList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_search_product, null);
                ImageView ivFood = (ImageView) view.findViewById(R.id.iv_food);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                TextView tvSummary = (TextView) view.findViewById(R.id.tv_summary);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
                x.image().bind(ivFood, UrlConstant.ImageBaseUrl + searchBusinessInfo.goodsSellList.get(i).imgPath, NetConfig.optionsLogoImage);
                tvName.setText(searchBusinessInfo.goodsSellList.get(i).name);
                tvSummary.setText("销量" + (searchBusinessInfo.goodsSellList.get(i).salesnum == null ? 0 : searchBusinessInfo.goodsSellList.get(i).salesnum));
                tvPrice.setText("¥" + searchBusinessInfo.goodsSellList.get(i).goodsSellStandardList.get(0).price);
                if (i > 1) {
                    view.setVisibility(View.GONE);
                }

                searchBusinessViewHolder.llContainProduct.addView(view);
            }
            searchBusinessViewHolder.tvShowProduct.setVisibility(searchBusinessInfo.goodsSellList.size() > 2 ? View.VISIBLE : View.GONE);
            searchBusinessViewHolder.tvShowProduct.setText("查看全部商品");
        } else {
            searchBusinessViewHolder.tvShowProduct.setVisibility(View.GONE);
        }

        searchBusinessViewHolder.tvShowProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBusinessInfo.goodsSellList.size() > 2) {
                    boolean isShowProduct = false;
                    for (int i = 0; i < searchBusinessInfo.goodsSellList.size(); i++) {
                        searchBusinessViewHolder.llContainProduct.getChildAt(i).setVisibility(View.VISIBLE);
                        if (i > 1) {
                            if ((boolean) searchBusinessViewHolder.llContainProduct.getTag()) {
                                searchBusinessViewHolder.llContainProduct.getChildAt(i).setVisibility(View.GONE);
                                isShowProduct = false;
                            } else {
                                searchBusinessViewHolder.llContainProduct.getChildAt(i).setVisibility(View.VISIBLE);
                                isShowProduct = true;
                            }
                        }
                    }
                    searchBusinessViewHolder.tvShowProduct.setText(isShowProduct ? "收起" : "查看全部商品");
                    searchBusinessViewHolder.llContainProduct.setTag(isShowProduct);
                }
            }
        });

        searchBusinessViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((int) v.getTag(), data.get((int) v.getTag()));
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

    public void setList(List<SearchBusinessInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class SearchBusinessViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.ll_contain_product)
        LinearLayout llContainProduct;
        @BindView(R.id.tv_show_product)
        TextView tvShowProduct;
        @BindView(R.id.view_close)
        View viewClose;
        @BindView(R.id.tv_home_business_close)
        TextView tvHomeBusinessClose;

        SearchBusinessViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, SearchBusinessInfo searchBusinessInfo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
