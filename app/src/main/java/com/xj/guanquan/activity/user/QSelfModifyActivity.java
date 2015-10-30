package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QSelfModifyActivity extends QBaseActivity implements View.OnClickListener {
    private StringRequest request;
    private StringRequest requestDetail;
    private StringRequest requestSave;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eself_data);
    }

    @Override
    protected void initView() {
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        _setHeaderTitle("修改个人信息");
        _setRightHomeGone();
        _setRightHomeText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initHandler() {
        requestDetail = new StringRequest(Request.Method.POST, ApiList.USER_DETAIL, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QSelfModifyActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", String.valueOf(userInfo.getUserId()));
                map.put("lng", PreferencesUtils.getString(QSelfModifyActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QSelfModifyActivity.this, "lat"));
                return map;
            }
        };

        requestSave = new StringRequest(Request.Method.POST, ApiList.UPDATEUSERDETAIL, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QSelfModifyActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", String.valueOf(userInfo.getUserId()));
                map.put("lng", PreferencesUtils.getString(QSelfModifyActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QSelfModifyActivity.this, "lat"));
                return map;
            }
        };
    }


    @Override
    protected void doResponse(Object response) {
        if (StringUtils.isEquals(request.getTag().toString(), ApiList.USER_DETAIL)) {

        } else if (StringUtils.isEquals(request.getTag().toString(), ApiList.UPDATEUSERDETAIL)) {

        }
    }

    @Override
    public void onClick(View v) {

    }
}
