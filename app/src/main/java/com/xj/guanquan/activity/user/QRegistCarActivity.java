package com.xj.guanquan.activity.user;

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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xj.guanquan.R;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.KeyValue;
import com.xj.guanquan.model.UserDetailInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QRegistCarActivity extends QBaseActivity implements View.OnClickListener {
    private UserDetailInfo userDetailInfo;
    private Button hasCarBtn;
    private Button noCarBtn;
    private Button brandBtn;
    private TextView brandText;
    private LinearLayout brandArea;
    private Button modelBtn;
    private TextView modelText;
    private LinearLayout modelArea;
    private Button configurationBtn;
    private TextView configurationText;
    private LinearLayout configurationArea;
    private Button nextStep;

    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private TextView selectView;

    private MultipartRequest request;
    private Map<String, String> params;
    private KeyValue keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qregist_car);
    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderTitle(getString(R.string.title_activity_qregist_car));
        _setRightHomeGone();
        userDetailInfo = (UserDetailInfo) getIntent().getExtras().getSerializable("userDetailInfo");

        hasCarBtn.setSelected(true);
        userDetailInfo.setHaveCar(1);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == brandArea) {
            selectView = brandText;
            initSelectPicker("brand");
            initAlertDialog("请选择品牌：", selectPicker);
        } else if (v == modelArea) {
            selectView = modelText;
            initSelectPicker("model");
            initAlertDialog("请选择型号：", selectPicker);
        } else if (v == configurationArea) {
            selectView = configurationText;
            initSelectPicker("configuration");
            initAlertDialog("请选择配置：", selectPicker);
        } else if (v == nextStep) {
            if (!StringUtils.isEquals("暂无", brandText.getText().toString())) {
                userDetailInfo.setBrand(brandText.getText().toString());
            }
            if (!StringUtils.isEquals("暂无", modelText.getText().toString())) {
                userDetailInfo.setBrand(modelText.getText().toString());
            }
            if (!StringUtils.isEquals("暂无", configurationText.getText().toString())) {
                userDetailInfo.setBrand(configurationText.getText().toString());
            }
            regist();
        } else if (v == hasCarBtn) {
            if (!hasCarBtn.isSelected()) {
                hasCarBtn.setSelected(!hasCarBtn.isSelected());
                noCarBtn.setSelected(!noCarBtn.isSelected());
                userDetailInfo.setHaveCar(1);
            }
            brandArea.setVisibility(View.VISIBLE);
            modelArea.setVisibility(View.VISIBLE);
            configurationArea.setVisibility(View.VISIBLE);
        } else if (v == noCarBtn) {
            if (!noCarBtn.isSelected()) {
                noCarBtn.setSelected(!noCarBtn.isSelected());
                hasCarBtn.setSelected(!hasCarBtn.isSelected());
                userDetailInfo.setSex(0);
            }
            brandArea.setVisibility(View.GONE);
            modelArea.setVisibility(View.GONE);
            configurationArea.setVisibility(View.GONE);
        }
    }

    private void initialize() {
        hasCarBtn = (Button) findViewById(R.id.hasCarBtn);
        noCarBtn = (Button) findViewById(R.id.noCarBtn);
        brandBtn = (Button) findViewById(R.id.brandBtn);
        brandText = (TextView) findViewById(R.id.brandText);
        brandArea = (LinearLayout) findViewById(R.id.brandArea);
        modelBtn = (Button) findViewById(R.id.modelBtn);
        modelText = (TextView) findViewById(R.id.modelText);
        modelArea = (LinearLayout) findViewById(R.id.modelArea);
        configurationBtn = (Button) findViewById(R.id.configurationBtn);
        configurationText = (TextView) findViewById(R.id.configurationText);
        configurationArea = (LinearLayout) findViewById(R.id.configurationArea);
        nextStep = (Button) findViewById(R.id.nextStep);
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

    private void initSelectPicker(String type) {
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
            }
        });
    }

    private void regist() {
        params = new HashMap<String, String>();
        params.put("phone", userDetailInfo.getPhone());
        params.put("password", userDetailInfo.getPassword());
        params.put("nickName", userDetailInfo.getNickName());
        params.put("sex", String.valueOf(userDetailInfo.getSex()));
        params.put("birthday", userDetailInfo.getBirthday());
        if (userDetailInfo.getHeight() != null)
            params.put("height", userDetailInfo.getHeight());
        if (userDetailInfo.getWeight() != null)
            params.put("weight", userDetailInfo.getWeight());
        if (userDetailInfo.getIncome() != null)
            params.put("income", userDetailInfo.getIncome());
        if (userDetailInfo.getConstellation() != null)
            params.put("constellation", userDetailInfo.getConstellation());
        if (userDetailInfo.getFeelingStatus() != null)
            params.put("feelingStatus", userDetailInfo.getFeelingStatus());
        if (userDetailInfo.getJob() != null)
            params.put("job", userDetailInfo.getJob());
        if (userDetailInfo.getIndustry() != null)
            params.put("industry", userDetailInfo.getIndustry());
        if (userDetailInfo.getHaveCar() != null)
            params.put("haveCar", String.valueOf(userDetailInfo.getHaveCar()));
        if (userDetailInfo.getBrand() != null)
            params.put("brand", userDetailInfo.getBrand());
        if (userDetailInfo.getModel() != null)
            params.put("model", userDetailInfo.getModel());
        if (userDetailInfo.getConfiguration() != null)
            params.put("configuration", userDetailInfo.getConfiguration());
        File file = new File(userDetailInfo.getFile_avatar());
        request = new MultipartRequest(ApiList.ACCOUNT_REGIST, this, this, "file_avatar", file, params);
        addToRequestQueue(request, true);
    }

    @Override
    public void onResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        alertDialog(result.getMsg(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRegistCarActivity.this, QLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 加入此标志后，intent中的参数被清空。
                startActivity(intent);
            }
        });
    }
}
