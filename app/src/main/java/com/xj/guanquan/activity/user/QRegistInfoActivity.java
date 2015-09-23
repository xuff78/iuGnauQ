package com.xj.guanquan.activity.user;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.roast.SelectPicActivity;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.KeyValue;
import com.xj.guanquan.model.UserDetailInfo;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QRegistInfoActivity extends QBaseActivity implements View.OnClickListener {
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    private SimpleDraweeView headImage;
    private EditText nickNameText;
    private Button maleButton;
    private Button femaleButton;
    private TextView birthdayText;
    private TextView starText;
    private Button nextStep;

    private DatePickerDialog dpd = null;
    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private UserDetailInfo userDetailInfo;
    private KeyValue starValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_qregist_info);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qregist_info));
        _setRightHomeGone();
        initialize();
        userDetailInfo = (UserDetailInfo) getIntent().getSerializableExtra("userDetailInfo");

        maleButton.setSelected(true);
        maleButton.setOnClickListener(this);
        femaleButton.setOnClickListener(this);
        headImage.setOnClickListener(this);
        birthdayText.setOnClickListener(this);
        starText.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        userDetailInfo.setSex(1);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == maleButton) {
            if (!maleButton.isSelected()) {
                maleButton.setSelected(!maleButton.isSelected());
                femaleButton.setSelected(!femaleButton.isSelected());
                userDetailInfo.setSex(1);
            }
        } else if (v == femaleButton) {
            if (!femaleButton.isSelected()) {
                maleButton.setSelected(!maleButton.isSelected());
                femaleButton.setSelected(!femaleButton.isSelected());
                userDetailInfo.setSex(2);
            }
        } else if (v == headImage) {
            Intent intent = new Intent(this, SelectPicActivity.class);
            startActivityForResult(intent, TO_SELECT_PHOTO);
        } else if (v == birthdayText) {
            initTimePicker();
            dpd.show();
        } else if (v == starText) {
            initSelectPicker("constellation");
            selectPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    starText.setText(valueList.get(newVal).getValue());
                    starValue = valueList.get(newVal);
                }
            });
            if (starValue == null)
                starValue = valueList.get(0);
            initAlertDialog("请选择星座", selectPicker);
        } else if (v == nextStep) {
            if (StringUtils.isEmpty(nickNameText.getText().toString().trim())) {
                showToastShort("请输入正确的昵称！");
                return;
            }
            if (StringUtils.isEmpty(birthdayText.getText().toString().trim())) {
                showToastShort("请选择生日！");
                return;
            }
            if (StringUtils.isEmpty(starText.getText().toString().trim())) {
                showToastShort("请选择星座！");
                return;
            }
            if (StringUtils.isEmpty(userDetailInfo.getFile_avatar())) {
                showToastShort("请选择头像！");
                return;
            }
            userDetailInfo.setNickName(nickNameText.getText().toString());
            userDetailInfo.setConstellation(starValue == null ? null : starValue.getKey());
            userDetailInfo.setBirthday(birthdayText.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putSerializable("userDetailInfo", userDetailInfo);
            if (maleButton.isSelected()) {
                toActivity(QRegistIndustryActivity.class, bundle);
            } else {
                toActivity(QRegistCertifyActivity.class, bundle);
            }

        }
    }

    private void initialize() {

        headImage = (SimpleDraweeView) findViewById(R.id.avatar);
        nickNameText = (EditText) findViewById(R.id.nickNameText);
        maleButton = (Button) findViewById(R.id.maleButton);
        femaleButton = (Button) findViewById(R.id.femaleButton);
        birthdayText = (TextView) findViewById(R.id.birthdayText);
        starText = (TextView) findViewById(R.id.starText);
        nextStep = (Button) findViewById(R.id.nextStep);
    }

    void initTimePicker() {
        DatePickerDialog.OnDateSetListener otsl = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayText.setText(+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                dpd.dismiss();
            }
        };
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(this, otsl, year, month, day);
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
                        starText.setText(starValue.getValue());
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            String picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Bitmap bitmap = ImageUtils.getSmallBitmap(picPath);
            headImage.setImageBitmap(bitmap);
            userDetailInfo.setFile_avatar(picPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
