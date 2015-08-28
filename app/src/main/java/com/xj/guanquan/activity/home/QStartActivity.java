package com.xj.guanquan.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.xj.guanquan.R;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toActivity(QHomeActivity.class);
                QStartActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void initHandler() {

    }

}
