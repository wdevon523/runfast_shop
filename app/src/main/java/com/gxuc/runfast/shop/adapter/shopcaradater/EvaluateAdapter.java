package com.gxuc.runfast.shop.adapter.shopcaradater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.EvaluateInfo;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.R;
import com.willy.ratingbar.ScaleRatingBar;

import org.xutils.x;

import java.util.List;

/**
 * Created by 天上白玉京 on 2017/8/3.
 */

public class EvaluateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EvaluateInfo> strings;

    public EvaluateAdapter(Context context, List<EvaluateInfo> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate_head, parent, false);
            holder = new EvaluateViewHeadHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate_info, parent, false);
            holder = new EvaluateViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EvaluateInfo evaluateInfo = strings.get(position);
        if (holder instanceof EvaluateViewHeadHolder) {
            EvaluateViewHeadHolder viewHolder = (EvaluateViewHeadHolder) holder;
            viewHolder.tvScore.setText(evaluateInfo.zb + "");
            viewHolder.tvUserScore.setText("购买此产品的用户满意度为" + evaluateInfo.zb);
            viewHolder.tvUserNum.setText("已有" + evaluateInfo.evaluateNum + "人点评");
        }
        if (holder instanceof EvaluateViewHolder) {
            EvaluateViewHolder evaluateViewHolder = (EvaluateViewHolder) holder;
            evaluateViewHolder.tvUserName.setText(TextUtils.isEmpty(evaluateInfo.userName) ? "匿名用户" : evaluateInfo.userName);
            evaluateViewHolder.tvEvaluateTime.setText(evaluateInfo.createTime);
            if (TextUtils.isEmpty(evaluateInfo.content)) {
                evaluateViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                evaluateViewHolder.tvContent.setVisibility(View.VISIBLE);
                evaluateViewHolder.tvContent.setText(evaluateInfo.content);
            }
            evaluateViewHolder.tvFlag.setText(evaluateInfo.shangstr);
            if (TextUtils.isEmpty(evaluateInfo.feedback)) {
                evaluateViewHolder.layoutBusiness.setVisibility(View.GONE);
            } else {
                evaluateViewHolder.layoutBusiness.setVisibility(View.VISIBLE);
                evaluateViewHolder.tvReply.setText(evaluateInfo.feedback);
            }

            evaluateViewHolder.rb_order_evaluate.setRating((float) (evaluateInfo.score + 2));
            evaluateViewHolder.rb_order_evaluate.setTouchable(false);
            x.image().bind(evaluateViewHolder.ivHead, UrlConstant.ImageBaseUrl + evaluateInfo.pic, NetConfig.optionsPagerCache);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class EvaluateViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvEvaluateTime, tvContent, tvFlag, tvReply;
        public LinearLayout layoutBusiness;
        public ImageView ivHead;
        public ScaleRatingBar rb_order_evaluate;

        public EvaluateViewHolder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvEvaluateTime = (TextView) itemView.findViewById(R.id.tv_evaluate_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvFlag = (TextView) itemView.findViewById(R.id.tv_flag);
            tvReply = (TextView) itemView.findViewById(R.id.tv_business_reply);
            layoutBusiness = (LinearLayout) itemView.findViewById(R.id.layout_business);
            rb_order_evaluate = (ScaleRatingBar) itemView.findViewById(R.id.rb_order_evaluate);

        }
    }

    public class EvaluateViewHeadHolder extends RecyclerView.ViewHolder {

        public TextView tvScore, tvUserScore, tvUserNum;

        public EvaluateViewHeadHolder(View itemView) {
            super(itemView);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            tvUserScore = (TextView) itemView.findViewById(R.id.tv_user_score);
            tvUserNum = (TextView) itemView.findViewById(R.id.tv_user_num);
        }
    }
}
