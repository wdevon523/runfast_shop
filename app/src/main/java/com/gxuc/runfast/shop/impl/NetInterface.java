package com.gxuc.runfast.shop.impl;


import com.gxuc.runfast.shop.impl.constant.UrlConstant;

import java.util.IdentityHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/6/17.
 */
public interface NetInterface {
    //@GET 代表发送的GET请求
    //article/list/text?page=1 请求的路径(并且加上了基地址)
    //Call<T> 代表返回值的类型，ResponseBody 是Retrofit的原生的返回值
    @GET("article/list/text?page=1")
    Call<ResponseBody> getNetData();

    /**
     * 可以通过Retrofit 直接去解析获取到的json 得到的数据可以直接开始使用
     * TestModle 是gson生成的实体类
     *
     * @return
     */
    @GET("article/list/text?page=1")
    Call<ResponseBody> getNetModle();

    /**
     * 如果是get请求的参数 必须是加了@Field("page") 这个注解 括号里面试服务器接收的参数名字
     * 必须和服务器保持统一
     *
     * @param page
     * @return
     */
    @GET("article/list/text?")
    Call<ResponseBody> getNetModleWithParams(@Field("page") String page);

    /**
     * 动态替换地址
     * 需要替换的地址必须要用"{}"包裹起来，然后通过@path 来声明需要替换的参数
     *
     * @param contents
     * @return
     */
    @GET("article/list/{content}?page=1")
    Call<ResponseBody> getNetModleWithPath(@Path("content") String contents);

    @GET("article/{name}/text?")
    Call<ResponseBody> getNetModlePathWithParams(@Path("name") String list,
                                                 @Field("page") String path);

    /**
     * 通过map传递多个参数
     *
     * @param map
     * @return
     */
    @GET("article/list/text?")
    Call<ResponseBody> getNetModleWithMap(@FieldMap Map<String, String> map);

//-----------------------------------------------------------------------------------------------

    /**
     * 获取商家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_LIST)
    Call<String> getBusiness(@Field("longitude") String longitude,
                             @Field("latitude") String latitude,
                             @Field("page") Integer page);


    /**
     * 获取附近商家
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS)
    Call<String> getNearbyBusinesses(@Field("longitude") String longitude,
                                     @Field("latitude") String latitude,
                                     @Field("page") Integer page,
                                     @Field("version") Integer version);


    /**
     * 获取商品活动过
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_ACTIVITY)
    Call<String> getBusinessActivity(@Field("id") Integer id);

    /**
     * 获取商品规格
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_GUIGE)
    Call<String> getGoodsSpec(@Field("id") Integer id);

    /**
     * 获取商品详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GOODS_DETAIL)
    Call<String> getGoodsDetail(@Field("id") Integer id);

    /**
     * 商家收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_IS_SHOUCANG)
    Call<String> getIsShoucang(@Field("shopId") Integer shopId,
                               @Field("type") Integer type,
                               @Field("uId") Integer uId);

    /**
     * 获取商家信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BUSINESS_INFO)
    Call<String> getBusinessInfo(@Field("id") Integer id,
                                 @Field("longitude") String longitude,
                                 @Field("latitude") String latitude,
                                 @Field("version") int version);


    /**
     * 上传地址
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_ADDRESS)
    Call<String> postAddress(@Field("longitude") Double longitude,
                             @Field("latitude") Double latitude,
                             @Field("version") int version);

    /**
     * 获取首页轮播图
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_ADVERT)
    Call<String> getAdvert(@Field("agentId") int agentId);

    /**
     * 获取首页
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.HOME_PAGE)
    Call<String> getHomePage(@Field("noshow") Integer noshow,
                             @Field("agentId") Integer agentId);

    /**
     * 登录请求
     * 发送post请求的时候，必须要加上@FormUrlEncoded，不然就会报错
     *
     * @param mobile
     * @param password
     * @return
     */

    @FormUrlEncoded
    @POST(UrlConstant.LOGIN)
    Call<String> postLogin(@Field("mobile") String mobile,
                           @Field("password") String password,
                           @Field("alias") String alias,
                           @Field("bptype") Integer bptype);

