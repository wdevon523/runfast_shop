package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.example.supportv1.utils.DeviceUtil;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.home.HomeDataInfo;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.GlideImageLoader;
import com.gxuc.runfast.shop.view.FilterView;
import com.gxuc.runfast.shop.view.IndicatorView;
import com.gxuc.runfast.shop.view.MyGridView;
import com.willy.ratingbar.BaseRatingBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.xutils.x;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter {

    private Context context;
    private AMapLocation aMapLocation;
    private ArrayList<HomeDataInfo> homeDataInfoList;

    private OnFilterListener mOnFilterListener;
    private BigDecimal bigDecimal = new BigDecimal("1000");

    public static final int HOME_ENTRANCE_PAGE_SIZE = 10;//首页菜单单页显示数量

    public HomeAdapter(Context context, AMapLocation aMapLocation, ArrayList<HomeDataInfo> homeDataInfoList) {
        this.context = context;
        this.aMapLocation = aMapLocation;
        this.homeDataInfoList = homeDataInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View topView = LayoutInflater.from(context).inflate(R.layout.item_home_one, parent, false);
                HomeDataViewHolder homeDataHolder = new HomeDataViewHolder(topView);
                return homeDataHolder;
            case 1:
                View businessView = LayoutInflater.from(context).inflate(R.layout.item_home_business, parent, false);
                NearByBusinessViewHolder nearByBusinessViewHolder = new NearByBusinessViewHolder(businessView);
                return nearByBusinessViewHolder;
//            case 2:
//                View thirdView = LayoutInflater.from(context).inflate(R.layout.item_home_third, parent, false);
//                HomeThirdViewHolder thirdHolder = new HomeThirdViewHolder(thirdView);
//                return thirdHolder;
//            case 3:
//                View businessView = LayoutInflater.from(context).inflate(R.layout.item_business_info, parent, false);
//                BusinessViewHolder businessHolder = new BusinessViewHolder(businessView);
//                return businessHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeDataInfo homeDataInfo = homeDataInfoList.get(position);
        if (holder instanceof HomeDataViewHolder) {
            final HomeDataViewHolder homeDataViewHolder = (HomeDataViewHolder) holder;
            initImages(homeDataViewHolder, homeDataInfo);
            initHomePage(homeDataViewHolder, homeDataInfo);
            initAgentZoneBusiness(homeDataViewHolder, homeDataInfo);
            initOffZoneGoods(homeDataViewHolder, homeDataInfo);

            homeDataViewHolder.fakeFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
                @Override
                public void onFilterClick(int position) {
                    mOnFilterListener.onFilterClick(position, homeDataViewHolder.fakeFilterView);
                }
            });

        } else if (holder instanceof NearByBusinessViewHolder) {
            NearByBusinessViewHolder nearByBusinessViewHolder = (NearByBusinessViewHolder) holder;
            initNearByBusiness(nearByBusinessViewHolder, homeDataInfo, position);
        }
    }

    private void initImages(HomeDataViewHolder homeDataViewHolder, HomeDataInfo homeDataInfo) {
        homeDataViewHolder.banner.setImageLoader(new GlideImageLoader());
        homeDataViewHolder.banner.setImages(homeDataInfo.advertInfoList);
        homeDataViewHolder.banner.isAutoPlay(true);
        homeDataViewHolder.banner.setDelayTime(3000);
        homeDataViewHolder.banner.setIndicatorGravity(BannerConfig.CENTER);
        homeDataViewHolder.banner.start();
    }

    private void initHomePage(final HomeDataViewHolder homeDataViewHolder, HomeDataInfo homeDataInfo) {
//        ArrayList<HomeCategory> homeCategories = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            homeCategories.addAll(homeDataInfo.homeCategoryList);
//        }

        LinearLayout.LayoutParams layoutParams12 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) DeviceUtil.getScreenWidth(context) / 2.0f));

        //首页菜单分页
        LinearLayout.LayoutParams entrancelayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) DeviceUtil.getScreenWidth(context) / 2.0f + 70));
        homeDataViewHolder.llHomeEntrance.setLayoutParams(entrancelayoutParams);
        homeDataViewHolder.vpHomeEntrance.setLayoutParams(layoutParams12);
        LayoutInflater inflater = LayoutInflater.from(context);
        //将RecyclerView放至ViewPager中：
        int pageSize = HOME_ENTRANCE_PAGE_SIZE;
        //一共的页数等于 总数/每页数量，并取整。
        int pageCount = (int) Math.ceil(homeDataInfo.homeCategoryList.size() * 1.0 / pageSize);

        List<View> viewList = new ArrayList<View>();
        for (int index = 0; index < pageCount; index++) {
            //每个页面都是inflate出一个新实例
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_home_entrance_vp, homeDataViewHolder.vpHomeEntrance, false);
            recyclerView.setLayoutParams(layoutParams12);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
            EntranceAdapter entranceAdapter = new EntranceAdapter(context, homeDataInfo.homeCategoryList, index, HOME_ENTRANCE_PAGE_SIZE);
            recyclerView.setAdapter(entranceAdapter);
            viewList.add(recyclerView);
        }
        CagegoryViewPagerAdapter adapter = new CagegoryViewPagerAdapter(viewList);
        homeDataViewHolder.vpHomeEntrance.setAdapter(adapter);
        homeDataViewHolder.idvHomeEntrance.setIndicatorCount(homeDataViewHolder.vpHomeEntrance.getAdapter().getCount());
        homeDataViewHolder.idvHomeEntrance.setCurrentIndicator(homeDataViewHolder.vpHomeEntrance.getCurrentItem());
        homeDataViewHolder.idvHomeEntrance.setVisibility(pageCount > 1 ? View.VISIBLE : View.GONE);
        homeDataViewHolder.vpHomeEntrance.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                homeDataViewHolder.idvHomeEntrance.setCurrentIndicator(position);
            }
        });
    }


    private void initAgentZoneBusiness(HomeDataViewHolder homeDataViewHolder, HomeDataInfo homeDataInfo) {
        if (homeDataInfo.agentZoneBusinessList.size() < 2) {
            return;
        }
        x.image().bind(homeDataViewHolder.ivPreferentialBuisnessOne, UrlConstant.ImageBaseUrl + homeDataInfo.agentZoneBusinessList.get(0).special_img);
        x.image().bind(homeDataViewHolder.ivPreferentialBuisnessTwo, UrlConstant.ImageBaseUrl + homeDataInfo.agentZoneBusinessList.get(1).special_img);

    }

    private void initOffZoneGoods(HomeDataViewHolder homeDataViewHolder, HomeDataInfo homeDataInfo) {
        OffZoneGoodAdapter offZoneGoodAdapter = new OffZoneGoodAdapter(context, homeDataInfo.offZoneGoodsList);
        homeDataViewHolder.preferentialGridview.setAdapter(offZoneGoodAdapter);
//        homeDataViewHolder.preferentialGridview.setOverScrollMode(View.OVER_SCROLL_NEVER);


    }

    private void initNearByBusiness(NearByBusinessViewHolder nearByBusinessViewHolder, HomeDataInfo homeDataInfo, int position) {
        NearByBusinessInfo nearByBusinessInfo = homeDataInfo.nearByBusinessInfo;
        BigDecimal distanceM = new BigDecimal(nearByBusinessInfo.distance);
        BigDecimal distanceKM = distanceM.divide(bigDecimal).setScale(2, RoundingMode.HALF_UP);

        x.image().bind(nearByBusinessViewHolder.ivHomeBusinessLogo, UrlConstant.ImageBaseUrl + nearByBusinessInfo.mini_imgPath, NetConfig.optionsLogoImage);
        nearByBusinessViewHolder.tvHomeBusinessName.setText(nearByBusinessInfo.name);
        nearByBusinessViewHolder.tvHomeBusinessLabel.setVisibility(nearByBusinessInfo.newBusiness ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.tvHomeBusinessCategory.setText(nearByBusinessInfo.saleRange);
        nearByBusinessViewHolder.tvHomeBusinessEvaluateSale.setText(nearByBusinessInfo.levelId + " 月售" + nearByBusinessInfo.salesnum);
//        nearByBusinessViewHolder.tvHomeBusinessDistanceTime.setText(nearByBusinessInfo.deliveryDuration + "分钟 | " + distanceKM + "km");
        nearByBusinessViewHolder.tvHomeBusinessDistance.setText(distanceKM + "km");
        nearByBusinessViewHolder.tvHomeBusinessTime.setText(nearByBusinessInfo.deliveryDuration + "分钟");

//        nearByBusinessViewHolder.tvHomeBusinessStartPriceAndShippingPrice.setText("起送 ¥ " + nearByBusinessInfo.startPay + " | 配送费 ¥ " + nearByBusinessInfo.deliveryFee);
        nearByBusinessViewHolder.tvHomeBusinessStartPrice.setText("起送 ¥ " + nearByBusinessInfo.startPay);
        nearByBusinessViewHolder.tvHomeBusinessShippingPrice.setText("配送费 ¥ " + nearByBusinessInfo.deliveryFee);
        nearByBusinessViewHolder.ivHomeBusinessGold.setVisibility(nearByBusinessInfo.goldBusiness ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.ivHomeBusinessIsCharge.setVisibility(nearByBusinessInfo.isDeliver == 0 ? View.VISIBLE : View.GONE);
        nearByBusinessViewHolder.ivHomeBusinessToTake.setVisibility(nearByBusinessInfo.autoTaking ? View.VISIBLE : View.GONE);
//        nearByBusinessViewHolder.rbHomeBusinessEvaluate.setRating(nearByBusinessInfo.levelId);
        nearByBusinessViewHolder.rbHomeBusinessEvaluate.setRating(2.7f);
    }


    @Override
    public int getItemCount() {
        return homeDataInfoList == null ? 0 : homeDataInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        }
//        if (position == 1) {
//            return 1;
//        }
//        if (position == 2) {
//            return 2;
//        }
        return 1;
    }

    public void setHomeDataInfoList(ArrayList<HomeDataInfo> homeDataInfoList) {
        this.homeDataInfoList = homeDataInfoList;
        notifyDataSetChanged();
    }

    class HomeDataViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.ll_above_business)
