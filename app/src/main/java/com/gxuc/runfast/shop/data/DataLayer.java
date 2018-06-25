package com.gxuc.runfast.shop.data;

import android.app.Application;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.example.supportv1.utils.LogUtil;
import com.gxuc.runfast.shop.BuildConfig;
import com.example.supportv1.assist.netWork.CookieManger;
import com.google.gson.Gson;
import com.gxuc.runfast.shop.impl.constant.CustomConstant;
import com.gxuc.runfast.shop.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 数据层
 * Created by Berial on 2017/8/24.
 */
public final class DataLayer {

    private static Application aplication; // 缓存文件
    private static final String RESPONSE_CACHE_FILE = "responseCache"; // 缓存文件

    private static final long RESPONSE_CACHE_SIZE = 10 * 1024 * 1024L; // 缓存大小
    private static final long HTTP_CONNECT_TIMEOUT = 10L; // 连接超时时间
    private static final long HTTP_READ_TIMEOUT = 20L; // 读取超时时间
    private static final long HTTP_WRITE_TIMEOUT = 10L; // 写入超时时间

    private final Gson gson = new Gson();

    private OkHttpClient mOkHttpClient;


    private DataLayer() {
    }

    public static DataLayer getInstance() {
        return DataLayerHolder.INSTANCE;
    }

    private static class DataLayerHolder {
        private static final DataLayer INSTANCE = new DataLayer();
    }

    public static OkHttpClient getClient() {
        return getInstance().mOkHttpClient;
    }

    public static Gson getGson() {
        return getInstance().gson;
    }

    public static void init(Application app) {
        aplication = app;
        Paper.init(app);

        File cacheFile = new File(app.getCacheDir(), RESPONSE_CACHE_FILE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    message = message.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
//                    Log.d("devon", "OkHttp====Message111:" + message);
                    message = URLDecoder.decode(message, "UTF-8");
                    Log.d("devon", "OkHttp====Message:" + message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        });

        logging.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE
        );

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(cacheFile, RESPONSE_CACHE_SIZE))
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(new CookieManger(app.getApplicationContext()))
                .addInterceptor(new TokenInterceptord())
                .addInterceptor(logging);

        getInstance().mOkHttpClient = builder.build();
    }

    public static class TokenInterceptord implements Interceptor {

        private final String TAG = "respond";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            String url = oldRequest.url().toString();
            Response response = null;

            // 新的请求,添加参数
            Request newRequest = addParam(oldRequest);
            response = chain.proceed(newRequest);

//            LogUtil.d("xxxxx", "url-----" + url + "    head------" + newRequest.headers().toString());
            ResponseBody value = response.body();
            byte[] resp = value.bytes();
            String json = new String(resp, "UTF-8");

            // 判断stateCode值
            try {
                if (json.contains("token") && !url.contains("login") && !url.contains("register")) {
                    JSONObject jsonObject = new JSONObject(json);
//                    if (jsonObject.optBoolean("success")) {
                    String token = jsonObject.optString("token");
                    Log.d(TAG, "token失效，新的token：" + token);
//                    SharePreferenceUtil.getInstance().putStringValue("token", token);
                    // token失效，重新执行请求
                    Request newTokenRequest = addParam(oldRequest);
                    response = chain.proceed(newTokenRequest);
//                    }
                } else {
                    // 这里值得注意。由于前面value.bytes()把响应流读完并关闭了，所以这里需要重新生成一个response，否则数据就无法正常解析了
                    response = response.newBuilder()
                            .body(ResponseBody.create(null, resp))
                            .build();
                }
            } catch (Exception e) {

            }


//            Headers requestHeaders = response.networkResponse().request().headers();
//            int requestHeadersLength = requestHeaders.size();
//
//            for (int i = 0; i < requestHeadersLength; i++) {
//
//                String headerName = requestHeaders.name(i);
//
//                String headerValue = requestHeaders.get(headerName);
//
//                Log.d(TAG, "TAG----------->Name:" + headerName + "------------>Value:" + headerValue + "\n");
//
//            }

            return response;
        }

        /**
         * 添加公共参数
         *
         * @param oldRequest
         * @return
         */
        private Request addParam(Request oldRequest) {
            Request newRequest;
            Headers.Builder builder = oldRequest.headers()
                    .newBuilder();
//                    .add("token", SharePreferenceUtil.getInstance().getStringValue("token"));
            if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance().getStringValue("token"))) {
                builder.add("token", SharePreferenceUtil.getInstance().getStringValue("token"));
            }

            if (oldRequest.url().toString().contains("wm/cart/add") || oldRequest.url().toString().contains("wm/cart/delete")) {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = FormBody.create(mediaType, SharePreferenceUtil.getInstance().getStringValue("shopCart"));
//                FormBody.Builder fromBodyBuilder = new FormBody.Builder();
//                FormBody formBody = (FormBody) oldRequest.body();
//                for (int i = 0; i < formBody.size(); i++) {
//                    fromBodyBuilder.addEncoded(formBody.encodedName(i),formBody.encodedValue(i))
//
//                }
                int businessId = SharePreferenceUtil.getInstance().getIntValue("businessId");
                newRequest = oldRequest.newBuilder()
                        .url(oldRequest.url().toString() + "?businessId=" + businessId)
                        .method(oldRequest.method(), oldRequest.body())
                        .headers(builder.build())
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

            } else if (oldRequest.url().toString().contains("wm/cart/chooseCartItem")) {
                MediaType mediaType = MediaType.parse("application/json");
                int businessId = SharePreferenceUtil.getInstance().getIntValue("businessId");
                String lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
                String lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);

                RequestBody requestBody = FormBody.create(mediaType, SharePreferenceUtil.getInstance().getStringValue("BusinessShopCart"));

                newRequest = oldRequest.newBuilder()
                        .url(oldRequest.url().toString() + "?businessId=" + businessId + "&userLng=" + lon + "&userLat=" + lat)
                        .method(oldRequest.method(), oldRequest.body())
                        .headers(builder.build())
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();

            } else if (oldRequest.url().toString().contains("wm/cart/fillInDiy")) {
                MediaType mediaType = MediaType.parse("application/json");
                int businessId = SharePreferenceUtil.getInstance().getIntValue("businessId");
                String lat = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLAT);
                String lon = SharePreferenceUtil.getInstance().getStringValue(CustomConstant.POINTLON);
