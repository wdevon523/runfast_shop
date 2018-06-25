package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.bean.SearchBusinessInfo;
import com.gxuc.runfast.shop.bean.home.BusinessEvent;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.willy.ratingbar.BaseRatingBar;

import org.json.JSONException;
import org.xutils.x;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.BreakfastViewHolder> {

    private List<SearchBusinessInfo> data;

    private Context context;

    private View.OnClickListener listener;

    public BreakfastAdapter(List<SearchBusinessInfo> data, Context context, View.OnClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public BreakfastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business_info, parent, false);
        BreakfastViewHolder viewHolder = new BreakfastViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BreakfastViewHolder holder, int position) {
        final SearchBusinessInfo businessInfo = data.get(position);
        if (businessInfo != null) {
            holder.view_close.setVisibility(businessInfo.isopen == 0 ? View.VISIBLE : View.GONE);
            holder.ll_business_open.setVisibility(businessInfo.isopen == 0 ? View.GONE : View.VISIBLE);
            holder.ll_business_close.setVisibility(businessInfo.isopen == 0 ? View.VISIBLE : View.GONE);

            holder.tv_business_name.setText(businessInfo.name);
            holder.tv_business_levelId.setText(String.valueOf(businessInfo.levelId));
            holder.rb_order_evaluate.setRating(businessInfo.levelId);
            holder.rb_order_evaluate.setClickable(false);
            holder.tv_sale_distance.setText(String.valueOf(new DecimalFormat("#0.0").format(businessInfo.distance)) + "km");
            holder.tv_business_sales_num.setText("月售" + (businessInfo.salesnum == null ? 0 : businessInfo.salesnum) + "单");
            holder.tv_sale_startPay.setText(businessInfo.startPay == null ? "起送 ¥ 0元" : "起送 ¥ " + String.valueOf(businessInfo.startPay));
            holder.tv_sale_time.setText(businessInfo.deliveryDuration + "分钟");

            holder.iv_gold_business.setVisibility(businessInfo.goldBusiness ? View.VISIBLE : View.GONE);

            holder.tv_sale_price.setText("配送费¥" + businessInfo.deliveryFee.divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());

            holder.tv_is_charge.setText(businessInfo.isDeliver == 0 ? "快车转送" : "商家配送");
            holder.tv_is_charge.setTextColor(businessInfo.isDeliver == 0 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.bg_44be99));
            holder.tv_is_charge.setBackgroundResource(businessInfo.isDeliver == 0 ? R.drawable.icon_orange_back : R.drawable.biankuang_44be99);

            final List<BusinessEvent> alist = businessInfo.activityList;
            holder.ll_contain_act.removeAllViews();
            holder.ll_contain_act.setTag(false);
            if (alist != null && alist.size() > 0) {
                for (int i = 0; i < alist.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                    ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                    TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                    tvAct.setText(alist.get(i).name);
                    showActImage(ivAct, alist.get(i));
                    if (i > 1) {
                        view.setVisibility(View.GONE);
                    }
                    holder.ll_contain_act.addView(view);
                }

                if (alist.size() > 2) {
                    holder.ll_contain_act.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
                }
            }

            holder.ll_contain_product.removeAllViews();
            holder.ll_contain_product.setTag(false);
            if (businessInfo.goodsSellList != null && businessInfo.goodsSellList.size() > 0) {

                for (int i = 0; i < businessInfo.goodsSellList.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_search_product, null);
                    ImageView ivFood = (ImageView) view.findViewById(R.id.iv_food);
                    TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                    TextView tvSummary = (TextView) view.findViewById(R.id.tv_summary);
                    TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
                    x.image().bind(ivFood, UrlConstant.ImageBaseUrl + businessInfo.goodsSellList.get(i).mini_imgPath, NetConfig.optionsLogoImage);
                    tvName.setText(businessInfo.goodsSellList.get(i).name);
                    tvSummary.setText("销量" + (businessInfo.goodsSellList.get(i).salesnum == null ? 0 : businessInfo.goodsSellList.get(i).salesnum));
                    tvPrice.setText("¥" + businessInfo.goodsSellList.get(i).price);
                    if (i > 1) {
                        view.setVisibility(View.GONE);
                    }

                    holder.ll_contain_product.addView(view);
                }
                holder.tv_show_product.setVisibility(businessInfo.goodsSellList.size() > 2 ? View.VISIBLE : View.GONE);
                holder.tv_show_product.setText("查看全部商品");
            } else {
                holder.tv_show_product.setVisibility(View.GONE);
            }

            holder.ll_contain_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alist.size() > 2) {
                        boolean showStatus = false;
                        for (int i = 0; i < alist.size(); i++) {
                            holder.ll_contain_act.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean) holder.ll_contain_act.getTag()) {
                                    holder.ll_contain_act.getChildAt(i).setVisibility(View.GONE);
                                    showStatus = false;
                                } else {
                                    holder.ll_contain_act.getChildAt(i).setVisibility(View.VISIBLE);
                                    showStatus = true;
                                }
                            }
                        }
                        holder.ll_contain_act.setTag(showStatus);
                    }

                }
            });

            holder.tv_show_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (businessInfo.goodsSellList.size() > 2) {
                        boolean isShowProduct = false;
                        for (int i = 0; i < businessInfo.goodsSellList.size(); i++) {
                            holder.ll_contain_product.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean) holder.ll_contain_product.getTag()) {
                                    holder.ll_contain_product.getChildAt(i).setVisibility(View.GONE);
                                    isShowProduct = false;
                                } else {
                                    holder.ll_contain_product.getChildAt(i).setVisibility(View.VISIBLE);
                                    isShowProduct = true;
                                }
                            }
                        }
                        holder.tv_show_product.setText(isShowProduct ? "收起" : "查看全部商品");
                        holder.ll_contain_product.setTag(isShowProduct);
                    }
                }
            });

            x.image().bind(holder.iv_business_logo, UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath, NetConfig.optionsPagerCache);
            //GlideUtils.loadImage(3,item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_item_movie_top));
            holder.layout_breakfast_item.setTag(position);
            holder.layout_breakfast_item.setOnClickListener(listener);
