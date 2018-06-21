package com.gxuc.runfast.shop.bean.spend;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huiliu on 2017/9/13.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class AccountRecord implements Serializable{

    /**
     * id : 50032
     * balance : 55.1
     * cardnumber : w201709040006
     * cid : 471551
     * minmonety : null
     * mobile : 15871720785
     * monetary : -0.5
     * name : null
     * type : 0
     * createTime : 2017-09-04 13:31:57
     * genreType : null
     * typename : 消费
     * beforemonety : 55.6
     * showtype : 1
     */

    public int id;
    public double balance;
    public String cardnumber;
    public int cid;
    public Object minmonety;
    public String mobile;
    public BigDecimal monetary;
    public Object name;
    public int type;
    public String createTime;
    public Object genreType;
    public String typename;
    public double beforemonety;
    public int showtype;

}
