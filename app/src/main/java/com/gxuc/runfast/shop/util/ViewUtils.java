package com.gxuc.runfast.shop.util;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.DeviceUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.gxuc.runfast.shop.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jp.wasabeef.fresco.processors.BlurPostprocessor;


public class ViewUtils {
    private static long lastClickTime;
    private static Bitmap bitmap;

    public static void getFrescoController(Context context, SimpleDraweeView imgIv, String uri, int width, int height) {
        if (uri != null) {
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(uri))
                    .setResizeOptions(new ResizeOptions(dip2px(context, width), dip2px(context, height)))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(imgIv.getController()).setImageRequest
                    (request)
                    .build();
            imgIv.setController(controller);
        }
    }

    public static void getBlurFresco(Context context, SimpleDraweeView simpleDraweeView, String uri) {
        if (uri != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                    .setPostprocessor(new BlurPostprocessor(context, 25))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .build();

            simpleDraweeView.setController(controller);
        }
    }

    public static int dip2px(Context context, double d) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void addTvAnim(View fromView, int[] carLoc, Context context, final CoordinatorLayout rootview) {
        int[] addLoc = new int[2];
        fromView.getLocationInWindow(addLoc);

        Path path = new Path();
        path.moveTo(addLoc[0], addLoc[1] - DeviceUtil.dip2px(context, 22));
//        path.quadTo(carLoc[0], addLoc[1] - 200, carLoc[0], carLoc[1]);
        path.cubicTo(addLoc[0], addLoc[1] - DeviceUtil.dip2px(context, 22), addLoc[0] - 300, addLoc[1] - 500, carLoc[0], carLoc[1]);

        final TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.circle_blue);
//        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
        rootview.addView(textView, lp);
        ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                rootview.removeView(textView);
            }
        }).start();
    }

    public static void showClearCar(Context mContext, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        TextView tv = new TextView(mContext);
        tv.setText("清空购物车?");
        tv.setTextSize(14);
        tv.setPadding(ViewUtils.dip2px(mContext, 16), ViewUtils.dip2px(mContext, 16), 0, 0);
        tv.setTextColor(Color.parseColor("#757575"));
        AlertDialog alertDialog = builder
                .setNegativeButton("取消", null)
                .setCustomTitle(tv)
                .setPositiveButton("清空", onClickListener)
                .show();
        Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(ContextCompat.getColor(mContext, R.color.dodgerblue));
        nButton.setTypeface(Typeface.DEFAULT_BOLD);
        Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(ContextCompat.getColor(mContext, R.color.dodgerblue));
        pButton.setTypeface(Typeface.DEFAULT_BOLD);
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 200) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


}
