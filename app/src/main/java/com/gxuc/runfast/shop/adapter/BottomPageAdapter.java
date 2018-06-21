package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.BreakfastActivity;
import com.gxuc.runfast.shop.activity.WebActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.maintop.TopImage1;
import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huiliu on 2017/9/4.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class BottomPageAdapter extends RecyclerView.Adapter<BottomPageAdapter.BottomViewHolder> {

    private Context mContext;
    private List<TopImage1> data;

    public BottomPageAdapter(Context context, List<TopImage1> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public BottomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bottom_scroll_page, parent, false);
        return new BottomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BottomViewHolder holder, int position) {
        final TopImage1 topImage1 = data.get(position);
        holder.tv_bottom_scroll_name.setText(topImage1.getAgentName());
        holder.tv_bottom_scroll_tag.setText(topImage1.getTitle());
        holder.iv_bottom_scroll.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.iv_bottom_scroll.setImageURI(ApiServiceFactory.BASE_IMG_URL + topImage1.getAdImages());
        holder.ll_bottom_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (topImage1.getAdType()) {
                    case 1://内容
                        Intent webIntent = new Intent(mContext, WebActivity.class);
                        if (topImage1.getLinkAddr().contains("http")) {
                            webIntent.putExtra("url", topImage1.getLinkAddr());
                        } else {
                            webIntent.putExtra("url", ApiServiceFactory.WEB_HOST + topImage1.getLinkAddr());
                        }
                        mContext.startActivity(webIntent);
                        break;
                    case 2:
                        ToastUtil.showToast("2链接");
                        break;
                    case 3:
                        Intent data = new Intent(mContext, BreakfastActivity.class);
                        data.putExtra("typeName", topImage1.getTypename());
                        mContext.startActivity(data);
                        break;
                    case 4:
                        Intent intent = new Intent(mContext, BusinessActivity.class);
                        intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_BOTTOM_AD);
                        intent.putExtra("business", topImage1);
                        mContext.startActivity(intent);
                        break;
                    case 5:
                        String[] split = topImage1.getLinkAddr().split("=");
                        int goodId = Integer.parseInt(split[1]);
                        requestBusinessId(goodId);
                        break;
                    case 6:
                        Intent webData = new Intent(mContext, WebActivity.class);
                        if (topImage1.getLinkAddr().contains("http")) {
                            webData.putExtra("url", topImage1.getLinkAddr());
                        } else {
                            webData.putExtra("url", ApiServiceFactory.WEB_HOST + topImage1.getLinkAddr());
                        }
                        mContext.startActivity(webData);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BottomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bottom_scroll_name;
        TextView tv_bottom_scroll_tag;
        SimpleDraweeView iv_bottom_scroll;
        LinearLayout ll_bottom_ad;

        public BottomViewHolder(View itemView) {
            super(itemView);
            tv_bottom_scroll_name = (TextView) itemView.findViewById(R.id.tv_bottom_scroll_name);
            tv_bottom_scroll_tag = (TextView) itemView.findViewById(R.id.tv_bottom_scroll_tag);
            iv_bottom_scroll = (SimpleDraweeView) itemView.findViewById(R.id.iv_bottom_scroll);
            ll_bottom_ad = (LinearLayout) itemView.findViewById(R.id.ll_bottom_ad);
        }
    }

    private void requestBusinessId(int goodId) {
        CustomApplication.getRetrofit().getBusinessId(goodId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }

                String body = response.body();
                if (!TextUtils.isEmpty(body)) {
                    dealGood(body);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealGood(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject goods = jsonObject.optJSONObject("goods");
            int businessId = goods.optInt("businessId");
            Intent intent = new Intent(mContext, BusinessActivity.class);
            intent.putExtra("businessId", businessId);
            intent.putExtra("goodId", goods.optInt("id"));
            intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_GOOD_AD);
            mContext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
