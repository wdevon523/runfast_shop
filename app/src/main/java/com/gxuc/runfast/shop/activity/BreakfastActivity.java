package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.adapter.BreakfastSortAdapter;
import com.gxuc.runfast.shop.adapter.BusinessCategoryAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessCategoryInfo;
import com.gxuc.runfast.shop.bean.home.NearByBusinessInfo;
import com.gxuc.runfast.shop.bean.mainmiddle.ClassTypeInfos;
import com.gxuc.runfast.shop.adapter.BreakfastClassAdapter;
import com.gxuc.runfast.shop.bean.SortInfo;
import com.gxuc.runfast.shop.bean.mainmiddle.ClassTypeInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.mainmiddle.MiddleSort;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 早餐
 */
public class BreakfastActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.view_class_list)
    RecyclerView recyclerViewClass;

    @BindView(R.id.tv_class_name)
    TextView tvClassName;

    @BindView(R.id.iv_class_arrow)
    ImageView ivClassArrow;

    @BindView(R.id.tv_sort_name)
    TextView tvSortName;

    @BindView(R.id.iv_sort_arrow)
    ImageView ivSortArrow;

    @BindView(R.id.view_breakfast_list)
    RecyclerView recyclerViewList;

    @BindView(R.id.view_sort_list)
    RecyclerView recyclerViewSort;

    @BindView(R.id.layout_class_back)
    RelativeLayout layoutClassBack;
    @BindView(R.id.rl_refresh)
    SmartRefreshLayout smartRefreshLayout;

    private boolean isClass;

    private boolean isSort;

    private List<NearByBusinessInfo> businessInfos = new ArrayList<>();

    private ArrayList<BusinessCategoryInfo> businessCategoryInfoList = new ArrayList<>();
    private List<SortInfo> sortInfos = new ArrayList<>();


    private BreakfastClassAdapter adapterType;
    private BreakfastSortAdapter adapterSort;

    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;

    private double lat;
    private double lon;
    private String url;
    private MiddleSort mSort;
    private Integer mPosition;
    private Integer mPositionSort = 1;
    private String mName = "";
    private int mTotalpage;
    private String typeId;
    private int sortId;
    private BusinessCategoryAdapter businessCategoryAdapter;
    private String agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);
        ButterKnife.bind(this);
        initData();
        setData();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        tvToolbarTitle.setText(mName);
    }

    private void setData() {
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSort.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewClass.setAdapter(adapterType);
        recyclerViewSort.setAdapter(adapterSort);
        recyclerViewList.setAdapter(businessCategoryAdapter);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        mName = getIntent().getStringExtra("typeName");
        if (TextUtils.isEmpty(mName)) {
            mSort = getIntent().getParcelableExtra("middleData");
            mName = mSort.getTypename();
        }

        lat = Double.valueOf(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT));
        lon = Double.valueOf(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON));
        agentId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.AGENTID);
        if (!TextUtils.isEmpty(mName)) {
            tvClassName.setText(mName);
        }
        getSortInfo();

        adapterType = new BreakfastClassAdapter(businessCategoryInfoList, this, this);
        adapterSort = new BreakfastSortAdapter(sortInfos, this, this);
        businessCategoryAdapter = new BusinessCategoryAdapter(this, businessInfos);
