package com.gxuc.runfast.shop.bean;

import java.math.BigDecimal;
import java.util.HashMap;

public class SpecSelectInfo {


    /**
     * standarId : 1
     * optionIdPairList : [{"optionId":1,"subOptionId":2}]
     * num : 2
     */

    //    public int standarId;
    public int num;
    public int standardId;
    public String standardName;
    public String standarOptionName;
    public BigDecimal totalPrice;

    public Integer isLimited;
    public int limitNum;

    public BigDecimal price;
    public BigDecimal disprice;

    //optionId,subOptionId 的 map
    public HashMap<Integer, SubOptionInfo> optionIdMap;

}
