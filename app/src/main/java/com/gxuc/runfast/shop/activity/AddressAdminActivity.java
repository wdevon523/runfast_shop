package com.gxuc.runfast.shop.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.supportv1.utils.JsonUtil;
import com.example.supportv1.utils.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.gxuc.runfast.shop.R;
import com.gxuc.runfast.shop.activity.usercenter.UpdateAddressActivity;
import com.gxuc.runfast.shop.adapter.MyAddressAdapter;
import com.gxuc.runfast.shop.adapter.SearchAddressAdapter;
import com.gxuc.runfast.shop.application.CustomApplication;
import com.gxuc.runfast.shop.bean.Address;
import com.gxuc.runfast.shop.bean.address.AddressBean;
import com.gxuc.runfast.shop.bean.user.UserInfo;
import com.gxuc.runfast.shop.config.UserService;
import com.gxuc.runfast.shop.data.IntentFlag;
import com.gxuc.runfast.shop.impl.MyCallback;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.gxuc.runfast.shop.util.ToastUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

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

public class AddressAdminActivity extends ToolBarActivity implements PoiSearch.OnPoiSearchListener, SearchAddressAdapter.OnItemClickListener {

    @BindView(R.id.ll_choose_address)
    LinearLayout llChooseAddress;
    @BindView(R.id.tv_refresh_location)
    TextView tvRefreshLocation;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_search_address)
    EditText etSearchAddress;
    @BindView(R.id.ll_contain_my_address)
    LinearLayout llContainMyAddress;
    @BindView(R.id.tv_show_hide_more_address)
    TextView tvShowHideMoreAddress;
    @BindView(R.id.ll_nearby_address)
    LinearLayout llNearbyAddress;
    @BindView(R.id.ll_show_hide_more_address)
    LinearLayout llShowHideMoreAddress;
    @BindView(R.id.ll_my_address)
    LinearLayout llMyAddress;
    @BindView(R.id.ll_contain_nearby_address)
    LinearLayout llContainNearbyAddress;
    //    @BindView(R.id.rl_recycle)
//    RelativeLayout rlRecycle;
    @BindView(R.id.view_gray)
    View viewGray;
    @BindView(R.id.search_recycleview)
    RecyclerView searchRecycleview;
    private List<HotCity> hotCities;
    private Address address;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private boolean isShowMore = false;
    private PoiSearch searchPoiSearch;
    private PoiSearch.Query searchQuery;
    private List<PoiItem> poisList;
    private SearchAddressAdapter searchAddressAdapter;
    private List<PoiItem> pois;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);
        ButterKnife.bind(this);
        setRightMsg("新增地址");

        initMap();
        initData();
        setListener();
    }

    private void initMap() {
        address = (Address) getIntent().getParcelableExtra("Address");
        LogUtil.d("wdevon", "---address---" + address.toString());
        tvAddress.setText(address.address);
        tvLocation.setText(address.title);

        query = new PoiSearch.Query("", "120000", address.address);
        //keyWord表示搜索字符串
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字），120000  商务住宅相关
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setCityLimit(true);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        if (address.latLng != null) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(address.latLng.latitude,
                    address.latLng.longitude), 1000));//设置周边搜索的中心点以及半径
        }
//        else {
//            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(39.9088600000,
//                    116.3973900000), 1000));
//        }
        poiSearch.searchPOIAsyn();

    }

    private void initData() {

        hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));

        searchRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchAddressAdapter = new SearchAddressAdapter(this, poisList, address.latLng, this);
        searchRecycleview.setAdapter(searchAddressAdapter);

        requestAddressList();
    }


    private void setListener() {

        etSearchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                viewGray.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchRecycleview.setVisibility(View.GONE);
                    llNearbyAddress.setVisibility(View.VISIBLE);
                } else {
                    viewGray.setVisibility(View.GONE);
                    searchRecycleview.setVisibility(View.VISIBLE);
                    llNearbyAddress.setVisibility(View.GONE);
                }
                searchQuery = new PoiSearch.Query(s.toString(), "", tvAddress.getText().toString());
                //keyWord表示搜索字符串
                //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字），120000  商务住宅相关
                //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索

//                searchQuery.setCityLimit(true);
//                searchQuery.setLocation(new LatLonPoint(address.latLng.latitude, address.latLng.longitude));
//                searchQuery.setDistanceSort(true);
                searchQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
                searchQuery.setPageNum(1);//设置查询页码
                searchPoiSearch = new PoiSearch(AddressAdminActivity.this, searchQuery);
                searchPoiSearch.setOnPoiSearchListener(SearchPoiSearchImp);
