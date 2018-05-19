package com.gxuc.runfast.shop.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gxuc.runfast.shop.bean.home.AdvertInfo;
import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//        Glide.with(context).load(ApiServiceFactory.BASE_IMG_URL + ((AdvertInfo) path).adImages).into(imageView);
        AdvertInfo advertInfo = (AdvertInfo) path;
        Glide.with(context).load(UrlConstant.ImageBaseUrl + advertInfo.adImages).into(imageView);
    }
}
