package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserDetailInfo;

import common.eric.com.ebaselibrary.util.StringUtils;

public class QRegistActivity extends QBaseActivity implements View.OnClickListener {

    private EditText phoneText;
    private EditText pwdText;
    private Button sendSms;
    private EditText codeText;
    private Button nextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qregist);

    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderTitle(getString(R.string.title_activity_qregist));
        _setRightHomeGone();


    }

    @Override
    protected void initHandler() {

    }

    private void initialize() {
        phoneText = (EditText) findViewById(R.id.phoneText);
        pwdText = (EditText) findViewById(R.id.pwdText);
        sendSms = (Button) findViewById(R.id.sendSms);
        codeText = (EditText) findViewById(R.id.codeText);
        nextStep = (Button) findViewById(R.id.nextStep);
    }

    @Override
    public void onClick(View v) {
        if (v == sendSms) {
            showToastShort("目前测试阶段，验证码可不输入！");
        } else if (v == nextStep) {
            if (StringUtils.isEmpty(phoneText.getText().toString().trim())) {
                showToastShort("请输入正确的手机号码！");
                return;
            }
            if (StringUtils.isEmpty(pwdText.getText().toString().trim())) {
                showToastShort("请输入正确的密码！");
                return;
            }
            UserDetailInfo userDetailInfo = new UserDetailInfo();
            userDetailInfo.setPhone(phoneText.getText().toString());
            userDetailInfo.setPassword(pwdText.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putSerializable("userDetailInfo", userDetailInfo);
            toActivity(QRegistInfoActivity.class, bundle);
        }
    }
}
