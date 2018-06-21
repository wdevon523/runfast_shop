package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.adapter.moneyadapter.ScoreRecordAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.score.MyScore;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 积分
 */
public class IntegralActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycler_integral_detail)
    RecyclerView recyclerView;

    private UserInfo userInfo;
    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;
    private ScoreRecordAdapter mAllAdapter;
    private ArrayList<MyScore> myScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        ButterKnife.bind(this);
        initData();
        refreshData();
    }

    /**
     * 积分明细获取
     */
    private void getDataList() {
        userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        String score = String.valueOf(userInfo.score);
        if (score.contains(".")) {
            score = score.substring(0, score.indexOf("."));
        }
        tvIntegral.setText(score);

        CustomApplication.getRetrofitNew().getScoreList(currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealScoreList(jsonObject.optJSONArray("data"));
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

    private void dealScoreList(JSONArray data) {

        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        ArrayList<MyScore> scoreList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<MyScore>>() {
        }.getType());
        myScoreList.addAll(scoreList);
        mAllAdapter.setList(myScoreList);
    }

    private void initData() {
        myScoreList = new ArrayList<>();
        mAllAdapter = new ScoreRecordAdapter(myScoreList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAllAdapter);

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
                        getDataList();
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
        getDataList();
    }

    private void clearRecyclerViewData() {
        myScoreList.clear();
        mAllAdapter.setList(myScoreList);
    }


    @OnClick(R.id.layout_integral_rule)
    public void onViewClicked() {
    }

    @Override
    public void onClick(View v) {

    }

}
