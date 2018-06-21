package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.FilterInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterSortAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FilterInfo> filterInfoList;
    private ArrayList<FilterInfo> list;

    public FilterSortAdapter(Context mContext, ArrayList<FilterInfo> filterInfoList) {
        this.mContext = mContext;
        this.filterInfoList = filterInfoList;
    }

    @Override
    public int getCount() {
        return filterInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterInfo filterInfo = filterInfoList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_sort, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFilterSortName.setText(filterInfo.name);
        viewHolder.tvFilterSortName.setTextColor(filterInfo.isCheck ? ContextCompat.getColor(mContext, R.color.text_ff9f14) : ContextCompat.getColor(mContext, R.color.text_666666));
        return convertView;
    }

    public void setList(ArrayList<FilterInfo> filterInfoList) {
        this.filterInfoList = filterInfoList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_filter_sort_name)
        TextView tvFilterSortName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}