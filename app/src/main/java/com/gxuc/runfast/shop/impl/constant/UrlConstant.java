package com.gxuc.runfast.shop.impl.constant;


import com.gxuc.runfast.shop.data.ApiServiceFactory;

/**
 * 常量
 *
 * @author Sun.bl
 * @version [1.0, 2016/6/23]
 */
public class UrlConstant {

    public static final String HOST = ApiServiceFactory.PAORTUI_HOST;
    public static final String BaseUrl = ApiServiceFactory.BASE_URL;

    public static final String ImageBaseUrl = "http://image.gxptkc.com/";
//    public static final String ImageBaseUrl = "http://192.168.2.221:8080";
//    public static final String ImageBaseUrl = "http://192.168.2.192:8082";

    public static final String ImageHeadBaseUrl = "http://image.gxptkc.com/";

//    public static final String BaseUrl = "http://115.28.39.61:28080/wanglu/";

//    public static final String BaseUrl = "http://api.wanglu114.com/wanglu/";

//    public static final String BaseUrl = "http://saasapi.wanglu114.com:19081/wanglu/";

    /**
     * 初始化
     */
    public static final String INIT = BaseUrl + "init";

    /***
     * 上传文件
     */
    public static final String UPLOAD_FILE = "uploadfile";

    /**
     * 检查更新
     */
    public static final String QUERY_APP_UPDATE = "getLatestVersion";

    /**
     * 获取首页轮播图
     */
    public static final String GET_ADVERT = "getAdvertadd.do";

    /**
     * 登录
     */
    public static final String LOGIN = "login.do";

    /**
     * 个人中心
     */
    public static final String USER_INFO = "user/index.do";

    /**
     * 获取找回密码验证码
     */
    public static final String FORGET_PASSWORD = "findpwd.do";

    /**
     * 首页
     */
    public static final String INDEX = "index.do";

    /**
     * 主页
     */
    public static final String HOME_PAGE = "getHomepageadd.do";

    /**
     * 商家列表
     */
    public static final String GET_BUSINESS_LIST = "getBusiness.do";

    /**
     * 设置当前位置页
     */
    public static final String SET_CURRENT_ADDRESS = "address/setAddress.do";

    /***
     * 获取注册验证码
     */
    public static final String QUERY_AUTH_CODE = "getSmsCode.do";

    /***
     * 获取绑定手机的验证码
     */
    public static final String BIND_MOBILE_CODE = "getThirdSmsCode.do";

    /***
     * 获取修改密码验证码
     */
    public static final String QUERY_EDIT_PWD_CODE = "user/getcode.do";

    /***
     * 获取快速登录验证码
     */
    public static final String LOGIN_QIUCK_CODE = "getCode.do";

    /***
     * 上传图片
     */
    public static final String UPLOAD_PIC = HOST + "user/wm/my/fileUpload";

    /***
     * 更换头像
     */
    public static final String QUERY_EDIT_HEAD = "user/editavatar.do";

    /***
     *  商家列表
     */
    public static final String GET_BUSINESS = "getBusiness.do";
    /***
     *  分类商家
     */
    public static final String BUSINESS_LIST = "business/list.do";

    /***
     * 商家商品列表
     */
    public static final String GET_GOODS_LIST = "business/getGoods.do";

    /***
     * 商家活动列表
     */
    public static final String GET_BUSINESS_ACTIVITY = "business/getactivify.do";

    /**
     * 地图定位上传
     */
    public static final String GET_ADDRESS = "getaddressadd.do";


    /**
     * 商品详情
     */
    public static final String GOODS_DETAIL = "business/deail.do";

    /**
     * 商家收藏
     */
    public static final String GET_IS_SHOUCANG = "business/getIsShouCang.do";

    /***
     * 商家评价
     */
    public static final String BUSINESS_COMMENT = "business/businessComment.do";

    /***
     * 商家信息
     */
    public static final String BUSINESS_INFO = "business/showBusiness.do";


    /**
     * 忘记密码
     */
    public static final String FORGOT_PWD = "sendpwd.do";
    /**
     * 修改密码
     */
    public static final String EDIT_PASSWORD = "user/editPassword.do";


    /***
     * 获取规格数据
     */
    public static final String GET_GUIGE = "business/getguige.do";

