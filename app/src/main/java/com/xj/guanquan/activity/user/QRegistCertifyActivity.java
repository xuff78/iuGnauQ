package com.xj.guanquan.activity.user;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.roast.SelectPicActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.KeyValue;
import com.xj.guanquan.model.UserDetailInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

public class QRegistCertifyActivity extends QBaseActivity implements View.OnClickListener {
    public static final int TO_SELECT_PHOTO = 3;
    private TextView heightText;
    private LinearLayout heightArea;
    private TextView weightText;
    private LinearLayout weightArea;
    private ImageView photoView;
    private LinearLayout photoArea;
    private Button nextStep;
    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private TextView selectView;
    private UserDetailInfo userDetailInfo;
    private KeyValue keyValue;
    private Map<String, String> params;
    private MultipartRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qregist_certify);
    }

    @Override
    protected void initView() {
        initialize();
        _setHeaderTitle(getString(R.string.title_activity_qregist_certify));
        _setRightHomeGone();
        userDetailInfo = (UserDetailInfo) getIntent().getExtras().getSerializable("userDetailInfo");


    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == heightArea) {
            initSelectPicker("height");
            selectView = heightText;
            initAlertDialog("请选择身高", selectPicker);
            userDetailInfo.setHeight(valueList.get(0).getKey());
        } else if (v == weightArea) {
            initSelectPicker("weight");
            selectView = weightText;
            initAlertDialog("请选择体重", selectPicker);
            userDetailInfo.setWeight(valueList.get(0).getKey());
        } else if (v == nextStep) {
            regist();
        } else if (v == photoArea) {
            Intent intent = new Intent(this, SelectPicActivity.class);
            startActivityForResult(intent, TO_SELECT_PHOTO);
        }
    }

    private void initialize() {
        heightText = (TextView) findViewById(R.id.heightText);
        heightArea = (LinearLayout) findViewById(R.id.heightArea);
        weightText = (TextView) findViewById(R.id.weightText);
        weightArea = (LinearLayout) findViewById(R.id.weightArea);
        photoView = (ImageView) findViewById(R.id.photoView);
        photoArea = (LinearLayout) findViewById(R.id.photoArea);
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
                if (type.equals("height")) {
                    userDetailInfo.setHeight(valueList.get(newVal).getKey());
                } else if (type.equals("weight")) {
                    userDetailInfo.setWeight(valueList.get(newVal).getKey());
                }
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
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(userDetailInfo.getFile_avatar());
        List<File> files = new ArrayList<File>();
        if (userDetailInfo.getFile_beautyCert() != null) {
            fileNames.add(userDetailInfo.getFile_beautyCert());
            File cert = new File(userDetailInfo.getFile_beautyCert());
            files.add(file);
            files.add(cert);
        }
        if (fileNames.size() > 1) {
            request = new MultipartRequest(ApiList.ACCOUNT_REGIST, this, this, fileNames, files, params);
        } else {
            request = new MultipartRequest(ApiList.ACCOUNT_REGIST, this, this, "file_avatar", file, params);
        }

        addToRequestQueue(request, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            String picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Bitmap bitmap = ImageUtils.getSmallBitmap(picPath);
            photoView.setImageBitmap(bitmap);
            userDetailInfo.setFile_beautyCert(picPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void doResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        alertDialog(result.getMsg(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRegistCertifyActivity.this, QLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 加入此标志后，intent中的参数被清空。
                startActivity(intent);
            }
        });
    }
}
