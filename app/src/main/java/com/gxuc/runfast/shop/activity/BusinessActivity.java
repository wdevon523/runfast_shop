package com.gxuc.runfast.shop.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.adapter.BusinessAdapter;
import com.gxuc.runfast.shop.adapter.shopcaradater.SpecCarAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.OptionBean;
import com.gxuc.runfast.shop.bean.SubOptionBean;
import com.gxuc.runfast.shop.bean.TypeBean;
import com.gxuc.runfast.shop.bean.business.BusinessAct;
import com.gxuc.runfast.shop.bean.business.BusinessDetail;
import com.gxuc.runfast.shop.bean.business.BusinessDetails;
import com.gxuc.runfast.shop.bean.maintop.TopImage;
import com.gxuc.runfast.shop.bean.order.ShoppingCartInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.fragment.BusinessInfoFragment;
import com.gxuc.runfast.shop.fragment.EvaluateFragment;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.LogUtils;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.ViewUtils;
import com.gxuc.runfast.shop.view.AppBarStateChangeListener;
import com.gxuc.runfast.shop.view.MaxHeightRecyclerView;
import com.gxuc.runfast.shop.view.ZFlowLayout;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.bean.SpecBean;
import com.gxuc.runfast.shop.bean.maintop.TopImage1;
import com.gxuc.runfast.shop.bean.order.ShoppingTrolley;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.fragment.BusinessFragment;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.view.AddWidget;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.order.ShoppingCarBean;
import com.gxuc.runfast.shop.view.HeadZoomScrollView;
import com.example.supportv1.utils.LogUtil;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shopex.cn.sharelibrary.ShareViewDataSource;
import shopex.cn.sharelibrary.SharedView;

/**
 * 商家界面
 */
public class BusinessActivity extends ToolBarActivity implements AddWidget.OnAddClick, View.OnClickListener, SpecCarAdapter.UpdateSpecCountImp {

    @BindView(R.id.tl_list_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_list_view)
    ViewPager mViewPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_business_name)
    TextView tvBusinessName;
    @BindView(R.id.ll_contain_act)
    LinearLayout llContainAct;
    @BindView(R.id.iv_business_logo)
    ImageView ivBusinessLogo;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.tv_sale_startPay)
    TextView tvSaleStartPay;
    @BindView(R.id.tv_sale_price)
    TextView tvSalePrice;
    @BindView(R.id.tv_sale_time)
    TextView tvSaleTime;
    @BindView(R.id.tv_business_sales_num)
    TextView tvSalesNum;
    @BindView(R.id.add_widget)
    AddWidget addWidgetDetail;
    @BindView(R.id.iv_title_bg)
    ImageView ivTitleBg;
    @BindView(R.id.iv_collection)
    ImageView mIvCollection;
    @BindView(R.id.iv_share)
    ImageView ivShare;

    private TextView car_badge, car_limit, tv_amount, tv_old_amount, tv_sale_price_bottom;

    private ImageView iv_shop_car;

//    private CarAdapter carAdapter;

    private SpecCarAdapter carAdapter;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mStringList = new ArrayList<>();

    private BusinessAdapter mAdapter;

    public View blackView, scroll_container;

    private int shopWidth;
    private int[] carLoc;

    public BottomSheetBehavior behavior;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private boolean sheetScrolling;
    private CoordinatorLayout rootview;
    private Toolbar mToolbar;
    private FrameLayout layoutShopCar;
    private AppBarLayout appBarLayout;
    private TextView business_title;
    private BigDecimal decimal;
    private RelativeLayout layoutProductDetail;

    //商品详情视图
    private TextView tvProductName;
    private TextView tvProductSale;
    private TextView tvProductPrice;
    private TextView tvOldProductPrice;
    private TextView tvProductGiftName;
    private TextView tvProductContent;
    //private TextView tvProductCount;
    //private ImageView ivSub;
    private ImageView ivGoodsHead;
    private TextView tvSpec;
    private TextView tvSpecNum;
    private RelativeLayout layoutSpec1;

    //商品集合
    private List<FoodBean> foodBeens = new ArrayList<>();
    private List<FoodBean> carFoods = new ArrayList<>();
    //类型集合
    private List<TypeBean> types = new ArrayList<>();

    private int businessId;
    //规格选择弹框------------
    private AlertDialog specDialog;
    private TextView tvSpecTitle;
    private TextView tvSpecProperty;
    private TextView tvSpecPrice;
    private TextView tvSpecOldPrice;
    private ZFlowLayout layoutSpec;
    private ZFlowLayout layoutProduct;

    List<SpecBean> specBeanList = new ArrayList<>();
    List<OptionBean> optionBeenList = new ArrayList<>();
    private Integer positionSpec;
    private TextView tvSpecSelect;
    private ImageView tvAdd;
    private ImageView ivAddNum;
    private TextView tvSpecCount;
    private ImageView ivSubNum;
    private TextView tvSpecPropertyTwo;
    private ZFlowLayout layoutProductTwo;
    private LinearLayout layoutGood;
    private LinearLayout layoutGoodOne;
    private LinearLayout layoutGoodTwo;
    private int point = 0;
    private FoodBean foodBeanDetail;
    private String specName;
    private String typeName;
    private String typeNameTwo;
    private boolean isShowDetail;
    private FoodBean mFoodBean;
    //起送价
    private Double startPay;
    //配送费
    private Double salePrice;
    private String specId;//规格id
    private String typeId;
    private String optionIds;
    private BusinessDetail business;
    private ArrayList<BusinessAct> businessActList;
    private LinearLayout llProductAct;
    private TextView tvProductDiscount;
    private TextView tvProductLimit;
    private AlertDialog alertDialog;
    private UserInfo userInfo;
    private int specNum = 1;
    private RelativeLayout rlPacking;
    private TextView tvPackingPrice;
    private String packing;
    private BigDecimal totalPacking = new BigDecimal(0.0);
    private boolean isResume = false;
    private String lat;
    private String lon;
    private int goodId;

    //-----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
        initView();
        initProductView();
        initData();
        initShopCar();
        setData();
        setListener();
        initDialog();
