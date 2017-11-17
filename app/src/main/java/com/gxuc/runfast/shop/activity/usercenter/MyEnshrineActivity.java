package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.adapter.AddressSelectAdapter;
import com.gxuc.runfast.shop.adapter.EnshrineAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.enshrien.Enshrine;
import com.gxuc.runfast.shop.bean.enshrien.Enshrines;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

public class MyEnshrineActivity extends ToolBarActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, LoadMoreAdapter.LoadMoreApi {

    @BindView(R.id.recycler_my_enshrine)
    RecyclerView recyclerViewList;
    @BindView(R.id.rl_my_enshrine)
    BGARefreshLayout mRefreshLayout;

    private LoadMoreAdapter loadMoreAdapter;
    private List<Enshrine> mEnshrines;
    private List<BusinessInfo> mBusinessInfos;
    private int page = 1;
    private EnshrineAdapter mEnshrineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enshrine);
        ButterKnife.bind(this);
        initData();
        getEnshrineData();
        initRefreshLayout();
        setData();
    }

    private void getEnshrineData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo != null) {
            CustomApplication.getRetrofit().getEnshrine(page).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    dealEnshrine(response.body());
                    mRefreshLayout.endRefreshing();
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {
                    mRefreshLayout.endRefreshing();
                }
            });
        }
    }

    private void dealEnshrine(String body) {
        if (body != null) {
            Enshrines enshrines = GsonUtil.parseJsonWithGson(body, Enshrines.class);
            if (enshrines == null || enshrines.getEnshrine() == null || enshrines.getEnshrine().size() <= 0) {
                loadMoreAdapter.loadAllDataCompleted();
                return;
            }
            mEnshrines.addAll(enshrines.getEnshrine());
            loadMoreAdapter.loadAllDataCompleted();
            mRefreshLayout.endRefreshing();
        }
    }


    private void initData() {
        mEnshrines = new ArrayList<>();
        mBusinessInfos = new ArrayList<>();
        mEnshrineAdapter = new EnshrineAdapter(mEnshrines, this);
        mEnshrineAdapter.setOnItemClickListener(new EnshrineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Enshrine enshrine) {
                Intent intent = new Intent(MyEnshrineActivity.this, BusinessActivity.class);
                intent.setFlags(IntentFlag.ORDER_LIST);
                intent.putExtra("orderInfo", enshrine.shopId);
                startActivity(intent);
            }
        });
        loadMoreAdapter = new LoadMoreAdapter(this, mEnshrineAdapter);
        loadMoreAdapter.setLoadMoreListener(this);
    }

    private void setData() {
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewList.setAdapter(loadMoreAdapter);
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mEnshrines.clear();
        getEnshrineData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void loadMore() {
        page += 1;
        getEnshrineData();
    }

}
