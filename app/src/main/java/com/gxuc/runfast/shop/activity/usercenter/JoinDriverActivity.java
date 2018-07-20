package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinDriverActivity extends ToolBarActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.cb_man)
    CheckBox cbMan;
    @BindView(R.id.cb_woman)
    CheckBox cbWoman;
    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.tv_choose_province)
    TextView tvChooseProvince;
    @BindView(R.id.tv_choose_city)
    TextView tvChooseCity;
    @BindView(R.id.cb_experience_yes)
    CheckBox cbExperienceYes;
    @BindView(R.id.cb_experience_no)
    CheckBox cbExperienceNo;
    @BindView(R.id.cb_full_time)
    CheckBox cbFullTime;
    @BindView(R.id.cb_part_time)
    CheckBox cbPartTime;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_driver);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cb_man, R.id.cb_woman, R.id.tv_get_code, R.id.cb_experience_yes, R.id.cb_experience_no, R.id.cb_full_time, R.id.cb_part_time, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_man:
                break;
            case R.id.cb_woman:
                break;
            case R.id.tv_get_code:
                break;
            case R.id.cb_experience_yes:
                break;
            case R.id.cb_experience_no:
                break;
            case R.id.cb_full_time:
                break;
            case R.id.cb_part_time:
                break;
            case R.id.tv_submit:
                break;
        }
    }
}
