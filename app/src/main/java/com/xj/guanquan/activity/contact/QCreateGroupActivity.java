package com.xj.guanquan.activity.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.roast.SelectPicActivity;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.CircleInfo;

import common.eric.com.ebaselibrary.util.StringUtils;

public class QCreateGroupActivity extends QBaseActivity implements View.OnClickListener {
    public static final int TO_SELECT_PHOTO = 3;
    private LinearLayout addGroupIcon;
    private EditText groupName;
    private SimpleDraweeView groupImage;
    private CircleInfo circleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcreate_group);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcreate_group));
        _setRightHomeGone();
        initialize();

        circleInfo = new CircleInfo();
        _setRightHomeText("下一步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(circleInfo.getFile_logo())) {
                    showToastShort("请选择群组！");
                    return;
                }
                if (StringUtils.isEmpty(groupName.getText().toString())) {
                    showToastShort("请输入群组名称！");
                    return;
                }
                circleInfo.setName(groupName.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("circleInfo", circleInfo);
                toActivity(QCreateGroupTwoActivity.class, bundle);
                QCreateGroupActivity.this.finish();
            }
        });
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == addGroupIcon) {
            Intent intent = new Intent(this, SelectPicActivity.class);
            startActivityForResult(intent, TO_SELECT_PHOTO);
        }
    }

    private void initialize() {
        addGroupIcon = (LinearLayout) findViewById(R.id.addGroupIcon);
        groupName = (EditText) findViewById(R.id.groupName);
        groupImage = (SimpleDraweeView) findViewById(R.id.groupImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            String picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Bitmap bmp= ImageUtils.getSmallBitmap(picPath);
//            ViewGroup.LayoutParams layoutParams = groupImage.getLayoutParams();
//            layoutParams.height = addGroupIcon.getHeight();
//            layoutParams.width = addGroupIcon.getWidth();
            groupImage.setImageBitmap(bmp);
            circleInfo.setFile_logo(picPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
