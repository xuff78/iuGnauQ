package com.xj.guanquan.activity.roast;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;

/**
 * Created by 可爱的蘑菇 on 2015/9/1.
 */
public class TucaoDetailAct extends QBaseActivity {

    private int PageType=0;
    private RecyclerView recyclerList;

    @Override
    protected void initHandler() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tucao_item_frg);

        PageType=getIntent().getIntExtra("PageType", 0);
        findViewById(R.id.menuLayout).setVisibility(View.GONE);

        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        initData();
    }

    private void initData() {

        _setHeaderTitle(getResources().getString(R.string.hello_roast_detail));
        _setRightHomeGone();

    }

    @Override
    protected void initView() {

        recyclerList=(RecyclerView)findViewById(R.id.dataList);
    }
}
