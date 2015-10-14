package com.xj.guanquan.activity.user;

import android.os.Bundle;
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

public class QPwdSetActivity extends QBaseActivity implements OnClickListener {

    private EditText oldPwd;
    private EditText newPwd;
    private EditText confirmPwd;
    private Button confirmbtn;

    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpwd_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qpwd_set));
        _setRightHomeGone();
        initialize();
        confirmbtn.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.UPDATEPWD, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QPwdSetActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("password", newPwd.getText().toString());
                map.put("oldPassword", oldPwd.getText().toString());
                map.put("lng", PreferencesUtils.getString(QPwdSetActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QPwdSetActivity.this, "lat"));
                return map;
            }
        };
    }

    private void initialize() {
        oldPwd = (EditText) findViewById(R.id.oldPwd);
        newPwd = (EditText) findViewById(R.id.newPwd);
        confirmPwd = (EditText) findViewById(R.id.confirmPwd);
        confirmbtn = (Button) findViewById(R.id.confirm_btn);
    }

    @Override
    public void onClick(View v) {
        if (v == confirmbtn) {
            if (StringUtils.isEmpty(oldPwd.getText().toString())) {
                showToastShort("请输入原始密码！");
                return;
            }
            if (StringUtils.isEmpty(newPwd.getText().toString())) {
                showToastShort("请输入新密码！");
                return;
            }
            if (StringUtils.isEmpty(confirmPwd.getText().toString())) {
                showToastShort("请输入确认密码");
                return;
            }
            if (!StringUtils.isEquals(newPwd.getText().toString(), confirmPwd.getText().toString())) {
                showToastShort("两次密码输入不一致！");
                return;
            }
            addToRequestQueue(request, true);
        }
    }

    @Override
    protected void doResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            alertDialog("密码修改成功！", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    QPwdSetActivity.this.finish();
                }
            });
        } else {
            alertDialog(result.getMsg(), null);
        }
    }
}
