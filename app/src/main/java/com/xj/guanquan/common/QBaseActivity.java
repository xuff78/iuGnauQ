package com.xj.guanquan.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.activity.user.QLoginActivity;
import com.xj.guanquan.views.CustomProgressDialog;

import common.eric.com.ebaselibrary.common.EBaseApplication;
import common.eric.com.ebaselibrary.util.StringUtils;


/**
 * 应用界面
 *
 * @author jixiangxiang@infohold.com.cn
 */

public abstract class QBaseActivity extends AppCompatActivity implements QBaseFragment.OnFragmentListener, Response.Listener, Response.ErrorListener {

    public ImageButton btnHome, btnBack;

    public Button rightTxtBtn;

    protected boolean needRefresh = true;

    public boolean flag;

    private ClickListener listener;

    private NiftyDialogBuilder dialogBuilder;

    private CustomProgressDialog progressDialog;

    protected String requestMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.setContentView(R.layout.qbase_activity);
        listener = new ClickListener();
        btnHome = (ImageButton) findViewById(R.id.of_back_home_imagebtn);
        btnHome.setOnClickListener(listener);
        btnBack = (ImageButton) findViewById(R.id.of_back_imagebtn);
        btnBack.setOnClickListener(listener);
        rightTxtBtn = (Button) findViewById(R.id.of_back_right_txtbtn);
        rightTxtBtn.setOnClickListener(listener);
    }

    protected abstract void initView();

    protected abstract void initHandler();

    @Override
    public void setContentView(int layoutResID) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.app_frame_content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);

        frameLayout.addView(content, -1, layoutParams);
        initView();
        initHandler();
    }

    private final class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.of_back_home_imagebtn) {
                //ActUtil.showHome(EBaseActivity.this);
            } else if (id == R.id.of_back_imagebtn) {
                KeyBoardCancle();
                finish();
            }
        }
    }

    protected static final int Menu_ItemId_Exit = 0x80000000;

    protected void _setHeaderTitle(String title) {
        LinearLayout rl = (LinearLayout) findViewById(R.id.app_heder_layout);
        rl.setVisibility(View.VISIBLE);
        TextView tv = (TextView) findViewById(R.id.of_header_title_tv);
        tv.setVisibility(View.VISIBLE);
        tv.setText(title);
    }

    /**
     * 显示导航栏文字标题
     */
    protected void _setHeaderTitle(String title, int color) {
        TextView tv = (TextView) findViewById(R.id.of_header_title_tv);
        tv.setVisibility(View.VISIBLE);
        tv.setTextColor(getResources().getColor(color));
        tv.setText(title);
    }

    protected View _setHeaderGone() {
        LinearLayout rl = (LinearLayout) findViewById(R.id.app_heder_layout);
        rl.setVisibility(View.GONE);
        return rl;
    }

    protected View _setHeaderShown() {
        LinearLayout rl = (LinearLayout) findViewById(R.id.app_heder_layout);
        rl.setVisibility(View.VISIBLE);
        return rl;
    }

    /**
     * 隐藏左边后退按钮
     */
    protected void _setLeftBackGone() {
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_imagebtn);
        btn.setVisibility(View.GONE);
        flag = true;
    }

    protected void _setLeftBackListener(OnClickListener listener) {
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_imagebtn);
        btn.setOnClickListener(listener);
    }

    protected void _setLeftBackListener(int backImage, OnClickListener listener) {
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_imagebtn);
        btn.setVisibility(View.VISIBLE);
        btn.setImageResource(backImage);
        btn.setOnClickListener(listener);
    }

    protected void _setRightHomeListener(OnClickListener listener) {
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_home_imagebtn);
        btn.setOnClickListener(listener);
    }

    protected void _setRightHome(int res, OnClickListener listener) {
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_home_imagebtn);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(listener);
        btn.setImageResource(res);
    }

    protected void _setRightSubmit(OnClickListener listener) {
        findViewById(R.id.of_back_home_imagebtn).setVisibility(View.GONE);
        ImageButton btn = (ImageButton) findViewById(R.id.of_back_right_imagebtn);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(listener);
        flag = true;
    }

    /**
     * 隐藏右边主页按钮
     */
    protected void _setRightHomeGone() {
        btnHome.setVisibility(View.GONE);
        flag = true;
    }

    /**
     * 显示右边主页文字按钮
     */
    protected void _setRightHomeText(String name, OnClickListener listener) {
        rightTxtBtn.setVisibility(View.VISIBLE);
        rightTxtBtn.setText(name);
        flag = false;
        rightTxtBtn.setOnClickListener(listener);
    }

    /**
     * 显示右边主页文字按钮
     */
    protected void _setRightHomeText(String name) {
        rightTxtBtn.setVisibility(View.VISIBLE);
        rightTxtBtn.setText(name);

        flag = false;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // String is_start = SharedPreferencesUtil.getString(this,
        // "is_start");//银行开启5分钟
        // if (is_start.equals("1")) {
        // clazzTime();
        // }
    }

    public void KeyBoardCancle() {

        View view = getWindow().peekDecorView();
        if (view != null) {

            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see com.ih.impl.base.HttpCallback#httpCallback(java.lang.String,
	 * java.lang.String)
	 */

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        super.onPause();
    }

    /**
     * 跳转到某一界面
     *
     * @param cls
     */
    public void toActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 跳转到首页
     */
    public void showHome() {
        Intent intent = new Intent(this, QHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 加入此标志后，intent中的参数被清空。
        startActivity(intent);
    }

    /**
     * 跳转到登录页面
     */
    public void showLogin() {
        Intent intent = new Intent(this, QLoginActivity.class);
        intent.putExtra("fromCode", true);
        startActivityForResult(intent, 999);//为返回是否登录的状态
    }


    /**
     * 带参数跳转到某一个页面
     *
     * @param cls
     * @param bundle
     */
    public void toActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 短时间toast显示
     *
     * @param message
     */
    public void showToastShort(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间toast显示
     *
     * @param message
     */
    public void showToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoad(Fragment frg) {

    }

    /**
     * 弹出确认提示框
     *
     * @param message
     * @param confirmClickListener
     * @param cancelClickListener
     */
    public void alertConfirmDialog(String message, final OnClickListener confirmClickListener, final OnClickListener cancelClickListener) {
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("温馨提示")
                .withDialogColor(getResources().getColor(R.color.view_color))
                .withIcon(R.mipmap.logo)
                .withButton1Text("是")                                      //def gone
                .withButton2Text("否")
                .withDuration(500)
                .withEffect(Effectstype.Fliph);

        dialogBuilder.withMessage(message).setButton1Click(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClickListener.onClick(v);
                dialogBuilder.dismiss();
            }
        });
        if (cancelClickListener != null) {
            dialogBuilder.setButton2Click(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelClickListener.onClick(v);
                    dialogBuilder.dismiss();
                }
            });
        } else {
            dialogBuilder.setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
        }
        dialogBuilder.show();

    }

    /**
     * 弹出信息提示框
     *
     * @param message
     * @param okClickListener
     */
    public void alertDialog(String message, final OnClickListener okClickListener) {
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("温馨提示")
                .withDialogColor(getResources().getColor(R.color.view_color))
                .withIcon(R.mipmap.logo)
                .withButton1Text("确定")                                      //def gone
                .withDuration(500)
                .withEffect(Effectstype.RotateBottom);

        dialogBuilder.withMessage(message).setButton1Click(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okClickListener != null)
                    okClickListener.onClick(v);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();
    }

    /**
     * 弹出信息提示框
     *
     * @param message
     * @param okClickListener
     */
    public void alertDialogNoCancel(String message, final OnClickListener okClickListener) {
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("温馨提示")
                .withDialogColor(getResources().getColor(R.color.view_color))
                .withIcon(R.mipmap.logo)
                .withButton1Text("确定")                                    //def gone
                .withDuration(500)
                .withEffect(Effectstype.RotateBottom);
        dialogBuilder.setCancelable(false);
        dialogBuilder.isCancelableOnTouchOutside(false);
        dialogBuilder.withMessage(message).setButton1Click(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okClickListener != null)
                    okClickListener.onClick(v);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
        alertDialog(error.toString(), null);
    }

    @Override
    public void onResponse(Object response) {
        Log.i("Net", "Response: " + response.toString());
        getProgressDialog().dismiss();

        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            doResponse(response);
        } else
            alertDialog(result.getMsg(), null);
    }

    protected void doResponse(Object response) {
    }

    protected CustomProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressUtil.getProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }

    public <T> void addToRequestQueue(Request<T> req, Boolean isShowDialog) {
        if (!getProgressDialog().isShowing() && isShowDialog)
            getProgressDialog().show();
        req.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        ((EBaseApplication) getApplication()).addToRequestQueue(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, Boolean isShowDialog) {
        if (!getProgressDialog().isShowing() && isShowDialog)
            getProgressDialog().show();
        requestMethod = tag;
        req.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        ((EBaseApplication) getApplication()).addToRequestQueue(req, tag);
    }

    public <T> void addUploadToRequestQueue(Request<T> req, String tag, Boolean isShowDialog) {
        if (!getProgressDialog().isShowing() && isShowDialog)
            getProgressDialog().show();
        requestMethod = tag;
        req.setRetryPolicy(new DefaultRetryPolicy(300 * 1000, 1, 1.0f));
        ((EBaseApplication) getApplication()).addToRequestQueue(req, tag);
    }
}