//
                RequestBody requestBody = FormBody.create(mediaType, SharePreferenceUtil.getInstance().getStringValue("BusinessShopCart"));
//                if (SharePreferenceUtil.getInstance().getBooleanValue("isFromCart", false)) {
//                    newRequest = oldRequest.newBuilder()
//                            .url(oldRequest.url().toString() + "?businessId=" + businessId + "&userLng=" + lon + "&userLat=" + lat)
//                            .method(oldRequest.method(), oldRequest.body())
//                            .headers(builder.build())
//                            .addHeader("Content-Type", "application/json")
//                            .post(requestBody)
//                            .build();
//                } else {
                String json = SharePreferenceUtil.getInstance().getStringValue("paramJson");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                newRequest = oldRequest.newBuilder()
                        .url(oldRequest.url().toString() + "?businessId=" + businessId + "&userLng=" + lon + "&userLat=" + lat
                                + "&toAddressId=" + jsonObject.optString("toAddressId") + "&userRedId=" + jsonObject.optString("userRedId")
                                + "&suportSelf=" + jsonObject.optString("suportSelf") + "&eatInBusiness=" + jsonObject.optString("eatInBusiness")
                                + "&selfTime=" + jsonObject.optString("selfTime") + "&selfMobile=" + jsonObject.optString("selfMobile") + "&userCouponId=" + jsonObject.optString("userCouponId"))
                        .method(oldRequest.method(), oldRequest.body())
                        .headers(builder.build())
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
//                }
            } else {
                newRequest = oldRequest.newBuilder()
                        .method(oldRequest.method(), oldRequest.body())
                        .headers(builder.build())
                        .build();

            }
            return newRequest;
        }
    }

    private static class NetworkIntercept implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            String json = response.body().string();
//            json = json.replace("{\"relogin\":1}", "");
//            Logger.json(json);
            return response.newBuilder()
                    .body(ResponseBody.create(MediaType.parse("application/json"), json))
                    .build();
        }
    }

    private static class CookieStore implements CookieJar {

        private SimpleArrayMap<String, List<Cookie>> map = new SimpleArrayMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            map.put(url.toString(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = map.get(url.toString());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }
}
