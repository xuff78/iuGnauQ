package com.xj.guanquan.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

public class QScreenActivity extends QBaseActivity implements View.OnClickListener {

    private TextView allSex;
    private TextView male;
    private TextView female;
    private TextView oneHour;
    private TextView oneDay;
    private TextView moreDay;
    private TextView ageText;
    private ImageView selectAge;
    private TextView heightText;
    private ImageView selectHeight;
    private TextView car;
    private ImageView selectCar;
    private Button confirmBtn;

    private Integer sex;
    private Integer age;
    private String height;
    private Integer carCert;
    private Integer finallyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qscreen);
    }

    @Override
    protected void initView() {
        _setRightHomeGone();
        _setHeaderTitle(getString(R.string.title_activity_qscreen));
        initialize();

        sex = (Integer) getIntent().getSerializableExtra("sex");
        age = (Integer) getIntent().getSerializableExtra("age");
        finallyTime = (Integer) getIntent().getSerializableExtra("finallyTime");
        carCert = (Integer) getIntent().getSerializableExtra("carCert");
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
            Intent intent = new Intent();
            intent.putExtra("sex", sex);
            intent.putExtra("age", age);
            intent.putExtra("height", height);
            intent.putExtra("carCert", carCert);
            intent.putExtra("finallyTime", finallyTime);
            setResult(111, intent);
            this.finish();
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
        selectAge = (ImageView) findViewById(R.id.selectAge);
        heightText = (TextView) findViewById(R.id.height);
        selectHeight = (ImageView) findViewById(R.id.selectHeight);
        car = (TextView) findViewById(R.id.car);
        selectCar = (ImageView) findViewById(R.id.selectCar);
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


}
