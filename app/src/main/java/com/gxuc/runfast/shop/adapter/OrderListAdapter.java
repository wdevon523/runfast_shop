package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.activity.ordercenter.OrderComplainActivity;
import com.gxuc.runfast.shop.activity.ordercenter.OrderEvaluationActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.order.OrderInfo;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.gxuc.runfast.shop.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gxuc.runfast.shop.impl.MyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> implements View.OnClickListener {

    private List<OrderInfo> data;

    private Context context;

    private OnClickListener listener;

    public OrderListAdapter(List<OrderInfo> data, Context context, OnClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_info, parent, false);
        view.setOnClickListener(this);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, int position) {
//        //将position保存在itemView的Tag中，以便点击时进行获取
        final OrderInfo orderInfo = data.get(position);
        holder.itemView.setTag(position);
        holder.tv_order_shop_name.setText(orderInfo.getBusinessName());
        switch (orderInfo.getStatus()) {
            case -3:
                holder.tv_order_shop_state.setText("商家拒单");
                break;
            case -1:
                holder.tv_order_shop_state.setText("订单取消");
                break;
            case 0:
                holder.tv_order_shop_state.setText("客户下单");
                break;
            case 1:
                holder.tv_order_shop_state.setText("客户已付款");
                break;
            case 2:
                holder.tv_order_shop_state.setText("商家接单");
                break;
            case 3:
                holder.tv_order_shop_state.setText("骑手接单");
                break;
            case 4:
                holder.tv_order_shop_state.setText("商品打包");
                break;
            case 5:
                holder.tv_order_shop_state.setText("商品配送");
                break;
            case 6:
                holder.tv_order_shop_state.setText("商品送达");
                break;
            case 7:
                holder.tv_order_shop_state.setText("确认收货");
                break;
            case 8:
                holder.tv_order_shop_state.setText("订单完成");
                break;

        }

        holder.tv_order_shop_time.setText(orderInfo.getAccptTime());
        if (orderInfo.getGoodsTotal() > 1) {
            holder.tv_order_shop_content.setText(orderInfo.getGoodsSellName() + "等" + orderInfo.getGoodsTotal() + "件商品");
        } else {
            holder.tv_order_shop_content.setText(orderInfo.getGoodsSellName());
        }
        holder.tv_order_shop_price.setText("¥ " + String.valueOf(orderInfo.getPrice()));
        holder.iv_order_shop.setImageURI(ApiServiceFactory.BASE_IMG_URL + orderInfo.getLogo());
        holder.tv_order_shop_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, BusinessActivity.class);
//                intent.setFlags(IntentFlag.ORDER_LIST);
//                intent.putExtra("orderInfo", orderInfo.getBusinessId());
//                intent.putExtra("orderInfos", orderInfo);
//                context.startActivity(intent);
                requestBuyAgain(orderInfo.getId(), orderInfo.getBusinessId());
            }
        });

        holder.tv_order_shop_complain.setVisibility(orderInfo.getStatus() == 8 ? View.VISIBLE : View.GONE);
        holder.tv_order_shop_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderComplainActivity.class);
//                intent.putExtra("orderInfo", orderInfo);
//                context.startActivity(intent);

                Intent evaluationIntent = new Intent(context, OrderEvaluationActivity.class);
                evaluationIntent.putExtra("oid", orderInfo.getId());
                evaluationIntent.putExtra("logo", orderInfo.getLogo());
                evaluationIntent.putExtra("businessName", orderInfo.getBusinessName());
                evaluationIntent.putExtra("isDeliver", orderInfo.getIsDeliver());
                context.startActivity(evaluationIntent);
            }
        });

    }

    private void requestBuyAgain(int orderId, final int businessId) {
        CustomApplication.getRetrofit().buyAgain(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(context, BusinessActivity.class);
                        intent.setFlags(IntentFlag.ORDER_LIST);
                        intent.putExtra("orderInfo", businessId);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //注意这里使用getTag方法获取position
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_shop_name;
        TextView tv_order_shop_state;
        TextView tv_order_shop_time;
        TextView tv_order_shop_content;
        TextView tv_order_shop_complain;
        TextView tv_order_shop_again;
        TextView tv_order_shop_price;
        SimpleDraweeView iv_order_shop;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            tv_order_shop_name = (TextView) itemView.findViewById(R.id.tv_order_shop_name);
            tv_order_shop_state = (TextView) itemView.findViewById(R.id.tv_order_shop_state);
            tv_order_shop_time = (TextView) itemView.findViewById(R.id.tv_order_shop_time);
            tv_order_shop_content = (TextView) itemView.findViewById(R.id.tv_order_shop_content);
            tv_order_shop_price = (TextView) itemView.findViewById(R.id.tv_order_shop_price);
            tv_order_shop_complain = (TextView) itemView.findViewById(R.id.tv_order_shop_complain);
            tv_order_shop_again = (TextView) itemView.findViewById(R.id.tv_order_shop_again);
            iv_order_shop = (SimpleDraweeView) itemView.findViewById(R.id.iv_order_shop);
        }
    }

    //define interface
    public interface OnClickListener {
        void onItemClick(View view, int position);
    }
}
