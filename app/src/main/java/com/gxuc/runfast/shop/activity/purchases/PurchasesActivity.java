package com.gxuc.runfast.shop.activity.purchases;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.fragment.DeliveryFragment;
import com.gxuc.runfast.shop.fragment.PurchaseFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.TabLayout.MODE_FIXED;

import com.gxuc.runfast.shop.util.ViewUtils;

/**
 * Created by Devon on 2018/2/7.
 */

public class PurchasesActivity extends ToolBarActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.fl_back_but)
    FrameLayout flBackBut;
    @BindView(R.id.fl_right)
    FrameLayout flRight;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;
    private String[] mTitles = {"代购", "取送件"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
//                setIndicator(tabLayout, (int) getResources().getDimension(R.dimen.dimen_70_dp), (int) getResources().getDimension(R.dimen.dimen_70_dp));
                ViewUtils.setIndicator(tabLayout, 60, 60);
            }
        });

        viewpagerOrder.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), mTitles));
        tabLayout.setupWithViewPager(viewpagerOrder);
        tabLayout.setTabMode(MODE_FIXED);
        viewpagerOrder.setOffscreenPageLimit(mTitles.length);
    }

    private void initData() {


    }

    @OnClick({R.id.fl_back_but, R.id.fl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_back_but:
                finish();
                break;
            case R.id.fl_right:
                startActivity(new Intent(this, DeliveryOrderActivity.class));
                break;
        }
    }

    public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

        private String[] mTitles;

        public FragmentViewPagerAdapter(FragmentManager fm, String[] mTitles) {
            super(fm);
            this.mTitles = mTitles;
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            //创建Fragment并返回
            if (position == 0) {
                return new PurchaseFragment();
            } else if (position == 1) {
                return new DeliveryFragment();
            }
            return new PurchaseFragment();
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }

}
