package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.TimeCount;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
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
            if (StringUtils.isEmpty(phoneText.getText().toString().trim()) || phoneText.getText().toString().length() != 11) {
                showToastShort("请输入正确的手机号码！");
                return;
            }
            SMSSDK.getVerificationCode("86", phoneText.getText().toString());
            getProgressDialog().show();
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
            SMSSDK.submitVerificationCode("86", phoneText.getText().toString(), codeText.getText().toString());
            getProgressDialog().show();
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

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    getProgressDialog().dismiss();
                    addToRequestQueue(request, true);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    getProgressDialog().dismiss();
                    showToastShort("验证码已经发送");
                    TimeCount time = TimeCount.getInstance(60000, 1000, sendSms, QFindPwdActivity.this);
                    time.start();
                }
            } else {
                getProgressDialog().dismiss();
                ((Throwable) data).printStackTrace();
                showToastShort("验证码错误");
            }

        }

    };
}
