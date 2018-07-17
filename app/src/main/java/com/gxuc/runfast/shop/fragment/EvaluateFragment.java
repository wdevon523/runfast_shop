package com.gxuc.runfast.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.adapter.shopcaradater.EvaluateAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessEvaluationInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.shizhefei.fragment.LazyFragment;
import com.willy.ratingbar.BaseRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 评价
 * A simple {@link Fragment} subclass.
 */
public class EvaluateFragment extends LazyFragment {


    RecyclerView recycler;

    TextView tvScore;
    TextView tvUserScore;
    TextView tvUserNum;

    Unbinder unbinder;

    private List<BusinessEvaluationInfo> businessEvaluationInfoList = new ArrayList<>();
    private EvaluateAdapter evaluateAdapter;
    private BaseRatingBar rbTaste;
    private BaseRatingBar rbPackage;
    private TextView tvTaste;
    private TextView tvPackage;
    //    private LoadMoreAdapter loadMoreAdapter;

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
//        Log.d("initView", "EvaluateFragment");
        recycler = (RecyclerView) findViewById(R.id.recycler);
        tvScore = (TextView) findViewById(R.id.tv_score);
        rbTaste = (BaseRatingBar) findViewById(R.id.rb_taste);
        tvTaste = (TextView) findViewById(R.id.tv_taste);
        rbPackage = (BaseRatingBar) findViewById(R.id.rb_package);
        tvPackage = (TextView) findViewById(R.id.tv_package);

        tvUserScore = (TextView) findViewById(R.id.tv_user_score);
        tvUserNum = (TextView) findViewById(R.id.tv_user_num);
    }

    private void initData() {
        evaluateAdapter = new EvaluateAdapter(getContext(), businessEvaluationInfoList);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(evaluateAdapter);
        getBusinessEvaluate(((BusinessNewActivity) getActivity()).getBusiness().id);
    }

    /**
     * 获取商家评价
     */
    private void getBusinessEvaluate(int id) {
        CustomApplication.getRetrofitNew().getBusinessEvaluation(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject data = jsonObject.optJSONObject("data");
//                        tvScore.setText(data.optString("satisfactionDegreen"));
                        tvScore.setText(data.optString("avgScore"));
                        rbTaste.setRating((float) data.optDouble("avgTastescore"));
                        tvTaste.setText(data.optString("avgTastescore"));
                        rbPackage.setRating((float) data.optDouble("avgPackagesScore"));
                        tvPackage.setText(data.optString("avgPackagesScore"));
//                        tvUserScore.setText("购买此产品的用户满意度为" + data.optString("satisfactionDegreen"));
                        tvUserNum.setText("已有" + data.optString("commentCount") + "人点评");
                        dealBusinessEvaluation(data.optJSONArray("commentList"));
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

//        CustomApplication.getRetrofit().getBusinessEvaluate(id, 1).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                dealBusinessEvaluate(response.body());
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

    private void dealBusinessEvaluation(JSONArray data) {
        if (data != null && data.length() > 0) {
            businessEvaluationInfoList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<BusinessEvaluationInfo>>() {
            }.getType());
            evaluateAdapter.setList(businessEvaluationInfoList);
        }

    }


//    private void dealBusinessEvaluate(String body) {
//        try {
//            JSONObject object = new JSONObject(body);
//            JSONArray rows = object.getJSONArray("rows");
//            JSONObject map = object.getJSONObject("map");
//            int length = rows.length();
//
//            EvaluateInfo evaluateInfo = new EvaluateInfo();
//            evaluateInfo.evaluateNum = map.optInt("total");
//            evaluateInfo.zb = map.optString("zb");
//            evaluateInfos.add(evaluateInfo);
//            if (length <= 0) {
//                return;
//            }
//            for (int i = 0; i < length; i++) {
//                JSONObject jsonObject = rows.getJSONObject(i);
//                if (jsonObject != null) {
//                    EvaluateInfo evaluate = GsonUtil.parseJsonWithGson(jsonObject.toString(), EvaluateInfo.class);
//                    evaluateInfos.add(evaluate);
//                }
//            }
//            loadMoreAdapter.loadAllDataCompleted();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
