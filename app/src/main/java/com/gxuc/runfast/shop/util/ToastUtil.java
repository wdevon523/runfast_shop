package com.gxuc.runfast.shop.util;

import android.widget.Toast;

import com.gxuc.runfast.shop.application.CustomApplication;

/**
 * Created by cici on 17/2/13.
 */

public class ToastUtil {
    private static String oldMsg;
    private static long time;
    private static int oldMsgid;

    public static void showToast(String msg) {
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(CustomApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于3秒时才显示
            if (System.currentTimeMillis() - time > 3000) {
                Toast.makeText(CustomApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    public static void showToast(int msg) {
        if (msg != oldMsgid) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(CustomApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于3秒时才显示
            if (System.currentTimeMillis() - time > 3000) {
                Toast.makeText(CustomApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsgid = msg;
    }
}
