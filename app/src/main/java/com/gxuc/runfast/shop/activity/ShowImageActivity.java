package com.gxuc.runfast.shop.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Devon on 2017/11/20.
 */

public class ShowImageActivity extends ToolBarActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.text_num)
    TextView text_num;

    private int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);
        ButterKnife.bind(this);
        final String[] images = getIntent().getStringArrayExtra("image");
        currentPosition = getIntent().getIntExtra("position", 0);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ivImage.setLayoutParams(new LinearLayout.LayoutParams(lp));
//        x.image().bind(ivImage, imageUrl, NetConfig.optionsPagerCache);
//        ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
////                .setLowResImageRequest(ImageRequest.fromUri(ApiServiceFactory.BASE_IMG_URL + topImage.getAdImages()))
////                .setUri(Uri.parse(ApiServiceFactory.BASE_IMG_URL + imageUrl))
//                .setImageRequest(ImageRequest.fromUri(imageUrl))
//                .setOldController(ivImage.getController())
//                .build();
//        ivImage.setController(controller);

        List<View> listViews = new ArrayList<View>();

        for (int i = 0; i < images.length; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.item_image_viewpager, null);
            final PhotoView photoview = (PhotoView) view.findViewById(R.id.photoview);
            photoview.setAdjustViewBounds(true);
            photoview.setScaleType(ImageView.ScaleType.FIT_CENTER);

            String url = images[i];
            Glide.with(this)
                    .load(UrlConstant.ImageBaseUrl + url)
                    .skipMemoryCache(true)//不缓存到内存
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.gugong_details)
//                    .error(R.drawable.gugong_details)
                    .into(photoview);
            listViews.add(view);
        }

        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(listViews);
        view_pager.setAdapter(photoPagerAdapter);
        //通过viewPager.setCurrentItem来定义当前显示哪一个图片，position由上一个页面传过来
        view_pager.setCurrentItem(currentPosition);
        text_num.setText(currentPosition + 1 + "/" + images.length);

        photoPagerAdapter.notifyDataSetChanged();
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                text_num.setText(currentPosition + 1 + "/" + images.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_image:
//                finish();
//                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    public class PhotoPagerAdapter extends PagerAdapter {
        private List<View> list;

        public PhotoPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}