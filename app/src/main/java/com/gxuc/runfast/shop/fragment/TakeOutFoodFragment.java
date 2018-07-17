package com.gxuc.runfast.shop.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.activity.DeliveryAddressActivity;
import com.gxuc.runfast.shop.activity.SearchProductActivity;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.adapter.NearbyBusinessAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.HomeDataItemBean;
import com.gxuc.runfast.shop.bean.mainmiddle.MiddleSort;
import com.gxuc.runfast.shop.bean.mainmiddle.MiddleSorts;
import com.gxuc.runfast.shop.bean.maintop.MapInfo;
import com.gxuc.runfast.shop.bean.maintop.MapInfos;
import com.gxuc.runfast.shop.bean.maintop.TopImage;
import com.gxuc.runfast.shop.bean.maintop.TopImage1;
import com.gxuc.runfast.shop.bean.maintop.TopImages;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.view.recyclerview.PagingScrollHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

import static com.gxuc.runfast.shop.config.IntentConfig.AGENT_ID;

/**
 * 首页
 * A simple {@link Fragment} subclass.
 */
public class TakeOutFoodFragment extends Fragment implements
        BGARefreshLayout.BGARefreshLayoutDelegate,
        View.OnClickListener, PagingScrollHelper.onPageChangeListener, LoadMoreAdapter.LoadMoreApi {


    Unbinder unbinder;
    //    @BindView(R.id.roll_view_pager)
//    RollPagerView pagerView;
//    @BindView(R.id.scrollView)
//    CustomScrollView scrollView;
    @BindView(R.id.layout_search_location)
    LinearLayout layoutSearchLocation;
    @BindView(R.id.layout_search_input)
    RelativeLayout layoutSearchInput;

    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerView;

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.ll_no_business)
    LinearLayout llNoBusiness;
//    @BindView(R.id.rv_home_middle)
//    RecyclerView mRvHomeMiddle;
//    @BindView(R.id.rv_home_bottom)
//    RecyclerView mRvHomeBottom;

    private List<HomeDataItemBean> businessInfos = new ArrayList<>();
    private List<HomeDataItemBean> businessNewInfos = new ArrayList<>();
    private double pointLat;
    private double pointLon;

    private int page = 1;
    private LinearLayoutManager linearLayoutManager;
    //    private List<TopImage> imgurl;//轮播图地址
