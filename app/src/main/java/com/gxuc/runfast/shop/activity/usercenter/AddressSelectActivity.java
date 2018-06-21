package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.adapter.AddressSelectAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 收货地址管理
 */
public class AddressSelectActivity extends ToolBarActivity implements AddressSelectAdapter.OnItemClickListener {

    @BindView(R.id.recycler_address_list)
    RecyclerView recyclerView;

    private ArrayList<AddressBean> addressBeanList;
    private AddressSelectAdapter mSelectAdapter;
    private int mFlags;
    private int bid;
    private int isfull;
    private String full;
    private int businessId;
    private String lat;
    private String lng;
    private HashMap<String, String> paramMap;
    private boolean isFromCart;
    private JSONObject paramJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        ButterKnife.bind(this);
        setRightMsg("新增地址");
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }

    private void initData() {
        lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
        lng = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
        mFlags = getIntent().getIntExtra(IntentFlag.KEY, -1);
//        bid = getIntent().getIntExtra("bid", 0);
//        isfull = getIntent().getIntExtra("isfull", 0);
//        full = getIntent().getStringExtra("full");
//        businessId = getIntent().getIntExtra("businessId", 0);
        isFromCart = getIntent().getBooleanExtra("isFromCart", false);
        try {
            if (isFromCart) {
                paramJson = new JSONObject(getIntent().getStringExtra("paramJson"));
            } else {
                paramMap = (HashMap<String, String>) getIntent().getSerializableExtra("paramMap");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addressBeanList = new ArrayList<>();
        mSelectAdapter = new AddressSelectAdapter(addressBeanList, this);
        mSelectAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mSelectAdapter);
    }

    @OnClick(R.id.tv_right_title)
    public void onViewClicked() {
        Intent intent = new Intent(this, UpdateAddressActivity.class);
        intent.putExtra(IntentFlag.KEY, IntentFlag.ADD_ADDRESS);
        startActivity(intent);
    }

    private void getNetData() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getAddressList().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        if (!TextUtils.isEmpty(data)) {
                            dealAddressList(data);
                        }
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
//        CustomApplication.getRetrofit().postListAddress(userInfo.getId(), 1).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                dealAddressList(response.body());
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

//    private void dealAddressList(String body) {
//        //处理数据 显示地址
//        mData.clear();
//        AddressInfos addressInfos = GsonUtil.parseJsonWithGson(body, AddressInfos.class);
//        if (addressInfos.getRows().size() > 0) {
//            mData.addAll(addressInfos.getRows());
//            mSelectAdapter.notifyDataSetChanged();
//        }
//    }

    private void dealAddressList(String data) {
        //处理数据 显示地址
        addressBeanList = JsonUtil.fromJson(data, new TypeToken<ArrayList<AddressBean>>() {
        }.getType());
        mSelectAdapter.setList(addressBeanList);

    }

    @Override
    public void onItemClick(View view, int position) {
        AddressBean addressBean = addressBeanList.get(position);
        if (mFlags == IntentFlag.ORDER_ADDRESS) {
            if (isFromCart) {
                requestFromCartOrderInfo(addressBean);
            } else {
                requestSelectAddr(addressBean);
            }

        } else if (mFlags == IntentFlag.MANAGER_ADDRESS) {
            Intent intent = new Intent(this, UpdateAddressActivity.class);
            intent.putExtra(IntentFlag.KEY, IntentFlag.EDIT_ADDRESS);
            intent.putExtra("addressInfo", addressBean);
            startActivity(intent);
        } else if (mFlags == IntentFlag.PURCHASE) {
            Intent intent = new Intent();
            intent.putExtra("addressInfo", addressBean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void requestFromCartOrderInfo(final AddressBean addressBean) {

        final String toAddressId = paramJson.optString("toAddressId");
        try {
            paramJson.put("toAddressId", addressBean.id + "");
            paramJson.put("suportSelf", false + "");
            SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomApplication.getRetrofitNew().fillInDiy().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
                            ShopCartBean shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
                            Intent intent = new Intent();
                            intent.putExtra("shopCartBean", shopCartBean);
                            intent.putExtra("paramJson", paramJson.toString());
                            intent.putExtra("isFromCart", isFromCart);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramJson.put("toAddressId", toAddressId);
                        SharePreferenceUtil.getInstance().putStringValue("paramJson", paramJson.toString());
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

    private void requestSelectAddr(final AddressBean addressBean) {
        final String toAddressId = paramMap.get("toAddressId");
        paramMap.put("toAddressId", addressBean.id + "");
        paramMap.put("suportSelf", false + "");

        CustomApplication.getRetrofitNew().submitBusinessShopCart(paramMap).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        if (!TextUtils.equals("null", jsonObject.optString("data"))) {
                            ShopCartBean shopCartBean = JsonUtil.fromJson(jsonObject.optString("data"), ShopCartBean.class);
                            Intent intent = new Intent();
                            intent.putExtra("paramMap", (Serializable) paramMap);
                            intent.putExtra("shopCartBean", shopCartBean);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        paramMap.put("toAddressId", toAddressId);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });


//        CustomApplication.getRetrofit().selectAddr(bid, addressInfo.getLatitude(), addressInfo.getLongitude(), isfull, full).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body());
//                    if (jsonObject.optBoolean("success")) {
//                        Intent intent = new Intent();
//                        intent.putExtra("addressInfo", addressInfo);
//                        intent.putExtra("shippingPrice", jsonObject.optDouble("total"));
//                        intent.putExtra("ftotal", jsonObject.optDouble("ftotal"));
//                        intent.putExtra("oldShippingPrice", jsonObject.optDouble("showps"));
//                        setResult(IntentConfig.ADDRESS_SELECT, intent);
//                        finish();
//                    } else {
//                        showToastMessage(jsonObject.optString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                requestSelectAddr(addressInfo);
////                Intent intent = new Intent();
////                intent.putExtra("addressInfo", addressInfo);
////                setResult(IntentConfig.ADDRESS_SELECT, intent);
////                finish();
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }
}
