package com.gxuc.runfast.shop.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.order.GoodsSellRecordChildren;
import com.gxuc.runfast.shop.R;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Devon on 2017/9/28.
 */

public class OrderGoodsAdapter extends BaseAdapter {
    private Activity context = null;
    private List<GoodsSellRecordChildren> orderGoodsList;

    public OrderGoodsAdapter(Activity context, List<GoodsSellRecordChildren> orderGoodsList) {
        this.context = context;
        this.orderGoodsList = orderGoodsList;
    }

    @Override
    public int getCount() {
        return orderGoodsList == null ? 0 : orderGoodsList.size();
    }

    @Override
    public GoodsSellRecordChildren getItem(int position) {
        return orderGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        GoodsSellRecordChildren goodsSellRecordChildren = orderGoodsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_detail_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvOrderDetailGoodName.setText(goodsSellRecordChildren.getGoodsSellName());
        viewHolder.tvOrderDetailGoodNum.setText("x" + goodsSellRecordChildren.getNum());
        String spec = "";
        if (goodsSellRecordChildren.getGoodsSellStandardName() != null) {
            spec = goodsSellRecordChildren.getGoodsSellStandardName();
        }
        if (goodsSellRecordChildren.getGoodsSellOptionName() != null) {
            spec = spec + goodsSellRecordChildren.getGoodsSellOptionName();
        }
//        viewHolder.tvOrderDetailGoodSpec.setText(goodsSellRecordChildren.getGoodsSellStandardName() + " " + goodsSellRecordChildren.getGoodsSellOptionName());
        viewHolder.tvOrderDetailGoodSpec.setText(spec);
        BigDecimal prudoctTotalPrice = goodsSellRecordChildren.getPrice().multiply(new BigDecimal(String.valueOf(goodsSellRecordChildren.getNum())));
        viewHolder.tvOrderDetailGoodPrice.setText("¥ " + prudoctTotalPrice);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_order_detail_good_name)
        TextView tvOrderDetailGoodName;
        @BindView(R.id.tv_order_detail_good_spec)
        TextView tvOrderDetailGoodSpec;
        @BindView(R.id.tv_order_detail_good_num)
        TextView tvOrderDetailGoodNum;
        @BindView(R.id.tv_order_detail_good_price)
        TextView tvOrderDetailGoodPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
