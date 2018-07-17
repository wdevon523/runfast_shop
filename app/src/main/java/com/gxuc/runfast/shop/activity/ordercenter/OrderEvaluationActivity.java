package com.gxuc.runfast.shop.activity.ordercenter;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.MyCallback;
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
    @BindView(R.id.fl_order_evaluation_well_options)
    TagFlowLayout flOrderEvaluationWellOptions;
    @BindView(R.id.fl_order_evaluation_bad_options)
    TagFlowLayout flOrderEvaluationBadOptions;
    @BindView(R.id.rb_order_evaluate)
    BaseRatingBar rbOrderEvaluate;
    //    @BindView(R.id.rb_driver_evaluate)
//    BaseRatingBar rbDriverEvaluate;
    @BindView(R.id.cb_anonymous)
    CheckBox cbAnonymous;
    @BindView(R.id.et_evaluation)
    EditText etEvaluation;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cb_dont_satisfy)
    CheckBox cbDontSatisfy;
    @BindView(R.id.cb_satisfy)
    CheckBox cbSatisfy;
    @BindView(R.id.rb_order_taste)
    BaseRatingBar rbOrderTaste;
    @BindView(R.id.tv_order_evaluate)
    TextView tvOrderEvaluate;
    @BindView(R.id.tv_taste)
    TextView tvTaste;
    @BindView(R.id.rb_order_package)
    BaseRatingBar rbOrderPackage;
    @BindView(R.id.tv_package)
    TextView tvPackage;
    @BindView(R.id.ll_taste_and_package)
    LinearLayout llTasteAndPackage;
    @BindView(R.id.iv_order_evaluation_driver_img)
    ImageView ivOrderEvaluationDriverImg;
    @BindView(R.id.tv_order_evaluation_driver_name)
    TextView tvOrderEvaluationDriverName;
    private String[] goodOptions = {
            "风雨无阻", "穿戴工服", "礼貌热情", "快速准时",
            "货品完好", "仪表整洁"
    };

    private String[] badOptions = {
            "联系沟通困难", "仪表不整", "送错餐", "提前点送达",
            "路线不熟", "货品不完好", "要好评", "送达不通知",
            "未送到指定地点", "其他"
    };
    private LayoutInflater layoutInflater;
    private Set<Integer> wellSet;
    private Set<Integer> badSet;
    private int isDeliver;
    private int oid;
    private String businessName;
    private String logo;
    private boolean driverSatisfy;
    private String shopper;

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
        shopper = getIntent().getStringExtra("shopper");
        logo = getIntent().getStringExtra("logo");
        businessName = getIntent().getStringExtra("businessName");

        tvOrderEvaluationDriverName.setText(shopper);
        x.image().bind(ivOrderEvaluationBusinessImg, UrlConstant.ImageBaseUrl + logo, NetConfig.optionsLogoImage);
        tvOrderEvaluationBusinessName.setText(businessName);
        isDeliver = getIntent().getIntExtra("isDeliver", 0);

        flOrderEvaluationWellOptions.setAdapter(new TagAdapter<String>(goodOptions) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvOptions = (TextView) layoutInflater.inflate(R.layout.item_options_textview, flOrderEvaluationWellOptions, false);
                tvOptions.setText(goodOptions[position]);
                return tvOptions;
            }
        });

        flOrderEvaluationBadOptions.setAdapter(new TagAdapter<String>(badOptions) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvOptions = (TextView) layoutInflater.inflate(R.layout.item_options_textview, flOrderEvaluationBadOptions, false);
                tvOptions.setText(badOptions[position]);
                return tvOptions;
            }
        });

    }

    private void setListener() {
        flOrderEvaluationWellOptions.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                LogUtil.d("devon", "select-----------" + selectPosSet.toString());
                wellSet = selectPosSet;
            }
        });

        flOrderEvaluationBadOptions.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                LogUtil.d("devon", "select-----------" + selectPosSet.toString());
                badSet = selectPosSet;
            }
        });

        rbOrderEvaluate.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                LogUtil.d("devon", "OrderEvaluate-----------" + v);
                llTasteAndPackage.setVisibility(View.VISIBLE);
                showEvaluate(tvOrderEvaluate, v);

            }
        });

        rbOrderTaste.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                tvTaste.setVisibility(View.VISIBLE);
                showEvaluate(tvTaste, v);

            }
        });

        rbOrderPackage.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                tvPackage.setVisibility(View.VISIBLE);
                showEvaluate(tvPackage, v);

            }
        });


    }

    private void showEvaluate(TextView tv, float v) {
        if (Float.compare(v, 1f) == 0) {
            tv.setText("很差");
        } else if (Float.compare(v, 2f) == 0) {
            tv.setText("差");
        } else if (Float.compare(v, 3f) == 0) {
            tv.setText("一般");
        } else if (Float.compare(v, 4f) == 0) {
            tv.setText("满意");
        } else if (Float.compare(v, 5f) == 0) {
            tv.setText("非常满意");
        }
    }

    @OnClick(R.id.tv_publish)
    public void onViewClicked() {

        if (rbOrderEvaluate.getRating() == 0.0F) {
            ToastUtil.showToast("请先给商家打分");
            return;
        }
        if (rbOrderTaste.getRating() == 0.0F) {
            ToastUtil.showToast("请先给商品口味打分");
            return;
        }
        if (rbOrderPackage.getRating() == 0.0F) {
            ToastUtil.showToast("请先给商品包装打分");
            return;
        }

        int orderRate = (int) rbOrderEvaluate.getRating();
        int tasteRate = (int) rbOrderTaste.getRating();
        int packageRate = (int) rbOrderPackage.getRating();

//        if (rbDriverEvaluate.getRating() == 0.0F) {
//            ToastUtil.showToast("请先给骑手打分");
//            return;
//        }
//        int driverRate = (int) (rbDriverEvaluate.getRating() - 2);

        String optionStr = "";
        if (cbSatisfy.isChecked()) {
            if (wellSet != null && wellSet.size() > 0) {
                Iterator<Integer> orderIterator = wellSet.iterator();
                while (orderIterator.hasNext()) {
                    optionStr += (goodOptions[orderIterator.next()] + ",");
                }
                optionStr = optionStr.substring(0, optionStr.length() - 1);
            }
        } else {
            if (badSet != null && badSet.size() > 0) {
                Iterator<Integer> driverIterator = badSet.iterator();
                while (driverIterator.hasNext()) {
                    optionStr += (badOptions[driverIterator.next()] + ",");
                }
                optionStr = optionStr.substring(0, optionStr.length() - 1);
            }
        }

        LogUtil.d("devon", "optionStr= " + optionStr);
        String content = etEvaluation.getText().toString().trim();

        requestEvaluation(oid, driverSatisfy, optionStr, orderRate, tasteRate, packageRate, cbAnonymous.isChecked(), content, isDeliver);
    }

    private void requestEvaluation(int oid, boolean driverSatisfy, String optionStr, int orderRate, int tasteRate, int packageRate, boolean anonymous, String content, int isDeliver) {
        CustomApplication.getRetrofitNew().postEvaluation(oid, driverSatisfy, optionStr, orderRate, tasteRate, packageRate, anonymous, content, isDeliver).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optBoolean("success")) {
                        ToastUtils.showShortToast(OrderEvaluationActivity.this, "评价成功");
                        finish();
                    } else {
                        ToastUtils.showShortToast(OrderEvaluationActivity.this, jsonObject.optString("errorMsg"));
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


    @OnClick({R.id.cb_dont_satisfy, R.id.cb_satisfy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_dont_satisfy:
                cbSatisfy.setChecked(false);
                cbDontSatisfy.setChecked(true);
                flOrderEvaluationWellOptions.setVisibility(View.GONE);
                flOrderEvaluationBadOptions.setVisibility(View.VISIBLE);
                driverSatisfy = false;
                break;
            case R.id.cb_satisfy:
                cbSatisfy.setChecked(true);
                cbDontSatisfy.setChecked(false);
                flOrderEvaluationWellOptions.setVisibility(View.VISIBLE);
                flOrderEvaluationBadOptions.setVisibility(View.GONE);
                driverSatisfy = true;
                break;
        }
    }
}
