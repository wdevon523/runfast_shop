package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.adapter.moneyadapter.SelectBankAdapter;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;

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

    private List<CashBankInfo> bankInfos = new ArrayList<>();
    private SelectBankAdapter bankAdapter;
    private User userInfo;
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
        List<CashBankInfo> bankInfoList = (List<CashBankInfo>) getIntent().getSerializableExtra("bankInfo");
        if (bankInfoList != null) {
            bankInfos.addAll(bankInfoList);
        }
        CashBankInfo bankInfo = new CashBankInfo();
        bankInfo.setBanktype("使用新卡提现");
        bankInfos.add(bankInfo);
        bankAdapter = new SelectBankAdapter(bankInfos,this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(bankAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            if (position == bankInfos.size() -1){
                startActivityForResult(new Intent(this,AddBankActivity.class),1001);
                return;
            }
            for (int i = 0; i < bankInfos.size(); i++) {
                bankInfos.get(i).setSelect(false);
            }
            bankInfos.get(position).setSelect(true);
            bankAdapter.notifyDataSetChanged();
            Intent intent = new Intent();
            intent.putExtra("bank",bankInfos.get(position));
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        getBankInfo();
    }

    /**
     * 提现账号
     * @param
     */
    private void getBankInfo() {
        CustomApplication.getRetrofit().getWathdrawallList(userInfo.getId()).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealWathdrawallList(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealWathdrawallList(String body) {
        try {
            JSONObject object = new JSONObject(body);
            JSONArray banks = object.getJSONArray("banks");
            int length = banks.length();
            if (length <= 0){
                return;
            }
            //获取已选择的银行卡
            for (int i = 0; i < bankInfos.size(); i++) {
                if (bankInfos.get(i).isSelect()){
                    bankInfoSelect = bankInfos.get(i);
                }
            }
            bankInfos.clear();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = banks.getJSONObject(i);
                CashBankInfo bankInfo = GsonUtil.parseJsonWithGson(jsonObject.toString(),CashBankInfo.class);
                if (bankInfo.getId().equals(bankInfoSelect.getId())){
                    bankInfo.setSelect(true);
                }
                bankInfos.add(bankInfo);
            }
            CashBankInfo bankInfo = new CashBankInfo();
            bankInfo.setBanktype("使用新卡提现");
            bankInfos.add(bankInfo);
            bankAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
