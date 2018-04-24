package com.gxuc.runfast.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;

/**
 * Created by Devon on 2018/1/15.
 */

public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            if (TextUtils.equals(packageName, context.getPackageName())) {
                SharePreferenceUtil.getInstance().putBooleanValue(CustomConstant.IS_FIRST, true);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
            if (TextUtils.equals(packageName, context.getPackageName())) {
                SharePreferenceUtil.getInstance().putBooleanValue(CustomConstant.IS_FIRST, true);
            }
        }


    }

}