//        getBusiness(lon, lat, page);
        getBusinessCategory();

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearRecyclerViewData();
                        refreshData();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.setEnableLoadMore(true);//恢复上拉状态
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage++;
                        requestBusienss();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });

        businessCategoryAdapter.setOnNearByBusinessClickListener(new BusinessCategoryAdapter.OnNearByBusinessClickListener() {
            @Override
            public void onNearByBusinessClickListener(int position, NearByBusinessInfo nearByBusinessInfo) {
                Intent intent = new Intent(BreakfastActivity.this, BusinessNewActivity.class);
                intent.putExtra("businessId", nearByBusinessInfo.id);
                startActivity(intent);
            }
        });
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        requestBusienss();
    }

    private void clearRecyclerViewData() {
        businessInfos.clear();
        businessCategoryAdapter.setList(businessInfos);
    }


    /**
     * 获取商品分类
     */
    private void getBusinessCategory() {
        CustomApplication.getRetrofitNew().getBusinessCategory().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealBusinessCategory(jsonObject.optString("data"));
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

    private void dealBusinessCategory(String data) {
        businessCategoryInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<BusinessCategoryInfo>>() {
        }.getType());

        for (int i = 0; i < businessCategoryInfoList.size(); i++) {
            if (!TextUtils.isEmpty(mName) && TextUtils.equals(businessCategoryInfoList.get(i).name, mName)) {
                businessCategoryInfoList.get(i).isSelect = true;
                typeId = businessCategoryInfoList.get(i).id + "";
                break;
            }
        }


//
//        ClassTypeInfos classTypeInfo = GsonUtil.parseJsonWithGson(data, ClassTypeInfos.class);
//        List<ClassTypeInfo> listTypeInfos = classTypeInfo.getBustype();
//        ClassTypeInfo info = new ClassTypeInfo();
//        info.id = "";
//        info.name = "全部分类";
//        info.isSelect = TextUtils.isEmpty(mName);
//        info.imgId = R.drawable.icon_class_all;
//        this.typeInfos.add(info);
//        for (int i = 0; i < listTypeInfos.size(); i++) {
//            info = new ClassTypeInfo();
//            info.id = listTypeInfos.get(i).getId();
//            info.name = listTypeInfos.get(i).getName();
//            if (!TextUtils.isEmpty(mName) && TextUtils.equals(info.name, mName)) {
//                info.isSelect = true;
//                typeId = info.id;
//            }
//            info.imgId = R.drawable.icon_class_all;
//            this.typeInfos.add(info);
//        }
        refreshData();
        adapterType.setList(businessCategoryInfoList);
    }

    /**
     * 分类选择
     */
    private void requestBusienss() {

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getNearByBusiness(agentId, lon, lat, sortId, new IdentityHashMap<String, Integer>(), new IdentityHashMap<String, Integer>(), typeId, currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            dealSearchGoodsType(jsonArray.toString());
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

    private void dealSearchGoodsType(String data) {

        if (TextUtils.isEmpty(data)) {
            isLastPage = true;
            return;
        }

        ArrayList<NearByBusinessInfo> nearByBusinessInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<NearByBusinessInfo>>() {
        }.getType());
        businessInfos.addAll(nearByBusinessInfoList);
        businessCategoryAdapter.setList(businessInfos);
    }

    @OnClick({R.id.layout_class, R.id.layout_sort, R.id.tv_class_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_class:
                tvClassName.setTextColor(getResources().getColor(R.color.color_orange_select));
                tvSortName.setTextColor(getResources().getColor(R.color.color_text_gray_two));
                ivSortArrow.setImageResource(R.drawable.icon_bottom_arrow);
                layoutClassBack.setVisibility(View.VISIBLE);
                recyclerViewClass.setVisibility(View.VISIBLE);
                recyclerViewSort.setVisibility(View.GONE);
                if (!isClass) {
                    ivClassArrow.setImageResource(R.drawable.icon_top_arrow);
                    isClass = true;
                    isSort = false;
                } else {
                    ivClassArrow.setImageResource(R.drawable.icon_bottom_arrow);
                    isClass = false;
                    layoutClassBack.setVisibility(View.GONE);
                }
                break;
            case R.id.layout_sort:
                layoutClassBack.setVisibility(View.VISIBLE);
                ivClassArrow.setImageResource(R.drawable.icon_bottom_arrow);
                tvClassName.setTextColor(getResources().getColor(R.color.color_text_gray_two));
                tvSortName.setTextColor(getResources().getColor(R.color.color_orange_select));
                recyclerViewSort.setVisibility(View.VISIBLE);
                recyclerViewClass.setVisibility(View.GONE);
                if (!isSort) {
                    ivSortArrow.setImageResource(R.drawable.icon_top_arrow);
                    isSort = true;
                    isClass = false;
                } else {
                    ivSortArrow.setImageResource(R.drawable.icon_bottom_arrow);
                    isSort = false;
                    layoutClassBack.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_class_back:
                uiHide();
                break;
        }
    }

    /**
     * 隐藏类型选择
     */
    private void uiHide() {
        layoutClassBack.setVisibility(View.GONE);
        ivClassArrow.setImageResource(R.drawable.icon_bottom_arrow);
        ivSortArrow.setImageResource(R.drawable.icon_bottom_arrow);
        isClass = false;
        isSort = false;
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.layout_class_select:
                    businessInfos.clear();
                    mPosition = (Integer) v.getTag();
                    for (int i = 0; i < businessCategoryInfoList.size(); i++) {
                        businessCategoryInfoList.get(i).isSelect = false;
                    }
                    businessCategoryInfoList.get(mPosition).isSelect = true;
                    adapterType.notifyDataSetChanged();
//                    if (mPosition == 0) {
//                        mName = mSort.getTypename();
//                    } else {
                    typeId = businessCategoryInfoList.get(mPosition).id + "";
//                    }
                    tvClassName.setText(businessCategoryInfoList.get(mPosition).name);
                    tvToolbarTitle.setText(businessCategoryInfoList.get(mPosition).name);
                    refreshData();
                    uiHide();
                    break;
                case R.id.layout_sort_select:
                    mPositionSort = (Integer) v.getTag();
                    for (int i = 0; i < sortInfos.size(); i++) {
                        sortInfos.get(i).isSelect = false;
                    }
                    sortInfos.get(mPositionSort).isSelect = true;
                    adapterSort.notifyDataSetChanged();
                    sortId = sortInfos.get(mPositionSort).id;
                    tvSortName.setText(sortInfos.get(mPositionSort).name);
                    refreshData();
                    uiHide();
                    break;
                case R.id.layout_breakfast_item:
                    Integer positionBusiness = (Integer) v.getTag();
                    Intent intent = new Intent(this, BusinessNewActivity.class);
                    intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_BOTTOM_PAGE);
                    intent.putExtra("businessId", businessInfos.get(positionBusiness).id);
                    startActivity(intent);
                    break;
            }
        }
    }

