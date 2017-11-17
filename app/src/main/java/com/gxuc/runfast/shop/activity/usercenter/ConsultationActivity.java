package com.gxuc.runfast.shop.activity.usercenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.ImageView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客服咨询
 */
public class ConsultationActivity extends ToolBarActivity {

    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        ButterKnife.bind(this);
        x.image().bind(ivCode, "http://www.gxptkc.com/image/ewm.jpg");
    }

    @OnClick(R.id.btn_call)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0775-2995588"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }
}
