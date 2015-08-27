package com.xj.guanquan.activity.message;

import android.os.Bundle;
import android.view.View;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QMsgSetActivity extends QBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmsg_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qmsg_set));
        _setRightHomeGone();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }
}
