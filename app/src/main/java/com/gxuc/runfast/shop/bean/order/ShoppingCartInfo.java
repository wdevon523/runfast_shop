package com.gxuc.runfast.shop.bean.order;

import java.util.List;

/**
 * Created by Devon on 2017/10/17.
 */

public class ShoppingCartInfo {

    /**
     * num : 7
     * prices : 28.0
     * rows : [{"id":1570240,"cid":471551,"cname":null,"businessId":179,"businessName":"小恒羊","barCode":null,"goodsSellName":"羊肉水煎包(个)","goodsSellId":152905,"goodsSellStandardId":313948,"goodsSellStandardName":"个","goodsSellOptionId":0,"goodsSellOptionName":null,"optionIds":null,"num":5,"price":4,"openid":null,"packing":null,"ptype":0,"pricedis":null,"disprice":null,"islimited":null,"limitNum":null,"list":null,"showprice":20,"showid":null,"goods":null,"acid":null,"acidName":null,"atype":null,"goodsSellRecordId":null},{"id":1570241,"cid":471551,"cname":null,"businessId":179,"businessName":"小恒羊","barCode":null,"goodsSellName":"羊肉水煎粑(个)","goodsSellId":155354,"goodsSellStandardId":318877,"goodsSellStandardName":"个","goodsSellOptionId":0,"goodsSellOptionName":null,"optionIds":null,"num":2,"price":4,"openid":null,"packing":null,"ptype":0,"pricedis":null,"disprice":null,"islimited":null,"limitNum":null,"list":null,"showprice":8,"showid":null,"goods":null,"acid":null,"acidName":null,"atype":null,"goodsSellRecordId":null}]
     */

    public int num;
    public String tejia;
    public String totalprice;
    public String zidingdan;
    public String packing;
    public String disprice;
    public String prices;
    public String totalpay;
    public List<ShoppingCartGoodsInfo> shopping;

}
