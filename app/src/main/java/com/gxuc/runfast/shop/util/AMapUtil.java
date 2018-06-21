/**
 *
 */
package com.gxuc.runfast.shop.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteRailwayItem;
import com.gxuc.runfast.shop.impl.constant.ChString;

public class AMapUtil {
    /**
     * 判断edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + ChString.Kilometer;
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + ChString.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + ChString.Meter;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    //路径规划方向指示和图片对应
//		public static int getDriveActionID(String actionName) {
//			if (actionName == null || actionName.equals("")) {
//				return R.drawable.dir3;
//			}
//			if ("左转".equals(actionName)) {
//				return R.drawable.dir2;
//			}
//			if ("右转".equals(actionName)) {
//				return R.drawable.dir1;
//			}
//			if ("向左前方行驶".equals(actionName) || "靠左".equals(actionName)) {
//				return R.drawable.dir6;
//			}
//			if ("向右前方行驶".equals(actionName) || "靠右".equals(actionName)) {
//				return R.drawable.dir5;
//			}
//			if ("向左后方行驶".equals(actionName) || "左转调头".equals(actionName)) {
//				return R.drawable.dir7;
//			}
//			if ("向右后方行驶".equals(actionName)) {
//				return R.drawable.dir8;
//			}
//			if ("直行".equals(actionName)) {
//				return R.drawable.dir3;
//			}
//			if ("减速行驶".equals(actionName)) {
//				return R.drawable.dir4;
//			}
//			return R.drawable.dir3;
//		}

//		public static int getWalkActionID(String actionName) {
//			if (actionName == null || actionName.equals("")) {
//				return R.drawable.dir13;
//			}
//			if ("左转".equals(actionName)) {
//				return R.drawable.dir2;
//			}
//			if ("右转".equals(actionName)) {
//				return R.drawable.dir1;
//			}
//			if ("向左前方".equals(actionName) || "靠左".equals(actionName)) {
//				return R.drawable.dir6;
//			}
//			if ("向右前方".equals(actionName) || "靠右".equals(actionName)) {
//				return R.drawable.dir5;
//			}
//			if ("向左后方".equals(actionName)) {
//				return R.drawable.dir7;
//			}
//			if ("向右后方".equals(actionName)) {
//				return R.drawable.dir8;
//			}
//			if ("直行".equals(actionName)) {
//				return R.drawable.dir3;
//			}
//			if ("通过人行横道".equals(actionName)) {
//				return R.drawable.dir9;
//			}
//			if ("通过过街天桥".equals(actionName)) {
//				return R.drawable.dir11;
//			}
//			if ("通过地下通道".equals(actionName)) {
//				return R.drawable.dir10;
//			}
//
//			return R.drawable.dir13;
//		}

    public static String getBusPathTitle(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        List<BusStep> busSetps = busPath.getSteps();
        if (busSetps == null) {
            return String.valueOf("");
        }
        StringBuffer sb = new StringBuffer();
        for (BusStep busStep : busSetps) {
            StringBuffer title = new StringBuffer();
            if (busStep.getBusLines().size() > 0) {
                for (RouteBusLineItem busline : busStep.getBusLines()) {
                    if (busline == null) {
                        continue;
                    }

                    String buslineName = getSimpleBusLineName(busline.getBusLineName());
                    title.append(buslineName);
                    title.append(" / ");
                }
//					RouteBusLineItem busline = busStep.getBusLines().get(0);

                sb.append(title.substring(0, title.length() - 3));
                sb.append(" > ");
            }
            if (busStep.getRailway() != null) {
                RouteRailwayItem railway = busStep.getRailway();
                sb.append(railway.getTrip() + "(" + railway.getDeparturestop().getName()
                        + " - " + railway.getArrivalstop().getName() + ")");
                sb.append(" > ");
            }
        }
        return sb.substring(0, sb.length() - 3);
    }

    public static String getBusPathDes(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        long second = busPath.getDuration();
        String time = getFriendlyTime((int) second);
        float subDistance = busPath.getDistance();
        String subDis = getFriendlyLength((int) subDistance);
        float walkDistance = busPath.getWalkDistance();
        String walkDis = getFriendlyLength((int) walkDistance);
        return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
    }

    public static String getSimpleBusLineName(String busLineName) {
        if (busLineName == null) {
            return String.valueOf("");
        }
        return busLineName.replaceAll("\\(.*?\\)", "");
    }

    /**
     * 启动高德App进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:58
     * <h3>UpdateTime</h3> 2016/6/27,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname           非必填 POI 名称
     * @param lat               必填 纬度
     * @param lon               必填 经度
     * @param dev               必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style             必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static void goToNaviActivity(Context context, String sourceApplication, String poiname, String lat, String lon, String dev, String style) {
////		StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
//		StringBuffer stringBuffer  = new StringBuffer("androidamap://openFeature?featureName=OnFootNavi&sourceApplication=")
//				.append(sourceApplication);
////		if (!TextUtils.isEmpty(poiname)){
////			stringBuffer.append("&poiname=").append(poiname);
////		}
//		stringBuffer.append("&lat=").append(lat)
//				.append("&lon=").append(lon)
//				.append("&dev=").append(dev);
////				.append("&style=").append(style);
//
//		Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
//		intent.setPackage("com.autonavi.minimap");
//		context.startActivity(intent);


        StringBuffer stringBuffer = new StringBuffer("amapuri://route/plan/?sourceApplication=")
                .append(sourceApplication);
//		if (!TextUtils.isEmpty(poiname)){
//			stringBuffer.append("&poiname=").append(poiname);
//		}
        stringBuffer.append("&dlat=").append(lat)
                .append("&dlon=").append(lon)
                .append("&dev=").append(dev)
                .append("&t=3");

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(Context context, String packageName) {
//        boolean exists = new File("/data/data/" + packageName).exists();
//        if (!exists) {
//            ToastUtil.showToast("您尚未安装高德地图");
//        }
//        return exists;
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        ToastUtil.showToast("您尚未安装高德地图");
        return false;


    }
}
