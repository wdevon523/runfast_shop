package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.adapter.BusinessAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.spend.AccountRecord;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.fragment.walletfragmnet.MoneyAllFragment;
import com.gxuc.runfast.shop.fragment.walletfragmnet.MoneyExpenditureFragment;
import com.gxuc.runfast.shop.fragment.walletfragmnet.MoneyIncomeFragment;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.ViewUtils;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 收支明细
 */
public class MoneyDetailActivity extends ToolBarActivity {

    @BindView(R.id.tl_list_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_list_view)
    ViewPager mViewPager;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mStringList = new ArrayList<>();
    private BusinessAdapter mAdapter;
    private MoneyAllFragment mMoneyAllFragment;
    private MoneyIncomeFragment mMoneyIncomeFragment;
    private MoneyExpenditureFragment mMoneyExpenditureFragment;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);
        ButterKnife.bind(this);
        initData();
        setData();
        getNetData();
    }

    private void initData() {
        setTitle();

        mAdapter = new BusinessAdapter(getSupportFragmentManager(), mFragments, mStringList);
    }

    private void setData() {
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_orange_select));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_text_gray), getResources().getColor(R.color.color_orange_select));
    }

    private void setTitle() {
        mStringList.add(getResources().getString(R.string.pay_all));
        mStringList.add(getResources().getString(R.string.pay_income));
        mStringList.add(getResources().getString(R.string.pay_expenditure));
    }

    private void getNetData() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofitNew().getListConsume().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealConsumeList(jsonObject.optString("data"));
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

    private void dealConsumeList(String data) {
        ArrayList<AccountRecord> accountRecordList = JsonUtil.fromJson(data, new TypeToken<ArrayList<AccountRecord>>() {
        }.getType());
        if (accountRecordList != null && accountRecordList.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("record", accountRecordList);
            mMoneyAllFragment = new MoneyAllFragment();
            mMoneyIncomeFragment = new MoneyIncomeFragment();
            mMoneyExpenditureFragment = new MoneyExpenditureFragment();
            mFragments.add(mMoneyAllFragment);
            mFragments.add(mMoneyIncomeFragment);
            mFragments.add(mMoneyExpenditureFragment);
            mMoneyAllFragment.setArguments(bundle);
            mMoneyExpenditureFragment.setArguments(bundle);
            mMoneyIncomeFragment.setArguments(bundle);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(mTabLayout, 30, 30);
            }
        });
    }

}