    /**
     * 订单投诉
     * <p>
     * aram businessId
     *
     * @param userEmail
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ORDER_COMPLAINT)
    Call<String> postOrderComplaint(@Field("businessId") Integer businessId,
                                    @Field("goodsSellRecordId") Integer goodsSellRecordId,
                                    @Field("userEmail") String userEmail,
                                    @Field("content") String content);

    /**
     * 收藏
     *
     * @param shopId
     * @param type   0商品  1商家
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SAVE_SHOP)
    Call<String> postSaveShop(@Field("shopId") Integer shopId,
                              @Field("type") Integer type);

    /**
     * 地址列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.USER_ADDRESS_LIST)
    Call<String> postListAddress(@Field("id") int id,
                                 @Field("version") int version);

    /**
     * 修改头像
     *
     * @param imgpath
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.QUERY_EDIT_HEAD)
    Call<String> updateHead(@Field("imgpath") String imgpath);

    /**
     * 红包
     *
     * @param businessId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.RED_PACKAGE)
    Call<String> postRedPackage(@Field("businessId") Integer businessId);

    /**
     * 我的优惠券
     *
     * @param type 优惠券类型(1.商城、0.外卖)
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.MY_CONPON)
    Call<String> GetMyCoupan(@Field("type") Integer type);


    /**
     * 我的积分
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SCORE_DATA)
    Call<String> getScore();

    /**
     * 提现银行卡列表
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WATHDRAWALL_LIST)
    Call<String> getWathdrawallList(@Field("id") int id);

    /**
     * 获取银行卡
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BANK_NAME)
    Call<String> getBankName(@Field("cardNo") String cardNo,
                             @Field("cardBinCheck") boolean isCardBinCheck);

    /**
     * 确认订单
     *
     * @param businessId
     * @param userAddressId
     * @param couponId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CREATE_ORDER)
    Call<String> createOrder(@Field("businessId") int businessId,
                             @Field("userAddressId") int userAddressId,
                             @Field("couponId") String couponId,
                             @Field("content") String content);
//                             @Field("rid") String rid,
//                             @Field("yhprice") String yhprice,
//                             @Field("businesspay") String businesspay,
//                             @Field("totalpay") String totalpay,
//                             @Field("orderCode") String orderCode,
//                             @Field("t") String t);

    /**
     * 添加购物车
     *
     * @param t
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ORDER_CAR)
    Call<String> OrderCar(@Field("t") String t);

    /**
     * 余额支付
     *
     * @param orderId
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WALLET_PAY)
    Call<String> walletPay(@Field("orderId") int orderId,
                           @Field("password") String password);

    /**
     * 跑腿余额支付
     *
     * @param orderId
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PAO_TUI_WALLET_PAY)
    Call<String> paoTuiwalletPay(@Field("orderId") int orderId,
                                 @Field("password") String password);

    /**
     * 微信支付
     *
     * @param id
     * @param type 0-跑腿快车外卖款|1-跑腿快车商城款|2-跑腿快车手机充值款
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WEIXIN_PAY)
    Call<String> weiXintPay(@Field("id") Integer id,
                            @Field("type") String type);

    /**
     * 微信签名
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WEIXIN_SIGN)
    Call<String> weiXintSign(@Field("prepayId") String prepayId,
                             @Field("nonceStr") String nonceStr,
                             @Field("timeStamp") String timeStamp,
                             @Field("packageValue") String packageValue);

    /**
     * 支付宝支付
     *
     * @param id
     * @param orderType 1-支付宝付款|2-支付宝充值
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ALIPAY_PAY)
    Call<String> aliPay(@Field("id") Integer id,
                        @Field("orderType") String orderType);

    /**
     * 确认收货
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ORDER_RECEIVE)
    Call<String> receiveOrder(@Field("orderId") Integer id);

    /**
     * 确认订单获取商品
     *
     * @param bid
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_SHOPPING_CART)
    Call<String> getShoppingCar(@Field("businessId") Integer bid);

    /**
     * 获取购物车
     *
     * @param bid
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_SHOPPINGS)
    Call<String> getShoppings(@Field("businessId") Integer bid);

    /**
     * 选择收货地址
     *
     * @param bid
     * @param lat
     * @param lng
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SELECT_ADDR)
    Call<String> selectAddr(@Field("bid") int bid,
                            @Field("lat") String lat,
                            @Field("lng") String lng,
                            @Field("isfull") int isfull,
                            @Field("full") String full);

    /**
     * 选择优惠券
     *
     * @param bid
     * @param couponid
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SELECT_COUPON)
    Call<String> selectCoupon(@Field("bid") int bid,
                              @Field("couponid") int couponid
    );

    /**
     * 微信充值
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WEIXIN_RECHARGE)
    Call<String> weiXinRecharge(@Field("monetary") String monetary);

    /**
     * 支付宝充值
     *
     * @param id
     * @param orderType 1-支付宝付款|2-支付宝充值
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ALIPAY_PAY)
    Call<String> aliPayRecharge(@Field("monetary") String id,
                                @Field("orderType") String orderType,
                                @Field("payType") String payType);

    /**
     * 获取用户信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_USER_INFO)
    Call<String> getUserInfo(@Field("uId") int uId);

    /**
     * 清空购物车
     *
     * @param businessId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CLEAN_SHOPPING_CART)
    Call<String> cleanShoppingCart(@Field("businessId") int businessId);

    /**
     * 检查版本
     *
     * @param version
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CHECK_NEW_VERSION)
    Call<String> checkNewVersion(@Field("version") int version);

    /**
     * 获取骑手经纬度
     *
     * @param driverId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_DRIVER_LATLNG)
    Call<String> getDriverLatLng(@Field("driverId") String driverId);

    /**
     * 获取骑手经纬度
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_ID)
    Call<String> getBusinessId(@Field("id") int id);

    /**
     * 获取骑手经纬度
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.WEIXIN_LOGIN)
    Call<String> weiXinLogin(@Field("appid") String appid,
                             @Field("secret") String secret,
                             @Field("code") String code,
                             @Field("grant_type") String grant_type);

    /**
     * 提交订单（代购）
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PURCHASE)
    Call<String> confirmPurchase(@Field("goodsDescription") String goodsDescription,
                                 @Field("fromType") String fromType,
                                 @Field("fromLng") double fromLng,
                                 @Field("fromLat") double fromLat,
                                 @Field("fromName") String fromName,
                                 @Field("fromAddress") String fromAddress,
                                 @Field("type") String type,
                                 @Field("tip") int tip,
                                 @Field("toId") int toId,
                                 @Field("userLng") String userLng,
                                 @Field("userLat") String userLat);


    /**
     * 提交订单（跑腿）
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PURCHASE)
    Call<String> submitOrder(@Field("goodsType") String goodsType,
                             @Field("goodsWeight") int goodsWeight,
                             @Field("pickTime") String pickTime,
                             @Field("fromId") int fromId,
                             @Field("toId") int toId,
                             @Field("type") String type,
                             @Field("tip") int tip,
                             @Field("userLng") String userLng,
                             @Field("userLat") String userLat,
                             @Field("remark") String remark);

    /**
     * 获取跑腿代购订单列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELIVERY_ORDER)
    Call<String> getDeliveryOrder(@Field("page") int page,
                                  @Field("size") int goodsType);

    /**
     * 获取跑腿代购订单详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELIVERY_ORDER_DETAIL)
    Call<String> getDeliveryOrderDetail(@Field("orderId") int orderId);

    /**
     * 取消跑腿代购订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CANCEL_DELIVERY_ORDER)
    Call<String> cancelDeliveryOrder(@Field("orderId") int orderId);

    /**
     * 删除跑腿代购订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELETE_DELIVERY_ORDER)
    Call<String> deleteDeliveryOrder(@Field("orderId") int orderId);

    /**
     * 获取跑腿代购订单状态列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELIVERY_ORDER_STATUS)
    Call<String> getDeliveryOrderStatus(@Field("orderId") int orderId);

    /**
     * 根据填写的订单获取配送信息（代购）
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELIVERY_ORDER_INFO)
    Call<String> getPurchaseOrderInfo(@Field("fromType") String fromType,
                                      @Field("fromLng") double fromLng,
                                      @Field("fromLat") double fromLat,
                                      @Field("fromAddress") String fromAddress,
                                      @Field("goodsDescription") String goodsDescription,
                                      @Field("toId") int toId,
                                      @Field("type") String type,
                                      @Field("userLng") String userLng,
                                      @Field("userLat") String userLat);

    /**
     * 根据填写的订单获取配送信息（取送件）
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELIVERY_ORDER_INFO)
    Call<String> getDeliveryOrderInfo(@Field("goodsType") String goodsType,
                                      @Field("goodsWeight") int goodsWeight,
                                      @Field("pickTime") String pickTime,
                                      @Field("fromId") int fromId,
                                      @Field("toId") int toId,
                                      @Field("type") String type,
                                      @Field("userLng") String userLng,
                                      @Field("userLat") String userLat);

    /**
     * 跑腿支付
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PAO_TUI_PAY)
    Call<String> paoTuiPay(@Field("orderNo") String orderNo,
                           @Field("channel") String channel);

    /**
     * 跑腿支付
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.QUERY_PAOTUI_STATUS)
    Call<String> queryPaoTuiStatus(@Field("orderNo") String orderNo);

    /**
     * 获取当前所属代理商
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_AGENT)
    Call<String> getAgent(@Field("userLng") double userLng,
                          @Field("userLat") double userLat);

    /**
     * 统一获取获取代理商促销信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_HOME_ACT)
    Call<String> getHomeAct(@Field("agentId") String agenId,
                            @Field("userLng") double userLng,
                            @Field("userLat") double userLat);

    /**
     * 获取附近商家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_NEAR_BY_BUSINESS)
    Call<String> getNearByBusiness(@Field("agentId") String agenId,
                                   @Field("userLng") double userLng,
                                   @Field("userLat") double userLat,
                                   @Field("sorting") int sorting,
                                   @FieldMap IdentityHashMap<String, Integer> actMap,
                                   @FieldMap IdentityHashMap<String, Integer> featuretMap,
                                   @FieldMap IdentityHashMap<String, String> catalogMap,
                                   @Field("page") int page,
                                   @Field("size") int size);

//    @Field("catalogId") String catalogId,

    /**
     * 获取商家详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_DETAIL)
    Call<String> getBusinessDetail(@Field("businessId") int businessId,
                                   @Field("userLng") double userLng,
                                   @Field("userLat") double userLat);

    /**
     * 获取商家所有商品
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_GOODS)
    Call<String> getBusinessGoods(@Field("businessId") int businessId);

    /**
     * 添加购物车
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ADD_SHOP_CART)
    Call<String> addShopCart(@Field("businessId") int businessId);

    /**
     * 删除购物车
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SUB_SHOP_CART)
    Call<String> subShopCart(@Field("businessId") int businessId);

    /**
     * 获取商家购物车列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BUSINESS_SHOP_CART)
    Call<String> getBusinessShopCart(@Field("businessId") int businessId);

    /**
     * 清空购物车
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CLEAR_SHOP_CART)
    Call<String> clearShopCart(@Field("businessId") int businessId);

    /**
     * 根据商家购物车获取订单结算信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SUBMIT_BUSINESS_SHOP_CART)
    Call<String> submitBusinessShopCart(@FieldMap Map<String, String> map);

    /**
     * 生成订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CREATE_ORDER_NEW)
    Call<String> creatOrder(@Field("businessId") int businessId,
                            @Field("remark") String remark);

    /**
     * 支付订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PAY_ORDER)
    Call<String> payOrder(@Field("orderNo") String orderNo,
                          @Field("channel") String channel);

    /**
     * 查询订单状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ORDER_QUERY)
    Call<String> orderQuery(@Field("orderNo") String orderNo);

    /**
     * 获取商家下的用户评论列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BUSINESS_EVALUATION)
    Call<String> getBusinessEvaluation(@Field("businessId") int businessId);

    /**
     * 获取用户订单列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_ORDER_LIST)
    Call<String> getOrderList(@Field("page") int page,
                              @Field("size") int size);

    /**
     * 再来一单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BUY_AGAIN_NEW)
    Call<String> buyAgainNew(@Field("orderId") int orderId);

    /**
     * 取消订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CANCEL_ORDER)
    Call<String> cancelorderNew(@Field("orderId") int orderId);

    /**
     * 获取当前用户的收货地址列表
     *
     * @return
     */
    @POST(UrlConstant.ADDRESS_LIST)
    Call<String> getAddressList();


