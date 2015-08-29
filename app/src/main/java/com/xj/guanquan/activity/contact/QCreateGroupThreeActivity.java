package com.xj.guanquan.activity.contact;

import android.os.Bundle;
import android.view.View;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QCreateGroupThreeActivity extends QBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcreate_three_group);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcreate_group));
        _setRightHomeGone();
        _setRightHomeText("提交审核", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QCreateGroupThreeActivity.this.finish();
            }
        });
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }
}
