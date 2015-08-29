package com.xj.guanquan.fragment.roast;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.guanquan.R;
import com.xj.guanquan.adapter.TuCaoAdapter;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.model.NoteInfo;

import java.util.ArrayList;

/**
 * Created by 可爱的蘑菇 on 2015/8/23.
 */
public class TucaoListFrg extends QBaseFragment {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private TuCaoAdapter mAdapter;
    private TextView leftBtn, rightBtn;
    private int lastVisibleItem;
    private ArrayList<NoteInfo> notes;
    private RelativeLayout menuLayout;
    private MoveLineView moveHighlight;
    private int PageType =0;

    public static TucaoListFrg newInstance(int PageType) {
        TucaoListFrg fragment = new TucaoListFrg();
        Bundle args = new Bundle();
        args.putInt("PageType", PageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        PageType=getArguments().getInt("PageType", 0);
        View v = inflater.inflate(R.layout.tucao_item_frg, container, false);

        leftBtn = (TextView) v.findViewById(R.id.leftBtnSub);
        leftBtn.setOnClickListener(listener);
        rightBtn = (TextView) v.findViewById(R.id.rightBtnSub);
        rightBtn.setOnClickListener(listener);
        menuLayout=(RelativeLayout) v.findViewById(R.id.menuLayout);
        moveHighlight=new MoveLineView(getActivity(), menuLayout, 2);
        moveHighlight.setPos(1);
        leftBtn.setText("好友吐槽");
        rightBtn.setText("附近吐槽");
        leftBtn.setSelected(true);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.dataList);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        notes = new ArrayList<NoteInfo>();
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg","小白兔", " ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "晚上打算去悠唐商场看一步之遥，有兴趣的赶紧报名啊", "293", "100"));
        mAdapter = new TuCaoAdapter(getActivity(), notes, PageType);


        mRecyclerView.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        mAdapter.isLoadMore(true);
                    }
                }, 1500);
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mAdapter.isLoadMore(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.isLoadMore(false);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

        });

        return v;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftBtnSub:
                    leftBtn.setSelected(true);
                    rightBtn.setSelected(false);
                    moveHighlight.setPos(1);
                    break;
                case R.id.rightBtnSub:
                    leftBtn.setSelected(false);
                    rightBtn.setSelected(true);
                    moveHighlight.setPos(2);
                    break;
            }
        }
    };
}
