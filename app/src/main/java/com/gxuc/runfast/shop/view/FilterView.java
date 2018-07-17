package com.gxuc.runfast.shop.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.FilterAdaper;
import com.gxuc.runfast.shop.adapter.FilterSortAdapter;
import com.gxuc.runfast.shop.bean.FilterInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class FilterView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.tv_category_title)
    TextView tvCategoryTitle;
    @BindView(R.id.iv_category_arrow)
    ImageView ivCategoryArrow;
    @BindView(R.id.tv_sort_title)
    TextView tvSortTitle;
    @BindView(R.id.iv_sort_arrow)
    ImageView ivSortArrow;
    @BindView(R.id.tv_nearby_title)
    TextView tvNearbyTitle;
    @BindView(R.id.tv_filter_title)
    TextView tvFilterTitle;
    @BindView(R.id.iv_filter_arrow)
    ImageView ivFilterArrow;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.ll_sort)
    LinearLayout llSort;
    @BindView(R.id.ll_nearby)
    LinearLayout llNearby;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.rl_filter_content)
    RelativeLayout rlFilterContent;
    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.gv_business_feature)
    GridView gvBusinessFeature;
    @BindView(R.id.gv_business_act)
    GridView gvBusinessAct;
    @BindView(R.id.gv_business_category)
    GridView gvBusinessCategory;
    @BindView(R.id.lv_right)
    ListView lvRight;
    @BindView(R.id.ll_head_layout)
    LinearLayout llHeadLayout;
    @BindView(R.id.ll_content_list_view)
    LinearLayout llContentListView;
    @BindView(R.id.view_mask_bg)
    View viewMaskBg;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private Context mContext;
    private Activity mActivity;

    private int filterPosition = -1;
    private int lastFilterPosition = -1;
    public static final int POSITION_CATEGORY = 0; // 综合的位置
    public static final int POSITION_SORT = 1; // 销量的位置
    public static final int POSITION_DISTANCE = 2; // 距离的位置
    public static final int POSITION_FILTER = 3; // 筛选的位置

    private boolean isShowing = false;
    private int panelHeight;
    private ArrayList<FilterInfo> filterInfoFeatureList;
    private ArrayList<FilterInfo> filterInfoActList;
    private ArrayList<FilterInfo> filterSortList;
    private FilterSortAdapter sortAdapter;
    private FilterAdaper filterFeatureAdaper;
    private FilterAdaper filterActAdaper;
    //    private FilterData filterData;

