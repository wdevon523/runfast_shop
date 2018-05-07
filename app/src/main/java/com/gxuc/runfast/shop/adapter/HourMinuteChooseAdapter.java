package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourMinuteChooseAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<String> data;
    private Context context;
    private OnHourMinuteChooseClickListener listener;
    private String day;
    private int clickPosition = -1;
    private String clickDay;


    public HourMinuteChooseAdapter(Context context, OnHourMinuteChooseClickListener listener, List<String> data, String day) {
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.day = day;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_hour_minute, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String hourMinute = data.get(position);
        viewHolder.itemView.setTag(position);
        viewHolder.itemView.setTag(R.id.tag_day, day);
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.tvHoutMinute.setText(hourMinute);
        viewHolder.tvHoutMinute.setTextColor(position == clickPosition && TextUtils.equals(day, clickDay) ? ContextCompat.getColor(context, R.color.text_4d89e6) : ContextCompat.getColor(context, R.color.text_333333));
        viewHolder.ivCheckTime.setVisibility(position == clickPosition && TextUtils.equals(day, clickDay) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setList(List<String> data, String day) {
        this.data = data;
        this.day = day;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //注意这里使用getTag方法获取position
            listener.onItemClick(v, (int) v.getTag());
            clickPosition = (int) v.getTag();
            clickDay = v.getTag(R.id.tag_day).toString();
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hout_minute)
        TextView tvHoutMinute;
        @BindView(R.id.iv_check_time)
        ImageView ivCheckTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    //define interface
    public interface OnHourMinuteChooseClickListener {
        void onItemClick(View view, int position);
    }
}
