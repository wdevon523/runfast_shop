package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.supportv1.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.adapter.moneyadapter.CashUserNameAdapter;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.CashBankInfo;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.CustomUtils;
import com.gxuc.runfast.shop.util.GsonUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.gxuc.runfast.shop.view.PromptDialogFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

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

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_money_list)
    RecyclerView recyclerView;

    private final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private boolean isLastPage = false;

    private CashUserNameAdapter adapter;
    private UserInfo userInfo;
    private PromptDialogFragment dialogFragment;
    private ArrayList<CashBankInfo> cashBankInfoList;
    private CashBankInfo cashBankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bank);
        ButterKnife.bind(this);
        initView();
        initData();
        refreshData();
    }

    private void initView() {
        dialogFragment = PromptDialogFragment.newInstance(getString(R.string.prompt), getString(R.string.delete_bank));
        dialogFragment.setLeftButton(getString(R.string.cancel), new GoToTripImpl());
        dialogFragment.setRightButton(getString(R.string.sure), new GoToTripImpl());
    }

    private void initData() {
        userInfo = UserService.getUserInfo(this);
        cashBankInfoList = new ArrayList<>();
        adapter = new CashUserNameAdapter(cashBankInfoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearRecyclerViewData();
                        refreshData();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.setEnableLoadMore(true);//恢复上拉状态
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage++;
                        getBankInfo();
                        smartRefreshLayout.finishLoadMore();
//                        refreshlayout.setLoadmoreFinished(true);
                    }
                }, 1000);
            }

        });
    }

    private void refreshData() {
        currentPage = FIRST_PAGE;
        isLastPage = false;
        clearRecyclerViewData();
        getBankInfo();
    }

    private void clearRecyclerViewData() {
        cashBankInfoList.clear();
        adapter.setList(cashBankInfoList);
    }


    @OnClick(R.id.tv_add_bank)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, AddBankActivity.class), 1001);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            Integer position = (Integer) v.getTag();
            cashBankInfo = cashBankInfoList.get(position);
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
                    if (cashBankInfo != null) {
                        deleteBankInfo(cashBankInfo.id);
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

        if (isLastPage) {
            ToastUtil.showToast(R.string.load_all_date);
            return;
        }

        CustomApplication.getRetrofitNew().getBankList().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        dealBankUser(jsonObject.optJSONArray("data"));
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

    private void dealBankUser(JSONArray data) {

        if (data == null || data.length() == 0) {
            isLastPage = true;
            return;
        }

        cashBankInfoList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<CashBankInfo>>() {
        }.getType());
        if (cashBankInfoList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        adapter.setList(cashBankInfoList);

//        try {
//            JSONObject object = new JSONObject(body);
//            JSONArray banks = object.getJSONArray("rows");
//            int length = banks.length();
//            if (length <= 0) {
//                recyclerView.setVisibility(View.GONE);
//                return;
//            }
//            bankInfoList.clear();
//            for (int i = 0; i < length; i++) {
//                JSONObject jsonObject = banks.getJSONObject(i);
//                CashBankInfo bankInfo = GsonUtil.parseJsonWithGson(jsonObject.toString(), CashBankInfo.class);
//                bankInfoList.add(bankInfo);
//            }
//            adapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
        CustomApplication.getRetrofitNew().deleteBank(id).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        ToastUtil.showToast("删除成功");
                        getBankInfo();
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

    private void dealDeleteBankUser(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            ToastUtil.showToast(msg);
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
        refreshData();
    }
}
