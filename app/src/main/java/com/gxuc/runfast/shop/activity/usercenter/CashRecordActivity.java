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
import com.gxuc.runfast.shop.adapter.CashRecordAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.spend.WithdrawalRecord;
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
import retrofit2.Call;
import retrofit2.Response;

/**
 * 提现记录
 */
public class CashRecordActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_money_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_no_cash_record)
    TextView mTvNoCashRecord;

    private ArrayList<WithdrawalRecord> mRecordList;
    private CashRecordAdapter mAllAdapter;

    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);
        ButterKnife.bind(this);
        initData();
        getNetData();
    }

    private void getNetData() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getCashRecord(currentPage, 10).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealCashRecord(jsonObject.optJSONArray("data"));
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

    private void dealCashRecord(JSONArray data) {
        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        ArrayList<WithdrawalRecord> WithdrawalRecordList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<WithdrawalRecord>>() {
        }.getType());
        mRecordList.addAll(WithdrawalRecordList);
        mAllAdapter.setList(mRecordList);
        mTvNoCashRecord.setVisibility(mRecordList.size() > 0 ? View.GONE : View.VISIBLE);

    }

    private void initData() {
        mRecordList = new ArrayList<>();
        mAllAdapter = new CashRecordAdapter(mRecordList, this, this);
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
                        getNetData();
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
        getNetData();
    }

    private void clearRecyclerViewData() {
        mRecordList.clear();
        mAllAdapter.setList(mRecordList);
    }


    @Override
    public void onClick(View v) {

    }
}