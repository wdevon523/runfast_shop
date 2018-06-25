package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.activity.LoginQucikActivity;
import com.gxuc.runfast.shop.activity.MyEvaluateActivity;
import com.gxuc.runfast.shop.activity.purchases.PurchasesActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.activity.usercenter.ConsultationActivity;
import com.gxuc.runfast.shop.activity.usercenter.CouponActivity;
import com.gxuc.runfast.shop.activity.usercenter.HelpCenterActivity;
import com.gxuc.runfast.shop.activity.usercenter.IntegralActivity;
import com.gxuc.runfast.shop.activity.usercenter.MyEnshrineActivity;
import com.gxuc.runfast.shop.activity.usercenter.UserInfoActivity;
import com.gxuc.runfast.shop.activity.usercenter.WalletActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.coupon.CouponBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.activity.usercenter.JoinBusinessActivity;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.ComplaintActivity;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;
    @BindView(R.id.tv_integral_num)
    TextView tvIntegralNum;
    @BindView(R.id.view_new_version)
    View viewNewVersion;

    Unbinder unbinder;
    private UserInfo userInfo;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initData() {
        userInfo = UserService.getUserInfo(getActivity());
        if (userInfo == null) {
            clearUi();
        } else {
            requestGetUserInfo();
//            requesttMyRedPackage();
        }
    }

//    private void requesttMyRedPackage() {
//        CustomApplication.getRetrofitNew().getMyRedPack(0, 10).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                String body = response.body();
//                try {
//                    JSONObject jsonObject = new JSONObject(body);
//                    if (jsonObject.optBoolean("success")) {
//                        dealMyRedPack(jsonObject.optJSONArray("data"));
//                    } else {
//                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private void dealMyRedPack(JSONArray data) {
//        ArrayList<CouponBean> couponBeanList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<CouponBean>>() {
//        }.getType());
//
//    }

    /**
     * 重置页面
     */
    private void clearUi() {
        ivHead.setImageResource(R.drawable.icon_default_head);
        UserService.clearUserInfo();
        SharePreferenceUtil.getInstance().putStringValue("token", "");
        tvUserName.setText("登录/注册");
        tvUserInfo.setVisibility(View.GONE);
        tvIntegralNum.setText("0");
    }

    /**
     * 更新页面
     */
    private void updateUi() {
        userInfo = UserService.getUserInfo(getActivity());
        if (!TextUtils.isEmpty(userInfo.pic)) {
            x.image().bind(ivHead, UrlConstant.ImageHeadBaseUrl + userInfo.pic, NetConfig.optionsHeadImage);
        }
        tvUserName.setText(TextUtils.isEmpty(userInfo.nickname) ? userInfo.mobile : userInfo.nickname);
        tvUserInfo.setVisibility(View.VISIBLE);
        String score = String.valueOf(userInfo.score);
        if (score.contains(".")) {
            score = score.substring(0, score.indexOf("."));
        }
        tvIntegralNum.setText((userInfo.score == null) ? "0" : (score + ""));
    }

    @Override
    public void onResume() {
        super.onResume();
//        initData();
        if (!isHidden()) {
            userInfo = UserService.getUserInfo(getActivity());
            if (userInfo == null) {
                clearUi();
            } else {
                updateUi();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        viewNewVersion.setVisibility(CustomApplication.isNeedUpdate ? View.VISIBLE : View.GONE);
//        StatusUtil.setUseStatusBarColor(getActivity(), getResources().getColor(R.color.white));
        StatusUtil.setSystemStatus(getActivity(), true, true);
//        if (!hidden && userInfo != null) {
//            requestGetUserInfo();
//        }
        if (!hidden) {
            initData();
        }
    }

    private void requestGetUserInfo() {

        CustomApplication.getRetrofitNew().getUserInformation().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        dealUserInfo(data);
                    } else {
                        clearUi();
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {
                clearUi();
            }
        });

//        CustomApplication.getRetrofit().getUserInfo(userInfo.getId()).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful()) {
//                    ToastUtil.showToast("网络数据异常");
//                    return;
//                }
//
//                String body = response.body().toString();
//                if (!body.contains("{\"relogin\":1}")) {
//                    dealUserInfo(body);
//                } else {
//                    clearUi();
//                    UserService.clearUserInfo();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//
//        });
    }

    private void dealUserInfo(String data) {
        userInfo = JsonUtil.fromJson(data, UserInfo.class);
        UserService.saveUserInfo(userInfo);
        updateUi();
//        try {
//            JSONObject jsonObject = new JSONObject(body);
//            String cuser = jsonObject.optString("cuser");
//            User user = GsonUtil.fromJson(cuser, User.class);
////            user.setPassword(userInfo.getPassword());
//            user.setUnusedCoupon(jsonObject.optString("unusedCoupon"));
//            UserService.saveUserInfo(user);
//
//            updateUi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_head, R.id.tv_user_name, R.id.layout_address, R.id.layout_collection, R.id.layout_evalute, R.id.layout_my_wallet, R.id.layout_red_package, R.id.layout_integral, R.id.layout_business_join, R.id.layout_join, R.id.layout_complaint, R.id.layout_consulting, R.id.layout_about, R.id.layout_help_center})
    public void onViewClicked(View view) {
        userInfo = UserService.getUserInfo(getActivity());

        switch (view.getId()) {
            case R.id.iv_head://头像
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            case R.id.tv_user_name://登陆注册
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                }
                break;
            case R.id.layout_address://地址管理
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                Intent intent = new Intent(getActivity(), AddressSelectActivity.class);
                intent.putExtra(IntentFlag.KEY, IntentFlag.MANAGER_ADDRESS);
                startActivity(intent);
                break;
            case R.id.layout_collection://收藏
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), MyEnshrineActivity.class));

                break;
            case R.id.layout_evalute://收藏
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), MyEvaluateActivity.class));

                break;
            case R.id.layout_my_wallet://我的钱包余额
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), WalletActivity.class));

                break;
            case R.id.layout_red_package://优惠券
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), CouponActivity.class));

                break;
            case R.id.layout_integral://积分
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(getContext(), LoginQucikActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), IntegralActivity.class));

                break;

            case R.id.layout_join://加盟
                startActivity(new Intent(getContext(), JoinBusinessActivity.class));
                break;
            case R.id.layout_complaint://投诉
                startActivity(new Intent(getContext(), ComplaintActivity.class));
                break;
            case R.id.layout_consulting://客服咨询
                startActivity(new Intent(getContext(), ConsultationActivity.class));
                break;
            case R.id.layout_about://关于
//                startActivity(new Intent(getContext(), AboutActivity.class));
                startActivity(new Intent(getContext(), PurchasesActivity.class));
//                startActivity(new Intent(getContext(), SubmitOrderActivity.class));
                break;
            case R.id.layout_help_center://帮助中心
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
                break;
        }
    }
}
