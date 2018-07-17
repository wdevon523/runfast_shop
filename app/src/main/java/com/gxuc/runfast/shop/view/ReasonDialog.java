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
import android.widget.TextView;

import com.gxuc.runfast.shop.R;

import java.util.List;

public class ReasonDialog extends Dialog {

    private Context context;
    private List<String> reasonList;
    private String reason;
    private OnReasonDialogClickListener listener;
    private LinearLayout llContainReason;

    public ReasonDialog(@NonNull Context context, List<String> reasonList, OnReasonDialogClickListener listener) {
        super(context);
        this.context = context;
        this.reasonList = reasonList;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_refund_reason);
        getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);

        llContainReason = (LinearLayout) findViewById(R.id.ll_contain_reason);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);

        initView();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView() {
        llContainReason.removeAllViews();
        for (int i = 0; i < reasonList.size(); i++) {
            View itemView = getLayoutInflater().inflate(R.layout.item_reason, null);
            TextView tvReason = (TextView) itemView.findViewById(R.id.tv_reason);
            tvReason.setTextColor(TextUtils.equals(reason, reasonList.get(i)) ? context.getResources().getColor(R.color.bg_fba42a) : context.getResources().getColor(R.color.text_333333));
            tvReason.setText(reasonList.get(i));
            tvReason.setTag(reasonList.get(i));
            llContainReason.addView(itemView);

            tvReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReasonDialogClick((String) v.getTag());
                    dismiss();
                }
            });
        }
    }

    public void setReason(String reason) {
        this.reason = reason;
        initView();
    }

    public interface OnReasonDialogClickListener {
        /**
         * 点击按钮的回调方法
         *
         * @param selectReason
         */
        void onReasonDialogClick(String selectReason);
    }

}
