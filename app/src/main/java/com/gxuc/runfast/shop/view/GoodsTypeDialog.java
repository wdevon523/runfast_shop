package com.gxuc.runfast.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;

public class GoodsTypeDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private OnTypeDialogClickListener listener;
    private TextView tvCancel;
    private TextView tvSure;
    private TextView tvFlower;
    private TextView tvFood;
    private TextView tvFresh;
    private TextView tvFile;
    private TextView tvKey;
    private TextView tvOrther;
    private SeekBar sbWeight;
    private TextView tvWeight;

    private String goodsType = "";
    private int goodsWeight = 5;

    public GoodsTypeDialog(@NonNull Context context, OnTypeDialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_flower:
                goodsType = "鲜花";
                changeBg();
                break;
            case R.id.tv_food:
                goodsType = "餐饮";
                changeBg();
                break;
            case R.id.tv_fresh:
                goodsType = "生鲜";
                changeBg();
                break;
            case R.id.tv_file:
                goodsType = "文件";
                changeBg();
                break;
            case R.id.tv_key:
                goodsType = "钥匙";
                changeBg();
                break;
            case R.id.tv_orther:
                goodsType = "其他";
                changeBg();
                break;
            case R.id.tv_cancel:
                listener.onTypeDialogClick("", goodsWeight);
                dismiss();
                break;
            case R.id.tv_sure:
                listener.onTypeDialogClick(goodsType, goodsWeight);
                dismiss();
                break;
        }

    }

    private void changeBg() {
        tvFlower.setBackgroundResource(TextUtils.equals("鲜花", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvFood.setBackgroundResource(TextUtils.equals("餐饮", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvFresh.setBackgroundResource(TextUtils.equals("生鲜", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvFile.setBackgroundResource(TextUtils.equals("文件", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvKey.setBackgroundResource(TextUtils.equals("钥匙", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
        tvOrther.setBackgroundResource(TextUtils.equals("其他", goodsType) ? R.drawable.round_biankuang_fc9153 : R.drawable.round_biankuang_cccccc);
    }

    public interface OnTypeDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param goodsType
         * @param goodsWeight
         */
        void onTypeDialogClick(String goodsType, int goodsWeight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_goods_type);
        getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        tvFlower = (TextView) findViewById(R.id.tv_flower);
        tvFlower.setOnClickListener(this);
        tvFood = (TextView) findViewById(R.id.tv_food);
        tvFood.setOnClickListener(this);
        tvFresh = (TextView) findViewById(R.id.tv_fresh);
        tvFresh.setOnClickListener(this);
        tvFile = (TextView) findViewById(R.id.tv_file);
        tvFile.setOnClickListener(this);
        tvKey = (TextView) findViewById(R.id.tv_key);
        tvKey.setOnClickListener(this);
        tvOrther = (TextView) findViewById(R.id.tv_orther);
        tvOrther.setOnClickListener(this);

        tvWeight = (TextView) findViewById(R.id.tv_weight);
        sbWeight = (SeekBar) findViewById(R.id.sb_weight);
        sbWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                goodsWeight = progress + 5;

                if (progress == 0) {
                    tvWeight.setText("小于5公斤");
                } else {
                    tvWeight.setText(goodsWeight + "公斤");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
