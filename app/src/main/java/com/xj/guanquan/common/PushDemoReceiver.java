package com.xj.guanquan.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.home.QStartActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.common.EBaseApplication;
import common.eric.com.ebaselibrary.util.PreferencesUtils;

public class PushDemoReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);

                    Log.d("GetuiSdkDemo", "receiver payload : " + data);
                    String message=null;
                    int type=-1;
                    try {
                        org.json.JSONObject json=new org.json.JSONObject(data);
                        type=json.getInt("type");
                        message=json.getJSONObject("data").getString("message");
                    }catch(Exception e){
                        Log.d("GetuiSdkDemo", "json failure");
                    }
                    if(message!=null) {
                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,
                                new Intent(context, QStartActivity.class), 0);
                        // 通过Notification.Builder来创建通知，注意API Level
                        // API11之后才支持
                        Notification notify2 = new Notification.Builder(context)
                                .setSmallIcon(R.mipmap.logo)
                                .setTicker(message)// 设置在status
                                .setContentTitle("贵圈")
                                .setContentText(message)// TextView中显示的详细内容
//                                .setContentIntent(pendingIntent2) // 关联PendingIntent
                                .getNotification(); // 需要注意build()是在API level
                        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                        if(!isTopActivy(context.getPackageName(), context))
                            notify2.contentIntent=pendingIntent2;
                        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
                        manager.notify(1, notify2);
                    }
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                final String cid = bundle.getString("clientid");
                PreferencesUtils.putString(context, Constant.GeTuiCid, cid);
                bindId(context, cid);
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    private void bindId(final Context con, final String clientId) {
        String method = ApiList.PUSH_Bind;
        StringRequest request = new StringRequest(Request.Method.POST, method, response, onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(con, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("clientId", clientId);
                map.put("lng", PreferencesUtils.getString(con, "lng"));
                map.put("lat", PreferencesUtils.getString(con, "lat"));
                return map;
            }
        };

        Log.i("PushGT", "cid: " + clientId);
        request.setTag(method);
        EBaseApplication.getInstance().getRequestQueue().add(request);
    }

    Response.Listener response = new Response.Listener() {

        @Override
        public void onResponse(Object response) {
            final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
            Log.i("PushGT", "response: " + response.toString());
        }
    };

    Response.ErrorListener onError = new Response.ErrorListener() {


        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    public boolean isTopActivy(String cmdName, Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if(null != runningTaskInfos){
            cmpNameTemp=(runningTaskInfos.get(0).topActivity).toString();
        }
        if(null == cmpNameTemp)return false;
        return cmpNameTemp.equals(cmdName);
    }
}
