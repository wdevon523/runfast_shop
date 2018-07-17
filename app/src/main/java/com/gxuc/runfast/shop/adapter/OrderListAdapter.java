package com.gxuc.runfast.shop.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.activity.ordercenter.OrderEvaluationActivity;
import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.OrderInformation;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 天上白玉京 on 2017/7/28.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> implements View.OnClickListener {

    private List<OrderInformation> data;

    private Context context;

    private OnClickListener listener;

    public OrderListAdapter(List<OrderInformation> data, Context context, OnClickListener listener) {
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
    public void onBindViewHolder(OrderListViewHolder holder, final int position) {
//        //将position保存在itemView的Tag中，以便点击时进行获取
        final OrderInformation orderInfo = data.get(position);
        holder.itemView.setTag(position);
        holder.tv_order_shop_name.setText(orderInfo.businessName);
//        holder.tv_order_shop_state.setText(orderInfo.statStr);
        switch (orderInfo.status) {
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

        holder.ll_contain_goods.removeAllViews();
        for (int i = 0; i < orderInfo.cartItems.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_list_goods, null);
            TextView tvOrderGoodsName = (TextView) view.findViewById(R.id.tv_order_goods_name);
            TextView tvOrderGoodsNum = (TextView) view.findViewById(R.id.tv_order_goods_num);
            tvOrderGoodsName.setText(orderInfo.cartItems.get(i).goodsName);
            tvOrderGoodsNum.setText("x" + orderInfo.cartItems.get(i).num);
            holder.ll_contain_goods.addView(view);
        }

        holder.tv_order_shop_time.setText(orderInfo.createTime);

        String orderPriceStr = "共" + orderInfo.totalNum + "件商品，实付" + "¥ " + orderInfo.totalPay.stripTrailingZeros().toPlainString();
        SpannableString spanString = new SpannableString(orderPriceStr);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spanString.setSpan(span, orderPriceStr.indexOf("¥"), orderPriceStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_order_shop_price.setText(spanString);

        x.image().bind(holder.iv_order_shop, UrlConstant.ImageBaseUrl + orderInfo.businessImg, NetConfig.optionsLogoImage);

        holder.tv_order_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payChannelIntent = new Intent(context, PayChannelActivity.class);
                payChannelIntent.putExtra("orderId", orderInfo.orderId);
                payChannelIntent.putExtra("orderCode", orderInfo.orderNo);
                payChannelIntent.putExtra("price", orderInfo.totalPay);
                payChannelIntent.putExtra("businessName", orderInfo.businessName);
                payChannelIntent.putExtra("logo", orderInfo.businessImg);
                payChannelIntent.putExtra("createTime", orderInfo.createTime);
                context.startActivity(payChannelIntent);
            }
        });

        holder.tv_order_shop_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBuyAgain(orderInfo.orderId, orderInfo.businessId);
            }
        });

//        holder.tv_order_shop_complain.setVisibility((orderInfo.getStatus() == 8 && orderInfo.getIsComent() == null) ? View.VISIBLE : View.GONE);


//        holder.tv_order_confirm_completed.setVisibility(orderInfo.status >= 5 && orderInfo.status <= 7 ? View.VISIBLE : View.GONE);
//        holder.tv_order_call_driver.setVisibility(orderInfo.status >= 3 ? View.VISIBLE : View.GONE);
//        holder.tv_order_call_business.setVisibility(orderInfo.status >= 2 ? View.VISIBLE : View.GONE);
        holder.tv_order_pay_now.setVisibility(orderInfo.status == 0 ? View.VISIBLE : View.GONE);
