package com.gxuc.runfast.shop.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.example.supportv1.utils.DeviceUtil;
import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.AddressAdminActivity;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.activity.SearchProductActivity;
import com.gxuc.runfast.shop.activity.ShopCartActivity;
import com.gxuc.runfast.shop.activity.ordercenter.OrderDetailActivity;
import com.gxuc.runfast.shop.activity.usercenter.JoinBusinessActivity;
import com.gxuc.runfast.shop.adapter.HomeAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.Address;
import com.gxuc.runfast.shop.bean.BusinessCouponInfo;
import com.gxuc.runfast.shop.bean.FilterInfo;
import com.gxuc.runfast.shop.bean.RecentlyOrderInfo;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.home.AdvertInfo;
import com.gxuc.runfast.shop.bean.home.AgentInfo;
import com.gxuc.runfast.shop.bean.home.AgentZoneBusinessInfo;
import com.gxuc.runfast.shop.bean.home.HomeCategory;
import com.gxuc.runfast.shop.bean.home.HomeDataInfo;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;
import com.gxuc.runfast.shop.bean.home.OffZoneGoodsInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ColorUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.ChatView;
import com.gxuc.runfast.shop.view.CircleImageView;
import com.gxuc.runfast.shop.view.FilterView;
import com.gxuc.runfast.shop.view.HomeRedpackageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class HomeFragment extends Fragment implements AMapLocationListener {

    @BindView(R.id.home_recycleview)
    RecyclerView homeRecycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_top_address)
    TextView tvTopAddress;
    @BindView(R.id.tv_shop_cart_num)
    TextView tvShopCartNum;
    @BindView(R.id.ll_no_business)
    LinearLayout llNoBusiness;
    @BindView(R.id.tv_join_us)
    TextView tvJoinUs;
    @BindView(R.id.ll_no_net)
    LinearLayout llNoNet;
    @BindView(R.id.tv_refresh)
    TextView tvrefresh;
    @BindView(R.id.ll_home_top)
    LinearLayout llHomeTop;
    @BindView(R.id.ll_top_address)
    LinearLayout llTopAddress;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_shoppping_cart)
    RelativeLayout rlShopppingCart;
    @BindView(R.id.real_filterView)
    FilterView realFilterView;
    Unbinder unbinder;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private final int FIRST_PAGE = 0;

    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private AgentInfo agentInfo;
    private double pointLat;
    private double pointLon;
    private ArrayList<HomeDataInfo> homeDataInfoList = new ArrayList<>();
    private ArrayList<AdvertInfo> advertInfoList;
    private ArrayList<HomeCategory> homeCategoryList;
    private ArrayList<AgentZoneBusinessInfo> agentZoneBusinessList;
    private ArrayList<OffZoneGoodsInfo> offZoneGoodsList;

    private HomeAdapter homeAdapter;
    private AMapLocation myMapLocation;

    private int HOME_SELECT = 1000;

    private boolean isHide = false;
    private boolean isFirtIn = true;
    private int scrollY = 0;
    private int titleViewHeight = 30; // 标题栏的高度
    private ArrayList<NearByBusinessInfo> nearByBusinessInfoList;
    private View itemHeaderView;
    private int homeMarginHeight;
    private int homeTopViewHeight;
    private int homeTopViewTop;
    private LinearLayoutManager mLayoutManager;
    private int sorting = 1;
    private ChatView chatView;
    private HomeDataInfo homeDataInfo;
    private FilterView fakeFilterView;
    private IdentityHashMap<String, Integer> featureMap = new IdentityHashMap<>();
    private IdentityHashMap<String, Integer> actMap = new IdentityHashMap<>();
    private CircleImageView ivHomeBusinessLogo;
    private TextView tvHomeOrderStatus;
    private TextView tvDeliverTime;
    private RecentlyOrderInfo recentlyOrderInfo;
    private UserInfo userInfo;
    float fraction;
    private boolean isTooShort;
    private ArrayList<BusinessCouponInfo> businessCouponInfoList;
    private HomeRedpackageDialog homeRedpackageDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    10000);//自定义的code
        } else {
            initMap();
        }
        initView();
        initData();
        setListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            if (!isFirtIn) {
                userInfo = UserService.getUserInfo(getActivity());
                requestAllShopCart();
                if (agentInfo != null) {
                    requestRedPackage();
                    requestRecentlyOrder();
                }
            } else {
                isFirtIn = false;
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (aMapLocation.getLatitude() != 0.0 && aMapLocation.getLongitude() != 0.0) {
//                        tvAddress.setText(aMapLocation.getPoiName());

                    myMapLocation = aMapLocation;
                    pointLat = aMapLocation.getLatitude();
                    pointLon = aMapLocation.getLongitude();
                    Log.e("AmapError", "pointLat:"
                            + pointLat + ", pointLon:"
                            + pointLon + "，code" + aMapLocation.getAdCode());
                    tvTopAddress.setText(aMapLocation.getAoiName());
//                        CustomProgressDialog.startProgressDialog(getActivity());
                    requestGetAgent(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                    if (mLocationClient != null) {
                        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                    }

                    SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLAT, String.valueOf(pointLat));
                    SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLON, String.valueOf(pointLon));
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("AmapError", "权限允许");
        switch (requestCode) {
            case 10000:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    initMap();
                } else {
                    //用户拒绝授权
                }
                break;
        }
    }

    private void initMap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        //关闭缓存机制
