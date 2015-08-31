package com.xj.guanquan.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QRegistInfoActivity extends QBaseActivity implements View.OnClickListener {

    private SimpleDraweeView headImage;
    private EditText nickNameText;
    private Button maleButton;
    private Button femaleButton;
    private EditText birthdayText;
    private EditText starText;
    private Button nextStep;

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

        maleButton.setSelected(true);
        maleButton.setOnClickListener(this);
        femaleButton.setOnClickListener(this);

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
            }
        } else if (v == femaleButton) {
            if (!femaleButton.isSelected()) {
                maleButton.setSelected(!maleButton.isSelected());
                femaleButton.setSelected(!femaleButton.isSelected());
            }
        }
    }

    private void initialize() {

        headImage = (SimpleDraweeView) findViewById(R.id.avatar);
        nickNameText = (EditText) findViewById(R.id.nickNameText);
        maleButton = (Button) findViewById(R.id.maleButton);
        femaleButton = (Button) findViewById(R.id.femaleButton);
        birthdayText = (EditText) findViewById(R.id.birthdayText);
        starText = (EditText) findViewById(R.id.starText);
        nextStep = (Button) findViewById(R.id.nextStep);
    }
}
