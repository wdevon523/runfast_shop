package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.adapter.BreakfastAdapter;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.SearchBusinessInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.ZFlowLayout;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.example.supportv1.utils.SharedPreferencesUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

public class SearchProductActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.et_search_name)
    EditText etSearchName;
    @BindView(R.id.flow_layout)
    ZFlowLayout flowLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_search_list)
    RecyclerView recyclerViewList;
    @BindView(R.id.ll_search_history)
    LinearLayout search_history;
    @BindView(R.id.iv_search_back)
    ImageView mIvSearchBack;
    @BindView(R.id.tv_cancel_search)
    TextView mTvCancelSearch;


    private List<String> data = new ArrayList<>();
    private List<SearchBusinessInfo> businessInfos = new ArrayList<>();
    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private SharedPreferencesUtil mUtil;
    private String SEARCH_HISTORY = "search_history";
    private String mSearch;
    private String lat;
    private String lon;
    private BreakfastAdapter breakfastAdapert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ButterKnife.bind(this);
        initData();
        updateData();
    }

    private void initData() {

        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);

        mUtil = new SharedPreferencesUtil(this, SEARCH_HISTORY);
        smartRefreshLayout.setVisibility(View.GONE);
        breakfastAdapert = new BreakfastAdapter(businessInfos, this, this);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewList.setAdapter(breakfastAdapert);

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
                        searchGoods(mSearch);
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });
    }

    @SuppressWarnings("ResourceType")
    public void addItem(final List<String> data) {
        flowLayout.removeAllViews();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 10, 10);// 设置边距

        for (int i = 0; i < data.size(); i++) {
            final TextView textView = new TextView(this);
            textView.setText(data.get(i));
            textView.setBackgroundResource(R.drawable.search_history_background);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(getResources().getColor(R.color.color_text_gray_two));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer) textView.getTag();
                    mSearch = data.get(tag);
                    etSearchName.setText(mSearch);
                    searchGoods(mSearch);
                }
            });
            flowLayout.addView(textView, layoutParams);
        }
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        searchGoods(mSearch);
    }

    private void clearRecyclerViewData() {
        businessInfos.clear();
        breakfastAdapert.setList(businessInfos);
    }

    /**
     * 搜索商品
     */
    private void searchGoods(String name) {

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        String agantId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.AGENTID);
        CustomProgressDialog.startProgressDialog(this);
        CustomApplication.getRetrofitNew().searchGoods(currentPage, 10, lon, lat, name, 1, agantId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                CustomProgressDialog.stopProgressDialog();
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealSearchGoods(jsonObject.optString("data"));
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

    private void dealSearchGoods(String data) {
//        if (TextUtils.isEmpty(data)) {
//            isLastPage = true;
//            return;
//        }

        ArrayList<SearchBusinessInfo> searchBusinessInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<SearchBusinessInfo>>() {
        }.getType());

        if (searchBusinessInfoList == null || searchBusinessInfoList.size() == 0) {
            ToastUtil.showToast(R.string.load_all_date);
            isLastPage = true;
            return;
        }

        search_history.setVisibility(View.GONE);
        smartRefreshLayout.setVisibility(View.VISIBLE);
        businessInfos.addAll(searchBusinessInfoList);
        breakfastAdapert.setList(businessInfos);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layout_breakfast_item:
        Integer positionBusiness = (Integer) v.getTag();
        Intent intent = new Intent(this, BusinessNewActivity.class);
        intent.putExtra(IntentFlag.KEY, IntentFlag.SEARCH_VIEW);
        intent.putExtra("businessId", businessInfos.get(positionBusiness).id);
        startActivity(intent);
//                break;
//        }
    }

    @OnClick({R.id.iv_search_back, R.id.tv_cancel_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.tv_cancel_search://搜索
                businessInfos.clear();
                mSearch = etSearchName.getText().toString().trim();
                if (!TextUtils.isEmpty(mSearch)) {
                    saveHistory(mSearch);
                    searchGoods(mSearch);
                } else {
                    ToastUtil.showToast("请输入搜索内容");
                }
                break;
        }
    }

    /**
     * 保存历史
     *
     * @param text
     */
    private void saveHistory(String text) {
        String oldText = (String) mUtil.getData(SEARCH_HISTORY);
        if (!TextUtils.isEmpty(text) && !oldText.contains(text) && !TextUtils.isEmpty(oldText)) {
            mUtil.setData(SEARCH_HISTORY, oldText + "," + text);
        } else if (TextUtils.isEmpty(oldText)) {
            mUtil.setData(SEARCH_HISTORY, text);
        }
    }

    /**
     * 更新布局
     */
    public void updateData() {
        String history = (String) mUtil.getData(SEARCH_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            String[] split = history.split(",");
            Collections.addAll(data, split);
            addItem(data);
        }
    }
}
