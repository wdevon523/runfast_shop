package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.view.CenteredImageSpan;
import com.hedan.textdrawablelibrary.TextViewDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.AddressSelectViewHolder> implements View.OnClickListener {

    private List<AddressBean> addressBeanList;

    private Context context;

    private OnItemClickListener mOnItemClickListener = null;

    public AddressSelectAdapter(List<AddressBean> addressBeanList, Context context) {
        this.addressBeanList = addressBeanList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public AddressSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address_user_info, parent, false);
        AddressSelectViewHolder viewHolder = new AddressSelectViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddressSelectViewHolder holder, int position) {
        AddressBean addressBean = addressBeanList.get(position);
//        if (address != null) {
//            if (address.status == 1){
//                holder.title.setTextColor(context.getResources().getColor(R.color.color_orange_select));
//                holder.title.setText("[当前位置]"+address.title);
//            }else {
//                holder.title.setText(address.title);
//                holder.title.setTextColor(context.getResources().getColor(R.color.color_address_black));
//            }
//            holder.name.setText(address.address);

//        holder.tag.setVisibility(addressBean.tag == null || addressBean.tag < 0 || addressBean.tag > 3 ? View.GONE : View.VISIBLE);

        holder.title.setText(addressBean.userAddress + addressBean.address);
        holder.phone.setText(addressBean.phone);

        if (addressBean.tag != null) {
            if (addressBean.tag == 1) {
                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_home);
                SpannableString ss = new SpannableString("  " + addressBean.userAddress + addressBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.title.setText(ss);

//                holder.title.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_home), null, null, null);
//                holder.title.setCompoundDrawablePadding(8);

            } else if (addressBean.tag == 2) {
                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_company);
                SpannableString ss = new SpannableString("  " + addressBean.userAddress + addressBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.title.setText(ss);

//                holder.title.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_company), null, null, null);
//                holder.title.setCompoundDrawablePadding(8);
            } else if (addressBean.tag == 3) {
                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_school);
                SpannableString ss = new SpannableString("  " + addressBean.userAddress + addressBean.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.title.setText(ss);

//                holder.title.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_school), null, null, null);
//                holder.title.setCompoundDrawablePadding(8);
            }
        }

        String gender = "";
        if (addressBean.gender == null) {
            gender = "先生";
        } else {
            gender = addressBean.gender == 0 ? "女士" : "先生";
        }

        holder.name.setText(addressBean.name + "  " + gender);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return addressBeanList == null ? 0 : addressBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setList(ArrayList<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
        notifyDataSetChanged();
    }


    public class AddressSelectViewHolder extends RecyclerView.ViewHolder {

        public TextView tag, name, phone;
        public TextViewDrawable title;

        public AddressSelectViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tv_user_address_tag);
            title = (TextViewDrawable) itemView.findViewById(R.id.tv_user_title);
            name = (TextView) itemView.findViewById(R.id.tv_user_name);
            phone = (TextView) itemView.findViewById(R.id.tv_user_phone);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
