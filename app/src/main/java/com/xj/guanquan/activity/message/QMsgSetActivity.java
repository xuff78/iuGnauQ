package com.xj.guanquan.activity.message;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QMsgSetActivity extends QBaseActivity implements View.OnClickListener {

    private Button msgNotify;
    private Button actNotify;
    private Button groupNotify;
    private Button opinionNotify;
    private Button hideNotify;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmsg_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qmsg_set));
        _setRightHomeGone();
        initialize();

        msgNotify.setOnClickListener(this);
        actNotify.setOnClickListener(this);
        groupNotify.setOnClickListener(this);
        opinionNotify.setOnClickListener(this);
        hideNotify.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == msgNotify) {
            msgNotify.setSelected(!msgNotify.isSelected());
        } else if (v == actNotify) {
            actNotify.setSelected(!actNotify.isSelected());
        } else if (v == groupNotify) {
            groupNotify.setSelected(!groupNotify.isSelected());
        } else if (v == opinionNotify) {
            opinionNotify.setSelected(!opinionNotify.isSelected());
        } else if (v == hideNotify) {
            hideNotify.setSelected(!hideNotify.isSelected());
        }
    }

    private void initialize() {

        msgNotify = (Button) findViewById(R.id.msgNotify);
        actNotify = (Button) findViewById(R.id.actNotify);
        groupNotify = (Button) findViewById(R.id.groupNotify);
        opinionNotify = (Button) findViewById(R.id.opinionNotify);
        hideNotify = (Button) findViewById(R.id.hideNotify);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }
}