//    private List<TopImage1> imgurl1;//轮播图地址
//    private List<MiddleSort> middleurl;//中部模块数据
    //private NormalAdapter mNormalAdapter;
    private int mAgentId;//代理商id

    //private PageScrollAdapter myAdapter;
    //private HorizontalPageLayoutManager horizontalPageLayoutManager;
    //private HorizontalPageLayoutManager horizontalPageLayoutManager1;
    //private PagingScrollHelper scrollHelper;
    // private static final int MROWS = 2;//行数
    //private static final int MCOLUMNS = 4;//列数
    // private BottomPageAdapter mBottomPageAdapter;
    private Intent mIntent;
    private LoadMoreAdapter moreAdapter;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private HomeDataItemBean itemBean1;
    private HomeDataItemBean itemBean2;

    private int scrollY = 0;
    private int height;

    public TakeOutFoodFragment() {
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (aMapLocation.getLatitude() != 0.0 && aMapLocation.getLongitude() != 0.0) {
                        tvAddress.setText(aMapLocation.getPoiName());
                        pointLat = aMapLocation.getLatitude();
                        pointLon = aMapLocation.getLongitude();
                        Log.e("AmapError", "pointLat:"
                                + pointLat + ", pointLon:"
                                + pointLon);
                        CustomProgressDialog.startProgressDialog(getActivity());
                        netPostAddress(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                        if (mLocationClient != null) {
                            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        }
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_out_food, container, false);
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
        initData();
        initRefreshLayout(view);
        setData();
        setListener();
        return view;
    }

    private void initRefreshLayout(View view) {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        BGAMeiTuanRefreshViewHolder meiTuanRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(getActivity(), true);
        meiTuanRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        meiTuanRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        meiTuanRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
        mRefreshLayout.setRefreshViewHolder(meiTuanRefreshViewHolder);
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
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void setData() {
        NearbyBusinessAdapter mAdapter = new NearbyBusinessAdapter(getActivity(), businessNewInfos);
        moreAdapter = new LoadMoreAdapter(getActivity(), mAdapter);
        moreAdapter.setLoadMoreListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(moreAdapter);

        mAdapter.setOnItemClickListener(new NearbyBusinessAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(BusinessInfo businessInfo, View view) {
                Intent intent = new Intent(getContext(), BusinessActivity.class);
                intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_BOTTOM_PAGE);
                intent.putExtra("business", businessInfo);
                startActivity(intent);
            }
        });

    }

    private void setListener() {
//        scrollView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
//            @Override
//            public void onScroll(int scrollY) {
//                int height = pagerView.getHeight();
//                Log.d("scrollY", "scrollY = " + scrollY + ",height =" + height);
//                if (scrollY > height) {
//                    layoutSearchLocation.setVisibility(View.GONE);
//                    layoutSearchInput.setVisibility(View.VISIBLE);
//                } else if (scrollY < 0) {
//                    layoutSearchLocation.setVisibility(View.GONE);
//                    layoutSearchInput.setVisibility(View.GONE);
//                } else {
//                    layoutSearchLocation.setVisibility(View.VISIBLE);
//                    layoutSearchInput.setVisibility(View.GONE);
//                }
//            }
//        });


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (mRefreshLayout.isEnabled()) {
                    scrollY += dy;
                }
                Log.d("scrollY", "mRefreshLayout.isEnabled() = " + mRefreshLayout.isEnabled());
                Log.d("scrollY", "scrollY = " + dy + ",height =" + height);
                if (scrollY > height) {
                    layoutSearchLocation.setVisibility(View.GONE);
                    layoutSearchInput.setVisibility(View.VISIBLE);
                } else if (scrollY < 0) {
                    layoutSearchLocation.setVisibility(View.GONE);
                    layoutSearchInput.setVisibility(View.GONE);
                } else {
                    layoutSearchLocation.setVisibility(View.VISIBLE);
                    layoutSearchInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {


            }
        });
    }

    private void initData() {

//        netPostAddress(pointLon, pointLat);

//        //轮播页
//        imgurl = new ArrayList<>();
//        mNormalAdapter = new NormalAdapter(getActivity(), imgurl);
//        pagerView.setAdapter(mNormalAdapter);

//        //中部横向滚动
//        middleurl = new ArrayList<>();
//        myAdapter = new PageScrollAdapter(getActivity(), middleurl);
//        mRvHomeMiddle.setAdapter(myAdapter);
//        horizontalPageLayoutManager = new HorizontalPageLayoutManager(MROWS, MCOLUMNS);
//        mRvHomeMiddle.setLayoutManager(horizontalPageLayoutManager);
//        scrollHelper = new PagingScrollHelper();
//        scrollHelper.setUpRecycleView(mRvHomeMiddle);
//        scrollHelper.setOnPageChangeListener(this);

//        //底部滚动
//        imgurl1 = new ArrayList<>();
//        horizontalPageLayoutManager1 = new HorizontalPageLayoutManager(1, 3);
//        mRvHomeBottom.setLayoutManager(horizontalPageLayoutManager1);
//        mBottomPageAdapter = new BottomPageAdapter(getActivity(), imgurl1);
//        mRvHomeBottom.setAdapter(mBottomPageAdapter);
    }

    /**
     * 上传经纬度
     */
    private void netPostAddress(final Double lon, final Double lat) {

        if (page == 1) {
            businessInfos.clear();
            businessNewInfos.clear();
            moreAdapter.resetLoadState();
//            moreAdapter.notifyDataSetChanged();
        }

//        CustomApplication.getRetrofit().postAddress(lon, lat).enqueue(this);
        //TODO 经纬度
//        CustomApplication.getRetrofit().postAddress(110.07, 23.38).enqueue(new MyCallback<String>() {
        CustomApplication.getRetrofit().postAddress(lon, lat, 1).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                MapInfos mapInfos = GsonUtil.parseJsonWithGson(data, MapInfos.class);
                mAgentId = mapInfos.getAgentId();
                AGENT_ID = mAgentId;
                MapInfo map1 = mapInfos.getMap();
                if (map1 != null) {
//                    pointLat = map1.getLatitude();
//                    pointLon = map1.getLongitude();
                    pointLat = lat;
                    pointLon = lon;
                }
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.AGENTID, String.valueOf(mAgentId));
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLAT, String.valueOf(pointLat));
                SharePreferenceUtil.getInstance().putStringValue(CustomConstant.POINTLON, String.valueOf(pointLon));
                CustomProgressDialog.stopProgressDialog();
                netHomeImage(mAgentId);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                CustomProgressDialog.stopProgressDialog();
                moreAdapter.loadFailed();
                if (mRefreshLayout != null) {
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.setEnabled(true);
                }
            }
        });
    }


    /**
     * 获取首页轮播图
     */
    private void netHomeImage(int agentId) {

        CustomApplication.getRetrofit().getAdvert(agentId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                TopImages topImages = GsonUtil.parseJsonWithGson(data, TopImages.class);

                HomeDataItemBean itemBean = new HomeDataItemBean();

                if (topImages != null && topImages.getRows1().size() > 0) {

                    for (TopImage topImage : topImages.getRows1()) {
                        itemBean.imgurl.add(topImage);
                    }
                    //mNormalAdapter.notifyDataSetChanged();

                }
//                LogUtil.e("getRows2", topImages.getRows2().size() + "");
                itemBean1 = new HomeDataItemBean();
                if (topImages != null && topImages.getRows2().size() > 0) {
                    for (TopImage1 topImage : topImages.getRows2()) {
//                        itemBean.imgurl1.add(topImage);
                        itemBean1.imgurl1.add(topImage);
                    }
                    //mBottomPageAdapter.notifyDataSetChanged();

                }

                itemBean2 = new HomeDataItemBean();
                if (topImages != null && topImages.getRows3().size() > 0) {
                    for (TopImage1 topImage1 : topImages.getRows3()) {
                        itemBean2.imgurl1.add(topImage1);
//                        itemBean.imgurl.add(topImage);

                    }
                    //mBottomPageAdapter.notifyDataSetChanged();
                }

                businessInfos.add(itemBean);
                CustomProgressDialog.stopProgressDialog();
                netHomePager(mAgentId);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * 获取首页
     */
    private void netHomePager(int agentId) {
        CustomApplication.getRetrofit().getHomePage(0, agentId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                MiddleSorts middleSorts = GsonUtil.parseJsonWithGson(data, MiddleSorts.class);
                HomeDataItemBean itemBean = new HomeDataItemBean();
                if (middleSorts != null && middleSorts.getRows().size() > 0) {
                    for (MiddleSort middle : middleSorts.getRows()) {
                        itemBean.middleurl.add(middle);
                    }
//                    for (int i = 0; i < businessInfos.size(); i++) {
//                        businessNewInfos.add(businessInfos.get(i));
//                    }

//                    businessNewInfos.add(businessInfos.get(0));
//                    businessNewInfos.add(businessInfos.get(1));
//                    businessNewInfos.add(businessInfos.get(2));
                    //myAdapter.notifyDataSetChanged();
                }
                //加2次是为了在list中占个位置， position = 2的数据暂时没有
                businessInfos.add(itemBean);
                businessInfos.add(itemBean1);

                businessNewInfos.addAll(businessInfos);
                CustomProgressDialog.stopProgressDialog();
                Log.d("devon", "businessNewInfos.size" + businessNewInfos.size());
                getNearbyBusiness(pointLon, pointLat, page);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
//        CustomApplication.getRetrofit().getHomePage(0, 4).enqueue(this);
    }

    /**
     * 获取附近商家
     */
    private void getNearbyBusiness(Double lon, Double lat, int pager) {
        CustomApplication.getRetrofit().getNearbyBusinesses(String.valueOf(lon), String.valueOf(lat), pager, 1).enqueue(new MyCallback<String>() {
            //        CustomApplication.getRetrofit().getNearbyBusinesses("110.07", "23.38", pager).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray bus = jsonObject.getJSONArray("rows");
                    llNoBusiness.setVisibility(businessNewInfos.size() > 3 ? View.GONE : View.VISIBLE);
                    mRefreshLayout.setEnabled(businessNewInfos.size() > 3);
                    if (bus == null || bus.length() <= 0) {
                        moreAdapter.loadAllDataCompleted();
                        mRefreshLayout.endRefreshing();
//                        mRefreshLayout.setEnabled(true);
                        return;
                    }
                    for (int i = 0; i < bus.length(); i++) {
                        JSONObject busObject = bus.getJSONObject(i);
                        BusinessInfo info = new BusinessInfo();
                        info.id = busObject.optInt("id");
                        info.mini_imgPath = busObject.optString("mini_imgPath");
                        info.imgPath = busObject.optString("imgPath");
                        info.isopen = busObject.optInt("isopen");
                        info.name = busObject.optString("name");
                        info.distance = busObject.optDouble("distance");
                        info.levelId = busObject.optInt("levelId");
                        info.salesnum = busObject.optInt("salesnum");
                        info.startPay = busObject.optDouble("startPay");
                        info.busshowps = busObject.optDouble("busshowps");
                        info.baseCharge = busObject.optDouble("baseCharge");
                        info.charge = busObject.optDouble("charge");
                        info.isDeliver = busObject.optInt("isDeliver");
                        info.speed = busObject.optString("speed");
                        info.goldBusiness = busObject.optBoolean("goldBusiness");
                        info.news = busObject.optInt("news");
                        info.alist = new ArrayList<>();
                        JSONArray alist = busObject.optJSONArray("alist");
                        if (alist != null) {
                            int length1 = alist.length();
                            for (int j = 0; j < length1; j++) {
                                JSONObject alistObject = alist.getJSONObject(j);
                                BusinessExercise exercise = new BusinessExercise();
                                exercise.ptype = alistObject.optInt("ptype");
                                exercise.fulls = alistObject.optDouble("fulls");
                                exercise.lesss = alistObject.optDouble("lesss");
                                exercise.showname = alistObject.optString("showname");
                                info.alist.add(exercise);
                            }
                        }
                        HomeDataItemBean itemBean = new HomeDataItemBean();
                        itemBean.businessInfos = info;
                        businessNewInfos.add(itemBean);
                    }
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.setEnabled(true);
                    moreAdapter.loadCompleted();

                    llNoBusiness.setVisibility(businessNewInfos.size() > 3 ? View.GONE : View.VISIBLE);
                    mRefreshLayout.setEnabled(businessNewInfos.size() > 3);
                    CustomProgressDialog.stopProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
//                CustomToast.INSTANCE.showToast(getContext(), "网络异常");
                CustomProgressDialog.stopProgressDialog();
                moreAdapter.loadFailed();
                if (mRefreshLayout != null) {
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_search_name_two, R.id.layout_select_address, R.id.layout_search_product, R.id.layout_search_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_search_product:
                mIntent = new Intent(getActivity(), SearchProductActivity.class);
                mIntent.putExtra("pointLat", pointLat);
                mIntent.putExtra("pointLon", pointLon);
                startActivity(mIntent);
                break;
            case R.id.layout_search_input:
                mIntent = new Intent(getActivity(), SearchProductActivity.class);
                mIntent.putExtra("pointLat", pointLat);
                mIntent.putExtra("pointLon", pointLon);
                startActivity(mIntent);
                break;
            case R.id.tv_search_name_two:
                startActivity(new Intent(getActivity(), SearchProductActivity.class));
                break;
            case R.id.layout_select_address:
                startActivityForResult(new Intent(getActivity(), DeliveryAddressActivity.class), 1001);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 1002) {
            return;
        }
        String address = data.getStringExtra("address");
        pointLat = data.getDoubleExtra("pointLat", 0.0);
        pointLon = data.getDoubleExtra("pointLon", 0.0);
        tvAddress.setText(address);
        page = 1;
        netPostAddress(pointLon, pointLat);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mRefreshLayout.setEnabled(false);
        scrollY = 0;
        page = 1;
        netPostAddress(pointLon, pointLat);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onClick(View v) {
        Integer positionBusiness = (Integer) v.getTag();
        if (positionBusiness < 3) {
            return;
        }
        Intent intent = new Intent(getContext(), BusinessActivity.class);
        intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_BOTTOM_PAGE);
        intent.putExtra("business", businessInfos.get(positionBusiness).businessInfos);
        startActivity(intent);
    }

    @Override
    public void onPageChange(int index) {
        switch (index) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    @Override
    public void loadMore() {
        page += 1;
        getNearbyBusiness(pointLon, pointLat, page);
    }
}
