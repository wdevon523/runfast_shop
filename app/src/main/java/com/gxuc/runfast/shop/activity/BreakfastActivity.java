package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.adapter.BreakfastAdapter;
import com.gxuc.runfast.shop.adapter.BreakfastSortAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.mainmiddle.ClassTypeInfos;
import com.gxuc.runfast.shop.adapter.BreakfastClassAdapter;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.SortInfo;
import com.gxuc.runfast.shop.bean.mainmiddle.ClassTypeInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.mainmiddle.MiddleSort;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 早餐
 */
public class BreakfastActivity extends ToolBarActivity implements View.OnClickListener, LoadMoreAdapter.LoadMoreApi, BGARefreshLayout.BGARefreshLayoutDelegate {

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
    BGARefreshLayout mRefreshLayout;

    private boolean isClass;

    private boolean isSort;

    private List<BusinessInfo> businessInfos = new ArrayList<>();

    private List<ClassTypeInfo> typeInfos = new ArrayList<>();
    private List<SortInfo> sortInfos = new ArrayList<>();


    private BreakfastClassAdapter adapterType;
    private BreakfastSortAdapter adapterSort;

    private int page = 1;

    private LoadMoreAdapter loadMoreAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);
        ButterKnife.bind(this);
        initData();
        initRefreshLayout();
        setData();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        tvToolbarTitle.setText(mSort.getTypename());
    }

    private void setData() {
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSort.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewClass.setAdapter(adapterType);
        recyclerViewSort.setAdapter(adapterSort);
        recyclerViewList.setAdapter(loadMoreAdapter);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        mSort = getIntent().getParcelableExtra("middleData");
        mName = mSort.getTypename();

        lat = Double.valueOf(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT));
        lon = Double.valueOf(SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON));
        if (!TextUtils.isEmpty(mName)) {
            tvClassName.setText(mName);
        }
        getSortInfo();

        adapterType = new BreakfastClassAdapter(typeInfos, this, this);
        adapterSort = new BreakfastSortAdapter(sortInfos, this, this);
        BreakfastAdapter breakfastAdapert = new BreakfastAdapter(businessInfos, this, this);
        loadMoreAdapter = new LoadMoreAdapter(this, breakfastAdapert);
        loadMoreAdapter.setLoadMoreListener(this);
        //getBusiness(lon, lat, page);
        searchGoods("黄");
    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        BGAMeiTuanRefreshViewHolder meiTuanRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(this, true);
        meiTuanRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        meiTuanRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        meiTuanRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
        mRefreshLayout.setRefreshViewHolder(meiTuanRefreshViewHolder);
    }

    /**
     * 获取商品分类
     */
    private void searchGoods(String name) {
        CustomApplication.getRetrofit().searchGoodsType(name).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealSearchGoods(data);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealSearchGoods(String data) {
        ClassTypeInfos classTypeInfo = GsonUtil.parseJsonWithGson(data, ClassTypeInfos.class);
        List<ClassTypeInfo> listTypeInfos = classTypeInfo.getBustype();
        ClassTypeInfo info = new ClassTypeInfo();
        info.id = "";
        info.name = "全部分类";
        info.isSelect = TextUtils.isEmpty(mName);
        info.imgId = R.drawable.icon_class_all;
        this.typeInfos.add(info);
        for (int i = 0; i < listTypeInfos.size(); i++) {
            info = new ClassTypeInfo();
            info.id = listTypeInfos.get(i).getId();
            info.name = listTypeInfos.get(i).getName();
            if (!TextUtils.isEmpty(mName) && TextUtils.equals(info.name, mName)) {
                info.isSelect = true;
                typeId = info.id;
            }
            info.imgId = R.drawable.icon_class_all;
            this.typeInfos.add(info);
        }
        page = 1;
        getBusinessType(page, 10, sortId, typeId);
        adapterType.notifyDataSetChanged();
    }

    /**
     * 分类选择
     */
    private void getBusinessType(int page, int raw, int sortId, String typeId) {
        //TODO 经纬度

        CustomApplication.getRetrofit().getBusinessType(page, raw, lon, lat, sortId, typeId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                dealSearchGoodsType(data);

            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealSearchGoodsType(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray bus = jsonObject.getJSONArray("bus");
            mTotalpage = jsonObject.optInt("totalpage");
            if (bus == null || bus.length() <= 0) {
                mRefreshLayout.endRefreshing();
                loadMoreAdapter.loadAllDataCompleted();
                return;
            }
            if (page == 1) {
                businessInfos.clear();
                loadMoreAdapter.resetLoadState();
            }

            int length = bus.length();
            for (int i = 0; i < length; i++) {
                JSONObject busObject = bus.getJSONObject(i);
                BusinessInfo info = new BusinessInfo();
                info.id = busObject.optInt("id");
                info.mini_imgPath = busObject.optString("mini_imgPath");
                info.imgPath = busObject.optString("imgPath");
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
                businessInfos.add(info);
            }
            loadMoreAdapter.loadCompleted();
            mRefreshLayout.endRefreshing();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    for (int i = 0; i < typeInfos.size(); i++) {
                        typeInfos.get(i).isSelect = false;
                    }
                    typeInfos.get(mPosition).isSelect = true;
                    adapterType.notifyDataSetChanged();
//                    if (mPosition == 0) {
//                        mName = mSort.getTypename();
//                    } else {
                    typeId = typeInfos.get(mPosition).id;
//                    }
                    tvClassName.setText(typeInfos.get(mPosition).name);
                    page = 1;
                    getBusinessType(page, 10, sortId, typeId);
                    uiHide();
                    break;
                case R.id.layout_sort_select:
                    mPositionSort = (Integer) v.getTag();
                    for (int i = 0; i < sortInfos.size(); i++) {
                        sortInfos.get(i).isSelect = false;
                    }
                    sortInfos.get(mPositionSort).isSelect = true;
                    adapterSort.notifyDataSetChanged();
                    sortId = sortInfos.get(mPosition).id;
                    tvSortName.setText(sortInfos.get(mPositionSort).name);
                    getBusinessType(page, 10, sortId, typeId);
                    uiHide();
                    break;
                case R.id.layout_breakfast_item:
                    Integer positionBusiness = (Integer) v.getTag();
                    Intent intent = new Intent(this, BusinessActivity.class);
                    intent.setFlags(IntentFlag.MAIN_BOTTOM_PAGE);
                    intent.putExtra("business", businessInfos.get(positionBusiness));
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

    @Override
    public void loadMore() {
        if (page < mTotalpage) {
            page += 1;
            getBusinessType(page, 10, sortId, typeId);
        } else {
            Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    loadMoreAdapter.loadAllDataCompleted();
                }
            };
            handler.post(r);

        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        getBusinessType(page, 10, sortId, typeId);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
