package com.gxuc.runfast.shop.activity.ordercenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.supportv1.utils.JsonUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.activity.MainActivity;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.OrderGoodsNewAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.DriverInfo;
import com.gxuc.runfast.shop.bean.OrderInformation;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.ViewUtils;
import com.gxuc.runfast.shop.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

public class OrderDetailActivity extends ToolBarActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate
//        , AMap.OnMyLocationChangeListener
{


    @BindView(R.id.map)
    MapView mMapView;
    //    @BindView(R.id.scroll_view)
//    ScrollView scrollView;
    @BindView(R.id.rl_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.iv_back_map)
    ImageView ivBackMap;
    @BindView(R.id.iv_close_map)
    ImageView ivCloseMap;
    @BindView(R.id.tv_order_man_state)
    TextView mTvOrderManState;
    @BindView(R.id.ll_man_deliver_time)
    LinearLayout mLlManDeliverTime;
    @BindView(R.id.tv_man_deliver_time)
    TextView mTvManDeliverTime;
    @BindView(R.id.btn_buy_again)
    Button mBtnBuyAgain;
    @BindView(R.id.btn_pay_now)
    Button mBtnPayNow;
    @BindView(R.id.btn_confirm_completed)
    Button mBtnConfirmCompleted;
    @BindView(R.id.btn_cancel_order)
    Button mBtnCancelOrder;
    @BindView(R.id.btn_appraise)
    Button mBtnAppraise;
    @BindView(R.id.iv_order_detail_business_img)
    ImageView mIvOrderDetailBusinessImg;
    @BindView(R.id.tv_order_detail_business_name)
    TextView mTvOrderDetailBusinessName;
    @BindView(R.id.ll_contain_product)
    LinearLayout llContainProduct;
    @BindView(R.id.tv_order_detail_price)
    TextView mTvOrderDetailPrice;
    @BindView(R.id.tv_order_detail_coupon_price)
    TextView mTvOrderDetailCouponPrice;
    @BindView(R.id.tv_order_detail_sub_price)
    TextView mTvOrderDetailSubPrice;
    @BindView(R.id.tv_order_detail_deliver_type)
    TextView mTvOrderDetailDeliverType;
    @BindView(R.id.ll_order_detail_deliver_time)
    LinearLayout mLlOrderDetailDeliverTime;
    @BindView(R.id.tv_order_detail_deliver_time)
    TextView mTvOrderDetailDeliverTime;
    @BindView(R.id.tv_order_detail_man_info)
    TextView mTvOrderDetailManInfo;
    @BindView(R.id.tv_order_detail_man_phone)
    TextView mTvOrderDetailManPhone;
    @BindView(R.id.tv_order_detail_number)
    TextView mTvOrderDetailNumber;
    @BindView(R.id.ll_order_detail_pay_type)
    LinearLayout mLlOrderDetailPatType;
    @BindView(R.id.tv_order_detail_pat_type)
    TextView mTvOrderDetailPatType;
    @BindView(R.id.tv_order_detail_remark)
    TextView mTvOrderDetailRemark;
    @BindView(R.id.tv_order_detail_old_shipping)
    TextView tvOrderDetailOldShipping;
    @BindView(R.id.tv_order_detail_showps)
    TextView tvOrderDetailShowps;
    @BindView(R.id.tv_order_detail_packaging)
    TextView tvOrderDetailPackaging;
    @BindView(R.id.tv_order_detail_coupons)
    TextView tvOrderDetailCoupons;
    @BindView(R.id.tv_order_detail_creat_time)
    TextView tvOrderDetailCreatTime;
    @BindView(R.id.iv_driver_avater)
    CircleImageView ivDriverAvater;
    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;
    @BindView(R.id.tv_driver_type)
    TextView tvDriverType;
    @BindView(R.id.btn_contact_driver)
    ImageView btnContactDriver;
    @BindView(R.id.rl_contact_business)
    RelativeLayout rlContactBusiness;
    @BindView(R.id.rl_driver_info)
    RelativeLayout rlDriverInfo;
    @BindView(R.id.rl_business_name)
    RelativeLayout llBusinessName;

    private AMap aMap;
    private List<String> mStrings;
    private OrderInformation orderDetailInfo;
    private UserInfo userInfo;
    private Integer orderId;
    private boolean isFromePayFinish;
    private AlertDialog alertDialog;
    private CameraUpdate cameraUpdateBusiness;
    private CameraUpdate cameraUpdateDriver;
    private CameraUpdate cameraUpdateCenter;
    private Marker marker;
    private boolean isFirstLocation = true;
    private CameraUpdate cameraUpdateUser;
    private DriverInfo driverInfo;
    private MarkerOptions markerOptionsDriver;
    private LatLng businessLatLng;
    private LatLng driverLatLng;
    private LatLng userLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
//            UiSettings uiSettings = aMap.getUiSettings();
//            // 通过UISettings.setZoomControlsEnabled(boolean)来设置缩放按钮是否能显示
//            uiSettings.setZoomControlsEnabled(false);
            //可视化区域，将指定位置指定到屏幕中心位置

//            aMap.setOnMapClickListener(this);
//            drawMarkers();//绘制小蓝气泡
//            initMap();
        }
        initRefreshLayout();
        initData();
        setListener();
