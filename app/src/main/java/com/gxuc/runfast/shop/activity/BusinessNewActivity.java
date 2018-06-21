package com.gxuc.runfast.shop.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.shopcaradater.SpecCarAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessCouponInfo;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.bean.GoodsSellOptionInfo;
import com.gxuc.runfast.shop.bean.GoodsSellStandardInfo;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.SpecSelectInfo;
import com.gxuc.runfast.shop.bean.SpecSelectInfoAll;
import com.gxuc.runfast.shop.bean.SubOptionInfo;
import com.gxuc.runfast.shop.bean.TypeBean;
import com.gxuc.runfast.shop.bean.home.BusinessEvent;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.behaviors.AppBarBehavior;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.fragment.BusinessInfoFragment;
import com.gxuc.runfast.shop.fragment.EvaluateFragment;
import com.gxuc.runfast.shop.fragment.FirstFragment;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.BlurBitmapUtil;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.ViewUtils;
import com.gxuc.runfast.shop.view.AddWidget;
import com.gxuc.runfast.shop.view.AppBarStateChangeListener;
import com.gxuc.runfast.shop.view.BusinessCouponDialog;
import com.gxuc.runfast.shop.view.FlowRadioGroup;
import com.gxuc.runfast.shop.view.ShopCarView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class BusinessNewActivity extends BaseActivity implements AddWidget.OnAddClick, View.OnClickListener, SpecCarAdapter.UpdateSpecCountImp, BusinessCouponDialog.OnDialogClickListener {

    public static final String CAR_ACTION = "handleCar";
    public static final String CLEARCAR_ACTION = "clearCar";
    @BindView(R.id.iv_shop_bg)
    ImageView ivShopBg;
    @BindView(R.id.iv_shop)
    RoundedImageView ivShop;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_describe)
    TextView tvShopDescribe;
    @BindView(R.id.tv_shop_notice)
    TextView tvShopNotice;
    @BindView(R.id.tv_send_type)
    TextView tvSendType;
    @BindView(R.id.tv_get_coupon)
    TextView tvGetCoupon;
    @BindView(R.id.ll_contain_act)
    LinearLayout llContainAct;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tool_bar_business_name)
    TextView toolBarBusinessName;
    private CoordinatorLayout rootview;
    public BottomSheetBehavior behavior;
    public View scroll_container;
    private FirstFragment firstFragment;
    public static SpecCarAdapter carAdapter;
    private ShopCarView shopCarView;
    private int businessId;
    private String lon;
    private String lat;
    private BusinessNewDetail businessInfo;
    //商品集合
    private List<FoodBean> foodBeens = new ArrayList<>();
    private List<FoodBean> carFoods = new ArrayList<>();
    //类型集合
    private List<TypeBean> types = new ArrayList<>();

    private HashMap<Integer, HashMap<Integer, SpecSelectInfoAll>> specSelectAllMap = new HashMap<>();

    private AlertDialog specDialog;
    private TextView tvSpecTitle;
    private TextView tvSpecCount;
    private TextView tvSpecPrice;
    private LinearLayout llContainSpec;
    private FoodBean foodBeanOperation;
    private int standardId;
    private SpecSelectInfo specSelectInfo;
    private TextView tvPackingPrice;
    private RelativeLayout rlPacking;

    private String standardName;
    private UserInfo userInfo;
    private TextView tvSpecGroup;
    private ShopCartBean shopCartBean;
    private boolean isFirst = true;
    private RelativeLayout layoutProductDetail;
    private TextView tvProductName;
    private TextView tvProductSale;
    private TextView tvProductPrice;
    private TextView tvOldProductPrice;
    private TextView tvProductGiftName;
    private TextView tvProductContent;
    private LinearLayout llProductAct;
    private TextView tvProductDiscount;
    private TextView tvProductLimit;
    private RelativeLayout layoutSpec;
    private TextView tvSpec;
    private TextView tvSpecNum;
    private RoundedImageView ivGoodsHead;
    private AddWidget addWidgetDetail;
    private boolean isShowDetail;
    private int goodClickPosition;
    private AlertDialog alertDialog;
    private TextView shopCartNotice;
    private BusinessCouponDialog businessCouponDialog;
    private ArrayList<BusinessCouponInfo> businessCouponInfoList;
    private Bitmap blurBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initViews();
        initData();
        setListener();
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        intentFilter.addAction(CLEARCAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = UserService.getUserInfo(this);
        requestBufisnessGoods();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_business_new;
    }


    private void initViews() {
        rootview = (CoordinatorLayout) findViewById(R.id.rootview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewpager();
        initShopCar();
        initGoodDetail();
        initSpecDialog();
        initBusinessCouponDialog();
    }

    private void initData() {
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        businessId = getIntent().getIntExtra("businessId", 0);

        specSelectInfo = new SpecSelectInfo();
        specSelectInfo.optionIdMap = new HashMap<Integer, SubOptionInfo>();

        requestBusinessDetail();
//        requestBufisnessGoods();
    }

    private void setListener() {
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    toolbar.setBackgroundResource(R.color.transparent);
                    toolBarBusinessName.setText("");
                    //展开状态
//                    ivBack.setImageResource(R.drawable.icon_back_map);
//                    ivBack.setAlpha(1f);
                } else if (state == State.COLLAPSED) {
                    toolbar.setBackground(new BitmapDrawable(blurBitmap));
//                    toolbar.setBackgroundResource(R.color.black);
//                    toolbar.setTitle(businessInfo.name);
//                    toolBarBusinessName.setText(businessInfo.name);
//                    ivBack.setImageResource(R.drawable.icon_back_map);
//                    //折叠状态
//                    ivBack.setAlpha(1f);
                } else {
                    toolbar.setBackgroundResource(R.color.transparent);
//                    business_title.setText("");
//                    //中间状态
//                    Log.d("STATE", "verticalOffset =" + verticalOffset);
//                    ivBack.setImageResource(R.drawable.icon_back_map);
//                    ivBack.setAlpha(0.5f);
                }
            }
        });
    }

    private void initGoodDetail() {
        layoutProductDetail = (RelativeLayout) findViewById(R.id.layout_product_detail);
//        HeadZoomScrollView scrollView = (HeadZoomScrollView) findViewById(R.id.scrollView);
        tvProductName = (TextView) findViewById(R.id.tv_food_name);
        tvProductSale = (TextView) findViewById(R.id.tv_food_sale);
        tvProductPrice = (TextView) findViewById(R.id.tv_food_price);
        tvOldProductPrice = (TextView) findViewById(R.id.tv_old_product_price);
        tvProductGiftName = (TextView) findViewById(R.id.tv_product_gift_name);
        tvProductContent = (TextView) findViewById(R.id.tv_food_content);

        llProductAct = (LinearLayout) findViewById(R.id.ll_product_act);
        tvProductDiscount = (TextView) findViewById(R.id.tv_product_discount);
        tvProductLimit = (TextView) findViewById(R.id.tv_product_limit);

        addWidgetDetail = (AddWidget) findViewById(R.id.add_widget);
        layoutSpec = (RelativeLayout) findViewById(R.id.layout_detail_spec);
        tvSpec = (TextView) findViewById(R.id.tv_detail_spec);
        tvSpecNum = (TextView) findViewById(R.id.tv_detail_spec_num);
        //tvProductCount = (TextView) findViewById(R.id.tv_product_count);
        //ivSub = (ImageView) findViewById(R.id.iv_sub);
        ivGoodsHead = (RoundedImageView) findViewById(R.id.iv_product_head);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_product_back);
        ivBack.setOnClickListener(this);
        layoutSpec.setOnClickListener(this);
    }

    private void initSpecDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_spec, null);
        tvSpecTitle = (TextView) view.findViewById(R.id.tv_spec_title);
        ImageView ivCloseSpec = (ImageView) view.findViewById(R.id.iv_close_spec);
        llContainSpec = (LinearLayout) view.findViewById(R.id.ll_contain_spec);
        tvSpecPrice = (TextView) view.findViewById(R.id.tv_spec_price);
        tvSpecGroup = (TextView) view.findViewById(R.id.tv_spec_group);
        tvSpecCount = (TextView) view.findViewById(R.id.tv_spec_count);
        ImageView ivAddNum = (ImageView) view.findViewById(R.id.iv_add_num);
        ImageView ivSubNum = (ImageView) view.findViewById(R.id.iv_sub_num);
        ivAddNum.setOnClickListener(this);
        ivSubNum.setOnClickListener(this);
        ivCloseSpec.setOnClickListener(this);
        specDialog = new AlertDialog.Builder(this).setView(view).create();
    }

    private void initBusinessCouponDialog() {
        businessCouponDialog = new BusinessCouponDialog(this, this);
    }

    @Override
    public void onDialogClick(int position) {
        int couponId = businessCouponInfoList.get(position).id;
        requestReceiveBusinessCoupon(couponId);
    }

    private void requestReceiveBusinessCoupon(int couponId) {

        CustomApplication.getRetrofitNew().receiverBusinessCoupon(couponId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("领取成功");
                        businessCouponDialog.dismiss();
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


    private void requestBusinessDetail() {

        CustomApplication.getRetrofitNew().getBusinessDetail(businessId, Double.valueOf(lon), Double.valueOf(lat)).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                LogUtil.d("wdevon", "BusinessDetail------" + body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        businessInfo = JsonUtil.fromJson(data, BusinessNewDetail.class);
                        fillBusinessDetailView();
                    } else {
                        businessInfo = new BusinessNewDetail();
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

    private void fillBusinessDetailView() {
        if (!businessInfo.isopen) {
            showWarnDialog();
        }

        tvShopName.setText(businessInfo.name);


//            ivShopBg.setImageBitmap(BlurBuilder.blur(getApplicationContext(), myBitmap));

//        new DownloadImageTask().execute(UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath);


        x.image().bind(ivShopBg, UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                Bitmap bitmap = ((BitmapDrawable) result).getBitmap();
                blurBitmap = BlurBitmapUtil.blurBitmap(BusinessNewActivity.this, bitmap, 2);
                ivShopBg.setImageBitmap(blurBitmap);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        x.image().bind(ivShop, UrlConstant.ImageBaseUrl + businessInfo.mini_imgPath);
//        ivShopBg.setAlpha(0.8f);

        tvShopDescribe.setText("¥" + (businessInfo.startPay == null ? 0 : businessInfo.startPay.stripTrailingZeros().toPlainString()) + "起送 | 配送费¥" + businessInfo.deliveryFee.divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + " | 月售" + (businessInfo.salesnum == null ? 0 : businessInfo.salesnum));
        tvShopNotice.setVisibility(TextUtils.isEmpty(businessInfo.content) ? View.GONE : View.VISIBLE);
        tvShopNotice.setText(businessInfo.content);
        if (businessInfo.isDeliver == 0) {
            tvSendType.setText("快车专送·约" + businessInfo.deliveryDuration + "分钟");
        } else {
            tvSendType.setText("商家配送·约" + businessInfo.deliveryDuration + "分钟");
        }
        ivFavorite.setImageResource(businessInfo.enshrined ? R.drawable.icon_favorite : R.drawable.icon_not_favorite);

        llContainAct.removeAllViews();
        llContainAct.setTag(false);
        if (businessInfo.activityList != null && businessInfo.activityList.size() > 0) {
            for (int i = 0; i < businessInfo.activityList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_business_act, null);
                ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                tvAct.setText(businessInfo.activityList.get(i).name);
                showActImage(ivAct, businessInfo.activityList.get(i));
                if (i == 0) {
                    TextView tvActAll = (TextView) view.findViewById(R.id.tv_act_all);
                    tvActAll.setText(businessInfo.activityList.size() + "个活动");
                    tvActAll.setVisibility(View.VISIBLE);
                }

                if (i > 1) {
                    view.setVisibility(View.GONE);
                }
                llContainAct.addView(view);
            }

            if (businessInfo.activityList.size() > 2) {
                llContainAct.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
            }
        }

        llContainAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessInfo.activityList.size() > 2) {
                    boolean showStatus = false;
                    for (int i = 0; i < businessInfo.activityList.size(); i++) {
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

        ivFavorite.setOnClickListener(this);
        tvGetCoupon.setOnClickListener(this);
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

    private void showActImage(ImageView ivAct, BusinessEvent businessEvent) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
        switch (businessEvent.ptype) {
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
                ivAct.setImageResource(R.drawable.icon_free);
                break;
            case 6:
                ivAct.setImageResource(R.drawable.icon_coupon);
                break;
        }
    }

    private void requestBufisnessGoods() {
        CustomApplication.getRetrofitNew().getBusinessGoods(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                LogUtil.d("wdevon", "BusinessGoods------" + body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        dealBusinessGoods(jsonArray);
                        if (userInfo != null && jsonArray.length() > 0) {
                            requestShopCart();
                        } else {
                            shopCarView.showBadge(0);
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

    private void dealBusinessGoods(JSONArray jsonArray) {
        try {
            foodBeens.clear();
            types.clear();
            int adPosition = 0;

            if (jsonArray == null || jsonArray.length() <= 0) {
                return;
            }
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject listObject = jsonArray.getJSONObject(i);
                TypeBean typeBean = new TypeBean();
                typeBean.setName(listObject.optString("name"));
                types.add(typeBean);

                JSONArray glistArray = listObject.optJSONArray("goodsSellList");
                if (glistArray != null) {
                    int length1 = glistArray.length();
                    for (int j = 0; j < length1; j++) {
                        JSONObject jsonObject = glistArray.getJSONObject(j);
                        FoodBean foodBean = new FoodBean();
                        foodBean.setSelectCount(0);
//                        if (goodId != 0 && goodId == jsonObject.optInt("id")) {
//                            positionSpec = adPosition;
//                        } else {
//                            adPosition++;
//                        }
                        foodBean.setId(jsonObject.optInt("id"));
                        foodBean.setName(jsonObject.optString("name"));
                        foodBean.setIcon(jsonObject.optString("mini_imgPath"));
                        foodBean.setImgPath(jsonObject.optString("imgPath"));
//                        foodBean.setType(jsonObject.optString("sellTypeName"));
                        foodBean.setType(listObject.optString("name"));
                        foodBean.setPtype(jsonObject.optInt("ptype"));
                        foodBean.setShowprice(jsonObject.optString("showprice"));
                        foodBean.setIsonly(jsonObject.optInt("isonly"));
                        foodBean.setPrice(new BigDecimal(TextUtils.equals("null", jsonObject.optString("price")) ? "0" : jsonObject.optString("price")));
//                        foodBean.setDisprice(jsonObject.optString("disprice"));
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
                        ArrayList<GoodsSellStandardInfo> goodsSellStandardList = JsonUtil.fromJson(jsonObject.optString("goodsSellStandardList"), new TypeToken<ArrayList<GoodsSellStandardInfo>>() {
                        }.getType());
                        foodBean.setGoodsSellStandardList(goodsSellStandardList);
                        foodBean.setDisprice(goodsSellStandardList.get(0).disprice == null ? foodBean.getPrice() : goodsSellStandardList.get(0).disprice);
                        ArrayList<GoodsSellOptionInfo> goodsSellOptionList = JsonUtil.fromJson(jsonObject.optString("goodsSellOptionList"), new TypeToken<ArrayList<GoodsSellOptionInfo>>() {
                        }.getType());
                        foodBean.setGoodsSellOptionList(goodsSellOptionList);
                        foodBean.setSpecInfo(new SpecSelectInfo());

                        foodBean.setStandardId(goodsSellStandardList.get(0).id);

                        foodBeens.add(foodBean);
                    }
                }
            }
            LogUtil.d("devon", specSelectAllMap.toString());

            if (firstFragment.getFoodAdapter() != null) {
                firstFragment.getFoodAdapter().setNewData(foodBeens);
//                if (goodId != 0 && positionSpec != null) {
//                    showGoodDetail();
//                }
            }
            if (firstFragment.getTypeAdapter() != null) {
                firstFragment.getTypeAdapter().setNewData(types);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestShopCart() {

        CustomApplication.getRetrofitNew().getBusinessShopCart(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealShopCartInfo(jsonObject);
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

    private void dealShopCartInfo(JSONObject jsonObject) {
        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
            shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
            if (shopCartBean.cartItems != null && shopCartBean.cartItems.size() > 0) {
                dealShopCart();
            }

            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                shopCarView.cart_notice.setVisibility(View.GONE);
            } else {
                shopCarView.cart_notice.setVisibility(TextUtils.isEmpty(shopCartBean.cartTips) ? View.GONE : View.VISIBLE);
            }
            shopCartNotice.setVisibility(TextUtils.isEmpty(shopCartBean.cartTips) ? View.GONE : View.VISIBLE);
            shopCartNotice.setText(shopCartBean.cartTips);
            shopCarView.cart_notice.setText(shopCartBean.cartTips);


        } else {
            clearCar();
//            carAdapter.notifyDataSetChanged();
//            updateTyprSelect();
////            firstFragment.getFoodAdapter().setNewData(foodBeens);
//            updatePackingPrice();
//            shopCarView.showBadge(0);
//            shopCartNotice.setVisibility(View.GONE);
//            shopCarView.cart_notice.setVisibility(View.GONE);
//            shopCarView.updateAmount(new BigDecimal(0), businessInfo.startPay);
        }

        if (foodBeens.size() > 0) {
            if (foodBeens.get(goodClickPosition).getGoodsSellStandardList().size() > 1 || foodBeens.get(goodClickPosition).getGoodsSellOptionList().size() > 0) {
                tvSpecNum.setVisibility(foodBeens.get(goodClickPosition).getSelectCount() == 0 ? View.GONE : View.VISIBLE);
                tvSpecNum.setText(foodBeens.get(goodClickPosition).getSelectCount() + "");
            } else {
                addWidgetDetail.setData(this, foodBeens.get(goodClickPosition));
            }
        }
    }

    private void dealShopCart() {
        carFoods.clear();

        for (int i = 0; i < foodBeens.size(); i++) {
            int specTotalNum = 0;
            foodBeens.get(i).setSelectCount(0);
            foodBeens.get(i).getSpecInfo().num = 0;

            for (int j = 0; j < shopCartBean.cartItems.size(); j++) {
                if (foodBeens.get(i).getId() == shopCartBean.cartItems.get(j).key.goodsId) {
//                    if (foodBeens.get(i).getGoodsSellStandardList().size() > 1) {
                    if (foodBeens.get(i).getGoodsSellStandardList().size() > 1 || foodBeens.get(i).getGoodsSellOptionList().size() > 0) {
                        specTotalNum += shopCartBean.cartItems.get(j).num;
                        FoodBean foodSpecBean = new FoodBean();
                        foodSpecBean.setId(shopCartBean.cartItems.get(j).key.goodsId);
                        foodSpecBean.setName(foodBeens.get(i).getName());
                        foodSpecBean.setIcon(foodBeens.get(i).getIcon());
                        foodSpecBean.setImgPath(foodBeens.get(i).getImgPath());
//                        foodBean.setType(jsonObject.optString("sellTypeName"));
                        foodSpecBean.setType(foodBeens.get(i).getType());
                        foodSpecBean.setPtype(foodBeens.get(i).getPtype());
                        foodSpecBean.setShowprice(foodBeens.get(i).getShowprice());
//                        foodSpecBean.setIsonly(foodBeens.get(i).getIsonly());
                        foodSpecBean.setPrice(shopCartBean.cartItems.get(j).price);
//                        foodBean.setDisprice(jsonObject.optString("disprice"));
                        foodSpecBean.setDisprice(shopCartBean.cartItems.get(j).disprice == null ? foodSpecBean.getPrice() : shopCartBean.cartItems.get(j).disprice);
                        foodSpecBean.setSalesnum(foodBeens.get(i).getSalesnum());
                        foodSpecBean.setBusinessId(foodBeens.get(i).getBusinessId());
                        foodSpecBean.setBusinessName(foodBeens.get(i).getBusinessName());
                        foodSpecBean.setAgentId(foodBeens.get(i).getAgentId());
                        foodSpecBean.setContent(foodBeens.get(i).getContent());
                        foodSpecBean.setLimittype(foodBeens.get(i).getLimittype());
                        foodSpecBean.setIslimited(foodBeens.get(i).getIslimited());
                        foodSpecBean.setLimitNum(foodBeens.get(i).getLimitNum());
                        foodSpecBean.setNum(foodBeens.get(i).getNum());
                        foodSpecBean.setShowzs(foodBeens.get(i).getShowzs());
                        foodSpecBean.setGoodsSellStandardList(foodBeens.get(i).getGoodsSellStandardList());
                        foodSpecBean.setGoodsSellOptionList(foodBeens.get(i).getGoodsSellOptionList());
                        foodSpecBean.setStandardId(shopCartBean.cartItems.get(j).key.standarId);

                        foodBeens.get(i).setSelectCount(shopCartBean.cartItems.get(j).num);
                        foodSpecBean.setSelectCount(shopCartBean.cartItems.get(j).num);
                        foodSpecBean.setSpecInfo(new SpecSelectInfo());
                        foodSpecBean.getSpecInfo().num = shopCartBean.cartItems.get(j).num;
                        foodSpecBean.getSpecInfo().standardId = shopCartBean.cartItems.get(j).key.standarId;
                        foodSpecBean.getSpecInfo().standarOptionName = shopCartBean.cartItems.get(j).standarOptionName;
                        foodSpecBean.getSpecInfo().totalPrice = shopCartBean.cartItems.get(j).totalPrice;
//                        foodSpecBean.getSpecInfo().isLimited = shopCartBusinessInfoList.get(j).key.standarId;

                        foodSpecBean.getSpecInfo().optionIdMap = new HashMap<>();

                        if (shopCartBean.cartItems.get(j).key.optionIdPairList != null) {
                            for (int k = 0; k < shopCartBean.cartItems.get(j).key.optionIdPairList.size(); k++) {
                                for (int l = 0; l < foodBeens.get(i).getGoodsSellOptionList().size(); l++) {
                                    if (shopCartBean.cartItems.get(j).key.optionIdPairList.get(k).optionId == foodBeens.get(i).getGoodsSellOptionList().get(l).id) {
                                        for (int m = 0; m < foodBeens.get(i).getGoodsSellOptionList().get(l).subOptionList.size(); m++) {
                                            if (shopCartBean.cartItems.get(j).key.optionIdPairList.get(k).subOptionId == foodBeens.get(i).getGoodsSellOptionList().get(l).subOptionList.get(m).id) {
                                                foodSpecBean.getSpecInfo().optionIdMap.put(shopCartBean.cartItems.get(j).key.optionIdPairList.get(k).optionId, foodBeens.get(i).getGoodsSellOptionList().get(l).subOptionList.get(m));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        boolean isContain = false;
                        for (int k = 0; k < carFoods.size(); k++) {
                            if (carFoods.get(k).getId() == foodSpecBean.getId() && carFoods.get(k).getSpecInfo().standardId == foodSpecBean.getSpecInfo().standardId) {
                                isContain = true;
                                for (Map.Entry<Integer, SubOptionInfo> entry : carFoods.get(k).getSpecInfo().optionIdMap.entrySet()) {
                                    if (carFoods.get(k).getSpecInfo().optionIdMap.get(entry.getKey()).id == foodSpecBean.getSpecInfo().optionIdMap.get(entry.getKey()).id) {
                                        isContain = true;
                                    } else {
                                        isContain = false;
                                        break;
                                    }
                                }

                                if (isContain) {
                                    break;
                                }
                            }
                        }
                        if (!isContain) {
                            carFoods.add(foodSpecBean);
                        }
                    } else {
                        foodBeens.get(i).setSelectCount(shopCartBean.cartItems.get(j).num);
                        foodBeens.get(i).getSpecInfo().num = shopCartBean.cartItems.get(j).num;
                        foodBeens.get(i).getSpecInfo().totalPrice = shopCartBean.cartItems.get(j).totalPrice;
                        boolean isContain = false;
                        for (int k = 0; k < carFoods.size(); k++) {
                            if (carFoods.get(k).getId() == foodBeens.get(i).getId()) {
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            carFoods.add(foodBeens.get(i));
                        }
                    }
                }
            }
//            if (foodBeens.get(i).getGoodsSellStandardList().size() > 1) {
            if (foodBeens.get(i).getGoodsSellOptionList().size() > 0 || foodBeens.get(i).getGoodsSellStandardList().size() > 1) {
                foodBeens.get(i).setSelectCount(specTotalNum);
            }
        }

        carAdapter.notifyDataSetChanged();

        updateTyprSelect();
//        firstFragment.getTypeAdapter().notifyDataSetChanged();
//        if (isFirst) {
//            this.isFirst = false;
        firstFragment.getFoodAdapter().setNewData(foodBeens);

        //        }
        updatePackingPrice();
        shopCarView.showBadge(shopCartBean.totalNum);
//        int totalNum = 0;
//        for (int i = 0; i < shopCartBean.cartItems.size(); i++) {
//            totalNum += shopCartBean.cartItems.get(i).num;
//        }
//        shopCarView.showBadge(totalNum);
        shopCarView.updateAmount(shopCartBean.totalPay, businessInfo.startPay == null ? new

                BigDecimal(0) : businessInfo.startPay);

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
        firstFragment.getTypeAdapter().updateBadge(typeSelect);
    }


    public void showSpecDialog(int position) {
        foodBeanOperation = foodBeens.get(position);
        specSelectInfo.optionIdMap.clear();

        tvSpecTitle.setText(foodBeanOperation.getName());
        llContainSpec.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.item_spec, null);
        TextView tvSpceName = (TextView) view.findViewById(R.id.tv_spce_name);
        FlowRadioGroup frgSpec = (FlowRadioGroup) view.findViewById(R.id.frg_spec);
        tvSpceName.setText("规格:");
        for (int i = 0; i < foodBeanOperation.getGoodsSellStandardList().size(); i++) {
            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.item_spec_radio_button, null);
            rb.setText(foodBeanOperation.getGoodsSellStandardList().get(i).name);
            rb.setTag(foodBeanOperation.getGoodsSellStandardList().get(i));

            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 20, 20);
            rb.setLayoutParams(lp);
            frgSpec.addView(rb);
            if (i == 0) {
                frgSpec.check(rb.getId());
                onSellStandardClickListener.onClick(rb);
            }
            rb.setOnClickListener(onSellStandardClickListener);
        }
        llContainSpec.addView(view);

        if (foodBeanOperation.getGoodsSellOptionList() != null && foodBeanOperation.getGoodsSellOptionList().size() > 0) {
            for (int i = 0; i < foodBeanOperation.getGoodsSellOptionList().size(); i++) {
                View item_spec = getLayoutInflater().inflate(R.layout.item_spec, null);
                TextView tvSpceNameView = (TextView) item_spec.findViewById(R.id.tv_spce_name);
                FlowRadioGroup frgSpecView = (FlowRadioGroup) item_spec.findViewById(R.id.frg_spec);
                tvSpceNameView.setText(foodBeanOperation.getGoodsSellOptionList().get(i).name);
                for (int j = 0; j < foodBeanOperation.getGoodsSellOptionList().get(i).subOptionList.size(); j++) {
                    RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.item_spec_radio_button, null);
                    rb.setText(foodBeanOperation.getGoodsSellOptionList().get(i).subOptionList.get(j).name);
                    rb.setTag(foodBeanOperation.getGoodsSellOptionList().get(i).subOptionList.get(j));

                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 20, 20);
                    rb.setLayoutParams(lp);
                    frgSpecView.addView(rb);
                    if (j == 0) {
                        frgSpecView.check(rb.getId());
                        onSubOptionClickListener.onClick(rb);
                    }
                    rb.setOnClickListener(onSubOptionClickListener);
                }
                llContainSpec.addView(item_spec);
            }
        }

        specDialog.show();

    }

    View.OnClickListener onSellStandardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isInShopCar = false;
            int carPosition = -1;
            GoodsSellStandardInfo goodsSellStandardInfo = (GoodsSellStandardInfo) v.getTag();
            standardId = goodsSellStandardInfo.id;
            standardName = goodsSellStandardInfo.name;

            specSelectInfo.standardId = standardId;
            specSelectInfo.standardName = standardName;

            specSelectInfo.price = goodsSellStandardInfo.price;
            specSelectInfo.isLimited = goodsSellStandardInfo.isLimited;
            specSelectInfo.limitNum = goodsSellStandardInfo.limitNum;
//            ToastUtil.showToast("--goodId--" + foodBeanOperation.getId());
            tvSpecPrice.setText(goodsSellStandardInfo.price + "");

//            if (specSelectInfo.optionIdMap == null || specSelectInfo.optionIdMap.size() == 0) {
//                return;
//            }

            for (int i = 0; i < carFoods.size(); i++) {
                if (foodBeanOperation.getId() == carFoods.get(i).getId() &&
                        standardId == carFoods.get(i).getSpecInfo().standardId) {
                    if (foodBeanOperation.getGoodsSellOptionList() != null && foodBeanOperation.getGoodsSellOptionList().size() != 0) {
                        if (carFoods.get(i).getSpecInfo().optionIdMap == null || carFoods.get(i).getSpecInfo().optionIdMap.size() == 0) {
                            if (specSelectInfo.optionIdMap == null || specSelectInfo.optionIdMap.size() == 0) {
                                isInShopCar = true;
                                carPosition = i;
                            } else {
                                isInShopCar = false;
                            }
                            //比较carFoods.get(i).getSpecInfo().optionIdMap 和 specSelectInfo.optionIdMap是否相等
                        } else {
                            isInShopCar = true;
                            if (carFoods.get(i).getSpecInfo().optionIdMap.size() == specSelectInfo.optionIdMap.size()) {
                                for (int j = 0; j < carFoods.get(i).getGoodsSellOptionList().size(); j++) {
                                    if (carFoods.get(i).getSpecInfo().optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id !=
                                            specSelectInfo.optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id) {
                                        isInShopCar = false;
                                    } else {
                                        carPosition = i;
                                    }
                                }
                            } else {
                                isInShopCar = false;
                            }
                        }
                    } else {
                        specSelectInfo.optionIdMap.clear();
                        isInShopCar = true;
                        carPosition = i;
                    }
                } else {
                    if (foodBeanOperation.getGoodsSellOptionList() == null || foodBeanOperation.getGoodsSellOptionList().size() == 0) {
                        specSelectInfo.optionIdMap.clear();
                    }
                }
                if (isInShopCar) break;
            }
            tvSpecCount.setText(isInShopCar ? carFoods.get(carPosition).getSpecInfo().num + "" : "0");
            specSelectInfo.num = isInShopCar ? carFoods.get(carPosition).getSpecInfo().num : 0;

            showSpceStr();
        }

    };

    private void showSpceStr() {
        String specStr = "";
        specStr += specSelectInfo.standardName;
        for (Map.Entry<Integer, SubOptionInfo> entry : specSelectInfo.optionIdMap.entrySet()) {
            specStr += "," + entry.getValue().name;
        }
        tvSpecGroup.setText(specStr);

    }

    View.OnClickListener onSubOptionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isInShopCar = false;
            int carPosition = -1;
            SubOptionInfo subOptionInfo = (SubOptionInfo) v.getTag();
            int subOptionId = subOptionInfo.id;
            int optionId = subOptionInfo.optionId;
            specSelectInfo.optionIdMap.put(optionId, subOptionInfo);

            for (int i = 0; i < carFoods.size(); i++) {
                if (foodBeanOperation.getId() == carFoods.get(i).getId() &&
                        standardId == carFoods.get(i).getSpecInfo().standardId && specSelectInfo.optionIdMap.size() == carFoods.get(i).getSpecInfo().optionIdMap.size()) {
                    if (carFoods.get(i).getSpecInfo().optionIdMap != null && carFoods.get(i).getSpecInfo().optionIdMap.size() != 0) {
                        isInShopCar = true;
                        for (int j = 0; j < carFoods.get(i).getGoodsSellOptionList().size(); j++) {
                            if (carFoods.get(i).getSpecInfo().optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id !=
                                    specSelectInfo.optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id) {
                                isInShopCar = false;
                            } else {
                                carPosition = i;
                            }
                        }
                        //比较carFoods.get(i).getSpecInfo().optionIdMap 和 specSelectInfo.optionIdMap是否相等

                    } else {
                        isInShopCar = false;
                    }
                }
                if (isInShopCar) break;
            }

            if (isInShopCar) {
                tvSpecCount.setText(carFoods.get(carPosition).getSpecInfo().num + "");
                specSelectInfo.num = carFoods.get(carPosition).getSpecInfo().num;
            } else {
                tvSpecCount.setText("0");
                specSelectInfo.num = 0;
            }
            showSpceStr();
        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CAR_ACTION:
                    FoodBean foodBean = (FoodBean) intent.getSerializableExtra("foodbean");
                    FoodBean fb = foodBean;
                    int p = intent.getIntExtra("position", -1);
                    if (p >= 0 && p < firstFragment.getFoodAdapter().getItemCount()) {
                        fb = firstFragment.getFoodAdapter().getItem(p);
                        fb.setSelectCount(foodBean.getSelectCount());
                        firstFragment.getFoodAdapter().setData(p, fb);
                    } else {
                        for (int i = 0; i < firstFragment.getFoodAdapter().getItemCount(); i++) {
                            fb = firstFragment.getFoodAdapter().getItem(i);
                            if (fb.getId() == foodBean.getId()) {
                                fb.setSelectCount(foodBean.getSelectCount());
                                firstFragment.getFoodAdapter().setData(i, fb);
                                break;
                            }
                        }
                    }
                    dealCar(fb);
                    break;
                case CLEARCAR_ACTION:
                    clearCar();
                    break;
            }
            if (CAR_ACTION.equals(intent.getAction())) {

            }
        }
    };

    private void initShopCar() {
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
        rlPacking = (RelativeLayout) findViewById(R.id.rl_packing);
        shopCartNotice = (TextView) findViewById(R.id.shop_cart_notice);
        tvPackingPrice = (TextView) findViewById(R.id.tv_packing_price);

//        carRecView.setNestedScrollingEnabled(false);
        carRecView.setLayoutManager(new LinearLayoutManager(mContext));

        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
//        carAdapter = new CarAdapter(new ArrayList<FoodBean>(), this);
        carAdapter = new SpecCarAdapter(carFoods, this, this);
//        carAdapter.bindToRecyclerView(carRecView);
        carRecView.setAdapter(carAdapter);
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView.setBehavior(behavior, blackView);

        shopCarView.setOnSaveShopCartListener(new ShopCarView.OnSaveShopCartListener() {
            @Override
            public void onSaveShopCartListener() {
                //点击去结算
                if (userInfo == null) {
                    Intent intent = new Intent(BusinessNewActivity.this, LoginQucikActivity.class);
                    intent.putExtra("isRelogin", true);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(BusinessNewActivity.this, SubmitOrderActivity.class);
                intent.putExtra("businessId", businessId);
                intent.putExtra("suportSelf", businessInfo.suportSelf);
                startActivity(intent);
            }
        });

    }


    private void initViewpager() {
        scroll_container = findViewById(R.id.scroll_container);
        ScrollIndicatorView mSv = (ScrollIndicatorView) findViewById(R.id.indicator);
        ColorBar colorBar = new ColorBar(mContext, ContextCompat.getColor(mContext, R.color.text_ff9f14), 6,
                ScrollBar.Gravity.BOTTOM);
        colorBar.setWidth(ViewUtils.dip2px(mContext, 30));
        mSv.setScrollBar(colorBar);
        mSv.setSplitAuto(true);
        mSv.setOnTransitionListener(new OnTransitionTextListener().setColor(ContextCompat.getColor(mContext, R.color.text_ff9f14),
                ContextCompat.getColor(mContext, R.color.text_666666)));
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mSv, mViewPager);
        firstFragment = new FirstFragment();
        ViewpagerAdapter myAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        indicatorViewPager.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_favorite:
                //收藏按钮
                requestFavorite();
                break;
            case R.id.tv_get_coupon:
                //领券
                requestBusinessCoupon();
                break;
            case R.id.iv_close_spec:
                //关闭规格dialog
                specDialog.dismiss();
                break;
            case R.id.iv_add_num:
//                foodBeanOperation.setSelectCount(specSelectInfo.num);
                FoodBean foodBeanTwo = getSelectFoodBean();
                requestAddSubShopCart(foodBeanTwo, true, true);
//                onAddSubClickSpec(foodBeanTwo);
                break;
            case R.id.iv_sub_num:
                if (specSelectInfo.num == 0) {
                    return;
                }
                FoodBean foodBeanNew = getSelectFoodBean();
                requestAddSubShopCart(foodBeanNew, false, true);
//                onAddSubClickSpec(foodBeanNew);
                break;
            case R.id.layout_detail_spec://规格框
                showSpecDialog(goodClickPosition);
                break;
            case R.id.iv_product_back://规格框
                layoutProductDetail.setVisibility(View.GONE);
                appbar.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void requestBusinessCoupon() {
        CustomProgressDialog.startProgressDialog(this);

        CustomApplication.getRetrofitNew().getBusinessCoupon(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        if (!TextUtils.isEmpty(data) || !TextUtils.equals("null", jsonObject.optString("data"))) {
                            fillBusinessCouponDialog(data);
                        } else {
                            ToastUtil.showToast("暂无可领取优惠券");
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
                CustomProgressDialog.stopProgressDialog();
            }
        });


    }

    private void fillBusinessCouponDialog(String data) {
        businessCouponInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<BusinessCouponInfo>>() {
        }.getType());

        if (businessCouponInfoList != null && businessCouponInfoList.size() > 0) {
            businessCouponDialog.show();
            businessCouponDialog.setData(businessCouponInfoList);
        } else {
            ToastUtil.showToast("暂无可领取优惠券");
        }

    }

    private void requestFavorite() {
        if (businessInfo.enshrined) {
            CustomApplication.getRetrofitNew().deleteFavorite(businessId).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            ToastUtil.showToast("取消收藏成功");
                            ivFavorite.setImageResource(R.drawable.icon_not_favorite);
                            businessInfo.enshrined = !businessInfo.enshrined;
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

        } else {
            CustomApplication.getRetrofitNew().addFavorite(businessId).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            ToastUtil.showToast("收藏成功");
                            ivFavorite.setImageResource(R.drawable.icon_favorite);
                            businessInfo.enshrined = !businessInfo.enshrined;
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

    }

    @Nullable
    private FoodBean getSelectFoodBean() {
        boolean isCarFoodData = false;

        for (int i = 0; i < carFoods.size(); i++) {
            if (foodBeanOperation.getId() == carFoods.get(i).getId() &&
                    standardId == carFoods.get(i).getSpecInfo().standardId) {
                if (carFoods.get(i).getSpecInfo().optionIdMap != null) {
                    isCarFoodData = true;
                    for (int j = 0; j < carFoods.get(i).getGoodsSellOptionList().size(); j++) {
                        if (carFoods.get(i).getSpecInfo().optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id !=
                                specSelectInfo.optionIdMap.get(carFoods.get(i).getGoodsSellOptionList().get(j).id).id) {
                            isCarFoodData = false;
                        }
                    }
                    //比较carFoods.get(i).getSpecInfo().optionIdMap 和 specSelectInfo.optionIdMap是否相等

                } else {
                    isCarFoodData = true;
                }

            }
        }
        FoodBean foodBeanSelect = new FoodBean();

        SpecSelectInfo specInfo = new SpecSelectInfo();
        foodBeanSelect.setSpecInfo(specInfo);
        if (!isCarFoodData) {
            foodBeanSelect.setSelectCount(specSelectInfo.num);
            foodBeanSelect.getSpecInfo().num = specSelectInfo.num;
        } else {
            if (foodBeanOperation.getIslimited() == 1) {
                if (foodBeanOperation.getLimittype() == 0) {
                    if (specSelectInfo.num > foodBeanOperation.getLimitNum()) {
                        ToastUtil.showToast("已达到限购上限");
                        return null;
                    } else {
                        foodBeanSelect.setSelectCount(specSelectInfo.num);
                        foodBeanSelect.getSpecInfo().num = specSelectInfo.num;
                    }
                } else {
                    if (specSelectInfo.num >= foodBeanOperation.getLimitNum()) {
                        ToastUtil.showToast("已超过优惠件数，将以原价购买");
                    }
                    foodBeanSelect.setSelectCount(specSelectInfo.num);
                    foodBeanSelect.getSpecInfo().num = specSelectInfo.num;
                }
            } else {
                foodBeanSelect.setSelectCount(specSelectInfo.num);
                foodBeanSelect.getSpecInfo().num = specSelectInfo.num;
            }
        }
        foodBeanSelect.setStandardId(standardId);
        foodBeanSelect.getSpecInfo().standardId = standardId;
        foodBeanSelect.getSpecInfo().standardName = standardName;
        foodBeanSelect.getSpecInfo().optionIdMap = new HashMap<>();
        foodBeanSelect.getSpecInfo().optionIdMap.putAll(specSelectInfo.optionIdMap);
        foodBeanSelect.setId(foodBeanOperation.getId());
        foodBeanSelect.setName(foodBeanOperation.getName());
        foodBeanSelect.setSalesnum(foodBeanOperation.getSalesnum());
        foodBeanSelect.setIsCommand(foodBeanOperation.getIsCommand());
        foodBeanSelect.setPrice(specSelectInfo.price);
        foodBeanSelect.setDisprice(specSelectInfo.disprice);
//                foodBeanTwo.setDisprice(specBeanList.get(point).getDiscount() != null ? String.valueOf(specBeanList.get(point).getDiscount()) : "0");
        foodBeanSelect.setIcon(foodBeanOperation.getIcon());
        foodBeanSelect.setImgPath(foodBeanOperation.getImgPath());
        foodBeanSelect.setCut(foodBeanOperation.getCut());
        foodBeanSelect.setType(foodBeanOperation.getType());
        foodBeanSelect.setPtype(foodBeanOperation.getPtype());
        foodBeanSelect.setIsonly(foodBeanOperation.getIsonly());
        foodBeanSelect.setNum(foodBeanOperation.getNum());
        foodBeanSelect.setLimittype(foodBeanOperation.getLimittype());
        foodBeanSelect.setIslimited(foodBeanOperation.getIslimited());
        foodBeanSelect.setLimitNum(foodBeanOperation.getLimitNum());
        foodBeanSelect.setShowprice(foodBeanOperation.getShowprice());
        foodBeanSelect.setGoodsSellStandardList(foodBeanOperation.getGoodsSellStandardList());
        foodBeanSelect.setGoodsSellOptionList(foodBeanOperation.getGoodsSellOptionList());
        return foodBeanSelect;
    }

    private void onAddSubClickSpec(FoodBean foodBeanOperation) {
        dealCarSpec(foodBeanOperation);

        int specSelectNum = 0;
        for (int i = 0; i < carFoods.size(); i++) {

            if (carFoods.get(i).getId() == foodBeanOperation.getId()) {
                specSelectNum += carFoods.get(i).getSelectCount();
            }

        }

        for (int i = 0; i < foodBeens.size(); i++) {

            if (foodBeanOperation.getId() == foodBeens.get(i).getId()) {
                foodBeens.get(i).setSelectCount(specSelectNum);
                firstFragment.getFoodAdapter().setData(i, foodBeens.get(i));
//                if (position == i) {
//                    tvSpecNum.setText(specSelectNum + "");
//                    tvSpecNum.setVisibility(specSelectNum > 0 ? View.VISIBLE : View.GONE);
//                }
            }
        }

    }

    @Override
    public void add(View view, FoodBean foodBean, int position) {
//        if (foodBean.getGoodsSellStandardList().size() > 1) {
        requestAddSubShopCart(foodBean, true, false);

//        if (foodBean.getGoodsSellOptionList().size() > 0 || foodBean.getGoodsSellStandardList().size() > 1) {
//            onAddSubClickSpec(foodBean);
//        } else {
//            onAddClick(view, foodBean);
//        }
    }

    @Override
    public void sub(FoodBean fb, int position) {
        requestAddSubShopCart(fb, false, false);
//        onAddSubClickSpec(fb);
    }

    private void requestAddSubShopCart(FoodBean foodBean, boolean isAdd, final boolean isSpec) {

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonKey = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", foodBean.getId());
            jsonObject.put("standarId", foodBean.getGoodsSellStandardList().size() == 1 ? foodBean.getStandardId() : foodBean.getSpecInfo().standardId);
            JSONArray optionIdPairArray = new JSONArray();
            if (foodBean.getGoodsSellOptionList() != null && foodBean.getGoodsSellOptionList().size() > 0) {
                for (int j = 0; j < foodBean.getGoodsSellOptionList().size(); j++) {
                    JSONObject optionIdPairObject = new JSONObject();
                    optionIdPairObject.put("optionId", foodBean.getGoodsSellOptionList().get(j).id);
                    optionIdPairObject.put("subOptionId", foodBean.getSpecInfo().optionIdMap.get(foodBean.getGoodsSellOptionList().get(j).id).id);
                    optionIdPairArray.put(optionIdPairObject);
                }
            }
            jsonObject.put("optionIdPairList", optionIdPairArray);
            jsonKey.put("key", jsonObject);
//            jsonKey.put("num", foodBean.getSpecInfo().num);
            jsonKey.put("num", 1);

            jsonArray.put(jsonKey);

            SharePreferenceUtil.getInstance().putStringValue("shopCart", jsonArray.toString());
            SharePreferenceUtil.getInstance().putIntValue("businessId", businessId);

            if (isAdd) {
                CustomApplication.getRetrofitNew().addShopCart(businessId).enqueue(new MyCallback<String>() {
                    @Override
                    public void onSuccessResponse(Call<String> call, Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.optBoolean("success")) {
                                dealShopCartInfo(jsonObject);
                                if (isSpec) {
                                    specSelectInfo.num++;
                                    tvSpecCount.setText(specSelectInfo.num + "");
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
            } else {
                CustomApplication.getRetrofitNew().subShopCart(businessId).enqueue(new MyCallback<String>() {
                    @Override
                    public void onSuccessResponse(Call<String> call, Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.optBoolean("success")) {
                                dealShopCartInfo(jsonObject);
                                if (isSpec) {
                                    specSelectInfo.num--;
                                    tvSpecCount.setText(specSelectInfo.num + "");
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


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ViewpagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private LayoutInflater inflater;
        private int padding;
        private String[] tabs = new String[]{"商品", "评价", "商家"};

        ViewpagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(mContext);
            padding = ViewUtils.dip2px(mContext, 20);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            convertView = inflater.inflate(R.layout.item_textview, container, false);
            TextView textView = (TextView) convertView;
            textView.setText(tabs[position]); //名称
            textView.setPadding(padding, 0, padding, 0);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position) {
                case 0:
                    return firstFragment;
                case 1:
                    return new EvaluateFragment();
                case 2:
                    return new BusinessInfoFragment();
            }
            return null;
        }

    }

    @Override
    public void onAddClick(View view, FoodBean fb) {
//        dealCar(fb);
        requestAddSubShopCart(fb, true, false);
        ViewUtils.addTvAnim(view, shopCarView.carLoc, mContext, rootview);
    }


    @Override
    public void onSubClick(FoodBean fb) {
        requestAddSubShopCart(fb, false, false);
//        dealCar(fb);
    }

    private void dealCar(FoodBean foodBean) {
        HashMap<String, Integer> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            firstFragment.getFoodAdapter().notifyDataSetChanged();
        }
//        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean fb = carFoods.get(i);
            if (fb.getId() == foodBean.getId()) {
                fb = foodBean;
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else {
                    fb.setSelectCount(foodBean.getSelectCount());
                    fb.getSpecInfo().num = foodBean.getSelectCount();
                }
            }
            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
        }
        if (p >= 0) {
            carFoods.remove(p);
        } else if (!hasFood && foodBean.getSelectCount() > 0) {
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
        shopCarView.showBadge(total);
        firstFragment.getTypeAdapter().updateBadge(typeSelect);
//        firstFragment.getFoodAdapter().notifyDataSetChanged();
        updatePackingPrice();
        shopCarView.updateAmount(amount, businessInfo.startPay);
    }

    private void dealCarSpec(FoodBean foodBean) {
        HashMap<String, Integer> typeSelect = new HashMap<>();
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;

        boolean isHasFood = false;
//        boolean isEquals = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            firstFragment.getFoodAdapter().notifyDataSetChanged();
        }
        //List<FoodBean> flist = carFoods;
        int p = -1;

        for (int i = 0; i < carFoods.size(); i++) {
            int carPosition = -1;
            boolean hasFood = false;
            FoodBean fb = carFoods.get(i);

            if (fb.getId() == foodBean.getId() &&
                    foodBean.getSpecInfo().standardId == carFoods.get(i).getSpecInfo().standardId) {

                if (fb.getSpecInfo().optionIdMap != null && fb.getSpecInfo().optionIdMap.size() != 0) {
                    hasFood = true;
                    for (int j = 0; j < fb.getGoodsSellOptionList().size(); j++) {
                        if (fb.getSpecInfo().optionIdMap.get(fb.getGoodsSellOptionList().get(j).id).id !=
                                foodBean.getSpecInfo().optionIdMap.get(fb.getGoodsSellOptionList().get(j).id).id) {
                            hasFood = false;
                            break;
//                            isEquals = true;
//                            fb.setSelectCount(foodBean.getSelectCount());
//                            fb.getSpecInfo().num = foodBean.getSpecInfo().num;
                        } else {
                            carPosition = i;
                        }
                    }
                } else {
                    if (foodBean.getSpecInfo().optionIdMap == null || foodBean.getSpecInfo().optionIdMap.size() == 0) {
                        hasFood = true;
                        carPosition = i;
                    } else {
                        hasFood = false;
                    }
                }

                if (foodBean.getSpecInfo().num == 0) {
                    p = i;
                }
//                if (!isEquals){
//                    if (i == carFoods.size() - 1) {
//                        hasFood = false;
//                    }
//                }
            }

            if (hasFood) {
                isHasFood = true;
                carFoods.get(carPosition).setSelectCount(foodBean.getSpecInfo().num);
                carFoods.get(carPosition).getSpecInfo().num = foodBean.getSpecInfo().num;
//                break;
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
        } else if (!isHasFood) {
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
        shopCarView.showBadge(total);
        firstFragment.getTypeAdapter().updateBadge(typeSelect);
        firstFragment.getFoodAdapter().notifyDataSetChanged();
        updatePackingPrice();
        shopCarView.updateAmount(amount, businessInfo.startPay);
    }

    public void expendCut(View view) {
        float cty = scroll_container.getTranslationY();
        if (!ViewUtils.isFastClick()) {
            ViewAnimator.animate(scroll_container)
                    .translationY(cty, cty == 0 ? AppBarBehavior.cutExpHeight : 0)
                    .decelerate()
                    .duration(100)
                    .start();
        }
    }

    public void clearCar(View view) {
        ViewUtils.showClearCar(mContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestClearShopCart();
            }
        });
    }

    private void requestClearShopCart() {

        CustomApplication.getRetrofitNew().clearShopCart(businessId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        clearCar();
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

    private void clearCar() {
        for (int i = 0; i < carFoods.size(); i++) {
            FoodBean fb = carFoods.get(i);
            fb.setSelectCount(0);
            fb.getSpecInfo().num = 0;
            for (int j = 0; j < foodBeens.size(); j++) {
                if (fb.getId() == foodBeens.get(j).getId()) {
                    foodBeens.get(j).setSelectCount(0);
                    foodBeens.get(j).getSpecInfo().num = 0;
                    firstFragment.getFoodAdapter().notifyItemChanged(j, 0);
                }
            }
        }

        carFoods.clear();
        carAdapter.notifyDataSetChanged();
        firstFragment.getFoodAdapter().notifyDataSetChanged();
        shopCarView.showBadge(0);
        firstFragment.getTypeAdapter().updateBadge(new HashMap<String, Integer>());
        updatePackingPrice();
        shopCarView.updateAmount(new BigDecimal(0), businessInfo.startPay);

        addWidgetDetail.setData(this, firstFragment.getFoodAdapter().getItem(goodClickPosition));
        tvSpecNum.setText(foodBeens.get(goodClickPosition).getSelectCount() + "");
        tvSpecNum.setVisibility(foodBeens.get(goodClickPosition).getSelectCount() > 0 ? View.VISIBLE : View.GONE);
    }

    public void toShopDetail(View view) {
//        ShopInfoNewContainer shopInfoContainer = (ShopInfoNewContainer) findViewById(R.id.shopcontainer);
        if (Build.VERSION.SDK_INT > 20) {
//            startActivity(new Intent(mContext, ShopDetailActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, shopInfoContainer.iv_shop, "transitionShopImg").toBundle());
        } else {
//            startActivity(new Intent(mContext, ShopDetailActivity.class));
        }

    }

    private void updatePackingPrice() {
//        rlPacking.setVisibility(carFoods.size() > 0 ? View.VISIBLE : View.GONE);
//        int packNum = 0;
//        if (carFoods.size() > 0) {
//            for (int i = 0; i < carFoods.size(); i++) {
//                if (carFoods.get(i).getPtype() != 1) {
//                    packNum += carFoods.get(i).getSelectCount();
//                }
//            }
//            totalPacking = new BigDecimal(String.valueOf(packNum)).multiply(new BigDecimal(String.valueOf(businessInfo.packing)));
//            rlPacking.setVisibility(totalPacking.compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
//            tvPackingPrice.setText("¥" + totalPacking);
//        } else {
//            totalPacking = new BigDecimal(0.0);
//        }
        if (shopCartBean == null || shopCartBean.totalPackageFee.compareTo(BigDecimal.ZERO) == 0) {
            rlPacking.setVisibility(View.GONE);
        } else {
            rlPacking.setVisibility(View.VISIBLE);
            tvPackingPrice.setText("¥" + shopCartBean.totalPackageFee);
        }
//        rlPacking.setVisibility(shopCartBean == null || shopCartBean.totalPackageFee.compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
//        tvPackingPrice.setText("¥" + shopCartBean.totalPackageFee);
    }

    public void showGoodDetail(int position) {
        if (!businessInfo.isopen) {
            layoutSpec.setVisibility(View.GONE);
            addWidgetDetail.setVisibility(View.GONE);
        }

        goodClickPosition = position;
        FoodBean foodBeanDetail = foodBeens.get(position);
        //positionDetail = position;
        isShowDetail = true;
        if (foodBeanDetail.getGoodsSellStandardList().size() > 1 || foodBeanDetail.getGoodsSellOptionList().size() > 0) {
            layoutSpec.setVisibility(View.VISIBLE);
            addWidgetDetail.setVisibility(View.GONE);
            tvSpecNum.setText(foodBeanDetail.getSelectCount() + "");
            tvSpecNum.setVisibility(foodBeanDetail.getSelectCount() > 0 ? View.VISIBLE : View.GONE);
        } else {
            layoutSpec.setVisibility(View.GONE);
            addWidgetDetail.setVisibility(View.VISIBLE);
        }
//        if (((BusinessFragment) mFragments.get(0)).getFoodAdapter() != null) {
        addWidgetDetail.setData(this, firstFragment.getFoodAdapter().getItem(goodClickPosition));
//        addWidgetDetail.setData(((BusinessFragment) mFragments.get(0)).getFoodAdapter(), positionSpec, this);
//        }
        layoutProductDetail.setVisibility(View.VISIBLE);
        rotateyAnimShow(layoutProductDetail);
        appbar.setVisibility(View.GONE);

        if (!businessInfo.isopen) {
            layoutSpec.setVisibility(View.GONE);
            addWidgetDetail.setVisibility(View.GONE);
        }

        fillProductDetail(foodBeanDetail);
    }

    private void fillProductDetail(FoodBean foodBeanDetail) {
        tvProductName.setText(foodBeanDetail.getName());
        tvProductSale.setText("月售" + foodBeanDetail.getSalesnum());
//        tvProductPrice.setText(foodBeanDetail.getPrice() + "");
        tvProductContent.setText(foodBeanDetail.getContent());
        x.image().bind(ivGoodsHead, UrlConstant.ImageBaseUrl + foodBeanDetail.getImgPath(), NetConfig.optionsHeadImage);

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


    public List<FoodBean> getFoodBeens() {
        return foodBeens;
    }

    public List<TypeBean> getTypes() {
        return types;
    }

    public BusinessNewDetail getBusiness() {
        return businessInfo;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutProductDetail.getVisibility() == View.VISIBLE) {
                layoutProductDetail.setVisibility(View.GONE);
                appbar.setVisibility(View.VISIBLE);
                return true;
            } else {
                finish();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
