package com.xj.guanquan.activity.home;

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
    private TextView age;
    private ImageView selectAge;
    private TextView height;
    private ImageView selectHeight;
    private TextView car;
    private ImageView selectCar;
    private Button confirmBtn;

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

        allSex.setSelected(true);
        oneHour.setSelected(true);

        allSex.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        oneHour.setOnClickListener(this);
        oneDay.setOnClickListener(this);
        moreDay.setOnClickListener(this);
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
        age = (TextView) findViewById(R.id.age);
        selectAge = (ImageView) findViewById(R.id.selectAge);
        height = (TextView) findViewById(R.id.height);
        selectHeight = (ImageView) findViewById(R.id.selectHeight);
        car = (TextView) findViewById(R.id.car);
        selectCar = (ImageView) findViewById(R.id.selectCar);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
    }

    private void setSelectSexView(View v) {
        allSex.setSelected(v == allSex);
        male.setSelected(v == male);
        female.setSelected(v == female);
    }

    private void setSelectTimeView(View v) {
        oneHour.setSelected(v == oneHour);
        oneDay.setSelected(v == oneDay);
        moreDay.setSelected(v == moreDay);
    }


}
