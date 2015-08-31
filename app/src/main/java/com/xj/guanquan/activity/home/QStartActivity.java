package com.xj.guanquan.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.xj.guanquan.R;
import com.xj.guanquan.activity.user.QLoginActivity;
import com.xj.guanquan.common.LocationService;
import com.xj.guanquan.common.QBaseActivity;

public class QStartActivity extends QBaseActivity {

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = new Bundle();
                bundle.putBoolean("isStart", true);
                toActivity(QLoginActivity.class, bundle);
                QStartActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void initHandler() {

    }

    private void startLocationService() {
        //        Intent stopLocationServiceIntent = new Intent(this,
        //                LocationService.class);
        //        stopService(stopLocationServiceIntent);
        Intent startLocationServiceIntent = new Intent(QStartActivity.this,
                LocationService.class);
        startService(startLocationServiceIntent);
    }

}
