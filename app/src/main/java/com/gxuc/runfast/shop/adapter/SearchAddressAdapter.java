package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.gxuc.runfast.shop.R;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAddressAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private List<PoiItem> list;
    private LatLng latLon;
    private OnItemClickListener listener;

    public SearchAddressAdapter(Context context, List<PoiItem> list, LatLng latLon, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.latLon = latLon;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_address, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PoiItem poiItem = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setTag(position);
        viewHolder.tvSearchAddressName.setText(poiItem.getTitle());
        viewHolder.tvSearchAddressDetail.setText(poiItem.getSnippet());
//        viewHolder.tvSearchAddressDistance.setText(poiItem.getDistance() + "米");
        float distancM = AMapUtils.calculateLineDistance(latLon, new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
        viewHolder.tvSearchAddressDistance.setText(distancM < 1000 ? String.format("%.2f", distancM) + "m" : String.format("%.2f", distancM / 1000f) + "km");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setPoiList(List<PoiItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //注意这里使用getTag方法获取position
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_search_address_name)
        TextView tvSearchAddressName;
        @BindView(R.id.tv_search_address_detail)
        TextView tvSearchAddressDetail;
        @BindView(R.id.tv_search_address_distance)
        TextView tvSearchAddressDistance;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
