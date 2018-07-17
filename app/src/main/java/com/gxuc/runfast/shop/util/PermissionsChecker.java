package com.gxuc.runfast.shop.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionsChecker {
    private Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 判断权限
     */
    public boolean judgePermissions(String[] permissions) {
        for (String permission : permissions) {
            if (deniedPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     * PackageManager.PERMISSION_GRANTED 授予权限
     * PackageManager.PERMISSION_DENIED 缺少权限
     */
    private boolean deniedPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }
}