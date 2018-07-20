package com.gxuc.runfast.shop.bean;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class OrderInformation {

    /**
     * orderId : 18
     * businessImg : upload/1530529193262.jpg
     * isDeliver : 1
     * businessId : 13420
     * businessName : 全聚福小炒菜馆
     * businessAddr : 徐家棚街办事处秦园中路
     * businessAddressLat : 30.582476
     * businessAddressLng : 114.33965599999999
     * businessMobile : 15623252111
     * deliveryDuration : 40
     * validActivityList : []
     * userId : 489002
     * userName : 哦哦啦啦
     * userMobile : 18602728198
     * userPhone : 15607205410
     * userAddress : 都市经典(徐东大街)
     * address : 这
     * userAddressId : 145418
     * userAddressLat : 30.585778
     * userAddressLng : 114.361249
     * userAddressTag : 1
     * userAddressGender : 1
     * deliveryFee : 3.0
     * finalDeliveryFee : 0.0
     * oldShopper : null
     * oldShopperId : null
     * oldShopperMobile : null
     * agree : null
     * shopper : null
     * shopperId : null
     * shopperMobile : null
     * createTime : 2018-07-03 12:03:48
     * status : 2
     * statStr : 商家接单
     * isReceive : null
     * isPay : 1
     * isRefund : null
     * isComent : null
     * isCancel : null
     * refund : null
     * refundcontext : null
     * booked : true
     * bookTime : null
     * disTime : 2018-07-03 12:43:48
     * payTime : 2018-07-03 12:03:53
     * payType : 2
     * orderNo : nw20180703187288
     * orderNumber : 9
     * totalPay : 16.8
     * totalPackageFee : 2.0
     * cartPrice : 19.8
     * cartDisprice : 19.8
     * offAmount : 8.0
     * cartTips : null
     * limitTips : null
     * cartItems : [{"id":1765067,"key":{"goodsId":649457,"standarId":1149907,"optionIdPairList":[]},"num":2,"goodsName":"酸辣土豆丝","goodsImg":"upload/1530529831156.jpg","standarName":"份","standarOptionName":"份","price":9.9,"totalPrice":19.8,"activityId":null,"activityName":null,"activityType":null,"disprice":null,"totalDisprice":null,"createTime":"2018-07-03 12:03:48","checked":null}]
     * distance : 2099
     * totalNum : 2
     * remark : null
     * userRedId : null
     * userCouponId : null
     * selfTime : null
     * selfMobile : null
     * suportSelf : true
     * userSuportSelf : false
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
    public int userId;
    public String userName;
    public String userMobile;
    public String userPhone;
    public String userAddress;
    public String address;
    public int userAddressId;
    public double userAddressLat;
    public double userAddressLng;
    public Integer userAddressTag;
    public Integer userAddressGender;
    public BigDecimal deliveryFee;
    public BigDecimal finalDeliveryFee;
    public Object oldShopper;
    public Object oldShopperId;
    public Object oldShopperMobile;
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
    public Object refundcontext;
    public boolean booked;
    public String bookTime;
    public String disTime;
    public String payTime;
    public int payType;
    public String orderNo;
    public int orderNumber;
    public BigDecimal totalPay;
    public BigDecimal totalPackageFee;
    public BigDecimal cartPrice;
    public BigDecimal cartDisprice;
    public BigDecimal offAmount;
    public Object cartTips;
    public Object limitTips;
    public int distance;
    public int totalNum;
    public String remark;
    public Object userRedId;
    public String userCouponId;
    public String selfTime;
    public String selfMobile;
    public boolean suportSelf;
    public boolean userSuportSelf;
    public List<?> validActivityList;
    public List<CartItemsBean> cartItems;
}
