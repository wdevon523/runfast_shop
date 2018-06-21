package com.gxuc.runfast.shop.fragment;


import android.os.Bundle;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.adapter.FoodNewAdapter;
import com.gxuc.runfast.shop.adapter.TypeNewAdapter;
import com.gxuc.runfast.shop.view.ListContainer;
import com.shizhefei.fragment.LazyFragment;

public class FirstFragment extends LazyFragment {

	private ListContainer listContainer;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_first);
		listContainer = (ListContainer) findViewById(R.id.listcontainer);
		listContainer.setAddClick((BusinessNewActivity) getActivity());
	}

	public FoodNewAdapter getFoodAdapter() {
		return listContainer.foodAdapter;
	}

	public TypeNewAdapter getTypeAdapter() {
		return listContainer.typeAdapter;
	}

}
