package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包
 */
public class WalletActivity extends ToolBarActivity {


    @BindView(R.id.tv_wallet_money)
    TextView tvWalletMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        setRightMsg("收支明细");
        initView();
    }

    private void initView() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            return;
        }
        tvWalletMoney.setText(String.valueOf(userInfo.remainder));
    }

    @OnClick({R.id.tv_right_title, R.id.btn_wallet_recharge, R.id.btn_wallet_withdrawals, R.id.layout_withdrawals_record, R.id.layout_withdrawals_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title://收支明细
                startActivity(new Intent(this,MoneyDetailActivity.class));
                break;
            case R.id.btn_wallet_recharge://充值
                startActivity(new Intent(this,WalletRechargeActivity.class));
                break;
            case R.id.btn_wallet_withdrawals://提现
                startActivity(new Intent(this,CashActivity.class));
                break;
            case R.id.layout_withdrawals_record://提现记录
                startActivity(new Intent(this,CashRecordActivity.class));
                break;
            case R.id.layout_withdrawals_account://提现账号
                startActivity(new Intent(this,UserBankActivity.class));
                break;
        }
    }
}
