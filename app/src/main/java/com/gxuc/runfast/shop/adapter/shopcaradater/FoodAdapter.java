package com.gxuc.runfast.shop.adapter.shopcaradater;


import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.bean.business.BusinessDetail;
import com.gxuc.runfast.shop.view.AddWidget;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.R;

import java.math.BigDecimal;
import java.util.List;

public class FoodAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    private List<FoodBean> flist;
    private AddWidget.OnAddClick onAddClick;
    private Context context;
    private View.OnClickListener listener;
    private BusinessDetail businessDetail;

    public FoodAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick, Context context, View.OnClickListener listener) {
        super(R.layout.item_food, data);
        flist = data;
        this.onAddClick = onAddClick;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FoodBean item) {

        businessDetail = ((BusinessActivity) context).getBusiness();
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_summary, "月售" + item.getSale());

        helper.setVisible(R.id.view_no_store, item.getNum() == 0);

        if (item.getIslimited() == 1 || (!TextUtils.isEmpty(item.getShowprice()) && !TextUtils.equals("null", item.getShowprice()))) {
            helper.setVisible(R.id.ll_food_act, true);

            if ((!TextUtils.isEmpty(item.getShowprice()) && !TextUtils.equals("null", item.getShowprice()))) {
                helper.setVisible(R.id.tv_food_discount, true);
                helper.setText(R.id.tv_food_discount, item.getShowprice());
            } else {
                helper.setVisible(R.id.tv_food_discount, false);
            }

            if (item.getIslimited() == 1) {
                helper.setVisible(R.id.tv_food_limit, true);
                helper.setText(R.id.tv_food_limit, "限购" + item.getLimitNum() + "件");
            } else {
                helper.setVisible(R.id.tv_food_limit, false);
            }

        } else {
            helper.setVisible(R.id.ll_food_act, false);
        }


        if (!TextUtils.isEmpty(item.getShowzs()) && !TextUtils.equals("null", item.getShowzs())) {
            helper.setVisible(R.id.tv_gift_name, true);
            helper.setText(R.id.tv_gift_name, item.getShowzs());
        } else {
            helper.setVisible(R.id.tv_gift_name, false);
        }

        if (!TextUtils.isEmpty(item.getDisprice()) && !TextUtils.equals("0", item.getDisprice())) {
            helper.setText(R.id.tv_price, "¥" + item.getDisprice());
            helper.setText(R.id.tv_old_price, "¥" + item.getPrice());
            helper.setVisible(R.id.tv_old_price, true);
        } else {
            helper.setText(R.id.tv_price, "¥" + item.getPrice());
            helper.setVisible(R.id.tv_old_price, false);
        }

        ((TextView) helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        helper.setVisible(R.id.layout_spec, item.getIsonly() == 1);
        helper.setVisible(R.id.tv_spec_num, item.getSelectCount() > 0)
                .setText(R.id.tv_spec_num, item.getSelectCount() + "");

        helper.setVisible(R.id.addwidget, item.getIsonly() == 0);

        Glide.with(context)
                .load(UrlConstant.ImageBaseUrl + item.getIcon())
                .placeholder(R.drawable.icon_default_head)
                .error(R.drawable.icon_default_head)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into((ImageView) helper.getView(R.id.iv_food));
        //x.image().bind((ImageView) helper.getView(R.id.iv_food), UrlConstant.ImageBaseUrl + item.getIcon(), NetConfig.optionsPagerCache);

        AddWidget addWidget = helper.getView(R.id.addwidget);
        addWidget.setData(this, helper.getAdapterPosition(), onAddClick);

        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.stick_header, true)
                    .setText(R.id.tv_header, item.getType())
                    .setTag(R.id.food_main, FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(item.getType(), flist.get(helper.getAdapterPosition() - 1).getType())) {
                helper.setVisible(R.id.stick_header, true)
                        .setText(R.id.tv_header, item.getType())
                        .setTag(R.id.food_main, HAS_STICKY_VIEW);
            } else {
                helper.setVisible(R.id.stick_header, false)
                        .setTag(R.id.food_main, NONE_STICKY_VIEW);
            }
        }
        helper.getConvertView().setContentDescription(item.getType());

        helper.setTag(R.id.food_main, helper.getPosition());
        helper.setOnClickListener(R.id.food_main, listener);

        helper.setTag(R.id.layout_spec, helper.getPosition());
        helper.setOnClickListener(R.id.layout_spec, listener);

        if (businessDetail != null && businessDetail.getIsopen() != 0) {
            helper.setVisible(R.id.addwidget, false);
            helper.setVisible(R.id.layout_spec, false);
        }

        //context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("food",item));
    }

}
