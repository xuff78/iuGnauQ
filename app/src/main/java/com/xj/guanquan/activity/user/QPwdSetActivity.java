package com.xj.guanquan.activity.user;

import android.os.Bundle;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QPwdSetActivity extends QBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpwd_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qpwd_set));
        _setRightHomeGone();

    }

    @Override
    protected void initHandler() {

    }
}
