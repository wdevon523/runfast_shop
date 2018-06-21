package com.gxuc.runfast.shop.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.activity.BusinessLicenceActivity;
import com.gxuc.runfast.shop.activity.BusinessNewActivity;
import com.gxuc.runfast.shop.activity.ShowImageActivity;
import com.gxuc.runfast.shop.adapter.BusinessImageAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.BusinessNewDetail;
import com.gxuc.runfast.shop.bean.business.BusinessDetail;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.BusinessActivity;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.ZFlowLayout;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.Arrays;

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

    RelativeLayout rlBusinessLicence;

//    ZFlowLayout flImgContain;

    private BusinessNewDetail businessDetail;

    Unbinder unbinder;
    private RecyclerView recyclerImage;
    private String[] images;
    private BusinessImageAdapter businessImageAdapter;
    private LinearLayout llTakeSelf;
    private String[] split;

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
        businessDetail = ((BusinessNewActivity) getActivity()).getBusiness();
        dealBusinessInfo();
    }

    private void iniView() {
        tvBusinessAddress = (TextView) findViewById(R.id.tv_business_address);
        tvBusinessPhone = (TextView) findViewById(R.id.tv_business_phone);
        tvBusinessRemark = (TextView) findViewById(R.id.tv_business_remark);
        tvDistributionTime = (TextView) findViewById(R.id.tv_distribution_time);
        rlBusinessLicence = (RelativeLayout) findViewById(R.id.rl_business_licence);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);
        llTakeSelf = (LinearLayout) findViewById(R.id.ll_take_self);

//        flImgContain = (ZFlowLayout) findViewById(R.id.ll_img_contain);
        rlBusinessLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BusinessLicenceActivity.class);
//                intent.putExtra("licence", businessDetail.getImgs());
                startActivity(intent);
            }
        });

        tvBusinessPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + businessDetail.mobile));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showToast("请先开启电话权限");
                    return;
                }
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerImage.setLayoutManager(linearLayoutManager);
        businessImageAdapter = new BusinessImageAdapter(getContext(), images);
        recyclerImage.setAdapter(businessImageAdapter);
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
        tvBusinessAddress.setText(businessDetail.address);
        tvBusinessPhone.setText(businessDetail.mobile);
        tvBusinessRemark.setText(businessDetail.content);
        llTakeSelf.setVisibility(businessDetail.suportSelf ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(businessDetail.face_image)) {
            images = businessDetail.face_image.split(",");
        }
        int imageLength = images == null ? 0 : images.length;
        if (!TextUtils.isEmpty(businessDetail.inner_image)) {
            split = businessDetail.inner_image.split(",");
        }
        int spitLength = split == null ? 0 : split.length;
        if (images != null) {
            images = Arrays.copyOf(images, imageLength + spitLength);
            System.arraycopy(split, 0, images, imageLength, spitLength);
            businessImageAdapter.setList(images);
        }

//        tvDistributionTime.setText(businessDetail.getWordTime());

//        flImgContain.removeAllViews();
//        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(15, 0, 15, 20);// 设置边距
//        if (businessDetail.getImgs() != null && businessDetail.getImgs().size() > 0) {
//            for (int i = 0; i < businessDetail.getImgs().size(); i++) {
//                View view = getActivity().getLayoutInflater().inflate(R.layout.item_image, null, false);
//                ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
////                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(300, 150);
////                ivImage.setLayoutParams(new LinearLayout.LayoutParams(lp));
//                x.image().bind(ivImage, UrlConstant.ImageBaseUrl + businessDetail.getImgs().get(i).imgUrl);
//                flImgContain.addView(view, layoutParams);
//                view.setTag(UrlConstant.ImageBaseUrl + businessDetail.getImgs().get(i).imgUrl);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), ShowImageActivity.class);
//                        intent.putExtra("image", v.getTag().toString());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
    }

}