//        getNetData();
    }

//    private void initMap() {
//        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_mylocation)));//
//        myLocationStyle.anchor(0.5f, 0.5f);
//        myLocationStyle.strokeColor(Color.argb(180, 63, 157, 226));
//        myLocationStyle.radiusFillColor(Color.argb(100, 148, 200, 239));
//
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
////        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        aMap.getUiSettings().setZoomControlsEnabled(false);
//
//    }

//    /**
//     * 定位发生变化
//     *
//     * @param location
//     */
//    @Override
//    public void onMyLocationChange(Location location) {
//        if (location != null) {
//            if (isFirstLocation) {
//                isFirstLocation = false;
//                Log.d("location", "lat = " + location.getLatitude() + "，lng = " + location.getLongitude());
//                cameraUpdateUser = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
////                aMap.animateCamera(cameraUpdateUser);
//            }
//        }
//    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        BGAMeiTuanRefreshViewHolder meiTuanRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(this, false);
        meiTuanRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        meiTuanRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        meiTuanRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
        mRefreshLayout.setRefreshViewHolder(meiTuanRefreshViewHolder);
    }


    private void drawDriverMarkers() {
        if (marker != null) {
            marker.remove();
        }
        driverLatLng = new LatLng(driverInfo.latitude, driverInfo.longitude);
        View view = getLayoutInflater().inflate(R.layout.icon_business, null);
        ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        ivLogo.setImageResource(R.drawable.icon_driver_logo);

        LinearLayout llMapOrderStatus = (LinearLayout) view.findViewById(R.id.ll_map_order_status);
        TextView tvMapOrdertatus = (TextView) view.findViewById(R.id.tv_map_order_status);
        llMapOrderStatus.setVisibility(View.VISIBLE);
        if (orderDetailInfo.status == 3 || orderDetailInfo.status == 4) {
            float distance = AMapUtils.calculateLineDistance(driverLatLng, businessLatLng);
            tvMapOrdertatus.setText("距离商家" + (int) distance + "m");
        } else if (orderDetailInfo.status == 5) {
            float distance = AMapUtils.calculateLineDistance(driverLatLng, userLatLng);
            tvMapOrdertatus.setText("距离您" + (int) distance + "m");
        } else {
            llMapOrderStatus.setVisibility(View.GONE);
        }


        Bitmap bitmap = ViewUtils.convertViewToBitmap(view);

        cameraUpdateDriver = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(driverLatLng, 15, 0, 0));

        cameraUpdateCenter = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(driverInfo.latitude - 0.005,
                        driverInfo.longitude), 15, 0, 0));
        markerOptionsDriver = new MarkerOptions();
        // 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptionsDriver.position(driverLatLng);
        // 设置Marker点击之后显示的标题
//        markerOptionsDriver.snippet("距离商家500米");
        // 设置Marker的图标样式
