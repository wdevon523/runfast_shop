package com.gxuc.runfast.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class BusinessAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mStrings;

    public BusinessAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> mStrings) {
        super(fm);
        this.mFragments = mFragments;
        this.mStrings = mStrings;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(mFragments!=null){
            ret = mFragments.size();
        }
        return ret;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStrings.get(position);
    }
}
