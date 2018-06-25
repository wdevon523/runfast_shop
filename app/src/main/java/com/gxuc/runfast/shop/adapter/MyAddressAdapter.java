package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.view.CenteredImageSpan;
import com.hedan.textdrawablelibrary.TextViewDrawable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAddressAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddressBean> addressInfoList;
    private OnMyAddressClickLstener mOnMyAddressClickLstener;

    public MyAddressAdapter(Context context, ArrayList<AddressBean> addressInfoList) {
        this.context = context;
        this.addressInfoList = addressInfoList;
    }

    @Override
    public int getCount() {
        return addressInfoList == null ? 0 : addressInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        AddressBean addressInfo = addressInfoList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_my_address, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.tvMyAddressTag.setVisibility(addressInfo.tag == null || addressInfo.tag < 0 || addressInfo.tag > 3 ? View.GONE : View.VISIBLE);

        String gender = "";
        if (addressInfo.gender == null) {
            gender = "先生";
        } else {
            gender = addressInfo.gender == 0 ? "女士" : "先生";
        }

        viewHolder.tvMyName.setText(addressInfo.name + "  " + gender);
        viewHolder.tvMyMobile.setText(addressInfo.phone);
        viewHolder.tvMyAddressDetail.setText(addressInfo.userAddress + addressInfo.address);

        if (addressInfo.tag != null) {
            if (addressInfo.tag == 1) {
                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_home);
                SpannableString ss = new SpannableString("  " + addressInfo.userAddress + addressInfo.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.tvMyAddressDetail.setText(ss);

//                viewHolder.tvMyAddressDetail.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_home), null, null, null);
//                viewHolder.tvMyAddressDetail.setCompoundDrawablePadding(8);
            } else if (addressInfo.tag == 2) {

                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_company);
                SpannableString ss = new SpannableString("  " + addressInfo.userAddress + addressInfo.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.tvMyAddressDetail.setText(ss);

//                viewHolder.tvMyAddressDetail.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_company), null, null, null);
//                viewHolder.tvMyAddressDetail.setCompoundDrawablePadding(8);
            } else if (addressInfo.tag == 3) {
                CenteredImageSpan span = new CenteredImageSpan(context, R.drawable.icon_school);
                SpannableString ss = new SpannableString("  " + addressInfo.userAddress + addressInfo.address);
                ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.tvMyAddressDetail.setText(ss);
//                viewHolder.tvMyAddressDetail.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_school), null, null, null);
//                viewHolder.tvMyAddressDetail.setCompoundDrawablePadding(8);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMyAddressClickLstener != null) {
                    mOnMyAddressClickLstener.onMyAddressClick(position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_my_address_tag)
        TextView tvMyAddressTag;
        @BindView(R.id.tv_my_address_detail)
        TextViewDrawable tvMyAddressDetail;
        @BindView(R.id.tv_my_name)
        TextView tvMyName;
        @BindView(R.id.tv_my_mobile)
        TextView tvMyMobile;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnMyAddressClickLstener {
        void onMyAddressClick(int position);
    }

    public void setOnMyAddressClickLstener(OnMyAddressClickLstener mOnMyAddressClickLstener) {
        this.mOnMyAddressClickLstener = mOnMyAddressClickLstener;
    }
}
