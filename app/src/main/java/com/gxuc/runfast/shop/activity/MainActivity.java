package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.fragment.MessageFragment;
import com.gxuc.runfast.shop.fragment.MineFragment;
import com.gxuc.runfast.shop.fragment.OrderFragment;
import com.gxuc.runfast.shop.fragment.TakeOutFoodFragment;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ActivityManager;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.MyAutoUpdate;

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
        initDate(savedInstanceState);
    }

    private void requestCheckNewVersion() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            CustomApplication.getRetrofit().checkNewVersion(versionCode).enqueue(new MyCallback<String>() {
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
            Fragment fragment = new TakeOutFoodFragment();
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
