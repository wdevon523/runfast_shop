package com.gxuc.runfast.shop.bean;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.gxuc.runfast.shop.bean.gooddetail.GoodsSellStandard;
import com.gxuc.runfast.shop.bean.gooddetail.GoodsSellSubOption;
import com.gxuc.runfast.shop.bean.gooddetail.GoodsSellOption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodBean implements Serializable {

    private String isCommand;//是否推荐
    private String cut;//打折
    private String type;//类
    private String icon;//图片

    private int selectCount;

    private String goodsSpec;//规格
    private String goodsSpecId;//规格Id
    private String goodsType;//
    private String goodsTypeTwo;//
    private String goodsTypeSpecId;//子规格Id
    private String goodsSellOptionId;//商品选项ID
    private String goodsSellOptionName;//商品选项名称
    private String optionIds;//商品选项ID

    public String getGoodsSellOptionId() {
        return goodsSellOptionId;
    }

    public void setGoodsSellOptionId(String goodsSellOptionId) {
        this.goodsSellOptionId = goodsSellOptionId;
    }

    public String getGoodsSellOptionName() {
        return goodsSellOptionName;
    }

    public void setGoodsSellOptionName(String goodsSellOptionName) {
        this.goodsSellOptionName = goodsSellOptionName;
    }

    public String getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(String optionIds) {
        this.optionIds = optionIds;
    }

    private Integer id;
    private String businessName;//商家名称
    private Integer businessId;//商家id

    private String name;//商品名称
    private String imgPath;//原图
    private String mini_imgPath;//压缩图
    private BigDecimal price;//价格

    private String content;//商品描述
    private Integer typeId;//商品分类
    private String typeName;//商品分类
    private Integer sellTypeId;//商品类型（店内）
    private String sellTypeName;//商品类型（店内）
    private Integer status;//状态 0正常销售 1：暂停销售 2：商品下架
    private String createTime;//
    private Integer star;//商品星级
    private Integer salesnum;//月销量
    private Integer num;//数量
    private int ptype;//是否收取打包费0是1否

    private String agentName;//代理商名称：
    private Integer agentId;//代理商Id：

    private int islimited;//是否限购1是 0否
    private Integer limittype;//超出后是否允许原价购买0否 1是
    private int limitNum;//限购数量
    private String limiStartTime;//限购开始时间
    private String limiEndTime;//限购结束时间
    private BigDecimal discount;//优惠价
    private BigDecimal disprice;//优惠金额
    private Integer standardListPosition;
    private Integer shownum;//数量
    private Integer lid;
    private List<Integer> goodsellids; //商品id集合
    private Integer sid;//单规格 规格ID


    private String showprice;//活动名称

    private Integer isonly;//是否单规格单选项1否0是

    private String showzs;//赠送活动
    private Integer isdz;//是否有打折商品
    private String showlimit;//限购

    private List<GoodsSellStandard> standardList;//商品规格
    private List<GoodsSellOption> optionList;//商品选项
    private List<GoodsSellSubOption> opsubList;//商品子选项

    private ArrayList<GoodsSellStandardInfo> goodsSellStandardList;//商品规格
    private ArrayList<GoodsSellOptionInfo> goodsSellOptionList;//商品选项

    private SpecSelectInfo specInfo;//商品选项
    private int standardId;

    public int getStandardId() {
        return standardId;
    }

    public void setStandardId(int standardId) {
        this.standardId = standardId;
    }

    public SpecSelectInfo getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(SpecSelectInfo specInfo) {
        this.specInfo = specInfo;
    }

    public ArrayList<GoodsSellOptionInfo> getGoodsSellOptionList() {
        return goodsSellOptionList;
    }

    public void setGoodsSellOptionList(ArrayList<GoodsSellOptionInfo> goodsSellOptionList) {
        this.goodsSellOptionList = goodsSellOptionList;
    }

    public ArrayList<GoodsSellStandardInfo> getGoodsSellStandardList() {
        return goodsSellStandardList;
    }

    public void setGoodsSellStandardList(ArrayList<GoodsSellStandardInfo> goodsSellStandardList) {
        this.goodsSellStandardList = goodsSellStandardList;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeTwo() {
        return goodsTypeTwo;
    }

    public void setGoodsTypeTwo(String goodsTypeTwo) {
        this.goodsTypeTwo = goodsTypeTwo;
    }

    public Integer getIsonly() {
        return isonly;
    }

    public void setIsonly(Integer isonly) {
        this.isonly = isonly;
    }

    public String getShowprice() {
        return showprice;
    }

    public void setShowprice(String showprice) {
        this.showprice = showprice;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCommand() {
        return isCommand;
    }

    public void setIsCommand(String isCommand) {
        this.isCommand = isCommand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMini_imgPath() {
        return mini_imgPath;
    }

    public void setMini_imgPath(String mini_imgPath) {
        this.mini_imgPath = mini_imgPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getSellTypeId() {
        return sellTypeId;
    }

    public void setSellTypeId(Integer sellTypeId) {
        this.sellTypeId = sellTypeId;
    }

    public String getSellTypeName() {
        return sellTypeName;
    }

    public void setSellTypeName(String sellTypeName) {
        this.sellTypeName = sellTypeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getSalesnum() {
        return salesnum;
    }

    public void setSalesnum(Integer salesnum) {
        this.salesnum = salesnum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public int getIslimited() {
        return islimited;
    }

    public void setIslimited(int islimited) {
        this.islimited = islimited;
    }

    public Integer getLimittype() {
        return limittype;
    }

    public void setLimittype(Integer limittype) {
        this.limittype = limittype;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getLimiStartTime() {
        return limiStartTime;
    }

    public void setLimiStartTime(String limiStartTime) {
        this.limiStartTime = limiStartTime;
    }

    public String getLimiEndTime() {
        return limiEndTime;
    }

    public void setLimiEndTime(String limiEndTime) {
        this.limiEndTime = limiEndTime;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDisprice() {
        return disprice;
    }

    public void setDisprice(BigDecimal disprice) {
        this.disprice = disprice;
    }

    public Integer getShownum() {
        return shownum;
    }

    public void setShownum(Integer shownum) {
        this.shownum = shownum;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public List<Integer> getGoodsellids() {
        return goodsellids;
    }

    public void setGoodsellids(List<Integer> goodsellids) {
        this.goodsellids = goodsellids;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getShowzs() {
        return showzs;
    }

    public void setShowzs(String showzs) {
        this.showzs = showzs;
    }

    public Integer getIsdz() {
        return isdz;
    }

    public void setIsdz(Integer isdz) {
        this.isdz = isdz;
    }

    public String getShowlimit() {
        return showlimit;
    }

    public void setShowlimit(String showlimit) {
        this.showlimit = showlimit;
    }

    public List<GoodsSellOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<GoodsSellOption> optionList) {
        this.optionList = optionList;
    }

    public List<GoodsSellStandard> getStandardList() {
        return standardList;
    }

    public void setStandardList(List<GoodsSellStandard> standardList) {
        this.standardList = standardList;
    }

    public List<GoodsSellSubOption> getOpsubList() {
        return opsubList;
    }

    public void setOpsubList(List<GoodsSellSubOption> opsubList) {
        this.opsubList = opsubList;
    }

    public Integer getStandardListPosition() {
        return standardListPosition;
    }

    public void setStandardListPosition(Integer standardListPosition) {
        this.standardListPosition = standardListPosition;
    }

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getGoodsTypeSpecId() {
        return goodsTypeSpecId;
    }

    public void setGoodsTypeSpecId(String goodsTypeSpecId) {
        this.goodsTypeSpecId = goodsTypeSpecId;
    }

    @Override
    public String toString() {
        return "FoodBean{" +
                ", isCommand='" + isCommand + '\'' +
                ", cut='" + cut + '\'' +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                ", selectCount=" + selectCount +
                ", goodsSpec='" + goodsSpec + '\'' +
                ", goodsSpecId=" + goodsSpecId +
                ", goodsType='" + goodsType + '\'' +
                ", goodsTypeTwo='" + goodsTypeTwo + '\'' +
                ", goodsTypeSpecId=" + goodsTypeSpecId +
                ", id=" + id +
                ", businessName='" + businessName + '\'' +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", mini_imgPath='" + mini_imgPath + '\'' +
                ", price=" + price +
                ", content='" + content + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", sellTypeId=" + sellTypeId +
                ", sellTypeName='" + sellTypeName + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", star=" + star +
                ", salesnum=" + salesnum +
                ", num=" + num +
                ", ptype=" + ptype +
                ", agentName='" + agentName + '\'' +
                ", agentId=" + agentId +
                ", islimited=" + islimited +
                ", limittype=" + limittype +
                ", limitNum=" + limitNum +
                ", limiStartTime='" + limiStartTime + '\'' +
                ", limiEndTime='" + limiEndTime + '\'' +
                ", discount=" + discount +
                ", disprice=" + disprice +
                ", optionList=" + optionList +
                ", standardList=" + standardList +
                ", standardListPosition=" + standardListPosition +
                ", shownum=" + shownum +
                ", lid=" + lid +
                ", goodsellids=" + goodsellids +
                ", sid=" + sid +
                ", showprice='" + showprice + '\'' +
                ", isonly=" + isonly +
                ", opsubList=" + opsubList +
                ", showzs='" + showzs + '\'' +
                ", isdz=" + isdz +
                ", showlimit='" + showlimit + '\'' +
                '}';
    }

}
