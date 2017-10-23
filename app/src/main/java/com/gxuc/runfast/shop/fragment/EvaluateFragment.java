package com.gxuc.runfast.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.adapter.shopcaradater.EvaluateAdapter;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.adapter.LoadMoreAdapter;
import com.gxuc.runfast.shop.bean.EvaluateInfo;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 评价
 * A simple {@link Fragment} subclass.
 */
public class EvaluateFragment extends LazyFragment implements Callback<String>,LoadMoreAdapter.LoadMoreApi {


    RecyclerView recycler;

    Unbinder unbinder;

    private List<EvaluateInfo> evaluateInfos = new ArrayList<>();
    private LoadMoreAdapter loadMoreAdapter;

    public EvaluateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_evaluate);
        initView();
        initData();
    }

    private void initView() {
        Log.d("initView","EvaluateFragment");
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    private void initData() {

        EvaluateAdapter adapter = new EvaluateAdapter(getContext(),evaluateInfos);
        loadMoreAdapter = new LoadMoreAdapter(getContext(), adapter);
        loadMoreAdapter.setLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(loadMoreAdapter);
        getBusinessEvaluate(((BusinessActivity) getActivity()).getBusinessId());
    }

    /**
     * 获取商家评价
     */
    private void getBusinessEvaluate(int id){
        CustomApplication.getRetrofit().getBusinessEvaluate(id,1).enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        String data = response.body();
        if (response.isSuccessful()) {
            ResolveData(data);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }

    /**
     * 解析数据
     *
     * @param data
     */
    private void ResolveData(String data) {
        try {
            JSONObject object = new JSONObject(data);
            JSONArray rows = object.getJSONArray("rows");
            JSONObject map = object.getJSONObject("map");
            int length = rows.length();

            EvaluateInfo evaluateInfo = new EvaluateInfo();
            evaluateInfo.evaluateNum = map.optInt("total");
            evaluateInfo.zb = map.optString("zb");
            evaluateInfos.add(evaluateInfo);
            if (length <= 0){
                return;
            }
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                if (jsonObject != null) {
                    EvaluateInfo evaluate = GsonUtil.parseJsonWithGson(jsonObject.toString(), EvaluateInfo.class);
                    evaluateInfos.add(evaluate);
                }
            }
            loadMoreAdapter.loadAllDataCompleted();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMore() {

    }
}
