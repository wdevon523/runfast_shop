package com.gxuc.runfast.shop.impl;

import android.os.AsyncTask;

import com.gxuc.runfast.shop.util.ToastUtil;
import com.netease.nis.captcha.Captcha;

/**
 * Created by Devon on 2018/3/23.
 */

public class UserCaptchaTask extends AsyncTask<Void, Void, Boolean> {
    public Captcha mCaptcha;

    public UserCaptchaTask(Captcha mCaptcha) {
        this.mCaptcha = mCaptcha;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //可选：简单验证DeviceId、CaptchaId、Listener值
        return mCaptcha.checkParams();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            //必填：开始验证
            mCaptcha.Validate();

        } else {
            ToastUtil.showToast("验证码SDK参数设置错误,请检查配置");
        }
    }

    @Override
    protected void onCancelled() {
//        mLoginTask = null;
    }
}
