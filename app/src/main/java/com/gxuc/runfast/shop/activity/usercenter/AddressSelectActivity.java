package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.shop.adapter.AddressSelectAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.address.AddressInfo;
import com.gxuc.runfast.shop.config.IntentConfig;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.address.AddressInfos;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收货地址管理
 */
public class AddressSelectActivity extends ToolBarActivity implements Callback<String>, AddressSelectAdapter.OnItemClickListener {

    @BindView(R.id.recycler_address_list)
    RecyclerView recyclerView;

    private List<AddressInfo> mData;
    private AddressSelectAdapter mSelectAdapter;
    private int mFlags;
    private int bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }

    private void initData() {
        mFlags = getIntent().getFlags();
        bid = getIntent().getIntExtra("bid", 0);
        mData = new ArrayList<>();
        mSelectAdapter = new AddressSelectAdapter(mData, this);
        mSelectAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mSelectAdapter);
    }

    @OnClick(R.id.layout_add_address)
    public void onViewClicked() {
        Intent intent = new Intent(this, UpdateAddressActivity.class);
        intent.setFlags(IntentFlag.ADD_ADDRESS);
        startActivity(intent);
    }

    private void getNetData() {
        User userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().postListAddress().enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        String data = response.body();
        if (response.isSuccessful()) {
            ResolveData(data);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        CustomToast.INSTANCE.showToast(this, "网络异常");
    }

    private void ResolveData(String data) {
        //处理数据 显示地址
        mData.clear();
        AddressInfos addressInfos = GsonUtil.parseJsonWithGson(data, AddressInfos.class);
        if (addressInfos.getRows().size() > 0) {
            mData.addAll(addressInfos.getRows());
            mSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        AddressInfo addressInfo = mData.get(position);
        if (mFlags == IntentFlag.ORDER_ADDRESS) {
            requestSelectAddr(addressInfo);
        } else if (mFlags == IntentFlag.MANAGER_ADDRESS) {
            Intent intent = new Intent(this, UpdateAddressActivity.class);
            intent.setFlags(IntentFlag.EDIT_ADDRESS);
            intent.putExtra("addressInfo", addressInfo);
            startActivity(intent);
        }
    }

    private void requestSelectAddr(final AddressInfo addressInfo) {
        CustomApplication.getRetrofit().selectAddr(bid, addressInfo.getLatitude(), addressInfo.getLongitude()).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent();
                        intent.putExtra("addressInfo", addressInfo);
                        setResult(IntentConfig.ADDRESS_SELECT, intent);
                        finish();
                    } else {
                        showToastMessage(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                requestSelectAddr(addressInfo);
//                Intent intent = new Intent();
//                intent.putExtra("addressInfo", addressInfo);
//                setResult(IntentConfig.ADDRESS_SELECT, intent);
//                finish();
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }
}