//        LinearLayout llAboveBusiness;
        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.vp_home_entrance)
        ViewPager vpHomeEntrance;
        @BindView(R.id.idv_home_entrance)
        IndicatorView idvHomeEntrance;
        @BindView(R.id.ll_home_entrance)
        LinearLayout llHomeEntrance;
        @BindView(R.id.iv_preferential_buisness_one)
        ImageView ivPreferentialBuisnessOne;
        @BindView(R.id.iv_preferential_buisness_two)
        ImageView ivPreferentialBuisnessTwo;
        @BindView(R.id.tv_preferential_more)
        TextView tvPreferentialMore;
        @BindView(R.id.preferential_gridview)
        MyGridView preferentialGridview;
        //        @BindView(R.id.tv_top_address)
//        TextView tvTopAddress;
//        @BindView(R.id.ll_top_address)
//        LinearLayout llTopAddress;
//        @BindView(R.id.ll_search)
//        LinearLayout llSearch;
        @BindView(R.id.fake_filterView)
        FilterView fakeFilterView;

        HomeDataViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

//        @OnClick({R.id.fake_filterView})
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.fake_filterView:
//
//                    break;
//                case R.id.ll_search:
//                    break;
//            }
//
//        }
    }

    class NearByBusinessViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home_business_logo)
        RoundedImageView ivHomeBusinessLogo;
        @BindView(R.id.iv_home_business_gold)
        ImageView ivHomeBusinessGold;
        @BindView(R.id.iv_home_business_is_charge)
        ImageView ivHomeBusinessIsCharge;
        @BindView(R.id.iv_home_business_to_take)
        ImageView ivHomeBusinessToTake;
        @BindView(R.id.tv_home_business_label)
        TextView tvHomeBusinessLabel;
        @BindView(R.id.tv_home_business_name)
        TextView tvHomeBusinessName;
        @BindView(R.id.tv_home_business_evaluate_sale)
        TextView tvHomeBusinessEvaluateSale;
        //        @BindView(R.id.tv_home_business_distance_time)