//    private FilterLeftAdapter leftAdapter;
//    private FilterRightAdapter rightAdapter;
//    private FilterOneAdapter sortAdapter;
//    private FilterOneAdapter filterAdapter;
//
//    private FilterTwoEntity leftSelectedCategoryEntity; // 被选择的分类项左侧数据
//    private FilterEntity rightSelectedCategoryEntity; // 被选择的分类项右侧数据
//    private FilterEntity selectedSortEntity; // 被选择的排序项
//    private FilterEntity selectedFilterEntity; // 被选择的筛选项

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        filterSortList = new ArrayList<>();
        FilterInfo filterSort = new FilterInfo();
        filterSort.name = "综合排序";
        filterSort.type = 1;
        filterSort.isCheck = true;
        FilterInfo filterSort1 = new FilterInfo();
        filterSort1.name = "速度最快";
        filterSort1.type = 6;
        filterSort1.isCheck = false;
        FilterInfo filterSort2 = new FilterInfo();
        filterSort2.name = "评分最高";
        filterSort2.type = 4;
        filterSort2.isCheck = false;
        FilterInfo filterSort3 = new FilterInfo();
        filterSort3.name = "起送价最低";
        filterSort3.type = 5;
        filterSort3.isCheck = false;
        FilterInfo filterSort4 = new FilterInfo();
        filterSort4.name = "配送费最低";
        filterSort4.type = 7;
        filterSort4.isCheck = false;
        filterSortList.add(filterSort);
        filterSortList.add(filterSort1);
        filterSortList.add(filterSort2);
        filterSortList.add(filterSort3);
        filterSortList.add(filterSort4);

        filterInfoFeatureList = new ArrayList<>();
        FilterInfo filterInfoFeature = new FilterInfo();
        filterInfoFeature.name = "快车专送";
        filterInfoFeature.type = 0;
        FilterInfo filterInfoFeature1 = new FilterInfo();
        filterInfoFeature1.name = "商家配送";
        filterInfoFeature1.type = 1;
        FilterInfo filterInfoFeature2 = new FilterInfo();
        filterInfoFeature2.name = "支持自取";
        filterInfoFeature2.type = 2;
        FilterInfo filterInfoFeature3 = new FilterInfo();
        filterInfoFeature3.name = "金牌商家";
        filterInfoFeature3.type = 3;
        FilterInfo filterInfoFeature4 = new FilterInfo();
        filterInfoFeature4.name = "新商家";
        filterInfoFeature4.type = 4;
        FilterInfo filterInfoFeature5 = new FilterInfo();
        filterInfoFeature5.name = "0元起送";
        filterInfoFeature5.type = 5;
        filterInfoFeatureList.add(filterInfoFeature);
        filterInfoFeatureList.add(filterInfoFeature1);
        filterInfoFeatureList.add(filterInfoFeature2);
        filterInfoFeatureList.add(filterInfoFeature3);
        filterInfoFeatureList.add(filterInfoFeature4);
        filterInfoFeatureList.add(filterInfoFeature5);

        filterInfoActList = new ArrayList<>();
        FilterInfo filterInfoAct = new FilterInfo();
        filterInfoAct.name = "满减优惠";
        filterInfoAct.type = 1;
        FilterInfo filterInfoAct1 = new FilterInfo();
        filterInfoAct1.name = "折扣商品";
        filterInfoAct1.type = 2;
        FilterInfo filterInfoAct2 = new FilterInfo();
        filterInfoAct2.name = "免配送费";
        filterInfoAct2.type = 5;
        FilterInfo filterInfoAct3 = new FilterInfo();
        filterInfoAct3.name = "首单立减";
        filterInfoAct3.type = 8;
        FilterInfo filterInfoAct4 = new FilterInfo();
        filterInfoAct4.name = "进店领券";
        filterInfoAct4.type = 10;
        FilterInfo filterInfoAct5 = new FilterInfo();
        filterInfoAct5.name = "优惠商家";
        filterInfoAct5.type = 0;

        filterInfoActList.add(filterInfoAct);
        filterInfoActList.add(filterInfoAct1);
        filterInfoActList.add(filterInfoAct2);
        filterInfoActList.add(filterInfoAct3);
        filterInfoActList.add(filterInfoAct4);
        filterInfoActList.add(filterInfoAct5);

    }

    private void initView() {
        viewMaskBg.setVisibility(GONE);
        llContentListView.setVisibility(GONE);
        rlFilterContent.setVisibility(GONE);
        sortAdapter = new FilterSortAdapter(mContext, filterSortList);
        lvRight.setAdapter(sortAdapter);
    }

    private void initListener() {
        llCategory.setOnClickListener(this);
        llSort.setOnClickListener(this);
        llNearby.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        llContentListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_category:
                filterPosition = 0;
                changeBold(0);
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_sort:
                filterPosition = 1;
                changeBold(1);
                tvCategoryTitle.setText("综合排序");
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_nearby:
                filterPosition = 2;
                changeBold(2);
                tvCategoryTitle.setText("综合排序");
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;

            case R.id.ll_filter:
                filterPosition = 3;
                changeBold(3);
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.view_mask_bg:
                hide();
                break;
            case R.id.tv_clear:
                for (int i = 0; i < filterInfoFeatureList.size(); i++) {
                    filterInfoFeatureList.get(i).isCheck = false;
                }
                for (int i = 0; i < filterInfoActList.size(); i++) {
                    filterInfoActList.get(i).isCheck = false;
                }
                filterFeatureAdaper.notifyDataSetChanged();
                filterActAdaper.notifyDataSetChanged();
                break;
            case R.id.tv_sure:
                if (onFiltrateClickListener != null) {
                    onFiltrateClickListener.onFiltrateClick(filterInfoFeatureList, filterInfoActList);
                }
                hide();
                break;
        }
    }

    // 复位筛选的显示状态
    public void resetViewStatus() {
        tvCategoryTitle.setTextColor(mContext.getResources().getColor(R.color.text_333333));
        ivCategoryArrow.setImageResource(R.drawable.icon_down_arrow);

        tvSortTitle.setTextColor(mContext.getResources().getColor(R.color.text_333333));
        ivSortArrow.setImageResource(R.drawable.icon_down_arrow);

        tvFilterTitle.setTextColor(mContext.getResources().getColor(R.color.text_333333));
//        ivFilterArrow.setImageResource(R.drawable.icon_down_arrow);
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetViewStatus();
        hide();
    }

    // 设置分类数据
    private void setCategoryAdapter() {
        lvLeft.setVisibility(GONE);
        rlFilterContent.setVisibility(GONE);
        llContentListView.setVisibility(VISIBLE);
        lvRight.setVisibility(VISIBLE);
        sortAdapter.setList(filterSortList);

        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterInfo filterInfo = filterSortList.get(position);
                tvCategoryTitle.setText(filterInfo.name);
                for (int i = 0; i < filterSortList.size(); i++) {
                    if (i == position) {
                        filterSortList.get(i).isCheck = true;
                    } else {
                        filterSortList.get(i).isCheck = false;
                    }
                }
                if (onItemCategoryClickListener != null) {
                    onItemCategoryClickListener.onItemCategoryClick(filterInfo);
                }
                hide();
            }
        });
        // 左边列表视图
