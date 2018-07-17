package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.BusinessCategoryAdapter;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;

import java.util.ArrayList;

public class SameBusinessDialog extends Dialog {
    private Context context;
    private OnDialogClickListener listener;
    private GloriousRecyclerView recyclerView;
    private ArrayList<NearByBusinessInfo> data;
    private BusinessCategoryAdapter businessCategoryAdapter;

    public SameBusinessDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_same_business);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.TOP);

        recyclerView = (GloriousRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        businessCategoryAdapter = new BusinessCategoryAdapter(context, data);
        View topView = LayoutInflater.from(context).inflate(R.layout.item_dialog_same_business_top, recyclerView, false);

//        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(businessCategoryAdapter);
//        mHeaderAndFooterWrapper.addHeaderView(topView);
        recyclerView.setAdapter(businessCategoryAdapter);
        recyclerView.addHeaderView(topView);

        businessCategoryAdapter.setOnNearByBusinessClickListener(new BusinessCategoryAdapter.OnNearByBusinessClickListener() {
            @Override
            public void onNearByBusinessClickListener(int position, NearByBusinessInfo nearByBusinessInfo) {
                if (listener != null) {
                    listener.onDialogClick(position, nearByBusinessInfo);
                }
            }
        });
    }

    public void setData(ArrayList<NearByBusinessInfo> data) {
        businessCategoryAdapter.setList(data);
    }

    public interface OnDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param position
         */
        void onDialogClick(int position, NearByBusinessInfo nearByBusinessInfo);
    }
}
