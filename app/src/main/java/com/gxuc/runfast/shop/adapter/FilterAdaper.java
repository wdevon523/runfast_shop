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

public class FilterAdaper extends BaseAdapter {
    private Context mContext;
    private ArrayList<FilterInfo> filterInfoList;

    public FilterAdaper(Context mContext, ArrayList<FilterInfo> filterInfoList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFilterName.setText(filterInfo.name);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_filter_logo)
        ImageView ivFilterLogo;
        @BindView(R.id.tv_filter_name)
        TextView tvFilterName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
