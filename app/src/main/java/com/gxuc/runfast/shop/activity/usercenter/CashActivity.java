package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.util.ToastUtil;

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

    private UserInfo userInfo;

    private ArrayList<CashBankInfo> bankInfoList = new ArrayList<>();

    private CashBankInfo bankInfoSelect;

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
        tvWalletMoney.setText(String.valueOf(userInfo.remainder));
        getBankInfo();
    }

    @OnClick({R.id.tv_bank_mode, R.id.tv_cash_all, R.id.btn_cash_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bank_mode:
                startActivityForResult(new Intent(this, SelectBankActivity.class).putExtra("bankInfo", bankInfoSelect), 1001);
                break;
            case R.id.tv_cash_all:
                etAccountMoney.setText(String.valueOf(userInfo.remainder));
                etAccountMoney.setSelection(etAccountMoney.getText().length());
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
        if (bankInfoList.size() <= 0) {
            tvBankMode.setText("请添加银行卡");
            return;
        }

        String account = bankInfoList.get(0).account;
        if (account.length() > 4) {
            account = account.substring(account.length() - 4);
        }
        tvBankMode.setText(bankInfoList.get(0).banktype + "(" + account + ")");
        bankInfoSelect = bankInfoList.get(0);

    }


    /**
     * 提现
     *
     * @param
     */
    private void sendCash() {
        String money = etAccountMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ToastUtil.showToast("请输入提现金额");
            return;
        }
        if (bankInfoSelect == null) {
            ToastUtil.showToast("请选择提现银行卡");
            return;
        }
        CustomApplication.getRetrofitNew().getCashSend(Double.parseDouble(money), bankInfoSelect.id, "").enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("申请已提交，2个工作日内审核处理请耐心等待");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        bankInfoSelect = (CashBankInfo) data.getSerializableExtra("bankInfo");
        if (bankInfoSelect != null) {

            String account = bankInfoSelect.account;
            if (account.length() > 4) {
                account = account.substring(account.length() - 4);
            }
            tvBankMode.setText(bankInfoSelect.banktype + "(" + account + ")");
        }
    }
}
