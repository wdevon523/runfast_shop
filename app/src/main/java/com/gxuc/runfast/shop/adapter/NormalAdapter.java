package com.gxuc.runfast.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gxuc.runfast.shop.activity.BreakfastActivity;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.activity.WebActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.maintop.TopImage;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 轮播图适配
 * Created by 天上白玉京 on 2017/6/21.
 */

public class NormalAdapter extends StaticPagerAdapter {

    private Context mContext;
    private List<TopImage> imageUrls;

    public NormalAdapter(Context context, List<TopImage> imageUrls) {
        mContext = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        final TopImage topImage = imageUrls.get(position);
        SimpleDraweeView draweeView = new SimpleDraweeView(container.getContext());
        draweeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        draweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(ImageRequest.fromUri(ApiServiceFactory.BASE_IMG_URL + topImage.getAdImages()))
                .setUri(Uri.parse(ApiServiceFactory.BASE_IMG_URL + topImage.getAdImages()))
//                .setImageRequest(ImageRequest.fromUri(ApiServiceFactory.BASE_IMG_URL + topImage.getAdImages()))
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
        //x.image().bind(view, imageUrls[position], NetConfig.optionsPagerCache);
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //type: 广告类型0外卖1商城2一元购
                //adType;//链接类型  1内容  2链接 3商家分类类型、4商家、5商品、6自定义
                switch (topImage.getAdType()) {
                    case 1://内容
                        Intent webIntent = new Intent(mContext, WebActivity.class);
                        if (topImage.getLinkAddr().contains("http")) {
                            webIntent.putExtra("url", topImage.getLinkAddr());
                        } else {
                            webIntent.putExtra("url", ApiServiceFactory.WEB_HOST + topImage.getLinkAddr());
                        }
                        mContext.startActivity(webIntent);
                        break;
                    case 2://链接
                        CustomToast.INSTANCE.showToast(mContext, "2链接");
                        break;
                    case 3://商家分类类型
//                        String[] split = topImage.getLinkAddr().split("=");
//                        int typeId = Integer.parseInt(split[1]);
                        Intent data = new Intent(mContext, BreakfastActivity.class);
                        data.putExtra("typeName", topImage.getTypename());
                        mContext.startActivity(data);
                        break;
                    case 4://商家
                        Intent intent = new Intent(mContext, BusinessActivity.class);
                        intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_TOP_PAGE);
                        intent.putExtra("business", topImage);
                        mContext.startActivity(intent);
                        break;
                    case 5:
                        String[] split = topImage.getLinkAddr().split("=");
                        int goodId = Integer.parseInt(split[1]);
                        requestBusinessId(goodId);
                        break;
                    case 6:
                        Intent webData = new Intent(mContext, WebActivity.class);
                        if (topImage.getLinkAddr().contains("http")) {
                            webData.putExtra("url", topImage.getLinkAddr());
                        } else {
                            webData.putExtra("url", ApiServiceFactory.WEB_HOST + topImage.getLinkAddr());
                        }
                        mContext.startActivity(webData);
                        break;
                }
            }
        });
        return draweeView;
    }

    private void requestBusinessId(int goodId) {
        CustomApplication.getRetrofit().getBusinessId(goodId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccessful()) {
                    CustomToast.INSTANCE.showToast(CustomApplication.getContext(), "网络数据异常");
                    return;
                }

                String body = response.body();
                if (!TextUtils.isEmpty(body)) {
                    dealGood(body);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealGood(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject goods = jsonObject.optJSONObject("goods");
            int businessId = goods.optInt("businessId");
            Intent intent = new Intent(mContext, BusinessActivity.class);
            intent.putExtra("businessId", businessId);
            intent.putExtra("goodId", goods.optInt("id"));
            intent.putExtra(IntentFlag.KEY, IntentFlag.MAIN_GOOD_AD);
            mContext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }

}
