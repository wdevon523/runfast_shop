package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.LoginQucikActivity;
import com.gxuc.runfast.shop.activity.MainActivity;
import com.gxuc.runfast.shop.adapter.MessageAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.message.MessageInfo;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    Unbinder unbinder;
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;
    @BindView(R.id.ll_empty_message)
    LinearLayout llEmptyMessage;
    @BindView(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private Integer page = 1;
    private ArrayList<MessageInfo> messageInfoList;
    private MessageAdapter mAdapter;
    private UserInfo userInfo;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
//        toolbarTitle.setText("消息");
        initData();
        return view;
    }

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvMessageList.setLayoutManager(manager);
        messageInfoList = new ArrayList<>();
        mAdapter = new MessageAdapter(getActivity(), messageInfoList);
        mRvMessageList.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            userInfo = UserService.getUserInfo(getActivity());
            getNetData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            StatusUtil.setUseStatusBarColor(getActivity(), getResources().getColor(R.color.white));
            StatusUtil.setSystemStatus(getActivity(), true, true);
            userInfo = UserService.getUserInfo(getActivity());
            if (userInfo == null) {
                messageInfoList.clear();
                mAdapter.notifyDataSetChanged();
                llNotLogin.setVisibility(View.VISIBLE);
                llEmptyMessage.setVisibility(View.GONE);
                return;
            }
            getNetData();
        }
    }

    /**
     * 获取消息
     */
    private void getNetData() {
        if (userInfo == null) {
            return;
        }

        CustomApplication.getRetrofitNew().getMessageList().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showToast("网络数据异常");
                    return;
                }

                String body = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (!body.contains("{\"relogin\":1}") && jsonObject.optBoolean("success")) {
                        if (!TextUtils.isEmpty(body)) {
                            dealMessageList(jsonObject.optJSONArray("data"));
                            llNotLogin.setVisibility(View.GONE);
                        }
                    } else {
                        messageInfoList.clear();
                        mAdapter.notifyDataSetChanged();
                        llNotLogin.setVisibility(View.VISIBLE);
                        llEmptyMessage.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });
    }

    private void dealMessageList(JSONArray data) {
        if (data != null && data.length() > 0) {
            messageInfoList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<MessageInfo>>() {
            }.getType());
            mAdapter.setList(messageInfoList);
            llEmptyMessage.setVisibility(messageInfoList.size() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginQucikActivity.class));
    }
}
