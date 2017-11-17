package com.gxuc.runfast.shop.activity.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.bean.user.User;
import com.gxuc.runfast.shop.data.DataLayer;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.impl.NetInterface;
import com.gxuc.runfast.shop.util.CustomToast;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.util.GetJsonDataUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 添加银行卡
 */
public class AddBankActivity extends ToolBarActivity {

    @BindView(R.id.et_bank_code)
    EditText etBankCode;
    @BindView(R.id.et_bank_user_name)
    EditText etBankUserName;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.btn_save_password)
    Button btnSavePassword;

    private boolean bankCodeIsEmpty;
    private boolean bankNameIsEmpty;
    private JSONObject bankJson;
    private NetInterface netInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        etBankCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    bankCodeIsEmpty = true;
                } else {
                    bankCodeIsEmpty = false;
                }
                String bankName = tvBankName.getText().toString().trim();

                if (bankCodeIsEmpty && bankNameIsEmpty && !TextUtils.equals(bankName, "开户行")) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });

        etBankUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    bankNameIsEmpty = true;
                } else {
                    bankNameIsEmpty = false;
                }
                String bankName = tvBankName.getText().toString().trim();
                if (bankCodeIsEmpty && bankNameIsEmpty && !TextUtils.equals(bankName, "开户行")) {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay);
                } else {
                    btnSavePassword.setBackgroundResource(R.drawable.shape_button_pay_gary);
                }
            }
        });
    }

    private void initData() {

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("https://ccdcapi.alipay.com/")
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加转换工厂，用于解析json并转化为javaBean
                .addConverterFactory(GsonConverterFactory.create())
                .client(DataLayer.getClient())
                .build();

        netInterface = mRetrofit.create(NetInterface.class);

        String json = new GetJsonDataUtil().getJson(this, "bank_name.json");
        try {
            bankJson = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        etBankCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {// 此处为得到焦点时的处理内容
//                    String code = etBankCode.getText().toString().trim();
//                    if (!TextUtils.isEmpty(code)) {
//                        getBankName(code);
//                    }
//                }
//            }
//        });
    }

    @OnClick({R.id.btn_save_password, R.id.tv_bank_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save_password:
                addBank();
                break;

            case R.id.tv_bank_name:
                String code = etBankCode.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    getBankName(code);
                }
                break;
        }
    }

    /**
     * 获取银行
     *
     * @param
     */
    private void getBankName(String code) {

        netInterface.getBankName(code, true).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealBankName(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
//        CustomApplication.getRetrofit().getBankName(code, true).enqueue(new MyCallback<String>() {
//            @Override
//            public void onSuccessResponse(Call<String> call, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailureResponse(Call<String> call, Throwable t) {
//
//            }
//        });
    }

    private void dealBankName(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optBoolean("validated")) {

                String bank = jsonObject.optString("bank");
                tvBankName.setText(bankJson.optString(bank));
                tvBankName.setTextColor(getResources().getColor(R.color.color_address_black));

            } else {
                ToastUtil.showToast("请输入正确的银行卡号");
                tvBankName.setText("");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加银行卡
     *
     * @param
     */
    private void addBank() {
        User userInfo = UserService.getUserInfo(this);
        String code = etBankCode.getText().toString().trim();
        String bankName = tvBankName.getText().toString().trim();
        String userName = etBankUserName.getText().toString().trim();
        if (userInfo == null) {
            return;
        }
        CustomApplication.getRetrofit().addBank(userName, bankName, code).enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                dealAddBank(response.body());
            }

            @Override
            public void onFailureResponse(Call<String> call, Throwable t) {

            }
        });
    }

    private void dealAddBank(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            boolean success = jsonObject.optBoolean("success");
            String msg = jsonObject.optString("msg");
            if (!success) {
                CustomToast.INSTANCE.showToast(this, msg);
                return;
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出选择方式
     */
    private void showBankMode() {
        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(16)
                .setData(new String[]{"中国银行", "中国工商银行", "中国建设银行"})
                .title("支付方式")
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#333333")
                .backgroundPop(0xa0333333)
                .confirTextColor("#fc9153")
                .cancelTextColor("#999999")
                .textColor(Color.parseColor("#333333"))
                .itemPadding(10)
                .onlyShowProvinceAndCity(true)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];

            }

            @Override
            public void onCancel() {
            }
        });
    }

}
