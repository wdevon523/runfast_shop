package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.DeviceUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BreakfastActivity;
import com.gxuc.runfast.shop.activity.LoginQucikActivity;
import com.gxuc.runfast.shop.activity.WebActivity;
import com.gxuc.runfast.shop.activity.purchases.PurchasesActivity;
import com.gxuc.runfast.shop.bean.home.HomeCategory;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import java.util.List;

/**
 * Author: Mr.xiao on 2017/5/23
 *
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 首页分页菜单项列表适配器
 */
public class EntranceAdapter extends RecyclerView.Adapter<EntranceAdapter.EntranceViewHolder> {

    private List<HomeCategory> mDatas;

    /**
     * 页数下标,从0开始(通俗讲第几页)
     */
    private int mIndex;

    /**
     * 每页显示最大条目个数
     */
    private int mPageSize;

    private Context mContext;

    private final LayoutInflater mLayoutInflater;

    private List<HomeCategory> homeEntrances;

    public EntranceAdapter(Context context, List<HomeCategory> datas, int index, int pageSize) {
        this.mContext = context;
        this.homeEntrances = datas;
        mPageSize = pageSize;
        mDatas = datas;
        mIndex = index;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public EntranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntranceViewHolder(mLayoutInflater.inflate(R.layout.item_home_entrance, null));
    }

    @Override
    public void onBindViewHolder(EntranceViewHolder holder, final int position) {
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        final int pos = position + mIndex * mPageSize;
        holder.entranceNameTextView.setText(homeEntrances.get(pos).name);
//        holder.entranceIconImageView.setImageResource(homeEntrances.get(pos).getImage());

        x.image().bind(holder.entranceIconImageView, UrlConstant.ImageBaseUrl + homeEntrances.get(pos).icon, NetConfig.optionsLogoImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeCategory homeCategory = homeEntrances.get(pos);

                if (homeCategory.typelink == 1) {//商家分类列表
                    Intent intent = new Intent(mContext, BreakfastActivity.class);
                    intent.putExtra("commonId", homeCategory.commonId);
                    intent.putExtra("typeName", homeCategory.typename);
                    mContext.startActivity(intent);
                } else if (homeCategory.typelink == 4) {//自定义
                    Intent webData = new Intent(mContext, WebActivity.class);
                    if (homeCategory.link.contains("http")) {
                        webData.putExtra("url", homeCategory.link);
                    } else {
                        webData.putExtra("url", ApiServiceFactory.WEB_HOST + homeCategory.link);
                    }
                    mContext.startActivity(webData);
                } else if (homeCategory.typelink == 5) {//跑腿
//                    if (UserService.getUserInfo(mContext) != null) {
                    mContext.startActivity(new Intent(mContext, PurchasesActivity.class));
//                    }else {
//                        mContext.startActivity(new Intent(mContext, LoginQucikActivity.class));
//                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    class EntranceViewHolder extends RecyclerView.ViewHolder {

        private TextView entranceNameTextView;
        private ImageView entranceIconImageView;

        public EntranceViewHolder(View itemView) {
            super(itemView);
            entranceIconImageView = (ImageView) itemView.findViewById(R.id.entrance_image);
            entranceNameTextView = (TextView) itemView.findViewById(R.id.entrance_name);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) DeviceUtil.getScreenWidth(mContext) / 4.0f));
            itemView.setLayoutParams(layoutParams);
        }
    }
}
