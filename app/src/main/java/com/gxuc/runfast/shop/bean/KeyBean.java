package com.gxuc.runfast.shop.bean;

import java.io.Serializable;
import java.util.List;

public class KeyBean implements Serializable {
    /**
     * goodsId : 197642
     * standarId : 380459
     * optionIdPairList : [{"optionId":79257,"subOptionId":147712}]
     */

    public int goodsId;
    public int standarId;
    public List<OptionIdPairListBean> optionIdPairList;


    public class OptionIdPairListBean implements Serializable {
        /**
         * optionId : 79257
         * subOptionId : 147712
         */

        public int optionId;
        public int subOptionId;

    }
}
