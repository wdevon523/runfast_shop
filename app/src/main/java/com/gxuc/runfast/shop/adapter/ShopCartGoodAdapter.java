package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.CartItemsBean;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCartGoodAdapter extends BaseAdapter {

    private Context context = null;
    private List<CartItemsBean> cartItemsBeanList;
    private int businessId;

    public ShopCartGoodAdapter(Context context, List<CartItemsBean> cartItemsBeanList, int businessId) {
        this.context = context;
        this.cartItemsBeanList = cartItemsBeanList;
        this.businessId = businessId;
    }

    @Override
    public int getCount() {
        return cartItemsBeanList == null ? 0 : cartItemsBeanList.size();
    }

    @Override
    public CartItemsBean getItem(int position) {
        return cartItemsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final CartItemsBean cartItemsBean = cartItemsBeanList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(viewHolder.ivShoppingCartGoodLogo, UrlConstant.ImageBaseUrl + cartItemsBean.goodsImg, NetConfig.optionsLogoImage);
        viewHolder.cbShoppingcartGoods.setChecked(cartItemsBean.checked);
        viewHolder.tvShoppingCartGoodName.setText(cartItemsBean.goodsName);
        viewHolder.tvShoppingCartGoodNum.setText("x" + cartItemsBean.num);
        viewHolder.tvShoppingCartGoodSpec.setText(cartItemsBean.standarOptionName);

        if (cartItemsBean.totalDisprice == null) {
            viewHolder.tvShoppingCartGoodOldprice.setVisibility(View.GONE);
            viewHolder.tvShoppingCartGoodDisprice.setText("¥" + cartItemsBean.totalPrice);
        } else {
            viewHolder.tvShoppingCartGoodOldprice.setVisibility(View.VISIBLE);
            viewHolder.tvShoppingCartGoodDisprice.setText("¥" + cartItemsBean.totalDisprice);
            viewHolder.tvShoppingCartGoodOldprice.setText("¥" + cartItemsBean.totalPrice);
            viewHolder.tvShoppingCartGoodOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.cbShoppingcartGoods.setTag(position);

        viewHolder.cbShoppingcartGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCheckListener != null) {
                    CartItemsBean cartItemsBean = cartItemsBeanList.get((int) v.getTag());
                    cartItemsBean.checked = !cartItemsBean.checked;
                    mOnCheckListener.onChecked(businessId, cartItemsBean, (int) v.getTag());
                }
            }
        });

        viewHolder.tvDeleteOrder.setTag(position);

        viewHolder.tvDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(businessId, (int) v.getTag());
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.cb_shoppingcart_goods)
        CheckBox cbShoppingcartGoods;
        @BindView(R.id.iv_shopping_cart_good_logo)
        ImageView ivShoppingCartGoodLogo;
        @BindView(R.id.tv_shopping_cart_good_name)
        TextView tvShoppingCartGoodName;
        @BindView(R.id.tv_shopping_cart_good_spec)
        TextView tvShoppingCartGoodSpec;
        @BindView(R.id.tv_shopping_cart_good_num)
        TextView tvShoppingCartGoodNum;
        @BindView(R.id.tv_shopping_cart_good_disprice)
        TextView tvShoppingCartGoodDisprice;
        @BindView(R.id.tv_shopping_cart_good_oldprice)
        TextView tvShoppingCartGoodOldprice;
        @BindView(R.id.tv_delete_order)
        Button tvDeleteOrder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private OnCheckListener mOnCheckListener;
    private OnDeleteListener mOnDeleteListener;

    public interface OnCheckListener {
        void onChecked(int businessId, CartItemsBean cartItemsBean, int position);
    }

    public void setOnCheckListener(OnCheckListener mOnCheckListener) {
        this.mOnCheckListener = mOnCheckListener;
    }

    public interface OnDeleteListener {
        void onDelete(int businessId, int position);
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }


}
