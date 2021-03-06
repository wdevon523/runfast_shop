package com.gxuc.runfast.shop.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.CartItemsBean;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderGoodsNewAdapter extends BaseAdapter {
    private Activity context = null;
    private List<CartItemsBean> orderGoodsList;

    public OrderGoodsNewAdapter(Activity context, List<CartItemsBean> orderGoodsList) {
        this.context = context;
        this.orderGoodsList = orderGoodsList;
    }

    @Override
    public int getCount() {
        return orderGoodsList == null ? 0 : orderGoodsList.size();
    }

    @Override
    public CartItemsBean getItem(int position) {
        return orderGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CartItemsBean cartItemsBean = orderGoodsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_detail_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(viewHolder.ivOrderDetailGoodImage, UrlConstant.ImageBaseUrl + cartItemsBean.goodsImg, NetConfig.optionsLogoImage);
        viewHolder.tvOrderDetailGoodName.setText(cartItemsBean.goodsName);
        viewHolder.tvOrderDetailGoodNum.setText("x" + cartItemsBean.num);

        viewHolder.tvOrderDetailGoodSpec.setText(cartItemsBean.standarOptionName);

        viewHolder.tvOrderDetailGoodOldPrice.setVisibility(cartItemsBean.totalDisprice == null || cartItemsBean.totalDisprice.compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);

        viewHolder.tvOrderDetailGoodPrice.setText("¥ " + (cartItemsBean.totalDisprice == null || cartItemsBean.totalDisprice.compareTo(BigDecimal.ZERO) == 0 ? cartItemsBean.totalPrice.stripTrailingZeros().toPlainString() : cartItemsBean.totalDisprice.stripTrailingZeros().toPlainString()));
        viewHolder.tvOrderDetailGoodOldPrice.setText("¥ " + cartItemsBean.totalPrice.stripTrailingZeros().toPlainString());
        viewHolder.tvOrderDetailGoodOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

//        if (goodsSellRecordChildren.getActivityType() != null) {
//            viewHolder.llOrderAct.setVisibility(View.VISIBLE);
//            viewHolder.tvOrderDetailGoodAct.setText(goodsSellRecordChildren.getGoods());
//            showActImage(viewHolder.ivOrderDetailGoodAct, goodsSellRecordChildren);
//        } else {
//            viewHolder.llOrderAct.setVisibility(View.GONE);
//        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_order_detail_goods_image)
        ImageView ivOrderDetailGoodImage;
        @BindView(R.id.tv_order_detail_good_name)
        TextView tvOrderDetailGoodName;
        @BindView(R.id.tv_order_detail_good_spec)
        TextView tvOrderDetailGoodSpec;
        @BindView(R.id.tv_order_detail_good_num)
        TextView tvOrderDetailGoodNum;
        @BindView(R.id.tv_order_detail_good_price)
        TextView tvOrderDetailGoodPrice;
        @BindView(R.id.tv_order_detail_good_old_price)
        TextView tvOrderDetailGoodOldPrice;
        @BindView(R.id.ll_order_act)
        LinearLayout llOrderAct;
        @BindView(R.id.iv_order_detail_good_act)
        ImageView ivOrderDetailGoodAct;
        @BindView(R.id.tv_order_detail_good_act)
        TextView tvOrderDetailGoodAct;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

//    private void showActImage(ImageView ivAct, ShopCartBean.CartItemsBean goodsSellRecordChildren) {
//        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
//        switch (goodsSellRecordChildren.getActivityType()) {
//            case 1:
//                ivAct.setImageResource(R.drawable.icon_reduce);
//                break;
//            case 2:
//                ivAct.setImageResource(R.drawable.icon_fracture);
//                break;
//            case 3:
//                ivAct.setImageResource(R.drawable.icon_give);
//                break;
//            case 4:
//                ivAct.setImageResource(R.drawable.icon_special);
//                break;
//            case 5:
//                ivAct.setImageResource(R.drawable.icon_free);
//                break;
//            case 6:
//                ivAct.setImageResource(R.drawable.icon_coupon);
//                break;
//        }
//    }
}
