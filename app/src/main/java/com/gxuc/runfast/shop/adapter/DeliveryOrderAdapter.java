package com.gxuc.runfast.shop.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ordercenter.PayChannelActivity;
import com.gxuc.runfast.shop.activity.purchases.DeliveryOrderDetailActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.DeliveryOrderInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class DeliveryOrderAdapter extends RecyclerView.Adapter {
    private Activity context = null;
    private List<DeliveryOrderInfo> deliveryOrderInfoList;

    private onRefreshListener mOnRefreshListener;

    public DeliveryOrderAdapter(Activity context, List<DeliveryOrderInfo> deliveryOrderInfoList) {
        this.context = context;
        this.deliveryOrderInfoList = deliveryOrderInfoList;
    }

    public void setdeliveryOrderList(List<DeliveryOrderInfo> deliveryOrderInfoList) {
        this.deliveryOrderInfoList = deliveryOrderInfoList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delivery_order, parent, false);
        return new DeliveryOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DeliveryOrderViewHolder deliveryOrderViewHolder = (DeliveryOrderViewHolder) holder;
        DeliveryOrderInfo deliveryOrderInfo = deliveryOrderInfoList.get(position);
        deliveryOrderViewHolder.tvDeliveryOrderStatus.setText(CustomUtils.getStatusStr(deliveryOrderInfo.status));
        deliveryOrderViewHolder.tvCargoName.setText(TextUtils.equals("DAIGOU", deliveryOrderInfo.type) ? deliveryOrderInfo.goodsDescription : deliveryOrderInfo.goodsType);
        deliveryOrderViewHolder.tvBuyAddress.setText(TextUtils.equals("NEARBY", deliveryOrderInfo.fromType) ? "就近购买" : deliveryOrderInfo.fromAddress);
        deliveryOrderViewHolder.tvSendAddress.setText(deliveryOrderInfo.toAddress);
        deliveryOrderViewHolder.tvSendName.setText(deliveryOrderInfo.toName);
        deliveryOrderViewHolder.tvSendMobile.setText(deliveryOrderInfo.toMobile);
        deliveryOrderViewHolder.tvDeliveryOrderTime.setText(deliveryOrderInfo.createTime.substring(0, 16));
        ShowHideButton(deliveryOrderViewHolder, deliveryOrderInfo);

    }

    private void ShowHideButton(DeliveryOrderViewHolder deliveryOrderViewHolder, DeliveryOrderInfo deliveryOrderInfo) {
        if (TextUtils.equals("CREATED", deliveryOrderInfo.status)) {
            //待支付
            deliveryOrderViewHolder.tvPayNow.setVisibility(View.VISIBLE);

            deliveryOrderViewHolder.tvBuyAgain.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvCallDriver.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvCancel.setVisibility(View.GONE);
        } else if (TextUtils.equals("PAID", deliveryOrderInfo.status)) {
            //已支付，待骑手接单
            deliveryOrderViewHolder.tvCancel.setVisibility(View.VISIBLE);

            deliveryOrderViewHolder.tvPayNow.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvBuyAgain.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvCallDriver.setVisibility(View.GONE);

        } else if (TextUtils.equals("TAKEN", deliveryOrderInfo.status)) {
            //骑手接单
            deliveryOrderViewHolder.tvCancel.setVisibility(View.VISIBLE);
            deliveryOrderViewHolder.tvCallDriver.setVisibility(View.VISIBLE);

            deliveryOrderViewHolder.tvPayNow.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvBuyAgain.setVisibility(View.GONE);
        } else if (TextUtils.equals("COMPLETED", deliveryOrderInfo.status)) {
            //已完成
            deliveryOrderViewHolder.tvCallDriver.setVisibility(View.VISIBLE);
            deliveryOrderViewHolder.tvBuyAgain.setVisibility(View.VISIBLE);

            deliveryOrderViewHolder.tvCancel.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvPayNow.setVisibility(View.GONE);
        } else if (TextUtils.equals("CANCELED", deliveryOrderInfo.status)) {
            //已取消
            deliveryOrderViewHolder.tvBuyAgain.setVisibility(View.VISIBLE);

            deliveryOrderViewHolder.tvCallDriver.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvCancel.setVisibility(View.GONE);
            deliveryOrderViewHolder.tvPayNow.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return deliveryOrderInfoList == null ? 0 : deliveryOrderInfoList.size();
    }

    public class DeliveryOrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_go_to_detail)
        LinearLayout llGoToDetail;
        @BindView(R.id.tv_cargo_name)
        TextView tvCargoName;
        @BindView(R.id.tv_delivery_order_status)
        TextView tvDeliveryOrderStatus;
        @BindView(R.id.tv_buy_address)
        TextView tvBuyAddress;
        @BindView(R.id.tv_send_address)
        TextView tvSendAddress;
        @BindView(R.id.tv_send_name)
        TextView tvSendName;
        @BindView(R.id.tv_send_mobile)
        TextView tvSendMobile;
        @BindView(R.id.tv_delivery_order_time)
        TextView tvDeliveryOrderTime;
        @BindView(R.id.tv_pay_now)
        TextView tvPayNow;
        @BindView(R.id.tv_buy_again)
        TextView tvBuyAgain;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_call_driver)
        TextView tvCallDriver;
        @BindView(R.id.tv_delete_order)
        TextView tvDeleteOrder;

        DeliveryOrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ll_go_to_detail, R.id.tv_pay_now, R.id.tv_buy_again, R.id.tv_call_driver, R.id.tv_cancel, R.id.tv_delete_order})
        public void onClick(View view) {
            DeliveryOrderInfo deliveryOrderInfo = deliveryOrderInfoList.get(getAdapterPosition());

            switch (view.getId()) {
                //跳转订单详情
                case R.id.ll_go_to_detail:
                    Intent intent = new Intent(context, DeliveryOrderDetailActivity.class);
                    intent.putExtra("orderId", deliveryOrderInfo.id);
                    context.startActivity(intent);
                    break;
                case R.id.tv_pay_now:
                    Intent data = new Intent(context, PayChannelActivity.class);
                    data.putExtra("orderId", deliveryOrderInfo.id);
                    data.putExtra("price", deliveryOrderInfo.amountPayable / 100);
                    data.putExtra("orderCode", deliveryOrderInfo.orderNo);
                    data.putExtra("isPaotui", true);
                    context.startActivity(data);
                    break;
                case R.id.tv_buy_again:
                    break;
                case R.id.tv_call_driver:
                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + deliveryOrderInfo.driverMobile));
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
                    context.startActivity(intentCall);
                    break;
                case R.id.tv_cancel:
                    requestCancelDeliveryOrder(deliveryOrderInfo.id);
                    break;
                case R.id.tv_delete_order:
                    requestDeleteDeliveryOrder(deliveryOrderInfo.id, getAdapterPosition());
                    break;
            }
        }
    }

    private void requestCancelDeliveryOrder(int orderId) {

        CustomApplication.getRetrofitPaoTui().cancelDeliveryOrder(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
//                        ToastUtil.showToast(jsonObject.optString("msg"));
                        ToastUtil.showToast("取消成功");
                        if (null != mOnRefreshListener) {
                            mOnRefreshListener.onRef();
                        }
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
    }

    private void requestDeleteDeliveryOrder(int orderId, final int position) {
        CustomApplication.getRetrofitPaoTui().deleteDeliveryOrder(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
//                        ToastUtil.showToast(jsonObject.optString("msg"));
                        ToastUtil.showToast("删除成功");
                        deliveryOrderInfoList.remove(position);
                        notifyDataSetChanged();
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
    }

    public interface onRefreshListener {
        void onRef();

    }

    public void setOnRefreshListener(onRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }
}
