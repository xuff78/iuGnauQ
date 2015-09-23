package com.xj.guanquan.activity.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.CircleInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QCreateGroupThreeActivity extends QBaseActivity implements View.OnClickListener {
    private CircleInfo circleInfo;
    private SimpleDraweeView groupImage;
    private LinearLayout addGroupIcon;
    private EditText groupDesc;

    private MultipartRequest request;
    private Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcreate_three_group);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcreate_group));
        _setRightHomeGone();
        initialize();
        circleInfo = (CircleInfo) getIntent().getExtras().getSerializable("circleInfo");

        _setRightHomeText("提交审核", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(groupDesc.getText().toString())) {
                    showToastShort("请填写群组描述！");
                    return;
                }
                circleInfo.setDescription(groupDesc.getText().toString());
                createGroup();
            }
        });
        Bitmap bmp = BitmapFactory.decodeFile(circleInfo.getFile_logo());
        groupImage.setImageBitmap(bmp);
    }

    private void createGroup() {
        params = new HashMap<String, String>();
        params.put("name", circleInfo.getName());
        params.put("description", circleInfo.getDescription());
        params.put("address", "北京");
        params.put("lng", PreferencesUtils.getString(QCreateGroupThreeActivity.this, "lng"));
        params.put("lat", PreferencesUtils.getString(QCreateGroupThreeActivity.this, "lat"));
        List<File> fileList = new ArrayList<File>();
        List<String> fileNames = new ArrayList<String>();
        File file1 = new File(circleInfo.getFile_logo());
        File file2 = new File(circleInfo.getFile_logo());
        fileList.add(file1);
        fileList.add(file2);
        fileNames.add("file_logo");
        fileNames.add("file_pic");
        request = new MultipartRequest(ApiList.GROUP_CREATE, this, this, fileNames, fileList, params) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = super.getHeaders();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QCreateGroupThreeActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }
        };
        addToRequestQueue(request, true);
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    private void initialize() {
        groupImage = (SimpleDraweeView) findViewById(R.id.groupImage);
        addGroupIcon = (LinearLayout) findViewById(R.id.addGroupIcon);
        groupDesc = (EditText) findViewById(R.id.groupDesc);
    }

    @Override
    protected void doResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        alertDialog(result.getMsg(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QCreateGroupThreeActivity.this, QHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 加入此标志后，intent中的参数被清空。
                startActivity(intent);
            }
        });
    }
}
