package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gxuc.runfast.shop.adapter.moneyadapter.ScoreRecordAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.score.MyScore;
import com.gxuc.runfast.shop.bean.score.MyScores;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.util.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 积分
 */
public class IntegralActivity extends ToolBarActivity implements View.OnClickListener, LoadMoreAdapter.LoadMoreApi {

    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.recycler_integral_detail)
    RecyclerView recyclerView;

    private List<MyScore> mMyScores;
    private User userInfo;
    private LoadMoreAdapter moreAdapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        ButterKnife.bind(this);
        initData();
        getDataList();
    }

    /**
     * 积分明细获取
     */
    private void getDataList() {
        userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        String score = String.valueOf(userInfo.getScore());
        if (score.contains(".")) {
            score = score.substring(0, score.indexOf("."));
        }
        tvIntegral.setText(score);
        CustomApplication.getRetrofit().getListScore(page, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealScoreList(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealScoreList(String body) {
        if (!TextUtils.isEmpty(body)) {
            MyScores scores = GsonUtil.parseJsonWithGson(body, MyScores.class);
            if (scores.getRows() != null && scores.getRows().size() > 0) {
                mMyScores.addAll(scores.getRows());
                moreAdapter.loadCompleted();
            } else {
                moreAdapter.loadAllDataCompleted();
            }
        }
    }

    private void initData() {
        mMyScores = new ArrayList<>();
        ScoreRecordAdapter mAllAdapter = new ScoreRecordAdapter(mMyScores, this, this);
        moreAdapter = new LoadMoreAdapter(this, mAllAdapter);
        moreAdapter.setLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(moreAdapter);
    }

    @OnClick(R.id.layout_integral_rule)
    public void onViewClicked() {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadMore() {
        page += 1;
        getDataList();
    }
}
