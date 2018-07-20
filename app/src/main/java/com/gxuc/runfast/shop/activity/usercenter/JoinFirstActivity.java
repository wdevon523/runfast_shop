package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinFirstActivity extends ToolBarActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_first);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_get_code, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                break;
            case R.id.tv_sure:
                break;
        }
    }
}