//                searchPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(address.latLng.latitude,
//                        address.latLng.longitude), 50000000));//设置周边搜索的中心点以及半径
                searchPoiSearch.searchPOIAsyn();

            }
        });

        etSearchAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                viewGray.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });

    }

    private PoiSearch.OnPoiSearchListener SearchPoiSearchImp = new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
            //获取周围兴趣点
            poisList = poiResult.getPois();
            if (poisList == null || poisList.size() == 0) {
                poisList = new ArrayList<>();
            }

            searchAddressAdapter.setPoiList(poisList);
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    };


    private void requestAddressList() {
        UserInfo userInfo = UserService.getUserInfo(this);
        if (userInfo == null) {
            llMyAddress.setVisibility(View.GONE);
            return;
        }
        CustomApplication.getRetrofitNew().getAddressList().enqueue(new MyCallback<String>() {
            @Override
            public void onSuccessResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data != null && data.length() > 0) {
                            dealAddressList(data);
                            llMyAddress.setVisibility(View.VISIBLE);
                        } else {
                            llMyAddress.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtil.showToast(jsonObject.optString("errorMsg"));
                        llMyAddress.setVisibility(View.GONE);
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

    private void dealAddressList(JSONArray data) {
        ArrayList<AddressBean> addressBeanList = JsonUtil.fromJson(data.toString(), new TypeToken<ArrayList<AddressBean>>() {
        }.getType());
        llShowHideMoreAddress.setVisibility(addressBeanList.size() > 3 ? View.VISIBLE : View.GONE);

        fillMyAddressView(addressBeanList);
    }

    private void fillMyAddressView(final ArrayList<AddressBean> addressInfoList) {
        MyAddressAdapter myAddressAdapter = new MyAddressAdapter(this, addressInfoList);
        llContainMyAddress.removeAllViews();
        for (int i = 0; i < addressInfoList.size(); i++) {
            llContainMyAddress.addView(myAddressAdapter.getView(i, null, null));
            if (i > 2) {
                llContainMyAddress.getChildAt(i).setVisibility(View.GONE);
            }
        }

        myAddressAdapter.setOnMyAddressClickLstener(new MyAddressAdapter.OnMyAddressClickLstener() {
            @Override
            public void onMyAddressClick(int position) {
                AddressBean addressInfo = addressInfoList.get(position);
                address.title = addressInfo.userAddress;
                address.address = addressInfo.cityName;
                address.latLng = new LatLng(Double.valueOf(addressInfo.latitude), Double.valueOf(addressInfo.longitude));
                finishAct();
            }
        });
    }

    @OnClick({R.id.tv_right_title, R.id.ll_choose_address, R.id.tv_refresh_location, R.id.ll_show_hide_more_address, R.id.view_gray})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title:

                userInfo = UserService.getUserInfo(this);
                if (userInfo == null || userInfo.id == null) {
                    startActivity(new Intent(this, LoginQucikActivity.class));
                    return;
                }
                Intent intent = new Intent(this, UpdateAddressActivity.class);
                intent.putExtra(IntentFlag.KEY, IntentFlag.ADD_ADDRESS);
                startActivity(intent);
                break;
            case R.id.ll_choose_address:
                chooseLocation();
                break;
            case R.id.tv_refresh_location:
                finishAct();
                break;
            case R.id.ll_show_hide_more_address:
                isShowMore = !isShowMore;
                tvShowHideMoreAddress.setText(isShowMore ? "隐藏部分地址" : "展开全部地址");
                tvShowHideMoreAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, isShowMore ? getResources().getDrawable(R.drawable.icon_up) : getResources().getDrawable(R.drawable.icon_down), null);
                for (int i = 0; i < llContainMyAddress.getChildCount(); i++) {
                    if (i > 2) {
                        llContainMyAddress.getChildAt(i).setVisibility(isShowMore ? View.VISIBLE : View.GONE);
                    }
                }
                break;
            case R.id.view_gray:
                if (viewGray.getVisibility() == View.VISIBLE) {
                    viewGray.setVisibility(View.GONE);
                    etSearchAddress.clearFocus();
                    SystemUtil.hideSoftKeyboard(etSearchAddress);
                }
                break;
        }
    }

    private void chooseLocation() {

        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                .enableAnimation(true)    //启用动画效果
                .setAnimationStyle(R.style.CustomAnim)    //自定义动画
//                .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101")) //APP自身已定位的城市，默认为null（定位失败）
                .setLocatedCity(new LocatedCity(address.address.substring(0, address.address.length() - 1), "浙江", "101210101")) //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
//                        Toast.makeText(getApplicationContext(), data.getName(), Toast.LENGTH_SHORT).show();
                        if (data != null) {
                            tvAddress.setText(data.getName());
                            etSearchAddress.setText("");
                            etSearchAddress.clearFocus();
                            if (poisList == null) {
                                poisList = new ArrayList<>();
                            } else {
                                poisList.clear();
                            }
                            searchAddressAdapter.setPoiList(poisList);
                        }
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                CityPicker.getInstance()
                                        .locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 2000);
                    }
                })
                .show();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

        //获取周围兴趣点
        pois = poiResult.getPois();
        if (pois == null || pois.size() == 0) {
            return;
        }
        fillNearByAddress(pois);

    }

    private void fillNearByAddress(final List<PoiItem> pois) {
        llContainNearbyAddress.removeAllViews();

        for (int i = 0; i < pois.size(); i++) {
            View nearByView = LayoutInflater.from(this).inflate(R.layout.item_address_name, null);
            TextView tvAddressName = (TextView) nearByView.findViewById(R.id.tv_address_title);
            tvAddressName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvAddressName.setText(pois.get(i).getTitle());
            nearByView.setTag(pois.get(i));
            llContainNearbyAddress.addView(nearByView);
            if (i != pois.size() - 1) {
                View view = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);//宽度、高度
                params.setMargins(40, 0, 0, 0);
                view.setLayoutParams(params);
                view.setBackgroundResource(R.color.view_e5e5e5);
                llContainNearbyAddress.addView(view);
            }
            nearByView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PoiItem poiItem = (PoiItem) v.getTag();
                    address.title = poiItem.getTitle();
                    address.address = poiItem.getCityName();
                    address.latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    finishAct();
                }
            });
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onItemClick(View view, int position) {
        PoiItem poiItem = poisList.get(position);
        address.title = poiItem.getTitle();
        address.address = poiItem.getCityName();
        address.latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
        finishAct();
    }

    private void finishAct() {
        Intent intent = new Intent();
        intent.putExtra("Address", address);
        setResult(RESULT_OK, intent);
        finish();
    }

}
