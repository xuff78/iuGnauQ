package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QInvisibleSetActivity extends QBaseActivity implements View.OnClickListener {

    private Button showAll;
    private Button hideSelf;
    private Button invisibleAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinvisible_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qinvisible_set));
        _setRightHomeGone();
        initialize();

        showAll.setOnClickListener(this);
        hideSelf.setOnClickListener(this);
        invisibleAll.setOnClickListener(this);

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == showAll) {
            showAll.setSelected(!showAll.isSelected());
        } else if (v == hideSelf) {
            hideSelf.setSelected(!hideSelf.isSelected());
        } else if (v == invisibleAll) {
            invisibleAll.setSelected(!invisibleAll.isSelected());
        }
    }

    private void initialize() {
        showAll = (Button) findViewById(R.id.showAll);
        hideSelf = (Button) findViewById(R.id.hideSelf);
        invisibleAll = (Button) findViewById(R.id.invisibleAll);
    }
}