//        markerOptionsDriver.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bga_refresh_mt_change_to_release_refresh_01));
        markerOptionsDriver.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        // 设置Marker是否可以被拖拽，
        markerOptionsDriver.draggable(false);
        // 设置Marker的可见性
        markerOptionsDriver.visible(true);
        //将Marker添加到地图上去
        marker = aMap.addMarker(markerOptionsDriver);
        marker.showInfoWindow();

        aMap.moveCamera(cameraUpdateCenter);
    }

    private void drawBusinessMarkers() {
        businessLatLng = new LatLng(orderDetailInfo.businessAddressLat,
                orderDetailInfo.businessAddressLng);

        View view = getLayoutInflater().inflate(R.layout.icon_business, null);
        LinearLayout llMapOrderStatus = (LinearLayout) view.findViewById(R.id.ll_map_order_status);
        TextView tvMapOrdertatus = (TextView) view.findViewById(R.id.tv_map_order_status);
        CircleImageView ivLogo = (CircleImageView) view.findViewById(R.id.iv_logo);
        x.image().bind(ivLogo, UrlConstant.ImageBaseUrl + orderDetailInfo.businessImg, NetConfig.optionsHeadImage);
        llMapOrderStatus.setVisibility(View.VISIBLE);
        if (orderDetailInfo.status == 1) {
            tvMapOrdertatus.setText("等待商家接单");
        } else if (orderDetailInfo.status == 2 || orderDetailInfo.status == 3) {
            tvMapOrdertatus.setText("商家正在备货");
        } else if (orderDetailInfo.status == 4) {
            tvMapOrdertatus.setText("商家已打包");
        } else {
            llMapOrderStatus.setVisibility(View.GONE);
        }
//        ImageLoaderUtil.displayImage(this, ivLogo, UrlConstant.ImageBaseUrl + orderDetailInfo.goodsSellRecord.logo);
        Bitmap bitmap = ViewUtils.convertViewToBitmap(view);

        cameraUpdateBusiness = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(businessLatLng, 15, 0, 0));

        cameraUpdateCenter = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(orderDetailInfo.businessAddressLat - 0.005,
                        orderDetailInfo.businessAddressLng), 15, 0, 0));

        MarkerOptions markerOptionsBusiness = new MarkerOptions();
        // 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptionsBusiness.position(businessLatLng);
        // 设置Marker点击之后显示的标题
        // 设置Marker的图标样式
//        markerOptionsBusiness.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_shop_car));
        markerOptionsBusiness.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        // 设置Marker是否可以被拖拽，
        markerOptionsBusiness.draggable(false);
        // 设置Marker的可见性
        markerOptionsBusiness.visible(true);
        //将Marker添加到地图上去
        aMap.addMarker(markerOptionsBusiness).showInfoWindow();

        if (orderDetailInfo.status <= 2 && orderDetailInfo.status > 0) {
            aMap.moveCamera(cameraUpdateCenter);
        }
    }


    private void drawUserMarkers() {
        userLatLng = new LatLng(orderDetailInfo.userAddressLat, orderDetailInfo.userAddressLng);

        cameraUpdateUser = CameraUpdateFactory.newLatLngZoom(userLatLng, 15);
        MarkerOptions markerOptionsUser = new MarkerOptions();
        // 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptionsUser.position(userLatLng);
        // 设置Marker点击之后显示的标题
        // 设置Marker的图标样式
//        markerOptionsBusiness.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_shop_car));
        markerOptionsUser.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_refresh_location));
        // 设置Marker是否可以被拖拽，
        markerOptionsUser.draggable(false);
        // 设置Marker的可见性
        markerOptionsUser.visible(true);
        //将Marker添加到地图上去
        aMap.addMarker(markerOptionsUser).showInfoWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    private void initData() {
        orderId = getIntent().getIntExtra("orderId", 0);
        isFromePayFinish = getIntent().getBooleanExtra("isFromePayFinish", false);
    }

    private void setListener() {
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isFromePayFinish) {
//                    Intent data = new Intent(OrderDetailActivity.this, MainActivity.class);
//                    data.putExtra("currentPage", 2);
//                    startActivity(data);
//                }
//                finish();
//            }
//        });

        ivBackMap.setOnClickListener(this);

        viewTop.setOnClickListener(this);

        ivCloseMap.setOnClickListener(this);

//        aMap.setOnMyLocationChangeListener(this);
    }

    public void showBigMap(Boolean isshowBigMap) {
        if (marker != null) {
//            marker.hideInfoWindow();
            marker.remove();
            Log.d("devon", "----------marker.hideInfoWindow()----------");
            marker = aMap.addMarker(markerOptionsDriver);
            marker.showInfoWindow();
        }

//        scrollView.setVisibility(isshowBigMap ? View.GONE : View.VISIBLE);
        mRefreshLayout.setVisibility(isshowBigMap ? View.GONE : View.VISIBLE);
        ivCloseMap.setVisibility(isshowBigMap ? View.VISIBLE : View.GONE);
        //设置默认定位按钮是否显示，非必需设置。
//        aMap.getUiSettings().setMyLocationButtonEnabled(isshowBigMap);
        if (driverInfo != null) {
            aMap.moveCamera(isshowBigMap ? cameraUpdateDriver : cameraUpdateCenter);
        } else {
            aMap.moveCamera(isshowBigMap ? cameraUpdateBusiness : cameraUpdateCenter);
        }
//        if (marker != null) {
//            marker.showInfoWindow();
//            Log.d("devon", "----------marker.showInfoWindow()----------");
//        }
    }
