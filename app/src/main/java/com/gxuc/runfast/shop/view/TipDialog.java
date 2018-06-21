package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.purchases.PurchasesActivity;
import com.gxuc.runfast.shop.util.SystemUtil;

public class TipDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private OnDialogClickListener listener;
    private int tip;
    private boolean isOrder;

    private TextView tvNoTip;
    private TextView tvTwo;
    private TextView tvFive;
    private TextView tvTen;
    private TextView tvFifteen;
    private TextView tvTwenty;
    private EditText etOrtherTip;
    private RelativeLayout rlOrderTip;
    private TextView tvCancel;
    private TextView tvSure;


    public TipDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public interface OnDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param tip
         * @param isPositive
         */
        void onDialogClick(int tip, boolean isPositive);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_tip);
//        LayoutInflater inflater = ((PurchasesActivity) context).getLayoutInflater();
//        View localView = inflater.inflate(R.layout.dialog_tip, null);
//        setContentView(localView);
        getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        tvNoTip = (TextView) findViewById(R.id.tv_no_tip);
        tvNoTip.setOnClickListener(this);
        tvTwo = (TextView) findViewById(R.id.tv_two);
        tvTwo.setOnClickListener(this);
        tvFive = (TextView) findViewById(R.id.tv_five);
        tvFive.setOnClickListener(this);
        tvTen = (TextView) findViewById(R.id.tv_ten);
        tvTen.setOnClickListener(this);
        tvFifteen = (TextView) findViewById(R.id.tv_fifteen);
        tvFifteen.setOnClickListener(this);
        tvTwenty = (TextView) findViewById(R.id.tv_twenty);
        tvTwenty.setOnClickListener(this);
        rlOrderTip = (RelativeLayout) findViewById(R.id.rl_order_tip);
        rlOrderTip.setOnClickListener(this);
        etOrtherTip = (EditText) findViewById(R.id.et_orther_tip);
        etOrtherTip.setOnClickListener(this);
        etOrtherTip.setText("");
        etOrtherTip.clearFocus();
        etOrtherTip.setCursorVisible(false);
        etOrtherTip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && Integer.valueOf(s.toString().trim()) > 200) {
                    etOrtherTip.setText("200");
                    etOrtherTip.setSelection(etOrtherTip.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no_tip:
                tip = 0;
                isOrder = false;
                changeBg();
                break;
            case R.id.tv_two:
                tip = 2;
                isOrder = false;
                changeBg();
                break;
            case R.id.tv_five:
                tip = 5;
                isOrder = false;
                changeBg();
                break;
            case R.id.tv_ten:
                tip = 10;
                isOrder = false;
                changeBg();
                break;
            case R.id.tv_fifteen:
                tip = 15;
                isOrder = false;
                changeBg();
                break;
            case R.id.tv_twenty:
                tip = 20;
                isOrder = false;
                changeBg();
                break;
            case R.id.rl_order_tip:
//                tip = Integer.valueOf(etOrtherTip.getText().toString().trim());
                isOrder = true;
                changeBg();
                break;
            case R.id.tv_cancel:
                tip = 0;
                listener.onDialogClick(tip, false);
                dismiss();
                break;
            case R.id.tv_sure:
                if (isOrder && !TextUtils.isEmpty(etOrtherTip.getText().toString())) {
                    tip = Integer.valueOf(etOrtherTip.getText().toString());
                }
                listener.onDialogClick(tip, true);
                dismiss();
                break;
        }
    }

    private void changeBg() {
        if (isOrder) {
            etOrtherTip.setFocusable(true);
            etOrtherTip.setFocusableInTouchMode(true);
            etOrtherTip.requestFocus();
            SystemUtil.showSoftKeyboard(etOrtherTip);
        } else {
            etOrtherTip.setText("");
            SystemUtil.hideSoftKeyboard(etOrtherTip);
        }
        etOrtherTip.setCursorVisible(isOrder);
        tvNoTip.setBackgroundResource((tip == 0 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvTwo.setBackgroundResource((tip == 2 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvFive.setBackgroundResource((tip == 5 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvTen.setBackgroundResource((tip == 10 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvFifteen.setBackgroundResource((tip == 15 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvTwenty.setBackgroundResource((tip == 20 && !isOrder) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        rlOrderTip.setBackgroundResource(isOrder ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
    }

}
