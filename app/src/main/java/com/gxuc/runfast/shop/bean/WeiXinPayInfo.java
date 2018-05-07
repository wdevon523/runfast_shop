package com.gxuc.runfast.shop.bean;

import com.google.gson.annotations.SerializedName;

public class WeiXinPayInfo {

    /**
     * package : Sign=WXPay
     * appid : wx7b0ceb559269b805
     * sign : 455F05E75C3B783F4C2539D08DAAB812
     * partnerid : 1440581602
     * prepayid : wx0419355998554227e67d6a9b0836673308
     * noncestr : 022b89330e60461a84b3a23b13324d7c
     * timestamp : 1525433761
     */

    @SerializedName("package")
    public String packageX;
    public String appid;
    public String sign;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;

}