//        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        Log.e("AmapError", "启动定位。。。。。。。。。。。。");
    }

    private void initView() {

        chatView = new ChatView(getActivity());
        ivHomeBusinessLogo = (CircleImageView) chatView.findViewById(R.id.iv_home_business_logo);
        tvHomeOrderStatus = (TextView) chatView.findViewById(R.id.tv_home_order_status);
        tvDeliverTime = (TextView) chatView.findViewById(R.id.tv_deliver_time);
//        chatView.show();

        mLayoutManager = new LinearLayoutManager(getContext());
        homeRecycleview.setLayoutManager(mLayoutManager);
        homeAdapter = new HomeAdapter(getContext(), myMapLocation, homeDataInfoList);
        homeRecycleview.setAdapter(homeAdapter);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {

                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        clearRecyclerViewData();
                        refreshData();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.setEnableLoadMore(true);//恢复上拉状态
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage++;
                        requestNearbyBusiness();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });

//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshData();
//                smartRefreshLayout.finishRefresh();
//            }
//        });

//        deliveryOrderAdapter.setOnRefreshListener(new DeliveryOrderAdapter.onRefreshListener() {
//            @Override
//            public void onRef() {
//                refreshData();
//            }
//        });

    }

    private void initData() {

        homeRecycleview.setHasFixedSize(true);
        homeRecycleview.setNestedScrollingEnabled(false);

        homeRecycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != SCROLL_STATE_IDLE && !isHide) {
                    rlShopppingCart.setTranslationX(120);
                    rlShopppingCart.setEnabled(false);
                    isHide = true;
                } else if (newState == SCROLL_STATE_IDLE && isHide) {
                    rlShopppingCart.setTranslationX(0);
                    rlShopppingCart.setEnabled(true);
                    isHide = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);

                if (smartRefreshLayout.isEnabled()) {
                    scrollY += dy;
                }
//
//                Log.d("scrollY", "mRefreshLayout.isEnabled() = " + smartRefreshLayout.isEnabled());
//                Log.d("devonxxx", "scrollY = " + scrollY + ",height =" + titleViewHeight);

                if (scrollY >= titleViewHeight) {
//                    llSearch.setLayoutParams(layoutParams);
                }

                if (itemHeaderView == null) {
                    itemHeaderView = homeRecycleview.getChildAt(0);
                }

                if (itemHeaderView != null) {
                    //----476
                    homeTopViewTop = DeviceUtil.px2dip(getContext(), itemHeaderView.getTop());
                    //526   486
                    homeTopViewHeight = DeviceUtil.px2dip(getContext(), itemHeaderView.getHeight());
                }
                //80
                homeMarginHeight = DeviceUtil.px2dip(getContext(), llHomeTop.getHeight());


//                LogUtil.d("wdevon", "-------homeTopViewHeight-------" + homeMarginHeight);
//                LogUtil.d("devon", "-------------- linearLayoutManager.findFirstVisibleItemPosition()--------------" + mLayoutManager.findFirstVisibleItemPosition());
//                LogUtil.d("devon", "-----homeTopViewHeight-------" + homeTopViewHeight + "-----homeTopViewTop-------" + homeTopViewTop + "----scrollY----" + scrollY + "+++++++++++" + (homeTopViewHeight + homeTopViewTop));

