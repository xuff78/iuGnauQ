package com.xj.guanquan.activity.user;

import android.os.Bundle;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QFindPwdActivity extends QBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qfind_pwd);
    }

    @Override
    protected void initView() {
        _setRightHomeGone();
        _setHeaderTitle(getString(R.string.title_activity_qfind_pwd));
    }

    @Override
    protected void initHandler() {

    }
}
