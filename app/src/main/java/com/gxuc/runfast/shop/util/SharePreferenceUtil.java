package com.gxuc.runfast.shop.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.gxuc.runfast.shop.application.CustomApplication;

//@AUTHOR: CiCi
public class SharePreferenceUtil {

    private static SharedPreferences mSharedPreferences = null;
    private static SharedPreferences.Editor mEditor = null;
    private static SharePreferenceUtil mSharePreferenceUtil = null;


    public SharePreferenceUtil() {
        super();
        mSharedPreferences = CustomApplication.context().getSharedPreferences("runfast_shop", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }


    public static SharePreferenceUtil getInstance() {
        if (mSharePreferenceUtil == null) {
            mSharePreferenceUtil = new SharePreferenceUtil();
        }
        return mSharePreferenceUtil;

    }

    public void putStringValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getStringValue(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public void putIntValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getIntValue(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void putBooleanValue(String key, Boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public Boolean getBooleanValue(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }

    public void cancel() {
        mEditor.clear();
        mEditor.commit();
    }

}
