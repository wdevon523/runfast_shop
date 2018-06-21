package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.BusinessCategoryInfo;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class BreakfastClassAdapter extends RecyclerView.Adapter<BreakfastClassAdapter.BreakfastClassViewHolder> {

    private List<BusinessCategoryInfo> businessCategoryInfoList;

    private Context context;

    private View.OnClickListener listener;

    public BreakfastClassAdapter(List<BusinessCategoryInfo> businessCategoryInfoList, Context context, View.OnClickListener listener) {
        this.businessCategoryInfoList = businessCategoryInfoList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public BreakfastClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class_select, parent, false);
        BreakfastClassViewHolder viewHolder = new BreakfastClassViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BreakfastClassViewHolder holder, int position) {
        BusinessCategoryInfo businessCategoryInfo = businessCategoryInfoList.get(position);
        if (businessCategoryInfo != null) {
            holder.tvName.setText(businessCategoryInfo.name);
//            holder.ivType.setImageResource(classTypeInfo.imgId);
            if (businessCategoryInfo.isSelect) {
                holder.tvName.setTextColor(context.getResources().getColor(R.color.color_orange_select));
                holder.ivSelect.setVisibility(View.VISIBLE);
            } else {
                holder.tvName.setTextColor(context.getResources().getColor(R.color.color_text_gray_two));
                holder.ivSelect.setVisibility(View.GONE);
            }
            holder.layoutSelect.setTag(position);
            holder.layoutSelect.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return businessCategoryInfoList == null ? 0 : businessCategoryInfoList.size();
    }

    public void setList(ArrayList<BusinessCategoryInfo> businessCategoryInfoList) {
        this.businessCategoryInfoList = businessCategoryInfoList;
        notifyDataSetChanged();
    }

    public class BreakfastClassViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutSelect;
        public ImageView ivType, ivSelect;
        public TextView tvName;

        public BreakfastClassViewHolder(View itemView) {
            super(itemView);
            layoutSelect = (LinearLayout) itemView.findViewById(R.id.layout_class_select);
            ivType = (ImageView) itemView.findViewById(R.id.iv_class_type);
            ivSelect = (ImageView) itemView.findViewById(R.id.iv_class_select);
            tvName = (TextView) itemView.findViewById(R.id.tv_class_name);
        }
    }
}
