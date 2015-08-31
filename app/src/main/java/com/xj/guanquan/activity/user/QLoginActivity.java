package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;

import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

/**
 * A login screen that offers login via email/password.
 */
public class QLoginActivity extends QBaseActivity implements View.OnClickListener {
    private StringRequest request;

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

    private Boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlogin);
        if (getIntent().getExtras() != null) {
            isStart = getIntent().getExtras().getBoolean("isStart");
        }
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
        request = new StringRequest(Request.Method.POST, ApiList.ACCOUNT_LOGIN, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", edittextname.getText().toString());
                map.put("password", editextpassword.getText().toString());
                map.put("lng", PreferencesUtils.getString(QLoginActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QLoginActivity.this, "lat"));
                return map;
            }
        };
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
            addToRequestQueue(request, true);
        }
    }


    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            PreferencesUtils.putString(QLoginActivity.this, "loginData", (String) response);
            if (isStart) {
                toActivity(QHomeActivity.class);
            } else {
                setResult(999);
            }
            QLoginActivity.this.finish();
        } else {
            alertDialog(result.getMsg(), null);
        }
    }
}