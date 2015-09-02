package com.xj.guanquan.activity.roast;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.adapter.TuCaoAdapter;
import com.xj.guanquan.adapter.TucaoCommentAdapter;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.PageInfo;
import com.xj.guanquan.model.TucaoCommentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

/**
 * Created by 可爱的蘑菇 on 2015/9/1.
 */
public class TucaoDetailAct extends QBaseActivity {

    private int PageType=0;
    private RecyclerView recyclerList;
    private int currentPage = 1;
    private int numPerPage = 20;;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private TucaoCommentAdapter mAdapter;
    private int lastVisibleItem;
    private StringRequest request;
    private NoteInfo note;
    private ArrayList<TucaoCommentInfo> comments;

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.TUCAO_Detail+note.getId(), this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(TucaoDetailAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("currentPage", String.valueOf(currentPage));
                map.put("numPerPage", String.valueOf(numPerPage));
                return map;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        note= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
        setContentView(R.layout.tucao_item_frg);

        PageType=getIntent().getIntExtra("PageType", 0);
        findViewById(R.id.menuLayout).setVisibility(View.GONE);

        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        initData();
        addToRequestQueue(request, true);
    }

    private void initData() {

        _setHeaderTitle(getResources().getString(R.string.hello_roast_detail));
        _setRightHomeGone();

    }

    @Override
    protected void initView() {

        recyclerList=(RecyclerView)findViewById(R.id.dataList);
        mLayoutManager=new LinearLayoutManager(this);
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        recyclerList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                    mAdapter.isLoadMore(true);
//                    currentPage++;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

        });
    }

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        swipeRefresh.setRefreshing(false);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
//        PageInfo pageInfo = JSONObject.parseObject(result.getData().getJSONObject("page").toJSONString(), PageInfo.class);
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            JSONObject obj=result.getData().getJSONObject("content");
            if (obj.getJSONArray("commentList") != null) {
                List<TucaoCommentInfo> resultData = JSONArray.parseArray(obj.getJSONArray("commentList").toJSONString(), TucaoCommentInfo.class);
                if (currentPage == 1) {
                    swipeRefresh.setRefreshing(false);
                    comments = new ArrayList<TucaoCommentInfo>();
                    comments.addAll(resultData);
                    mAdapter=new TucaoCommentAdapter(this, comments, note, PageType);
                    recyclerList.setAdapter(mAdapter);
                } else {
                    comments.addAll(resultData);
                    mAdapter.isLoadMore(false);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.isLoadMore(false);
//                if (pageInfo.getCurrentPage() < pageInfo.getTotalPage()) {
//                    mAdapter.isLoadMore(true);
//                }
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }
}
