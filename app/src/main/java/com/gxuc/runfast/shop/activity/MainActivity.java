package com.gxuc.runfast.shop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gxuc.runfast.shop.BuildConfig;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.fragment.HomeFragment;
import com.gxuc.runfast.shop.fragment.MessageFragment;
import com.gxuc.runfast.shop.fragment.MineFragment;
import com.gxuc.runfast.shop.fragment.OrderFragment;
import com.gxuc.runfast.shop.fragment.TakeOutFoodFragment;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.ActivityManager;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.MyAutoUpdate;
import com.ta.utdid2.android.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends ToolBarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.main_tab_bar)
    RadioGroup mainGroup;
    @BindView(R.id.view_new_version)
    View viewNewVersion;

    private boolean isExit = false;
    public int MAIN_PAGE = 0;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActivityManager.pushActivity(this);
        //toolbar.setNavigationIcon(null);
        bindJpushAlias();
        requestCheckNewVersion();
        requestPermissions();
        initDate(savedInstanceState);
    }

    private void requestPermissions() {

        if (SharePreferenceUtil.getInstance().getBooleanValue(CustomConstant.IS_FIRST, false)){
            SharePreferenceUtil.getInstance().putBooleanValue(CustomConstant.IS_FIRST, false);
            if (!SystemUtil.isNotificationEnabled(this)) {
                showPermissionDialog("应用需要获取通知栏权限,请前往应用信息-权限中开启", true);
            }
        }
    }

    private void showPermissionDialog(String message, final boolean isNotification) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isNotification) {
                            toSetting();
                        } else {
                            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                        }
                        dialog.dismiss();
                        finish();
                    }
                }).show();
        dialog.setCanceledOnTouchOutside(false);
        Button negativeButton = dialog.getButton(BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.text_999999));
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
        Button positiveButton = dialog.getButton(BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
    }

    private void toSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    private void requestCheckNewVersion() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            CustomApplication.getRetrofitNew().checkNewVersion(versionCode).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        viewNewVersion.setVisibility(jsonObject.optBoolean("update") ? View.VISIBLE : View.GONE);
                        if (jsonObject.optBoolean("update")) {
                            MyAutoUpdate myAutoUpdate = new MyAutoUpdate(MainActivity.this);
                            myAutoUpdate.showNoticeDialog(jsonObject.optString("msg"), jsonObject.optString("downloadUrl"));
                        }
                        CustomApplication.isNeedUpdate = jsonObject.optBoolean("update");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void bindJpushAlias() {

        JPushInterface.setAlias(this, CustomApplication.alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("  绑定极光 ", "[initJPush] 设置别名: " + "i:" + i
                        + ",s:" + s + ",set:" + set);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.popActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int currentPage = intent.getIntExtra("currentPage", MAIN_PAGE);
        if (currentPage == 2) {
            mainGroup.check(R.id.main_tab_order);
        }
    }

    private void initDate(Bundle savedInstanceState) {
        //mFragments = mFragments;
        mFragments = new ArrayList<>();
        FragmentManager manager = getSupportFragmentManager();
        if (savedInstanceState == null) {
//            Fragment fragment = new TakeOutFoodFragment();
            Fragment fragment = new HomeFragment();
            mFragments.add(fragment);
            fragment = new MessageFragment();
            mFragments.add(fragment);
            fragment = new OrderFragment();
            mFragments.add(fragment);
            fragment = new MineFragment();
            mFragments.add(fragment);

            FragmentTransaction transaction = manager.beginTransaction();
            int index = 0;
            for (Fragment f : mFragments) {
                transaction.add(R.id.frame_container, f, "tag" + index);
                transaction.hide(f);
                index++;
            }
            transaction.show(mFragments.get(0));
            transaction.commit();
        } else {
            for (int i = 0; i < 4; i++) {
                String tag = "tag" + i;
                Fragment fragmentByTag = manager.findFragmentByTag(tag);
                if (fragmentByTag != null) {
                    mFragments.add(fragmentByTag);
                }
            }
        }
        if (mainGroup != null) {
            mainGroup.setOnCheckedChangeListener(this);
        }
    }

    private void chekedFragment(int index) {
        Bundle bundle = new Bundle();
        if (index >= 0 && index < mFragments.size()) {
            int size = mFragments.size();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            for (int i = 0; i < size; i++) {
                Fragment fragment = mFragments.get(i);
                if (index == i) {
                    transaction.show(fragment);
                } else {
                    transaction.hide(fragment);
                }
            }
            transaction.commit();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int index = 0;
        switch (checkedId) {
            case R.id.main_tab_food:
                index = 0;
                break;
            case R.id.main_tab_message:
                index = 1;
                break;
            case R.id.main_tab_order:
                index = 2;
                break;
            case R.id.main_tab_user:
                index = 3;
                break;
        }
        chekedFragment(index);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitApp() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            // 获取自己的PID来结�?
            // android.os.Process.killProcess(android.os.Process.myPid());
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
            finish();
        }
    }
}
