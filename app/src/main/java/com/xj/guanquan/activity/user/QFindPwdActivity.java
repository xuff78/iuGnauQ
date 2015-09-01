package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;

import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QFindPwdActivity extends QBaseActivity implements OnClickListener {
    private Button sendSms;
    private EditText phoneText;
    private EditText newPwdText;
    private Button loginbtn;
    private EditText confirmPwdText;
    private EditText codeText;

    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qfind_pwd);
    }

    @Override
    protected void initView() {
        initialize();
        _setRightHomeGone();
        _setHeaderTitle(getString(R.string.title_activity_qfind_pwd));

        sendSms = (Button) findViewById(R.id.sendSms);
        sendSms.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.ACCOUNT_FIND_PWD, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QFindPwdActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", phoneText.getText().toString());
                map.put("password", newPwdText.getText().toString());
                map.put("lng", PreferencesUtils.getString(QFindPwdActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QFindPwdActivity.this, "lat"));
                return map;
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == sendSms) {
            showToastShort("验证码已发送，请注意查收");
            sendSms.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendSms.setEnabled(true);
                }
            }, 10000);
        } else if (v == loginbtn) {
            if (StringUtils.isEmpty(phoneText.getText().toString())) {
                showToastShort("手机号不能为空！");
                return;
            }
            if (phoneText.getText().toString().length() != 11) {
                showToastShort("手机号输入有误！");
                return;
            }
            if (!StringUtils.isEquals(confirmPwdText.getText().toString(), confirmPwdText.getText().toString())) {
                showToastShort("两次密码输入不一致，请重新输入密码！");
                return;
            }
            addToRequestQueue(request, true);
        }
    }

    private void initialize() {
        phoneText = (EditText) findViewById(R.id.phoneText);
        newPwdText = (EditText) findViewById(R.id.newPwdText);
        loginbtn = (Button) findViewById(R.id.login_btn);
        confirmPwdText = (EditText) findViewById(R.id.confirmPwdText);
        codeText = (EditText) findViewById(R.id.codeText);
    }

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            alertDialog("密码找回成功！", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    QFindPwdActivity.this.finish();
                }
            });
        } else {
            alertDialog(result.getMsg(), null);
        }
    }
}
