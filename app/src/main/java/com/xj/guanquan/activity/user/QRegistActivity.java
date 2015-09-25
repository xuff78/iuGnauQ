package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.TimeCount;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserDetailInfo;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QRegistActivity extends QBaseActivity implements View.OnClickListener {

    private EditText phoneText;
    private EditText pwdText;
    private Button sendSms;
    private EditText codeText;
    private Button nextStep;

    private StringRequest request;
    private Boolean isUsePhone = false;

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

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString();
                if (phone.length() == 11) {
                    addToRequestQueue(request, true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

        request = new StringRequest(Request.Method.POST, ApiList.ACCOUNT_CHECK, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", phoneText.getText().toString());
                map.put("lng", PreferencesUtils.getString(QRegistActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QRegistActivity.this, "lat"));
                return map;
            }
        };
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
            if (StringUtils.isEmpty(phoneText.getText().toString().trim()) || phoneText.getText().toString().length() != 11) {
                showToastShort("请输入正确的手机号码！");
                return;
            }
            if (!isUsePhone) {
                showToastShort("手机号已被注册，请更换手机号注册，或点击忘记密码");
            }
            SMSSDK.getVerificationCode("86", phoneText.getText().toString());
            getProgressDialog().show();
        } else if (v == nextStep) {
            if (StringUtils.isEmpty(phoneText.getText().toString().trim()) || phoneText.getText().toString().length() != 11) {
                showToastShort("请输入正确的手机号码！");
                return;
            }
            if (!isUsePhone) {
                showToastShort("手机号已被注册，请更换手机号注册，或点击忘记密码");
            }
            if (StringUtils.isEmpty(pwdText.getText().toString().trim())) {
                showToastShort("请输入正确的密码！");
                return;
            }
            if (StringUtils.isEmpty(codeText.getText().toString().trim())) {
                showToastShort("请输入正确的验证码！");
                return;
            }
            SMSSDK.submitVerificationCode("86", phoneText.getText().toString(), codeText.getText().toString());
            getProgressDialog().show();
        }
    }

    @Override
    protected void doResponse(Object response) {
        isUsePhone = true;
        showToastShort("当前手机号可用");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        isUsePhone = false;
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
                    UserDetailInfo userDetailInfo = new UserDetailInfo();
                    userDetailInfo.setPhone(phoneText.getText().toString());
                    userDetailInfo.setPassword(pwdText.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userDetailInfo", userDetailInfo);
                    toActivity(QRegistInfoActivity.class, bundle);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    getProgressDialog().dismiss();
                    showToastShort("验证码已经发送");
                    TimeCount time = TimeCount.getInstance(60000, 1000, sendSms, QRegistActivity.this);
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
