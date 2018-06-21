package com.gxuc.runfast.shop.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItemsBean implements Serializable {
    /**
     * key : {"goodsId":197642,"standarId":380459,"optionIdPairList":[{"optionId":79262,"subOptionId":147733}]}
     * num : 2
     * goodsName : 草拟大爷
     * goodsImg :
     * standarName : 两份
     * standarOptionName : 两份 妮妮好好
     * price : 2.02
     * totalPrice : 4.04
     * activityId : null
     * activityName : null
     * activityType : null
     * disprice : null
     * totalDisprice : null
     * createTime : null
     * checked : null
     */

    public KeyBean key;
    public int num;
    public String goodsName;
    public String goodsImg;
    public String standarName;
    public String standarOptionName;
    public BigDecimal price;
    public String totalPrice;
    public Object activityId;
    public Object activityName;
    public Object activityType;
    public BigDecimal disprice;
    public BigDecimal totalDisprice;
    public Object createTime;
    public boolean checked;

}