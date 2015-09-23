package com.xj.guanquan.activity.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.CircleInfo;

public class QCreateGroupTwoActivity extends QBaseActivity implements View.OnClickListener {
    private CircleInfo circleInfo;
    private SimpleDraweeView groupImage;
    private LinearLayout addGroupIcon;
    private TextView village;
    private TextView merchantHourse;
    private TextView school;
    private ImageView createGroup;
    private RelativeLayout selectGroupAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcreate_two_group);

    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcreate_group));
        _setRightHomeGone();
        initialize();
        circleInfo = (CircleInfo) getIntent().getExtras().getSerializable("circleInfo");

        _setRightHomeText("下一步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("circleInfo", circleInfo);
                toActivity(QCreateGroupThreeActivity.class, bundle);
                QCreateGroupTwoActivity.this.finish();
            }
        });

        Bitmap bmp = ImageUtils.getSmallBitmap(circleInfo.getFile_logo());
        groupImage.setImageBitmap(bmp);
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
        village = (TextView) findViewById(R.id.village);
        merchantHourse = (TextView) findViewById(R.id.merchantHourse);
        school = (TextView) findViewById(R.id.school);
        createGroup = (ImageView) findViewById(R.id.createGroup);
        selectGroupAddress = (RelativeLayout) findViewById(R.id.selectGroupAddress);
    }
}
