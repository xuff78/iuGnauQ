package com.xj.guanquan.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

public class QMapSelectActivity extends QBaseActivity {
    private MapView mapView;
    private AMap aMap;
    private Bundle bundle;
    private MarkerOptions markerOptions;
    private double lat;
    private double lng;
    private String addr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmap_select);
        bundle = savedInstanceState;
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qmap_select));
        _setRightHomeGone();
        _setRightHomeText("保存地点", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("addr", addr);
                setResult(RESULT_OK, intent);
                QMapSelectActivity.this.finish();
            }
        });
        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(bundle);// 必须要写
        aMap = mapView.getMap();

        markerOptions = new MarkerOptions();
        lat = Double.valueOf(PreferencesUtils.getString(this, "lat"));
        lng = Double.valueOf(PreferencesUtils.getString(this, "lng"));
        LatLng point = new LatLng(lat, lng);
        markerOptions.position(point);
        markerOptions.draggable(true);
        markerOptions.title("当前位置");
        aMap.addMarker(markerOptions);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, aMap.getMaxZoomLevel() * 7 / 8));
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                aMap.clear();
                lat = latLng.latitude;
                lng = latLng.longitude;
                markerOptions.position(latLng);
                markerOptions.draggable(true);
                markerOptions.title("要选择的位置");
                aMap.addMarker(markerOptions);
                //aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            }
        });
    }

    @Override
    protected void initHandler() {

    }

}
