package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.bean.business.SafetyRecordImg;
import com.gxuc.runfast.shop.config.NetConfig;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.view.ZFlowLayout;

import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessLicenceActivity extends ToolBarActivity {

    @BindView(R.id.ll_img_contain)
    ZFlowLayout flImgContain;
    private ArrayList<SafetyRecordImg> businessLicenceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_licence);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        businessLicenceList = (ArrayList<SafetyRecordImg>) getIntent().getSerializableExtra("licence");
        flImgContain.removeAllViews();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 30, 30);// 设置边距
        if (businessLicenceList != null && businessLicenceList.size() > 0) {
            for (int i = 0; i < businessLicenceList.size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.item_image, null, false);
                ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
//                LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(296, 240);
//                ivImage.setLayoutParams(new LinearLayout.LayoutParams(lp));
                x.image().bind(ivImage, UrlConstant.ImageBaseUrl + businessLicenceList.get(i).imgUrl, NetConfig.optionsLogoImage);
                flImgContain.addView(view, layoutParams);
                view.setTag(UrlConstant.ImageBaseUrl + businessLicenceList.get(i).imgUrl);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BusinessLicenceActivity.this, ShowImageActivity.class);
                        intent.putExtra("image", v.getTag().toString());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

