package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinSecondActivity extends ToolBarActivity {

    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_introduce)
    EditText etIntroduce;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_second);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
    }
}
