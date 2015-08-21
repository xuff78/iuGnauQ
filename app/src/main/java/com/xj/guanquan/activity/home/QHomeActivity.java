package com.xj.guanquan.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.fragment.contact.QContactFragment;
import com.xj.guanquan.fragment.found.QFindFragment;
import com.xj.guanquan.fragment.message.QMessageFragment;
import com.xj.guanquan.fragment.roast.QRoastFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qhome);
    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderTitle(getString(R.string.hello_blank_fragment), R.color.yellow);
        _setLeftBackGone();
        _setRightHomeGone();
        _setRightHomeText(getString(R.string.Filter), new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });
        initFragment(QFindFragment.newInstance(null, null));

        homeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioBtnfind.getId()) {
                    initFragment(QFindFragment.newInstance(null, null));
                    _setHeaderTitle(getString(R.string.hello_blank_fragment), R.color.yellow);
                } else if (checkedId == radioBtncontact.getId()) {
                    initFragment(QContactFragment.newInstance(null, null));
                    _setHeaderTitle(getString(R.string.hello_contact_fragment), R.color.yellow);
                } else if (checkedId == radioBtnme.getId()) {
                    initFragment(QMeFragment.newInstance(null, null));
                    _setHeaderTitle(getString(R.string.hello_me_fragment), R.color.yellow);
                } else if (checkedId == radioBtnmessage.getId()) {
                    initFragment(QMessageFragment.newInstance(null, null));
                    _setHeaderTitle(getString(R.string.hello_message_fragment), R.color.yellow);
                } else if (checkedId == raidoBtnshits.getId()) {
                    initFragment(QRoastFragment.newInstance(null, null));
                    _setHeaderTitle(getString(R.string.hello_roast_fragment), R.color.yellow);
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

    private void initFragment(Fragment fragment) {
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }
}
