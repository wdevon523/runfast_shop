package com.gxuc.runfast.shop.activity.ordercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gxuc.runfast.shop.config.IntentConfig;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.lljjcoder.citylist.Toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuhui on 2017/08/29
 * @email liu594545591@126.com
 * @introduce 订单备注
 */
public class OrderRemarkActivity extends ToolBarActivity {

    @BindView(R.id.et_order_remark)
    EditText mEtOrderRemark;
    @BindView(R.id.btn_order_remark_commit)
    Button mBtnOrderRemarkCommit;

    private int businessId;
    private HashMap<String, String> paramMap;
    private JSONObject paramJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_remark);
        ButterKnife.bind(this);
        String remark_data = getIntent().getStringExtra("remark_data");
        if (!TextUtils.isEmpty(remark_data)) {
            mEtOrderRemark.setText(remark_data);
        }

    }

    @OnClick(R.id.btn_order_remark_commit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_order_remark_commit:
//                if (!TextUtils.isEmpty(mEtOrderRemark.getText())) {
                Intent intent = new Intent();
                intent.putExtra("order_remark", mEtOrderRemark.getText().toString());
//                    setResult(IntentConfig.REMARK_RESULT_CODE, intent);
                setResult(RESULT_OK, intent);
                finish();
//                } else {
//                    ToastUtils.showShortToast(this, "请填写备注内容");
//                }
        }
    }
}
