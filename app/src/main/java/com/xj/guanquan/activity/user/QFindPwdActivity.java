package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QFindPwdActivity extends QBaseActivity implements OnClickListener {
    private Button sendSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qfind_pwd);
    }

    @Override
    protected void initView() {
        _setRightHomeGone();
        _setHeaderTitle(getString(R.string.title_activity_qfind_pwd));

        sendSms = (Button) findViewById(R.id.sendSms);
        sendSms.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == sendSms) {

        }
    }
}
