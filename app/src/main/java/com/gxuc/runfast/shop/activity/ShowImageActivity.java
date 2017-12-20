package com.gxuc.runfast.shop.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.data.ApiServiceFactory;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Devon on 2017/11/20.
 */

public class ShowImageActivity extends ToolBarActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_image)
    SimpleDraweeView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);
        ButterKnife.bind(this);
        String imageUrl = getIntent().getStringExtra("image");
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ivImage.setLayoutParams(new LinearLayout.LayoutParams(lp));
//        x.image().bind(ivImage, imageUrl, NetConfig.optionsPagerCache);
        ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(ImageRequest.fromUri(ApiServiceFactory.BASE_IMG_URL + topImage.getAdImages()))
//                .setUri(Uri.parse(ApiServiceFactory.BASE_IMG_URL + imageUrl))
                .setImageRequest(ImageRequest.fromUri(imageUrl))
                .setOldController(ivImage.getController())
                .build();
        ivImage.setController(controller);
    }

    @OnClick({R.id.iv_image, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }
}