    /**
     * 修改用户地址
     * gender 0:女；1：男
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_ADDRESS)
    Call<String> updateAddress(@Field("id") int id,
                               @Field("name") String name,
                               @Field("gender") int gender,
                               @Field("phone") String phone,
                               @Field("userAddress") String userAddress,
                               @Field("address") String address,
                               @Field("longitude") String longitude,
                               @Field("latitude") String latitude,
                               @Field("tag") int tag);

    /**
     * 删除当前用户所属的收货地址
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELETE_ADDRESS)
    Call<String> deleteAddress(@Field("id") int id);

    /**
     * 添加用户地址
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ADD_ADDRESS)
    Call<String> addAddress(@Field("name") String name,
                            @Field("gender") int gender,
                            @Field("phone") String phone,
                            @Field("userAddress") String userAddress,
                            @Field("address") String address,
                            @Field("longitude") String longitude,
                            @Field("latitude") String latitude,
                            @Field("tag") int tag);

    /**
     * 登陆
     * <p>
     * loginMode : mobile_pwd, mobile_sms_code,third
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.LOGIN_NEW)
    Call<String> login(@Field("mobile") String mobile,
                       @Field("password") String password,
                       @Field("alias") String alias,
                       @Field("loginMode") String loginMode);

    /**
     * 根据原密码修改密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_PWD_BY_OLD)
    Call<String> updatePwdByOld(@Field("oldPwd") String oldPwd,
                                @Field("newPwd") String newPwd);

    /**
     * 根据短信验证码修改密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_PWD_BY_SMSCODE)
    Call<String> updatePwdBySmsCode(@Field("mobile") String mobile,
                                    @Field("smsCode") String smsCode,
                                    @Field("newPwd") String newPwd);

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @POST(UrlConstant.GET_USER_INFORMATION)
    Call<String> getUserInformation();

    /**
     * 修改用户email
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_USER_INFO)
    Call<String> updateUserEmail(@Field("email") String email);

    /**
     * 修改用户昵称
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_USER_INFO)
    Call<String> updateUserNick(@Field("nickname") String nickname);

    /**
     * 修改用户头像
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.UPDATE_USER_INFO)
    Call<String> updateUserPic(@Field("pic") String imagePath);

    /**
     * 商户加盟
     *
     * @param businessName
     * @param mobile
     * @param contacts
     * @param address
     * @param remark
     * @param local
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BUSINESS_JOIN)
    Call<String> businessJoin(@Field("businessName") String businessName,
                              @Field("moblie") String mobile,
                              @Field("contacts") String contacts,
                              @Field("address") String address,
                              @Field("remark") String remark,
                              @Field("local") String local);

    /**
     * 用户投诉
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.COMPLAIN)
    Call<String> complain(@Field("userEmail") String email,
                          @Field("content") String content);

    /**
     * 获取代理商客服
     *
     * @param agentId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_CUSTOM_INFO)
    Call<String> getCustomInfo(@Field("agentId") String agentId);

    /**
     * 获取用户积分明细
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_SCORE_LIST)
    Call<String> getScoreList(@Field("page") int page,
                              @Field("size") int size);

    /**
     * 获取用户消息分页列表
     *
     * @return
     */
    @POST(UrlConstant.GET_MESSAGE_LIST)
    Call<String> getMessageList();

