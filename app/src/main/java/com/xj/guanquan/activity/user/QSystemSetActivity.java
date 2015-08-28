package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgSetActivity;
import com.xj.guanquan.common.QBaseActivity;

public class QSystemSetActivity extends QBaseActivity implements View.OnClickListener {
    private RelativeLayout notifySet;
    private RelativeLayout invisibleSet;
    private RelativeLayout pwdSet;
    private RelativeLayout blackListSet;
    private ScrollView scrollView;
    private RelativeLayout score;
    private RelativeLayout clearCache;
    private RelativeLayout feedBackSet;
    private Button logOut;
    private RelativeLayout userProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsystem_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qsystem_set));
        _setRightHomeGone();
        initialize();

        notifySet.setOnClickListener(this);
        pwdSet.setOnClickListener(this);
        invisibleSet.setOnClickListener(this);
        blackListSet.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        feedBackSet.setOnClickListener(this);
        score.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == notifySet) {
            toActivity(QMsgSetActivity.class);
        } else if (v == pwdSet) {
            toActivity(QPwdSetActivity.class);
        } else if (v == invisibleSet) {
            toActivity(QInvisibleSetActivity.class);
        } else if (v == blackListSet) {
            toActivity(QBlackListActivity.class);
        } else if (v == clearCache) {
            showToastShort("缓存清理成功!");
        } else if (v == feedBackSet) {
            toActivity(QFeedbackActivity.class);
        } else if (v == score) {
        }
    }

    private void initialize() {
        notifySet = (RelativeLayout) findViewById(R.id.notifySet);
        invisibleSet = (RelativeLayout) findViewById(R.id.invisibleSet);
        pwdSet = (RelativeLayout) findViewById(R.id.pwdSet);
        blackListSet = (RelativeLayout) findViewById(R.id.blackListSet);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        score = (RelativeLayout) findViewById(R.id.score);
        clearCache = (RelativeLayout) findViewById(R.id.clearCache);
        feedBackSet = (RelativeLayout) findViewById(R.id.feedBackSet);
        logOut = (Button) findViewById(R.id.logOut);
        userProtocol = (RelativeLayout) findViewById(R.id.userProtocol);
    }
}
