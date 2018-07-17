package com.gxuc.runfast.shop.bean;

import java.util.List;

public class BusinessCategoryInfo {


    /**
     * id : 1
     * content : 中式快餐
     * createTime : 2016-10-14 10:23:59
     * name : 中餐
     * parent_id : null
     * deleted : null
     * children : [{"id":26,"content":null,"createTime":null,"name":"面条","parent_id":1,"deleted":null,"children":null},{"id":27,"content":null,"createTime":null,"name":"粥","parent_id":1,"deleted":null,"children":null}]
     */

    public String id;
    public String content;
    public String createTime;
    public String name;
    public Object parent_id;
    public Object deleted;
    public boolean isSelect;

    public List<ChildrenBean> children;


    public static class ChildrenBean {
        /**
         * id : 26
         * content : null
         * createTime : null
         * name : 面条
         * parent_id : 1
         * deleted : null
         * children : null
         */

        public int id;
        public String content;
        public String createTime;
        public String name;
        public int parent_id;
        public Object deleted;
        public Object children;
    }
}
