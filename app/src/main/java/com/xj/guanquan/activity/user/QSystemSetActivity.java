package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgSetActivity;
import com.xj.guanquan.common.QBaseActivity;

public class QSystemSetActivity extends QBaseActivity implements View.OnClickListener {
    private RelativeLayout notifySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsystem_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qsystem_set));
        _setRightHomeGone();

        notifySet = (RelativeLayout) findViewById(R.id.notifySet);
        notifySet.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == notifySet) {
            toActivity(QMsgSetActivity.class);
        }
    }
}