//                if (homeTopViewHeight + homeTopViewTop - homeMarginHeight <= 40) {
                if (homeTopViewHeight + homeTopViewTop <= 40 || mLayoutManager.findFirstVisibleItemPosition() > 0 || isTooShort) {
//                if (homeTopViewHeight + homeTopViewTop <= 40) {
                    realFilterView.setVisibility(View.VISIBLE);
                } else {
                    realFilterView.setVisibility(View.GONE);
                }

                handleTitleBarColorEvaluate();
            }
        });


    }

    private void setListener() {

        chatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recentlyOrderInfo != null) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("orderId", recentlyOrderInfo.id);
                    startActivity(intent);
                }
            }
        });

        chatView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        homeAdapter.setOnFilteristener(new HomeAdapter.OnFilterListener() {
            @Override
            public void onFilterClick(int position, FilterView filterView) {
                fakeFilterView = filterView;
                scrollToBusinessTop();
                if (position == 1) {
                    sorting = 2;
                    requestNearbyBusiness();
                } else if (position == 2) {
                    sorting = 3;
                    requestNearbyBusiness();
                } else {
                    realFilterView.show(position);
                }
                realFilterView.changeBold(position);
            }
        });

        // (真正的)筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                if (position == 1) {
                    sorting = 2;
                    realFilterView.hide();
                    requestNearbyBusiness();
                    scrollToBusinessTop();
                } else if (position == 2) {
                    sorting = 3;
                    realFilterView.hide();
                    requestNearbyBusiness();
                    scrollToBusinessTop();
                } else {
                    realFilterView.show(position);
                }
                if (fakeFilterView != null) {
                    fakeFilterView.changeBold(position);
                }
                realFilterView.changeBold(position);
            }
        });

        // 分类Item点击
        realFilterView.setOnItemCategoryClickListener(new FilterView.OnItemCategoryClickListener() {
            @Override
            public void onItemCategoryClick(FilterInfo filterInfo) {
//                fillAdapter(ModelUtil.getCategoryTravelingData(leftEntity, rightEntity));
                sorting = filterInfo.type;
                if (fakeFilterView != null) {
                    fakeFilterView.setTvCategoryTitle(filterInfo);
                }
                realFilterView.setTvCategoryTitle(filterInfo);
                scrollToBusinessTop();
                requestNearbyBusiness();

            }
        });

        //筛选，完成
        realFilterView.setOnFiltrateClickListener(new FilterView.OnFiltrateClickListener() {
            @Override
            public void onFiltrateClick(ArrayList<FilterInfo> filterInfoFeatureList, ArrayList<FilterInfo> filterInfoActList) {
                featureMap.clear();
                actMap.clear();
                for (int i = 0; i < filterInfoFeatureList.size(); i++) {
                    if (filterInfoFeatureList.get(i).isCheck) {
                        featureMap.put(new String("businessFeature"), filterInfoFeatureList.get(i).type);
                    }
                }

                if (filterInfoActList.get(5).isCheck) {
                    for (int i = 0; i < filterInfoActList.size() - 1; i++) {
                        actMap.put(new String("activityType"), filterInfoActList.get(i).type);
                    }
                } else {
                    for (int i = 0; i < filterInfoActList.size(); i++) {
                        if (filterInfoActList.get(i).isCheck) {
                            actMap.put(new String("activityType"), filterInfoActList.get(i).type);
                        }
                    }
                }

                currentPage = FIRST_PAGE;
                isLastPage = false;
                requestNearbyBusiness();
            }

        });

        realFilterView.setOnHideListener(new FilterView.OnHideListener() {
            @Override
            public void onHide() {
                if (isTooShort) {
                    isTooShort = false;
                    realFilterView.setVisibility(View.GONE);
                }
            }
        });

        homeAdapter.setOnNearByBusinessClickListener(new HomeAdapter.OnNearByBusinessClickListener() {
            @Override
            public void onNearByBusinessClickListener(int position, NearByBusinessInfo nearByBusinessInfo) {
                Intent intent = new Intent(getContext(), BusinessNewActivity.class);
                intent.putExtra("businessId", nearByBusinessInfo.id);
                startActivity(intent);
            }
        });
    }

    private void scrollToBusinessTop() {
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        isTooShort = false;
        if (homeRecycleview.getChildAt(1 - firstItem) != null && homeAdapter.getItemCount() > 4) {
            int top = homeRecycleview.getChildAt(1 - firstItem).getTop() - 105;
            homeRecycleview.smoothScrollBy(0, top);
        } else {
            if (homeRecycleview.getChildAt(1 - firstItem) != null) {
                int top = homeRecycleview.getChildAt(1 - firstItem).getTop() - 105;
                homeRecycleview.smoothScrollBy(0, top);
            }
            isTooShort = true;
            realFilterView.setVisibility(View.VISIBLE);
        }

//        if (1 <= firstItem) {
//            homeRecycleview.scrollToPosition(1);
//            homeRecycleview.smoothScrollBy(0, -110);
//        } else if (2 < lastItem) {
//            int top = homeRecycleview.getChildAt(1 - firstItem).getTop() - 105;
//            homeRecycleview.smoothScrollBy(0, top);
//        } else {
//            isTooShort = true;
//            realFilterView.setVisibility(View.VISIBLE);
//            if (firstItem != 0 && lastItem != 0) {
//                int top = homeRecycleview.getChildAt(1 - firstItem).getTop() - 105;
//                homeRecycleview.smoothScrollBy(0, top);
//            }
//            homeRecycleview.scrollToPosition(1);
//            homeRecycleview.smoothScrollBy(0, -10);
//                    move = true;
//        }
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        requestGetAgent(pointLon, pointLat);
    }

    private void clearRecyclerViewData() {
        homeDataInfoList.clear();
        homeAdapter.setHomeDataInfoList(homeDataInfoList);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d("wdevon", "HomeFragment is hidden" + hidden);
        if (!hidden) {
//            refreshData();
            StatusUtil.setSystemStatus(getActivity(), true, true);
//            StatusUtil.setUseStatusBarColor(getActivity(), ColorUtil.getNewColorByStartEndColor(getContext(), fraction, R.color.bg_fba42a, R.color.snow));
//            StatusUtil.setUseStatusBarColor(getActivity(), getResources().getColor(R.color.white));
            userInfo = UserService.getUserInfo(getActivity());
            requestAllShopCart();
            if (agentInfo != null) {
                requestRedPackage();
                requestRecentlyOrder();
            }
        } else {
            if (chatView != null) {
                chatView.hide();
            }
        }
    }

    private void requestRecentlyOrder() {

        userInfo = UserService.getUserInfo(getActivity());

        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getRecentlyOrder(agentInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        recentlyOrderInfo = JsonUtil.fromJson(data, RecentlyOrderInfo.class);
                        if (recentlyOrderInfo != null) {
                            fillRecentlyOrder();
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

    private void fillRecentlyOrder() {
        chatView.hide();

        x.image().bind(ivHomeBusinessLogo, UrlConstant.ImageBaseUrl + recentlyOrderInfo.businessImg, NetConfig.optionsLogoImage);
        tvHomeOrderStatus.setText(recentlyOrderInfo.statStr);

        SpannableStringBuilder builder = new SpannableStringBuilder("预计送达: " + recentlyOrderInfo.disTime.substring(11, 16));
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.text_fb4e44));
        builder.setSpan(span, 6, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDeliverTime.setText(builder);

        if (!isHidden()) {
            if (recentlyOrderInfo.status >= 1 && recentlyOrderInfo.status < 8) {
                chatView.show();
            }
        }

    }


    private void requestAllShopCart() {

        if (userInfo == null) {
            rlShopppingCart.setVisibility(View.GONE);
            return;
        }

        CustomApplication.getRetrofitNew().getAllShopCart().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        dealShopCart(data);
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

    private void dealShopCart(String data) {
        int total = 0;
        ArrayList<ShopCartBean> shopCartBeanList = JsonUtil.fromJson(data, new TypeToken<ArrayList<ShopCartBean>>() {
        }.getType());
        if (shopCartBeanList != null && shopCartBeanList.size() > 0) {
            for (int i = 0; i < shopCartBeanList.size(); i++) {
                for (int j = 0; j < shopCartBeanList.get(i).cartItems.size(); j++) {
                    total += shopCartBeanList.get(i).cartItems.get(j).num;
                }
            }
        }
        tvShopCartNum.setVisibility(total == 0 ? View.GONE : View.VISIBLE);
        rlShopppingCart.setVisibility(total == 0 ? View.GONE : View.VISIBLE);
        tvShopCartNum.setText(total + "");
    }

    private void requestGetAgent(double longitude, double latitude) {
        CustomApplication.getRetrofitNew().getAgent(longitude, latitude).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                llNoNet.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        llNoBusiness.setVisibility(View.GONE);
                        String data = jsonObject.optString("data");
                        agentInfo = JsonUtil.fromJson(data, AgentInfo.class);
                        SharePreferenceUtil.getInstance().putStringValue(CustomConstant.AGENTID, agentInfo.id);
                        requestRecentlyOrder();
                        requestRedPackage();
                        requestGetHomeAct();
                    } else {
//                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        llNoBusiness.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                llNoNet.setVisibility(View.VISIBLE);
                llNoBusiness.setVisibility(View.GONE);
            }
        });


    }

    private void requestGetHomeAct() {
        CustomApplication.getRetrofitNew().getHomeAct(agentInfo.id, pointLon, pointLat).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        dealData(data);
                        requestNearbyBusiness();
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

    private void dealData(JSONObject data) {
        //轮播图
        String advert = data.optJSONArray("advertList").toString();
        advertInfoList = JsonUtil.fromJson(advert, new TypeToken<ArrayList<AdvertInfo>>() {
        }.getType());
        homeDataInfo = new HomeDataInfo();
        homeDataInfo.advertInfoList = advertInfoList;

        //分类
        String homePage = data.optJSONArray("homepageList").toString();
        homeCategoryList = JsonUtil.fromJson(homePage, new TypeToken<ArrayList<HomeCategory>>() {
        }.getType());
        homeDataInfo.homeCategoryList = homeCategoryList;

        //优惠专区
        String agentZoneBusiness = data.optJSONArray("agentZoneBusiness").toString();
        agentZoneBusinessList = JsonUtil.fromJson(agentZoneBusiness, new TypeToken<ArrayList<AgentZoneBusinessInfo>>() {
        }.getType());

        homeDataInfo.agentZoneBusinessList = agentZoneBusinessList;

        //特惠优选
        String offZoneGoods = data.optJSONArray("offZoneGoods").toString();
        offZoneGoodsList = JsonUtil.fromJson(offZoneGoods, new TypeToken<ArrayList<OffZoneGoodsInfo>>() {
        }.getType());

        homeDataInfo.offZoneGoodsList = offZoneGoodsList;

        homeDataInfoList.add(homeDataInfo);

        homeAdapter.setHomeDataInfoList(homeDataInfoList);

    }

    private void requestNearbyBusiness() {

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getNearByBusiness(agentInfo.id, pointLon, pointLat, sorting, actMap, featureMap, new IdentityHashMap<String, String>(), currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        if (currentPage == 0) {
                            homeDataInfoList.clear();
                            homeDataInfoList.add(homeDataInfo);
                            homeAdapter.setHomeDataInfoList(homeDataInfoList);
                        }
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            dealNearByBusinessData(jsonArray.toString());
                        } else {
                            isLastPage = true;
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

    private void dealNearByBusinessData(String data) {
        nearByBusinessInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<NearByBusinessInfo>>() {
        }.getType());
        for (int i = 0; i < nearByBusinessInfoList.size(); i++) {
            HomeDataInfo homeNearByDataInfo = new HomeDataInfo();
            homeNearByDataInfo.nearByBusinessInfo = nearByBusinessInfoList.get(i);
            homeDataInfoList.add(homeNearByDataInfo);
        }
        homeAdapter.setHomeDataInfoList(homeDataInfoList);
    }

    private void requestRedPackage() {
        if (userInfo == null) {
            return;
        }
        long timeMillis = System.currentTimeMillis();
        long redPackageTime = SharePreferenceUtil.getInstance().getLongValue(CustomConstant.RED_PACKAGE_TIME);
        if (timeMillis - redPackageTime < 10800000) {
            return;
        }
        SharePreferenceUtil.getInstance().putLongValue(CustomConstant.RED_PACKAGE_TIME, timeMillis);

        CustomApplication.getRetrofitNew().getHomeRedpackge(agentInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data != null && data.length() > 0) {
                            dealCoupon(data.toString());
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

    /**
     * 解析数据
     *
     * @param data
     */
    private void dealCoupon(String data) {

        businessCouponInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<BusinessCouponInfo>>() {
        }.getType());

        if (businessCouponInfoList != null && businessCouponInfoList.size() > 0) {
//            mAdapter.setList(businessCouponInfoList);
            homeRedpackageDialog = new HomeRedpackageDialog(getContext(), businessCouponInfoList, new HomeRedpackageDialog.OnDialogClickListener() {
                @Override
                public void onDialogClick(int position) {
                    BusinessCouponInfo businessCouponInfo = businessCouponInfoList.get(position);
                    receiverCoupon(position, businessCouponInfo);
                }
            });
            homeRedpackageDialog.show();
        }
    }


    /**
     * 领取优惠券
     */
    private void receiverCoupon(final int position, final BusinessCouponInfo businessCouponInfo) {

        CustomApplication.getRetrofitNew().receiverBusinessCoupon(businessCouponInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("领取成功");
//                        homeRedpackageDialog.dismiss();
                        businessCouponInfo.picked = true;
                        homeRedpackageDialog.updateData(businessCouponInfoList);

                        if (businessCouponInfo.ptype == 10) {
                            if (homeRedpackageDialog.isShowing()) {
                                homeRedpackageDialog.dismiss();
                            }
                            Intent intent = new Intent(getContext(), BusinessNewActivity.class);
                            intent.putExtra("businessId", businessCouponInfo.busId);
                            startActivity(intent);
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

    @OnClick({R.id.tv_refresh, R.id.tv_join_us, R.id.ll_top_address, R.id.ll_search, R.id.rl_shoppping_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_refresh:
                refreshData();
                break;
            case R.id.tv_join_us:
                startActivity(new Intent(getContext(), JoinBusinessActivity.class));
                break;
            case R.id.ll_top_address:
                Intent intent = new Intent(getContext(), AddressAdminActivity.class);
                Address address = new Address();
                if (myMapLocation != null) {
                    address.address = myMapLocation.getCity();
                    address.title = myMapLocation.getAoiName();
                    address.latLng = new LatLng(myMapLocation.getLatitude(), myMapLocation.getLongitude());
                }
                intent.putExtra("Address", address);
                startActivityForResult(intent, HOME_SELECT);
                break;
            case R.id.ll_search:
                Intent mIntent = new Intent(getActivity(), SearchProductActivity.class);
                mIntent.putExtra("pointLat", pointLat);
                mIntent.putExtra("pointLon", pointLon);
                startActivity(mIntent);
                break;
            case R.id.rl_shoppping_cart:
                Intent data = new Intent(getContext(), ShopCartActivity.class);
                startActivity(data);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == HOME_SELECT) {
            Address address = (Address) data.getParcelableExtra("Address");
            if (address != null && address.latLng != null) {
                fillView(address);
                pointLat = address.latLng.latitude;
                pointLon = address.latLng.longitude;
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLAT, String.valueOf(pointLat));
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLON, String.valueOf(pointLon));
                refreshData();
            }
        }
    }

    private void fillView(Address address) {
        tvTopAddress.setText(address.title);
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {

        if (homeTopViewTop > 0) {
            fraction = 1f - homeTopViewTop * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            llHomeTop.setAlpha(fraction);
            return;
        }

        float space = Math.abs(homeTopViewTop) * 1f;
        fraction = space / (homeMarginHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        llHomeTop.setAlpha(1f);
        llSearch.setAlpha(1f);
//        if (fraction >= 1f || isStickyTop) {
        if (fraction >= 1f) {
//            isStickyTop = true;
//            viewTitleBg.setAlpha(0f);
//            viewActionMoreBg.setAlpha(0f);
            llHomeTop.setBackgroundColor(getContext().getResources().getColor(R.color.snow));
            llSearch.setBackgroundColor(getContext().getResources().getColor(R.color.bg_f3f4f5));
//            StatusUtil.setUseStatusBarColor(getActivity(), getResources().getColor(R.color.snow));
//            llTopAddress.setVisibility(View.GONE);
        } else {
//            viewTitleBg.setAlpha(1f - fraction);
//            viewActionMoreBg.setAlpha(1f - fraction);
            llHomeTop.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getContext(), fraction, R.color.bg_fba42a, R.color.snow));
            llSearch.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getContext(), fraction, R.color.white, R.color.bg_f3f4f5));
//            StatusUtil.setUseStatusBarColor(getActivity(), ColorUtil.getNewColorByStartEndColor(getContext(), fraction, R.color.bg_fba42a, R.color.snow));
//            llTopAddress.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            Log.i("devon", "PendingOrderFragment-----" + !hidden);
//            refreshData();
//        }
//    }

}
