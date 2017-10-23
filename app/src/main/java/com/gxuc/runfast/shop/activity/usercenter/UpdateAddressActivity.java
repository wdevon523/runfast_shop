package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.geocoder.RegeocodeAddress;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.Address;
import com.gxuc.runfast.shop.bean.address.AddressInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 地址新增和修改
 */
public class UpdateAddressActivity extends ToolBarActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_house_number)
    EditText etHouseNumber;
    @BindView(R.id.btn_delete_address)
    Button mBtnDeleteAddress;
    private String mUserName;
    private String mUserPhone;
    private String mHouseNumber;
    private RegeocodeAddress mRegeocodeAddress;
    private Address mAddressLat;
    private String mAddress;
    private int mFlags;
    private AddressInfo mAddressInfo;
    private String mTvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        ButterKnife.bind(this);
        mFlags = getIntent().getFlags();
        if (mFlags == 1) {
            mAddressInfo = getIntent().getParcelableExtra("addressInfo");
            etUserName.setText(mAddressInfo.getName());
            etUserPhone.setText(mAddressInfo.getPhone());
            etHouseNumber.setText(mAddressInfo.getAddress());
            tvAddress.setText(mAddressInfo.getUserAddress());
        } else {
            mBtnDeleteAddress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mFlags == 0) {
            tvToolbarTitle.setText("新增地址");
        }
    }

    @OnClick({R.id.layout_select_address, R.id.btn_save_address, R.id.btn_delete_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_select_address:
                startActivityForResult(new Intent(this, AddressManagerActivity.class), 1001);
                break;
            case R.id.btn_save_address:
                if (!checkAvailable()) {
                    if (mFlags == 0) {
                        toAddAddress();
                    } else {
                        editAddress();
                    }
                }
                break;
            case R.id.btn_delete_address:
                deleteAddress();
                break;
        }
    }

    private boolean checkAvailable() {
        mUserName = etUserName.getText().toString();
        mUserPhone = etUserPhone.getText().toString();
        mHouseNumber = etHouseNumber.getText().toString();
        mTvAddress = tvAddress.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            CustomToast.INSTANCE.showToast(this, "请填入收货人名称");
            return true;
        } else if (TextUtils.isEmpty(mUserPhone)) {
            CustomToast.INSTANCE.showToast(this, "请填入手机号");
            return true;
        } else if (TextUtils.isEmpty(mHouseNumber)) {
            CustomToast.INSTANCE.showToast(this, "请填入门牌号");
            return true;
        } else if (TextUtils.isEmpty(mTvAddress)) {
            CustomToast.INSTANCE.showToast(this, "请选择地址");
            return true;
        }
        return false;
    }

    /**
     * 修改地址
     */
    private void editAddress() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        if (mAddressLat != null) {
            CustomApplication.getRetrofit().postEditAddress(mAddressInfo.getId(),
                    mUserName, mUserPhone, mAddress,
                    mHouseNumber, String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude),
                    mRegeocodeAddress.getProvince(), mRegeocodeAddress.getCity(), mRegeocodeAddress.getDistrict())
                    .enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealAddress(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });
        } else {
            CustomApplication.getRetrofit().postEditAddress(mAddressInfo.getId(),
                    mUserName, mUserPhone, mAddress,
                    mHouseNumber, String.valueOf(mAddressInfo.getLongitude()), String.valueOf(mAddressInfo.getLatitude()),
                    mAddressInfo.getProvinceName(), mAddressInfo.getCityName(), mAddressInfo.getCountyName())
                    .enqueue(new MyCallback<String>() {
                        @Override
                        public void onSuccessResponse(Call<String> call, Response<String> response) {
                            dealAddress(response.body());
                        }

                        @Override
                        public void onFailureResponse(Call<String> call, Throwable t) {

                        }
                    });
        }
    }

    private void dealAddress(String body) {
        if (!TextUtils.isEmpty(body)) {
            try {
                JSONObject object = new JSONObject(body);
                boolean success = object.optBoolean("success");
                String msg = object.optString("msg");
                CustomToast.INSTANCE.showToast(this, "保存成功");
                if (success) {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 删除地址
     */
    private void deleteAddress() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().postDeleteAddress(mAddressInfo.getId()).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealDeleteAddress(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealDeleteAddress(String body) {
        if (!TextUtils.isEmpty(body)) {
            CustomToast.INSTANCE.showToast(this, "删除成功");
            finish();
        }
    }

    /**
     * 新增地址
     */
    private void toAddAddress() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
//        Integer id = UserService.getUserInfo().getId();
        CustomApplication.getRetrofit().postAddAddress(mUserName, mUserPhone, mAddress,
                mHouseNumber, String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude),
                mRegeocodeAddress.getProvince(), mRegeocodeAddress.getCity(), mRegeocodeAddress.getDistrict())
                .enqueue(new MyCallback<String>() {
                    @Override
                    public void onSuccessResponse(Call<String> call, Response<String> response) {
                        dealAddress(response.body());
                    }

                    @Override
                    public void onFailureResponse(Call<String> call, Throwable t) {

                    }
                });
//        CustomToast.INSTANCE.showToast(this,"添加成功");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        mAddress = data.getStringExtra("address");
        mRegeocodeAddress = data.getParcelableExtra("addressInfo");
        mAddressLat = data.getParcelableExtra("addressLat");
        tvAddress.setText(mAddress);
    }

}
