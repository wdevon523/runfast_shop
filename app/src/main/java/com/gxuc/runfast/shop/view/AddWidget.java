package com.gxuc.runfast.shop.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxuc.runfast.shop.adapter.shopcaradater.FoodAdapter;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.github.florent37.viewanimator.ViewAnimator;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.lljjcoder.citylist.Toast.ToastUtils;

public class AddWidget extends FrameLayout {

    private View add, sub;
    private TextView tv_count;
    private int position;
    private long count;

    private BaseQuickAdapter foodAdapter;

    public interface OnAddClick {
        void onAddClick(View view, FoodBean fb);

        void onSubClick(FoodBean fb);
    }

    private OnAddClick onAddClick;

    public AddWidget(@NonNull Context context) {
        super(context);
    }

    public AddWidget(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_addwidget, this);
        add = findViewById(R.id.iv_add);
        sub = findViewById(R.id.iv_sub);
        tv_count = (TextView) findViewById(R.id.tv_count);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodBean fb = (FoodBean) foodAdapter.getItem(position);

                if (count >= fb.getNum()){
                    ToastUtil.showToast("库存不足");
                    return;
                }

                if (count == 0) {
                    ViewAnimator.animate(sub)
                            .translationX(add.getLeft() - sub.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .duration(300)
                            .interpolator(new DecelerateInterpolator())
                            .andAnimate(tv_count)
                            .translationX(add.getLeft() - tv_count.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .interpolator(new DecelerateInterpolator())
                            .duration(300)
                            .start()
                    ;
//                    sub.setAlpha(1f);
//                    tv_count.setAlpha(1f);
                }


                if (fb.getIslimited() == 1) {
                    if (fb.getLimittype() == 0) {
                        if (count == fb.getLimitNum()) {
                            ToastUtil.showToast("已达到限购上线");
                            return;
                        } else {
                            count++;
                        }
                    } else {
                        if (count == fb.getLimitNum()) {
                            ToastUtil.showToast("已超过优惠件数，将以原价购买");
                        }
                        count++;
                    }
                } else {
                    count++;
                }
                fb.setSelectCount(count);
                foodAdapter.setData(position, fb);
                if (onAddClick != null) {
                    onAddClick.onAddClick(v, fb);
                }
                tv_count.setText(count + "");
            }
        });
        sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    return;
                }
//                if (count == 1 && foodAdapter instanceof FoodAdapter) {
                if (count == 1) {
//                    ViewAnimator.animate(sub)
//                            .translationX(0, add.getLeft() - sub.getLeft())
//                            .rotation(-360)
//                            .alpha(255, 0)
//                            .duration(300)
//                            .interpolator(new AccelerateInterpolator())
//                            .andAnimate(tv_count)
//                            .translationX(0, add.getLeft() - tv_count.getLeft())
//                            .rotation(-360)
//                            .alpha(255, 0)
//                            .interpolator(new AccelerateInterpolator())
//                            .duration(300)
//                            .start()
//                    ;
                    sub.setAlpha(0f);
                    tv_count.setAlpha(0f);
                }
                count--;
                FoodBean fb = (FoodBean) foodAdapter.getItem(position);
                fb.setSelectCount(count);
                foodAdapter.setData(position, fb);
                if (onAddClick != null) {
                    onAddClick.onSubClick(fb);
                }
                tv_count.setText(count == 0 ? "1" : count + "");
            }
        });
    }

    public void setData(BaseQuickAdapter fa, int c, OnAddClick onAddClick) {
        foodAdapter = fa;
        position = c;
        this.onAddClick = onAddClick;
        FoodBean flist = (FoodBean) fa.getData().get(c);
        count = flist.getSelectCount();
        if (count == 0) {
            sub.setAlpha(0);
            tv_count.setAlpha(0);
        } else {
            sub.setAlpha(1f);
            tv_count.setAlpha(1f);
            tv_count.setText(count + "");
        }
    }

}
