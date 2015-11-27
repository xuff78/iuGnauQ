package com.xj.guanquan.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.SelectPicActivity;
import com.xj.guanquan.activity.roast.ViewPagerExampleActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.views.Photo4Layout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.ScreenUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QEditPhotoActivity extends QBaseActivity {
    public static final int TO_SELECT_PHOTO = 3;
    private LinearLayout photoLayout;
    private NiftyDialogBuilder dialog;
    private String headImgs;
    int width = 0;
    private String[] urls;
    private MultipartRequest uploadRequest;
    private String requestURL;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qedit_photo);
    }

    @Override
    protected void initView() {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        initialize();
        _setHeaderTitle(getString(R.string.title_activity_qedit_photo));
        headImgs = getIntent().getExtras().getString("headImgs");
        if (headImgs.length() > 0) {
            urls = headImgs.split(",");
        }
        initHeadImgs();

    }

    private void initHeadImgs() {
        photoLayout.removeAllViews();
        Photo4Layout photo4Layout = new Photo4Layout(this, (int) (width - ScreenUtils.dpToPxInt(this, 20)), urls);
        photoLayout.addView(photo4Layout);
        photo4Layout.setAddViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QEditPhotoActivity.this, SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
            }
        });
        photo4Layout.setImgCallback(new Photo4Layout.ClickListener() {

            @Override
            public void onClick(View v, final int position) {
                final View view = getLayoutInflater().inflate(R.layout.user_photo_list_dialog_view, null);
                dialog = NiftyDialogBuilder.getInstance(QEditPhotoActivity.this);
                dialog
                        .withTitle("温馨提示")
                        .withMessage("请选择下列操作:")
                        .withDialogColor(getResources().getColor(R.color.view_color))
                        .withIcon(R.mipmap.logo)
                        .withDuration(500)
                        .withEffect(Effectstype.SlideBottom)
                        .setCustomView(view, QEditPhotoActivity.this);
                dialog.show();
                view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        urls = arrContrast(urls, position);
                    }
                });
                view.findViewById(R.id.toLook).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QEditPhotoActivity.this, ViewPagerExampleActivity.class);
                        intent.putExtra("Images", urls);
                        intent.putExtra("pos", position);
                        QEditPhotoActivity.this.startActivity(intent);
                    }
                });
                view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void initHandler() {

    }

    private void initialize() {
        photoLayout = (LinearLayout) findViewById(R.id.photoLayout);
    }

    //处理数组字符
    private static String[] arrContrast(String[] arr, int position) {
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < arr.length; i++) {
            if (i != position) {
                list.add(arr[i]);
            }
        }
        String[] result = {}; //创建空数组
        return list.toArray(result); //List to Array
    }

    private void uploadRequest(String method, final Map<String, String> params, List<File> files, String key) {
        uploadRequest = new MultipartRequest(method, this, this, key, files, params) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = super.getHeaders();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QEditPhotoActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

        };
        addUploadToRequestQueue(uploadRequest, method, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Map<String, String> params = new HashMap<>();
            params.put("lng", PreferencesUtils.getString(this, "lng"));
            params.put("lat", PreferencesUtils.getString(this, "lat"));
            List<File> files = new ArrayList<File>();
            File file = new File(picPath);
            files.add(file);
            requestURL = ApiList.UPDATEAVATAR;
            uploadRequest(requestURL, params, files, "file_avatar");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void doResponse(Object response) {
        final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(requestMethod, ApiList.UPDATEAVATAR)) {
            String[] newUrls = new String[urls.length + 1];
            for (int i = 0; i < urls.length; i++) {
                newUrls[i] = urls[i];
            }
            newUrls[urls.length] = result.getData().getJSONObject("content").getString("url");
            urls = newUrls;
            initHeadImgs();
        }
    }
}
