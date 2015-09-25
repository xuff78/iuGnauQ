package com.xj.guanquan.activity.user;

import android.os.Bundle;
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
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserDetailInfo;

import java.util.HashMap;
import java.util.Map;

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
            showToastShort("目前测试阶段，验证码为123456");
            codeText.setText("123456");
        } else if (v == nextStep) {
            if (StringUtils.isEmpty(phoneText.getText().toString().trim()) || phoneText.getText().toString().length() != 11 || !isUsePhone) {
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
}
