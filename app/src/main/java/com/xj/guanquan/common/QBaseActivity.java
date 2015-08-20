package com.xj.guanquan.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.guanquan.R;


/**
 * 应用界面
 *
 * @author jixiangxiang@infohold.com.cn
 */

public abstract class QBaseActivity extends AppCompatActivity {

    public ImageButton btnHome, btnBack;

    public Button rightTxtBtn;

    protected boolean needRefresh = true;

    public boolean flag;

    private ClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        TextView tv = (TextView) findViewById(R.id.of_header_title_tv);
        tv.setVisibility(View.VISIBLE);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && flag) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
     * 带参数跳转到某一个页面
     *
     * @param cls
     * @param bundle
     */
    public void toAcitivity(Class cls, Bundle bundle) {
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
}
