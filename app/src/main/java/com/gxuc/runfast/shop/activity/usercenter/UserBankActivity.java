package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.adapter.moneyadapter.CashUserNameAdapter;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.view.PromptDialogFragment;

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
 * 提现账号列表
 */
public class UserBankActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.view_money_list)
    RecyclerView recyclerView;

    private List<CashBankInfo> bankInfoList = new ArrayList<>();

    private CashUserNameAdapter adapter;
    private User userInfo;
    private PromptDialogFragment dialogFragment;
    private CashBankInfo bankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bank);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        dialogFragment = PromptDialogFragment.newInstance(getString(R.string.prompt), getString(R.string.delete_bank));
        dialogFragment.setLeftButton(getString(R.string.cancel), new GoToTripImpl());
        dialogFragment.setRightButton(getString(R.string.sure), new GoToTripImpl());
    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);
        adapter = new CashUserNameAdapter(bankInfoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getBankInfo();
    }

    @OnClick(R.id.tv_add_bank)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, AddBankActivity.class), 1001);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            bankInfo = bankInfoList.get(position);
            CustomUtils.showDialogFragment(getSupportFragmentManager(), dialogFragment);
        }
    }

    /***
     * 进入行程的点击事件
     */
    private class GoToTripImpl implements PromptDialogFragment.OnClickApi {

        @Override
        public void onClick(DialogFragment dialogFragment, View view) {
            switch (view.getId()) {
                case R.id.tv_ok:
                    if (bankInfo != null) {
                        deleteBankInfo(bankInfo.getId());
                    }
                    break;
                case R.id.tv_cancel:
                    dialogFragment.dismiss();
                    break;
            }

        }
    }

    /**
     * 提现账号
     *
     * @param
     */
    private void getBankInfo() {
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().getBankUser(1).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                dealBankUser(body);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealBankUser(String body) {
        try {
            JSONObject object = new JSONObject(body);
            JSONArray banks = object.getJSONArray("rows");
            int length = banks.length();
            if (length <= 0) {
                recyclerView.setVisibility(View.GONE);
                return;
            }
            bankInfoList.clear();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = banks.getJSONObject(i);
                CashBankInfo bankInfo = GsonUtil.parseJsonWithGson(jsonObject.toString(), CashBankInfo.class);
                bankInfoList.add(bankInfo);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除账号
     *
     * @param
     */
    private void deleteBankInfo(Integer id) {
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().deleteBankUser(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                dealDeleteBankUser(body);
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealDeleteBankUser(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            CustomToast.INSTANCE.showToast(this, msg);
            if (success) {
                getBankInfo();
            }
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
        getBankInfo();
    }
}