//        TextView tvHomeBusinessDistanceTime;
        @BindView(R.id.tv_home_business_distance)
        TextView tvHomeBusinessDistance;
        @BindView(R.id.tv_home_business_time)
        TextView tvHomeBusinessTime;
        //        @BindView(R.id.tv_home_business_start_price_shipping_price)
//        TextView tvHomeBusinessStartPriceAndShippingPrice;
        @BindView(R.id.tv_home_business_start_price)
        TextView tvHomeBusinessStartPrice;
        @BindView(R.id.tv_home_business_shipping_price)
        TextView tvHomeBusinessShippingPrice;
        @BindView(R.id.tv_home_business_status)
        TextView tvHomeBusinessStatus;
        @BindView(R.id.tv_home_business_category)
        TextView tvHomeBusinessCategory;
        @BindView(R.id.rb_home_business_evaluate)
        BaseRatingBar rbHomeBusinessEvaluate;
        @BindView(R.id.ll_home_business_contain_act)
        LinearLayout llHomeBusinessContainAct;

        NearByBusinessViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnFilterListener {
        void onFilterClick(int position, FilterView filterView);
    }

    public void setOnFilteristener(OnFilterListener mOnFilterListener) {
        this.mOnFilterListener = mOnFilterListener;
    }

}
