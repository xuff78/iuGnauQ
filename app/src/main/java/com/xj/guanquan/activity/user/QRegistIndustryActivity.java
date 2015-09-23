package com.xj.guanquan.activity.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.KeyValue;
import com.xj.guanquan.model.UserDetailInfo;

import java.util.List;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

public class QRegistIndustryActivity extends QBaseActivity implements View.OnClickListener {


    private Button proBtn;
    private TextView proText;
    private LinearLayout proArea;
    private Button industryBtn;
    private TextView industryText;
    private LinearLayout industryArea;
    private Button nextStep;

    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private TextView selectView;
    private UserDetailInfo userDetailInfo;
    private KeyValue keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qregist_industry);
    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderTitle(getString(R.string.title_activity_qregist_industry));
        _setRightHomeGone();
        userDetailInfo = (UserDetailInfo) getIntent().getExtras().getSerializable("userDetailInfo");
    }

    @Override
    protected void initHandler() {

    }

    private void initialize() {
        proBtn = (Button) findViewById(R.id.proBtn);
        proText = (TextView) findViewById(R.id.proText);
        proArea = (LinearLayout) findViewById(R.id.proArea);
        industryBtn = (Button) findViewById(R.id.industryBtn);
        industryText = (TextView) findViewById(R.id.industryText);
        industryArea = (LinearLayout) findViewById(R.id.industryArea);
        nextStep = (Button) findViewById(R.id.nextStep);
    }

    @Override
    public void onClick(View v) {
        if (v == proArea) {
            initSelectPicker("job");
            selectView = proText;
            initAlertDialog("请选择职业类别", selectPicker);
            userDetailInfo.setJob(valueList.get(0).getKey());
        } else if (v == industryArea) {
            initSelectPicker("industry");
            selectView = industryText;
            initAlertDialog("请选择行业类别", selectPicker);
            userDetailInfo.setIndustry(valueList.get(0).getKey());
        } else if (v == nextStep) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userDetailInfo", userDetailInfo);
            toActivity(QRegistCarActivity.class, bundle);
        }
    }

    private void initAlertDialog(String message, View v) {
        if (selectDialog != null && selectDialog.isShowing()) {
            selectDialog.dismiss();
        }
        selectDialog = new AlertDialog.Builder(this, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert)
                .setMessage(message).setCancelable(true)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectView.setText(keyValue == null ? valueList.get(0).getValue() : keyValue.getValue());
                        selectDialog.cancel();
                    }
                }).create();
        Window window = selectDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animations);
        selectDialog.show();
    }

    private void initSelectPicker(final String type) {
        JSONObject content = JSONObject.parseObject(PreferencesUtils.getString(this, "data_dict"));
        valueList = JSONArray.parseArray(content.getJSONArray(type).toJSONString(), KeyValue.class);
        String[] values = new String[valueList.size()];
        for (int i = 0; i < valueList.size(); i++) {
            values[i] = valueList.get(i).getValue();
        }
        selectPicker = new NumberPicker(this);
        selectPicker.setDisplayedValues(values);
        selectPicker.setMinValue(0);
        selectPicker.setMaxValue(values.length - 1);
        selectPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ((EditText) selectPicker.getChildAt(0)).setInputType(InputType.TYPE_NULL);
        selectPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectView.setText(valueList.get(newVal).getValue());
                keyValue = valueList.get(newVal);
                if (type.equals("industry")) {
                    userDetailInfo.setIndustry(valueList.get(newVal).getKey());
                } else if (type.equals("job")) {
                    userDetailInfo.setJob(valueList.get(newVal).getKey());
                }
            }
        });
    }
}
