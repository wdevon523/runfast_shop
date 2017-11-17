package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.BusinessExercise;
import com.gxuc.runfast.shop.bean.BusinessInfo;
import com.gxuc.runfast.shop.bean.HomeDataItemBean;
import com.gxuc.runfast.shop.bean.mainmiddle.MiddleSort;
import com.gxuc.runfast.shop.bean.maintop.TopImage;
import com.gxuc.runfast.shop.bean.maintop.TopImage1;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.view.recyclerview.HorizontalPageLayoutManager;
import com.gxuc.runfast.shop.view.recyclerview.PagingScrollHelper;
import com.jude.rollviewpager.RollPagerView;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by quantan.liu on 2017/3/27.
 */

public class NearbyBusinessAdapter extends RecyclerView.Adapter {

    private Context context;

    private List<HomeDataItemBean> data;

    private static final int MROWS = 2;//行数
    private static final int MCOLUMNS = 4;//列数

    public NearbyBusinessAdapter(Context context, List<HomeDataItemBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View firstView = LayoutInflater.from(context).inflate(R.layout.item_home_first, parent, false);
                HomeFirstViewHolder homeHolder = new HomeFirstViewHolder(firstView);
                return homeHolder;
            case 1:
                View secondView = LayoutInflater.from(context).inflate(R.layout.item_home_second, parent, false);
                HomeSecondViewHolder secondHolder = new HomeSecondViewHolder(secondView);
                return secondHolder;
            case 2:
                View thirdView = LayoutInflater.from(context).inflate(R.layout.item_home_third, parent, false);
                HomeThirdViewHolder thirdHolder = new HomeThirdViewHolder(thirdView);
                return thirdHolder;
            case 3:
                View businessView = LayoutInflater.from(context).inflate(R.layout.item_business_info, parent, false);
                BusinessViewHolder businessHolder = new BusinessViewHolder(businessView);
                return businessHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeDataItemBean itemBean = data.get(position);
        if (holder instanceof HomeFirstViewHolder) {
            HomeFirstViewHolder fisrtHolder = (HomeFirstViewHolder) holder;
            List<TopImage> imgurl = itemBean.imgurl;
            NormalAdapter mNormalAdapter = new NormalAdapter(context, imgurl);
            fisrtHolder.pagerView.setAdapter(mNormalAdapter);
        } else if (holder instanceof HomeSecondViewHolder) {
            HomeSecondViewHolder secondHolder = (HomeSecondViewHolder) holder;
            //中部横向滚动
            List<MiddleSort> middleurl = itemBean.middleurl;
            PageScrollAdapter myAdapter = new PageScrollAdapter(context, middleurl);
            secondHolder.recyclerView.setAdapter(myAdapter);
            HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(MROWS, MCOLUMNS);
            secondHolder.recyclerView.setLayoutManager(horizontalPageLayoutManager);
            PagingScrollHelper scrollHelper = new PagingScrollHelper();
            scrollHelper.setUpRecycleView(secondHolder.recyclerView);
            //scrollHelper.setOnPageChangeListener(this);
        } else if (holder instanceof HomeThirdViewHolder) {
            HomeThirdViewHolder thirdHolder = (HomeThirdViewHolder) holder;
            List<TopImage1> imgurl1 = itemBean.imgurl1;
            //底部滚动
            HorizontalPageLayoutManager horizontalPageLayoutManager1 = new HorizontalPageLayoutManager(1, 3);
            thirdHolder.recyclerView.setLayoutManager(horizontalPageLayoutManager1);
            if (imgurl1 != null && imgurl1.size() > 0) {
                BottomPageAdapter mBottomPageAdapter = new BottomPageAdapter(context, imgurl1);
                thirdHolder.recyclerView.setAdapter(mBottomPageAdapter);
                thirdHolder.recyclerView.setVisibility(View.VISIBLE);
            } else {
                thirdHolder.recyclerView.setVisibility(View.GONE);
            }
        } else if (holder instanceof BusinessViewHolder) {
            final BusinessViewHolder businessHolder = (BusinessViewHolder) holder;
            final BusinessInfo businessInfos = itemBean.businessInfos;
            businessHolder.tv_business_name.setText(businessInfos.name);
            businessHolder.tv_business_levelId.setText(String.valueOf(businessInfos.levelId));
            businessHolder.rb_order_evaluate.setRating(businessInfos.levelId);
            businessHolder.rb_order_evaluate.setTouchable(false);
            businessHolder.tv_sale_distance.setText(String.valueOf(new DecimalFormat("#0.0").format(businessInfos.distance)) + "km");
            businessHolder.tv_business_sales_num.setText("月售" + String.valueOf(businessInfos.salesnum) + "单");
            businessHolder.tv_sale_startPay.setText(businessInfos.startPay.isNaN() ? "起送 ¥ 0元" : "起送 ¥ " + String.valueOf(businessInfos.startPay));
            businessHolder.tv_sale_time.setText(businessInfos.speed);

            businessHolder.ll_business_open.setVisibility(businessInfos.isopen == 0 ? View.VISIBLE :View.GONE);
            businessHolder.ll_business_close.setVisibility(businessInfos.isopen == 0 ? View.GONE :View.VISIBLE);
            businessHolder.view_close.setVisibility(businessInfos.isopen == 0 ? View.GONE :View.VISIBLE);


            if (businessInfos.isDeliver == 0) {
                businessHolder.tv_sale_price.setText(businessInfos.charge.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfos.charge));
                businessHolder.iv_is_charge.setVisibility(View.VISIBLE);
            } else {
                businessHolder.tv_sale_price.setText(businessInfos.busshowps.isNaN() ? "配送费¥0" : "配送费¥" + String.valueOf(businessInfos.busshowps));
                businessHolder.iv_is_charge.setVisibility(View.GONE);
            }

            final List<BusinessExercise> alist = businessInfos.alist;
            businessHolder.ll_contain_act.removeAllViews();
            businessHolder.ll_contain_act.setTag(false);
            if (alist != null && alist.size() > 0) {
                for (int i = 0; i < alist.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_business_act, null);
                    ImageView ivAct = (ImageView) view.findViewById(R.id.iv_act);
                    TextView tvAct = (TextView) view.findViewById(R.id.tv_act);
                    tvAct.setText(alist.get(i).showname);
                    showActImage(ivAct, alist.get(i));
                    if (i > 1) {
                        view.setVisibility(View.GONE);
                    }
                    businessHolder.ll_contain_act.addView(view);
                }

                if (alist.size() > 2) {
                    businessHolder.ll_contain_act.getChildAt(0).findViewById(R.id.tv_act_all).setVisibility(View.VISIBLE);
                }

            }