//        getBusiness(businessId);
//        getBusinessCollection();
    }

    /**
     * 初始化规格弹框界面
     */
    private void initDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_spec_select, null);
        tvSpecTitle = (TextView) view.findViewById(R.id.tv_spec_title);
        tvSpecProperty = (TextView) view.findViewById(R.id.tv_spec_property);
        tvSpecPropertyTwo = (TextView) view.findViewById(R.id.tv_spec_property_two);
        tvSpecPrice = (TextView) view.findViewById(R.id.tv_spec_price);
        tvSpecOldPrice = (TextView) view.findViewById(R.id.tv_spec_old_price);
        tvSpecOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvAdd = (ImageView) view.findViewById(R.id.iv_add);
        ivAddNum = (ImageView) view.findViewById(R.id.iv_add_num);
        tvSpecCount = (TextView) view.findViewById(R.id.tv_spec_count);
        ivSubNum = (ImageView) view.findViewById(R.id.iv_sub_num);

        layoutSpec = (ZFlowLayout) view.findViewById(R.id.flow_layout_spec);
        layoutProduct = (ZFlowLayout) view.findViewById(R.id.flow_product_property);
        layoutProductTwo = (ZFlowLayout) view.findViewById(R.id.flow_product_property_two);


        layoutGood = (LinearLayout) view.findViewById(R.id.layout_product);
        layoutGoodOne = (LinearLayout) view.findViewById(R.id.layout_product_one);
        layoutGoodTwo = (LinearLayout) view.findViewById(R.id.layout_product_two);
        tvSpecSelect = (TextView) view.findViewById(R.id.tv_select_spec);
        ImageView ivCloseSpec = (ImageView) view.findViewById(R.id.iv_close_spec);

        ivAddNum.setOnClickListener(this);
        ivSubNum.setOnClickListener(this);
        tvSpecSelect.setOnClickListener(this);
        ivCloseSpec.setOnClickListener(this);
        specDialog = new AlertDialog.Builder(this).setView(view).create();
    }

    /**
     * 初始化商品视图
     */
    private void initProductView() {
        HeadZoomScrollView scrollView = (HeadZoomScrollView) findViewById(R.id.scrollView);
        tvProductName = (TextView) findViewById(R.id.tv_food_name);
        tvProductSale = (TextView) findViewById(R.id.tv_food_sale);
        tvProductPrice = (TextView) findViewById(R.id.tv_food_price);
        tvOldProductPrice = (TextView) findViewById(R.id.tv_old_product_price);
        tvProductGiftName = (TextView) findViewById(R.id.tv_product_gift_name);
        tvProductContent = (TextView) findViewById(R.id.tv_food_content);

        llProductAct = (LinearLayout) findViewById(R.id.ll_product_act);
        tvProductDiscount = (TextView) findViewById(R.id.tv_product_discount);
        tvProductLimit = (TextView) findViewById(R.id.tv_product_limit);

        layoutSpec1 = (RelativeLayout) findViewById(R.id.layout_detail_spec);
        tvSpec = (TextView) findViewById(R.id.tv_detail_spec);
        tvSpecNum = (TextView) findViewById(R.id.tv_detail_spec_num);
        //tvProductCount = (TextView) findViewById(R.id.tv_product_count);
        //ivSub = (ImageView) findViewById(R.id.iv_sub);
        ivGoodsHead = (ImageView) findViewById(R.id.iv_product_head);
        layoutSpec1.setOnClickListener(this);
    }


    private void initShopCar() {

        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
        rlPacking = (RelativeLayout) findViewById(R.id.rl_packing);
        tvPackingPrice = (TextView) findViewById(R.id.tv_packing_price);


        carRecView.setLayoutManager(new LinearLayoutManager(this));
        carRecView.setNestedScrollingEnabled(false);
//        carRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
//        carAdapter = new CarAdapter(carFoods, this);
        carAdapter = new SpecCarAdapter(carFoods, this, this);
//        carAdapter.bindToRecyclerView(carRecView);
        carRecView.setAdapter(carAdapter);
        blackView = findViewById(R.id.blackview);
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        behavior.setHideable(false);
        layoutShopCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetScrolling) {
                    return;
                }
                if (carAdapter.getItemCount() == 0) {
                    return;
                }
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

        });
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetScrolling = false;
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    blackView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                sheetScrolling = true;
                blackView.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(blackView, slideOffset);
            }
        });
    }

    private void initView() {

        BusinessFragment businessFragment = new BusinessFragment();
        CustomUtils.fragment = businessFragment;
        mFragments.add(businessFragment);
        mFragments.add(new EvaluateFragment());
        mFragments.add(new BusinessInfoFragment());


        layoutShopCar = (FrameLayout) findViewById(R.id.layout_shop_car);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        iv_shop_car = (ImageView) findViewById(R.id.iv_shop_car);
        car_badge = (TextView) findViewById(R.id.car_badge);
        business_title = (TextView) findViewById(R.id.business_title);
        car_limit = (TextView) findViewById(R.id.car_limit);
        tv_sale_price_bottom = (TextView) findViewById(R.id.tv_sale_price_bottom);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_old_amount = (TextView) findViewById(R.id.tv_old_amount);
        tv_old_amount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        layoutProductDetail = (RelativeLayout) findViewById(R.id.layout_product_detail);

        rootview = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.tb_collapsing_test);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
    }

    private void setListener() {

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1 || position == 2) {
                    layoutShopCar.setVisibility(View.GONE);
                } else {
                    layoutShopCar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    business_title.setText("");
                    //展开状态
                    ivBack.setImageResource(R.drawable.icon_back_map);
                    ivBack.setAlpha(1f);
                } else if (state == State.COLLAPSED) {
                    business_title.setText(tvBusinessName.getText().toString());
                    ivBack.setImageResource(R.drawable.icon_back_map);
                    //折叠状态
                    ivBack.setAlpha(1f);
                } else {
                    business_title.setText("");
                    //中间状态
                    Log.d("STATE", "verticalOffset =" + verticalOffset);
                    ivBack.setImageResource(R.drawable.icon_back_map);
                    ivBack.setAlpha(0.5f);
                }
            }
        });


        llContainAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessActList != null && businessActList.size() > 2) {
                    boolean showStatus = false;
                    for (int i = 0; i < businessActList.size(); i++) {
                        llContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                        if (i > 1) {
                            if ((boolean) llContainAct.getTag()) {
                                llContainAct.getChildAt(i).setVisibility(View.GONE);
                                showStatus = false;
                            } else {
                                llContainAct.getChildAt(i).setVisibility(View.VISIBLE);
                                showStatus = true;
                            }
                        }
                    }
                    llContainAct.setTag(showStatus);
                }
            }
        });

    }

    private void setData() {
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_orange_select));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_text_gray_two), getResources().getColor(R.color.color_orange_select));
    }

    private void initData() {
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);

        int flags = getIntent().getIntExtra(IntentFlag.KEY, -1);
        if (flags == 0) {
            TopImage business = (TopImage) getIntent().getSerializableExtra("business");
            if (business != null) {
                String[] split = business.getLinkAddr().split("=");
                businessId = Integer.parseInt(split[1]);
//                getBusinessDetailFromBanner(businessId);
            }
        } else if (flags == 1) {
            BusinessInfo business = (BusinessInfo) getIntent().getSerializableExtra("business");
            if (business != null) {
                businessId = business.id;
//                getBusiness(business.id);
//                setTitleUi(business);
            }
        } else if (flags == 2) {
            businessId = getIntent().getIntExtra("orderInfo", 0);
//            getBusinessDetailFromOrder(businessId);
        } else if (flags == 3) {
            businessId = getIntent().getIntExtra("search", 0);
//            getBusinessDetailFromOrder(businessId);
        } else if (flags == 4) {
            TopImage1 business = getIntent().getParcelableExtra("business");
            if (business != null) {
                String[] split = business.getLinkAddr().split("=");
                businessId = Integer.parseInt(split[1]);
//                getBusinessDetailFromBanner(businessId);
            }
        } else if (flags == 5) {
            businessId = getIntent().getIntExtra("businessId", 0);
            goodId = getIntent().getIntExtra("goodId", 0);
        }

        if (businessId != 0) {
            getBusinessDetailFromBanner(businessId);
            getBusinessActivity(businessId);
        }
        setTitle();

        mAdapter = new BusinessAdapter(getSupportFragmentManager(), mFragments, mStringList);
    }

    private void getBusinessActivity(int businessId) {
        CustomApplication.getRetrofit().getBusinessActivity(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String rows = jsonObject.optJSONArray("rows").toString();
                    businessActList = GsonUtil.fromJson(rows, new TypeToken<ArrayList<BusinessAct>>() {
                    }.getType());

                    showBusinessAct(businessActList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void showBusinessAct(ArrayList<BusinessAct> businessActList) {
        llContainAct.removeAllViews();
        llContainAct.setTag(false);
        if (businessActList != null && businessActList.size() > 0) {
//            if (businessActList.size() == 0) {
//                layoutActivity.setVisibility(View.GONE);
//            }
            for (int i = 0; i < businessActList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_business_act, null);
                ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                tvAct.setText(businessActList.get(i).showname);
                showActImage(ivAct, businessActList.get(i));
                if (i == 0) {
                    TextView tvActAll = (TextView) view.findViewById(R.id.tv_act_all);
                    tvActAll.setText(businessActList.size() + "个活动");
                    tvActAll.setVisibility(View.VISIBLE);
                }

                if (i > 1) {
                    view.setVisibility(View.GONE);
                }
                llContainAct.addView(view);
            }
        }
    }

    private void showActImage(ImageView ivAct, BusinessAct businessAct) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
        switch (businessAct.ptype) {
            case 1:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
            case 2:
                ivAct.setImageResource(R.drawable.icon_fracture);
                break;
            case 3:
                ivAct.setImageResource(R.drawable.icon_give);
                break;
            case 4:
                ivAct.setImageResource(R.drawable.icon_special);
                break;
            case 5:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
            case 6:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
        }
    }

    /**
     * 获取商家信息
     *
     * @param id
     */
    private void getBusinessDetailFromBanner(int id) {
        CustomApplication.getRetrofit().getBusinessInfo(id, lon, lat, 1).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                getBusiness(businessId);
                String data = response.body();
                dealBusinessDetailFromBanner(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealBusinessDetailFromBanner(String data) {
        BusinessDetails businessDetails = GsonUtil.parseJsonWithGson(data, BusinessDetails.class);
        business = businessDetails.getBusiness();
//        BusinessInfo info = new BusinessInfo();
//        info.name = business.getName();
//        info.content = business.getContent();
//        info.imgPath = business.getImgPath();
//        info.mini_imgPath = business.getMini_imgPath();
//        info.startPay = (double) business.getStartPay();
//        info.baseCharge = (double) business.getBaseCharge();
//        info.speed = String.valueOf(business.getSpeed());
//        info.salesnum = business.getSalesnum() == null ? 0 : business.getSalesnum();

        if (business.getIsopen() != 0) {
            showWarnDialog();
        }

        tvBusinessName.setText(business.getName());
        llNotice.setVisibility(!TextUtils.isEmpty(business.getContent()) ? View.VISIBLE : View.GONE);
        tvNotice.setText(business.getContent());
        x.image().bind(ivBusinessLogo, UrlConstant.ImageBaseUrl + business.getMini_imgPath(), NetConfig.optionsPagerCache);
        if (!TextUtils.isEmpty(business.getImgPath())) {
            x.image().bind(ivTitleBg, UrlConstant.ImageBaseUrl + business.getImgPath(), NetConfig.optionsPagerCache);
        }
        startPay = Double.valueOf(business.getStartPay());
        tvSaleStartPay.setText(startPay.isNaN() ? "¥ 0元起送" : "¥ " + String.valueOf(business.getStartPay()) + "起送");
        car_limit.setText(startPay.isNaN() ? "¥ 0元起送" : "¥ " + String.valueOf(business.getStartPay()) + "起送");
        if (business.getIsDeliver() == 0) {
            salePrice = business.getBusshowps();
            tvSalePrice.setText(Double.valueOf(business.getShowps()).isNaN() ? "配送费¥0" : "配送费¥" + business.getShowps());
//            tv_sale_price_bottom.setText(Double.valueOf(business.getCharge()).isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(business.getCharge()));
            tv_sale_price_bottom.setText("配送费¥" + business.getShowps());
            tvSaleTime.setText("快车专送·约" + business.getSpeed() + "分钟");
        } else {
            salePrice = business.getBusshowps();
            tvSaleTime.setText("商家配送·约" + business.getSpeed() + "分钟");
            tvSalePrice.setText(Double.valueOf(business.getShowps()).isNaN() ? "配送费¥0" : "配送费¥" + business.getShowps());
//            tv_sale_price_bottom.setText(Double.valueOf(business.getBusshowps()).isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(business.getBusshowps()));
            tv_sale_price_bottom.setText("配送费¥" + business.getShowps());
        }
        if (business.getSalesnum() == null) {
            business.setSalesnum(0);
        }
        tvSalesNum.setText("月售" + business.getSalesnum() + "单");


        mIvCollection.setImageResource(businessDetails.isEnshrine ? R.drawable.icon_collection_yes : R.drawable.icon_collection_no);
//        setTitleUi(info);
    }

    private void showWarnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("商家已打烊，请去别家购买哦");
        tv.setTextColor(getResources().getColor(R.color.text_666666));
        tv.setTextSize(14);
        tv.setPadding(ViewUtils.dip2px(this, 16), ViewUtils.dip2px(this, 16), 0, 0);
        alertDialog = builder
                .setCustomTitle(tv)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).show();

    }

//    private void getBusinessDetailFromOrder(int id) {
//        LogUtil.e("订单", id + "");
//        CustomApplication.getRetrofit().getBusinessInfo(id).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                String data = response.body();
//                dealBusinessDetailFromBanner(data);
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    public int getBusinessId() {
        return businessId;
    }

    public BusinessDetail getBusiness() {
        return business;
    }

    /**
     * 设置ui
     *
     * @param businessInfo
     */
    private void setTitleUi(BusinessInfo businessInfo) {
        tvBusinessName.setText(businessInfo.name);
        llNotice.setVisibility(!TextUtils.isEmpty(businessInfo.content) ? View.VISIBLE : View.GONE);
        tvNotice.setText(businessInfo.content);
        x.image().bind(ivBusinessLogo, UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath, NetConfig.optionsPagerCache);
        if (!TextUtils.isEmpty(businessInfo.imgPath)) {
            x.image().bind(ivTitleBg, UrlConstant.ImageBaseUrl + businessInfo.imgPath, NetConfig.optionsPagerCache);
        }
        startPay = businessInfo.startPay;
        tvSaleStartPay.setText(businessInfo.startPay.isNaN() ? "¥ 0元起送" : "¥ " + String.valueOf(businessInfo.startPay) + "起送");
        car_limit.setText(businessInfo.startPay.isNaN() ? "¥ 0元起送" : "¥ " + String.valueOf(businessInfo.startPay) + "起送");
        if (businessInfo.isDeliver == 0) {
            salePrice = businessInfo.busshowps;
            tvSalePrice.setText(businessInfo.baseCharge.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.baseCharge));
            tv_sale_price_bottom.setText(businessInfo.baseCharge.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.baseCharge));
            tvSaleTime.setText("快车专送·约" + businessInfo.speed + "分钟");
        } else {
            salePrice = businessInfo.busshowps;
            tvSaleTime.setText("商家配送·约" + businessInfo.speed + "分钟");
            tvSalePrice.setText(businessInfo.busshowps.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.busshowps));
            tv_sale_price_bottom.setText(businessInfo.busshowps.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfo.busshowps));
        }

        tvSalesNum.setText("月售" + String.valueOf(businessInfo.salesnum) + "单");
    }


    /**
     * 获取商品列表
     */
    private void getBusiness(int id) {
        CustomApplication.getRetrofit().getBusinessGoods(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealBusiness(data);
                if (userInfo != null) {
                    requestShoppingCart(businessId);
                } else {
                    car_badge.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealBusiness(String data) {
        try {
            foodBeens.clear();
            types.clear();
            int adPosition = 0;
            JSONObject object = new JSONObject(data);
            packing = object.optString("packing");
            if (TextUtils.isEmpty(packing) || TextUtils.equals("null", packing)) {
                packing = "0";
            }
            BusinessFragment fragment = (BusinessFragment) mFragments.get(0);
            JSONArray gtlist = object.getJSONArray("gtlist");
            if (gtlist == null || gtlist.length() <= 0) {
                return;
            }
            int length = gtlist.length();
            for (int i = 0; i < length; i++) {
                JSONObject listObject = gtlist.getJSONObject(i);
                TypeBean typeBean = new TypeBean();
                typeBean.setName(listObject.optString("name"));
                types.add(typeBean);
                fragment.getTypeBeanList().add(typeBean);

                JSONArray glistArray = listObject.optJSONArray("glist");
                if (glistArray != null) {
                    int length1 = glistArray.length();
                    for (int j = 0; j < length1; j++) {
                        JSONObject jsonObject = glistArray.getJSONObject(j);
                        FoodBean foodBean = new FoodBean();
                        foodBean.setSelectCount(0);
                        if (goodId != 0 && goodId == jsonObject.optInt("id")) {
                            positionSpec = adPosition;
                        } else {
                            adPosition++;
                        }
                        foodBean.setId(jsonObject.optInt("id"));
                        foodBean.setName(jsonObject.optString("name"));
                        foodBean.setIcon(jsonObject.optString("imgPath"));
//                        foodBean.setType(jsonObject.optString("sellTypeName"));
                        foodBean.setType(listObject.optString("name"));
                        foodBean.setPtype(jsonObject.optInt("ptype"));
                        foodBean.setShowprice(jsonObject.optString("showprice"));
                        foodBean.setIsonly(jsonObject.optInt("isonly"));
                        foodBean.setPrice(new BigDecimal(TextUtils.equals("null", jsonObject.optString("price")) ? "0" : jsonObject.optString("price")));
                        foodBean.setDisprice(foodBean.getPrice());
                        foodBean.setSalesnum(jsonObject.optInt("salesnum"));
                        foodBean.setBusinessId(jsonObject.optInt("businessId"));
                        foodBean.setBusinessName(jsonObject.optString("businessName"));
                        foodBean.setAgentId(jsonObject.optInt("agentId"));
                        foodBean.setContent(jsonObject.optString("content"));
                        foodBean.setLimittype(jsonObject.optInt("limittype"));
                        foodBean.setIslimited(jsonObject.optInt("islimited"));
                        foodBean.setLimitNum(jsonObject.optInt("limitNum"));
                        foodBean.setNum(jsonObject.optInt("num"));
                        foodBean.setShowzs(jsonObject.optString("showzs"));
                        foodBeens.add(foodBean);
                        fragment.getFoodBeanList().add(foodBean);
                    }
                }
            }
            if (fragment.getFoodAdapter() != null) {
                fragment.getFoodAdapter().setNewData(foodBeens);
                if (goodId != 0 && positionSpec != null) {
                    showGoodDetail();
                }
            }
            if (fragment.getTypeAdapter() != null) {
                fragment.getTypeAdapter().setNewData(types);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestShoppingCart(int businessId) {
        CustomApplication.getRetrofit().getShoppings(businessId).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }

                String body = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (!jsonObject.optBoolean("sucess")) {
                        ToastUtil.showToast(jsonObject.optString("message"));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!body.contains("{\"relogin\":1}")) {
                    carFoods.clear();
                    ShoppingCartInfo shoppingCartInfo = GsonUtil.fromJson(response.body(), ShoppingCartInfo.class);
                    if (shoppingCartInfo.shoppings != null && shoppingCartInfo.shoppings.size() > 0) {
                        fillShoppingCartView(shoppingCartInfo);
                        car_badge.setVisibility(View.VISIBLE);
                        int totalNum = 0;
                        for (int i = 0; i < shoppingCartInfo.shoppings.size(); i++) {
                            totalNum += Integer.valueOf(shoppingCartInfo.shoppings.get(i).num);
                        }
                        car_badge.setText(totalNum + "");
                    } else {
                        updateAmount(BigDecimal.valueOf(Double.valueOf(shoppingCartInfo.totalprice)));
                        updateTyprSelect();
                        ((BusinessFragment) mFragments.get(0)).getTypeAdapter().notifyDataSetChanged();
                        carAdapter.notifyDataSetChanged();
                        car_badge.setVisibility(View.INVISIBLE);
                    }

                }
                if (goodId != 0 && positionSpec != null) {
                    showGoodDetail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtil.showToast("网络数据异常");
            }
        });
    }

    private void fillShoppingCartView(ShoppingCartInfo shoppingCartInfo) {
        for (int i = 0; i < foodBeens.size(); i++) {
            int spectotalNum = 0;
            for (int j = 0; j < shoppingCartInfo.shoppings.size(); j++) {
                if (foodBeens.get(i).getId() == shoppingCartInfo.shoppings.get(j).goodsSellId) {
                    if (foodBeens.get(i).getIsonly() == 1) {
                        spectotalNum += Integer.valueOf(shoppingCartInfo.shoppings.get(j).num);
//                        foodBeens.get(i).setGoodsSpecId(shoppingCartInfo.shoppings.get(j).goodsSellStandardId);
//                        foodBeens.get(i).setGoodsSpec(shoppingCartInfo.shoppings.get(j).goodsSellStandardName);
//                        foodBeens.get(i).setGoodsSellOptionId(shoppingCartInfo.shoppings.get(j).goodsSellOptionId);
//                        foodBeens.get(i).setGoodsSellOptionName(shoppingCartInfo.shoppings.get(j).goodsSellOptionName);
                        FoodBean foodSpecBean = new FoodBean();
                        foodSpecBean.setSelectCount(Integer.valueOf(shoppingCartInfo.shoppings.get(j).num));
                        foodSpecBean.setId(foodBeens.get(i).getId());
                        foodSpecBean.setName(foodBeens.get(i).getName());
                        foodSpecBean.setSalesnum(foodBeens.get(i).getSalesnum());
                        foodSpecBean.setIsCommand(foodBeens.get(i).getIsCommand());
                        foodSpecBean.setPrice(new BigDecimal(shoppingCartInfo.shoppings.get(j).price));
                        foodSpecBean.setDisprice(shoppingCartInfo.shoppings.get(j).disprice != null ? shoppingCartInfo.shoppings.get(j).disprice : BigDecimal.ZERO);
//                        foodSpecBean.setDisprice(foodBeens.get(j).getDisprice() != null ? foodBeens.get(j).getDisprice() : "0");
                        foodSpecBean.setIcon(foodBeens.get(i).getIcon());
                        foodSpecBean.setCut(foodBeens.get(i).getCut());
                        foodSpecBean.setType(foodBeens.get(i).getType());
                        foodSpecBean.setPtype(foodBeens.get(i).getPtype());
                        foodSpecBean.setIsonly(foodBeens.get(i).getIsonly());
                        foodSpecBean.setNum(foodBeens.get(i).getNum());
                        foodSpecBean.setLimittype(foodBeens.get(i).getLimittype());
                        foodSpecBean.setIslimited(foodBeens.get(i).getIslimited());
                        foodSpecBean.setLimitNum(foodBeens.get(i).getLimitNum());
                        foodSpecBean.setShowprice(foodBeens.get(i).getShowprice());
                        foodSpecBean.setGoodsSpecId(shoppingCartInfo.shoppings.get(j).goodsSellStandardId);
                        foodSpecBean.setGoodsSpec(shoppingCartInfo.shoppings.get(j).goodsSellStandardName);
                        foodSpecBean.setGoodsSellOptionName(shoppingCartInfo.shoppings.get(j).goodsSellOptionName);
//                        foodSpecBean.setGoodsTypeTwo(shoppingCartInfo.shoppings.get(j).);
                        if (!TextUtils.isEmpty(shoppingCartInfo.shoppings.get(j).optionIds) && shoppingCartInfo.shoppings.get(j).optionIds != null) {
                            if (shoppingCartInfo.shoppings.get(j).optionIds.contains(",")) {
                                String[] split = shoppingCartInfo.shoppings.get(j).optionIds.split(",");
                                foodSpecBean.setGoodsSellOptionId(split[0]);
                                foodSpecBean.setOptionIds(split[1]);
                            } else {
                                foodSpecBean.setGoodsSellOptionId(shoppingCartInfo.shoppings.get(j).optionIds);
                                foodSpecBean.setOptionIds("");
                            }
                        } else {
                            foodSpecBean.setGoodsSellOptionId(shoppingCartInfo.shoppings.get(j).goodsSellOptionId);
                            foodSpecBean.setOptionIds(shoppingCartInfo.shoppings.get(j).optionIds);
                        }
                        carFoods.add(foodSpecBean);
                    } else {
                        foodBeens.get(i).setSelectCount(Integer.valueOf(shoppingCartInfo.shoppings.get(j).num));
                        carFoods.add(foodBeens.get(i));
                    }
                }
            }
            if (foodBeens.get(i).getIsonly() == 1) {
                foodBeens.get(i).setSelectCount(spectotalNum);
            }

        }

        updateAmount(BigDecimal.valueOf(Double.valueOf(shoppingCartInfo.totalprice)));
        updateTyprSelect();
        ((BusinessFragment) mFragments.get(0)).getFoodAdapter().notifyDataSetChanged();
        ((BusinessFragment) mFragments.get(0)).getTypeAdapter().notifyDataSetChanged();
        carAdapter.notifyDataSetChanged();
    }

    private void updateTyprSelect() {
        HashMap<String, Integer> typeSelect = new HashMap<>();
        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean fb = carFoods.get(i);
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
        }
        ((BusinessFragment) mFragments.get(0)).getTypeAdapter().updateBadge(typeSelect);
    }

    private void updatePackingPrice() {
        rlPacking.setVisibility(carFoods.size() > 0 ? View.VISIBLE : View.GONE);
        int packNum = 0;
        if (carFoods.size() > 0) {
            for (int i = 0; i < carFoods.size(); i++) {
                if (carFoods.get(i).getPtype() != 1) {
                    packNum += carFoods.get(i).getSelectCount();
                }
            }
            totalPacking = new BigDecimal(String.valueOf(packNum)).multiply(new BigDecimal(packing));
            rlPacking.setVisibility(totalPacking.compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
            tvPackingPrice.setText("¥" + totalPacking);
        } else {
            totalPacking = new BigDecimal(0.0);
        }
    }

    /**
     * 获取商家收藏信息
     */
    private void getBusinessCollection() {
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().getIsShoucang(businessId, 1, userInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealBusinessCollection(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealBusinessCollection(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String isShoucang = jsonObject.optString("isShoucang");
            if (TextUtils.equals(isShoucang, "0")) {
                mIvCollection.setImageResource(R.drawable.icon_collection_no);
            } else {
                mIvCollection.setImageResource(R.drawable.icon_collection_yes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        mStringList.add(getResources().getString(R.string.text_commodity));
        mStringList.add(getResources().getString(R.string.text_evaluate));
        mStringList.add(getResources().getString(R.string.text_business));
    }

    @OnClick({R.id.iv_back, R.id.iv_product_back, R.id.iv_share, R.id.iv_collection, R.id.layout_business_info, R.id.car_limit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (userInfo != null) {
                    if (carFoods.size() == 0) {
                        requestCleanShoppingCart();
                    } else {
                        saveOrderCar(false);
                    }
                } else {
                    finish();
                }
                break;
            case R.id.iv_product_back:
                layoutProductDetail.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_share:
                SharedView sharedView = new SharedView(this);

                sharedView.setSharedDataSource(
                        new ShareViewDataSource(
//                                getApplication().getCurrentSelectedShop().shop_name,
                                "aksjdkasjd",
//                                getH5ShopShareUrl(),
                                "http://www.baidu.com",
//                                getApplication().getCurrentSelectedShop().desc,
                                "23231231232",
//                                getH5ShopLogoUrl(),// 微信分享 shareImageUrl不能为空
                                "http://www.baidu.com",// 微信分享 shareImageUrl不能为空
//                                getH5ShopShareUrl(),
                                "http://www.baidu.com",
                                SharedView.SHARE_TEXT_IMAGE));
                sharedView.setSharedStateListen(new SharedView.SharedStateListen() {
                    @Override
                    public void shareState(int state) {

                    }
                });

                sharedView.show();
                break;
            case R.id.iv_collection:
                saveFavorite();
                break;
            case R.id.layout_business_info:

                break;
            case R.id.car_limit:
                if (userInfo == null) {
                    Intent intent = new Intent(this, LoginQucikActivity.class);
                    intent.putExtra("isRelogin", true);
                    startActivity(intent);
                    return;
                }
                saveOrderCar(true);
                break;
        }
    }

    /**
     * 喜爱的商家
     */
    private void saveFavorite() {
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofit().postSaveShop(businessId, 1).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealSaveShop(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealSaveShop(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optString("succ").equals("收藏成功！")) {
                mIvCollection.setImageResource(R.drawable.icon_collection_yes);
            } else if (jsonObject.optString("succ").equals("已取消收藏！")) {
                mIvCollection.setImageResource(R.drawable.icon_collection_no);
            }
            ToastUtil.showToast(jsonObject.getString("succ"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = UserService.getUserInfo(this);
        if (businessId != 0 && isResume) {
            getBusinessDetailFromBanner(businessId);
//            getBusiness(businessId);
            getBusinessActivity(businessId);
        }
    }

    /**
     * 购物车
     */
    private void saveOrderCar(final boolean isToPay) {
        List<ShoppingTrolley> trolleys = new ArrayList<>();
        for (int i = 0; i < carFoods.size(); i++) {
            mFoodBean = carFoods.get(i);
            ShoppingTrolley trolley = new ShoppingTrolley();
            trolley.setGoodsSellId(mFoodBean.getId());
            trolley.setGoodsSellStandardId(mFoodBean.getGoodsSpecId());
//            trolley.setGoodsSellOptionId(mFoodBean.getGoodsSellOptionId());
//            trolley.setOptionIds(mFoodBean.getOptionIds());
//            if (carFoods.get(i).getStandardList() != null && carFoods.get(i).getStandardList().size() > 0) {
//                trolley.setGoodsSellStandardId(mFoodBean.getGoodsSpecId());
//            }
            String optionId = "";
            if (!TextUtils.isEmpty(mFoodBean.getGoodsSellOptionId()) && mFoodBean.getGoodsSellOptionId() != null && !TextUtils.equals("0", mFoodBean.getGoodsSellOptionId())) {
                optionId = mFoodBean.getGoodsSellOptionId();
            }
            if (!TextUtils.isEmpty(mFoodBean.getOptionIds()) && mFoodBean.getOptionIds() != null) {
                optionId = optionId + "," + mFoodBean.getOptionIds();
            }
            trolley.setOptionIds(optionId);
            //TODO 子选项
//            trolley.setOptionIds(carFoods.get(i).get);
            trolley.setNum((int) mFoodBean.getSelectCount());
            trolleys.add(trolley);
        }

        ShoppingCarBean carBean = new ShoppingCarBean();
        carBean.setBusinessId(businessId);
//        carBean.setBusinessId(1);
        carBean.setCid(userInfo.id);
        carBean.setCname(userInfo.name);
        carBean.setList(trolleys);

        String goodJson = new Gson().toJson(carBean);
        LogUtil.e("购物车", goodJson);
        CustomApplication.getRetrofit().OrderCar(goodJson).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }
                String body = response.body().toString();
                if (body.contains("{\"relogin\":1}") || !isToPay) {
                    finish();
                } else {
                    String data = response.body();
                    dealOrderCart(data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isToPay) {
                    finish();
                }
            }

        });
    }

    private void dealOrderCart(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if ((jsonObject.optBoolean("success"))) {
                List<FoodBean> flist = carFoods;
                isResume = true;
                startActivity(new Intent(this, ConfirmOrderActivity.class)
                        .putExtra("foodBean", (Serializable) flist)
                        .putExtra("price", decimal)
                        .putExtra("salePrice", salePrice)
                        .putExtra("businessId", businessId));
            } else {
                ToastUtil.showToast(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(mTabLayout, 30, 30);
            }
        });
    }

    public void toggleCar(View view) {
        if (sheetScrolling) {
            return;
        }
        if (carAdapter.getItemCount() == 0) {
            return;
        }
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    /**
     * 处理购物车
     *
     * @param foodBean
     */
    private void dealCar(FoodBean foodBean) {
        HashMap<String, Integer> typeSelect = new HashMap<>();
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            ((BusinessFragment) mFragments.get(0)).getFoodAdapter().notifyDataSetChanged();
        }
        //List<FoodBean> flist = carFoods;
        int p = -1;
        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean fb = carFoods.get(i);
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            if (fb.getId() == foodBean.getId()) {
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else {
                    fb.setSelectCount(foodBean.getSelectCount());
                }
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
            total += fb.getSelectCount();
        }
        if (p >= 0) {
            carFoods.remove(p);
        } else if (!hasFood) {
            carFoods.add(foodBean);
            if (typeSelect.containsKey(foodBean.getType())) {
                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
            } else {
                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
            }
            amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            total += foodBean.getSelectCount();
        }
        carAdapter.notifyDataSetChanged();
        if (total > 0) {
            car_badge.setVisibility(View.VISIBLE);
            car_badge.setText(total + "");
        } else {
            car_badge.setVisibility(View.INVISIBLE);
        }
        ((BusinessFragment) mFragments.get(0)).getTypeAdapter().updateBadge(typeSelect);
        updateAmount(amount);
    }

    private void updateAmount(BigDecimal amount) {
        updatePackingPrice();
        LogUtils.d(carFoods.toString());
        BigDecimal totalPrice = new BigDecimal(0.0);
        BigDecimal payPrice = new BigDecimal(0.0);

        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean foodBean = carFoods.get(i);
            BigDecimal foodPayPrice = new BigDecimal(0.0);
            BigDecimal foodTotalPrice = new BigDecimal(0.0);
            if (foodBean.getIslimited() == 0) {
                if (foodBean.getDisprice() != null && foodBean.getDisprice().compareTo(BigDecimal.ZERO) != 0) {
                    foodPayPrice = foodBean.getDisprice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                    foodTotalPrice = foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                } else {
                    foodTotalPrice = foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                    foodPayPrice = foodTotalPrice;
                }
            } else {
                if (foodBean.getDisprice() != null && foodBean.getDisprice().compareTo(BigDecimal.ZERO) != 0) {
                    if (foodBean.getSelectCount() <= foodBean.getLimitNum()) {
                        foodPayPrice = foodBean.getDisprice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                    } else {
                        foodPayPrice = foodBean.getDisprice().multiply(BigDecimal.valueOf(foodBean.getLimitNum())).add(
                                foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount() - foodBean.getLimitNum())));
                    }
                    foodTotalPrice = foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                } else {
                    foodTotalPrice = foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount()));
                    foodPayPrice = foodTotalPrice;
                }
            }
            payPrice = payPrice.add(foodPayPrice);
            totalPrice = totalPrice.add(foodTotalPrice);
        }

        payPrice = payPrice.add(totalPacking);
        totalPrice = totalPrice.add(totalPacking);

        amount = payPrice;
//        amount = totalPrice;

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            car_limit.setText(startPay.isNaN() ? "¥ 0元起送" : "¥ " + String.valueOf(startPay) + "起送");
            car_limit.setTextColor(Color.parseColor("#a8a8a8"));
            car_limit.setBackgroundColor(Color.parseColor("#99464646"));
            findViewById(R.id.car_nonselect).setVisibility(View.VISIBLE);
            findViewById(R.id.amount_container).setVisibility(View.GONE);
            iv_shop_car.setImageResource(R.drawable.icon_not_shop_car);
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            car_limit.setEnabled(false);
        } else if (amount.compareTo(BigDecimal.valueOf(startPay)) < 0) {
            car_limit.setText("还差 ¥" + (BigDecimal.valueOf(startPay).subtract(amount)) + " 起送");
            car_limit.setTextColor(Color.parseColor("#a8a8a8"));
            car_limit.setBackgroundColor(Color.parseColor("#99464646"));
            findViewById(R.id.car_nonselect).setVisibility(View.GONE);
            findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
            iv_shop_car.setImageResource(R.drawable.icon_not_shop_car);
            car_limit.setEnabled(false);
        } else {
            car_limit.setText("去结算");
            car_limit.setTextColor(Color.WHITE);
            car_limit.setBackgroundColor(Color.parseColor("#ff9f14"));
            findViewById(R.id.car_nonselect).setVisibility(View.GONE);
            findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
            iv_shop_car.setImageResource(R.drawable.icon_shop_car);
            car_limit.setEnabled(true);
        }
        tv_amount.setText("¥" + payPrice);
        tv_old_amount.setText("¥" + totalPrice);
        tv_old_amount.setVisibility(payPrice.compareTo(totalPrice) == 0 ? View.GONE : View.VISIBLE);
        decimal = payPrice;
    }

    public void clearCar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("清空购物车?");
        tv.setTextSize(14);
        tv.setPadding(ViewUtils.dip2px(this, 16), ViewUtils.dip2px(this, 16), 0, 0);
        tv.setTextColor(Color.parseColor("#757575"));
        AlertDialog alertDialog = builder
                .setNegativeButton("取消", null)
                .setCustomTitle(tv)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BusinessFragment fragment = (BusinessFragment) mFragments.get(0);
                        for (int i = 0; i < carFoods.size(); i++) {
                            FoodBean fb = carFoods.get(i);
                            fb.setSelectCount(0);
                            for (int j = 0; j < foodBeens.size(); j++) {
                                if (fb.getId() == foodBeens.get(j).getId()) {
                                    foodBeens.get(j).setSelectCount(0);
                                    fragment.getFoodAdapter().notifyItemChanged(j, 0);
                                }
                            }
                        }
                        carFoods.clear();
                        if (positionSpec != null) {
//                            addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), positionSpec, BusinessActivity.this);
                        }
                        tvSpecNum.setText("0");
                        tvSpecNum.setVisibility(View.GONE);
                        car_badge.setVisibility(View.INVISIBLE);
                        fragment.getTypeAdapter().updateBadge(new HashMap<String, Integer>());
                        updateAmount(new BigDecimal(0.0));
                    }
                })
                .show();
        Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(ContextCompat.getColor(this, R.color.dodgerblue));
        nButton.setTypeface(Typeface.DEFAULT_BOLD);
        Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(ContextCompat.getColor(this, R.color.dodgerblue));
        pButton.setTypeface(Typeface.DEFAULT_BOLD);
    }


    public void goAccount(View view) {
    }

    /**
     * 添加商品
     *
     * @param view
     * @param fb
     */
    @Override
    public void onAddClick(View view, FoodBean fb) {
        dealCar(fb);
        int[] addLoc = new int[2];
        view.getLocationInWindow(addLoc);
        if (shopWidth == 0) {
            carLoc = new int[2];
            shopWidth = iv_shop_car.getWidth() / 2;
            iv_shop_car.getLocationInWindow(carLoc);
            carLoc[0] = carLoc[0] + shopWidth - view.getWidth() / 2;
        }
        Path path = new Path();
        path.moveTo(addLoc[0], addLoc[1]);
        path.quadTo(addLoc[0] - 500, addLoc[1] - 200, carLoc[0], carLoc[1]);

        final TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.circle_red);
        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(view.getWidth(), view.getHeight());
        rootview.addView(textView, lp);
        ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                rootview.removeView(textView);
            }
        }).start();
    }


    public void onAddClickSpec(View view, FoodBean fb, int position) {

        dealCarSpec(fb);
//        List<FoodBean> data = ((BusinessFragment) mFragments.get(0)).getFoodAdapter().getData();
//        data.get(position).setSelectCount(data.get(position).getSelectCount() + 1);
//        data.get(position).setSelectCount(fb.getSelectCount());
//        ((BusinessFragment) mFragments.get(0)).getFoodAdapter().setData(position, data.get(position));

//        tvSpecNum.setText(data.get(position).getSelectCount() + "");
//        tvSpecNum.setText(fb.getSelectCount() + "");
//        tvSpecNum.setVisibility(data.get(position).getSelectCount() > 0 ? View.VISIBLE : View.GONE);

        List<FoodBean> data = ((BusinessFragment) mFragments.get(0)).getFoodAdapter().getData();
        int specSelectNum = 0;
        for (int j = 0; j < carFoods.size(); j++) {
            if (fb.getId() == carFoods.get(j).getId()) {
                specSelectNum += carFoods.get(j).getSelectCount();
            }
        }


        for (int i = 0; i < data.size(); i++) {

            if (fb.getId() == data.get(i).getId()) {
//                    data.get(i).setSelectCount(data.get(i).getSelectCount() - 1);
                data.get(i).setSelectCount(specSelectNum);
                ((BusinessFragment) mFragments.get(0)).getFoodAdapter().setData(i, data.get(i));
                if (position == i) {
                    tvSpecNum.setText(specSelectNum + "");
                    tvSpecNum.setVisibility(specSelectNum > 0 ? View.VISIBLE : View.GONE);
                }
            }
        }

        int[] addLoc = new int[2];
        view.getLocationInWindow(addLoc);
        if (shopWidth == 0) {
            carLoc = new int[2];
            shopWidth = iv_shop_car.getWidth() / 2;
            iv_shop_car.getLocationInWindow(carLoc);
            carLoc[0] = carLoc[0] + shopWidth - view.getWidth() / 2;
        }

        Path path = new Path();
        path.moveTo(addLoc[0], addLoc[1]);
        path.quadTo(addLoc[0] - 500, addLoc[1] - 200, carLoc[0], carLoc[1]);

        final TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.circle_red);
        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(view.getWidth(), view.getHeight());
        rootview.addView(textView, lp);
        ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                rootview.removeView(textView);
            }
        }).start();
    }

    /**
     * 规格--"选好了"点击事件
     *
     * @param foodBean
     */
    private void dealCarSpec(FoodBean foodBean) {
        HashMap<String, Integer> typeSelect = new HashMap<>();
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        boolean isEquals = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            ((BusinessFragment) mFragments.get(0)).getFoodAdapter().notifyDataSetChanged();
        }
        //List<FoodBean> flist = carFoods;
        int p = -1;
        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean fb = carFoods.get(i);

            if (fb.getId() == foodBean.getId()) {
                hasFood = true;
                if (foodBean.getSelectCount() == 0 && fb.getPrice().compareTo(foodBean.getPrice()) == 0
                        && TextUtils.equals(fb.getGoodsSpec(), foodBean.getGoodsSpec())
                        && TextUtils.equals(fb.getGoodsSellOptionName(), foodBean.getGoodsSellOptionName())
                        && TextUtils.equals(fb.getGoodsTypeTwo(), foodBean.getGoodsTypeTwo())) {
                    p = i;
                } else {
                    Log.d("price", fb.getPrice() + " = fb");
                    Log.d("price", foodBean.getPrice() + " = foodBean");
                    Log.d("price", foodBean.getDiscount() + " = foodBean");

                    if (fb.getPrice().compareTo(foodBean.getPrice()) == 0
                            && TextUtils.equals(fb.getGoodsSpec(), foodBean.getGoodsSpec())
                            && TextUtils.equals(fb.getGoodsSellOptionName(), foodBean.getGoodsSellOptionName())
                            && TextUtils.equals(fb.getGoodsTypeTwo(), foodBean.getGoodsTypeTwo())) {
                        isEquals = true;
                        fb.setSelectCount(foodBean.getSelectCount());
                    } else if (!isEquals) {
                        if (i == carFoods.size() - 1) {
                            hasFood = false;
                        }
                    }
                }
            }
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
            total += fb.getSelectCount();
        }
        if (p >= 0) {
            carFoods.remove(p);
        } else if (!hasFood) {
            carFoods.add(foodBean);
            if (typeSelect.containsKey(foodBean.getType())) {
                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
            } else {
                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
            }
            amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            total += foodBean.getSelectCount();
        }

        if (total > 0) {
            car_badge.setVisibility(View.VISIBLE);
            car_badge.setText(total + "");
        } else {
            car_badge.setVisibility(View.INVISIBLE);
        }
        carAdapter.notifyDataSetChanged();
        ((BusinessFragment) mFragments.get(0)).getTypeAdapter().updateBadge(typeSelect);
        updateAmount(amount);
    }

    /**
     * 减少商品
     *
     * @param fb
     */
    @Override
    public void onSubClick(FoodBean fb) {
        dealCar(fb);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.food_main://商品item
                    positionSpec = (Integer) v.getTag();
                    Log.d("position", "position =" + positionSpec);
                    showGoodDetail();

                    break;
                case R.id.layout_spec://商品item
                    positionSpec = (Integer) v.getTag();
                    Log.d("position", "position =" + positionSpec);
                    FoodBean foodBeanSpec = foodBeens.get(positionSpec);
                    if (foodBeanSpec.getSelectCount() >= foodBeanSpec.getNum()) {
                        ToastUtil.showToast("库存不足");
                        return;
                    }

                    tvSpecTitle.setText(foodBeanSpec.getName());

                    if (foodBeanSpec.getDiscount() != null && foodBeanSpec.getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                        tvSpecPrice.setText(foodBeanSpec.getDiscount() + "");
                        tvSpecOldPrice.setText("¥ " + foodBeanSpec.getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.VISIBLE);
                    } else {
                        tvSpecPrice.setText(foodBeanSpec.getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.GONE);
                    }
//                    tvSpecPrice.setText(foodBeanSpec.getPrice() + "");
                    getGoodsSpec(foodBeanSpec.getId());
                    break;
                case R.id.layout_detail_spec://商品详情框
                    if (foodBeanDetail.getSelectCount() >= foodBeanDetail.getNum()) {
                        ToastUtil.showToast("库存不足");
                        return;
                    }
                    tvSpecTitle.setText(foodBeanDetail.getName());
                    if (foodBeanDetail.getDiscount() != null && foodBeanDetail.getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                        tvSpecPrice.setText(foodBeanDetail.getDiscount() + "");
                        tvSpecOldPrice.setText("¥ " + foodBeanDetail.getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.VISIBLE);
                    } else {
                        tvSpecPrice.setText(foodBeanDetail.getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.GONE);
                    }
//                    tvSpecPrice.setText(foodBeanDetail.getPrice() + "");
                    getGoodsSpec(foodBeanDetail.getId());
                    break;
                case R.id.tv_select_spec://规格框
                    boolean isCarFoodData = false;
                    FoodBean fbSpec = ((BusinessFragment) mFragments.get(0)).getFoodAdapter().getItem(positionSpec);
                    if (fbSpec != null) {
                        for (int i = 0; i < carFoods.size(); i++) {
                            if (fbSpec.getId() == carFoods.get(i).getId()
//                                    && carFoods.get(i).getPrice().equals(BigDecimal.valueOf(Double.parseDouble(tvSpecPrice.getText().toString().trim())))
                                    && TextUtils.equals(carFoods.get(i).getGoodsSpec(), specName)
                                    && TextUtils.equals(carFoods.get(i).getGoodsSellOptionName(), typeName)
                                    && TextUtils.equals(carFoods.get(i).getGoodsTypeTwo(), typeNameTwo)) {
                                fbSpec = carFoods.get(i);
                                isCarFoodData = true;
                            }
                        }
                        FoodBean foodBeanTwo = new FoodBean();
                        if (!isCarFoodData) {
                            foodBeanTwo.setSelectCount(specNum);
                        } else {
                            if (fbSpec.getIslimited() == 1) {
                                if (fbSpec.getLimittype() == 0) {
                                    if (fbSpec.getSelectCount() + specNum > fbSpec.getLimitNum()) {
                                        ToastUtil.showToast("已达到限购上限");
                                        return;
                                    } else {
                                        foodBeanTwo.setSelectCount(fbSpec.getSelectCount() + specNum);
                                    }
                                } else {
                                    if (fbSpec.getSelectCount() + specNum >= fbSpec.getLimitNum()) {
                                        ToastUtil.showToast("已超过优惠件数，将以原价购买");
                                    }
                                    foodBeanTwo.setSelectCount(fbSpec.getSelectCount() + specNum);
                                }
                            } else {
                                foodBeanTwo.setSelectCount(fbSpec.getSelectCount() + specNum);
                            }
                        }
                        foodBeanTwo.setId(fbSpec.getId());
                        foodBeanTwo.setName(fbSpec.getName());
                        foodBeanTwo.setSalesnum(fbSpec.getSalesnum());
                        foodBeanTwo.setIsCommand(fbSpec.getIsCommand());
                        foodBeanTwo.setPrice(specBeanList.get(point).getPrice());
                        foodBeanTwo.setDisprice(specBeanList.get(point).getDiscount() != null ? specBeanList.get(point).getDiscount() : BigDecimal.ZERO);
                        foodBeanTwo.setIcon(fbSpec.getIcon());
                        foodBeanTwo.setCut(fbSpec.getCut());
                        foodBeanTwo.setType(fbSpec.getType());
                        foodBeanTwo.setPtype(fbSpec.getPtype());
                        foodBeanTwo.setIsonly(fbSpec.getIsonly());
                        foodBeanTwo.setNum(fbSpec.getNum());
                        foodBeanTwo.setLimittype(fbSpec.getLimittype());
                        foodBeanTwo.setIslimited(fbSpec.getIslimited());
                        foodBeanTwo.setLimitNum(fbSpec.getLimitNum());
                        foodBeanTwo.setShowprice(fbSpec.getShowprice());
                        foodBeanTwo.setGoodsSpecId(specId);
                        foodBeanTwo.setGoodsSpec(specName);
                        foodBeanTwo.setGoodsSellOptionId(typeId);
                        foodBeanTwo.setGoodsSellOptionName(typeName);
                        foodBeanTwo.setGoodsTypeTwo(typeNameTwo);
                        foodBeanTwo.setOptionIds(optionIds);
                        onAddClickSpec(tvAdd, foodBeanTwo, positionSpec);
                    }
                    isShowDetail = false;
                    specDialog.dismiss();
                    break;
                case R.id.iv_close_spec:
                    isShowDetail = false;
                    specDialog.dismiss();
                    break;
                case R.id.iv_add_num:
                    specNum = Integer.valueOf(tvSpecCount.getText().toString());
                    specNum++;
                    tvSpecCount.setText(specNum + "");
                    break;

                case R.id.iv_sub_num:
                    specNum = Integer.valueOf(tvSpecCount.getText().toString());
                    if (specNum > 1) {
                        specNum--;
                    }
                    tvSpecCount.setText(specNum + "");
                    break;
            }
        }
    }

    private void showGoodDetail() {
        foodBeanDetail = foodBeens.get(positionSpec);
        //positionDetail = position;
        isShowDetail = true;
        if (foodBeanDetail.getIsonly() == 1) {
            layoutSpec1.setVisibility(View.VISIBLE);
            addWidgetDetail.setVisibility(View.GONE);
            tvSpecNum.setText(foodBeanDetail.getSelectCount() + "");
            tvSpecNum.setVisibility(foodBeanDetail.getSelectCount() > 0 ? View.VISIBLE : View.GONE);
        } else {
            layoutSpec1.setVisibility(View.GONE);
            addWidgetDetail.setVisibility(View.VISIBLE);
        }
//        if (((BusinessFragment) mFragments.get(0)).getFoodAdapter() != null) {
//        addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), positionSpec, this);
//        addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), positionSpec, this);
//        }
        layoutProductDetail.setVisibility(View.VISIBLE);
        rotateyAnimShow(layoutProductDetail);
        appBarLayout.setVisibility(View.GONE);

        if (business.getIsopen() != 0) {
            layoutSpec1.setVisibility(View.GONE);
            addWidgetDetail.setVisibility(View.GONE);
        }

        if (foodBeanDetail != null) {
            fillProductDetail(foodBeanDetail);
            if (foodBeanDetail.getSelectCount() > 0) {
                //ivSub.setVisibility(View.VISIBLE);
                //tvProductCount.setText(foodBean.getSelectCount() + "");
            }
            getGoodsDetail(foodBeanDetail.getId());
        }
    }

    private void fillProductDetail(FoodBean foodBeanDetail) {
        tvProductName.setText(foodBeanDetail.getName());
        tvProductSale.setText("月售" + foodBeanDetail.getSalesnum());
//        tvProductPrice.setText(foodBeanDetail.getPrice() + "");
        tvProductContent.setText(foodBeanDetail.getContent());

        if (foodBeanDetail.getIslimited() == 1 || (!TextUtils.isEmpty(foodBeanDetail.getShowprice()) && !TextUtils.equals("null", foodBeanDetail.getShowprice()))) {
            llProductAct.setVisibility(View.VISIBLE);

            if ((!TextUtils.isEmpty(foodBeanDetail.getShowprice()) && !TextUtils.equals("null", foodBeanDetail.getShowprice()))) {
                tvProductDiscount.setVisibility(View.VISIBLE);
                tvProductDiscount.setText(foodBeanDetail.getShowprice());
            } else {
                tvProductDiscount.setVisibility(View.GONE);
            }

            if (foodBeanDetail.getIslimited() == 1) {
                tvProductLimit.setVisibility(View.VISIBLE);
                tvProductLimit.setText("限购" + foodBeanDetail.getLimitNum() + "件");
            } else {
                tvProductLimit.setVisibility(View.GONE);
            }

        } else {
            llProductAct.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(foodBeanDetail.getShowzs()) && !TextUtils.equals("null", foodBeanDetail.getShowzs())) {
            tvProductGiftName.setVisibility(View.VISIBLE);
            tvProductGiftName.setText(foodBeanDetail.getShowzs());
        } else {
            tvProductGiftName.setVisibility(View.GONE);
        }

        if (foodBeanDetail.getDisprice() != null && foodBeanDetail.getDisprice().compareTo(BigDecimal.ZERO) != 0) {
            tvProductPrice.setText("¥" + foodBeanDetail.getDisprice());
            tvOldProductPrice.setText("¥" + foodBeanDetail.getPrice());
            tvOldProductPrice.setVisibility(View.VISIBLE);
        } else {
            tvProductPrice.setText("¥" + foodBeanDetail.getPrice());
            tvOldProductPrice.setVisibility(View.GONE);
        }

        tvOldProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }

    /**
     * 获取商品规格
     */
    private void getGoodsSpec(int id) {
        CustomApplication.getRetrofit().getGoodsSpec(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealGoodsSpec(data);
                specNum = 1;
                tvSpecCount.setText(specNum + "");
                specDialog.show();
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealGoodsSpec(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            optionBeenList.clear();
            specBeanList.clear();
            JSONObject goodsSell = jsonObject.getJSONObject("goods");
            JSONArray standardList = goodsSell.getJSONArray("standardList");
            JSONArray optionList = goodsSell.getJSONArray("optionList");
            int standarLength = standardList.length();
            int optionLength = optionList.length();
            if (standarLength > 0) {
                for (int i = 0; i < standarLength; i++) {
                    SpecBean specBean = GsonUtil.parseJsonWithGson(standardList.getJSONObject(i).toString(), SpecBean.class);
                    specBeanList.add(specBean);
                }
                addItem(specBeanList, layoutSpec);

                if (specBeanList.get(0).getDiscount() != null && specBeanList.get(0).getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                    tvSpecPrice.setText(specBeanList.get(0).getDiscount() + "");
                    tvSpecOldPrice.setText("¥ " + specBeanList.get(0).getPrice() + "");
                    tvSpecOldPrice.setVisibility(View.VISIBLE);
                } else {
                    tvSpecPrice.setText(specBeanList.get(0).getPrice() + "");
                    tvSpecOldPrice.setVisibility(View.GONE);
                }
            } else {
                layoutSpec.removeAllViews();
            }
            if (optionLength > 0) {
                for (int i = 0; i < optionLength; i++) {
                    JSONObject optionObject = optionList.getJSONObject(i);
                    OptionBean optionBean = new OptionBean();
                    optionBean.id = optionObject.optInt("id");
                    optionBean.name = optionObject.optString("name");
                    optionBean.subOption = new ArrayList<>();
                    JSONArray subOption = optionObject.getJSONArray("subOption");
                    int subLength = subOption.length();
                    if (subLength > 0) {
                        for (int j = 0; j < subLength; j++) {
                            JSONObject subObject = subOption.getJSONObject(j);
                            SubOptionBean subOptionBean = new SubOptionBean();
                            subOptionBean.id = subObject.optInt("id");
                            subOptionBean.name = subObject.optString("name");
                            subOptionBean.sort = subObject.optInt("sort");
                            optionBean.subOption.add(subOptionBean);
                        }
                    }
                    optionBeenList.add(optionBean);
                }
                tvSpecProperty.setText(optionBeenList.get(0).name + "：");
                addItemOption(optionBeenList.get(0).subOption, layoutProduct);
                layoutGoodOne.setVisibility(View.VISIBLE);
                if (optionLength > 1) {
                    layoutGoodTwo.setVisibility(View.VISIBLE);
                    tvSpecPropertyTwo.setText(optionBeenList.get(1).name + "：");
                    addItemOptionTwo(optionBeenList.get(1).subOption, layoutProductTwo);
                } else {
                    layoutGoodTwo.setVisibility(View.GONE);
                }
            } else {
                layoutGoodOne.setVisibility(View.GONE);
                layoutGoodTwo.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 规格选项
     *
     * @param data
     * @param flowLayout
     */
    @SuppressWarnings("ResourceType")
    public void addItem(final List<SpecBean> data, final ZFlowLayout flowLayout) {
        flowLayout.removeAllViews();
        final ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 15, 10);// 设置边距

        point = 0;

        for (int i = 0; i < data.size(); i++) {

            final TextView textView = new TextView(this);
            textView.setText(data.get(i).getName());
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.color_white));
                textView.setBackgroundResource(R.drawable.product_spec_background_select);
            } else {
                textView.setBackgroundResource(R.drawable.round_biankuang_333333);
                textView.setTextColor(getResources().getColor(R.color.color_text_gray));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            specName = data.get(0).getName();
            specId = String.valueOf(data.get(0).getId());
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    point = (int) v.getTag();
                    if (specBeanList.get(point).getDiscount() != null && specBeanList.get(point).getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                        tvSpecPrice.setText(specBeanList.get(point).getDiscount() + "");
                        tvSpecOldPrice.setText("¥ " + specBeanList.get(point).getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.VISIBLE);
                    } else {
                        tvSpecPrice.setText(specBeanList.get(point).getPrice() + "");
                        tvSpecOldPrice.setVisibility(View.GONE);
                    }
//                    tvSpecPrice.setText(specBeanList.get(point).getPrice() + "");
                    specName = data.get(point).getName();
                    specId = String.valueOf(data.get(point).getId());
                    for (int j = 0; j < flowLayout.getChildCount(); j++) {
                        if (j == point) {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_white));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.product_spec_background_select);
                        } else {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_text_gray));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.round_biankuang_333333);
                        }
                    }
                }
            });
            flowLayout.addView(textView, layoutParams);
        }
    }

    /**
     * 规格选项
     *
     * @param data
     * @param flowLayout
     */
    @SuppressWarnings("ResourceType")
    public void addItemOption(final List<SubOptionBean> data, final ZFlowLayout flowLayout) {
        flowLayout.removeAllViews();
        final ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 15, 10);// 设置边距
        point = 0;
        for (int i = 0; i < data.size(); i++) {

            final TextView textView = new TextView(this);
            textView.setText(data.get(i).name);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.color_white));
                textView.setBackgroundResource(R.drawable.product_spec_background_select);
            } else {
                textView.setBackgroundResource(R.drawable.round_biankuang_333333);
                textView.setTextColor(getResources().getColor(R.color.color_text_gray));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            typeName = data.get(0).name;
            typeId = String.valueOf(data.get(0).id);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int point = (int) v.getTag();
                    typeName = data.get(point).name;
                    typeId = String.valueOf(data.get(point).id);
                    for (int j = 0; j < flowLayout.getChildCount(); j++) {
                        if (j == point) {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_white));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.product_spec_background_select);
                        } else {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_text_gray));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.round_biankuang_333333);
                        }
                    }

                }
            });
            flowLayout.addView(textView, layoutParams);
        }
    }

    /**
     * 规格选项
     *
     * @param data
     * @param flowLayout
     */
    @SuppressWarnings("ResourceType")
    public void addItemOptionTwo(final List<SubOptionBean> data, final ZFlowLayout flowLayout) {
        flowLayout.removeAllViews();
        final ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 15, 10);// 设置边距

        for (int i = 0; i < data.size(); i++) {

            final TextView textView = new TextView(this);
            textView.setText(data.get(i).name);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.color_white));
                textView.setBackgroundResource(R.drawable.product_spec_background_select);
            } else {
                textView.setBackgroundResource(R.drawable.round_biankuang_333333);
                textView.setTextColor(getResources().getColor(R.color.color_text_gray));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            typeNameTwo = data.get(0).name;
            optionIds = String.valueOf(data.get(0).id);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int point = (int) v.getTag();
                    typeNameTwo = data.get(point).name;
                    optionIds = String.valueOf(data.get(point).id);
                    for (int j = 0; j < flowLayout.getChildCount(); j++) {
                        if (j == point) {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_white));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.product_spec_background_select);
                        } else {
                            ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.color_text_gray));
                            flowLayout.getChildAt(j).setBackgroundResource(R.drawable.round_biankuang_333333);
                        }
                    }
                }
            });
            flowLayout.addView(textView, layoutParams);
        }
    }

    /**
     * 动画显示商品详情
     *
     * @param view
     */
    public void rotateyAnimShow(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha",
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX",
                0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY",
                0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(500).start();
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetail(int id) {
        CustomApplication.getRetrofit().getGoodsDetail(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealGoodsDetail(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealGoodsDetail(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject goodsSell = jsonObject.getJSONObject("goodsSell");
            String imgPath = goodsSell.optString("imgPath");
            x.image().bind(ivGoodsHead, UrlConstant.ImageBaseUrl + imgPath, NetConfig.optionsPagerCache);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(View view, FoodBean foodBean, int position) {
        if (foodBean.getIsonly() == 1) {
            onAddClickSpec(view, foodBean, position);
        } else {
            onAddClick(view, foodBean);
            if (isShowDetail) {
//                addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), position, this);
            }
        }
    }

    @Override
    public void sub(FoodBean fb, int position) {
        dealCarSpec(fb);
        if (fb.getIsonly() == 1) {
//            List<FoodBean> data = ((BusinessFragment) mFragments.get(0)).getFoodAdapter().getData();
//            for (int i = 0; i < data.size(); i++) {
//                if (fb.getId() == data.get(i).getId()) {
////                    data.get(i).setSelectCount(data.get(i).getSelectCount() - 1);
//                    data.get(i).setSelectCount(fb.getSelectCount());
//                    ((BusinessFragment) mFragments.get(0)).getFoodAdapter().setData(i, data.get(i));
//                    if (position == i) {
//                        tvSpecNum.setText(fb.getSelectCount() + "");
//                        tvSpecNum.setVisibility(data.get(i).getSelectCount() > 0 ? View.VISIBLE : View.GONE);
//                    }
//                }
//            }
            List<FoodBean> data = ((BusinessFragment) mFragments.get(0)).getFoodAdapter().getData();
            int specSelectNum = 0;
            for (int j = 0; j < carFoods.size(); j++) {
                if (fb.getId() == carFoods.get(j).getId()) {
                    specSelectNum += carFoods.get(j).getSelectCount();
                }
            }

            for (int i = 0; i < data.size(); i++) {

                if (fb.getId() == data.get(i).getId()) {
//                    data.get(i).setSelectCount(data.get(i).getSelectCount() - 1);
                    data.get(i).setSelectCount(specSelectNum);
                    ((BusinessFragment) mFragments.get(0)).getFoodAdapter().setData(i, data.get(i));
                    if (position == i) {
                        tvSpecNum.setText(specSelectNum + "");
                        tvSpecNum.setVisibility(specSelectNum > 0 ? View.VISIBLE : View.GONE);
                    }
                }
            }
        } else if (isShowDetail) {
//            addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), position, this);
        }
    }

    private void requestCleanShoppingCart() {
        CustomApplication.getRetrofit().cleanShoppingCart(businessId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }

                String body = response.body().toString();
//                if (!body.contains("{\"relogin\":1}")) {
//                    finish();
//                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                finish();
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutProductDetail.getVisibility() == View.VISIBLE) {
                layoutProductDetail.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                return true;
            } else {
                if (userInfo != null) {
                    if (carFoods.size() == 0) {
                        requestCleanShoppingCart();
                    } else {
                        saveOrderCar(false);
                    }
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
