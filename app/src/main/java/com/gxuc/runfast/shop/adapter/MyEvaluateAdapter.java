package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.bean.BusinessEvaluationInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.willy.ratingbar.BaseRatingBar;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyEvaluateAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<BusinessEvaluationInfo> evaluationInfoList;
    private UserInfo userInfo;

    public MyEvaluateAdapter(Context context, List<BusinessEvaluationInfo> evaluationInfoList, UserInfo userInfo) {
        this.context = context;
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_evaluate, parent, false);
        return new MyEvaluateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyEvaluateViewHolder evaluateViewHolder = (MyEvaluateViewHolder) holder;
        BusinessEvaluationInfo evaluationInfo = evaluationInfoList.get(position);
        evaluateViewHolder.tvBusinessName.setText(evaluationInfo.businessName);
        x.image().bind(evaluateViewHolder.ivHead, UrlConstant.ImageBaseUrl + userInfo.pic, NetConfig.optionsHeadImage);
        evaluateViewHolder.tvUserName.setText(TextUtils.isEmpty(userInfo.nickname) ? userInfo.mobile : userInfo.nickname);
        evaluateViewHolder.tvEvaluateTime.setText(evaluationInfo.createTime);
        evaluateViewHolder.rbOrderEvaluate.setRating((float) evaluationInfo.score);
        evaluateViewHolder.tvContent.setText(evaluationInfo.content);
        evaluateViewHolder.tvContentMore.setVisibility(TextUtils.isEmpty(evaluationInfo.recontent) ? View.GONE : View.VISIBLE);
        evaluateViewHolder.tvContentMore.setText("追评: " + evaluationInfo.recontent);
        evaluateViewHolder.layoutBusiness_reply.setVisibility(TextUtils.isEmpty(evaluationInfo.feedback) ? View.GONE : View.VISIBLE);
        evaluateViewHolder.tvBusinessReply.setText(evaluationInfo.feedback);

        evaluationInfo.tasteScore = evaluationInfo.tasteScore == null ? 0 : evaluationInfo.tasteScore;
        evaluationInfo.packagesScore = evaluationInfo.packagesScore == null ? 0 : evaluationInfo.tasteScore;
        evaluateViewHolder.tvTasteAndPackage.setText("口味: " + evaluationInfo.tasteScore + "星  包装： " + evaluationInfo.packagesScore + "星");

        evaluateViewHolder.rlBusiness.setTag(position);
        evaluateViewHolder.rlBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessNewActivity.class);
                intent.putExtra("businessId", evaluationInfoList.get((int) v.getTag()).businessId);
                context.startActivity(intent);
            }
        });

        evaluateViewHolder.tvEvaluateDelete.setTag(position);
        evaluateViewHolder.tvEvaluateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete((int) v.getTag());
                }
            }
        });

        evaluateViewHolder.tvEvaluateMore.setTag(position);
        evaluateViewHolder.tvEvaluateMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEvaluateListener != null) {
                    mOnEvaluateListener.onEvaluate((int) v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return evaluationInfoList == null ? 0 : evaluationInfoList.size();
    }

    public void setList(ArrayList<BusinessEvaluationInfo> evaluationInfoList) {
        this.evaluationInfoList = evaluationInfoList;
        notifyDataSetChanged();
    }

    class MyEvaluateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_business)
        RelativeLayout rlBusiness;
        @BindView(R.id.tv_business_name)
        TextView tvBusinessName;
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_evaluate_time)
        TextView tvEvaluateTime;
        @BindView(R.id.rb_order_evaluate)
        BaseRatingBar rbOrderEvaluate;
        @BindView(R.id.tv_taste_and_package)
        TextView tvTasteAndPackage;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_content_more)
        TextView tvContentMore;
        @BindView(R.id.tv_business_reply)
        TextView tvBusinessReply;
        @BindView(R.id.layout_business_reply)
        LinearLayout layoutBusiness_reply;
        @BindView(R.id.tv_evaluate_share)
        TextView tvEvaluateShare;
        @BindView(R.id.tv_evaluate_more)
        TextView tvEvaluateMore;
        @BindView(R.id.tv_evaluate_delete)
        TextView tvEvaluateDelete;

        MyEvaluateViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private OnDeleteListener mOnDeleteListener;
    private OnEvaluateListener mOnEvaluateListener;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

    public interface OnEvaluateListener {
        void onEvaluate(int position);
    }

    public void setOnEvaluateListener(OnEvaluateListener mOnEvaluateListener) {
        this.mOnEvaluateListener = mOnEvaluateListener;
    }

}
