package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.LoginActivity;
import com.gxuc.runfast.shop.activity.usercenter.AboutActivity;
import com.gxuc.runfast.shop.activity.usercenter.AddressSelectActivity;
import com.gxuc.runfast.shop.activity.usercenter.ConsultationActivity;
import com.gxuc.runfast.shop.activity.usercenter.CouponActivity;
import com.gxuc.runfast.shop.activity.usercenter.HelpCenterActivity;
import com.gxuc.runfast.shop.activity.usercenter.IntegralActivity;
import com.gxuc.runfast.shop.activity.usercenter.MyEnshrineActivity;
import com.gxuc.runfast.shop.activity.usercenter.UserInfoActivity;
import com.gxuc.runfast.shop.activity.usercenter.WalletActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.Users;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.activity.usercenter.JoinBusinessActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.ComplaintActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    Unbinder unbinder;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_wallet_money)
    TextView tvWalletMoney;
    @BindView(R.id.tv_coupons_num)
    TextView tvCouponsNum;
    @BindView(R.id.tv_integral_num)
    TextView tvIntegralNum;
    private User userInfo;

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
        }
    }

    /**
     * 重置页面
     */
    private void clearUi() {
        ivHead.setImageResource(R.drawable.icon_default_head);
        tvUserName.setText("登录/注册");
        tvWalletMoney.setText("0");
        tvCouponsNum.setText("0");
        tvIntegralNum.setText("0");
    }

    /**
     * 更新页面
     */
    private void updateUi() {
        userInfo = UserService.getUserInfo(getActivity());
        if (!TextUtils.isEmpty(userInfo.getPic())) {
            x.image().bind(ivHead, UrlConstant.ImageHeadBaseUrl + userInfo.getPic(), NetConfig.optionsHeadImage);
        }
        tvUserName.setText(TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getMobile() : userInfo.getNickname());
        tvWalletMoney.setText(userInfo.getShowremainder() + "");
        tvCouponsNum.setText((TextUtils.isEmpty(userInfo.getUnusedCoupon())) ? "0" : userInfo.getUnusedCoupon());
        String score = String.valueOf(userInfo.getScore());
        if (score.contains(".")) {
            score = score.substring(0, score.indexOf("."));
        }
        tvIntegralNum.setText((userInfo.getScore() == null) ? "0" : (score + ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && userInfo != null) {
            requestGetUserInfo();
        }
    }

    private void requestGetUserInfo() {
        CustomApplication.getRetrofit().getUserInfo(userInfo.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    CustomToast.INSTANCE.showToast(CustomApplication.getContext(), "网络数据异常");
                    return;
                }

                String body = response.body().toString();
                if (!body.contains("{\"relogin\":1}")) {
                    dealUserInfo(body);
                } else {
                    clearUi();
                    UserService.clearUserInfo();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });
    }

    private void dealUserInfo(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String cuser = jsonObject.optString("cuser");
            User user = GsonUtil.fromJson(cuser, User.class);
            user.setPassword(userInfo.getPassword());
            user.setUnusedCoupon(jsonObject.optString("unusedCoupon"));
            UserService.saveUserInfo(user);

            updateUi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_head, R.id.tv_user_name, R.id.layout_help_center, R.id.layout_my_wallet, R.id.layout_coupons, R.id.layout_integral, R.id.layout_address, R.id.layout_collection, R.id.layout_join, R.id.layout_complaint, R.id.layout_consulting, R.id.layout_about})
    public void onViewClicked(View view) {
        userInfo = UserService.getUserInfo(getActivity());

        switch (view.getId()) {
            case R.id.iv_head://头像
                if (userInfo == null || userInfo.getId() == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            case R.id.tv_user_name://登陆注册
                if (userInfo == null || userInfo.getId() == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.layout_help_center://帮助中心
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
                break;
            case R.id.layout_my_wallet://我的钱包余额
                if (UserService.getUserInfo(getActivity()) != null) {
                    startActivity(new Intent(getContext(), WalletActivity.class));
                }
                break;
            case R.id.layout_coupons://优惠券
                if (UserService.getUserInfo(getActivity()) != null) {
                    startActivity(new Intent(getContext(), CouponActivity.class));
                }
                break;
            case R.id.layout_integral://积分
                if (UserService.getUserInfo(getActivity()) != null) {
                    startActivity(new Intent(getContext(), IntegralActivity.class));
                }
                break;
            case R.id.layout_address://地址管理
                Intent intent = new Intent(getActivity(), AddressSelectActivity.class);
                intent.setFlags(IntentFlag.MANAGER_ADDRESS);
                startActivity(intent);
                break;
            case R.id.layout_collection://收藏
                if (UserService.getUserInfo(getActivity()) != null) {
                    startActivity(new Intent(getContext(), MyEnshrineActivity.class));
                }
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
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
        }
    }
}
