package com.xj.guanquan.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.KeyValue;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QScreenActivity extends QBaseActivity implements View.OnClickListener {

    private TextView allSex;
    private TextView male;
    private TextView female;
    private TextView oneHour;
    private TextView oneDay;
    private TextView moreDay;
    private TextView ageText;
    private RelativeLayout selectAge;
    private TextView heightText;
    private RelativeLayout selectHeight;
    private TextView car;
    private RelativeLayout selectCar;
    private Button confirmBtn;

    private Integer sex;
    private String age;
    private String height;
    private String carCert;
    private Integer finallyTime;

    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private KeyValue keyValue;
    private TextView selectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qscreen);
    }

    @Override
    protected void initView() {
        _setRightHomeGone();
        _setHeaderTitle(getString(R.string.title_activity_qscreen));
        _setLeftBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });
        initialize();
        sex = (Integer) getIntent().getSerializableExtra("sex");
        age = (String) getIntent().getSerializableExtra("age");
        carCert = getIntent().getStringExtra("carCert");
        finallyTime = (Integer) getIntent().getSerializableExtra("finallyTime");
        height = getIntent().getStringExtra("height");
        if (sex == null)
            sex = 0;
        switch (sex.intValue()) {
            case 0:
                allSex.setSelected(true);
                break;
            case 1:
                male.setSelected(true);
                break;
            case 2:
                female.setSelected(true);
                break;
        }
        if (finallyTime == null)
            finallyTime = 0;

        switch (finallyTime.intValue()) {
            case 0:
                oneHour.setSelected(true);
                break;
            case 1:
                oneDay.setSelected(true);
                break;
            case 2:
                moreDay.setSelected(true);
                break;
        }

        allSex.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        oneHour.setOnClickListener(this);
        oneDay.setOnClickListener(this);
        moreDay.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        selectAge.setOnClickListener(this);
        selectCar.setOnClickListener(this);
        selectHeight.setOnClickListener(this);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == allSex) {
            setSelectSexView(v);
        } else if (v == male) {
            setSelectSexView(v);
        } else if (v == female) {
            setSelectSexView(v);
        } else if (v == oneHour) {
            setSelectTimeView(v);
        } else if (v == oneDay) {
            setSelectTimeView(v);
        } else if (v == moreDay) {
            setSelectTimeView(v);
        } else if (v == confirmBtn) {
            backPressed();
        } else if (v == selectAge) {
            selectView = ageText;
            initSelectPicker("age");
            keyValue = valueList.get(0);
            selectPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    keyValue = valueList.get(newVal);
                    ageText.setText(keyValue.getValue());
                }
            });
            initAlertDialog("请选择年龄", selectPicker);
        } else if (v == selectCar) {
            selectView = car;
            initSelectPicker("configuration");
            keyValue = valueList.get(0);
            selectPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    keyValue = valueList.get(newVal);
                    car.setText(keyValue.getValue());
                }
            });
            initAlertDialog("请选择车认证", selectPicker);

        } else if (v == selectHeight) {
            selectView = heightText;
            initSelectPicker("height");
            keyValue = valueList.get(0);
            selectPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    keyValue = valueList.get(newVal);
                    heightText.setText(keyValue.getValue());
                }
            });
            initAlertDialog("请选择身高", selectPicker);
        }
    }

    private void initialize() {
        allSex = (TextView) findViewById(R.id.allSex);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        female = (TextView) findViewById(R.id.female);
        oneHour = (TextView) findViewById(R.id.oneHour);
        oneDay = (TextView) findViewById(R.id.oneDay);
        moreDay = (TextView) findViewById(R.id.moreDay);
        ageText = (TextView) findViewById(R.id.age);
        selectAge = (RelativeLayout) findViewById(R.id.selectAge);
        heightText = (TextView) findViewById(R.id.heightTxt);
        selectHeight = (RelativeLayout) findViewById(R.id.selectHeight);
        car = (TextView) findViewById(R.id.car);
        selectCar = (RelativeLayout) findViewById(R.id.selectCar);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
    }

    private void setSelectSexView(View v) {
        allSex.setSelected(v == allSex);
        male.setSelected(v == male);
        female.setSelected(v == female);
        if (allSex.isSelected()) {
            sex = 0;
        } else if (male.isSelected()) {
            sex = 1;
        } else if (female.isSelected()) {
            sex = 2;
        }
    }

    private void setSelectTimeView(View v) {
        oneHour.setSelected(v == oneHour);
        oneDay.setSelected(v == oneDay);
        moreDay.setSelected(v == moreDay);
        if (oneHour.isSelected()) {
            finallyTime = 0;
        } else if (oneDay.isSelected()) {
            finallyTime = 1;
        } else if (moreDay.isSelected()) {
            finallyTime = 2;
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        Intent intent = new Intent();
        intent.putExtra("sex", sex);
        intent.putExtra("age", StringUtils.isEquals(ageText.getText().toString(), "不限") ? null : age);
        intent.putExtra("height", StringUtils.isEquals(heightText.getText().toString(), "不限") ? null : height);
        intent.putExtra("carCert", StringUtils.isEquals(car.getText().toString(), "不限") ? null : carCert);
        intent.putExtra("finallyTime", finallyTime);
        setResult(111, intent);
        this.finish();
    }

    private void initAlertDialog(final String message, View v) {
        if (selectDialog != null && selectDialog.isShowing()) {
            selectDialog.dismiss();
        }
        selectDialog = new AlertDialog.Builder(this, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert)
                .setMessage(message).setCancelable(true)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectView.setText(keyValue.getValue());
                        if (message.equals("请选择年龄")) {
                            age = keyValue.getKey();
                        } else if (message.equals("请选择车认证")) {
                            carCert = keyValue.getKey();
                        } else if (message.equals("请选择身高")) {
                            height = keyValue.getKey();
                        }
                        selectDialog.cancel();
                    }
                }).create();
        Window window = selectDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animations);
        selectDialog.show();
    }

    private void initSelectPicker(String type) {
        if (type.equals("configuration")) {
            valueList = new ArrayList<KeyValue>();
            valueList.add(new KeyValue("0", "未认证"));
            valueList.add(new KeyValue("1", "已认证"));
        } else {
            JSONObject content = JSONObject.parseObject(PreferencesUtils.getString(this, "data_dict"));
            valueList = JSONArray.parseArray(content.getJSONArray(type).toJSONString(), KeyValue.class);
        }
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
    }
}
