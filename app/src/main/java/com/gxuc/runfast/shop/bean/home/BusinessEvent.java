package com.gxuc.runfast.shop.bean.home;

import java.util.List;

public class BusinessEvent {


    /**
     * id : 6083
     * createTime : 2018-04-27 08:16:23
     * name : 特价
     * ptype : 4
     * specialType : null
     * specialName : null
     * is_limited : false
     * limit_type : null
     * limit_num : 2
     * disprice : 2.0
     * discount : 0.1
     * fullLessList : []
     */

    public int id;
    public String createTime;
    public String name;
    public int ptype;
    public Object fulls;
    public Object lesss;
    public Object specialType;
    public Object specialName;
    public boolean is_limited;
    public Object limit_type;
    public int limit_num;
    public double disprice;
    public double discount;
    public List<FullLess> fullLessList;

    public class FullLess {

        /**
         * id : 3
         * full : 30.0
         * less : 2.0
         * subsidy : 1.0
         * activity_id : 6095
         */

        public int id;
        public double full;
        public double less;
        public double subsidy;
        public int activity_id;

    }

}
