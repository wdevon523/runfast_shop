package com.gxuc.runfast.shop.impl;


import android.content.Intent;

import com.gxuc.runfast.shop.activity.LoginActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.util.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Devon on 2017/10/13.
 */

public abstract class MyCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        String body = response.body().toString();
        if (body.contains("{\"relogin\":1}")) {
            //跳转到登录页面
            Intent intent = new Intent(CustomApplication.getContext(), LoginActivity.class);
            intent.putExtra("isRelogin", true);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CustomApplication.getContext().startActivity(intent);
        } else {
            onSuccessResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        CustomToast.INSTANCE.showToast(CustomApplication.getContext(), "网络异常");
        onFailureResponse(call, t);
    }

    public abstract void onSuccessResponse(Call<T> call, Response<T> response);


    public abstract void onFailureResponse(Call<T> call, Throwable t);

}
