package com.gxuc.runfast.shop.activity.usercenter;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.MyAutoUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 关于
 */
public class AboutActivity extends ToolBarActivity {

    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.tv_update)
    TextView tvUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersionCode.setText("版本号: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.tv_update)
    public void onViewClicked() {
        requestCheckNewVersion();
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
                        if (jsonObject.optBoolean("update")) {
                            MyAutoUpdate myAutoUpdate = new MyAutoUpdate(AboutActivity.this);
                            myAutoUpdate.showNoticeDialog(jsonObject.optString("msg"), jsonObject.optString("downloadUrl"));
                        } else {
                            ToastUtil.showToast(jsonObject.optString("msg"));
                        }
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

}