//    public List<ClassTypeInfo> getClassInfo() {
//        ClassTypeInfo info = new ClassTypeInfo();
//        info.name = "全部分类";
//        info.isSelect = true;
//        info.imgId = R.drawable.icon_class_all;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "中餐";
//        info.imgId = R.drawable.icon_chinese_food;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "西餐";
//        info.imgId = R.drawable.icon_wester_food;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "奶茶饮料";
//        info.imgId = R.drawable.icon_tea_drinks;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "烧烤";
//        info.imgId = R.drawable.icon_barbecue;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "糕点";
//        info.imgId = R.drawable.icon_cake;
//        typeInfos.add(info);
//        info = new ClassTypeInfo();
//        info.name = "快餐";
//        info.imgId = R.drawable.icon_fast_food;
//        typeInfos.add(info);
//        return typeInfos;
//    }

    public List<SortInfo> getSortInfo() {
        SortInfo info = new SortInfo();
        info.id = 1;
        info.name = "智能排序";
        info.isSelect = true;
        sortInfos.add(info);
        info = new SortInfo();
        info.id = 2;
        info.name = "销量最高";
        sortInfos.add(info);
        info = new SortInfo();
        info.id = 3;
        info.name = "距离最近";
        sortInfos.add(info);
        info = new SortInfo();
        info.id = 4;
        info.name = "评分最高";
        sortInfos.add(info);
        info = new SortInfo();
        info.id = 5;
        info.name = "起送价最低";
        sortInfos.add(info);
        info = new SortInfo();
        info.id = 6;
        info.name = "送餐速度最快";
        sortInfos.add(info);
        return sortInfos;
    }

}
