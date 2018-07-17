package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.OrderTimeBean;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderTimeChooseAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<OrderTimeBean> data;
    private Context context;
    private OnOrderTimeChooseClickListener listener;
    private String day;
    private int clickPosition = -1;
    private String clickDay;


    public OrderTimeChooseAdapter(Context context, OnOrderTimeChooseClickListener listener, List<OrderTimeBean> data) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_time_dialog, parent, false);
        return new OrderTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderTimeViewHolder orderTimeViewHolder = (OrderTimeViewHolder) holder;
        OrderTimeBean orderTimeBean = data.get(position);
        orderTimeViewHolder.itemView.setTag(position);
        orderTimeViewHolder.itemView.setTag(R.id.tag_day, day);
        orderTimeViewHolder.itemView.setOnClickListener(this);
        orderTimeViewHolder.tvHoutMinute.setText(orderTimeBean.hourMinute);
        orderTimeViewHolder.tvDeliveryCost.setText(new BigDecimal(orderTimeBean.deliveryFee).stripTrailingZeros().toPlainString() + "元配送费");
        orderTimeViewHolder.tvHoutMinute.setTextColor(position == clickPosition && TextUtils.equals(day, clickDay) ? ContextCompat.getColor(context, R.color.text_4d89e6) : ContextCompat.getColor(context, R.color.text_333333));
        orderTimeViewHolder.ivCheckTime.setVisibility(position == clickPosition && TextUtils.equals(day, clickDay) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setList(List<OrderTimeBean> timesList, String day) {
        this.data = data;
        this.day = day;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //注意这里使用getTag方法获取position
            clickDay = v.getTag(R.id.tag_day).toString();
            clickPosition = (int) v.getTag();
            listener.onItemClick(v, clickPosition, clickDay);
            notifyDataSetChanged();
        }
    }

    //define interface
    public interface OnOrderTimeChooseClickListener {
        void onItemClick(View view, int position, String clickDay);
    }

    class OrderTimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hout_minute)
        TextView tvHoutMinute;
        @BindView(R.id.tv_delivery_cost)
        TextView tvDeliveryCost;
        @BindView(R.id.iv_check_time)
        ImageView ivCheckTime;

        OrderTimeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
