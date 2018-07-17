package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.adapter.ShopCartAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.CartItemsBean;
import com.gxuc.runfast.shop.bean.ShopCartBean;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomProgressDialog;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class ShopCartActivity extends ToolBarActivity {

    @BindView(R.id.shopping_cart_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private ArrayList<ShopCartBean> shopCartBeanList;
    private ShopCartAdapter shopCartAdapter;
    private JSONArray shopCartArray;
    private int checkBusinessPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }


    private void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void initData() {
        shopCartBeanList = new ArrayList<>();
        shopCartAdapter = new ShopCartAdapter(this, shopCartBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(shopCartAdapter);

        smartRefreshLayout.setEnableLoadMore(false);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearRecyclerViewData();
                        refreshData();
                        smartRefreshLayout.finishRefresh();
//                        smartRefreshLayout.setEnableLoadMore(true);//恢复上拉状态
                    }
                }, 1000);
            }

        });

        shopCartAdapter.setOnCheckListener(new ShopCartAdapter.OnCheckListener() {
            @Override
            public void onChecked(int businessId, CartItemsBean cartItemsBean, int position) {
                requestCheckCart(businessId, position, false, false);
            }
        });

        shopCartAdapter.setOnAllCheckListener(new ShopCartAdapter.OnAllCheckListener() {
            @Override
            public void onAllChecked(int businessId, boolean checked) {
                requestCheckCart(businessId, -1, true, checked);
            }
        });

        shopCartAdapter.setPayListener(new ShopCartAdapter.OnPayListener() {
            @Override
            public void onPay(int businessId, int position) {
                try {
                    JSONObject jsonObject = shopCartArray.getJSONObject(position);
                    JSONArray jsonArray = new JSONArray();
                    for (int j = 0; j < jsonObject.optJSONArray("cartItems").length(); j++) {
                        if (jsonObject.optJSONArray("cartItems").getJSONObject(j).optBoolean("checked")) {
                            jsonArray.put(jsonObject.optJSONArray("cartItems").getJSONObject(j));
                        }

                    }
                    SharePreferenceUtil.getInstance().putStringValue("BusinessShopCart", jsonArray.toString());
                    SharePreferenceUtil.getInstance().putIntValue("businessId", businessId);

                    Intent intent = new Intent(ShopCartActivity.this, SubmitOrderActivity.class);
                    intent.putExtra("isFromCart", true);
                    intent.putExtra("businessId", businessId);
                    intent.putExtra("suportSelf", shopCartBeanList.get(position).suportSelf);

                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        shopCartAdapter.setOnDeleteListener(new ShopCartAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int businessId, int position) {
                requestDelete(businessId, position);
            }
        });
    }

    private void requestDelete(int businessId, int position) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < shopCartArray.length(); i++) {
                if (shopCartArray.getJSONObject(i).optInt("businessId") == businessId) {
                    checkBusinessPosition = i;
                    shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(position).put("num", "");
                    jsonArray.put(shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(position));
                    break;
                }
            }
            SharePreferenceUtil.getInstance().putStringValue("shopCart", jsonArray.toString());
            SharePreferenceUtil.getInstance().putIntValue("businessId", businessId);
            CustomProgressDialog.startProgressDialog(this);
            CustomApplication.getRetrofitNew().subShopCart(businessId).enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    CustomProgressDialog.stopProgressDialog();
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            if (!TextUtils.equals("null", jsonObject.optString("data")) && !TextUtils.isEmpty(jsonObject.optString("data"))) {
                                dealChecked(jsonObject.optString("data"));
                                shopCartArray.put(checkBusinessPosition, jsonObject.optJSONObject("data"));
                            } else {
                                requestAllShopCart();
                            }
                        } else {
                            ToastUtil.showToast(jsonObject.optString("errorMsg"));
                            requestAllShopCart();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {
                    CustomProgressDialog.stopProgressDialog();
                    requestAllShopCart();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestCheckCart(int businessId, int position, boolean isAllCheck,
                                  boolean checked) {
//        try {
//            JSONArray jsonArray = new JSONArray();
//            for (int i = 0; i < shopCartBeanList.size(); i++) {
//              for
//                    for (int j = 0; j < shopCartBeanList.get(i).cartItems.size(); j++) {
//                        if (shopCartBeanList.get(i).cartItems.get(j).checked) {
//                            String s = JsonUtil.object2Json(shopCartBeanList.get(i).cartItems.get(j));
//                            jsonArray.put(s);
//                            JSONObject jsonKey = new JSONObject();
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("goodsId", shopCartBeanList.get(i).cartItems.get(j).key.goodsId + "");
//                            jsonObject.put("standarId", shopCartBeanList.get(i).cartItems.get(j).key.standarId);
//                            if (shopCartBeanList.get(i).cartItems.get(j).key.optionIdPairList != null && shopCartBeanList.get(i).cartItems.get(j).key.optionIdPairList.size() > 0) {
//                                JSONArray optionIdPairArray = new JSONArray();
//                                for (int k = 0; k < shopCartBeanList.get(i).cartItems.get(j).key.optionIdPairList.size(); k++) {
//                                    JSONObject optionIdPairObject = new JSONObject();
//                                    optionIdPairObject.put("optionId", shopCartBeanList.get(i).cartItems.get(j).key.optionIdPairList.get(k).optionId);
//                                    optionIdPairObject.put("subOptionId", shopCartBeanList.get(i).cartItems.get(j).key.optionIdPairList.get(k).subOptionId);
//                                    optionIdPairArray.put(optionIdPairObject);
//                                }
//                                jsonObject.put("optionIdPairList", optionIdPairArray);
//                            }
//                            jsonKey.put("key", jsonObject);
//                            jsonKey.put("num", shopCartBeanList.get(i).cartItems.get(j).num);
//                            jsonArray.put(jsonKey);
//                        }
//                    }
//                }
//            }
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < shopCartArray.length(); i++) {
                if (shopCartArray.getJSONObject(i).optInt("businessId") == businessId) {
                    if (!isAllCheck) {
                        checkBusinessPosition = i;
                        shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(position).put("checked", !shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(position).optBoolean("checked"));
                    }

                    for (int j = 0; j < shopCartArray.getJSONObject(i).optJSONArray("cartItems").length(); j++) {
                        if (isAllCheck) {
                            shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(j).put("checked", checked);
                        }

                        if (shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(j).optBoolean("checked")) {
                            jsonArray.put(shopCartArray.getJSONObject(i).optJSONArray("cartItems").getJSONObject(j));
                        }

                    }
                }
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

            SharePreferenceUtil.getInstance().putStringValue("BusinessShopCart", jsonArray.toString());
            SharePreferenceUtil.getInstance().putIntValue("businessId", businessId);

            CustomProgressDialog.startProgressDialog(this);
            CustomApplication.getRetrofitNew().checkShopCart().enqueue(new MyCallback<String>() {
                @Override
                public void onSuccessResponse(Call<String> call, Response<String> response) {
                    CustomProgressDialog.stopProgressDialog();
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optBoolean("success")) {
                            dealChecked(jsonObject.optString("data"));
                        } else {
                            ToastUtil.showToast(jsonObject.optString("errorMsg"));
                            requestAllShopCart();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailureResponse(Call<String> call, Throwable t) {
                    CustomProgressDialog.stopProgressDialog();
                    requestAllShopCart();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dealChecked(String data) {
        ShopCartBean shopCartBean = JsonUtil.fromJson(data, ShopCartBean.class);
        shopCartBeanList.set(checkBusinessPosition, shopCartBean);
//        shopCartAdapter.notifyItemChanged(checkBusinessPosition);
        shopCartAdapter.notifyDataSetChanged();
    }

    private void refreshData() {
//        clearRecyclerViewData();
        requestAllShopCart();
    }

    private void clearRecyclerViewData() {
        shopCartBeanList.clear();
        shopCartAdapter.setList(shopCartBeanList);
    }

    private void requestAllShopCart() {
        CustomApplication.getRetrofitNew().getAllShopCart().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        String data = jsonObject.optString("data");
                        shopCartArray = jsonObject.optJSONArray("data");
                        dealShopCart(data);
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

    private void setListener() {

    }

    private void dealShopCart(String data) {
        shopCartBeanList = JsonUtil.fromJson(data, new TypeToken<ArrayList<ShopCartBean>>() {
        }.getType());
        shopCartAdapter.setList(shopCartBeanList);
    }

}
