package com.gxuc.runfast.shop.bean.order;

import com.gxuc.runfast.shop.bean.redpackage.RedPackage;

import java.util.List;

/**
 * Created by Devon on 2017/10/17.
 */

public class ShoppingCartInfo {


    /**
     * isfull : 0
     * bnum : 3
     * packets : [{"id":2166,"couponId":35,"cuserId":471551,"cuserName":"15871720785","couponName":"1","price":1,"quantity":1,"start":"2017-10-23 00:00:00","end":"2017-10-31 00:00:00","full":5,"createTime":"2017-10-23 18:32:31","userd":0,"range1":1,"rangeId":null,"businessName":null,"islimited":0,"startuse":null,"enduse":null,"agentName":"","agentId":null,"type":0,"yiyuangou":null},{"id":2167,"couponId":35,"cuserId":471551,"cuserName":"15871720785","couponName":"1","price":1,"quantity":1,"start":"2017-10-23 00:00:00","end":"2017-10-31 00:00:00","full":5,"createTime":"2017-10-23 18:32:40","userd":0,"range1":1,"rangeId":null,"businessName":null,"islimited":0,"startuse":null,"enduse":null,"agentName":"","agentId":null,"type":0,"yiyuangou":null},{"id":2168,"couponId":35,"cuserId":471551,"cuserName":"15871720785","couponName":"1","price":1,"quantity":1,"start":"2017-10-23 00:00:00","end":"2017-10-31 00:00:00","full":5,"createTime":"2017-10-23 18:32:59","userd":0,"range1":1,"rangeId":null,"businessName":null,"islimited":0,"startuse":null,"enduse":null,"agentName":"","agentId":null,"type":0,"yiyuangou":null}]
     * crecord : 2166
     * totalprice : 21.0
     * totaldisprice : 0.0
     * tpacking : 1.0
     * pnum : 1
     * activityId : -1
     * activityname :
     * shoppings : [{"id":1570598,"cid":471551,"cname":null,"businessId":179,"businessName":"小恒羊","barCode":null,"goodsSellName":"羊肉水煎包(个)","goodsSellId":152905,"goodsSellStandardId":313948,"goodsSellStandardName":"个","goodsSellOptionId":0,"goodsSellOptionName":null,"optionIds":null,"num":4,"price":4,"openid":null,"packing":null,"ptype":0,"pricedis":null,"disprice":null,"islimited":null,"limitNum":0,"total":null,"full":null,"couponid":null,"list":null,"showprice":16,"showid":null,"goods":null,"acid":null,"acidName":null,"atype":null,"goodsSellRecordId":null},{"id":1570599,"cid":471551,"cname":null,"businessId":179,"businessName":"小恒羊","barCode":null,"goodsSellName":"羊肉水煎粑(个)","goodsSellId":155354,"goodsSellStandardId":318877,"goodsSellStandardName":"个","goodsSellOptionId":0,"goodsSellOptionName":null,"optionIds":null,"num":1,"price":4,"openid":null,"packing":null,"ptype":0,"pricedis":null,"disprice":null,"islimited":null,"limitNum":1,"total":null,"full":null,"couponid":null,"list":null,"showprice":4,"showid":null,"goods":null,"acid":null,"acidName":null,"atype":null,"goodsSellRecordId":null}]
     * disprice : 21.0
     */

    public int isfull;
    public int bnum;
    public int crecord;
    public String totalprice;
    public String totaldisprice;
    public String tpacking;
    public String pnum;
    public int activityId;
    public String activityname;
    public String disprice;
    public List<RedPackage> packets;
    public List<ShoppingCartGoodsInfo> shoppings;

}
