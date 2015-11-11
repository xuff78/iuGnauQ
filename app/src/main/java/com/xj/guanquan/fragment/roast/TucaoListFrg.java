package com.xj.guanquan.fragment.roast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.adapter.TuCaoAdapter;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.DateInfo;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

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
    private int PageType = 0;
    private int currentPage = 1;
    private int numPerPage = 20;
    private boolean notNear = true;
    private boolean loadComplete = false;
    int usedItemPos=-1;

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

        PageType = getArguments().getInt("PageType", 0);
        View v = inflater.inflate(R.layout.tucao_item_frg, container, false);

        leftBtn = (TextView) v.findViewById(R.id.leftBtnSub);
        leftBtn.setOnClickListener(listener);
        rightBtn = (TextView) v.findViewById(R.id.rightBtnSub);
        rightBtn.setOnClickListener(listener);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        moveHighlight = new MoveLineView(getActivity(), menuLayout, 2);
        moveHighlight.setPos(1);
        switch (PageType) {
            case 0:
                leftBtn.setText("好友吐槽");
                rightBtn.setText("附近吐槽");
                break;
            case 1:
                leftBtn.setText("好友约会");
                rightBtn.setText("附近约会");
                break;
            case 2:
                leftBtn.setText("好友秘密");
                rightBtn.setText("附近秘密");
                break;
        }
        leftBtn.setSelected(true);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.dataList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();

            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAdapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == mAdapter.getItemCount()) {

                        if (!loadComplete) {
                            mAdapter.isLoadMore(true);
                            currentPage++;
                            doRequest(false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

        });
        initHandler();
        doRequest(true);
        return v;
    }

    public void refreshPage() {
//        mAdapter.isLoadMore(true);
        currentPage = 1;
        doRequest(true);
    }

    public void addCommentNum() {
        if(usedItemPos!=-1){
            int num=Integer.valueOf(notes.get(usedItemPos).getCommentNum())+1;
            notes.get(usedItemPos).setCommentNum(num+"");
            mAdapter.notifyDataSetChanged();
        }
        usedItemPos=-1;
    }

    private int presspos=-1;

    View.OnClickListener listBtnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final int position = (int) v.getTag();
            usedItemPos=position;
            Intent intent = null;
            switch (v.getId()) {
                case R.id.favorBtn:
                    if (notes.get(position).getIsLike() == 0) {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", notes.get(position).getId() + "");
                        if (PageType == QPublishAct.TypeTucao) {
                            startRequest(ApiList.TUCAO_AddLike, params);
                        } else if (PageType == QPublishAct.TypeDate) {
                            startRequest(ApiList.DATE_AddLike, params);
                        } else if (PageType == QPublishAct.TypeSecret) {
                            startRequest(ApiList.SECRET_AddLike, params);
                        }
                        notes.get(position).setIsLike(1);
                        notes.get(position).setLikeNum(notes.get(position).getLikeNum()+1);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if(presspos!=position)
                            ToastUtils.show(getActivity(), "此条已赞");
                    }
                    presspos=position;
                    break;
                case R.id.replyNums:
                    intent = new Intent(getActivity(), QPublishAct.class);
                    intent.putExtra("NoteInfo", notes.get(position));
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestAddComment);
                    startActivityForResult(intent, TucaoMianFrg.toComment);
                    break;
                case R.id.shareBtn:
                    break;
                case R.id.bookBtn:
                    intent = new Intent(getActivity(), QPublishAct.class);
                    intent.putExtra("NoteInfo", notes.get(position));
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestJoin);
//                    startActivityForResult(intent, 888);
                    startActivity(intent);
                    break;
                case R.id.complainBtn:
                    intent = new Intent(getActivity(), QPublishAct.class);
                    intent.putExtra("NoteInfo", notes.get(position));
                    intent.putExtra("PageType", PageType);
                    intent.putExtra("RequestType", QPublishAct.RequestComplain);
