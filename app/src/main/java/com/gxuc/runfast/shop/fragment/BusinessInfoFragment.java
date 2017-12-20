package com.gxuc.runfast.shop.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.ShowImageActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.business.BusinessDetail;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.view.ZFlowLayout;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 商家
 * A simple {@link Fragment} subclass.
 */
public class BusinessInfoFragment extends LazyFragment {

    TextView tvBusinessAddress;

    TextView tvBusinessPhone;

    TextView tvBusinessRemark;

    TextView tvDistributionTime;

    ZFlowLayout flImgContain;

    Unbinder unbinder;
    private BusinessDetail businessDetail;

    public BusinessInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateViewLazy(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_business_info);
        iniView();
//        getBusinessEvaluate(((BusinessActivity) getActivity()).getBusinessId());
        businessDetail = ((BusinessActivity) getActivity()).getBusiness();
        dealBusinessInfo();
    }

    private void iniView() {
        tvBusinessAddress = (TextView) findViewById(R.id.tv_business_address);
        tvBusinessPhone = (TextView) findViewById(R.id.tv_business_phone);
        tvBusinessRemark = (TextView) findViewById(R.id.tv_business_remark);
        tvDistributionTime = (TextView) findViewById(R.id.tv_distribution_time);
        flImgContain = (ZFlowLayout) findViewById(R.id.ll_img_contain);
        tvBusinessPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + businessDetail.getMobile()));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
    }

    /**
     * 获取商家评价
     */
//    private void getBusinessEvaluate(int id) {
//        CustomApplication.getRetrofit().getBusinessInfo(id, ).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
////                dealBusinessInfo(response.body());
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    private void dealBusinessInfo() {
        tvBusinessAddress.setText(businessDetail.getAddress());
        tvBusinessPhone.setText(businessDetail.getMobile());
        tvBusinessRemark.setText(businessDetail.getContent());
        tvDistributionTime.setText(businessDetail.getWordTime());

        flImgContain.removeAllViews();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 15, 20);// 设置边距
        if (businessDetail.getImgs() != null && businessDetail.getImgs().size() > 0) {
            for (int i = 0; i < businessDetail.getImgs().size(); i++) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.item_image, null, false);
                ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(500, 300);
                ivImage.setLayoutParams(new LinearLayout.LayoutParams(lp));
                x.image().bind(ivImage, UrlConstant.ImageBaseUrl + businessDetail.getImgs().get(i).imgUrl);
                flImgContain.addView(view, layoutParams);
                view.setTag(UrlConstant.ImageBaseUrl + businessDetail.getImgs().get(i).imgUrl);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ShowImageActivity.class);
                        intent.putExtra("image", v.getTag().toString());
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
