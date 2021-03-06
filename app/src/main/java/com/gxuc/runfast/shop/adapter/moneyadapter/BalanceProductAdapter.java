package com.gxuc.runfast.shop.adapter.moneyadapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.order.ShoppingCartGoodsInfo;
import com.gxuc.runfast.shop.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算商品列表适配
 * Created by 天上白玉京 on 2017/8/5.
 */

public class BalanceProductAdapter extends RecyclerView.Adapter<BalanceProductAdapter.HistorySearchViewHolder> {

    private List<ShoppingCartGoodsInfo> data;

    private Context context;

    public BalanceProductAdapter(List<ShoppingCartGoodsInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<ShoppingCartGoodsInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public HistorySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
        HistorySearchViewHolder holder = new HistorySearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistorySearchViewHolder holder, int position) {
        ShoppingCartGoodsInfo shoppingCartGoodsInfo = data.get(position);
        if (shoppingCartGoodsInfo != null) {
            holder.tvProductName.setText(shoppingCartGoodsInfo.goodsSellName);
            if (TextUtils.equals(shoppingCartGoodsInfo.goodsSellName, "配送费")) {
                holder.tvProductNum.setText((TextUtils.isEmpty(shoppingCartGoodsInfo.num) || TextUtils.equals(shoppingCartGoodsInfo.num, shoppingCartGoodsInfo.price)) ? "" : "¥ " + shoppingCartGoodsInfo.num);
                holder.tvProductNum.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else {
                holder.tvProductNum.setText(TextUtils.isEmpty(shoppingCartGoodsInfo.num) ? "" : "x" + shoppingCartGoodsInfo.num);
                holder.tvProductNum.getPaint().setFlags(0);
            }

            if (!TextUtils.isEmpty(shoppingCartGoodsInfo.price)) {
                if (TextUtils.equals(shoppingCartGoodsInfo.goodsSellName, "优惠券")) {
                    holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_balance_price));
                    holder.tvProductPrice.setText(" - ¥ " + shoppingCartGoodsInfo.price);
                } else {
                    if (!TextUtils.isEmpty(shoppingCartGoodsInfo.num) && !TextUtils.equals(shoppingCartGoodsInfo.goodsSellName, "配送费")) {
//                        BigDecimal prudoctTotalPrice = new BigDecimal(shoppingCartGoodsInfo.price).multiply(new BigDecimal(String.valueOf(shoppingCartGoodsInfo.num)));
                        holder.tvProductPrice.setText("¥ " + shoppingCartGoodsInfo.showprice);
                    } else {
                        holder.tvProductPrice.setText("¥ " + shoppingCartGoodsInfo.price);
                    }
                }
            } else {
                holder.tvProductPrice.setText("¥ 0");
            }
            if (shoppingCartGoodsInfo.goodsSellOptionName != null) {
                holder.tvProductSpec.setText(shoppingCartGoodsInfo.goodsSellOptionName);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class HistorySearchViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvProductNum, tvProductPrice, tvProductSpec;

        public HistorySearchViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvProductSpec = (TextView) itemView.findViewById(R.id.tv_product_spec);
            tvProductNum = (TextView) itemView.findViewById(R.id.tv_product_num);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tv_product_price);
        }
    }
}
