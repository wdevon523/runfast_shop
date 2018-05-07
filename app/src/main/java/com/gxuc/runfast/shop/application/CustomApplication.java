package com.gxuc.runfast.shop.application;

import android.content.Context;
import android.text.TextUtils;

import com.gxuc.runfast.shop.data.ApiServiceFactory;
import com.gxuc.runfast.shop.data.DataLayer;
import com.gxuc.runfast.shop.impl.NetInterface;
import com.gxuc.runfast.shop.impl.constant.UrlConstant;
import com.gxuc.runfast.shop.BuildConfig;
import com.example.supportv1.app.BaseApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.gxuc.runfast.shop.util.SystemUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
//import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.shopex.pay.Contants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * @author Sun.bl
 * @version [1.0, 2016/06/19]
 */

public class CustomApplication extends BaseApplication {

    //public static Place currentPosition; //当前位置

    //public final static String PAY_APP_ID = "wx1de37406286c1bb4";

    private static Context context;

    private static NetInterface mInterface;

    //public static IWXAPI sIWXAPIPay;

    //public static LocationClient mLocationClient;

    public static boolean isRelogining = false;
    public static boolean isNeedUpdate = false;
    public static String alias;
//    private static IWXAPI wxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        //MobclickAgent.setDebugMode(true);
        //registerPayToWx();
        //SDKInitializer.initialize(getApplicationContext());
        //ShareSDK.initSDK(this);
        //初始化 xutils的网络请求部分
        x.Ext.init(this);
//        Config.DEBUG = true;
        UMShareAPI.get(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        JAnalyticsInterface.init(this);

        PlatformConfig.setWeixin(Contants.WEI_XIN_ID, "dkehfeuu38575uydhj3Y75u3yei2o45h");
        PlatformConfig.setQQZone("1106021946", "2SWJL1R0L380FtAS");

//        regToWx();

        context = getApplicationContext();
//        initNet();
        //初始化网络
        DataLayer.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this, frescoConfig);

        alias = SystemUtil.getIMEI(this);
        if (TextUtils.isEmpty(alias)) {
            alias = JPushInterface.getRegistrationID(this);
        }
    }

//    private void regToWx() {
//        wxapi = WXAPIFactory.createWXAPI(this, Contants.WEI_XIN_ID, true);
//        wxapi.registerApp(Contants.WEI_XIN_ID);
//    }

    private void initNet() {
        Retrofit mRetrofit = new Retrofit.Builder()
                //添加网络请求的基地址
                .baseUrl(UrlConstant.BaseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加转换工厂，用于解析json并转化为javaBean
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mInterface = mRetrofit.create(NetInterface.class);
    }


    public static NetInterface getRetrofit() {
        return ApiServiceFactory.getApi();
    }

    public static NetInterface getRetrofitPaoTui() {
        return ApiServiceFactory.getApiPaoTui();
    }

    public static Context getContext() {
        return context;
    }

//    public static IWXAPI getIWXAPI() {
//        return wxapi;
//    }

    /**
     * 程序退出的时候 调用这个方法
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    /**
     * 当程序运行内存不足的时候就会回调这个方法
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void registerPayToWx() {
        //通过工厂类获取对象
        //sIWXAPIPay = WXAPIFactory.createWXAPI(this, PAY_APP_ID);

        //将应用的APP_ID注册到微信
        //sIWXAPIPay.registerApp(PAY_APP_ID);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
