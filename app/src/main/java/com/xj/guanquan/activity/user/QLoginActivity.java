package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

/**
 * A login screen that offers login via email/password.
 */
public class QLoginActivity extends QBaseActivity implements View.OnClickListener {

    private ImageView titlelogin;
    private View viewone;
    private ImageView titlepass;
    private View viewtwo;
    private TextView forgetpassword;
    private EditText editextpassword;
    private Button loginbtn;
    private TextView register;
    private TextView loginbuttom;
    private EditText edittextname;
    private ImageView titlename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlogin);
    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderGone();

        forgetpassword.setOnClickListener(this);
        register.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    private void initialize() {
        titlelogin = (ImageView) findViewById(R.id.title_login);
        viewone = (View) findViewById(R.id.viewone);
        titlepass = (ImageView) findViewById(R.id.title_pass);
        viewtwo = (View) findViewById(R.id.viewtwo);
        forgetpassword = (TextView) findViewById(R.id.forget_password);
        editextpassword = (EditText) findViewById(R.id.editext_password);
        loginbtn = (Button) findViewById(R.id.login_btn);
        register = (TextView) findViewById(R.id.register);
        loginbuttom = (TextView) findViewById(R.id.login_buttom);
        edittextname = (EditText) findViewById(R.id.edittext_name);
        titlename = (ImageView) findViewById(R.id.title_name);
    }

    @Override
    public void onClick(View v) {
        if (v == forgetpassword) {
            toActivity(QFindPwdActivity.class);
        } else if (v == register) {
            toActivity(QRegistInfoActivity.class);
        } else if (v == loginbtn) {
            PreferencesUtils.putString(this, "session", "test");
            this.finish();
        }
    }
}