    /***
     * 未读新闻数
     */
    public static final String UN_READ_NEWS = "unReadNews";

    /**
     * 反馈
     */
    public static final String ADD_FEEDBACK = "getAlarmInfo";
    /**
     * 二维码
     */
    public static final String APP_DOWNLOAD = "AppDownload";
    /**
     * 通讯录
     */
    public static final String GET_MAIL_LIST = "getMailList";

    /**
     * 历史
     */
    public static final String GET_HISTORY_ALARMS = "historyAlarms";

    /**
     * 通讯录
     */
    public static final String SAVE_POSITION = "savePosition";

    /**
     * 工区坐标点
     */
    public static final String GET_PROJECT_POINTS = "getProjectPoints";

    /**
     * 工友坐标点
     */
    public static final String GET_ORTHER_USER_POSITIONS = "getEmployeePositions";

    /**
     * 文件上传路径
     */
    public static final String KMZ_UPLOAD = "KMZ_upload";

    /**
     * 获取KMZ路径
     */
    public static final String GET_KMZ_PATH = "getKMZList";
    /**
     * 用户投诉
     */
    public static final String MINE_COMPLAINT = "userComplain/add.do";
    /**
     * 订单投诉
     */
    public static final String ORDER_COMPLAINT = "userComplain/add.do";
    /**
     * 加盟合作
     */
    public static final String JOIN_LEAGUE = "user/join.do";
    /**
     * 收藏商家/商品
     */
    public static final String SAVE_SHOP = "shopping/saveenshrine.do";
    /**
     * 收货地址列表
     */
    public static final String USER_ADDRESS_LIST = "cuserAddress/index.do";
    /**
     * 添加收货地址
     */
    public static final String ADD_USER_ADDRESS = "cuserAddress/postadd.do";
    /**
     * 修改收货地址
     */
    public static final String EDIT_USER_ADDRESS = "cuserAddress/edit.do";
    /**
     * 收货地址详情
     */
    public static final String DETAIL_USER_ADDRESS = "cuserAddress/deail.do";
    /**
     * 删除收货地址
     */
    public static final String DELETE_USER_ADDRESS = "cuserAddress/delete.do";
    /**
     * 消息列表
     */
    public static final String MESSAGE_LIST = "messge/getdate.do";
    /**
     * 订单列表
     */
    public static final String ORDER_LIST = "userOrder/getPageBean.do";
    /**
     * 修改昵称
     */
    public static final String CHANGE_NAME = "user/editNickname.do";
    /**
     * 绑定邮箱
     */
    public static final String CHANGE_EMAIL = "user/editEmail.do";
    /**
     * 获取红包
     */
    public static final String RED_PACKAGE = "pay/getConpon.do";
    /**
     * 我的优惠券
     */
    public static final String MY_CONPON = "coupon/mycoupon.do";

    /**
     * 领取优惠券
     */
    public static final String GET_CONPON = "coupon/receive.do";
    /**
     * 积分明细
     */
    public static final String LIST_SCORE = "userScore/getdate.do";
    /**
     * 我的积分
     */
    public static final String SCORE_DATA = "userScore/list.do";

    /**
     * 提现账号
     */
    public static final String BANK_INFO = "wallet/bankaccounts.do";
    /**
     * 删除提现账号
     */
    public static final String DELETE_BANK_INFO = "wallet/delete.do";

    /**
     * 提现银行卡列表
     */
    public static final String WATHDRAWALL_LIST = "wallet/withdrawal1.do";
    /**
     * 获取银行卡开户行
     */
    public static final String GET_BANK_NAME = "validateAndCacheCardInfo.json?_input_charset=utf-8";

    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL_INFO = "userOrder/dateil.do";
    /**
     * 我的收藏
     */
    public static final String MY_ENSHRINE = "myenshrine/index.do";
    /**
     * 生成订单
     */
//    public static final String CREATE_ORDER = "pay/addGoodsSellRecord.do";
    public static final String CREATE_ORDER = "pay/addGoodsSellRecordadd.do";
    /**
     * 添加购物车
     */
    public static final String ORDER_CAR = "pay/addCart.do";
    /**
     * 钱包支付
     */
    public static final String WALLET_PAY = "user/wm/order/wallletPay";
    /**
     * 跑腿钱包支付
     */
    public static final String PAO_TUI_WALLET_PAY = "user/order/wallletPay";
    /**
     * 支付宝支付
     */
    public static final String ALIPAY_PAY = "pay/alipayresultmap.do";
    /**
     * 微信支付
     */
    public static final String WEIXIN_PAY = "pay/wxPay.do";

