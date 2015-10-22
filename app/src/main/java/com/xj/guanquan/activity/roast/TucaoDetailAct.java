package com.xj.guanquan.activity.roast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.adapter.TucaoCommentAdapter;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.TucaoCommentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/9/1.
 */
public class TucaoDetailAct extends QBaseActivity {

    private int PageType = 0;
    private RecyclerView recyclerList;
    private int currentPage = 1;
    private int numPerPage = 20;
    ;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private TucaoCommentAdapter mAdapter;
    private int lastVisibleItem;
    private StringRequest request;
    private NoteInfo note;
    private ArrayList<TucaoCommentInfo> comments;

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.TUCAO_Detail + note.getId(), this, this) {
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
                map.put("lng", PreferencesUtils.getString(TucaoDetailAct.this, "lng"));
                map.put("lat", PreferencesUtils.getString(TucaoDetailAct.this, "lat"));
                return map;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        note = (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
        setContentView(R.layout.tucao_item_frg);

        PageType = getIntent().getIntExtra("PageType", 0);
        findViewById(R.id.menuLayout).setVisibility(View.GONE);

        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        initData();
        getCommentList();
    }

    private void getCommentList(){
        Map<String, String> params = new HashMap<>();
        if (PageType == QPublishAct.TypeTucao) {
            startRequest(ApiList.TUCAO_Detail + note.getId(), params);
        } else if (PageType == QPublishAct.TypeDate) {
            startRequest(ApiList.DATE_Detail + note.getId(), params);
        } else if (PageType == QPublishAct.TypeSecret) {
            startRequest(ApiList.SECRET_Detail + note.getId(), params);
        }
    }

    private void initData() {

        _setHeaderTitle(getResources().getString(R.string.hello_roast_detail));
        _setRightHomeGone();

    }

    @Override
    protected void initView() {

        recyclerList = (RecyclerView) findViewById(R.id.dataList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
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
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentList();
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.favorBtn:
                    if (note.getIsLike() == 0) {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", note.getId() + "");
                        if (PageType == QPublishAct.TypeTucao) {
                            startRequest(ApiList.TUCAO_AddLike, params);
                        } else if (PageType == QPublishAct.TypeDate) {
                            startRequest(ApiList.DATE_AddLike, params);
                        } else if (PageType == QPublishAct.TypeSecret) {
                            startRequest(ApiList.SECRET_AddLike, params);
                        }
                        note.setIsLike(1);
                        ((TextView)v).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.zan2), null, null, null);
                    } else {
                        ToastUtils.show(TucaoDetailAct.this, "此条已赞");
                    }
                    break;
                case R.id.replyNums:
                    intent = new Intent(TucaoDetailAct.this, QPublishAct.class);
                    intent.putExtra("NoteInfo", note);
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestAddComment);
                    startActivityForResult(intent, 888);
                    break;
                case R.id.shareBtn:
                    break;
                case R.id.bookBtn:
                    intent = new Intent(TucaoDetailAct.this, QPublishAct.class);
                    intent.putExtra("NoteInfo", note);
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestJoin);
//                    startActivityForResult(intent, 888);
                    startActivity(intent);
                    break;
                case R.id.complainBtn:
                    intent = new Intent(TucaoDetailAct.this, QPublishAct.class);
                    intent.putExtra("NoteInfo", note);
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestComplain);
//                    startActivityForResult(intent, 888);
                    startActivity(intent);
                    break;
                case R.id.deleteBtn:
                    alertConfirmDialog("确认删除？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, String> paramsDel = new HashMap<>();
                            if (PageType == QPublishAct.TypeTucao) {
                                startRequest(ApiList.TUCAO_Delete + note.getId(), paramsDel);
                            } else if (PageType == QPublishAct.TypeDate) {
                                startRequest(ApiList.DATE_Delete + note.getId(), paramsDel);
                            } else if (PageType == QPublishAct.TypeSecret) {
                                startRequest(ApiList.SECRET_Delete + note.getId(), paramsDel);
                            }
                        }
                    }, null);

                    break;
            }

        }
    };

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        swipeRefresh.setRefreshing(false);
        if (requestMethod.startsWith(ApiList.TUCAO_Delete) || requestMethod.startsWith(ApiList.DATE_Delete) || requestMethod.startsWith(ApiList.SECRET_Delete)) {
            ToastUtils.show(this, "删除成功");
            finish();
        }else {
            ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
            if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
                JSONObject obj = result.getData().getJSONObject("content");
                if (obj != null && obj.getJSONArray("commentList") != null) {
                    List<TucaoCommentInfo> resultData = JSONArray.parseArray(obj.getJSONArray("commentList").toJSONString(), TucaoCommentInfo.class);
                    if (currentPage == 1) {
                        swipeRefresh.setRefreshing(false);
                        comments = new ArrayList<TucaoCommentInfo>();
                        comments.addAll(resultData);
                        mAdapter = new TucaoCommentAdapter(this, comments, note, PageType, listener);
                        recyclerList.setAdapter(mAdapter);
                    } else {
                        comments.addAll(resultData);
                        mAdapter.isLoadMore(false);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.isLoadMore(false);
                }else{
                    swipeRefresh.setRefreshing(false);
                    comments = new ArrayList<TucaoCommentInfo>();
                    mAdapter = new TucaoCommentAdapter(this, comments, note, PageType, listener);
                    recyclerList.setAdapter(mAdapter);
                }
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }

    private void startRequest(String method, final Map<String, String> mapparams) {
        StringRequest requestForList = new StringRequest(Request.Method.POST, method, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(TucaoDetailAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = mapparams;
                map.put("lng", PreferencesUtils.getString(TucaoDetailAct.this, "lng"));
                map.put("lat", PreferencesUtils.getString(TucaoDetailAct.this, "lat"));
                return map;
            }
        };
        addToRequestQueue(requestForList, method, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888) {
            getCommentList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
