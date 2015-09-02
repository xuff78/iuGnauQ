package com.xj.guanquan.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.user.QLoginActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.LocationService;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QStartActivity extends QBaseActivity {
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qstart);
    }

    @Override
    protected void initView() {
        _setHeaderGone();
        startLocationService();
    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.GET_ALL, this, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToRequestQueue(request, false);
            }
        }, 1000);
    }

    private void startLocationService() {
        //        Intent stopLocationServiceIntent = new Intent(this,
        //                LocationService.class);
        //        stopService(stopLocationServiceIntent);
        Intent startLocationServiceIntent = new Intent(QStartActivity.this,
                LocationService.class);
        startService(startLocationServiceIntent);
    }

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            PreferencesUtils.putString(this, "data_dict", result.getData().getJSONObject("content").toJSONString());
            Bundle bundle = new Bundle();
            bundle.putBoolean("isStart", true);
            toActivity(QLoginActivity.class, bundle);
            QStartActivity.this.finish();
        } else {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QStartActivity.this.finish();
                    System.exit(0);
                }
            });
        }
    }
}