//            holder.ll_contain_product.setTag(position);
//            holder.ll_contain_product.setOnClickListener(listener);
        }
    }

    private void showActImage(ImageView ivAct, BusinessEvent businessEvent) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
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

    public class BreakfastViewHolder extends RecyclerView.ViewHolder {

        TextView tv_business_name, tv_business_levelId, tv_sale_distance,
                tv_business_sales_num, tv_sale_startPay, tv_sale_time, tv_sale_price, tv_show_product, tv_is_charge;

        ImageView iv_business_logo, iv_gold_business;

        LinearLayout ll_contain_act, layout_breakfast_item, ll_contain_product, ll_business_open, ll_business_close;

        BaseRatingBar rb_order_evaluate;
        View view_close;

        BreakfastViewHolder(View itemView) {
            super(itemView);
            tv_business_name = (TextView) itemView.findViewById(R.id.tv_business_name);
            tv_business_levelId = (TextView) itemView.findViewById(R.id.tv_business_levelId);
            tv_sale_distance = (TextView) itemView.findViewById(R.id.tv_sale_distance);
            tv_business_sales_num = (TextView) itemView.findViewById(R.id.tv_business_sales_num);
            tv_sale_startPay = (TextView) itemView.findViewById(R.id.tv_sale_startPay);
            tv_sale_time = (TextView) itemView.findViewById(R.id.tv_sale_time);
            tv_sale_price = (TextView) itemView.findViewById(R.id.tv_sale_price);
            tv_is_charge = (TextView) itemView.findViewById(R.id.tv_is_charge);
            iv_gold_business = (ImageView) itemView.findViewById(R.id.iv_gold_business);
            iv_business_logo = (ImageView) itemView.findViewById(R.id.iv_business_logo);

            rb_order_evaluate = (BaseRatingBar) itemView.findViewById(R.id.rb_order_evaluate);

            ll_contain_act = (LinearLayout) itemView.findViewById(R.id.ll_contain_act);
            ll_contain_product = (LinearLayout) itemView.findViewById(R.id.ll_contain_product);
            layout_breakfast_item = (LinearLayout) itemView.findViewById(R.id.layout_breakfast_item);
            ll_business_open = (LinearLayout) itemView.findViewById(R.id.ll_business_open);
            ll_business_close = (LinearLayout) itemView.findViewById(R.id.ll_business_close);
            tv_show_product = (TextView) itemView.findViewById(R.id.tv_show_product);
            view_close = itemView.findViewById(R.id.view_close);
        }
    }
}
