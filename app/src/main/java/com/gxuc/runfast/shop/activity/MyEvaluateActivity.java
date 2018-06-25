package com.gxuc.runfast.shop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class MyEvaluateActivity extends ToolBarActivity {

    @BindView(R.id.home_recycleview)
    RecyclerView homeRecycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluate);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {


    }

    private void initData() {

        requestMyEvaluate();
    }

    private void requestMyEvaluate() {

        CustomApplication.getRetrofitNew().getMyEvaluate().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });

    }
}
