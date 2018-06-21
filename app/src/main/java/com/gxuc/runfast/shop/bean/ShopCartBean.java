package com.gxuc.runfast.shop.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ShopCartBean implements Serializable {


    /**
     * orderId : null
     * businessImg : /upload/1526282189382.jpg
     * isDeliver : 0
     * businessId : 4165
     * businessName : 正一味
     * businessAddr : 武汉市
     * businessAddressLat : 30.581799
     * businessAddressLng : 114.363325
     * businessMobile : 13125195991
     * deliveryDuration : null
     * validActivityList : [{"id":null,"activityId":6095,"activityType":1,"shared":true,"less":2,"agentId":null,"businessId":null,"goodsId":null,"standarId":null,"orderId":null,"subsidy":1},{"id":null,"activityId":6098,"activityType":7,"shared":true,"less":0,"agentId":null,"businessId":null,"goodsId":null,"standarId":null,"orderId":null,"subsidy":1}]
     * toAddressId : null
     * userId : 471551
     * userName : null
     * userMobile : null
     * userPhone : null
     * userAddress : null
     * address : null
     * userAddressId : null
     * userAddressLat : null
     * userAddressLng : null
     * deliveryFee : 0
     * finalDeliveryFee : 0
     * oldShopper : null
     * oldShopperId : null
     * oldShopperMobile : null
     * agree : null
     * shopper : null
     * shopperId : null
     * shopperMobile : null
     * createTime : null
     * status : null
     * statStr : null
     * isReceive : null
     * isPay : null
     * isRefund : null
     * isComent : null
     * isCancel : null
     * refund : null
     * refundcontext : null
     * payTime : null
     * payType : null
     * orderNo : null
     * orderNumber : null
     * totalPay : 22335.78
     * totalPackageFee : 1.22
     * cartPrice : 22336.56
     * cartDisprice : null
     * offAmount : 2.0
     * cartTips : 已满30.0元可减2.0元
     * cartItems : [{"key":{"goodsId":197653,"standarId":380472,"optionIdPairList":null},"num":1,"goodsName":"MDZZ1","goodsImg":"","standarName":"1","standarOptionName":"1","price":11.56,"totalPrice":11.56,"activityId":null,"activityName":null,"activityType":null,"disprice":null,"totalDisprice":null,"createTime":"2018-06-06 14:40:14","checked":true},{"key":{"goodsId":197654,"standarId":380473,"optionIdPairList":null},"num":1,"goodsName":"WEWQE","goodsImg":"","standarName":"1","standarOptionName":"1","price":2,"totalPrice":2,"activityId":null,"activityName":null,"activityType":null,"disprice":null,"totalDisprice":null,"createTime":"2018-06-06 14:40:16","checked":true},{"key":{"goodsId":197655,"standarId":380474,"optionIdPairList":null},"num":1,"goodsName":"WDWQQ","goodsImg":"","standarName":"1","standarOptionName":"1","price":22323,"totalPrice":22323,"activityId":null,"activityName":null,"activityType":null,"disprice":null,"totalDisprice":null,"createTime":"2018-06-06 14:40:25","checked":true}]
     * distance : null
     * totalNum : 3
     * remark : null
     * userRedId : null
     * selfTime : null
     * suportSelf : null
     * selfMobile : null
     * suportSelf : null
     */

    public String orderId;
    public String businessImg;
    public int isDeliver;
    public int businessId;
    public String businessName;
    public String businessAddr;
    public String businessAddressLat;
    public String businessAddressLng;
    public String businessMobile;
    public String deliveryDuration;
    public Integer toAddressId;
    public int userId;
    public String userName;
    public String userMobile;
    public String userPhone;
    public String userAddress;
    public String address;
    public String userAddressId;
    public String userAddressLat;
    public String userAddressLng;
    public int deliveryFee;
    public int finalDeliveryFee;
    public Object oldShopper;
    public Object oldShopperId;
    public Object oldShopperMobile;
    public Object agree;
    public Object shopper;
    public Object shopperId;
    public Object shopperMobile;
    public String createTime;
    public Object status;
    public Object statStr;
    public Object isReceive;
    public boolean isPay;
    public boolean isRefund;
    public boolean isComent;
    public boolean isCancel;
    public Object refund;
    public Object refundcontext;
    public String payTime;
    public int payType;
    public String orderNo;
    public String orderNumber;
    public BigDecimal totalPay;
    public BigDecimal totalPackageFee;
    public BigDecimal cartPrice;
    public BigDecimal cartDisprice;
    public BigDecimal offAmount;
    public String cartTips;
    public Object distance;
    public int totalNum;
    public Object remark;
    public String userRedId;
    public String selfTime;
    public boolean suportSelf;
    public String selfMobile;
    public List<ValidActivityListBean> validActivityList;
    public List<CartItemsBean> cartItems;

}