//        leftAdapter = new FilterLeftAdapter(mContext, filterData.getCategory());
//        lvLeft.setAdapter(leftAdapter);
//        if (leftSelectedCategoryEntity == null) {
//            leftSelectedCategoryEntity = filterData.getCategory().get(0);
//        }
//        leftAdapter.setSelectedEntity(leftSelectedCategoryEntity);
//
//        lvLeft.setOnItemClickListener(new AdapterView.OnCheckListener() {
//            @Override
//            public void onChecked(AdapterView<?> parent, View view, int position, long id) {
//                leftSelectedCategoryEntity = filterData.getCategory().get(position);
//                leftAdapter.setSelectedEntity(leftSelectedCategoryEntity);
//
//                // 右边列表视图
//                rightAdapter = new FilterRightAdapter(mContext, leftSelectedCategoryEntity.getList());
//                lvRight.setAdapter(rightAdapter);
//                rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
//            }
//        });
//
//        // 右边列表视图
//        rightAdapter = new FilterRightAdapter(mContext, leftSelectedCategoryEntity.getList());
//        lvRight.setAdapter(rightAdapter);
//        rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
//        lvRight.setOnItemClickListener(new AdapterView.OnCheckListener() {
//            @Override
//            public void onChecked(AdapterView<?> parent, View view, int position, long id) {
//                rightSelectedCategoryEntity = leftSelectedCategoryEntity.getList().get(position);
//                rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
//                if (onItemCategoryClickListener != null) {
//                    onItemCategoryClickListener.onItemCategoryClick(leftSelectedCategoryEntity, rightSelectedCategoryEntity);
//                }
//                hide();
//            }
//        });
    }

    // 设置排序数据
    private void setSortAdapter() {
//        lvLeft.setVisibility(GONE);
//        rlFilterContent.setVisibility(GONE);
//        lvRight.setVisibility(VISIBLE);
//
//        sortAdapter = new FilterSortAdapter(mContext, filterSortList);
//        lvRight.setAdapter(sortAdapter);
//
//        lvRight.setOnItemClickListener(new AdapterView.OnCheckListener() {
//            @Override
//            public void onChecked(AdapterView<?> parent, View view, int position, long id) {
//                selectedSortEntity = filterData.getSorts().get(position);
//                sortAdapter.setSelectedEntity(selectedSortEntity);
//                if (onItemSortClickListener != null) {
//                    onItemSortClickListener.onItemSortClick(selectedSortEntity);
//                }
//                hide();
//            }
//        });
    }

    // 设置筛选数据
    private void setFilterAdapter() {
        lvLeft.setVisibility(GONE);
        lvRight.setVisibility(GONE);
        rlFilterContent.setVisibility(VISIBLE);
        filterFeatureAdaper = new FilterAdaper(mContext, filterInfoFeatureList);
        gvBusinessFeature.setAdapter(filterFeatureAdaper);

        filterActAdaper = new FilterAdaper(mContext, filterInfoActList);
        gvBusinessAct.setAdapter(filterActAdaper);


        gvBusinessFeature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterInfoFeatureList.get(position).isCheck = !filterInfoFeatureList.get(position).isCheck;
                if (position == 0 && filterInfoFeatureList.get(0).isCheck) {
                    filterInfoFeatureList.get(1).isCheck = false;
                }

                if (position == 1 && filterInfoFeatureList.get(1).isCheck) {
                    filterInfoFeatureList.get(0).isCheck = false;
                }
                filterFeatureAdaper.notifyDataSetChanged();
            }
        });

        gvBusinessAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterInfoActList.get(position).isCheck = !filterInfoActList.get(position).isCheck;
                filterActAdaper.notifyDataSetChanged();
            }
        });

