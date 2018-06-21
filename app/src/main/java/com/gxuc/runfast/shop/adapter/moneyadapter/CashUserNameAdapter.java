package com.gxuc.runfast.shop.adapter.moneyadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiliu on 2017/9/13.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class CashUserNameAdapter extends RecyclerView.Adapter<CashUserNameAdapter.CashUserNameViewHolder> {

    private List<CashBankInfo> cashBankInfoList;

    private Context context;

    private View.OnClickListener mListener;

    public CashUserNameAdapter(List<CashBankInfo> cashBankInfoList, Context context, View.OnClickListener listener) {
        this.cashBankInfoList = cashBankInfoList;
        this.context = context;
        mListener = listener;
    }

    @Override
    public CashUserNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank_info, parent, false);
        CashUserNameViewHolder holder = new CashUserNameViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CashUserNameViewHolder holder, int position) {
        CashBankInfo bankInfo = cashBankInfoList.get(position);
        if (bankInfo != null) {
            holder.tv_bank_name.setText(bankInfo.banktype);
            holder.tv_user_name.setText(bankInfo.name);
            String account = bankInfo.account;
            if (account.length() > 4){
                account = account.substring(account.length()-4);
            }
            holder.tv_bank_code.setText("**** **** **** "+account);
            holder.tv_bank_time.setText("添加日期 "+bankInfo.createTime);

            holder.iv_delete_bank.setTag(position);
            holder.iv_delete_bank.setOnClickListener(mListener);

        }
    }

    @Override
    public int getItemCount() {
        return cashBankInfoList == null ? 0 : cashBankInfoList.size();
    }

    public void setList(ArrayList<CashBankInfo> cashBankInfoList) {
        this.cashBankInfoList = cashBankInfoList;
        notifyDataSetChanged();
    }

    public class CashUserNameViewHolder extends RecyclerView.ViewHolder {

        TextView tv_bank_name,tv_user_name,tv_bank_code,tv_bank_time;
        ImageView iv_delete_bank;

        public CashUserNameViewHolder(View itemView) {
            super(itemView);
            tv_bank_name = (TextView) itemView.findViewById(R.id.tv_bank_name);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_bank_code = (TextView) itemView.findViewById(R.id.tv_bank_code);
            tv_bank_time = (TextView) itemView.findViewById(R.id.tv_bank_time);
            iv_delete_bank = (ImageView) itemView.findViewById(R.id.iv_delete_bank);
        }
    }
}