    /**
     * 微信签名
     */
    public static final String WEIXIN_SIGN = "pay/signForApp.do";

    /**
     * 确认收货
     */
    public static final String ORDER_RECEIVE = "user/wm/order/complete";

    /**
     * 取消订单
     */
    public static final String ORDER_CANCEL = "userOrder/cancel.do";

    /**
     * 确认订单获取商品
     */
//    public static final String GET_SHOPPING_CART = "pay/getCart.do";
    public static final String GET_SHOPPING_CART = "pay/fillinorder.do";

    /**
     * 获取购物车
     */
//    public static final String GET_SHOPPING_CART = "pay/getCart.do";
    public static final String GET_SHOPPINGS = "pay/getShopings.do";

    /**
     * 选择优惠券
     */
    public static final String SELECT_COUPON = "pay/getcoupon.do";

    /**
     * 选择收货地址
     */
    public static final String SELECT_ADDR = "userOrder/busshowps.do";

    /**
     * 微信充值
     */
    public static final String WEIXIN_RECHARGE = "pay/wxRecharge.do";

    /**
     * 再来一单
     */
    public static final String BUY_AGAIN = "pay/recuraddcart.do";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "user/userinfo.do";


    /**
     * 获取订单支付状态
     */
    public static final String GET_ORDER_PAY_STATUS = "pay/orderQuery.do";

    /**
     * 清空购物车
     */
    public static final String CLEAN_SHOPPING_CART = "pay/clearCart.do";

    /**
     * 清空购物车
     */
    public static final String GET_SERVICE_INFO = "custom.do";

    /**
     * 检查版本
     */
    public static final String CHECK_NEW_VERSION = "user/wm/my/checkVersion";

    /**
     * 检查版本
     */
    public static final String GET_DRIVER_LATLNG = "user/wm/order/driverLocation";

    /**
     * 根据商品id获取店家id
     */
    public static final String GET_BUSINESS_ID = "business/getguige.do";


    public static final String WEIXIN_LOGIN = "sns/oauth2/access_token";

    /**
     * 递交订单（代购）
     */
    public static final String PURCHASE = "user/order/confirm";

    /**
     * 获取跑腿代购订单列表
     */
    public static final String DELIVERY_ORDER = "user/order/list";

    /**
     * 获取跑腿代购订单详情
     */
    public static final String DELIVERY_ORDER_DETAIL = "user/order/detail";

    /**
     * 删除跑腿代购订单
     */
    public static final String DELETE_DELIVERY_ORDER = "user/order/delete";
    /**
     * 取消跑腿代购订单
     */
    public static final String CANCEL_DELIVERY_ORDER = "user/order/cancel";

    /**
     * 获取跑腿代购订单状态列表
     */
    public static final String DELIVERY_ORDER_STATUS = "user/order/status";

    /**
     * 根据填写的订单获取配送信息
     */
    public static final String DELIVERY_ORDER_INFO = "user/order/fillIn";

    /**
     * 根据填写的订单获取配送信息
     */
    public static final String PAO_TUI_PAY = "user/pay/prepay";

    /**
     * 查询跑腿订单状态
     */
    public static final String QUERY_PAOTUI_STATUS = "user/pay/orderQuery";

    /**
     * 获取当前所属代理商
     */
    public static final String GET_AGENT = "user/wm/home/agent";

    /**
     * 统一获取获取代理商促销信息
     */
    public static final String GET_HOME_ACT = "user/wm/home/page";

    /**
     * 获取附近商家列表
     */
    public static final String GET_NEAR_BY_BUSINESS = "user/wm/home/businessNearBy";

    /**
     * 获取附近商家列表
     */
    public static final String GET_BUSINESS_DETAIL = "user/wm/business/detail";

    /**
     * 获取商家所有商品
     */
    public static final String GET_BUSINESS_GOODS = "user/wm/goods/catalogs";