//        holder.tv_order_shop_again.setVisibility(orderInfo.status <= 0 || orderInfo.status == 8 ? View.VISIBLE : View.GONE);
        holder.tv_order_shop_again.setVisibility(View.VISIBLE);

        holder.tv_order_shop_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent evaluationIntent = new Intent(context, OrderEvaluationActivity.class);
                evaluationIntent.putExtra("oid", orderInfo.orderId);
                evaluationIntent.putExtra("logo", orderInfo.businessImg);
                evaluationIntent.putExtra("businessName", orderInfo.businessName);
                evaluationIntent.putExtra("isDeliver", orderInfo.isDeliver);
                context.startActivity(evaluationIntent);
            }
        });

        holder.tv_order_call_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderInfo.businessMobile));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showToast("请先开启电话权限");
                    return;
                }
                context.startActivity(intent);
            }
        });

        holder.tv_order_call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderInfo.shopperMobile));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showToast("请先开启电话权限");
                    return;
                }
                context.startActivity(intent);
            }
        });

        holder.tv_order_confirm_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestConfirmCompleted(orderInfo.orderId, position);
            }
        });


    }

    private void requestConfirmCompleted(int orderId, final int position) {
        CustomApplication.getRetrofit().receiveOrder(orderId).enqueue(new MyCallback<String>() {

            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    ToastUtil.showToast(jsonObject.optString("msg"));
                    if (jsonObject.optBoolean("success")) {
                        notifyItemChanged(position);
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

    private void requestBuyAgain(int orderId, final int businessId) {
        CustomApplication.getRetrofitNew().buyAgainNew(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(context, BusinessNewActivity.class);
                        intent.putExtra("businessId", businessId);
                        context.startActivity(intent);
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });

//        CustomApplication.getRetrofit().buyAgain(orderId).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                String body = response.body();
//                try {
//                    JSONObject jsonObject = new JSONObject(body);
//                    if (jsonObject.optBoolean("success")) {
//                        Intent intent = new Intent(context, BusinessActivity.class);
//                        intent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_LIST);
//                        intent.putExtra("orderInfo", businessId);
//                        context.startActivity(intent);
//                    } else {
//                        ToastUtil.showToast(jsonObject.optString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
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

    public void setOrderList(List<OrderInformation> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_shop_name;
        TextView tv_order_shop_state;
        TextView tv_order_shop_time;
        //        TextView tv_order_shop_content;
        TextView tv_order_shop_complain;
        TextView tv_order_call_driver;
        TextView tv_order_call_business;
        TextView tv_order_confirm_completed;
        TextView tv_order_shop_again;
        TextView tv_order_pay_now;
        TextView tv_order_shop_price;
        ImageView iv_order_shop;
        LinearLayout ll_contain_goods;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            tv_order_shop_name = (TextView) itemView.findViewById(R.id.tv_order_shop_name);
            tv_order_shop_state = (TextView) itemView.findViewById(R.id.tv_order_shop_state);
            tv_order_shop_time = (TextView) itemView.findViewById(R.id.tv_order_shop_time);
//            tv_order_shop_content = (TextView) itemView.findViewById(R.id.tv_order_shop_content);
            tv_order_shop_price = (TextView) itemView.findViewById(R.id.tv_order_shop_price);
            tv_order_shop_complain = (TextView) itemView.findViewById(R.id.tv_order_shop_complain);
            tv_order_call_driver = (TextView) itemView.findViewById(R.id.tv_order_call_driver);
            tv_order_call_business = (TextView) itemView.findViewById(R.id.tv_order_call_business);
            tv_order_confirm_completed = (TextView) itemView.findViewById(R.id.tv_order_confirm_completed);
            tv_order_shop_again = (TextView) itemView.findViewById(R.id.tv_order_shop_again);
            tv_order_pay_now = (TextView) itemView.findViewById(R.id.tv_order_pay_now);
            iv_order_shop = (ImageView) itemView.findViewById(R.id.iv_order_shop);
            ll_contain_goods = (LinearLayout) itemView.findViewById(R.id.ll_contain_goods);
        }
    }

    //define interface
    public interface OnClickListener {
        void onItemClick(View view, int position);
    }
}
