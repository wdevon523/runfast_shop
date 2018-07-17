package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.geocoder.RegeocodeAddress;
import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.Address;
import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.util.VaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    @BindView(R.id.cb_home)
    CheckBox cbHome;
    @BindView(R.id.cb_company)
    CheckBox cbCompany;
    @BindView(R.id.cb_school)
    CheckBox cbSchool;
    @BindView(R.id.cb_man)
    CheckBox cbMan;
    @BindView(R.id.cb_woman)
    CheckBox cbWoman;
    private String mUserName;
    private String mUserPhone;
    private String mHouseNumber;
    private RegeocodeAddress mRegeocodeAddress;
    private Address mAddressLat;
    private String mAddress;
    private int mFlags;
    private AddressBean mAddressInfo;
    private String mTvAddress;
    private int tag;
    private int gender = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        ButterKnife.bind(this);
        mFlags = getIntent().getIntExtra(IntentFlag.KEY, -1);
        initData();

        if (mFlags == 1) {
            mAddressInfo = (AddressBean) getIntent().getSerializableExtra("addressInfo");

            etUserName.setText(mAddressInfo.name);
            etUserPhone.setText(mAddressInfo.phone);
            etHouseNumber.setText(mAddressInfo.address);
            tvAddress.setText(mAddressInfo.userAddress);
            if (mAddressInfo.tag != null) {
                switch (mAddressInfo.tag) {
                    case 1:
                        tag = 1;
                        cbHome.setChecked(true);
                        break;
                    case 2:
                        tag = 2;
                        cbCompany.setChecked(true);
                        break;
                    case 3:
                        tag = 3;
                        cbSchool.setChecked(true);
                        break;
                }
            }

            if (mAddressInfo.gender != null) {
                cbMan.setChecked(mAddressInfo.gender == 1);
                cbWoman.setChecked(mAddressInfo.gender == 0);
                gender = mAddressInfo.gender;
            }
        } else {
            mBtnDeleteAddress.setVisibility(View.GONE);
        }
    }

    private void initData() {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mFlags == 0) {
            tvToolbarTitle.setText("新增地址");
        }
    }

    @OnClick({R.id.layout_select_address, R.id.btn_save_address, R.id.btn_delete_address, R.id.cb_home, R.id.cb_company, R.id.cb_school, R.id.cb_man, R.id.cb_woman})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_select_address:
                Intent intent = new Intent(this, AddressManagerActivity.class);
                if (mFlags == 1) {
                    intent.putExtra(IntentFlag.KEY, IntentFlag.EDIT_ADDRESS);
                    intent.putExtra("addressInfo", mAddressInfo);
                }
                startActivityForResult(intent, 1001);
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
            case R.id.cb_home:
                tag = cbHome.isChecked() ? 1 : -1;
                cbCompany.setChecked(false);
                cbSchool.setChecked(false);
                break;
            case R.id.cb_company:
                tag = cbCompany.isChecked() ? 2 : -1;
                cbHome.setChecked(false);
                cbSchool.setChecked(false);
                break;
            case R.id.cb_school:
                tag = cbSchool.isChecked() ? 3 : -1;
                cbHome.setChecked(false);
                cbCompany.setChecked(false);
                break;
            case R.id.cb_man:
                gender = 1;
                cbWoman.setChecked(false);
                cbMan.setChecked(true);
                break;
            case R.id.cb_woman:
                gender = 0;
                cbMan.setChecked(false);
                cbWoman.setChecked(true);
                break;
        }
    }

    private boolean checkAvailable() {
        mUserName = etUserName.getText().toString();
        mUserPhone = etUserPhone.getText().toString();
        mHouseNumber = etHouseNumber.getText().toString();
        mTvAddress = tvAddress.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            ToastUtil.showToast("请填入收货人名称");
            return true;
        } else if (TextUtils.isEmpty(mUserPhone)) {
            ToastUtil.showToast("请填入手机号");
            return true;
        } else if (mUserPhone.length() != 11 || !VaUtils.isMobileNo(mUserPhone)) {
            ToastUtil.showToast("请填入正确的手机号");
            return true;
        } else if (TextUtils.isEmpty(mHouseNumber)) {
            ToastUtil.showToast("请填入门牌号");
            return true;
        } else if (TextUtils.isEmpty(mTvAddress)) {
            ToastUtil.showToast("请选择地址");
            return true;
        }
        return false;
    }

    /**
     * 修改地址
     */
    private void editAddress() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        if (mAddressLat != null) {
//            CustomApplication.getRetrofit().postEditAddress(mAddressInfo.id,
//                    mUserName, mUserPhone, mTvAddress,
//                    mHouseNumber, String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude),
//                    mRegeocodeAddress.getProvince(), mRegeocodeAddress.getCity(), mRegeocodeAddress.getDistrict(), 1)
//                    .enqueue(new MyCallback<String>() {
//                        @Override
//                        public void onSuccessResponse(Call<String> call, Response<String> response) {
//                            dealAddress(response.body());
//                        }
//
//                        @Override
//                        public void onFailureResponse(Call<String> call, Throwable t) {
//
//                        }
//                    });

            CustomApplication.getRetrofitNew().updateAddress(mAddressInfo.id, mUserName, gender, mUserPhone, mTvAddress, mHouseNumber,
                    String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude), tag).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            ToastUtil.showToast("保存成功");
                            finish();
                        } else {
                            ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {

                }
            });
        } else {
//            CustomApplication.getRetrofit().postEditAddress(mAddressInfo.id,
//                    mUserName, mUserPhone, mTvAddress,
//                    mHouseNumber, mAddressInfo.longitude, mAddressInfo.latitude,
//                    mAddressInfo.provinceName, mAddressInfo.cityName, mAddressInfo.countyName, 1)
//                    .enqueue(new MyCallback<String>() {
//                        @Override
//                        public void onSuccessResponse(Call<String> call, Response<String> response) {
//                            dealAddress(response.body());
//                        }
//
//                        @Override
//                        public void onFailureResponse(Call<String> call, Throwable t) {
//
//                        }
//                    });

            CustomApplication.getRetrofitNew().updateAddress(mAddressInfo.id, mUserName, gender, mUserPhone, mTvAddress, mHouseNumber,
                    mAddressInfo.longitude, mAddressInfo.latitude, tag).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            ToastUtil.showToast("保存成功");
                            finish();
                        } else {
                            ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                ToastUtil.showToast("保存成功");
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
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().deleteAddress(mAddressInfo.id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("删除成功");
                        finish();
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });

//        CustomApplication.getRetrofit().postDeleteAddress(mAddressInfo.id).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                dealDeleteAddress(response.body());
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

    private void dealDeleteAddress(String body) {
        if (!TextUtils.isEmpty(body)) {
            ToastUtil.showToast("删除成功");
            finish();
        }
    }

    /**
     * 新增地址
     */
    private void toAddAddress() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().addAddress(mUserName, gender, mUserPhone, mAddress, mHouseNumber,
                String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude), tag).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("添加成功");
                        finish();
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });

//        Integer id = UserService.getUserInfo().getId();
//        CustomApplication.getRetrofit().postAddAddress(mUserName, mUserPhone, mAddress,
//                mHouseNumber, String.valueOf(mAddressLat.latLng.longitude), String.valueOf(mAddressLat.latLng.latitude),
//                mRegeocodeAddress.getProvince(), mRegeocodeAddress.getCity(), mRegeocodeAddress.getDistrict(), 1)
//                .enqueue(new MyCallback<String>() {
//                    @Override
//                    public void onSuccessResponse(Call<String> call, Response<String> response) {
//                        dealAddress(response.body());
//                    }
//
//                    @Override
//                    public void onFailureResponse(Call<String> call, Throwable t) {
//
//                    }
//                });
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
