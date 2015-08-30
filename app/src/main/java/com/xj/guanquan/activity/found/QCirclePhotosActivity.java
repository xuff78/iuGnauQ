package com.xj.guanquan.activity.found;

import android.os.Bundle;
import android.view.View;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QCirclePhotosActivity extends QBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcircle_photos);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcircle_photos));
        _setRightHome(R.mipmap.paizhao, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
