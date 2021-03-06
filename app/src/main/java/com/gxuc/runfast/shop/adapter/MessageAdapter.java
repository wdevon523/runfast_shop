package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.DiscountActivity;
import com.gxuc.runfast.shop.bean.message.MessageInfo;
import com.gxuc.runfast.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiliu on 2017/9/5.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private ArrayList<MessageInfo> messageInfoList;

    public MessageAdapter(Context context, ArrayList<MessageInfo> messageInfoList) {
        this.mContext = context;
        this.messageInfoList = messageInfoList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message_list, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageInfo messageInfo = messageInfoList.get(position);
        //TODO 消息图片在哪里获取
        //1系统消息2商家消息3骑手消息4用户消息5确认订单提示消息
        switch (messageInfo.type) {
            case 1:
                holder.iv_message_notice.setImageResource(R.drawable.icon_promotion);
                break;
            case 4:
                break;
            case 2:
            case 3:
            case 5:
                holder.iv_message_notice.setImageResource(R.drawable.icon_message_notice);
                holder.ll_message_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, DiscountActivity.class));
                    }
                });
                break;
        }
        holder.tv_message_name.setText(messageInfo.title);
        holder.tv_message_date.setText(messageInfo.createTime.substring(5, 16));
        holder.tv_message_content.setText(messageInfo.content);

    }

    @Override
    public int getItemCount() {
        return messageInfoList.size() <= 0 ? 0 : messageInfoList.size();
    }

    public void setList(ArrayList<MessageInfo> messageInfoList) {
        this.messageInfoList = messageInfoList;
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_message_notice;
        TextView tv_message_name, tv_message_content, tv_message_date;
        LinearLayout ll_message_content;

        public MessageViewHolder(View itemView) {
            super(itemView);
            iv_message_notice = (ImageView) itemView.findViewById(R.id.iv_message_notice);
            tv_message_name = (TextView) itemView.findViewById(R.id.tv_message_name);
            tv_message_content = (TextView) itemView.findViewById(R.id.tv_message_content);
            tv_message_date = (TextView) itemView.findViewById(R.id.tv_message_date);
            ll_message_content = (LinearLayout) itemView.findViewById(R.id.ll_message_content);
        }
    }
}
