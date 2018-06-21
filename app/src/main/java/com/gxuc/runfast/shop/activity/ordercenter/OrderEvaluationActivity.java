package com.gxuc.runfast.shop.activity.ordercenter;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.willy.ratingbar.BaseRatingBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Devon on 2017/10/16.
 */

public class OrderEvaluationActivity extends ToolBarActivity {

    @BindView(R.id.iv_order_evaluation_business_img)
    ImageView ivOrderEvaluationBusinessImg;
    @BindView(R.id.tv_order_evaluation_business_name)
    TextView tvOrderEvaluationBusinessName;
    @BindView(R.id.rb_order_evaluate)
    BaseRatingBar rbOrderEvaluate;
    @BindView(R.id.fl_order_evaluation_options)
    TagFlowLayout flOrderEvaluationOptions;
    @BindView(R.id.rb_driver_evaluate)
    BaseRatingBar rbDriverEvaluate;
    @BindView(R.id.fl_driver_evaluation_options)
    TagFlowLayout flDriverEvaluationOptions;
    @BindView(R.id.et_evaluation)
    EditText etEvaluation;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String[] orderOptions = {
            "味道好", "分量足", "做餐慢", "食材新鲜",
            "服务不错", "干净卫生", "包装精美", "物美价廉",
            "服务差", "商品质量问题", "做错做漏餐"
    };
    private String[] driverOptions = {
            "提前点击确认送达", "未收到餐", "服务态度差", "额外收费",
            "送错餐了", "干净卫生", "食品缺失或汤洒", "送货快",
            "服务好", "不是本人配送", "配送慢"
    };
    private LayoutInflater layoutInflater;
    private Set<Integer> orderSet;
    private Set<Integer> driverSet;
    private int isDeliver;
    private int oid;
    private String businessName;
    private String logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluation);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layoutInflater = getLayoutInflater();
    }

    private void initData() {
        oid = getIntent().getIntExtra("oid", 0);
        logo = getIntent().getStringExtra("logo");
        businessName = getIntent().getStringExtra("businessName");
        x.image().bind(ivOrderEvaluationBusinessImg, UrlConstant.ImageBaseUrl +logo, NetConfig.optionsLogoImage);
        tvOrderEvaluationBusinessName.setText(businessName);
        isDeliver = getIntent().getIntExtra("isDeliver", 0);
        flOrderEvaluationOptions.setAdapter(new TagAdapter<String>(orderOptions) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvOptions = (TextView) layoutInflater.inflate(R.layout.item_options_textview, flOrderEvaluationOptions, false);
                tvOptions.setText(orderOptions[position]);
                return tvOptions;
            }
        });

        flDriverEvaluationOptions.setAdapter(new TagAdapter<String>(driverOptions) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvOptions = (TextView) layoutInflater.inflate(R.layout.item_options_textview, flDriverEvaluationOptions, false);
                tvOptions.setText(driverOptions[position]);
                return tvOptions;
            }
        });

    }

    private void setListener() {
        flOrderEvaluationOptions.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                LogUtil.d("devon", "select-----------" + selectPosSet.toString());
                orderSet = selectPosSet;
            }
        });

//        flOrderEvaluationOptions.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                LogUtil.d("devon", "Tag-----------" + position + "string----" + orderOptions[position]);
//                return true;
//            }
//        });

        flDriverEvaluationOptions.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                LogUtil.d("devon", "select-----------" + selectPosSet.toString());
                driverSet = selectPosSet;
            }
        });

//        flDriverEvaluationOptions.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                LogUtil.d("devon", "Tag-----------" + position + "string----" + driverOptions[position]);
//                return true;
//            }
//        });

        rbOrderEvaluate.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                LogUtil.d("devon", "OrderEvaluate-----------" + v);
            }
        });

        rbDriverEvaluate.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                LogUtil.d("devon", "DriverEvaluate-----------" + v);
            }
        });

    }

    @OnClick(R.id.tv_publish)
    public void onViewClicked() {

        if (rbOrderEvaluate.getRating() == 0.0F) {
            ToastUtil.showToast("请先给商家打分");
            return;
        }

        int orderRate = (int) (rbOrderEvaluate.getRating() - 2);

        if (rbDriverEvaluate.getRating() == 0.0F) {
            ToastUtil.showToast("请先给骑手打分");
            return;
        }
        int driverRate = (int) (rbDriverEvaluate.getRating() - 2);

        String orderStr = "";
        if (orderSet != null && orderSet.size() > 0) {
            Iterator<Integer> orderIterator = orderSet.iterator();
            while (orderIterator.hasNext()) {
                orderStr += (orderOptions[orderIterator.next()] + ",");
            }
        }

        String driverStr = "";
        if (driverSet != null && driverSet.size() > 0) {
            Iterator<Integer> driverIterator = driverSet.iterator();
            while (driverIterator.hasNext()) {
                driverStr += (driverOptions[driverIterator.next()] + ",");
            }
        }
        String content = etEvaluation.getText().toString().trim();
        LogUtil.d("devon", "orderStr= " + orderStr + "--driverStr= " + driverStr);

        requestEvaluation(oid, orderRate, orderStr, driverRate, driverStr, content, isDeliver);
    }

    private void requestEvaluation(int oid, int orderRate, String orderStr, int driverRate, String driverStr, String content, int isDeliver) {
        CustomApplication.getRetrofitNew().postEvaluation(oid, orderRate, orderStr, driverRate, driverStr, content, isDeliver).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optBoolean("success")) {
                        ToastUtils.showShortToast(OrderEvaluationActivity.this, jsonObject.optString("msg"));
                        finish();
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
}
