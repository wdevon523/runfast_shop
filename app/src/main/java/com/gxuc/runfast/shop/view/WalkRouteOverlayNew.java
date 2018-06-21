package com.gxuc.runfast.shop.view;

import android.content.Context;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.gxuc.runfast.shop.R;

public class WalkRouteOverlayNew extends WalkRouteOverlay {
    public WalkRouteOverlayNew(Context context, AMap aMap, WalkPath walkPath, LatLonPoint latLonPoint, LatLonPoint latLonPoint1) {
        super(context, aMap, walkPath, latLonPoint, latLonPoint1);
    }

    @Override
    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_map_start);
    }

    @Override
    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_star);
    }
}
