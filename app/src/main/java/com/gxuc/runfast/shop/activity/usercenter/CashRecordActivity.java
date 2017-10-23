package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gxuc.runfast.shop.adapter.CashRecordAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.spend.AccountRecords;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.bean.spend.AccountRecord;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现记录
 */
public class CashRecordActivity extends ToolBarActivity implements View.OnClickListener, Callback<String> {

    @BindView(R.id.view_money_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_no_cash_record)
    TextView mTvNoCashRecord;

    private List<AccountRecord> mRecordList;
    private CashRecordAdapter mAllAdapter;

    private int page =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);
        ButterKnife.bind(this);
        initData();
        getNetData();
    }

    private void getNetData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().getCashRecord(page).enqueue(this);
    }

    private void initData() {
        mRecordList = new ArrayList<>();
        mAllAdapter = new CashRecordAdapter(mRecordList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAllAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        String data = response.body();
        if (response.isSuccessful()) {
            Log.d("params","response = "+data);
            ResolveData(data);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        CustomToast.INSTANCE.showToast(this, "网络错误");
    }


    /**
     * 解析数据
     *
     * @param data
     */
    private void ResolveData(String data) {
        AccountRecords accountRecords = GsonUtil.parseJsonWithGson(data, AccountRecords.class);
        if (accountRecords != null && accountRecords.getRows().size() > 0) {
            mRecordList.addAll(accountRecords.getRows());
            mTvNoCashRecord.setVisibility(View.GONE);
            mAllAdapter.notifyDataSetChanged();
        } else {
            mTvNoCashRecord.setVisibility(View.VISIBLE);
        }
    }
}