//    private void initView() {
//        mStrings = new ArrayList<>();
//        OrderDetailAdapter adapter = new OrderDetailAdapter(this, mStrings);
//        mRvOrderDetail.setAdapter(adapter);
//    }

    private void getNetData() {
        userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        getOrderDetail(orderId);
    }

    private void getOrderDetail(Integer id) {
        CustomApplication.getRetrofitNew().getOrderDetail(id).enqueue(new MyCallback<String>() {

            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        fillView(jsonObject.optString("data"));
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.setEnabled(true);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                if (mRefreshLayout != null) {
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.setEnabled(true);
                }
            }
        });
    }

    /**
     * 解析数据
     *
     * @param data
     */
    private void fillView(String data) {
        orderDetailInfo = JsonUtil.fromJson(data, OrderInformation.class);

        if (orderDetailInfo == null) {
            return;
        }
        drawUserMarkers();
        drawBusinessMarkers();
        if (!TextUtils.isEmpty(orderDetailInfo.shopperId) && !TextUtils.equals("null", orderDetailInfo.shopperId)) {
            requestDriverLatLng(orderDetailInfo.shopperId);
        }
        mTvOrderManState.setText(orderDetailInfo.statStr);

        if (orderDetailInfo.status == 2 ||
                orderDetailInfo.status == 3 ||
                orderDetailInfo.status == 4 ||
                orderDetailInfo.status == 5) {
            mLlManDeliverTime.setVisibility(View.VISIBLE);
            mTvManDeliverTime.setText(orderDetailInfo.deliveryDuration + "");
        } else {
            mLlManDeliverTime.setVisibility(View.INVISIBLE);
        }

        showBtnStatus(orderDetailInfo.status);

        if (orderDetailInfo.isCancel != null && orderDetailInfo.status != 8) {
            if (orderDetailInfo.isCancel == 1) {
                mBtnCancelOrder.setVisibility(View.GONE);
                mTvOrderManState.setText("已申请取消，等待商家处理");
            } else if (orderDetailInfo.isCancel == 2) {
                mBtnCancelOrder.setVisibility(View.GONE);
                mTvOrderManState.setText("商家同意取消订单");
            } else if (orderDetailInfo.isCancel == 3) {
                mBtnCancelOrder.setVisibility(View.VISIBLE);
                mTvOrderManState.setText("商家不同意取消订单");
            }
        }

        tvDriverName.setText(orderDetailInfo.shopper);
        tvDriverType.setText(orderDetailInfo.isDeliver == 0 ? "快车专送" : "商家配送");

//        ImageLoaderUtil.displayImage(this, mIvOrderDetailBusinessImg, orderDetailInfo.goodsSellRecord.logo);
        x.image().bind(mIvOrderDetailBusinessImg, UrlConstant.ImageBaseUrl + orderDetailInfo.businessImg, NetConfig.optionsHeadImage);
        mTvOrderDetailBusinessName.setText(orderDetailInfo.businessName);

        llContainProduct.removeAllViews();
        OrderGoodsNewAdapter orderGoodsNewAdapter = new OrderGoodsNewAdapter(this, orderDetailInfo.cartItems);
        for (int i = 0; i < orderDetailInfo.cartItems.size(); i++) {
            llContainProduct.addView(orderGoodsNewAdapter.getView(i, null, null));
        }

        tvOrderDetailOldShipping.setVisibility(orderDetailInfo.deliveryFee == orderDetailInfo.finalDeliveryFee ? View.GONE : View.VISIBLE);
        tvOrderDetailOldShipping.setText(orderDetailInfo.deliveryFee == 0 ? "" : "¥ " + orderDetailInfo.deliveryFee);
        tvOrderDetailOldShipping.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvOrderDetailShowps.setText("¥ " + orderDetailInfo.finalDeliveryFee);
        tvOrderDetailPackaging.setText("¥ " + orderDetailInfo.totalPackageFee);
        tvOrderDetailCoupons.setText("-¥ " + orderDetailInfo.offAmount);
        mTvOrderDetailPrice.setText("¥ " + (orderDetailInfo.cartDisprice));
        mTvOrderDetailCouponPrice.setText("¥ " + orderDetailInfo.offAmount);
        mTvOrderDetailSubPrice.setText("¥ " + orderDetailInfo.totalPay);
        mTvOrderDetailDeliverType.setText(orderDetailInfo.isDeliver == 0 ? "快车专送" : "商家配送");
        tvOrderDetailCreatTime.setText(orderDetailInfo.createTime);
//        if (!TextUtils.isEmpty(orderDetailInfo.readyTime) || !TextUtils.equals(orderDetailInfo.readyTime, "null")) {
//            mTvOrderDetailDeliverTime.setText(orderDetailInfo.disTime);
//            mLlOrderDetailDeliverTime.setVisibility(View.VISIBLE);
//        } else {
//            mLlOrderDetailDeliverTime.setVisibility(View.GONE);
//        }
        mTvOrderDetailManInfo.setText(orderDetailInfo.userAddress + orderDetailInfo.address);
        mTvOrderDetailManPhone.setText(orderDetailInfo.userName + "  " + orderDetailInfo.userPhone);
        mTvOrderDetailNumber.setText(orderDetailInfo.orderNo);
        if (orderDetailInfo.isPay != null && orderDetailInfo.isPay == 1) {
            mLlOrderDetailPatType.setVisibility(View.VISIBLE);
            if (orderDetailInfo.payType == 0) {
                mTvOrderDetailPatType.setText("支付宝");
            } else if (orderDetailInfo.payType == 1) {
                mTvOrderDetailPatType.setText("微信");
            } else if (orderDetailInfo.payType == 2) {
                mTvOrderDetailPatType.setText("钱包");
            }
        } else {
            mLlOrderDetailPatType.setVisibility(View.GONE);
        }
        mTvOrderDetailRemark.setText(orderDetailInfo.remark);
    }

    private void requestDriverLatLng(String shopperId) {

        CustomApplication.getRetrofit().getDriverLatLng(shopperId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        driverInfo = GsonUtil.fromJson(jsonObject.optString("driver"), DriverInfo.class);
                        if (driverInfo != null) {
                            drawDriverMarkers();
                        }
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

    private void showBtnStatus(int status) {

        if (status == -3) {
            //-3:商家拒单
            mBtnBuyAgain.setVisibility(View.VISIBLE);
            rlContactBusiness.setVisibility(View.VISIBLE);

            mBtnAppraise.setVisibility(View.GONE);
            mBtnCancelOrder.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);
            rlDriverInfo.setVisibility(View.GONE);

            mMapView.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        } else if (status == -1) {
            //-1：订单取消
            mBtnBuyAgain.setVisibility(View.VISIBLE);

            mBtnAppraise.setVisibility(View.GONE);
            mBtnCancelOrder.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);
            rlContactBusiness.setVisibility(View.GONE);
            rlDriverInfo.setVisibility(View.GONE);

            mMapView.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        } else if (status == 0) {
            //0：客户下单
            mBtnPayNow.setVisibility(View.VISIBLE);
            mBtnCancelOrder.setVisibility(View.VISIBLE);

            mBtnBuyAgain.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);
            rlContactBusiness.setVisibility(View.GONE);
            rlDriverInfo.setVisibility(View.GONE);
            mBtnAppraise.setVisibility(View.GONE);
            mMapView.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        } else if (status == 1 || status == 2) {
            //1：客户已付款  2：商家接单
            mBtnCancelOrder.setVisibility(View.VISIBLE);
            rlContactBusiness.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);

            mBtnPayNow.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);
            mBtnBuyAgain.setVisibility(View.GONE);
            rlDriverInfo.setVisibility(View.GONE);
            mBtnAppraise.setVisibility(View.GONE);
        } else if (status == 3) {
            //3：骑手接单
            mBtnCancelOrder.setVisibility(View.VISIBLE);
            rlContactBusiness.setVisibility(View.VISIBLE);
            rlDriverInfo.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);

            mBtnConfirmCompleted.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnBuyAgain.setVisibility(View.GONE);
            mBtnAppraise.setVisibility(View.GONE);
        }
