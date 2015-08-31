package com.xj.guanquan.activity.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QUserDetailActivity;
import com.xj.guanquan.adapter.MessageAdapter;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.MessageInfo;

import java.util.ArrayList;

import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QMsgDetailActivity extends QBaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<MessageInfo> datalist = new ArrayList<MessageInfo>();
    private EditText msgEdt;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmsg_detail);

        initData();
    }

    private void initData() {
        datalist.add(new MessageInfo("你", "", 0, "", "跟你说个事"));
        datalist.add(new MessageInfo("你", "", 0, "", "我就想问一下你是不是我最好的朋友"));
        datalist.add(new MessageInfo("我", "", 0, "", "不借"));
        datalist.add(new MessageInfo("你", "", 0, "", "我又不是找你借钱，姐又不穷，你就回答是或者不是"));
        ;
        datalist.add(new MessageInfo("我", "", 0, "", "是"));
        datalist.add(new MessageInfo("你", "", 0, "", "刚吃看了部非常感人的电视剧，电视上说狗才是人类最好的朋友，所以我想确认一下"));
        datalist.add(new MessageInfo("我", "", 0, "", "晕倒"));
        adapter = new MessageAdapter(this, datalist);
        mRecyclerView.setAdapter(adapter);
        msgEdt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        msgEdt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        msgEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!msgEdt.getText().toString().trim().equals("")) {
                        datalist.add(new MessageInfo("我", "", 0, "", msgEdt.getText().toString()));
                        adapter.notifyDataSetChanged();
                        msgEdt.setText("");
                        mRecyclerView.scrollToPosition(datalist.size() - 1);
                    } else
                        ToastUtils.show(QMsgDetailActivity.this, "请输入内容");
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }


    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qmsg_detail));
        _setRightHomeGone();
        _setRightHomeText("个人资料", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //UserInfo userInfo = new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会");
                //bundle.putSerializable("userInfo", userInfo);
                toActivity(QUserDetailActivity.class, bundle);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.messageList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        msgEdt = (EditText) findViewById(R.id.msgEdt);

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

}