//                    startActivityForResult(intent, 888);
                    startActivity(intent);
                    break;
                case R.id.deleteBtn:
                    ((QBaseActivity) getActivity()).alertConfirmDialog("确认删除？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, String> paramsDel = new HashMap<>();
                            if (PageType == QPublishAct.TypeTucao) {
                                startRequest(ApiList.TUCAO_Delete + notes.get(position).getId(), paramsDel);
                            } else if (PageType == QPublishAct.TypeDate) {
                                startRequest(ApiList.DATE_Delete + notes.get(position).getId(), paramsDel);
                            } else if (PageType == QPublishAct.TypeSecret) {
                                startRequest(ApiList.SECRET_Delete + notes.get(position).getId(), paramsDel);
                            }
                        }
                    }, null);

                    break;
            }

        }
    };

    private void doRequest(boolean showDialog) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("currentPage", String.valueOf(currentPage));
        params.put("numPerPage", String.valueOf(numPerPage));
        if (notNear) {
            switch (PageType) {
                case 0:
                    startRequest(ApiList.TUCAO_Friend, params);
                    break;
                case 1:
                    startRequest(ApiList.DATE_Friend, params);
                    break;
                case 2:
                    startRequest(ApiList.SECRET_Friend, params);
                    break;
            }
        } else {
            switch (PageType) {
                case 0:
                    startRequest(ApiList.TUCAO_Nearby, params);
                    break;
                case 1:
                    startRequest(ApiList.DATE_Nearby, params);
                    break;
                case 2:
                    startRequest(ApiList.SECRET_Nearby, params);
                    break;
            }
        }
    }

    @Override
    public void doResponse(Object response) {
        if (getActivity() == null)
            return;
        swipeRefresh.setRefreshing(false);
        if (requestMethod.equals(ApiList.TUCAO_AddLike) || requestMethod.equals(ApiList.DATE_AddLike) || requestMethod.equals(ApiList.SECRET_AddLike)) {
//            ToastUtils.show(getActivity(), "赞一个");
        } else if (requestMethod.startsWith(ApiList.TUCAO_Delete) || requestMethod.startsWith(ApiList.DATE_Delete) || requestMethod.startsWith(ApiList.SECRET_Delete)) {
            ToastUtils.show(getActivity(), "删除成功");
            currentPage = 1;
            doRequest(true);
        } else {
            ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
            PageInfo pageInfo = JSONObject.parseObject(result.getData().getJSONObject("page").toJSONString(), PageInfo.class);
//        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            if (result.getData().getJSONArray("content") != null) {
                List<NoteInfo> resultData = new ArrayList<>();
                if (requestMethod.equals(ApiList.TUCAO_Friend) || requestMethod.equals(ApiList.TUCAO_Nearby)) {
                    resultData = JSONArray.parseArray(result.getData().getJSONArray("content").toJSONString(), NoteInfo.class);
                } else if (requestMethod.equals(ApiList.DATE_Friend) || requestMethod.equals(ApiList.DATE_Nearby)) {
                    JSONArray array = result.getData().getJSONArray("content");
                    for (int i = 0; i < array.size(); i++) {
                        NoteInfo date = JSONObject.parseObject(array.get(i).toString(), DateInfo.class);
                        resultData.add(date);
                    }
                } else if (requestMethod.equals(ApiList.SECRET_Friend) || requestMethod.equals(ApiList.SECRET_Nearby)) {
                    resultData = JSONArray.parseArray(result.getData().getJSONArray("content").toJSONString(), NoteInfo.class);
                }
                if (currentPage == 1) {
                    swipeRefresh.setRefreshing(false);
                    notes = new ArrayList<NoteInfo>();
                    notes.addAll(resultData);
                    mAdapter = new TuCaoAdapter(getActivity(), notes, PageType, listBtnListener);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    notes.addAll(resultData);
                    mAdapter.notifyDataSetChanged();
                }
                if (pageInfo.getCurrentPage() < pageInfo.getTotalPage()) {
                    mAdapter.isLoadMore(true);
                    loadComplete = true;
                } else
                    mAdapter.isLoadMore(false);
            }
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftBtnSub:
                    if (!notNear) {
                        leftBtn.setSelected(true);
                        rightBtn.setSelected(false);
                        moveHighlight.setPos(1);
                        notNear = true;
                        currentPage = 1;
                        doRequest(false);
                    }
                    break;
                case R.id.rightBtnSub:
                    if (notNear) {
                        leftBtn.setSelected(false);
                        rightBtn.setSelected(true);
                        moveHighlight.setPos(2);
                        notNear = false;
                        currentPage = 1;
                        doRequest(false);
                    }
                    break;
            }
        }
    };

    private void initHandler() {
    }

    private void startRequest(String method, final Map<String, String> mapparams) {
        StringRequest requestForList = new StringRequest(Request.Method.POST, method, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(getActivity(), "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = mapparams;
                map.put("lng", PreferencesUtils.getString(getActivity(), "lng"));
                map.put("lat", PreferencesUtils.getString(getActivity(), "lat"));
                return map;
            }
        };
        addToRequestQueue(requestForList, method, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888) {
            refreshPage();
        }else if(requestCode==TucaoMianFrg.toComment&&resultCode==TucaoMianFrg.publishSuccess){
            addCommentNum();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
