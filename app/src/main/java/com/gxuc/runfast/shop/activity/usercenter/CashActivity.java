package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 提现
 */
public class CashActivity extends ToolBarActivity {

    @BindView(R.id.tv_bank_mode)
    TextView tvBankMode;
    @BindView(R.id.et_account_money)
    EditText etAccountMoney;
    @BindView(R.id.tv_wallet_money)
    TextView tvWalletMoney;

    private User userInfo;

    private List<CashBankInfo> bankInfoList = new ArrayList<>();

    private Integer bankId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        tvWalletMoney.setText(String.valueOf(userInfo.getRemainder()));
        getBankInfo();
    }

    @OnClick({R.id.tv_bank_mode, R.id.tv_cash_all, R.id.btn_cash_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bank_mode:
                startActivityForResult(new Intent(this, SelectBankActivity.class).putExtra("bankInfo", (ArrayList) bankInfoList), 1001);
                break;
            case R.id.tv_cash_all:
                etAccountMoney.setText(String.valueOf(userInfo.getRemainder()));
                break;
            case R.id.btn_cash_mode:
                sendCash();
                break;
        }
    }

    /**
     * 提现账号
     *
     * @param
     */
    private void getBankInfo() {
        CustomApplication.getRetrofit().getWathdrawallList(userInfo.getId()).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealWathdrawalllList(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealWathdrawalllList(String body) {
        try {
            JSONObject object = new JSONObject(body);
            JSONArray banks = object.getJSONArray("banks");
            int length = banks.length();
            if (length <= 0) {
                tvBankMode.setText("请添加银行卡");
                return;
            }
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = banks.getJSONObject(i);
                CashBankInfo bankInfo = GsonUtil.parseJsonWithGson(jsonObject.toString(), CashBankInfo.class);
                if (i == 0) {
                    bankInfo.setSelect(true);
                }
                bankInfoList.add(bankInfo);
            }
            String account = bankInfoList.get(0).getAccount();
            if (account.length() > 4) {
                account = account.substring(account.length() - 4);
            }
            tvBankMode.setText(bankInfoList.get(0).getBanktype() + "(" + account + ")");
            bankId = bankInfoList.get(0).getId();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提现
     *
     * @param
     */
    private void sendCash() {
        String money = etAccountMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            CustomToast.INSTANCE.showToast(this, "请输入提现金额");
            return;
        }
        if (bankId == null) {
            CustomToast.INSTANCE.showToast(this, "请选择提现银行卡");
            return;
        }
        CustomApplication.getRetrofit().getCashSend(Double.parseDouble(money), 2, bankId).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealCashSend(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealCashSend(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            CustomToast.INSTANCE.showToast(this, msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        CashBankInfo bankInfo = (CashBankInfo) data.getSerializableExtra("bank");
        if (bankInfo != null) {
            int size = bankInfoList.size();
            for (int i = 0; i < size; i++) {
                if (bankInfoList.get(i).getId().equals(bankInfo.getId())) {
                    bankInfoList.get(i).setSelect(true);
                } else {
                    bankInfoList.get(i).setSelect(false);
                }
            }
            String account = bankInfo.getAccount();
            if (account.length() > 4) {
                account = account.substring(account.length() - 4);
            }
            tvBankMode.setText(bankInfo.getBanktype() + "(" + account + ")");
            bankId = bankInfo.getId();
        }
    }
}