//        filterAdapter = new FilterOneAdapter(mContext, filterData.getFilters());
//        lvRight.setAdapter(filterAdapter);
//
//        lvRight.setOnItemClickListener(new AdapterView.OnCheckListener() {
//            @Override
//            public void onChecked(AdapterView<?> parent, View view, int position, long id) {
//                selectedFilterEntity = filterData.getFilters().get(position);
//                filterAdapter.setSelectedEntity(selectedFilterEntity);
//                if (onItemFilterClickListener != null) {
//                    onItemFilterClickListener.onItemFilterClick(selectedFilterEntity);
//                }
//                hide();
//            }
//        });
    }

    // 动画显示
    public void show(int position) {
        if (isShowing && lastFilterPosition == position) {
            hide();
            return;
        } else if (!isShowing) {
            viewMaskBg.setVisibility(VISIBLE);
//            llContentListView.setVisibility(VISIBLE);
//            llContentListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    llContentListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    panelHeight = llContentListView.getHeight();
//                    ObjectAnimator.ofFloat(llContentListView, "translationY", -panelHeight, 0).setDuration(200).start();
//                }
//            });
        }
        isShowing = true;
        resetViewStatus();
        rotateArrowUp(position);
        rotateArrowDown(lastFilterPosition);
        lastFilterPosition = position;

        switch (position) {
            case POSITION_CATEGORY:
//                tvCategoryTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                ivCategoryArrow.setImageResource(R.drawable.icon_down_arrow);
                setCategoryAdapter();
                break;
            case POSITION_SORT:
//                tvSortTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//                ivSortArrow.setImageResource(R.drawable.icon_down_arrow);
//                setSortAdapter();
                break;
            case POSITION_DISTANCE:
//                tvSortTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//                ivSortArrow.setImageResource(R.drawable.icon_down_arrow);
//                setSortAdapter();
                break;
            case POSITION_FILTER:
//                tvFilterTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//                ivFilterArrow.setImageResource(R.drawable.icon_down_arrow);
                setFilterAdapter();
                break;
        }
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetViewStatus();
        rotateArrowDown(filterPosition);
        rotateArrowDown(lastFilterPosition);
        filterPosition = -1;
        lastFilterPosition = -1;
        viewMaskBg.setVisibility(View.GONE);
        llContentListView.setVisibility(View.GONE);
        rlFilterContent.setVisibility(View.GONE);
        if (onHideListener != null) {
            onHideListener.onHide();
        }

//        ObjectAnimator.ofFloat(llContentListView, "translationY", 0, -panelHeight).setDuration(200).start();
    }

    // 设置筛选数据
//    public void setFilterData(Activity activity, FilterData filterData) {
//        this.mActivity = activity;
//        this.filterData = filterData;
//    }

    public void changeBold(int position) {
        tvCategoryTitle.setTypeface(position == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        tvSortTitle.setTypeface(position == 1 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        tvNearbyTitle.setTypeface(position == 2 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }


    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }

    public int getFilterPosition() {
        return filterPosition;
    }

    // 旋转箭头向上
    private void rotateArrowUp(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowUpAnimation(ivCategoryArrow);
                break;
//            case POSITION_SORT:
//                rotateArrowUpAnimation(ivSortArrow);
//                break;
//            case POSITION_FILTER:
//                rotateArrowUpAnimation(ivFilterArrow);
//                break;
        }
    }

    // 旋转箭头向下
    private void rotateArrowDown(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowDownAnimation(ivCategoryArrow);
                break;
//            case POSITION_SORT:
//                rotateArrowDownAnimation(ivSortArrow);
//                break;
//            case POSITION_FILTER:
//                rotateArrowDownAnimation(ivFilterArrow);
//                break;
        }
    }

    // 旋转箭头向上
    public static void rotateArrowUpAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    // 旋转箭头向下
    public static void rotateArrowDownAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    // 筛选视图点击
    private OnFilterClickListener onFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    // 分类Item点击
    private OnItemCategoryClickListener onItemCategoryClickListener;

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    public interface OnItemCategoryClickListener {
        void onItemCategoryClick(FilterInfo filterInfo);
    }

    public void setTvCategoryTitle(FilterInfo filterInfo) {
        tvCategoryTitle.setText(filterInfo.name);
        tvCategoryTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    // 分类Item点击
    private OnFiltrateClickListener onFiltrateClickListener;

    public void setOnFiltrateClickListener(OnFiltrateClickListener onFiltrateClickListener) {
        this.onFiltrateClickListener = onFiltrateClickListener;
    }

    public interface OnFiltrateClickListener {
        void onFiltrateClick(ArrayList<FilterInfo> filterInfoFeatureList, ArrayList<FilterInfo> filterInfoActList);
    }

    // 分类Item点击
    private OnHideListener onHideListener;

    public void setOnHideListener(OnHideListener onHideListener) {
        this.onHideListener = onHideListener;
    }

    public interface OnHideListener {
        void onHide();
    }

//
//    // 排序Item点击
//    private OnItemSortClickListener onItemSortClickListener;
//    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
//        this.onItemSortClickListener = onItemSortClickListener;
//    }
//    public interface OnItemSortClickListener {
//        void onItemSortClick(FilterEntity entity);
//    }
//
//    // 筛选Item点击
//    private OnItemFilterClickListener onItemFilterClickListener;
//    public void setOnItemFilterClickListener(OnItemFilterClickListener onItemFilterClickListener) {
//        this.onItemFilterClickListener = onItemFilterClickListener;
//    }
//    public interface OnItemFilterClickListener {
//        void onItemFilterClick(FilterEntity entity);
//    }

}
