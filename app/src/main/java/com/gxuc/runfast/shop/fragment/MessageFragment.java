package com.gxuc.runfast.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.LoginActivity;
import com.gxuc.runfast.shop.adapter.MessageAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.message.MessageInfo;
import com.gxuc.runfast.shop.bean.message.MessageInfos;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;
    @BindView(R.id.ll_empty_message)
    LinearLayout llEmptyMessage;
    @BindView(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private Integer page = 1;
    private List<MessageInfo> mInfoList;
    private MessageAdapter mAdapter;
    private User userInfo;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("消息");
        initData();
        return view;
    }

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvMessageList.setLayoutManager(manager);
        mInfoList = new ArrayList<>();
        mAdapter = new MessageAdapter(getActivity(), mInfoList);
        mRvMessageList.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = UserService.getUserInfo(getActivity());
        getNetData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            if (userInfo == null) {
//                mInfoList.clear();
//                mAdapter.notifyDataSetChanged();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                return;
//            }
            getNetData();
        }
    }

    /**
     * 获取消息
     */
    private void getNetData() {

        CustomApplication.getRetrofit().postMessageList(page, 10).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    CustomToast.INSTANCE.showToast(CustomApplication.getContext(), "网络数据异常");
                    return;
                }

                String body = response.body().toString();
                if (!body.contains("{\"relogin\":1}")) {
                    if (!TextUtils.isEmpty(body)) {
                        dealMessageList(body);
                        llNotLogin.setVisibility(View.GONE);
                    }
                } else {
                    mInfoList.clear();
                    mAdapter.notifyDataSetChanged();
                    llNotLogin.setVisibility(View.VISIBLE);
                    llEmptyMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });
    }

    private void dealMessageList(String body) {
        if (!TextUtils.isEmpty(body)) {
            mInfoList.clear();
            MessageInfos messageInfos = GsonUtil.parseJsonWithGson(body, MessageInfos.class);
            mInfoList.addAll(messageInfos.getMessge());
            mAdapter.notifyDataSetChanged();
            llEmptyMessage.setVisibility(mInfoList.size() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
