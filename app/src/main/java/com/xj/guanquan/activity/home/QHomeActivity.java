package com.xj.guanquan.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.fragment.contact.QContactFragment;
import com.xj.guanquan.fragment.found.QFindUserFragment;
import com.xj.guanquan.fragment.message.QMessageFragment;
import com.xj.guanquan.fragment.roast.TucaoMianFrg;
import com.xj.guanquan.fragment.user.QMeFragment;

import common.eric.com.ebaselibrary.util.FragmentManagerUtil;

public class QHomeActivity extends QBaseActivity implements OnClickListener {

    private LinearLayout replaceFragment;
    private RadioButton radioBtnfind;
    private RadioButton raidoBtnshits;
    private RadioButton radioBtncontact;
    private RadioButton radioBtnmessage;
    private RadioButton radioBtnme;
    private RadioGroup homeGroup;
    private long mLastExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qhome);
    }

    @Override
    protected void initView() {

        initialize();
        initFragment(QFindUserFragment.newInstance(null, null));
        homeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioBtnfind.getId()) {
                    initFragment(QFindUserFragment.newInstance(null, null));
                } else if (checkedId == radioBtncontact.getId()) {
                    initFragment(QContactFragment.newInstance());
                } else if (checkedId == radioBtnme.getId()) {
                    initFragment(QMeFragment.newInstance(null, null));
                } else if (checkedId == radioBtnmessage.getId()) {
                    initFragment(QMessageFragment.newInstance(null, null));
                } else if (checkedId == raidoBtnshits.getId()) {
                    initFragment(TucaoMianFrg.newInstance());
                }
            }
        });

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    public void initFragment(Fragment fragment) {
        initTitle(fragment);
        FragmentManagerUtil.newInstance().replaceFragment(getSupportFragmentManager(), fragment, R.id.replaceFragment);
    }

    private void initialize() {

        replaceFragment = (LinearLayout) findViewById(R.id.replaceFragment);
        radioBtnfind = (RadioButton) findViewById(R.id.radioBtn_find);
        raidoBtnshits = (RadioButton) findViewById(R.id.raidoBtn_shits);
        radioBtncontact = (RadioButton) findViewById(R.id.radioBtn_contact);
        radioBtnmessage = (RadioButton) findViewById(R.id.radioBtn_message);
        radioBtnme = (RadioButton) findViewById(R.id.radioBtn_me);
        homeGroup = (RadioGroup) findViewById(R.id.homeGroup);
    }

    public void initTitle(Fragment fragment) {
        //因为具体每个界面的导航栏都不一样，所以就隐藏掉activity的导航，直接在fragment
        //定义导航栏 add by jixiangxiang
        _setHeaderGone();
    }


    @Override
    public void onBackPressed() {
        alertConfirmDialog(getString(R.string.exit_app), new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        }, null);
    }
}
