package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QFeedbackActivity extends QBaseActivity implements View.OnClickListener {

    private EditText opinion;
    private Button commitOpinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qfeedback);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qfeedback));
        _setRightHomeGone();
        initialize();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    private void initialize() {

        opinion = (EditText) findViewById(R.id.opinion);
        commitOpinion = (Button) findViewById(R.id.commitOpinion);
    }
}