//        else if (status == 4) {
//        }
        else if (status == 4 || status == 5) {
            //4：商品打包 ，5：商品配送
            mBtnConfirmCompleted.setVisibility(View.VISIBLE);
            rlContactBusiness.setVisibility(View.VISIBLE);
            rlDriverInfo.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);

            mBtnCancelOrder.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnBuyAgain.setVisibility(View.GONE);
            mBtnAppraise.setVisibility(View.GONE);
        }
//        else if (status == 6) {
//        } else if (status == 7) {
//        }
        else if (status == 8) {
            // 6：商品送达，7:确认收货 ，8：订单完成
            mBtnBuyAgain.setVisibility(View.VISIBLE);
            rlContactBusiness.setVisibility(View.VISIBLE);
            rlDriverInfo.setVisibility(View.VISIBLE);
            mBtnAppraise.setVisibility(orderDetailInfo.isComent == null
                    ? View.VISIBLE : View.GONE);

            mBtnCancelOrder.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);

            mMapView.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        } else {
            mBtnBuyAgain.setVisibility(View.VISIBLE);

            rlContactBusiness.setVisibility(View.GONE);
            rlDriverInfo.setVisibility(View.GONE);
            mBtnAppraise.setVisibility(View.GONE);
            mBtnCancelOrder.setVisibility(View.GONE);
            mBtnPayNow.setVisibility(View.GONE);
            mBtnConfirmCompleted.setVisibility(View.GONE);
            mMapView.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tv_order_man_state, R.id.btn_buy_again, R.id.btn_pay_now, R.id.btn_confirm_completed, R.id.btn_cancel_order, R.id.rl_contact_business, R.id.btn_contact_driver, R.id.btn_appraise, R.id.rl_business_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_business_name:
                Intent businessIntent = new Intent(OrderDetailActivity.this, BusinessNewActivity.class);
                businessIntent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_LIST);
                businessIntent.putExtra("businessId", orderDetailInfo.businessId);
                startActivity(businessIntent);
                break;

            //订单状态
            case R.id.tv_order_man_state:
                Intent orderIntent = new Intent(this, OrderStatusActivity.class);
                orderIntent.putExtra("orderId", orderId);
                startActivity(orderIntent);
                break;
            //再次购买
            case R.id.btn_buy_again:
                requestBuyAgain();
