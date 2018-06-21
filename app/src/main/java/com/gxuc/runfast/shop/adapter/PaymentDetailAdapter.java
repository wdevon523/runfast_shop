package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.spend.AccountRecord;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.PaymentDetailViewHolder> {

    private ArrayList<AccountRecord> mRecordList;

    private Context context;

    private View.OnClickListener mListener;

    public PaymentDetailAdapter(ArrayList<AccountRecord> mRecordList, Context context, View.OnClickListener listener) {
        this.mRecordList = mRecordList;
        this.context = context;
        mListener = listener;
    }

    @Override
    public PaymentDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_money_detail, parent, false);
        PaymentDetailViewHolder holder = new PaymentDetailViewHolder(view);
        view.setOnClickListener(mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(PaymentDetailViewHolder holder, int position) {
        AccountRecord accountRecord = mRecordList.get(position);

        holder.mTvMakeMoneyTitle.setText(accountRecord.typename);

        if (accountRecord.monetary.compareTo(BigDecimal.ZERO) == -1) {
            holder.mTvMakeMoneyNum.setText(String.valueOf(accountRecord.monetary));
            holder.mTvMakeMoneyNum.setTextColor(context.getResources().getColor(R.color.color_address_black));
        } else {
            holder.mTvMakeMoneyNum.setText("+" + String.valueOf(accountRecord.monetary));
            holder.mTvMakeMoneyNum.setTextColor(context.getResources().getColor(R.color.color_money_green));
        }
        holder.mTvMakeMoneyTime.setText(accountRecord.createTime);
    }

    @Override
    public int getItemCount() {
        return mRecordList == null ? 0 : mRecordList.size();
    }

    public void setList(ArrayList<AccountRecord> mRecordList) {
        this.mRecordList = mRecordList;
        notifyDataSetChanged();
    }

    public class PaymentDetailViewHolder extends RecyclerView.ViewHolder {

        TextView mTvMakeMoneyTitle;
        TextView mTvMakeMoneyTime;
        TextView mTvMakeMoneyNum;

        public PaymentDetailViewHolder(View itemView) {
            super(itemView);
            mTvMakeMoneyNum = (TextView) itemView.findViewById(R.id.tv_make_money_num);
            mTvMakeMoneyTitle = (TextView) itemView.findViewById(R.id.tv_make_money_title);
            mTvMakeMoneyTime = (TextView) itemView.findViewById(R.id.tv_make_money_time);
        }
    }
}
