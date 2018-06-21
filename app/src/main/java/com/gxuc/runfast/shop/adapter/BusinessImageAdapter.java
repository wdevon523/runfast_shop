package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ShowImageActivity;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessImageAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] images;

    public BusinessImageAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
//        x.image().bind(viewHolder.ivImage, UrlConstant.ImageBaseUrl + images[position]);
        x.image().bind(viewHolder.ivImage, UrlConstant.ImageBaseUrl + images[position]);
        viewHolder.ivImage.setTag(position);
        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("image", images);
                intent.putExtra("position", (int)v.getTag());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.length;
    }

    public void setList(String[] images) {
        this.images = images;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