    /**
     * 添加购物车
     */
    public static final String ADD_SHOP_CART = "user/wm/cart/add";

    /**
     * 删除购物车
     */
    public static final String SUB_SHOP_CART = "user/wm/cart/delete";

    /**
     * 获取商家购物车列表
     */
    public static final String BUSINESS_SHOP_CART = "user/wm/cart/list";

    /**
     * 清空购物车
     */
    public static final String CLEAR_SHOP_CART = "user/wm/cart/clear";

    /**
     * 根据商家购物车获取订单结算信息
     */
    public static final String SUBMIT_BUSINESS_SHOP_CART = "user/wm/cart/fillIn";

    /**
     * 生成订单
     */
    public static final String CREATE_ORDER_NEW = "user/wm/order/confirm";

    /**
     * 生成订单
     */
    public static final String PAY_ORDER = "user/wm/pay/prepay";

    /**
     * 查询订单状态
     */
    public static final String ORDER_QUERY = "user/wm/pay/orderQuery";

    /**
     * 获取商家下的用户评论列表
     */
    public static final String BUSINESS_EVALUATION = "user/wm/business/listComment";

    /**
     * 获取用户订单列表
     */
    public static final String GET_ORDER_LIST = "user/wm/order/list";

    /**
     * 再来一单
     */
    public static final String BUY_AGAIN_NEW = "user/wm/order/recur";

    /**
     * 取消订单
     */
    public static final String CANCEL_ORDER = "user/wm/order/cancel";

    /**
     * 获取当前用户的收货地址列表
     */
    public static final String ADDRESS_LIST = "user/wm/address/list";

    /**
     * 修改收货地址
     */
    public static final String UPDATE_ADDRESS = "user/wm/address/update";

    /**
     * 删除当前用户所属的收货地址
     */
    public static final String DELETE_ADDRESS = "user/wm/address/delete";

    /**
     * 添加用户地址
     */
    public static final String ADD_ADDRESS = "user/wm/address/add";

    /**
     * 登陆
     */
    public static final String LOGIN_NEW = "user/account/login";

    /**
     * 根据原密码修改密码
     */
    public static final String UPDATE_PWD_BY_OLD = "user/wm/my/updatePwdByOld";

    /**
     * 根据短信验证码修改密码
     */
    public static final String UPDATE_PWD_BY_SMSCODE = "user/wm/my/updatePwdBySmsCode";

    /**
     * 获取当前用户信息
     */
    public static final String GET_USER_INFORMATION = "user/wm/my/info";

    /**
     * 修改用户信息
     */
    public static final String UPDATE_USER_INFO = "user/wm/my/update";

    /**
     * 商户加盟
     */
    public static final String BUSINESS_JOIN = "user/wm/my/businessJoin";

    /**
     * 用户投诉
     */
    public static final String COMPLAIN = "user/wm/my/complain";

    /**
     * 获取代理商客服
     */
    public static final String GET_CUSTOM_INFO = "user/wm/my/custom";

    /**
     * 获取用户积分明细
     */
    public static final String GET_SCORE_LIST = "user/wm/my/scoreRecord";

    /**
     * 获取用户消息分页列表
     */
    public static final String GET_MESSAGE_LIST = "user/wm/message/list";

    /**
     * 获取用户银行卡账户列表
     */
    public static final String GET_BANK_LIST = "user/wm/bankAccount/list";

    /**
     * 删除银行卡账户
     */
    public static final String DELETE_BANK = "user/wm/bankAccount/delete";

    /**
     * 添加银行卡
     */
    public static final String ADD_BANK = "user/wm/bankAccount/add";

    /**
     * 获取用户收藏列表
     */
    public static final String GET_FAVORITE_LIST = "user/wm/enshrine/list";

    /**
     * 收藏商家
     */
    public static final String ADD_FAVORITE = "user/wm/enshrine/add";

    /**
     * 取消收藏商家
     */
    public static final String DELETE_FAVORITE = "user/wm/enshrine/delete";

    /**
     * 获取当前用户的红包活动分页列表
     */
    public static final String GET_MY_RED_PACK = "user/wm/redPacket/list";

    /**
     * 获取当前用户可用的红包活动分页列表
     */
    public static final String GET_MY_RED_PACK_VALID = "user/wm/redPacket/listValid";

