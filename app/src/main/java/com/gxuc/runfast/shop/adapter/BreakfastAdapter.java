package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.willy.ratingbar.ScaleRatingBar;

import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.BreakfastViewHolder> {

    private List<BusinessInfo> data;

    private Context context;

    private View.OnClickListener listener;

    public BreakfastAdapter(List<BusinessInfo> data, Context context, View.OnClickListener listener) {
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
        BusinessInfo businessInfo = data.get(position);
        if (businessInfo != null) {
            holder.tv_business_name.setText(businessInfo.name);
            holder.tv_business_levelId.setText(String.valueOf(businessInfo.levelId));
            holder.rb_order_evaluate.setRating(businessInfo.levelId);
            holder.rb_order_evaluate.setTouchable(false);
            holder.tv_sale_distance.setText(String.valueOf(new DecimalFormat("#0.0").format(businessInfo.distance)) + "km");
            holder.tv_business_sales_num.setText("月售" + String.valueOf(businessInfo.salesnum) + "单");
            holder.tv_sale_startPay.setText(businessInfo.startPay.isNaN() ? "起送 ¥ 0元" : "起送 ¥ " + String.valueOf(businessInfo.startPay));
            holder.tv_sale_time.setText(businessInfo.speed);

            if (businessInfo.isDeliver == 0) {
               holder.tv_sale_price.setText(businessInfo.charge.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.charge));
               holder.iv_is_charge.setVisibility(View.VISIBLE);
            } else {
               holder.tv_sale_price.setText(businessInfo.busshowps.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.busshowps));
               holder.iv_is_charge.setVisibility(View.GONE);
            }

            final List<BusinessExercise> alist = businessInfo.alist;
           holder.ll_contain_act.removeAllViews();
           holder.ll_contain_act.setTag(false);
            if (alist != null && alist.size() > 0) {
                for (int i = 0; i < alist.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                    ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                    TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                    tvAct.setText(alist.get(i).showname);
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

           holder.ll_contain_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alist.size() > 2) {
                        boolean showStatus = false;
                        for (int i = 0; i < alist.size(); i++) {
                           holder.ll_contain_act.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean)holder.ll_contain_act.getTag()) {
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

            x.image().bind(holder.iv_business_logo, UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath, NetConfig.optionsPagerCache);
            //GlideUtils.loadImage(3,item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_item_movie_top));
            holder.layout_breakfast_item.setTag(position);
           holder.layout_breakfast_item.setOnClickListener(listener);
        }
    }

    private void showActImage(ImageView ivAct, BusinessExercise businessExercise) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
        switch (businessExercise.ptype) {
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

    public class BreakfastViewHolder extends RecyclerView.ViewHolder {

        TextView tv_business_name, tv_business_levelId, tv_sale_distance,
                tv_business_sales_num, tv_sale_startPay, tv_sale_time, tv_sale_price;

        ImageView iv_is_charge, iv_business_logo;

        LinearLayout ll_contain_act, layout_breakfast_item;

        ScaleRatingBar rb_order_evaluate;

        BreakfastViewHolder(View itemView) {
            super(itemView);
            tv_business_name = (TextView) itemView.findViewById(R.id.tv_business_name);
            tv_business_levelId = (TextView) itemView.findViewById(R.id.tv_business_levelId);
            tv_sale_distance = (TextView) itemView.findViewById(R.id.tv_sale_distance);
            tv_business_sales_num = (TextView) itemView.findViewById(R.id.tv_business_sales_num);
            tv_sale_startPay = (TextView) itemView.findViewById(R.id.tv_sale_startPay);
            tv_sale_time = (TextView) itemView.findViewById(R.id.tv_sale_time);
            tv_sale_price = (TextView) itemView.findViewById(R.id.tv_sale_price);
            iv_is_charge = (ImageView) itemView.findViewById(R.id.iv_is_charge);
            iv_business_logo = (ImageView) itemView.findViewById(R.id.iv_business_logo);

            rb_order_evaluate = (ScaleRatingBar) itemView.findViewById(R.id.rb_order_evaluate);

            ll_contain_act = (LinearLayout) itemView.findViewById(R.id.ll_contain_act);
            layout_breakfast_item = (LinearLayout) itemView.findViewById(R.id.layout_breakfast_item);
        }
    }
}