            businessHolder.ll_contain_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alist.size() > 2) {
                        boolean showStatus = false;
                        for (int i = 0; i < alist.size(); i++) {
                            businessHolder.ll_contain_act.getChildAt(i).setVisibility(View.VISIBLE);
                            if (i > 1) {
                                if ((boolean) businessHolder.ll_contain_act.getTag()) {
                                    businessHolder.ll_contain_act.getChildAt(i).setVisibility(View.GONE);
                                    showStatus = false;
                                } else {
                                    businessHolder.ll_contain_act.getChildAt(i).setVisibility(View.VISIBLE);
                                    showStatus = true;
                                }
                            }
                        }
                        businessHolder.ll_contain_act.setTag(showStatus);
                    }

                }
            });

            x.image().bind(businessHolder.iv_business_logo, UrlConstant.ImageBaseUrl + businessInfos.mini_imgPath, NetConfig.optionsPagerCache);
            //GlideUtils.loadImage(3,item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_item_movie_top));
            businessHolder.layout_breakfast_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickListener(businessInfos, v);
                }
            });
        }
    }

    private void showActImage(ImageView ivAct, BusinessExercise businessExercise) {
        //ptype:1满减,2打折,3赠品,4特价,5满减免运费,6优惠券
        switch (businessExercise.ptype) {
            case 1:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
            case 2:
                ivAct.setImageResource(R.drawable.icon_fracture);
                break;
            case 3:
                ivAct.setImageResource(R.drawable.icon_give);
                break;
            case 4:
                ivAct.setImageResource(R.drawable.icon_special);
                break;
            case 5:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
            case 6:
                ivAct.setImageResource(R.drawable.icon_reduce);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (position == 1) {
            return 1;
        }
        if (position == 2) {
            return 2;
        }
        return 3;
    }

    class HomeFirstViewHolder extends RecyclerView.ViewHolder {

        RollPagerView pagerView;

        HomeFirstViewHolder(View itemView) {
            super(itemView);
            pagerView = (RollPagerView) itemView.findViewById(R.id.roll_view_pager);
        }
    }

    class HomeSecondViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HomeSecondViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_home_middle);
        }
    }

    class HomeThirdViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        HomeThirdViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_home_bottom);
        }
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder {

        TextView tv_business_name, tv_business_levelId, tv_sale_distance,
                tv_business_sales_num, tv_sale_startPay, tv_sale_time, tv_sale_price;

        ImageView iv_is_charge, iv_business_logo;

        LinearLayout ll_contain_act, layout_breakfast_item,ll_business_open,ll_business_close;

        ScaleRatingBar rb_order_evaluate;

        View view_close;

        BusinessViewHolder(View itemView) {
            super(itemView);
            tv_business_name = (TextView) itemView.findViewById(R.id.tv_business_name);
            tv_business_levelId = (TextView) itemView.findViewById(R.id.tv_business_levelId);
            tv_sale_distance = (TextView) itemView.findViewById(R.id.tv_sale_distance);
            tv_business_sales_num = (TextView) itemView.findViewById(R.id.tv_business_sales_num);
            tv_sale_startPay = (TextView) itemView.findViewById(R.id.tv_sale_startPay);
            tv_sale_time = (TextView) itemView.findViewById(R.id.tv_sale_time);
            tv_sale_price = (TextView) itemView.findViewById(R.id.tv_sale_price);
            iv_is_charge = (ImageView) itemView.findViewById(R.id.iv_is_charge);
            iv_business_logo = (ImageView) itemView.findViewById(R.id.iv_business_logo);

            rb_order_evaluate = (ScaleRatingBar) itemView.findViewById(R.id.rb_order_evaluate);

            ll_business_open = (LinearLayout) itemView.findViewById(R.id.ll_business_open);
            ll_business_close = (LinearLayout) itemView.findViewById(R.id.ll_business_close);
            ll_contain_act = (LinearLayout) itemView.findViewById(R.id.ll_contain_act);
            layout_breakfast_item = (LinearLayout) itemView.findViewById(R.id.layout_breakfast_item);
            view_close = (View) itemView.findViewById(R.id.view_close);
        }
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(BusinessInfo positionData, View view);
    }

}
