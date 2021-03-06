package com.gxuc.runfast.shop.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 天上白玉京 on 2017/8/21.
 */

public class User implements Parcelable{


    /**
     * id : 488600
     * code :
     * areaName :
     * orgType :
     * mobile : 13716139127
     * name :
     * nickname :
     * password : e10adc3949ba59abbe56e057f20f883e
     * gender :
     * logTime :
     * qq :
     * xinl :
     * email :
     * score : 0
     * pic :
     * openid :
     * remainder : 0
     * totalremainder :
     * card : 03259021
     * byRoles :
     * createTime : 2017-07-28 16:38:04
     * provinceId :
     * cityId :
     * cityName :
     * countyId :
     * countyName :
     * townId :
     * townName :
     * rnum :
     * consume :
     * bptype :
     * bdpushChannelId :
     * bdpushUserId :
     * pushType :
     * otherId :
     * purviewLevel :
     * pids : []
     * orgIds : []
     * roleIdList : []
     * startTime :
     * endTime :
     * keyword :
     * showremainder : 0
     * minmonety :
     * addrName :
     * addrContent :
     * addrLog :
     * addrLat :
     */

    private int id;
    private String code;//区域编码
    private String areaName;//区域名称
    private String orgType;//所属层级
    private String mobile;//电话号码
    private String name;//名称
    private String nickname;//昵称
    private String password;//密码
    private Integer gender;//性别 0:男  ,1:女

    private String logTime;//最后登陆时间
    private String qq;//qq号
    private String xinl;//新浪微博
    private String email;//邮箱
    private Double score;//用户积分

    private String pic;//头像地址
    private String openid;//微信openid
    private BigDecimal remainder;//可用余额
    private BigDecimal totalremainder;//账号余额
    private String card;//会员卡
    private String byRoles;//所属角色

    private String createTime;//注册时间

    //层级字段
    private String provinceId;//省级id,区公司
    private String cityId;//城市
    private String cityName;
    private String countyId;//县份
    private String countyName;
    private String townId;//乡镇
    private String townName;
    private Integer rnum;//红包个数
    private BigDecimal consume;//累计消费金额

    private Integer bptype;//0或空  Android用户  1 ios用户
    private String bdpushChannelId;//推送的渠道id
    private String bdpushUserId;
    private Integer pushType;//push类型0百度推送1小米推送2华为推送
    private String otherId;//push类型对应的用户标识

    private Integer purviewLevel;//4省级,3,市级2，县级，1乡镇
    private Set<Integer> pids = new HashSet<Integer>();//用户权限ID
    private List<Integer> orgIds = new ArrayList<Integer>();//用户所在组织的orgid,存储用户所在的orgid及对应的下级 orgid
    private List<Integer> roleIdList;
    private String startTime;//查询时使用
    private String endTime;//查询时使用
    private String keyword;//查询时使用
    private BigDecimal showremainder;//账号余额
    private BigDecimal minmonety;//最低提现金额
    private String addrName;//地址
    private String addrContent;//地址详细
    private String addrLog;//地址经度
    private String addrLat;//地址纬度
    private String unusedCoupon;//可用优惠券数量

    public String getUnusedCoupon() {
        return unusedCoupon;
    }