//                startActivity(new Intent(this, OrderEvaluationActivity.class));
                break;
            //立即支付
            case R.id.btn_pay_now:
                Intent payChannelIntent = new Intent(this, PayChannelActivity.class);
//                payChannelIntent.putExtra("orderId", orderId);
//                payChannelIntent.putExtra("price", orderDetailInfo.goodsSellRecord.price);
                payChannelIntent.putExtra("orderId", orderId);
                payChannelIntent.putExtra("orderCode", orderDetailInfo.orderNo);
                payChannelIntent.putExtra("price", orderDetailInfo.totalPay);
                payChannelIntent.putExtra("businessName", orderDetailInfo.businessName);
                payChannelIntent.putExtra("logo", orderDetailInfo.businessImg);
                payChannelIntent.putExtra("createTime", orderDetailInfo.createTime);
                startActivity(payChannelIntent);
                break;
            //确认完成
            case R.id.btn_confirm_completed:
                requestConfirmCompleted();
                break;
            //取消订单
            case R.id.btn_cancel_order:
                showCancelDialog();
//                requestCancelOrder();
                break;
            //联系商家
            case R.id.rl_contact_business:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderDetailInfo.businessMobile));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                startActivity(intent);
                break;
            //联系骑手
            case R.id.btn_contact_driver:
                Intent data = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderDetailInfo.shopperMobile));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                startActivity(data);
                break;
            //评价
            case R.id.btn_appraise:
                Intent evaluationIntent = new Intent(this, OrderEvaluationActivity.class);
                evaluationIntent.putExtra("oid", orderId);
                evaluationIntent.putExtra("logo", orderDetailInfo.businessImg);
                evaluationIntent.putExtra("businessName", orderDetailInfo.businessName);
                evaluationIntent.putExtra("isDeliver", orderDetailInfo.isDeliver);
                startActivity(evaluationIntent);
                break;
        }
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("提前联系商家可以提高退款效率哦");
        tv.setTextColor(Color.parseColor("#757575"));
        tv.setTextSize(14);
        tv.setPadding(ViewUtils.dip2px(this, 16), ViewUtils.dip2px(this, 16), 0, 0);
        alertDialog = builder
                .setNegativeButton("取消", null)
                .setCustomTitle(tv)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestCancelOrder();
                    }
                }).show();

    }

    private void requestBuyAgain() {
        CustomApplication.getRetrofitNew().buyAgainNew(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(OrderDetailActivity.this, BusinessNewActivity.class);
                        intent.putExtra("businessId", orderDetailInfo.businessId);
                        startActivity(intent);
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
//                        Intent intent = new Intent(OrderDetailActivity.this, BusinessActivity.class);
//                        intent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_LIST);
//                        intent.putExtra("orderInfo", orderDetailInfo.goodsSellRecord.businessId);
//                        startActivity(intent);
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

    private void requestConfirmCompleted() {
        CustomApplication.getRetrofit().receiveOrder(orderId).enqueue(new MyCallback<String>() {

            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    ToastUtil.showToast(jsonObject.optString("msg"));
                    if (jsonObject.optBoolean("success")) {
                        getOrderDetail(orderId);
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

    private void requestCancelOrder() {
        CustomApplication.getRetrofitNew().cancelorderNew(orderId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        getOrderDetail(orderId);
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
//        CustomApplication.getRetrofit().cancelOrder(orderId).enqueue(new MyCallback<String>() {
//
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                getOrderDetail(orderId, userInfo);
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    return true;
                }
            }

//            if (scrollView.getVisibility() == View.GONE) {
            if (mRefreshLayout.getVisibility() == View.GONE) {
                showBigMap(false);
                return true;
            }

            if (isFromePayFinish) {
                Intent data = new Intent(OrderDetailActivity.this, MainActivity.class);
                data.putExtra("currentPage", 2);
                startActivity(data);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_map:
//                if (scrollView.getVisibility() == View.GONE) {
                if (mRefreshLayout.getVisibility() == View.GONE) {
                    showBigMap(false);
                } else {
                    if (isFromePayFinish) {
                        Intent data = new Intent(OrderDetailActivity.this, MainActivity.class);
                        data.putExtra("currentPage", 2);
                        startActivity(data);
                    }
                    finish();
                }
                break;
            case R.id.view_top:
                showBigMap(true);
                break;
            case R.id.iv_close_map:
                showBigMap(false);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mRefreshLayout.setEnabled(false);
        getOrderDetail(orderId);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

}
