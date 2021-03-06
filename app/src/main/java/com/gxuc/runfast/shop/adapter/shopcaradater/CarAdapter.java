package com.gxuc.runfast.shop.adapter.shopcaradater;


import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.view.AddWidget;
import com.gxuc.runfast.shop.R;

import java.math.BigDecimal;
import java.util.List;

public class CarAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
	private AddWidget.OnAddClick onAddClick;

	public CarAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
		super(R.layout.item_car, data);
		this.onAddClick = onAddClick;
	}

	@Override
	protected void convert(BaseViewHolder helper, FoodBean item) {
		helper.setText(R.id.car_name, item.getName())
				.setText(R.id.car_price, "¥" + item.getPrice().multiply(BigDecimal.valueOf(item.getSelectCount())));
		AddWidget addWidget = helper.getView(R.id.car_addwidget);
		addWidget.setData(onAddClick,item);
	}
}
