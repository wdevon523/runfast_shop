package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.moneyadapter.SelectBankAdapter;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择银行卡
 */
public class SelectBankActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.recycler_bank_list)
    RecyclerView recyclerView;

    private ArrayList<CashBankInfo> bankInfoList = new ArrayList<>();
    private SelectBankAdapter bankAdapter;
    private UserInfo userInfo;
    //已选择银行卡
    private CashBankInfo bankInfoSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);
        bankInfoSelect = (CashBankInfo) getIntent().getSerializableExtra("bankInfo");
        getBankInfo();
        bankAdapter = new SelectBankAdapter(bankInfoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bankAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            if (position == bankInfoList.size() - 1) {
                startActivityForResult(new Intent(this, AddBankActivity.class), 1001);
                return;
            }
            for (int i = 0; i < bankInfoList.size(); i++) {
                bankInfoList.get(i).isSelect = false;
            }
            bankInfoList.get(position).isSelect = true;
            bankAdapter.notifyDataSetChanged();
            Intent intent = new Intent();
            intent.putExtra("bankInfo", bankInfoList.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        getBankInfo();
    }

    /**
     * 提现账号
     *
     * @param
     */
    private void getBankInfo() {
        CustomApplication.getRetrofitNew().getBankList().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealBankUser(jsonObject.optString("data"));
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

    private void dealBankUser(String data) {
        bankInfoList = JsonUtil.fromJson(data, new TypeToken<ArrayList<CashBankInfo>>() {
        }.getType());

        for (int i = 0; i < bankInfoList.size(); i++) {
            if (bankInfoList.get(i).id == bankInfoSelect.id) {
                bankInfoList.get(i).isSelect = true;
            }
        }

        CashBankInfo bankInfo = new CashBankInfo();
        bankInfo.banktype = "使用新卡提现";
        bankInfoList.add(bankInfo);
        bankAdapter.setList(bankInfoList);
    }

}
