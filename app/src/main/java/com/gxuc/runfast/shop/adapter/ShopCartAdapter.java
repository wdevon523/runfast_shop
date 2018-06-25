package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.bean.CartItemsBean;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCartAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ShopCartBean> data;

    public ShopCartAdapter(Context context, List<ShopCartBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_business, parent, false);
        return new ShopCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShopCartViewHolder shopCartViewHolder = (ShopCartViewHolder) holder;
        final ShopCartBean shopCartBean = data.get(position);
        x.image().bind(shopCartViewHolder.ivShoppingCartBusinessLogo, UrlConstant.ImageBaseUrl + shopCartBean.businessImg, NetConfig.optionsLogoImage);
        shopCartViewHolder.tvShoppingCartBusinessName.setText(shopCartBean.businessName);
        shopCartViewHolder.tvShoppingCartBusinessName.setTag(shopCartBean.businessId);

        shopCartViewHolder.tvShoppingCartBusinessPackPrice.setText("¥" + shopCartBean.totalPackageFee);
        shopCartViewHolder.tvShoppingCartBusinessPreferentialPrice.setText("¥" + shopCartBean.offAmount);

        shopCartViewHolder.tvShoppingCartBusinessTotalPrice.setText("¥" + shopCartBean.cartPrice);

        shopCartViewHolder.llShoppingCartBusinessGoods.removeAllViews();

        ShopCartGoodAdapter shopCartGoodAdapter = new ShopCartGoodAdapter(context, shopCartBean.cartItems, shopCartBean.businessId);
        boolean isAllCheck = true;
        for (int i = 0; i < shopCartBean.cartItems.size(); i++) {
            shopCartViewHolder.llShoppingCartBusinessGoods.addView(shopCartGoodAdapter.getView(i, null, null));
            if (!shopCartBean.cartItems.get(i).checked) {
                isAllCheck = false;
            }
        }
        shopCartViewHolder.cbShoppingcartBusiness.setChecked(isAllCheck);
        shopCartViewHolder.cbShoppingcartBusiness.setTag(position);
        shopCartViewHolder.tvShoppingCartPay.setTag(position);

        shopCartViewHolder.tvShoppingCartBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessNewActivity.class);
                intent.putExtra("businessId", (int) v.getTag());
                context.startActivity(intent);
            }
        });

        shopCartGoodAdapter.setOnCheckListener(new ShopCartGoodAdapter.OnCheckListener() {
            @Override
            public void onChecked(int businessId, CartItemsBean cartItemsBean, int position) {
                if (mOnCheckListener != null) {
                    mOnCheckListener.onChecked(businessId, cartItemsBean, position);
                }
            }
        });

        shopCartViewHolder.cbShoppingcartBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopCartBean shopCartBean = data.get((int) v.getTag());
                if (mOnAllCheckListener != null) {
                    mOnAllCheckListener.onAllChecked(shopCartBean.businessId, ((CheckBox) v).isChecked());
                }
            }
        });

        shopCartViewHolder.tvShoppingCartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPayListener != null) {
                    mOnPayListener.onPay(shopCartBean.businessId, (int) v.getTag());
                }
            }
        });

        shopCartGoodAdapter.setOnDeleteListener(new ShopCartGoodAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int businessId, int position) {
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(businessId, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setList(ArrayList<ShopCartBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ShopCartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_too_far)
        LinearLayout llTooFar;
        @BindView(R.id.tv_out_of_range_address)
        TextView tvOutOfRangeAddress;
        @BindView(R.id.tv_open_close)
        TextView tvOpenClose;
        @BindView(R.id.ll_open_close)
        LinearLayout llOpenClose;
        @BindView(R.id.cb_shoppingcart_business)
        CheckBox cbShoppingcartBusiness;
        @BindView(R.id.iv_shopping_cart_business_logo)
        ImageView ivShoppingCartBusinessLogo;
        @BindView(R.id.tv_shopping_cart_business_name)
        TextView tvShoppingCartBusinessName;
        @BindView(R.id.ll_business)
        LinearLayout llBusiness;
        @BindView(R.id.ll_shopping_cart_business_act)
        LinearLayout llShoppingCartBusinessAct;
        @BindView(R.id.ll_shopping_cart_business_goods)
        LinearLayout llShoppingCartBusinessGoods;
        @BindView(R.id.tv_shopping_cart_business_pack_price)
        TextView tvShoppingCartBusinessPackPrice;
        @BindView(R.id.tv_shopping_cart_business_preferential_price)
        TextView tvShoppingCartBusinessPreferentialPrice;
        @BindView(R.id.rl_preferential)
        RelativeLayout rlPreferential;
        @BindView(R.id.tv_shopping_cart_business_total_preferential)
        TextView tvShoppingCartBusinessTotalPreferential;
        @BindView(R.id.tv_shopping_cart_pay)
        TextView tvShoppingCartPay;
        @BindView(R.id.tv_shopping_cart_business_total_price)
        TextView tvShoppingCartBusinessTotalPrice;

        ShopCartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private OnCheckListener mOnCheckListener;
    private OnAllCheckListener mOnAllCheckListener;
    private OnPayListener mOnPayListener;
    private OnDeleteListener mOnDeleteListener;

    public interface OnCheckListener {
        void onChecked(int businessId, CartItemsBean cartItemsBean, int position);
    }

    public void setOnCheckListener(OnCheckListener mOnCheckListener) {
        this.mOnCheckListener = mOnCheckListener;
    }

    public interface OnAllCheckListener {
        void onAllChecked(int businessId, boolean checked);
    }

    public void setOnAllCheckListener(OnAllCheckListener mOnAllCheckListener) {
        this.mOnAllCheckListener = mOnAllCheckListener;
    }

    public interface OnPayListener {
        void onPay(int businessId, int position);
    }

    public void setPayListener(OnPayListener mOnPayListener) {
        this.mOnPayListener = mOnPayListener;
    }

    public interface OnDeleteListener {
        void onDelete(int businessId, int position);
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }


}
