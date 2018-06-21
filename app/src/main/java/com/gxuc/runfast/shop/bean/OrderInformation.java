package com.gxuc.runfast.shop.bean;

import java.util.List;

public class OrderInformation {


    /**
     * orderId : 528745
     * businessImg : /upload/1526282220217.jpg
     * isDeliver : 0
     * businessId : 4166
     * businessName : 古茗
     * businessAddr : 武汉市
     * businessAddressLat : 30.59081
     * businessAddressLng : 114.361361
     * businessMobile : 13125195992
     * deliveryDuration : 25
     * validActivityList : []
     * toAddressId : 145286
     * userId : 471551
     * userName : 啦啦啊了
     * userMobile : null
     * userPhone : null
     * userAddress : 万科金域华府(友谊大道)
     * address : 10085
     * userAddressId : 145286
     * userAddressLat : 30.60667855971042
     * userAddressLng : 114.35815602441797
     * deliveryFee : 2.0
     * finalDeliveryFee : 2.0
     * oldShopper : null
     * oldShopperId : null
     * oldShopperMobile : null
     * agree : null
     * shopper : null
     * shopperId : null
     * shopperMobile : null
     * createTime : 2018-06-07 14:02:49
     * status : 0
     * statStr : 客户下单
     * isReceive : null
     * isPay : null
     * isRefund : null
     * isComent : null
     * isCancel : null
     * refund : null
     * refundcontext : null
     * payTime : null
     * payType : null
     * orderNo : nw20180607672317
     * orderNumber : 2
     * totalPay : 12.0
     * totalPackageFee : 0.0
     * cartPrice : 10.0
     * cartDisprice : 0
     * offAmount : 0.0
     * cartTips : null
     * cartItems : [{"key":{"goodsId":57999,"standarId":137988,"optionIdPairList":[{"optionId":33569,"subOptionId":56111}]},"num":1,"goodsName":"桂林卤粉","goodsImg":"/upload/1492001362040.jpg","standarName":"三两","standarOptionName":null,"price":8,"totalPrice":8,"activityId":null,"activityName":"","activityType":null,"disprice":8,"totalDisprice":null,"createTime":"2017-07-29 12:08:23","checked":null},{"key":{"goodsId":197635,"standarId":380448,"optionIdPairList":[]},"num":1,"goodsName":"丝袜奶茶","goodsImg":"/upload/1526300308927.jpg","standarName":"一杯","standarOptionName":"一杯","price":10,"totalPrice":10,"activityId":null,"activityName":null,"activityType":null,"disprice":null,"totalDisprice":null,"createTime":"2018-06-07 14:02:49","checked":null}]
     * distance : 1790
     * totalNum : 1
     * remark :
     * userRedId : null
     * selfTime : null
     * selfMobile : null
     * suportSelf : false
     */

    public int orderId;
    public String businessImg;
    public int isDeliver;
    public int businessId;
    public String businessName;
    public String businessAddr;
    public double businessAddressLat;
    public double businessAddressLng;
    public String businessMobile;
    public int deliveryDuration;
    public int toAddressId;
    public int userId;
    public String userName;
    public Object userMobile;
    public Object userPhone;
    public String userAddress;
    public String address;
    public int userAddressId;
    public double userAddressLat;
    public double userAddressLng;
    public double deliveryFee;
    public double finalDeliveryFee;
    public String oldShopper;
    public String oldShopperId;
    public String oldShopperMobile;
    public Object agree;
    public String shopper;
    public String shopperId;
    public String shopperMobile;
    public String createTime;
    public int status;
    public String statStr;
    public Object isReceive;
    public Integer isPay;
    public Integer isRefund;
    public Integer isComent;
    public Integer isCancel;
    public Object refund;
    public String refundcontext;
    public String payTime;
    public int payType;
    public String orderNo;
    public int orderNumber;
    public double totalPay;
    public double totalPackageFee;
    public double cartPrice;
    public double cartDisprice;
    public double offAmount;
    public Object cartTips;
    public int distance;
    public int totalNum;
    public String remark;
    public Object userRedId;
    public String selfTime;
    public String selfMobile;
    public boolean suportSelf;
    public List<?> validActivityList;
    public List<CartItemsBean> cartItems;

}