    /**
     * 获取用户银行卡账户列表
     *
     * @return
     */
    @POST(UrlConstant.GET_BANK_LIST)
    Call<String> getBankList();

    /**
     * 删除银行卡账户
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELETE_BANK)
    Call<String> deleteBank(@Field("id") int id);

    /**
     * 添加银行卡
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ADD_BANK)
    Call<String> addBank(@Field("name") String name,
                         @Field("banktype") String banktype,
                         @Field("account") String account);

    /**
     * 获取收藏商家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_FAVORITE_LIST)
    Call<String> getFavoriteList(@Field("userLng") String userLng,
                                 @Field("userLat") String userLat,
                                 @Field("page") int page,
                                 @Field("size") int size);

    /**
     * 收藏商家
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ADD_FAVORITE)
    Call<String> addFavorite(@Field("businessId") int businessId);

    /**
     * 取消收藏商家
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELETE_FAVORITE)
    Call<String> deleteFavorite(@Field("businessId") int businessId);

    /**
     * 获取当前用户的红包活动分页列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_MY_RED_PACK)
    Call<String> getMyRedPack(@Field("page") int page,
                              @Field("size") int size);

    /**
     * 获取当前用户可用的红包活动分页列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_MY_RED_PACK_VALID)
    Call<String> getMyRedPackValid(@Field("businessId") int businessId,
                                   @Field("page") int page,
                                   @Field("size") int size);

    /**
     * 提现记录
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CASH_RECORD)
    Call<String> getCashRecord(@Field("page") int page,
                               @Field("size") int size);

    /**
     * 提现
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CASH_SEND)
    Call<String> getCashSend(@Field("amountWithdraw") Double amountWithdraw,
                             @Field("bankAccountId") Integer bankAccountId,
                             @Field("remark") String remark);

    /**
     * 收支明细
     *
     * @return
     */
    @POST(UrlConstant.LIST_CONSUME)
    Call<String> getListConsume();

