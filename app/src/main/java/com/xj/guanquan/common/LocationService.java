package com.xj.guanquan.common;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

/**
 * 定位服务,基于高德的定位API V1.3.3实现
 *
 * @author jixiangxiang
 */
public class LocationService extends Service implements AMapLocationListener {
    public static final long LOCATION_UPDATE_MIN_TIME = 10 * 1000;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 5;

    // 位置服务代理
    private LocationManagerProxy locationManagerProxy;

    public LocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManagerProxy = LocationManagerProxy.getInstance(this);
        // 定位方式设置为混合定位，包括网络定位和GPS定位
        locationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, LOCATION_UPDATE_MIN_TIME,
                LOCATION_UPDATE_MIN_DISTANCE, this);
        // 如果定位方式包括GPS定位需要手动设置GPS可用
        locationManagerProxy.setGpsEnable(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        super.onDestroy();

        // 在Service销毁的时候销毁定位资源
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destory();
        }
        //设置为null是为了提醒垃圾回收器回收资源
        locationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        //在较新的SDK版本中，这个方法在位置发生变化的时候不会被
        //调用。这个方法默认是使用原生GPS服务的时候，当位置
        //变化被调用的方法。
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //当位置发生变化的时候调用这个方法。
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // 如果位置获取错误则不作处理，退出本方法
        // 返回错误码如果为0则表明定位成功，反之则定位失败
        //在虚拟机测试的时候，返回错误码31，为未知错误
        //如果使用虚拟机测试的时候遇到这个问题，建议使用真机测试。
        if (aMapLocation == null
                || aMapLocation.getAMapException().getErrorCode() != 0) {
            Log.v("locationservice", aMapLocation == null ? "null" : "not null");
            if (aMapLocation != null) {
                Log.v("locationservice", "errorcode"
                        + aMapLocation.getAMapException().getErrorCode());
                Log.v("locationservice", "errormessage"
                        + aMapLocation.getAMapException().getErrorMessage());
            }
            Log.v("locationservice", "request error");
            return;
        }
        PreferencesUtils.putString(this, "lat", String.valueOf(aMapLocation.getLatitude()));
        PreferencesUtils.putString(this, "lng", String.valueOf(aMapLocation.getLongitude()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}