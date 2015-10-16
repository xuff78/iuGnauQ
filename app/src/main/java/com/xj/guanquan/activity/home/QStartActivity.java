package com.xj.guanquan.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.igexin.sdk.PushManager;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.user.QLoginActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.LocationService;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;

import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QStartActivity extends QBaseActivity {
    private StringRequest request;
    private StringRequest requestLogin;
    private JSONObject userInfoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qstart);
    }

    @Override
    protected void initView() {
        _setHeaderGone();
        startLocationService();
    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.GET_ALL, this, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToRequestQueue(request, ApiList.GET_ALL, false);
            }
        }, 1000);

        requestLogin = new StringRequest(Request.Method.POST, ApiList.ACCOUNT_AUTO_LOGIN, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QStartActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("lng", PreferencesUtils.getString(QStartActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QStartActivity.this, "lat"));
                return map;
            }
        };
    }

    private boolean autoLogin() {
        String loginData = PreferencesUtils.getString(this, "loginData");
        if (StringUtils.isEmpty(loginData)) {
            return false;
        } else {
            JSONObject loginJson = JSONObject.parseObject(loginData).getJSONObject("data");
            if (StringUtils.isEmpty(loginJson.getString("authToken"))) {
                return false;
            } else {
                userInfoJson = loginJson;
                return true;
            }
        }
    }

    private void startLocationService() {

        Intent startLocationServiceIntent = new Intent(QStartActivity.this,
                LocationService.class);
        startService(startLocationServiceIntent);
    }

    @Override
    public void onResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            if (StringUtils.isEquals(requestMethod, ApiList.GET_ALL)) {
                getProgressDialog().dismiss();
                PreferencesUtils.putString(this, "data_dict", result.getData().getJSONObject("content").toJSONString());
                if (autoLogin()) {
                    addToRequestQueue(requestLogin, ApiList.ACCOUNT_AUTO_LOGIN, true);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isStart", true);
                    toActivity(QLoginActivity.class, bundle);
                    QStartActivity.this.finish();
                }
            } else if (StringUtils.isEquals(requestMethod, ApiList.ACCOUNT_AUTO_LOGIN)) {
                PushManager.getInstance().initialize(this.getApplicationContext());
                EMChatManager.getInstance().login(userInfoJson.getString("huanxinName"), userInfoJson.getString("huanxinPassword"), new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        getProgressDialog().dismiss();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                toActivity(QHomeActivity.class);
                                QStartActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getProgressDialog().dismiss();
                                alertDialog("登陆聊天服务器失败！错误信息：" + message, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("isStart", true);
                                        toActivity(QLoginActivity.class, bundle);
                                        QStartActivity.this.finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        } else if (StringUtils.isEquals(result.getCode(), "4100")) {
            getProgressDialog().dismiss();
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isStart", true);
                    toActivity(QLoginActivity.class, bundle);
                    QStartActivity.this.finish();
                }
            });
        } else {
            getProgressDialog().dismiss();
            alertDialog(result.getMsg(), null);
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
        alertDialog(error.toString(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isStart", true);
                toActivity(QLoginActivity.class, bundle);
                QStartActivity.this.finish();
            }
        });

    }
}