    /**
     * 获取当前用户所有的购物车
     *
     * @return
     */
    @POST(UrlConstant.ALL_SHOP_CART)
    Call<String> getAllShopCart();

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.ORDER_DETAIL)
    Call<String> getOrderDetail(@Field("orderId") Integer id);

    /**
     * 快速登录请求
     * loginMode : mobile_pwd, mobile_sms_code,third
     *
     * @param mobile
     * @return
     */

    @FormUrlEncoded
    @POST(UrlConstant.LOGIN_QUICK)
    Call<String> postLogiQuick(@Field("mobile") String mobile,
                               @Field("smsCode") String smsCode,
                               @Field("alias") String alias,
                               @Field("loginMode") String loginMode);

    /**
     * 发送短信
     *
     * @param mobile
     * @return
     */

    @FormUrlEncoded
    @POST(UrlConstant.SEND_SMS)
    Call<String> sendSms(@Field("mobile") String mobile,
                         @Field("smsType") String smsCode,
                         @Field("NECaptchaValidate") String nECaptchaValidate);

    /**
     * 第三方登录请求
     * loginMode : mobile_pwd, mobile_sms_code,third
     *
     * @param thirdLoginId
     * @param thirdLoginType
     * @param alias
     * @param loginMode
     * @return
     */

    @FormUrlEncoded
    @POST(UrlConstant.THIRD_LOGIN)
    Call<String> postThirdLogin(@Field("thirdLoginId") String thirdLoginId,
                                @Field("thirdLoginType") String thirdLoginType,
                                @Field("alias") String alias,
                                @Field("loginMode") String loginMode);

    /**
     * 通过短信验证码注册并绑定第三方账户
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.BIND_MOBILE)
    Call<String> bindMobile(@Field("mobile") String mobile,
                            @Field("password") String password,
                            @Field("smsCode") String smsCode,
                            @Field("thirdLoginId") String thirdLoginId,
                            @Field("thirdLoginType") String thirdLoginType,
                            @Field("alias") String alias);

    /**
     * 登出
     *
     * @return
     */

    @POST(UrlConstant.LOGOUT)
    Call<String> Logout();

    /**
     * 注册请求
     *
     * @param smsCode
     * @return
     */

    @FormUrlEncoded
    @POST(UrlConstant.REGISTER)
    Call<String> register(@Field("mobile") String mobile,
                          @Field("password") String password,
                          @Field("smsCode") String smsCode,
                          @Field("alias") String alias);

    /**
     * 评价
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.EVALUATION)
    Call<String> postEvaluation(@Field("orderId") int orderId,
                                @Field("driverSatisfy") boolean driverSatisfy,
                                @Field("driverTags") String driverTags,
                                @Field("businessScore") int businessScore,
                                @Field("tasteScore") int tasteScore,
                                @Field("packagesScore") int packagesScore,
                                @Field("anonymous") boolean anonymous,
                                @Field("comment") String comment,
                                @Field("isDeliver") int isDeliver);

    /**
     * 分类查询商家
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SEARCH_GOODS_TYPE)
    Call<String> searchGoods(@Field("page") Integer page,
                             @Field("size") Integer size,
                             @Field("userLng") String longitude,
                             @Field("userLat") String latitude,
                             @Field("name") String name,
                             @Field("sorting") Integer sort,
                             @Field("agentId") String agentId);

    /**
     * 商家代金券列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINESS_COUPON)
    Call<String> getBusinessCoupon(@Field("businessId") Integer businessId);

    /**
     * 领取商家代金券
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.RECEICER_BUSINESS_COUPON)
    Call<String> receiverBusinessCoupon(@Field("redActivityId") int redActivityId);


    /**
     * 可用商家代金券列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.MY_BUSINESS_CONPON)
    Call<String> getBusinessCoupan(@Field("businessId") int businessId);

    /**
     * 根据指定的购物车获取订单结算信息
     *
     * @return
     */
    @POST(UrlConstant.FILL_IN_DIY)
    Call<String> fillInDiy();

    /**
     * 商家分类列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_BUSINES_CATEGOTY)
    Call<String> getBusinessCategory(@Field("parentTypeId") int parentTypeId);

    /**
     * 获取特惠优选专区更多商家分页列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_PREFERENTIAL_BUSINES)
    Call<String> getPreferentialBusiness(@Field("agentId") String agentId,
                                         @Field("userLng") String longitude,
                                         @Field("userLat") String latitude,
                                         @Field("page") int page,
                                         @Field("size") int size);

    /**
     * 获取订单状态
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_ORDER_STATUS)
    Call<String> getOrderStatus(@Field("orderId") int orderId);

    /**
     * 获取最近订单
     *
     * @param agentId
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_RECENTLY_ORDER)
    Call<String> getRecentlyOrder(@Field("agentId") String agentId);

    /**
     * 判断手机号是否存在
     *
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.CHECK_MOBILE_EXIST)
    Call<String> checkMobileExist(@Field("mobile") String mobile);

    /**
     * 购物车勾选
     *
     * @return
     */
    @POST(UrlConstant.CHECK_SHOP_CART)
    Call<String> checkShopCart();

    /**
     * 获取用户评价列表
     *
     * @return
     */
    @POST(UrlConstant.GET_MY_EVALUATE)
    Call<String> getMyEvaluate();

    /**
     * 获取退款金额
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_REFUND_PRICE)
    Call<String> getRefundPrice(@Field("orderId") int orderId,
                                @FieldMap IdentityHashMap<String, String> orderItemId);

    /**
     * 退款
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.SUMBIT_REFUND)
    Call<String> sumbitRefund(@Field("orderId") int orderId,
                              @FieldMap IdentityHashMap<String, String> orderItemId,
                              @Field("reason") String reason);

    /**
     * 删除评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.DELETE_EVALUATION)
    Call<String> deleteEvaluate(@Field("commentId") int commentId);

    /**
     * 追加评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.EVALUATION_MORE)
    Call<String> evaluateMore(@Field("commentId") int commonId,
                              @Field("comment") String comment);

    /**
     * 首页领取红包
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_HOME_REDPACKAGE)
    Call<String> getHomeRedpackge(@Field("agentId") String agentId);

    /**
     * 个人中心领取红包
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_NEW_REDPACKAGE)
    Call<String> getNewRedpackge(@Field("agentId") String agentId);

    /**
     * 大额满减专区商家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_FULLLESS_ZONE_BUSINESS)
    Call<String> getFullLessZoneBusiness(@Field("agentId") String agentId,
                                         @Field("userLng") String userLng,
                                         @Field("userLat") String userLat,
                                         @Field("page") int page,
                                         @Field("size") int size);

    /**
     * 免配送费专区商家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GET_FREE_DELIVERY_ZONE_BUSINESS)
    Call<String> getFreeDeliveryZoneBusiness(@Field("agentId") String agentId,
                                             @Field("userLng") String userLng,
                                             @Field("userLat") String userLat,
                                             @Field("page") int page,
                                             @Field("size") int size);

//    /**
//     * 商家打烊，显示相同主营分类的商家列表
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(UrlConstant.GET_FREE_DELIVERY_ZONE_BUSINESS)
//    Call<String> getFreeDeliveryZoneBusiness(@Field("agentId") String agentId,
//                                             @Field("userLng") String userLng,
//                                             @Field("userLat") String userLat,
//                                             @Field("page") int page,
//                                             @Field("size") int size);

}
