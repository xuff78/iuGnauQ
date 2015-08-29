package com.xj.guanquan.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QAddFriendActivity extends QBaseActivity implements View.OnClickListener {

    private SearchView searchView;
    private RecyclerView searchResultView;
    private ImageView addGroupIcon;
    private RelativeLayout createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qadd_friend);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qadd_friend));
        _setRightHomeGone();
        initialize();

        createGroup.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        toActivity(QCreateGroupActivity.class);
    }

    private void initialize() {
        searchView = (SearchView) findViewById(R.id.searchView);
        searchResultView = (RecyclerView) findViewById(R.id.searchResultView);
        addGroupIcon = (ImageView) findViewById(R.id.addGroupIcon);
        createGroup = (RelativeLayout) findViewById(R.id.createGroup);
    }
}
