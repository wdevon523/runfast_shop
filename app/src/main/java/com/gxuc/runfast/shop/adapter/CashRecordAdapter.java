package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.spend.WithdrawalRecord;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by huiliu on 2017/9/13.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class CashRecordAdapter extends RecyclerView.Adapter<CashRecordAdapter.CashRecordViewHolder> {

    private ArrayList<WithdrawalRecord> mRecordList;

    private Context context;

    private View.OnClickListener mListener;

    public CashRecordAdapter(ArrayList<WithdrawalRecord> mRecordList, Context context, View.OnClickListener listener) {
        this.mRecordList = mRecordList;
        this.context = context;
        mListener = listener;
    }

    @Override
    public CashRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_money_detail, parent, false);
        CashRecordViewHolder holder = new CashRecordViewHolder(view);
        view.setOnClickListener(mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(CashRecordViewHolder holder, int position) {
        WithdrawalRecord withdrawalRecord = mRecordList.get(position);

//        if (!TextUtils.isEmpty(WithdrawalRecord.account)){
            String account = withdrawalRecord.account;
            if (account.length() > 4){
                account = account.substring(account.length()-4);
            }
            holder.mTvMakeMoneyTitle.setText("提现至"+withdrawalRecord.banktype+"("+account+")");
//        }else {
//            holder.mTvMakeMoneyTitle.setText(WithdrawalRecord.getTypename());
//        }

        if (withdrawalRecord.monetary.compareTo(BigDecimal.ZERO) == -1){
            holder.mTvMakeMoneyNum.setText(String.valueOf(withdrawalRecord.monetary));
            holder.mTvMakeMoneyNum.setTextColor(context.getResources().getColor(R.color.color_address_black));
        }else {
            holder.mTvMakeMoneyNum.setText("+"+String.valueOf(withdrawalRecord.monetary));
            holder.mTvMakeMoneyNum.setTextColor(context.getResources().getColor(R.color.color_money_green));
        }
        holder.mTvMakeMoneyTime.setText(withdrawalRecord.createTime);
    }

    @Override
    public int getItemCount() {
        return mRecordList == null ? 0 : mRecordList.size();
    }

    public void setList(ArrayList<WithdrawalRecord> mRecordList) {
        this.mRecordList = mRecordList;
        notifyDataSetChanged();
    }

    public class CashRecordViewHolder extends RecyclerView.ViewHolder {

        TextView mTvMakeMoneyTitle;
        TextView mTvMakeMoneyTime;
        TextView mTvMakeMoneyNum;

        public CashRecordViewHolder(View itemView) {
            super(itemView);
            mTvMakeMoneyNum = (TextView) itemView.findViewById(R.id.tv_make_money_num);
            mTvMakeMoneyTitle = (TextView) itemView.findViewById(R.id.tv_make_money_title);
            mTvMakeMoneyTime = (TextView) itemView.findViewById(R.id.tv_make_money_time);
        }
    }
}
