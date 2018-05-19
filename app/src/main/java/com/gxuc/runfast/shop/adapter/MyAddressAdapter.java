package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.address.AddressInfo;
import com.gxuc.runfast.shop.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAddressAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddressInfo> addressInfoList;
    private OnMyAddressClickLstener mOnMyAddressClickLstener;

    public MyAddressAdapter(Context context, ArrayList<AddressInfo> addressInfoList) {
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
        AddressInfo addressInfo = addressInfoList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_my_address, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMyName.setText(addressInfo.getName());
        viewHolder.tvMyMobile.setText(addressInfo.getMobile());
        viewHolder.tvMyAddressDetail.setText(addressInfo.getUserAddress() + addressInfo.getAddress());
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
        @BindView(R.id.tv_my_address_detail)
        TextView tvMyAddressDetail;
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