    public void setUnusedCoupon(String unusedCoupon) {
        this.unusedCoupon = unusedCoupon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer  getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getXinl() {
        return xinl;
    }

    public void setXinl(String xinl) {
        this.xinl = xinl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public BigDecimal getRemainder() {
        return remainder;
    }

    public void setRemainder(BigDecimal remainder) {
        this.remainder = remainder;
    }

    public BigDecimal getTotalremainder() {
        return totalremainder;
    }

    public void setTotalremainder(BigDecimal totalremainder) {
        this.totalremainder = totalremainder;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getByRoles() {
        return byRoles;
    }

    public void setByRoles(String byRoles) {
        this.byRoles = byRoles;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Integer getRnum() {
        return rnum;
    }

    public void setRnum(Integer rnum) {
        this.rnum = rnum;
    }

    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }

    public Integer getBptype() {
        return bptype;
    }

    public void setBptype(Integer bptype) {
        this.bptype = bptype;
    }

    public String getBdpushChannelId() {
        return bdpushChannelId;
    }

    public void setBdpushChannelId(String bdpushChannelId) {
        this.bdpushChannelId = bdpushChannelId;
    }

    public String getBdpushUserId() {
        return bdpushUserId;
    }

    public void setBdpushUserId(String bdpushUserId) {
        this.bdpushUserId = bdpushUserId;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public Integer getPurviewLevel() {
        return purviewLevel;
    }

    public void setPurviewLevel(Integer purviewLevel) {
        this.purviewLevel = purviewLevel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public BigDecimal getShowremainder() {
        return showremainder;
    }

    public void setShowremainder(BigDecimal showremainder) {
        this.showremainder = showremainder;
    }

    public BigDecimal getMinmonety() {
        return minmonety;
    }

    public void setMinmonety(BigDecimal minmonety) {
        this.minmonety = minmonety;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getAddrContent() {
        return addrContent;
    }

    public void setAddrContent(String addrContent) {
        this.addrContent = addrContent;
    }

    public String getAddrLog() {
        return addrLog;
    }

    public void setAddrLog(String addrLog) {
        this.addrLog = addrLog;
    }

    public String getAddrLat() {
        return addrLat;
    }

    public void setAddrLat(String addrLat) {
        this.addrLat = addrLat;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", areaName='" + areaName + '\'' +
                ", orgType='" + orgType + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", logTime='" + logTime + '\'' +
                ", qq='" + qq + '\'' +
                ", xinl='" + xinl + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", pic='" + pic + '\'' +
                ", openid='" + openid + '\'' +
                ", remainder=" + remainder +
                ", totalremainder=" + totalremainder +
                ", card='" + card + '\'' +
                ", byRoles='" + byRoles + '\'' +
                ", createTime='" + createTime + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countyId='" + countyId + '\'' +
                ", countyName='" + countyName + '\'' +
                ", townId='" + townId + '\'' +
                ", townName='" + townName + '\'' +
                ", rnum=" + rnum +
                ", consume=" + consume +
                ", bptype=" + bptype +
                ", bdpushChannelId='" + bdpushChannelId + '\'' +
                ", bdpushUserId='" + bdpushUserId + '\'' +
                ", pushType=" + pushType +
                ", otherId='" + otherId + '\'' +
                ", purviewLevel=" + purviewLevel +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", keyword='" + keyword + '\'' +
                ", showremainder=" + showremainder +
                ", minmonety=" + minmonety +
                ", addrName='" + addrName + '\'' +
                ", addrContent='" + addrContent + '\'' +
                ", addrLog='" + addrLog + '\'' +
                ", addrLat='" + addrLat + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.areaName);
        dest.writeString(this.orgType);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeString(this.nickname);
        dest.writeString(this.password);
        dest.writeValue(this.gender);
        dest.writeString(this.logTime);
        dest.writeString(this.qq);
        dest.writeString(this.xinl);
        dest.writeString(this.email);
        dest.writeValue(this.score);
        dest.writeString(this.pic);
        dest.writeString(this.openid);
        dest.writeSerializable(this.remainder);
        dest.writeSerializable(this.totalremainder);
        dest.writeString(this.card);
        dest.writeString(this.byRoles);
        dest.writeString(this.createTime);
        dest.writeString(this.provinceId);
        dest.writeString(this.cityId);
        dest.writeString(this.cityName);
        dest.writeString(this.countyId);
        dest.writeString(this.countyName);
        dest.writeString(this.townId);
        dest.writeString(this.townName);
        dest.writeValue(this.rnum);
        dest.writeSerializable(this.consume);
        dest.writeValue(this.bptype);
        dest.writeString(this.bdpushChannelId);
        dest.writeString(this.bdpushUserId);
        dest.writeValue(this.pushType);
        dest.writeString(this.otherId);
        dest.writeValue(this.purviewLevel);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.keyword);
        dest.writeSerializable(this.showremainder);
        dest.writeSerializable(this.minmonety);
        dest.writeString(this.addrName);
        dest.writeString(this.addrContent);
        dest.writeString(this.addrLog);
        dest.writeString(this.addrLat);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.areaName = in.readString();
        this.orgType = in.readString();
        this.mobile = in.readString();
        this.name = in.readString();
        this.nickname = in.readString();
        this.password = in.readString();
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.logTime = in.readString();
        this.qq = in.readString();
        this.xinl = in.readString();
        this.email = in.readString();
        this.score = (Double) in.readValue(Double.class.getClassLoader());
        this.pic = in.readString();
        this.openid = in.readString();
        this.remainder = (BigDecimal) in.readSerializable();
        this.totalremainder = (BigDecimal) in.readSerializable();
        this.card = in.readString();
        this.byRoles = in.readString();
        this.createTime = in.readString();
        this.provinceId = in.readString();
        this.cityId = in.readString();
        this.cityName = in.readString();
        this.countyId = in.readString();
        this.countyName = in.readString();
        this.townId = in.readString();
        this.townName = in.readString();
        this.rnum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.consume = (BigDecimal) in.readSerializable();
        this.bptype = (Integer) in.readValue(Integer.class.getClassLoader());
        this.bdpushChannelId = in.readString();
        this.bdpushUserId = in.readString();
        this.pushType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.otherId = in.readString();
        this.purviewLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.keyword = in.readString();
        this.showremainder = (BigDecimal) in.readSerializable();
        this.minmonety = (BigDecimal) in.readSerializable();
        this.addrName = in.readString();
        this.addrContent = in.readString();
        this.addrLog = in.readString();
        this.addrLat = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