    /**
     * 提现
     */
    public static final String CASH_SEND = "user/wm/wallet/withdraw";

    /**
     * 提现记录
     */
    public static final String CASH_RECORD = "user/wm/wallet/withdrawRecord";

    /**
     * 收支明细
     */
    public static final String LIST_CONSUME = "user/wm/wallet/inOutDetail";

    /**
     * 获取当前用户所有的购物车
     */
    public static final String ALL_SHOP_CART = "user/wm/cart/all";

    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL = "user/wm/order/detail";

    /**
     * 快速登录
     */
    public static final String LOGIN_QUICK = "user/account/login";

    /**
     * 发送短信
     */
    public static final String SEND_SMS = "user/wm/my/sendSms";

    /**
     * 第三方登录
     */
    public static final String THIRD_LOGIN = "api/user/account/login";

    /***
     * 通过短信验证码注册并绑定第三方账户
     */
    public static final String BIND_MOBILE = "user/account/registerAndBind";

    /**
     * 登出
     */
    public static final String LOGOUT = "user/account/logout";

    /**
     * 注册
     */
    public static final String REGISTER = "user/account/register ";

    /**
     * 商品分类搜索
     */
    public static final String SEARCH_GOODS_TYPE = "user/wm/home/search";

    /**
     * 商家代金券列表
     */
    public static final String GET_BUSINESS_COUPON = "user/wm/business/redActivityForPick";

    /**
     * 领取商家代金券或者红包
     */
    public static final String RECEICER_BUSINESS_COUPON = "user/wm/redPacket/pick";


    /**
     * 优惠券领取中心列表
     */
    public static final String MY_BUSINESS_CONPON = "user/wm/redPacket/listValidCoupon";

    /**
     * 根据指定的购物车获取订单结算信息
     */
    public static final String FILL_IN_DIY = "user/wm/cart/fillInDiy";

    /**
     * 商家分类列表
     */
    public static final String GET_BUSINES_CATEGOTY = "user/wm/home/businessSubTypes";

    /**
     * 获取特惠优选专区更多商家分页列表
     */
    public static final String GET_PREFERENTIAL_BUSINES = "user/wm/home/offZoneMoreBusiness";

    /**
     * 获取订单状态
     */
    public static final String GET_ORDER_STATUS = "user/wm/order/status";

    /**
     * 获取最近订单
     */
    public static final String GET_RECENTLY_ORDER = "user/wm/home/latestOrder";

    /**
     * 判断手机号是否存在
     */
    public static final String CHECK_MOBILE_EXIST = "user/account/mobileExist";

    /**
     * 根据指定的购物车获取订单结算信息
     */
    public static final String CHECK_SHOP_CART = "user/wm/cart/chooseCartItem";

    /**
     * 获取用户评价列表
     */
    public static final String GET_MY_EVALUATE = "user/wm/my/listComment";

    /**
     * 获取退款金额
     */
    public static final String GET_REFUND_PRICE = "user/wm/order/applyRefundInfo";

    /**
     * 退款
     */
    public static final String SUMBIT_REFUND = "user/wm/order/applyRefund";

    /**
     * 评价
     */
    public static final String EVALUATION = "user/wm/business/addComment";

    /**
     * 删除评价
     */
    public static final String DELETE_EVALUATION = "user/wm/business/deleteComment";

    /**
     * 追加评论
     */
    public static final String EVALUATION_MORE = "user/wm/business/reComment";

    /***
     * 首页领取红包
     */
    public static final String GET_HOME_REDPACKAGE = "user/wm/home/redActivityForPick";

    /***
     * 个人中心领取红包
     */
    public static final String GET_NEW_REDPACKAGE = "user/wm/my/redActivityForPick";

    /***
     * 优惠专区商家列表
     */
    public static final String GET_ZONE_BUSINESS = "user/wm/home/zoneBusiness";

    /***
     * 大额满减专区商家列表
     */
    public static final String GET_FULLLESS_ZONE_BUSINESS = "user/wm/home/fullLessZoneBusiness";

    /***
     * 免配送费专区商家列表
     */
    public static final String GET_FREE_DELIVERY_ZONE_BUSINESS = "user/wm/home/freeDeliveryZoneBusiness";


}
