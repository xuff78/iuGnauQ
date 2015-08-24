package com.xj.guanquan.activity.found;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserInfo;
import com.xj.guanquan.views.pullscrollview.PullScrollView;
import com.xj.guanquan.views.pullscrollview.PullScrollView.OnTurnListener;

public class QUserDetailActivity extends QBaseActivity implements View.OnClickListener, OnTurnListener {
    private UserInfo userInfo;
    private SimpleDraweeView backgroundImage;
    private RecyclerView userPhotos;
    private PullScrollView scrollview;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quser_detail);
    }

    @Override
    protected void initView() {
        initialize();
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        _setHeaderTitle(userInfo.getName());
        _setRightHomeGone();
        _setRightHomeText("投诉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        scrollview.setHeader(backgroundImage);
        scrollview.setOnTurnListener(this);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        userPhotos.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        userPhotos.setLayoutManager(mLayoutManager);
        userPhotos.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    private void initialize() {
        backgroundImage = (SimpleDraweeView) findViewById(R.id.backgroundImage);
        userPhotos = (RecyclerView) findViewById(R.id.userPhotos);
        scrollview = (PullScrollView) findViewById(R.id.scroll_view);
    }

    @Override
    public void onTurn() {

    }
}
