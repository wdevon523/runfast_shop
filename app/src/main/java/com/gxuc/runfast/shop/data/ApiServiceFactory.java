package com.gxuc.runfast.shop.data;


import com.gxuc.runfast.shop.impl.NetInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 工厂, 提供 ApiService 对象
 * Created by Berial on 16/8/18.
 */
public final class ApiServiceFactory {
    //        public static final String HOST = "http://user.gxptkc.com:9999/iwapb/";
    //    public static final String HOST = "http://120.77.70.27/iwapb/";


//    public static final String HOST = "https://www.gxptkc.com/iwapb/";
//    public static final String WEB_HOST = "https://www.gxptkc.com/web/";


    public static final String HOST = "http://192.168.2.221:8080/iwapb/";
    public static final String WEB_HOST = "http://192.168.2.221:8080/web/";

    //跑腿功能地址
    public static final String PAORTUI_HOST = "http://192.168.2.221:8088/api/";
//    public static final String PAORTUI_HOST = "http://192.168.2.165:8087/api/";
    //后面界面改动的新地址
//    public static final String NEW_HOST = "http://192.168.2.221:8088/api/";

    public static final String BASE_URL = HOST + "business/";
    public static final String BASE_IMG_URL = "http://image.gxptkc.com";


    private final NetInterface mApiService;

    private ApiServiceFactory() {
        Retrofit mRetrofit = new Retrofit.Builder()
                //添加网络请求的基地址
                .baseUrl(HOST)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加转换工厂，用于解析json并转化为javaBean
                .addConverterFactory(GsonConverterFactory.create())
                .client(DataLayer.getClient())
                .build();
        mApiService = mRetrofit.create(NetInterface.class);
    }

    private ApiServiceFactory(String PAORTUI_HOST) {
        Retrofit mRetrofit = new Retrofit.Builder()
                //添加网络请求的基地址
                .baseUrl(PAORTUI_HOST)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加转换工厂，用于解析json并转化为javaBean
                .addConverterFactory(GsonConverterFactory.create())
                .client(DataLayer.getClient())
                .build();
        mApiService = mRetrofit.create(NetInterface.class);
    }

    private static class ApiServiceFactoryHolder {
        private static final ApiServiceFactory INSTANCE = new ApiServiceFactory();
    }

    /**
     * 获取 ApiService 对象
     *
     * @return Api 接口对象
     */
    public static NetInterface getApi() {
        return ApiServiceFactoryHolder.INSTANCE.mApiService;
    }

    public static NetInterface getApiNew() {
        ApiServiceFactory INSTANCE = new ApiServiceFactory(PAORTUI_HOST);
        return INSTANCE.mApiService;
    }
}
