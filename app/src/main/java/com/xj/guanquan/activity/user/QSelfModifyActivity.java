package com.xj.guanquan.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.roast.SelectPicActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.KeyValue;
import com.xj.guanquan.model.UserDetailInfo;
import com.xj.guanquan.model.UserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QSelfModifyActivity extends QBaseActivity implements View.OnClickListener {
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    private Request request;
    private StringRequest requestDetail;
    private MultipartRequest requestSave;
    private UserInfo userInfo;
    private TextView username;
    private ImageView usernameBtn;
    private TextView orderid;
    private ImageView orderidBtn;
    private SimpleDraweeView headimage;
    private ImageView headimageBtn;
    private RelativeLayout headImageArea;
    private EditText nickname;
    private ImageView nicknameBtn;
    private RelativeLayout nickNameArea;
    private TextView marriage;
    private ImageView sexBtn;
    private RelativeLayout marriageArea;
    private TextView constellatory;
    private ImageView constellatoryBtn;
    private RelativeLayout constellatoryArea;
    private EditText hobby;
    private ImageView hobbyBtn;
    private RelativeLayout hobbyArea;
    private TextView height;
    private ImageView heightBtn;
    private RelativeLayout heightArea;
    private TextView weight;
    private ImageView weightBtn;
    private RelativeLayout weightArea;
    private TextView income;
    private ImageView incomeBtn;
    private RelativeLayout incomeArea;
    private EditText selfsign;
    private ImageView selfsignBtn;
    private RelativeLayout signArea;
    private TextView jobText;
    private RelativeLayout jobArea;

    private AlertDialog selectDialog;
    private NumberPicker selectPicker;
    private List<KeyValue> valueList;
    private KeyValue starValue;
    private UserDetailInfo userDetailInfo;
    private JSONObject content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eself_data);
        initialize();
    }

    @Override
    protected void initView() {
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        _setHeaderTitle("修改个人信息");
        _setRightHomeGone();
        _setRightHomeText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svaeDetail();
            }
        });
    }

    @Override
    protected void initHandler() {
        requestDetail = new StringRequest(Request.Method.POST, ApiList.USER_DETAIL, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QSelfModifyActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", String.valueOf(userInfo.getUserId()));
                map.put("lng", PreferencesUtils.getString(QSelfModifyActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QSelfModifyActivity.this, "lat"));
                return map;
            }
        };

        request = requestDetail;
        addToRequestQueue(request, ApiList.USER_DETAIL, true);
    }


    @Override
    public void onClick(View v) {
        if (v == headImageArea) {
            Intent intent = new Intent(this, SelectPicActivity.class);
            startActivityForResult(intent, TO_SELECT_PHOTO);
        } else if (v == jobArea) {
            initSelectPicker("job", jobText);
            selectPicker.setValue(getIndexByValue("job", content.getString("job")));
            initAlertDialog("请选择工作", selectPicker, jobText);
        } else if (v == marriageArea) {
            initSelectPicker("feelingStatus", marriage);
            selectPicker.setValue(getIndexByValue("feelingStatus", content.getString("feelingStatus")));
            initAlertDialog("请选择婚姻状态", selectPicker, marriage);
        } else if (v == constellatoryArea) {
            initSelectPicker("constellation", constellatory);
            selectPicker.setValue(getIndexByValue("constellation", content.getString("constellation")));
            initAlertDialog("请选择星座", selectPicker, constellatory);
        } else if (v == heightArea) {
            initSelectPicker("height", height);
            selectPicker.setValue(getIndexByValue("height", content.getString("height")));
            initAlertDialog("请选择身高", selectPicker, height);
        } else if (v == weightArea) {
            initSelectPicker("weight", weight);
            selectPicker.setValue(getIndexByValue("weight", content.getString("weight")));
            initAlertDialog("请选择体重", selectPicker, weight);
        } else if (v == incomeArea) {
            initSelectPicker("income", income);
            selectPicker.setValue(getIndexByValue("income", content.getString("income")));
            initAlertDialog("请选择收入", selectPicker, income);
        }
    }

    private void initialize() {
        username = (TextView) findViewById(R.id.username);
        usernameBtn = (ImageView) findViewById(R.id.usernameBtn);
        orderid = (TextView) findViewById(R.id.orderid);
        orderidBtn = (ImageView) findViewById(R.id.orderidBtn);
        headimage = (SimpleDraweeView) findViewById(R.id.headimage);
        headimageBtn = (ImageView) findViewById(R.id.headimageBtn);
        headImageArea = (RelativeLayout) findViewById(R.id.headImageArea);
        nickname = (EditText) findViewById(R.id.nickname);
        nicknameBtn = (ImageView) findViewById(R.id.nicknameBtn);
        nickNameArea = (RelativeLayout) findViewById(R.id.nickNameArea);
        marriage = (TextView) findViewById(R.id.marriage);
        sexBtn = (ImageView) findViewById(R.id.sexBtn);
        marriageArea = (RelativeLayout) findViewById(R.id.marriageArea);
        constellatory = (TextView) findViewById(R.id.constellatory);
        constellatoryBtn = (ImageView) findViewById(R.id.constellatoryBtn);
        constellatoryArea = (RelativeLayout) findViewById(R.id.constellatoryArea);
        hobby = (EditText) findViewById(R.id.hobby);
        hobbyBtn = (ImageView) findViewById(R.id.hobbyBtn);
        hobbyArea = (RelativeLayout) findViewById(R.id.hobbyArea);
        height = (TextView) findViewById(R.id.height);
        heightBtn = (ImageView) findViewById(R.id.heightBtn);
        heightArea = (RelativeLayout) findViewById(R.id.heightArea);
        weight = (TextView) findViewById(R.id.weight);
        weightBtn = (ImageView) findViewById(R.id.weightBtn);
        weightArea = (RelativeLayout) findViewById(R.id.weightArea);
        income = (TextView) findViewById(R.id.income);
        incomeBtn = (ImageView) findViewById(R.id.incomeBtn);
        incomeArea = (RelativeLayout) findViewById(R.id.incomeArea);
        selfsign = (EditText) findViewById(R.id.selfsign);
        selfsignBtn = (ImageView) findViewById(R.id.selfsignBtn);
        signArea = (RelativeLayout) findViewById(R.id.signArea);
        jobText = (TextView) findViewById(R.id.jobText);
        jobArea = (RelativeLayout) findViewById(R.id.jobArea);
        userDetailInfo = new UserDetailInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            final String picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            final Bitmap bitmap = ImageUtils.getSmallBitmap(picPath, headimage.getWidth(), headimage.getHeight());
            userDetailInfo.setFile_avatar(picPath);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    int bitmapSize = getBitmapSize(bitmap);
                    if (bitmapSize > 300000) {
                        try {
//                            float scale=300000f/bitmapSize;
                            FileOutputStream out = new FileOutputStream(new File(userDetailInfo.getFile_avatar()));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            headimage.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    private void initAlertDialog(String message, View v, final TextView textView) {
        if (selectDialog != null && selectDialog.isShowing()) {
            selectDialog.dismiss();
        }
        selectDialog = new AlertDialog.Builder(this, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert)
                .setMessage(message).setCancelable(true)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(valueList.get(selectPicker.getValue()).getValue());
                        selectDialog.cancel();
                    }
                }).create();
        Window window = selectDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animations);
        selectDialog.show();
    }

    private void initSelectPicker(String type, final TextView textView) {
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
                textView.setText(valueList.get(newVal).getValue());
            }
        });
    }

    private int getIndexByValue(String type, String value) {
        JSONObject content = JSONObject.parseObject(PreferencesUtils.getString(this, "data_dict"));
        List<KeyValue> keyValues = JSONArray.parseArray(content.getJSONArray(type).toJSONString(), KeyValue.class);
        for (int i = 0; i < keyValues.size(); i++) {
            if (StringUtils.isEquals(keyValues.get(i).getValue(), value)) {
                return i;
            }
        }
        return 0;
    }

    private String getKeyByValue(String type, String value) {
        JSONObject content = JSONObject.parseObject(PreferencesUtils.getString(this, "data_dict"));
        List<KeyValue> keyValues = JSONArray.parseArray(content.getJSONArray(type).toJSONString(), KeyValue.class);
        for (int i = 0; i < keyValues.size(); i++) {
            if (StringUtils.isEquals(keyValues.get(i).getValue(), value)) {
                return keyValues.get(i).getKey();
            }
        }
        return null;
    }


    private void svaeDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("lng", PreferencesUtils.getString(this, "lng"));
        params.put("lat", PreferencesUtils.getString(this, "lat"));
        if (!StringUtils.isEmpty(height.getText().toString()))
            params.put("height", getKeyByValue("height", height.getText().toString()));
        if (!StringUtils.isEmpty(weight.getText().toString()))
            params.put("weight", getKeyByValue("weight", weight.getText().toString()));
        if (!StringUtils.isEmpty(income.getText().toString()))
            params.put("income", getKeyByValue("income", income.getText().toString()));
        if (!StringUtils.isEmpty(constellatory.getText().toString()))
            params.put("constellation", getKeyByValue("constellation", constellatory.getText().toString()));
        if (!StringUtils.isEmpty(marriage.getText().toString()))
            params.put("feelingStatus", getKeyByValue("feelingStatus", marriage.getText().toString()));
        if (!StringUtils.isEmpty(jobText.getText().toString()))
            params.put("job", getKeyByValue("job", jobText.getText().toString()));
        if (!StringUtils.isEmpty(nickname.getText().toString()))
            params.put("nickName", nickname.getText().toString());
        if (!StringUtils.isEmpty(selfsign.getText().toString()))
            params.put("signature", selfsign.getText().toString());
        List<String> fileNames = new ArrayList<String>();
        if (!StringUtils.isEmpty(userDetailInfo.getFile_avatar())) {
            fileNames.add("file_avatar");
        }
        List<File> files = new ArrayList<File>();
        if (!StringUtils.isEmpty(userDetailInfo.getFile_avatar())) {
            File file = new File(userDetailInfo.getFile_avatar());
            files.add(file);
        }
        requestSave = new MultipartRequest(ApiList.UPDATEUSERDETAIL, this, this, fileNames, files, params) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = super.getHeaders();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QSelfModifyActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }
        };
        request = requestSave;
        addToRequestQueue(request, ApiList.UPDATEUSERDETAIL, true);
    }

    @Override
    protected void doResponse(Object response) {
        final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(request.getTag().toString(), ApiList.USER_DETAIL)) {
            content = result.getData().getJSONObject("content");
            Uri uri = Uri.parse(content.getString("avatar"));
            headimage.setImageURI(uri);
            nickname.setText(content.getString("nickName"));
            selfsign.setText(content.getString("signature"));
            marriage.setText(content.getString("feelingStatus"));
            constellatory.setText(content.getString("constellation"));
            hobby.setText(content.getString("hobby"));
            height.setText(content.getString("height"));
            weight.setText(content.getString("weight"));
            income.setText(content.getString("income"));
            jobText.setText(content.getString("job"));

        } else if (StringUtils.isEquals(request.getTag().toString(), ApiList.UPDATEUSERDETAIL)) {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("isUpdate", true);
                    setResult(RESULT_OK, intent);
                    QSelfModifyActivity.this.finish();
                }
            });
        }
    }

}
