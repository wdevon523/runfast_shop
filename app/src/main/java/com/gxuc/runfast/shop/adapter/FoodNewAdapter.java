package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.view.AddWidget;

import org.xutils.x;

import java.math.BigDecimal;
import java.util.List;

public class FoodNewAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    private Context mContext;
    private List<FoodBean> flist;
    private AddWidget.OnAddClick onAddClick;
    private BusinessNewDetail businessDetail;

    public FoodNewAdapter(Context mContext, @Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
        super(R.layout.item_food_new, data);
        flist = data;
        this.onAddClick = onAddClick;
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FoodBean item) {
        businessDetail = ((BusinessNewActivity) mContext).getBusiness();
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_price, "¥" + item.getPrice().stripTrailingZeros().toPlainString())
                .setText(R.id.tv_sale, "销量" + item.getSalesnum())
                .setText(R.id.tv_discribe, item.getContent())
//                .setImageResource(R.id.iv_food, item.getIcon())
                .addOnClickListener(R.id.addwidget)
                .addOnClickListener(R.id.rl_spec)
                .addOnClickListener(R.id.food_main);

        x.image().bind((ImageView) helper.getView(R.id.iv_food), UrlConstant.ImageBaseUrl + item.getImgPath(), NetConfig.optionsLogoImage);

//        LogUtil.d("devonxxx" ,UrlConstant.ImageBaseUrl + item.getImgPath());

        helper.setText(R.id.tv_old_price, "¥" + item.getDisprice());

        if (item.getPrice().compareTo(item.getDisprice()) == 0) {
            helper.setVisible(R.id.tv_old_price, false);
            helper.setText(R.id.tv_price, "¥" + item.getPrice().stripTrailingZeros().toPlainString());
        } else {
            helper.setVisible(R.id.tv_old_price, true);
            helper.setText(R.id.tv_price, "¥" + item.getDisprice().stripTrailingZeros().toPlainString())
                    .setText(R.id.tv_old_price, "¥" + item.getPrice().stripTrailingZeros().toPlainString());
            ((TextView) helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }


        helper.setVisible(R.id.view_no_store, item.getNum() == 0)
                .setVisible(R.id.tv_no_food_store, item.getNum() == 0)
                .setVisible(R.id.addwidget, item.getGoodsSellStandardList().size() == 1 && item.getGoodsSellOptionList().size() == 0)
                .setVisible(R.id.rl_spec, item.getGoodsSellStandardList().size() > 1 || item.getGoodsSellOptionList().size() > 0);
//                .setVisible(R.id.addwidget, item.getGoodsSellStandardList().size() == 1)
//                .setVisible(R.id.rl_spec, item.getGoodsSellStandardList().size() > 1);

        helper.setVisible(R.id.tv_spec_num, item.getSelectCount() > 0)
                .setText(R.id.tv_spec_num, item.getSelectCount() + "");

        if (item.getIslimited() == 1) {
            helper.setVisible(R.id.tv_limit, true);
            helper.setText(R.id.tv_limit, "每单限购" + item.getLimitNum() + "件");
        } else {
            helper.setVisible(R.id.tv_limit, false);
        }

        AddWidget addWidget = helper.getView(R.id.addwidget);
//		addWidget.setData(this, helper.getAdapterPosition(), onAddClick);
        addWidget.setData(onAddClick, item);

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

        if (businessDetail != null && !businessDetail.isopen) {
            helper.setVisible(R.id.addwidget, false);
            helper.setVisible(R.id.rl_spec, false);
        }

//        getActStr(item);
    }

//    private void getActStr(FoodBean item) {
//
//
//    }


}