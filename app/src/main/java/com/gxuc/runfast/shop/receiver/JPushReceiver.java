package com.gxuc.runfast.shop.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final int NOTIFICATION_SHOW_SHOW_AT_MOST = 5;   //推送通知最多显示条数

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", bundle: " + bundle);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知" + "extras====" + extras);
            //打开自定义的Activity
//        	Intent i = new Intent(context, OrderDetailActivity.class);
//			i.putExtra("notification", true);
//			i.putBoolean("notification", true);
//        	i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    /**
     * 实现自定义推送声音
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Intent mIntent = new Intent(context, MainActivity.class);
//		ThirdView.isReadList = false;
//        mIntent.putExtra("sex", "");
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        notification.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentTitle(title.equals("") ? context.getString(R.string.app_name) : title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setNumber(NOTIFICATION_SHOW_SHOW_AT_MOST);

        Log.e(TAG, "processCustomMessage: extras----->" + extras);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {
                    String sound = extraJson.getString("sound");
//                    if (Constants.NEW_ORDER.equals(sound) || Constants.NEW_REQUEST.equals(sound)) {
//                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.bell));
//                    } else if (Constants.REFUSE_ORDER.equals(sound)) {
//                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.default_sound));
//                    } else {
//                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.default_sound));
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Notification build = notification.build();
//        build.flags |= Notification.FLAG_INSISTENT;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_SHOW_SHOW_AT_MOST, build);  //id随意，正好使用定义的常量做id，0除外，0为默认的Notification

//        sendMsg(context, bundle);

    }

//    private void sendMsg(Context context, Bundle bundle) {
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//        msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//        if (!TextUtils.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (extraJson.length() > 0) {
//                    msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                }
//            } catch (JSONException e) {
//
//            }
//        }
//        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//    }

}
