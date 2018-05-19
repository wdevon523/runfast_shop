package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.home.OffZoneGoodsInfo;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffZoneGoodAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OffZoneGoodsInfo> offZoneGoodsList;

    public OffZoneGoodAdapter(Context context, ArrayList<OffZoneGoodsInfo> offZoneGoodsList) {
        this.context = context;
        this.offZoneGoodsList = offZoneGoodsList;
    }

    @Override
    public int getCount() {
        return offZoneGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return offZoneGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        OffZoneGoodsInfo offZoneGoodsInfo = offZoneGoodsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_off_zone_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        x.image().bind(viewHolder.ivOffZoneGoodLogo, UrlConstant.ImageBaseUrl + offZoneGoodsInfo.gs_mini_imgPath);
        x.image().bind(viewHolder.ivOffZoneBusinessLogo, UrlConstant.ImageBaseUrl + offZoneGoodsInfo.busMiniImgPath);

        viewHolder.tvOffZoneName.setText(offZoneGoodsInfo.a_specialName);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_off_zone_good_logo)
        ImageView ivOffZoneGoodLogo;
        @BindView(R.id.iv_off_zone_business_logo)
        ImageView ivOffZoneBusinessLogo;
        @BindView(R.id.tv_off_zone_name)
        TextView tvOffZoneName;
        @BindView(R.id.rl_shadow)
        RelativeLayout rlShadow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
