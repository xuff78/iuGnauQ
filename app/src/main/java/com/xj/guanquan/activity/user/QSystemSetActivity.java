package com.xj.guanquan.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgSetActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseApplication;

import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

public class QSystemSetActivity extends QBaseActivity implements View.OnClickListener {
    private RelativeLayout notifySet;
    private RelativeLayout invisibleSet;
    private RelativeLayout pwdSet;
    private RelativeLayout blackListSet;
    private ScrollView scrollView;
    private RelativeLayout score;
    private RelativeLayout clearCache;
    private RelativeLayout feedBackSet;
    private Button logOut;
    private RelativeLayout userProtocol;

    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsystem_set);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qsystem_set));
        _setRightHomeGone();
        initialize();

        notifySet.setOnClickListener(this);
        pwdSet.setOnClickListener(this);
        invisibleSet.setOnClickListener(this);
        blackListSet.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        feedBackSet.setOnClickListener(this);
        score.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.ACCOUNT_LOGINOUT, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QSystemSetActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("lng", PreferencesUtils.getString(QSystemSetActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QSystemSetActivity.this, "lat"));
                return map;
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == notifySet) {
            toActivity(QMsgSetActivity.class);
        } else if (v == pwdSet) {
            toActivity(QPwdSetActivity.class);
        } else if (v == invisibleSet) {
            toActivity(QInvisibleSetActivity.class);
        } else if (v == blackListSet) {
            toActivity(QBlackListActivity.class);
        } else if (v == clearCache) {
            showToastShort("缓存清理成功!");
        } else if (v == feedBackSet) {
            toActivity(QFeedbackActivity.class);
        } else if (v == score) {
        } else if (v == logOut) {
            alertConfirmDialog("确定要退出当前账号吗？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToRequestQueue(request, true);
                }
            }, null);
        }
    }

    private void initialize() {
        notifySet = (RelativeLayout) findViewById(R.id.notifySet);
        invisibleSet = (RelativeLayout) findViewById(R.id.invisibleSet);
        pwdSet = (RelativeLayout) findViewById(R.id.pwdSet);
        blackListSet = (RelativeLayout) findViewById(R.id.blackListSet);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        score = (RelativeLayout) findViewById(R.id.score);
        clearCache = (RelativeLayout) findViewById(R.id.clearCache);
        feedBackSet = (RelativeLayout) findViewById(R.id.feedBackSet);
        logOut = (Button) findViewById(R.id.logOut);
        userProtocol = (RelativeLayout) findViewById(R.id.userProtocol);
    }

    @Override
    protected void doResponse(Object response) {
        PreferencesUtils.putString(this, "loginData", null);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isStart", true);
        Intent intent = new Intent(this, QLoginActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 加入此标志后，intent中的参数被清空。
        startActivity(intent);
        ((QBaseApplication) getApplication()).finishAllActivity();
    }
}
