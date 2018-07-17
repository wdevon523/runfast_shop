package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.adapter.EnshrineAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class MyEnshrineActivity extends ToolBarActivity {

    @BindView(R.id.recycler_my_enshrine)
    RecyclerView recyclerViewList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private EnshrineAdapter mEnshrineAdapter;
    private ArrayList<BusinessNewDetail> businessDetailList;
    private UserInfo userInfo;
    private String lat;
    private String lon;
    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enshrine);
        ButterKnife.bind(this);
        initData();
        refreshData();
    }

    private void getEnshrineData() {
        if (userInfo == null) {
            return;
        }

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }


        CustomApplication.getRetrofitNew().getFavoriteList(lon, lat, currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealEnshrine(jsonObject.optJSONArray("data"));
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

    private void dealEnshrine(JSONArray data) {

        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        ArrayList<BusinessNewDetail> businessList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<BusinessNewDetail>>() {
        }.getType());
        businessDetailList.addAll(businessList);
        mEnshrineAdapter.setList(businessDetailList);
    }


    private void initData() {
        userInfo = UserService.getUserInfo(this);
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);

        businessDetailList = new ArrayList<>();
        mEnshrineAdapter = new EnshrineAdapter(businessDetailList, this);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewList.setAdapter(mEnshrineAdapter);

        mEnshrineAdapter.setOnItemClickListener(new EnshrineAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, BusinessNewDetail enshrine) {
                Intent intent = new Intent(MyEnshrineActivity.this, BusinessNewActivity.class);
                intent.putExtra(IntentFlag.KEY, IntentFlag.ORDER_LIST);
                intent.putExtra("businessId", enshrine.id);
                startActivity(intent);
            }
        });

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
                        getEnshrineData();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        getEnshrineData();
    }

    private void clearRecyclerViewData() {
        businessDetailList.clear();
        mEnshrineAdapter.setList(businessDetailList);
    }


//    private void initRefreshLayout() {
//        // 为BGARefreshLayout 设置代理
//        mRefreshLayout.setDelegate(this);
//        BGAMeiTuanRefreshViewHolder meiTuanRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(this, true);
//        meiTuanRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
//        meiTuanRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
//        meiTuanRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
//        mRefreshLayout.setRefreshViewHolder(meiTuanRefreshViewHolder);
//    }

}
