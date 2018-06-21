package com.gxuc.runfast.shop.view;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.FoodBean;
import com.gxuc.runfast.shop.util.ViewUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gxuc.runfast.shop.activity.BusinessNewActivity.carAdapter;

public class ShopCarView extends FrameLayout {
    private TextView car_limit, tv_amount;
    public ImageView iv_shop_car;
    public TextView car_badge;
    public TextView cart_notice;
    private BottomSheetBehavior behavior;
    public boolean sheetScrolling;
    public View shoprl;
    public int[] carLoc;
    public OnSaveShopCartListener mOnSaveShopCartListener;

    public void setBehavior(final BottomSheetBehavior behavior) {
        this.behavior = behavior;
    }

    public void setBehavior(final BottomSheetBehavior behavior, final View blackView) {
        this.behavior = behavior;
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetScrolling = false;
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    blackView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                sheetScrolling = true;
                blackView.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(blackView, slideOffset);
            }
        });
        blackView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
    }

    public ShopCarView(@NonNull Context context) {
        super(context);
    }

    public ShopCarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (iv_shop_car == null) {
            iv_shop_car = findViewById(R.id.iv_shop_car);
            car_badge = findViewById(R.id.car_badge);
            car_limit = findViewById(R.id.car_limit);
            tv_amount = findViewById(R.id.tv_amount);
            shoprl = findViewById(R.id.car_rl);
            cart_notice = findViewById(R.id.cart_notice);
            shoprl.setOnClickListener(new toggleCar());
            carLoc = new int[2];
            iv_shop_car.getLocationInWindow(carLoc);
            carLoc[0] = carLoc[0] + iv_shop_car.getWidth() / 2 - ViewUtils.dip2px(getContext(), 10);
            car_limit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSaveShopCartListener != null) {
                        mOnSaveShopCartListener.onSaveShopCartListener();
                    }
                }
            });
        }
    }

    public void updateAmount(BigDecimal amount, BigDecimal startPay) {
//        HashMap<Integer, ArrayList<FoodBean>> specMap = new HashMap<>();
//        BigDecimal totalPrice = new BigDecimal(0.0);
//        BigDecimal payPrice = new BigDecimal(0.0);

//        for (int i = 0; i < carFoods.size(); i++) {
//            BigDecimal foodPayPrice = new BigDecimal(0.0);
//            BigDecimal foodTotalPrice = new BigDecimal(0.0);
//
//            if (carFoods.get(i).getGoodsSellStandardList().size() > 1 || carFoods.get(i).getGoodsSellOptionList().size() > 0) {
//                for (int j = 0; j < carFoods.get(i).getGoodsSellStandardList().size(); j++) {
//                    if (carFoods.get(i).getSpecInfo().standardId == carFoods.get(i).getGoodsSellStandardList().get(j).id) {
//                        if (carFoods.get(i).getGoodsSellStandardList().get(j).isLimited == 0) {
//                            if (carFoods.get(i).getGoodsSellStandardList().get(j).disprice == null) {
//                                carFoods.get(i).getGoodsSellStandardList().get(j).disprice = carFoods.get(i).getGoodsSellStandardList().get(j).price;
//                            }
//                            foodPayPrice = carFoods.get(i).getGoodsSellStandardList().get(j).disprice.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//                            foodTotalPrice = carFoods.get(i).getGoodsSellStandardList().get(j).price.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//                        } else {
//                            if (specMap.containsKey(carFoods.get(i).getSpecInfo().standardId)) {
//                                specMap.get(carFoods.get(i).getSpecInfo().standardId).add(carFoods.get(i));
//                            } else {
//                                ArrayList<FoodBean> foodBeans = new ArrayList<>();
//                                foodBeans.add(carFoods.get(i));
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (carFoods.get(i).getGoodsSellStandardList().get(0).disprice == null) {
//                    carFoods.get(i).getGoodsSellStandardList().get(0).disprice = carFoods.get(i).getGoodsSellStandardList().get(0).price;
//                }
//                if (carFoods.get(i).getGoodsSellStandardList().get(0).isLimited == 0) {
//                    foodPayPrice = carFoods.get(i).getGoodsSellStandardList().get(0).disprice.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//                    foodTotalPrice = carFoods.get(i).getGoodsSellStandardList().get(0).price.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//
//                } else {
//                    if (carFoods.get(i).getSpecInfo().num <= carFoods.get(i).getGoodsSellStandardList().get(0).limitNum) {
//                        foodPayPrice = carFoods.get(i).getGoodsSellStandardList().get(0).disprice.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//                    } else {
//                        foodPayPrice = carFoods.get(i).getGoodsSellStandardList().get(0).disprice.multiply(BigDecimal.valueOf(carFoods.get(i).getGoodsSellStandardList().get(0).limitNum)
//                                .add(carFoods.get(i).getGoodsSellStandardList().get(0).price).multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num - carFoods.get(i).getGoodsSellStandardList().get(0).limitNum)));
//                    }
//                    foodTotalPrice = carFoods.get(i).getGoodsSellStandardList().get(0).price.multiply(BigDecimal.valueOf(carFoods.get(i).getSpecInfo().num));
//                }
//
//            }
//            payPrice = payPrice.add(foodPayPrice);
//            totalPrice = totalPrice.add(foodTotalPrice);
//        }

//        for (Integer key : specMap.keySet()) {
//            int specNum = 0;
//            for (int i = 0; i < specMap.get(key).size(); i++) {
//                specNum += specMap.get(key).get(i).getSpecInfo().num;
//                for (int j = 0; j < ; j++) {
//
//                }
//            }
//
//        }
//        BigDecimal amount = payPrice;

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            car_limit.setEnabled(false);
            car_limit.setText("¥" + startPay + " 起送");
            car_limit.setTextColor(Color.parseColor("#a8a8a8"));
            car_limit.setBackgroundColor(Color.parseColor("#535353"));
            findViewById(R.id.car_nonselect).setVisibility(View.VISIBLE);
            findViewById(R.id.amount_container).setVisibility(View.GONE);
            iv_shop_car.setImageResource(R.drawable.icon_not_shop_car);
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (amount.compareTo(startPay) < 0) {
            car_limit.setEnabled(false);
            car_limit.setText("还差 ¥" + startPay.subtract(amount) + " 起送");
            car_limit.setTextColor(Color.parseColor("#a8a8a8"));
            car_limit.setBackgroundColor(Color.parseColor("#535353"));
            findViewById(R.id.car_nonselect).setVisibility(View.GONE);
            findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
            iv_shop_car.setImageResource(R.drawable.icon_shop_car);
        } else {
            car_limit.setEnabled(true);
//            car_limit.setText("     去结算     ");
            car_limit.setText("去结算");
            car_limit.setTextColor(Color.WHITE);
            car_limit.setBackgroundColor(Color.parseColor("#fba42a"));
            findViewById(R.id.car_nonselect).setVisibility(View.GONE);
            findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
            iv_shop_car.setImageResource(R.drawable.icon_shop_car);
        }
        tv_amount.setText("¥" + amount);
    }

    public void showBadge(int total) {
        if (total > 0) {
            car_badge.setVisibility(View.VISIBLE);
            car_badge.setText(total + "");
            cart_notice.setVisibility(VISIBLE);
        } else {
            car_badge.setVisibility(View.INVISIBLE);
            cart_notice.setVisibility(GONE);
        }
    }

    private class toggleCar implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (sheetScrolling) {
                return;
            }
            if (carAdapter.getItemCount() == 0) {
                return;
            }
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cart_notice.setVisibility(TextUtils.isEmpty(cart_notice.getText()) ? GONE : VISIBLE);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                cart_notice.setVisibility(GONE);
            }
        }
    }

    public interface OnSaveShopCartListener {
        void onSaveShopCartListener();
    }

    public void setOnSaveShopCartListener(OnSaveShopCartListener mOnSaveShopCartListener) {
        this.mOnSaveShopCartListener = mOnSaveShopCartListener;
    }

}
