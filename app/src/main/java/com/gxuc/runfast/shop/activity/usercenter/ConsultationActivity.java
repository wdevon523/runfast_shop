package com.gxuc.runfast.shop.activity.usercenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.CustomInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 客服咨询
 */
public class ConsultationActivity extends ToolBarActivity {

    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_service_phone)
    TextView tvServicePhone;

    private String agentId;
    private String mobile;
    private CustomInfo customInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        ButterKnife.bind(this);
        agentId = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.AGENTID);
        requestServiceInfo();
//        x.image().bind(ivCode, "http://www.gxptkc.com/image/ewm.jpg");

    }

    private void requestServiceInfo() {
        CustomApplication.getRetrofitNew().getCustomInfo(agentId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {

                        String data = jsonObject.optString("data");
                        customInfo = JsonUtil.fromJson(data, CustomInfo.class);
                        x.image().bind(ivCode, UrlConstant.ImageBaseUrl + customInfo.qrcode);
                        tvServicePhone.setText("咨询热线：" + customInfo.mobile);
//                        String custom1 = jsonObject.optString("custom");
//                        customInfo = GsonUtil.fromJson(custom1, CustomInfo.class);
//
////                        JSONObject custom = jsonObject.optJSONObject("custom");
////                        mobile = custom.optString("mobile");
////                        x.image().bind(ivCode, UrlConstant.ImageBaseUrl + jsonObject.optString("qrcode"));
////                        tvServicePhone.setText("咨询热线：" + mobile);
//                        if (customInfo != null) {
//                            x.image().bind(ivCode, UrlConstant.ImageBaseUrl + customInfo.qrcode);
//                            tvServicePhone.setText("咨询热线：" + customInfo.mobile);
//                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_call)
    public void onViewClicked() {
        if (customInfo != null) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customInfo.mobile));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ToastUtil.showToast("请先开启电话权限");
                return;
            }
            startActivity(intent);
        }
    }
}
